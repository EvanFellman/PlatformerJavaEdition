package Game;

public class EnemyBullet extends Enemy {
	public boolean goLeft;
	public double speed;
	public EnemyBullet(double x, double y, boolean goLeft, double speed) {
		super(x, y, "enemy bullet " + (goLeft? "left" : "right"), 4, goLeft ? 0 : 1);
		this.goLeft = goLeft;
		this.speed = speed;
	}
	
	public boolean move() {
		this.x += this.dx;
		if(this.goLeft) {
			for(int i = 0; i < Main.level.size(); i++) {
				Thing a = Main.level.get(i);
				double x = this.x - a.getX();
				if(this.isTouching(a) && a.id.contains("wall") && x < Main.SPRITE_WIDTH) {
					this.die();
				}
			}
		} else {
			for(int i = 0; i < Main.level.size(); i++) {
				Thing a = Main.level.get(i);
				if(this.isTouching(a) && a.id.contains("wall") && this.toRightOf(a)) {
					this.die();
				}
			}
		}
		this.dx = goLeft ? -1 * speed : 1 * speed;
		return false;
	}

}
