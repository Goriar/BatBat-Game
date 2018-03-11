package al.artofsoul.BatBatGame.Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import al.artofsoul.BatBatGame.Handlers.LoggingHelper;
import al.artofsoul.BatBatGame.Main.GamePanel;

/**
 * @author ArtOfSoul
 *
 */

public class Title {
	
	private BufferedImage image;
	
	private int count;
	private boolean done;
	private boolean remove;
	
	private double x;
	private double y;
	private double dx;
	
	private int width;
	
	public Title(String s) {
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream(s));
			width = image.getWidth();
			x = -width;
			done = false;
		}
		catch(Exception e) {
			LoggingHelper.LOGGER.log(Level.SEVERE,e.getMessage());
		}
		
	}
	
	public Title(BufferedImage image) {
		this.image = image;
		width = image.getWidth();
		x = -width;
		done = false;
	}
	
	public void sety(double y) { this.y = y; }
	
	public void begin() {
		dx = 10;
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void update() {
		if(!done) {
			if(x >= (GamePanel.WIDTH - width) / 2.0) {
				x = (GamePanel.WIDTH - width) / 2.0;
				count++;
				if(count >= 120) done = true;
			}
			else {
				x += dx;
			}
		}
		else {
			x += dx;
			if(x > GamePanel.WIDTH) remove = true;
		}
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(image, (int)x, (int)y, null);
	}
	
}
