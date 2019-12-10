package Game;

public class EnemyBullet extends Enemy {
	public int direction;
	public final static int UP = 1;
	public final static int DOWN = 2;
	public final static int LEFT = 3;
	public final static int RIGHT = 4;
	public double speed;
	public EnemyBullet(double x, double y, int direction, double speed) {
		super(x, y, "enemy bullet " + (direction == UP ? "up" : (direction == DOWN ? "down" : (direction == LEFT ? "left" : "right"))), 4, direction == UP ? 2 : (direction == DOWN ? 3 : (direction == LEFT ? 0 : 1)));
		this.direction = direction;
		this.speed = speed;
	}
	
	public boolean move() {
		this.x += this.dx;
		this.y += this.dy;
		switch(this.direction) {
		case LEFT:
			for(int i = 0; i < Main.level.size(); i++) {
				Thing a = Main.level.get(i);
				double x = this.x - a.getX();
				if(this.isTouching(a) && a.id.contains("wall") && x < Main.SPRITE_WIDTH) {
					this.die();
				}
			}
			this.dx = -1 * this.speed;
			break;
		case RIGHT:
			for(int i = 0; i < Main.level.size(); i++) {
				Thing a = Main.level.get(i);
				double x = this.x - a.getX();
				if(this.isTouching(a) && a.id.contains("wall") && x < Main.SPRITE_WIDTH) {
					this.die();
				}
			}
			this.dx = 1 * this.speed;
			break;
		case UP:
			for(int i = 0; i < Main.level.size(); i++) {
				Thing a = Main.level.get(i);
				double y = this.y - a.getY();
				if(this.isTouching(a) && a.id.contains("wall") && y < Main.SPRITE_HEIGHT) {
					this.die();
				}
			}
			this.dy = -1 * this.speed;
			break;
		case DOWN:
			for(int i = 0; i < Main.level.size(); i++) {
				Thing a = Main.level.get(i);
				double y = this.y - a.getY();
				if(this.isTouching(a) && a.id.contains("wall") && y < Main.SPRITE_HEIGHT) {
					this.die();
				}
			}
			this.dy = 1 * this.speed;
			break;
		}
		return false;
	}

}
