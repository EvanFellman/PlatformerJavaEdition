package Game;

public class EnemyDumb extends Enemy {
	private boolean goLeft;
	private float speed;
	public EnemyDumb(float x, float y, boolean goLeft, float speed) {
		super(x, y, "enemy dumb", 1, 2);
		this.goLeft = goLeft;
		this.speed = speed;
	}
	
	public void move() {
		if(this.dx == 0) {
			this.dx = this.goLeft ? -1 * this.speed : this.speed;
			this.goLeft = !this.goLeft;
		}
		super.move();
	}
}
