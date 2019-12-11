package Game;

public class Shooter extends Thing {
	public int direction;
	public final static int UP = 1;
	public final static int DOWN = 2;
	public final static int LEFT = 3;
	public final static int RIGHT = 4;
	public double speed;
	private int countDown;
	private int SHOOTING_WAIT = 500;
	public Shooter(double x, double y, int direction, double speed) {
		super(x, y, "wall shooter " + (direction == UP ? "up" : (direction == DOWN ? "down" : (direction == LEFT ? "left" : "right"))), direction == UP ? 2 : (direction == DOWN ? 3 : (direction == LEFT ? 0 : 1)), 4);
		this.direction = direction;
		this.speed = speed;
		SHOOTING_WAIT *= Main.SLOW_SPEED / this.speed;
		countDown = SHOOTING_WAIT;
	}
	
	public boolean move() {
		int i = 0;
		int j = 0;
		switch(this.direction) {
		case UP:
			j = -1 * Main.SPRITE_HEIGHT;
			break;
		case DOWN:
			j = Main.SPRITE_HEIGHT;
			break;
		case LEFT:
			i = -1 * Main.SPRITE_WIDTH;
			break;
		case RIGHT:
			i = Main.SPRITE_WIDTH;
			break;
		}
		Thing moveable = Main.getFromMapMoving(this.x + i, this.y + j);
		Thing stable = Main.getFromMapStable(this.x + i, this.y + j);
		if((moveable == null || !this.isNextTo(moveable) ) && (stable == null || !stable.id.contains("wall"))) {
			this.countDown--;
			if(this.countDown <= 0) {
				Thing bullet = new EnemyBullet(this.x + i, this.y + j, this.direction, this.speed);
				Main.level.add(bullet);
				Main.putInMap(bullet);
				this.countDown = SHOOTING_WAIT;
			}
		}
		return false;
	}
}
