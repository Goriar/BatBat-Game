package al.artofsoul.batbatgame.gamestate;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import al.artofsoul.batbatgame.entity.Enemy;
import al.artofsoul.batbatgame.entity.EnemyProjectile;
import al.artofsoul.batbatgame.entity.Explosion;
import al.artofsoul.batbatgame.tilemap.TileMap;

/**
 * @author ArtOfSoul
 *
 */

public abstract class GameState {

	protected GameStateManager gsm;

	public GameState(GameStateManager gsm) {
		this.gsm = gsm;
	}

	public abstract void init();

	public abstract void update();

	public abstract void draw(Graphics2D g);

	public abstract void handleInput();

	public void handleObjects(TileMap tileMap, List<Enemy> enemies, List<EnemyProjectile> eprojectiles,
			List<Explosion> explosions) {
		ArrayList<Enemy> enemiesToRemove = new ArrayList<>();
		// update enemies
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update();
			if (e.isDead()) {
				enemiesToRemove.add(e);
				explosions.add(new Explosion(tileMap, e.getx(), e.gety()));
			}
		}

		for (Enemy enemy : enemiesToRemove) {
			enemies.remove(enemy);
		}

		ArrayList<EnemyProjectile> projectilesToRemove = new ArrayList<>();
		// update enemy projectiles
		for (int i = 0; i < eprojectiles.size(); i++) {
			EnemyProjectile ep = eprojectiles.get(i);
			ep.update();
			if (ep.shouldRemove()) {
				projectilesToRemove.add(ep);
			}
		}

		for (EnemyProjectile enemyProjectile : projectilesToRemove) {
			eprojectiles.remove(enemyProjectile);
		}

		ArrayList<Explosion> explosionsToRemove = new ArrayList<>();
		// update explosions
		for (int i = 0; i < explosions.size(); i++) {
			explosions.get(i).update();
			if (explosions.get(i).shouldRemove()) {
				explosionsToRemove.add(explosions.get(i));
			}
		}

		for (Explosion explosion : explosionsToRemove) {
			explosions.remove(explosion);
		}
	}
}
