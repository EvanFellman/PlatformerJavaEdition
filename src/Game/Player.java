package Game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player extends Thing {
	private static BufferedImage deadImage; 
	public Player(float x, float y) {
		super(x, y, "player", 1, 0);
		try {
			deadImage = ImageIO.read(new File("config/textures.png")).getSubimage(4 * Main.SPRITE_WIDTH, 4 * Main.SPRITE_HEIGHT, Main.SPRITE_WIDTH, Main.SPRITE_HEIGHT);
		} catch (IOException e) { }
		this.dx = 0;
	}

	@Override
	public boolean move() {
		Main.removeFromMap(this);
		if(!Main.deadPlayer) {
			if(this.dy > 15) {
				this.dy = 15;
			}
			if(Main.isAPressed) {
				if(this.dx > 0) {
					this.dx = 0;
				}
				if(this.dx > -4) {
					this.dx -= 0.5;
				}
			} else if(Main.isDPressed) {
				if(this.dx < 0) {
					this.dx = 0;
				}
				if(this.dx < 4) {
					this.dx += 0.5;
				}
			} else if(this.dx > 0){
				this.dx -= 0.5;
			} else if(this.dx < 0){
				this.dx += 0.5;
			}
			this.x += this.dx;
			boolean nearWalll = false, nearWallMovingl = false, nearWallr = false, nearWallMovingr = false;
			for(Thing a: Main.level) {
				if(a != null && (a.id.contains("wall") || (!this.equals(a) && a.id.equals("player"))) && this.isTouching(a)) {
					if(a.x > this.x) {
						if(a.id.equals("wall moving")) {
							if(a.dx < 0) {
								nearWallMovingr = true;
							}
						} else {
							nearWallr = true;
						}
					} else {
						if(a.id.equals("wall moving")) {
							if(a.dx > 0) {
								nearWallMovingl = true;
							}
						} else {
							nearWalll = true;
						}
					}
					if((a.y - this.y < a.dy + Main.SPRITE_HEIGHT && this.y - a.y < Main.SPRITE_HEIGHT + a.dy)){
						if(this.x <= a.x - Main.SPRITE_WIDTH + this.dx && this.dx >= 0) {
							this.dx = 0;
							this.x = a.getX() - Main.SPRITE_WIDTH;
						} else if(this.x >= a.x + Main.SPRITE_WIDTH + this.dx && this.dx <= 0) {
							this.dx = 0;
							this.x = a.getX() + Main.SPRITE_WIDTH;
						}
					}
				}
			}
			if((nearWalll && nearWallMovingr) || (nearWallMovingl && nearWallr) || (nearWallMovingl && nearWallMovingr)) {
				this.die();
				return true;
			}
		}
		this.dy += Main.GRAVITY;
		this.y += this.dy;
		if(!Main.deadPlayer) {
			boolean nearWalla = false, nearWallMovinga = false, nearWallb = false, nearWallMovingb = false;
			for(int i = 0; i < Main.level.size(); i++) {
				Thing a = Main.level.get(i);
				if(!this.equals(a) && this.isTouching(a)) {
					if(a.id.contains("wall") || (!this.equals(a) && a.id.equals("player"))) {
						if(a.y > this.y) {
							if(a.id.equals("wall moving")) {
								if(a.dy < 0) {
									nearWallMovinga = true;
								}
							} else {
								nearWalla = true;
							}
						} else {
							if(a.id.equals("wall moving")) {
								if(a.dy > 0) {
									nearWallMovingb = true;
								}
							} else {
								nearWallb = true;
							}
						}
						if(this.dy >= 0) {
							if(Main.isWPressed && a.getY() > this.y) {
								this.dy = -10;
							} else if(this.dy > a.dy) {
								this.dy = 0;
							}
							if(a.getY() > this.y) {
								this.y = (a.y - Main.SPRITE_HEIGHT);
							} else if(a.getY() < this.y) {
								this.y = (a.y + Main.SPRITE_HEIGHT);
							}
						} else if(this.dy < 0) {
							this.dy = 0;
							if(this.x + Main.SPRITE_WIDTH > a.x && this.x - Main.SPRITE_WIDTH < a.x) {
								this.y = a.y + Main.SPRITE_HEIGHT;
							}
						}
					} else if(a.id.equals("next level")) {
						Main.levelNumber += 1;
						Main.loadLevel();
						Main.deadPlayer = false;
						return false;
					} else if(a.id.contains("enemy")) {
						if(this.y + 3 < a.y && this.dy >= 0) {
							if(Main.isWPressed) {
								this.dy = -15;
							} else {
								this.dy = 0;
							}
							a.die();
						} else {
							this.die();
							return true;
						}
					}
				}
			}
			if((nearWalla && nearWallMovingb) || (nearWallMovinga && nearWallb) || (nearWallMovinga && nearWallMovingb)) {
				this.die();
				return true;
			}
			if(this.y > Main.DEATH_BELOW) {
				this.die();
				return true;
			}
		}
		Main.putInMap(this);
		return false;
	}
	
	public void die() {
		if(Main.STATE.equals("play0")) {
			Main.deadPlayer = true;
			Main.deadPlayerCounter = 75;
		} else {
			Main.deadPlayer = true;
			Main.deadPlayerCounter = 75;
//			Main.STATE = "menu";
//			Main.window.remove(Main.rp);
		}
	}
	
	@Override
	public void display(Graphics g) {
		if(Main.deadPlayer) {
			this.pic = deadImage;
		}
		super.display(g);
	}
}
