package Game;

public abstract class Enemy extends Thing {
	public Enemy(double x, double y, String id, int picX, int picY) {
		super(x, y, id, picX, picY);
	}
	
	public boolean move() {
		Main.removeFromMap(this);
		if(this.dy > 5) {
			this.dy = 5;
		}
		this.x += this.dx;
		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 1; j++) {
				Thing a = Main.getFromMap(this.x + (i * Main.SPRITE_WIDTH), this.y + (j * Main.SPRITE_HEIGHT));
				if(a != null && (a.id.contains("wall") || a.id.contains("enemy")) && this.isTouching(a)) {
					if(this.dx > 0) {
						this.dx = 0;
						this.x = a.getX() - Main.SPRITE_WIDTH;
						break;
					} else if(this.dx < 0) {
						this.dx = 0;
						this.x = a.getX() + Main.SPRITE_WIDTH;
						break;
					}
				} else if(a != null && this.isTouching(a) && a.id.equals("player")) {
					if(a.dy > this.dy && a.y + 1 < this.y) {
						if(Main.isWPressed) {
							a.dy = -2.5f;
						} else {
							a.dy = 0;
						}
						this.die();
					} else {
						a.die();
						return true;
					}
				}
			}
		}
		this.dy += Main.GRAVITY;
		this.y += this.dy;
		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 1; j++) {
				Thing a = Main.getFromMap(this.x + (i * Main.SPRITE_WIDTH), this.y + (j * Main.SPRITE_HEIGHT));
				if(a != null && !this.equals(a) && this.isTouching(a) && (a.id.contains("wall") || a.id.contains("enemy"))) {
					if(this.dy > a.dy) {
						if(a.dy < 0){
							this.dy = 0;
						}
						if(a.getY() > this.y) {
							this.y = a.y - Main.SPRITE_HEIGHT;
						}
						break;
					} else if(this.dy < a.dy) {
						this.dy = 0;
						this.y = a.y + Main.SPRITE_HEIGHT;
						break;
					}
				} else if(a != null && this.isTouching(a) && a.id.equals("player")) {
					if(a.dy > this.dy && a.y + 1 < this.y) {
						if(Main.isWPressed) {
							a.dy = -2.5f;
						} else {
							a.dy = 0;
						}
						this.die();
					} else {
						a.die();
						return true;
					}
				}
			}
		}
		if(this.y + Main.SPRITE_HEIGHT > Main.DEATH_BELOW) {
			this.die();
		}
		Main.putInMap(this);
		return false;
	}
	
	public void die() {
		Main.removeFromMap(this);
		Main.level.remove(this);
	}
}
