package Game;

public class EnemyOnlyJump extends Enemy {

	public EnemyOnlyJump(double x, double y) {
		super(x, y, "enemy only jump", 0, 2);
	}
	
	public boolean move() {
		if(Main.isWPressed) {
			for(int i = 0; i < Main.level.size(); i++) {
				Thing a = Main.level.get(i);
				if(a != null && !a.equals(this) && (a.id.contains("wall") || a.id.contains("enemy")) && this.dy >= 0 && this.isNextTo(a) && a.y > this.y) {
					this.dy = -2;
					break;
				}
			}
		}
		return super.move();
	}
}
