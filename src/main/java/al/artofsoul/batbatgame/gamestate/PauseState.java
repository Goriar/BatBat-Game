package al.artofsoul.batbatgame.gamestate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import al.artofsoul.batbatgame.handlers.Keys;
import al.artofsoul.batbatgame.main.GamePanel;

/**
 * @author ArtOfSoul
 *
 */

public class PauseState extends GameState {

	private Font font;

	public PauseState(GameStateManager gsm) {

		super(gsm);

		// fonts
		font = new Font("Arial", Font.PLAIN, 12);

	}

	@Override
	public void update() {
		handleInput();
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString("Game Paused", 110, 110);
	}

	@Override
	public void handleInput() {
		if (Keys.isPressed(Keys.ESCAPE))
			gsm.setPaused(false);
		if (Keys.isPressed(Keys.BUTTON1)) {
			gsm.setPaused(false);
			gsm.setState(GameStateManager.MENUSTATE);
		}
	}

}
