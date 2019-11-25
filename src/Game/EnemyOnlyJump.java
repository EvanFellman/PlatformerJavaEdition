package Game;

public class EnemyOnlyJump extends Enemy {

	public EnemyOnlyJump(float x, float y) {
		super(x, y, "enemy only jump", 0, 2);
	}
	
	public void move() {
		if(Main.isWPressed) {
			for(int i = -1; i <= 1; i++) {
				for(int j = -1; j <= 1; j++) {
					Thing a = Main.getFromMap(this.x + (i * Main.SPRITE_WIDTH), this.y + (j * Main.SPRITE_HEIGHT));
					if(a != null && !a.equals(this) && (a.id.equals("wall") || a.id.contains("enemy")) && this.dy > 0) {
						this.dy = -2;
						break;
					}
				}
			}
		}
		super.move();
	}
}
