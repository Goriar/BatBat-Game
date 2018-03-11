package al.artofsoul.batbatgame.gamestate;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import al.artofsoul.batbatgame.audio.JukeBox;
import al.artofsoul.batbatgame.entity.Enemy;
import al.artofsoul.batbatgame.entity.EnemyProjectile;
import al.artofsoul.batbatgame.entity.EnergyParticle;
import al.artofsoul.batbatgame.entity.Explosion;
import al.artofsoul.batbatgame.entity.HUD;
import al.artofsoul.batbatgame.entity.Player;
import al.artofsoul.batbatgame.entity.PlayerSave;
import al.artofsoul.batbatgame.entity.Teleport;
import al.artofsoul.batbatgame.entity.Title;
import al.artofsoul.batbatgame.entity.enemies.XhelBat;
import al.artofsoul.batbatgame.entity.enemies.Zogu;
import al.artofsoul.batbatgame.handlers.Keys;
import al.artofsoul.batbatgame.handlers.LoggingHelper;
import al.artofsoul.batbatgame.main.GamePanel;
import al.artofsoul.batbatgame.tilemap.Background;
import al.artofsoul.batbatgame.tilemap.TileMap;

/**
 * @author ArtOfSoul
 */

public class Level2State extends GameState {

	private Background perendimi;
	private Background mountains;

	private Player player;
	private TileMap tileMap;
	private ArrayList<Enemy> enemies;
	private ArrayList<EnemyProjectile> eprojectiles;
	private ArrayList<Explosion> explosions;

	private HUD hud;
	private BufferedImage batBatStart;
	private Title title;
	private Title subtitle;
	private Teleport teleport;

	// events
	private boolean blockInput = false;
	private int eventCount = 0;
	private boolean eventStart;
	private ArrayList<Rectangle> tb;
	private boolean eventFinish;
	private boolean eventDead;

	public Level2State(GameStateManager gsm) {
		super(gsm);
		init();
	}

	@Override
	public void init() {

		// backgrounds
		perendimi = new Background("/Backgrounds/perendimi.gif", 0.5, 0);
		mountains = new Background("/Backgrounds/mali2.gif", 0.2);

		// tilemap
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/ruinstileset.gif");
		tileMap.loadMap("/Maps/level2.map");
		tileMap.setPosition(140, 0);
		tileMap.setBounds(tileMap.getWidth() - 1 * tileMap.getTileSize(),
				tileMap.getHeight() - 2 * tileMap.getTileSize(), 0, 0);
		tileMap.setTween(1);

		// player
		player = new Player(tileMap);
		player.setPosition(300, 161);
		player.setHealth(PlayerSave.getHealth());
		player.setLives(PlayerSave.getLives());
		player.setTime(PlayerSave.getTime());

		// enemies
		enemies = new ArrayList<>();
		eprojectiles = new ArrayList<>();
		populateEnemies();

		// energy particle
		ArrayList<EnergyParticle> energyParticles;
		energyParticles = new ArrayList<>();

		// init player
		player.init(enemies, energyParticles);

		// explosions
		explosions = new ArrayList<>();

		// hud
		hud = new HUD(player);

		// title and subtitle
		try {
			batBatStart = ImageIO.read(getClass().getResourceAsStream("/HUD/batbat.gif"));
			title = new Title(batBatStart.getSubimage(0, 0, 178, 20));
			title.sety(60);
			subtitle = new Title(batBatStart.getSubimage(0, 33, 91, 13));
			subtitle.sety(85);
		} catch (Exception e) {
			LoggingHelper.LOGGER.log(Level.SEVERE, e.getMessage());
		}

		// teleport
		teleport = new Teleport(tileMap);
		teleport.setPosition(3700, 131);

		// start event
		eventStart = true;
		tb = new ArrayList<>();
		eventStart();

		// sfx
		JukeBox.load("/SFX/teleport.mp3", "teleport");
		JukeBox.load("/SFX/explode.mp3", "explode");
		JukeBox.load("/SFX/enemyhit.mp3", "enemyhit");

		// music
		JukeBox.load("/Music/level1.mp3", "level1");
		JukeBox.loop("level1", 600, JukeBox.getFrames("level1") - 2200);

	}

	private void populateEnemies() {
		enemies.clear();

		XhelBat gp;
		Zogu g;

		gp = new XhelBat(tileMap, player);
		gp.setPosition(1300, 100);
		enemies.add(gp);
		gp = new XhelBat(tileMap, player);
		gp.setPosition(1320, 100);
		enemies.add(gp);
		gp = new XhelBat(tileMap, player);
		gp.setPosition(1340, 100);
		enemies.add(gp);
		gp = new XhelBat(tileMap, player);
		gp.setPosition(1660, 100);
		enemies.add(gp);
		gp = new XhelBat(tileMap, player);
		gp.setPosition(1680, 100);
		enemies.add(gp);
		gp = new XhelBat(tileMap, player);
		gp.setPosition(1700, 100);
		enemies.add(gp);
		gp = new XhelBat(tileMap, player);
		gp.setPosition(2177, 100);
		enemies.add(gp);
		gp = new XhelBat(tileMap, player);
		gp.setPosition(2960, 100);
		enemies.add(gp);
		gp = new XhelBat(tileMap, player);
		gp.setPosition(2980, 100);
		enemies.add(gp);
		gp = new XhelBat(tileMap, player);
		gp.setPosition(3000, 100);
		enemies.add(gp);

		g = new Zogu(tileMap);
		g.setPosition(2600, 100);
		enemies.add(g);
		g = new Zogu(tileMap);
		g.setPosition(3500, 100);
		enemies.add(g);
	}

