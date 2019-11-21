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
		Main.removeFromMap(this);
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
		for(int i = -1 * Main.SPRITE_WIDTH; i <= Main.SPRITE_WIDTH; i++) {
			for(int j = 1 + (-1 * Main.SPRITE_HEIGHT); j < Main.SPRITE_HEIGHT; j++) {
				Thing a = Main.getFromMap(i + this.x, j + this.y);
				if(a != null && a.id.equals("wall")) {
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
					if(a.id.equals("wall")) {
						if(this.dy > 0) {
							if(Main.isWPressed && a.getY() > this.y) {
								this.dy = -2;
							} else {
								this.dy = 0;
							}
							if(a.getY() > this.y) {
								this.y = a.y - Main.SPRITE_HEIGHT;
							}
							break;
						} else if(this.dy < 0) {
							this.dy = 0;
							this.y = a.y + Main.SPRITE_HEIGHT;
							break;
						}
					} else if(a.id.equals("next level")) {
						Main.levelNumber += 1;
						Main.loadLevel();
					} else if(a.id.contains("enemy")) {
						if(this.dy > 1) {
							if(Main.isWPressed) {
								this.dy = -2.5f;
							} else {
								this.dy = 0;
							}
							a.die();
						} else {
							this.die();
						}
					}
				}
			}
		}
//		for(int j = 0; j < Main.level.size(); j++){
//			Thing i = Main.level.get(j);
//			if(this.isTouching(i)) {
//				
//			}
//		}
		if(this.y > Main.DEATH_BELOW) {
			this.die();
		}
		Main.putInMap(this);
	}
	
	void die() {
		Main.loadLevel();
	}
}
