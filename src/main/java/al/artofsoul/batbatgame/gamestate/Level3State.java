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
import al.artofsoul.batbatgame.entity.enemies.Ufo;
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

public class Level3State extends GameState {

	private Background temple;

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
	private boolean eventQuake;

	public Level3State(GameStateManager gsm) {
		super(gsm);
		init();
	}

	@Override
	public void init() {

		// backgrounds
		temple = new Background("/Backgrounds/temple.gif", 0.5, 0);

		// tilemap
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/ruinstileset.gif");
		tileMap.loadMap("/Maps/level3.map");
		tileMap.setPosition(140, 0);
		tileMap.setTween(1);

		// player
		player = new Player(tileMap);
		player.setPosition(300, 131);
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
		teleport.setPosition(2850, 371);

		// start event
		eventStart = true;
		tb = new ArrayList<>();
		eventStart();

		// sfx
		JukeBox.load("/SFX/teleport.mp3", "teleport");
		JukeBox.load("/SFX/explode.mp3", "explode");
		JukeBox.load("/SFX/enemyhit.mp3", "enemyhit");

		// music
		JukeBox.load("/Music/level1v2.mp3", "level2");
		JukeBox.loop("level2", 600, JukeBox.getFrames("level2") - 2200);

	}

	private void populateEnemies() {
		enemies.clear();
		XhelBat gp;
		Zogu g;
		Ufo t;

		gp = new XhelBat(tileMap, player);
		gp.setPosition(750, 100);
		enemies.add(gp);
		gp = new XhelBat(tileMap, player);
		gp.setPosition(900, 150);
		enemies.add(gp);
		gp = new XhelBat(tileMap, player);
		gp.setPosition(1320, 250);
		enemies.add(gp);
		gp = new XhelBat(tileMap, player);
		gp.setPosition(1570, 160);
		enemies.add(gp);
		gp = new XhelBat(tileMap, player);
		gp.setPosition(1590, 160);
		enemies.add(gp);
		gp = new XhelBat(tileMap, player);
		gp.setPosition(2600, 370);
		enemies.add(gp);
		gp = new XhelBat(tileMap, player);
		gp.setPosition(2620, 370);
		enemies.add(gp);
		gp = new XhelBat(tileMap, player);
		gp.setPosition(2640, 370);
		enemies.add(gp);

		g = new Zogu(tileMap);
		g.setPosition(904, 130);
		enemies.add(g);
		g = new Zogu(tileMap);
		g.setPosition(1080, 270);
		enemies.add(g);
		g = new Zogu(tileMap);
		g.setPosition(1200, 270);
		enemies.add(g);
		g = new Zogu(tileMap);
		g.setPosition(1704, 300);
		enemies.add(g);

		t = new Ufo(tileMap, player, enemies);
		t.setPosition(1900, 580);
		enemies.add(t);
		t = new Ufo(tileMap, player, enemies);
		t.setPosition(2330, 550);
		enemies.add(t);
		t = new Ufo(tileMap, player, enemies);
		t.setPosition(2400, 490);
		enemies.add(t);
		t = new Ufo(tileMap, player, enemies);
		t.setPosition(2457, 430);
		enemies.add(t);

	}

	@Override
	public void update() {

		// check keys
		handleInput();

		// check if quake event should start
		if (player.getx() > 2175 && !tileMap.isShaking()) {
			eventQuake = blockInput = true;
		}

		// check if end of level event should start
		if (teleport.contains(player)) {
			eventFinish = blockInput = true;
		}

		// play events
		if (eventStart)
			eventStart();
		if (eventDead)
			eventDead();
		if (eventQuake)
			eventQuake();
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
		temple.setPosition(tileMap.getx(), tileMap.gety());

		// update player
		player.update();
		if (player.getHealth() == 0 || player.gety() > tileMap.getHeight()) {
			eventDead = blockInput = true;
		}

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
		temple.draw(g);

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
		player.loseLife();
		player.reset();
		player.setPosition(300, 131);
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
		if (eventCount == 1)
			player.setDead();
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
				reset();
			}
		}
	}

	// earthquake
	private void eventQuake() {
		eventCount++;
		if (eventCount == 1) {
			player.stop();
			player.setPosition(2175, player.gety());
		}
		if (eventCount == 60) {
			player.setEmote(Player.CONFUSED);
		}
		if (eventCount == 120)
			player.setEmote(Player.NONE);
		if (eventCount == 150)
			tileMap.setShaking(true, 10);
		if (eventCount == 180)
			player.setEmote(Player.SURPRISED);
		if (eventCount == 300) {
			player.setEmote(Player.NONE);
			eventQuake = blockInput = false;
			eventCount = 0;
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
			gsm.setState(GameStateManager.LEVEL2STATE);
		}

	}

}