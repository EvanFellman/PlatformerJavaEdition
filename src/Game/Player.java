package Game;

public class Player extends Thing {
	public Player(float x, float y) {
		super(x, y, "player", 1, 0);
	}

	@Override
	public boolean move() {
		Main.removeFromMap(this);
		if(this.dy > 10) {
			this.dy = 10;
		}
		this.dx = 0;
		if(Main.isAPressed) {
			this.dx = -4f;
		}
		if(Main.isDPressed) {
			this.dx = 4f;
		}
		boolean nearWalll = false, nearWallMovingl = false, nearWallr = false, nearWallMovingr = false;
		this.x += this.dx;
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
		this.dy += Main.GRAVITY;
		this.y += this.dy;
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
		Main.putInMap(this);
		return false;
	}
	
	public void die() {
		if(Main.STATE.equals("play0")) {
			Main.loadLevel();
			Main.loadLevel();
		} else {
			Main.STATE = "menu";
			Main.window.remove(Main.rp);
		}
	}
}
