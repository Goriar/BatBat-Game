package al.artofsoul.batbatgame.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import al.artofsoul.batbatgame.handlers.LoggingHelper;
import al.artofsoul.batbatgame.tilemap.TileMap;

/**
 * @author ArtOfSoul
 *
 */

public class Teleport extends MapObject {
	
	private BufferedImage[] sprites;
	
	public Teleport(TileMap tm) {
		super(tm);
		facingRight = true;
		width = height = 40;
		cwidth = 20;
		cheight = 40;
		try {
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream("/Sprites/Player/Teleport.gif")
			);
			sprites = new BufferedImage[9];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(
					i * width, 0, width, height
				);
			}
			animation.setFrames(sprites);
			animation.setDelay(1);
		}
		catch(Exception e) {
			LoggingHelper.LOGGER.log(Level.SEVERE,e.getMessage());
		}
	}
	
	public void update() {
		animation.update();
	}
	
	public void draw(Graphics2D g) {
		super.draw(g);
	}
	
}
