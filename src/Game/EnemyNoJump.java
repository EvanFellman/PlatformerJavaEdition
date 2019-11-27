package Game;

public class EnemyNoJump extends Enemy {
	public float speed;
	public EnemyNoJump(float x, float y, float speed) {
		super(x, y, "enemy no jump", 0, 0);
		this.speed = speed;
	}
	
	public void move() {
		if(Main.player.getX() + 50 < this.x) {
			this.dx = -1 * this.speed;
		}
		if(Main.player.getX() - 50 > this.x) {
			this.dx = this.speed;
		}
		if(this.dx == 0) {
			this.dx = Main.player.getX() < this.x ? -1 * speed : speed;
		}
		super.move();
	}
}
