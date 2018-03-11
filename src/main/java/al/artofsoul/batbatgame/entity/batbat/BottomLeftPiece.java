package al.artofsoul.batbatgame.entity.batbat;

import java.awt.image.BufferedImage;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import al.artofsoul.batbatgame.entity.MapObject;
import al.artofsoul.batbatgame.handlers.LoggingHelper;
import al.artofsoul.batbatgame.tilemap.TileMap;

/**
 * @author ArtOfSoul
 *
 */

public class BottomLeftPiece extends MapObject {

	private BufferedImage[] sprites;

	public BottomLeftPiece(TileMap tm) {
		super(tm);
		try {
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Other/ballBatBoss.gif"));
			sprites = new BufferedImage[1];
			width = height = 4;
			sprites[0] = spritesheet.getSubimage(0, 10, 10, 10);
			animation.setFrames(sprites);
			animation.setDelay(-1);
		} catch (Exception e) {
			LoggingHelper.LOGGER.log(Level.SEVERE, e.getMessage());
		}
	}

	public void update() {
		x += dx;
		y += dy;
		animation.update();
	}

}
