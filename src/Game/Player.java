package Game;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player extends Thing {

	public Player(float x, float y) throws IOException {
		super(x, y, "player", ImageIO.read(new File("player.png")));
	}

	@Override
	void move() {
		if(this.dy > 5) {
			this.dy = 5;
		}
		this.dx = 0;
		if(Main.isAPressed) {
			this.dx = -1f;
		}
		if(Main.isDPressed) {
			this.dx = 1f;
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
				if(i.id.equals("wall")) {
					if(this.dy > 0) {
						if(Main.isWPressed) {
							this.dy = -2;
						} else {
							this.dy = 0;
						}
						this.y = i.y - Main.SPRITE_HEIGHT;
						break;
					} else if(this.dy < 0) {
						this.dy = 0;
						this.y = i.y + Main.SPRITE_HEIGHT;
						break;
					}
				} else if(i.id.equals("next level")) {
					Main.levelNumber += 1;
					Main.loadLevel();
				}
			}
		}
		if(this.y > Main.DEATH_BELOW) {
			this.die();
		}
	}
	
	void die() {
		Main.loadLevel();
	}
}