	@Override
	public void update() {

		// check keys
		handleInput();

		// check if end of level event should start
		if (teleport.contains(player)) {
			eventFinish = blockInput = true;
		}

		// check if player dead event should start
		if (player.getHealth() == 0 || player.gety() > tileMap.getHeight()) {
			eventDead = blockInput = true;
		}

		// play events
		if (eventStart)
			eventStart();
		if (eventDead)
			eventDead();
		if (eventFinish)
			eventFinish();

		// move title and subtitle
		if (title != null) {
			title.update();
			if (title.shouldRemove())
				title = null;
		}
		if (subtitle != null) {
			subtitle.update();
			if (subtitle.shouldRemove())
				subtitle = null;
		}

		// move backgrounds
		perendimi.setPosition(tileMap.getx(), tileMap.gety());
		mountains.setPosition(tileMap.getx(), tileMap.gety());

		// update player
		player.update();

		// update tilemap
		tileMap.setPosition(GamePanel.WIDTH / 2.0 - player.getx(), GamePanel.HEIGHT / 2.0 - player.gety());
		tileMap.update();
		tileMap.fixBounds();

		handleObjects(tileMap, enemies, eprojectiles, explosions);

		// update teleport
		teleport.update();

	}

	@Override
	public void draw(Graphics2D g) {

		// draw background

		perendimi.draw(g);
		mountains.draw(g);

		// draw tilemap
		tileMap.draw(g);

		// draw enemies
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}

		// draw enemy projectiles
		for (int i = 0; i < eprojectiles.size(); i++) {
			eprojectiles.get(i).draw(g);
		}

		// draw explosions
		for (int i = 0; i < explosions.size(); i++) {
			explosions.get(i).draw(g);
		}

		// draw player
		player.draw(g);

		// draw teleport
		teleport.draw(g);

		// draw hud
		hud.draw(g);

		// draw title
		if (title != null)
			title.draw(g);
		if (subtitle != null)
			subtitle.draw(g);

		// draw transition boxes
		g.setColor(java.awt.Color.BLACK);
		for (int i = 0; i < tb.size(); i++) {
			g.fill(tb.get(i));
		}

	}

	@Override
	public void handleInput() {
		if (Keys.isPressed(Keys.ESCAPE))
			gsm.setPaused(true);
		if (blockInput || player.getHealth() == 0)
			return;
		player.setUp(Keys.getKeyState()[Keys.UP]);
		player.setLeft(Keys.getKeyState()[Keys.LEFT]);
		player.setDown(Keys.getKeyState()[Keys.DOWN]);
		player.setRight(Keys.getKeyState()[Keys.RIGHT]);
		player.setJumping(Keys.getKeyState()[Keys.BUTTON1]);
		player.setDashing(Keys.getKeyState()[Keys.BUTTON2]);
		if (Keys.isPressed(Keys.BUTTON3))
			player.setAttacking();
		if (Keys.isPressed(Keys.BUTTON4))
			player.setCharging();
	}

	///////////////////////////////////////////////////////
	//////////////////// EVENTS
	///////////////////////////////////////////////////////

	// reset level
	private void reset() {
		player.reset();
		player.setPosition(300, 161);
		populateEnemies();
		blockInput = true;
		eventCount = 0;
		tileMap.setShaking(false, 0);
		eventStart = true;
		eventStart();
		title = new Title(batBatStart.getSubimage(0, 0, 178, 20));
		title.sety(60);
		subtitle = new Title(batBatStart.getSubimage(0, 33, 91, 13));
		subtitle.sety(85);
	}

	// level started
	private void eventStart() {
		eventCount++;
		if (eventCount == 1) {
			tb.clear();
			tb.add(new Rectangle(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT / 2));
			tb.add(new Rectangle(0, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT));
			tb.add(new Rectangle(0, GamePanel.HEIGHT / 2, GamePanel.WIDTH, GamePanel.HEIGHT / 2));
			tb.add(new Rectangle(GamePanel.WIDTH / 2, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT));
		}
		if (eventCount > 1 && eventCount < 60) {
			tb.get(0).height -= 4;
			tb.get(1).width -= 6;
			tb.get(2).y += 4;
			tb.get(3).x += 6;
		}
		if (eventCount == 30)
			title.begin();
		if (eventCount == 60) {
			eventStart = blockInput = false;
			eventCount = 0;
			subtitle.begin();
			tb.clear();
		}
	}

	// player has died
	private void eventDead() {
		eventCount++;
		if (eventCount == 1) {
			player.setDead();
			player.stop();
		}
		if (eventCount == 60) {
			tb.clear();
			tb.add(new Rectangle(GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2, 0, 0));
		} else if (eventCount > 60) {
			tb.get(0).x -= 6;
			tb.get(0).y -= 4;
			tb.get(0).width += 12;
			tb.get(0).height += 8;
		}
		if (eventCount >= 120) {
			if (player.getLives() == 0) {
				gsm.setState(GameStateManager.MENUSTATE);
			} else {
				eventDead = blockInput = false;
				eventCount = 0;
				player.loseLife();
				reset();
			}
		}
	}

	// finished level
	private void eventFinish() {
		eventCount++;
		if (eventCount == 1) {
			JukeBox.play("teleport");
			player.setTeleporting(true);
			player.stop();
		} else if (eventCount == 120) {
			tb.clear();
			tb.add(new Rectangle(GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2, 0, 0));
		} else if (eventCount > 120) {
			tb.get(0).x -= 6;
			tb.get(0).y -= 4;
			tb.get(0).width += 12;
			tb.get(0).height += 8;
			JukeBox.stop("teleport");
		}
		if (eventCount == 180) {
			PlayerSave.setHealth(player.getHealth());
			PlayerSave.setLives(player.getLives());
			PlayerSave.setTime(player.getTime());
			gsm.setState(GameStateManager.LEVEL4STATE);
		}

	}

}