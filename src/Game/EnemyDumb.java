package Game;

public class EnemyDumb extends Enemy {
	public boolean goLeft;
	public double speed;
	public EnemyDumb(double x, double y, boolean goLeft, double speed) {
		super(x, y, "enemy dumb", 1, 2);
		this.goLeft = goLeft;
		this.speed = speed;
	}
	
	public boolean move() {
		if(this.dx == 0) {
			this.dx = this.goLeft ? -1 * this.speed : this.speed;
			this.goLeft = !this.goLeft;
		}
		return super.move();
	}
}
