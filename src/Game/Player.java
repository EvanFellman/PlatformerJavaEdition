package Game;

public class Player extends Thing {
	public Player(float x, float y) {
		super(x, y, "player", 1, 0);
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
		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 1; j++) {
				Thing a = Main.getFromMap(this.x + (i * Main.SPRITE_WIDTH), this.y + (j * Main.SPRITE_HEIGHT));
				if(a != null && a.id.contains("wall") && this.isTouching(a)) {
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
		this.dy += Main.GRAVITY;
		this.y += this.dy;
		for(int i = -1; i <= 2; i++) {
			for(int j = -1; j <= 1; j++) {
				Thing a = Main.getFromMap(this.x + (i * Main.SPRITE_WIDTH), this.y + (j * Main.SPRITE_HEIGHT));
				if(!this.equals(a) && this.isTouching(a)) {
					if(a.id.contains("wall")) {
						if(this.dy > 0) {
							if(Main.isWPressed && a.getY() > this.y) {
								this.dy = -2;
							} else {
								this.dy = 0;
							}
							if(a.getY() > this.y) {
								this.y = a.y - Main.SPRITE_HEIGHT;
							}
						} else if(this.dy < 0) {
							this.dy = 0;
							this.y = a.y + Main.SPRITE_HEIGHT;
						}
					} else if(a.id.equals("next level")) {
						Main.levelNumber += 1;
						Main.loadLevel();
					} else if(a.id.contains("enemy")) {
						if(this.dy > 0 && this.y + 1 < a.getY()) {
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
		if(this.y > Main.DEATH_BELOW) {
			this.die();
		}
		Main.putInMap(this);
	}
	
	void die() {
		Main.loadLevel();
	}
}
