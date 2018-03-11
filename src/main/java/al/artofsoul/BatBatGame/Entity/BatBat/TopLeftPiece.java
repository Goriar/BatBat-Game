package al.artofsoul.BatBatGame.Entity.BatBat;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import al.artofsoul.BatBatGame.Entity.MapObject;
import al.artofsoul.BatBatGame.Handlers.LoggingHelper;
import al.artofsoul.BatBatGame.TileMap.TileMap;

/**
 * @author ArtOfSoul
 *
 */

public class TopLeftPiece extends MapObject{
	
	private BufferedImage[] sprites;
	
	public TopLeftPiece(TileMap tm) {
		super(tm);
		try {	
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream("/Sprites/Other/ballBatBoss.gif")
			);
			sprites = new BufferedImage[1];
			width = height = 4;
			sprites[0] = spritesheet.getSubimage(0, 0, 10, 10);
			animation.setFrames(sprites);
			animation.setDelay(-1);
		}
		catch(Exception e) {
			LoggingHelper.LOGGER.log(Level.SEVERE,e.getMessage());
		}
	}
	
	public void update() {
		x += dx;
		y += dy;
		animation.update();
	}
	
	public void draw(Graphics2D g) {
		super.draw(g);
	}
	
}
