package Game;

public class EnemyOnlyJump extends Enemy {

	public EnemyOnlyJump(double x, double y) {
		super(x, y, "enemy only jump", 0, 2);
	}
	
	public boolean move() {
		if(Main.isWPressed) {
			for(int i = -1; i <= 1; i++) {
				for(int j = 0; j <= 1; j++) {
					Thing a = Main.getFromMapStable(this.x + (i * Main.SPRITE_WIDTH), this.y + (j * Main.SPRITE_HEIGHT));
					if(a != null && !a.equals(this) && a.id.contains("enemy") && this.dy > 0 && this.y < a.y && this.x - a.x < Main.SPRITE_WIDTH && a.x - this.x < Main.SPRITE_WIDTH) {
						this.dy = -2;
						break;
					}
					a = Main.getFromMapStable(this.x + (i * Main.SPRITE_WIDTH), this.y + (j * Main.SPRITE_HEIGHT));
					if(a != null && !a.equals(this) && a.id.contains("wall") && this.dy > 0 && this.y < a.y && this.x - a.x < Main.SPRITE_WIDTH && a.x - this.x < Main.SPRITE_WIDTH) {
						this.dy = -2;
						break;
					}
				}
			}
		}
		return super.move();
	}
}
