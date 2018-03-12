package al.artofsoul.batbatgame.gamestate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import al.artofsoul.batbatgame.audio.JukeBox;
import al.artofsoul.batbatgame.entity.PlayerSave;
import al.artofsoul.batbatgame.handlers.Keys;
import al.artofsoul.batbatgame.handlers.LoggingHelper;
import al.artofsoul.batbatgame.main.GamePanel;

/**
 * @author ArtOfSoul
 *
 */

public class MenuState extends GameState {

	private BufferedImage bg;
	private BufferedImage head;
	private int currentChoice = 0;
	private String[] options = { "Play", "Options", "Quit" };

	private Font font;
	private Font font2;

	public MenuState(GameStateManager gsm) {

		super(gsm);

		try {
			bg = ImageIO.read(getClass().getResourceAsStream("/Backgrounds/sfondi.gif")).getSubimage(0, 0,
					GamePanel.WIDTH, GamePanel.HEIGHT);
			// load floating head
			head = ImageIO.read(getClass().getResourceAsStream("/HUD/Hud.gif")).getSubimage(0, 12, 12, 11);
			// titles and fonts
			font = new Font("Arial", Font.BOLD, 11);
			font2 = new Font("Arial", Font.PLAIN, 9);
			// load sound fx
			JukeBox.load("/SFX/menuoption.mp3", "menuoption");
			JukeBox.load("/SFX/menuselect.mp3", "menuselect");
		} catch (Exception e) {
			LoggingHelper.LOGGER.log(Level.SEVERE, e.getMessage());
		}
	}

	@Override
	public void update() {
		// check keys
		handleInput();
	}

	@Override
	public void draw(Graphics2D g) {
		// draw bg
		g.drawImage(bg, 0, 0, null);
		// draw menu options
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString("Play", 140, 133);
		g.drawString("Options", 140, 148);
		g.drawString("Quit", 140, 163);
		// draw floating head
		if (currentChoice == 0)
			g.drawImage(head, 120, 123, null);
		else if (currentChoice == 1)
			g.drawImage(head, 120, 138, null);
		else if (currentChoice == 2)
			g.drawImage(head, 120, 153, null);
		// other
		g.setFont(font2);
		g.drawString("2017 © toni kolaba", 10, 232);
	}

	private void select() {
		switch (currentChoice) {
		case 0:
			JukeBox.play("menuselect");
			PlayerSave.init();
			gsm.setState(GameStateManager.LEVEL1STATE); /// start this level entrance
			break;
		case 1:
			gsm.setState(GameStateManager.OPTIONSSTATE);
			break;
		case 2:
			System.exit(0);
			break;
		}
	}

	@Override
	public void handleInput() {
		if (Keys.isPressed(Keys.ENTER))
			select();
		if (Keys.isPressed(Keys.UP) && currentChoice > 0) {
			JukeBox.play("menuoption", 0);
			currentChoice--;
		}
		if (Keys.isPressed(Keys.DOWN) && currentChoice < options.length - 1) {
			JukeBox.play("menuoption", 0);
			currentChoice++;
		}
	}
}
