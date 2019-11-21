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
		for(Thing i: Main.level){
			if(i.id.equals("wall") && this.isTouching(i)) {
				if(this.dx > 0) {
					this.dx = 0;
					this.x = i.x - Main.SPRITE_WIDTH;
					break;
				} else if(this.dx < 0) {
					this.dx = 0;
					this.x = i.x + Main.SPRITE_WIDTH;
					break;
				}
			}
		}
		this.dy += Main.GRAVITY;
		this.y += this.dy;
		for(Thing i: Main.level){
			if(this.isTouching(i)) {
				if(i.id.equals("wall") || (!i.equals(this) && i.id.contains("enemy"))) {
					if(this.dy > 0) {
						this.dy = 0;
						this.y = i.y - Main.SPRITE_HEIGHT;
						break;
					} else if(this.dy < 0) {
						this.dy = 0;
						this.y = i.y + Main.SPRITE_HEIGHT;
						break;
					}
				}
			}
		}
		if(this.y > Main.DEATH_BELOW) {
			this.die();
		}
	}
	
	public void die() {
		Main.level.remove(this);
	}
}
