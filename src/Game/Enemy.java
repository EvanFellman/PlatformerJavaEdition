package Game;

public abstract class Enemy extends Thing {
	public Enemy(float x, float y, String id, int picX, int picY) {
		super(x, y, id, picX, picY);
	}
	
	void move() {
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
				}
			}
		}
		this.dy += Main.GRAVITY;
		this.y += this.dy;
		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 1; j++) {
				Thing a = Main.getFromMap(this.x + (i * Main.SPRITE_WIDTH), this.y + (j * Main.SPRITE_HEIGHT));
				if(a != null && this.isTouching(a)) {
					if(a.id.equals("wall") || a.id.contains("enemy")) {
						if(this.dy > 0) {
							if(a.getDy() < 0 || !a.id.contains("enemy")){
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
		Main.removeFromMap(this);
		Main.level.remove(this);
	}
}
