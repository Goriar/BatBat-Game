package al.artofsoul.batbatgame.gamestate;

import java.awt.Rectangle;

import al.artofsoul.batbatgame.audio.JukeBox;
import al.artofsoul.batbatgame.entity.Enemy.EnemyType;
import al.artofsoul.batbatgame.entity.Player;
import al.artofsoul.batbatgame.entity.PlayerSave;
import al.artofsoul.batbatgame.entity.Title;
import al.artofsoul.batbatgame.handlers.Keys;
import al.artofsoul.batbatgame.main.GamePanel;
import al.artofsoul.batbatgame.tilemap.Background;

/**
 * @author ArtOfSoul
 */

public class Level3State extends GameState {

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
		generateTileMap("/Maps/level3.map", 140, 0, false);

		setupGameObjects(300, 131, 2850, 371, false);
		setupTitle(new int[] { 0, 0, 178, 20 }, new int[] { 0, 33, 91, 13 });
		setupMusic("level2", "/Music/level1v2.mp3", true);

		enemyTypesInLevel = new EnemyType[] { EnemyType.XHELBAT, EnemyType.XHELBAT, EnemyType.XHELBAT,
				EnemyType.XHELBAT, EnemyType.XHELBAT, EnemyType.XHELBAT, EnemyType.XHELBAT, EnemyType.XHELBAT,
				EnemyType.ZOGU, EnemyType.ZOGU, EnemyType.ZOGU, EnemyType.ZOGU, EnemyType.UFO, EnemyType.UFO,
				EnemyType.UFO, EnemyType.UFO };
		coords = new int[][] { new int[] { 750, 100 }, new int[] { 900, 150 }, new int[] { 1320, 250 },
				new int[] { 1570, 160 }, new int[] { 1590, 160 }, new int[] { 2600, 370 }, new int[] { 2620, 370 },
				new int[] { 2640, 370 }, new int[] { 904, 130 }, new int[] { 1080, 270 }, new int[] { 1200, 270 },
				new int[] { 1704, 300 }, new int[] { 1900, 580 }, new int[] { 2330, 550 }, new int[] { 2400, 490 },
				new int[] { 2457, 430 } };

		populateEnemies(enemyTypesInLevel, coords);
	}

	@Override
	public void update() {

		super.update();

		// check if quake event should start
		if (player.getx() > 2175 && !tileMap.isShaking()) {
			eventQuake = blockInput = true;
		}

		if (eventQuake)
			eventQuake();

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
		populateEnemies(enemyTypesInLevel, coords);
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
	@Override
	protected void eventStart() {
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
	@Override
	protected void eventDead() {
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
	@Override
	protected void eventFinish() {
		JukeBox.stop("level2");
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
