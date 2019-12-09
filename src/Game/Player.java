package Game;

public class Player extends Thing {
	public Player(float x, float y) {
		super(x, y, "player", 1, 0);
	}

	@Override
	public boolean move() {
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
					if(a.id.equals("wall moving") && a.dx != 0) {
						if(this.x < a.x) {
							this.dx = 0;
							this.x = a.getX() - Main.SPRITE_WIDTH;
						} else if(this.x > a.x) {
							this.dx = 0;
							this.x = a.getX() + Main.SPRITE_WIDTH;
						}
					} else if(a.id.equals("wall moving")) {
						double x = this.x - a.x;
						if(!(a.y + Main.SPRITE_HEIGHT * 0.95 <= this.y || a.y - Main.SPRITE_HEIGHT * 0.95 >= this.y) && x < Main.SPRITE_WIDTH && x > -1 * Main.SPRITE_WIDTH) {
							if(this.x < a.x) {
								this.dx = 0;
								this.x = a.getX() - Main.SPRITE_WIDTH;
							} else if(this.x > a.x) {
								this.dx = 0;
								this.x = a.getX() + Main.SPRITE_WIDTH;
							}
						}
					} else {
						if(this.dx > 0) {
							this.dx = 0;
							this.x = a.getX() - Main.SPRITE_WIDTH;
						} else if(this.dx < 0) {
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
							this.dy = -2;
						} else if(!(a.id.equals("wall moving") && a.dy != 0)) {
							this.dy = 0;
						}
						if(a.getY() > this.y) {
							this.y = a.y - Main.SPRITE_HEIGHT;
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
					if(this.y < a.y && this.dy >= 0) {
						if(Main.isWPressed) {
							this.dy = -2.5f;
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
