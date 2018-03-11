package al.artofsoul.batbatgame.gamestate;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import al.artofsoul.batbatgame.audio.JukeBox;
import al.artofsoul.batbatgame.entity.Enemy;
import al.artofsoul.batbatgame.entity.Enemy.EnemyType;
import al.artofsoul.batbatgame.entity.EnemyProjectile;
import al.artofsoul.batbatgame.entity.EnergyParticle;
import al.artofsoul.batbatgame.entity.Explosion;
import al.artofsoul.batbatgame.entity.HUD;
import al.artofsoul.batbatgame.entity.Player;
import al.artofsoul.batbatgame.entity.PlayerSave;
import al.artofsoul.batbatgame.entity.Portal;
import al.artofsoul.batbatgame.entity.Spirit;
import al.artofsoul.batbatgame.entity.Teleport;
import al.artofsoul.batbatgame.entity.Title;
import al.artofsoul.batbatgame.entity.enemies.RedEnergy;
import al.artofsoul.batbatgame.entity.enemies.Ufo;
import al.artofsoul.batbatgame.entity.enemies.XhelBat;
import al.artofsoul.batbatgame.entity.enemies.Zogu;
import al.artofsoul.batbatgame.handlers.LoggingHelper;
import al.artofsoul.batbatgame.tilemap.TileMap;

/**
 * @author ArtOfSoul
 *
 */

public abstract class GameState {

	protected GameStateManager gsm;

	protected Player player;
	protected TileMap tileMap;
	protected ArrayList<Enemy> enemies;
	protected ArrayList<EnemyProjectile> eprojectiles;
	protected ArrayList<Explosion> explosions;

	protected HUD hud;
	protected BufferedImage batBatStart;
	protected Title title;
	protected Title subtitle;
	protected Teleport teleport;

	// events
	protected boolean blockInput = false;
	protected int eventCount = 0;
	protected boolean eventStart;
	protected ArrayList<Rectangle> tb;
	protected boolean eventFinish;
	protected boolean eventDead;

	protected Portal portal;

	protected EnemyType[] enemyTypesInLevel;
	protected int[][] coords;

	public GameState(GameStateManager gsm) {
		this.gsm = gsm;
	}

	public abstract void init();

	public abstract void update();

	public abstract void draw(Graphics2D g);

	public abstract void handleInput();

	protected abstract void eventStart();

	protected void handleObjects(TileMap tileMap, List<Enemy> enemies, List<EnemyProjectile> eprojectiles,
			List<Explosion> explosions) {
		ArrayList<Enemy> enemiesToRemove = new ArrayList<>();
		// update enemies
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update();
			if (e.isDead()) {
				enemiesToRemove.add(e);
				explosions.add(new Explosion(tileMap, e.getx(), e.gety()));
			}
		}

		for (Enemy enemy : enemiesToRemove) {
			enemies.remove(enemy);
		}

		ArrayList<EnemyProjectile> projectilesToRemove = new ArrayList<>();
		// update enemy projectiles
		for (int i = 0; i < eprojectiles.size(); i++) {
			EnemyProjectile ep = eprojectiles.get(i);
			ep.update();
			if (ep.shouldRemove()) {
				projectilesToRemove.add(ep);
			}
		}

		for (EnemyProjectile enemyProjectile : projectilesToRemove) {
			eprojectiles.remove(enemyProjectile);
		}

		ArrayList<Explosion> explosionsToRemove = new ArrayList<>();
		// update explosions
		for (int i = 0; i < explosions.size(); i++) {
			explosions.get(i).update();
			if (explosions.get(i).shouldRemove()) {
				explosionsToRemove.add(explosions.get(i));
			}
		}

		for (Explosion explosion : explosionsToRemove) {
			explosions.remove(explosion);
		}
	}

	protected void generateTileMap(String map, int x, int y, boolean bounds) {
		// tilemap
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/ruinstileset.gif");
		tileMap.loadMap(map);
		tileMap.setPosition(x, y);
		if (bounds)
			tileMap.setBounds(tileMap.getWidth() - 1 * tileMap.getTileSize(),
					tileMap.getHeight() - 2 * tileMap.getTileSize(), 0, 0);
		tileMap.setTween(1);
	}

	protected void setupGameObjects(int playerX, int playerY, int goalX, int goalY, boolean portal) {
		// player
		player = new Player(tileMap);
		player.setPosition(playerX, playerY);
		player.setHealth(PlayerSave.getHealth());
		player.setLives(PlayerSave.getLives());
		player.setTime(PlayerSave.getTime());

		// enemies
		enemies = new ArrayList<>();
		eprojectiles = new ArrayList<>();

		// energy particle
		ArrayList<EnergyParticle> energyParticles;
		energyParticles = new ArrayList<>();

		// init player
		player.init(enemies, energyParticles);

		// explosions
		explosions = new ArrayList<>();

		// hud
		hud = new HUD(player);

		// teleport
		if (!portal) {
			teleport = new Teleport(tileMap);
			teleport.setPosition(goalX, goalY);
		} else {
			this.portal = new Portal(tileMap);
			this.portal.setPosition(goalX, goalY);
		}

		// start event
		eventStart = true;
		tb = new ArrayList<>();
		eventStart();
	}

	protected void setupMusic(String level, String bgMusic, boolean loop) {
		// sfx
		JukeBox.load("/SFX/teleport.mp3", "teleport");
		JukeBox.load("/SFX/explode.mp3", "explode");
		JukeBox.load("/SFX/enemyhit.mp3", "enemyhit");

		// music
		JukeBox.load(bgMusic, level);
		if (loop)
			JukeBox.loop(level, 600, JukeBox.getFrames(level) - 2200);
	}

	protected void setupTitle(int[] titleCoords, int[] subtitleCoords) {
		// title and subtitle
		try {
			batBatStart = ImageIO.read(getClass().getResourceAsStream("/HUD/batbat.gif"));
			this.title = new Title(
					batBatStart.getSubimage(titleCoords[0], titleCoords[1], titleCoords[2], titleCoords[3]));
			this.title.sety(60);
			this.subtitle = new Title(batBatStart.getSubimage(subtitleCoords[0], subtitleCoords[1], subtitleCoords[2],
					subtitleCoords[3]));
			this.subtitle.sety(85);
		} catch (Exception e) {
			LoggingHelper.LOGGER.log(Level.SEVERE, e.getMessage());
		}
	}

	protected void populateEnemies(EnemyType[] enemies, int[][] coords) {
		this.enemies.clear();
		for (int i = 0; i < enemies.length; i++) {
			Enemy e = null;
			switch (enemies[i]) {
			case RED_ENERGY:
				e = new RedEnergy(this.tileMap);
				break;
			case UFO:
				e = new Ufo(this.tileMap, this.player, this.enemies);
				break;
			case XHELBAT:
				e = new XhelBat(this.tileMap, this.player);
				break;
			case SPIRIT:
				e = new Spirit(this.tileMap, this.player, this.enemies, this.explosions);
			default:
				e = new Zogu(this.tileMap);
				break;
			}

			e.setPosition(coords[i][0], coords[i][1]);
			this.enemies.add(e);
		}
	}
}
