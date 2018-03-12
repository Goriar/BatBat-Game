package al.artofsoul.batbatgame.gamestate;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import al.artofsoul.batbatgame.entity.Enemy.EnemyType;
import al.artofsoul.batbatgame.handlers.Keys;
import al.artofsoul.batbatgame.handlers.LoggingHelper;
import al.artofsoul.batbatgame.main.GamePanel;

/**
 * @author ArtOfSoul
 *
 */

public class AcidState extends GameState {

	private float hue;
	private Color color;

	private double angle;
	private BufferedImage image;

	public AcidState(GameStateManager gsm) {
		super(gsm);
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/PlayerSprites.gif")).getSubimage(0, 0,
					40, 40);
		} catch (Exception e) {
			LoggingHelper.LOGGER.log(Level.SEVERE, e.getMessage());
		}
	}

	@Override
	public void init() {
		// No init necessary
	}

	@Override
	public void update() {
		handleInput();
		color = Color.getHSBColor(hue, 1f, 1f);
		hue += 0.01;
		if (hue > 1)
			hue = 0;
		angle += 0.1;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(color);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		AffineTransform at = new AffineTransform();
		at.translate(GamePanel.WIDTH / 2.0, GamePanel.HEIGHT / 2.0);
		at.rotate(angle);
		g.drawImage(image, at, null);
	}

	@Override
	public void handleInput() {
		if (Keys.isPressed(Keys.ESCAPE))
			gsm.setState(GameStateManager.MENUSTATE);
	}

	@Override
	protected void populateEnemies(EnemyType[] enemies, int[][] coords) {
		// Not Necessary

	}

	@Override
	protected void eventStart() {
		// Not Necessary

	}

	@Override
	protected void eventDead() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void eventFinish() {
		// TODO Auto-generated method stub

	}

}
