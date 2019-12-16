package Game;

public class WallMoving extends Thing {
	public static final int UP = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;
	public static final int RIGHT = 4;
	public double speed;
	public int direction;
	public WallMoving(double x, double y, int dir, double speed) {
		super(x, y, "wall moving", 3, 3);
		this.direction = dir;
		this.speed = speed;
	}
		
	public boolean move() {
		this.x += this.dx;
		this.y += this.dy;
		for(int i = 0; i < Main.level.size(); i++) {
			Thing a = Main.level.get(i);
			if(a == null || !this.isTouching(a) || this.equals(a) || !a.id.contains("wall")) {
				continue;
			}
			switch(this.direction) {
			case UP:
				if(a.id.contains("wall") && this.below(a)) {
					if(a.dy <= 0  || (a.id.equals("wall moving") && ((WallMoving) a).direction == DOWN)) {
						this.direction = DOWN;
					}
					this.y = a.getY() + Main.SPRITE_HEIGHT;
				}
				break;
			case DOWN:
				if(a.id.contains("wall") && this.above(a)) {
					if(a.dy >= 0 || (a.id.equals("wall moving") && ((WallMoving) a).direction == UP)) {
						this.direction = UP;
					}
					this.y = a.getY() - Main.SPRITE_HEIGHT;
				}
				break;
			case LEFT:
				if(this.toRightOf(a)) {
					this.direction = RIGHT;
					this.x = a.getX() + Main.SPRITE_WIDTH;
				}
				break;
			case RIGHT:
				if(this.toLeftOf(a)) {
					this.direction = LEFT;
					this.x = a.getX() - Main.SPRITE_WIDTH;
				}
				break;
			}
			
		}
		switch(this.direction) {
		case UP:
			this.dx = 0;
			this.dy = -1 * this.speed;
			break;
		case DOWN:
			this.dx = 0;
			this.dy = this.speed;
			break;
		case LEFT:
			this.dx = -1 * this.speed;
			this.dy = 0;
			break;
		case RIGHT:
			this.dx = this.speed;
			this.dy = 0;
			break;
		}
		return super.move();
	}
}
