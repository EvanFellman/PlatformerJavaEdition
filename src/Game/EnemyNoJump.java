package Game;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class EnemyNoJump extends Thing {
	public float speed;
	public EnemyNoJump(float x, float y, float speed) throws IOException {
		super(x, y, "enemy no jump", ImageIO.read(new File("enemy.png")));
		this.speed = speed;
	}
	
	public void move() {
		Main.removeFromMap(this);
		if(this.dy > 5) {
			this.dy = 5;
		}
		this.dx = 0;
		if(Main.player.getX() < this.x) {
			this.dx = -1 * this.speed;
		}
		if(Main.player.getX() > this.x) {
			this.dx = this.speed;
		}
		this.x += this.dx;
		for(int i = 1 + (-1 * Main.SPRITE_WIDTH); i <= Main.SPRITE_WIDTH; i++) {
			for(int j = 1 + (-1 * Main.SPRITE_HEIGHT); j < Main.SPRITE_HEIGHT; j++) {
				Thing a = Main.getFromMap(i + this.x, j + this.y);
				if(a != null && (a.id.equals("wall") || a.id.contains("enemy")) ) {
					if(this.dx > 0) {
						this.dx = 0;
						this.x = a.getX() - Main.SPRITE_WIDTH;
						break;
					} else if(this.dx < 0) {
						this.dx = 0;
						this.x = a.getX() + Main.SPRITE_WIDTH;
						break;
					}
				}
			}
		}
		this.dy += Main.GRAVITY;
		this.y += this.dy;
		for(int i = 1 + (int)(-1 * Main.SPRITE_WIDTH); i < Main.SPRITE_WIDTH; i++) {
			for(int j = -1 * Main.SPRITE_HEIGHT; j < Main.SPRITE_HEIGHT; j++) {
				Thing a = Main.getFromMap(i + this.x, j + this.y);
				if(a != null) {
					if(a.id.equals("wall") || a.id.contains("enemy")) {
						if(this.dy > 0) {
							this.dy = 0;
							if(a.getY() > this.y) {
								this.y = a.y - Main.SPRITE_HEIGHT;
							}
							break;
						} else if(this.dy < 0) {
							this.dy = 0;
							this.y = a.y + Main.SPRITE_HEIGHT;
							break;
						}
					}
				}
			}
		}
		if(this.y > Main.DEATH_BELOW) {
			this.die();
		}
		Main.putInMap(this);
	}
	
	public void die() {
		Main.removeFromMap(this);
		Main.level.remove(this);
	}
}
