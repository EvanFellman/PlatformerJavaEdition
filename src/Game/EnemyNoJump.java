package Game;

public class EnemyNoJump extends Enemy {
	public double speed;
	public EnemyNoJump(double x, double y, double speed) {
		super(x, y, "enemy no jump", 0, 0);
		this.speed = speed;
	}
	
	public boolean move() {
		Player player = Main.player.get(0);
		for(int i = 1; i < Main.player.size(); i++) {
			if(this.dist(Main.player.get(i)) < this.dist(player)) {
				player = Main.player.get(i);
			}
		}
		if(player.getX() + (Main.SPRITE_WIDTH * 2) < this.x) {
			this.dx = -1 * this.speed;
		}
		if(player.getX() - (Main.SPRITE_WIDTH * 2) > this.x) {
			this.dx = this.speed;
		}
		if(this.dx == 0) {
			this.dx = player.getX() < this.x ? -1 * speed : speed;
		}
		return super.move();
	}
}
