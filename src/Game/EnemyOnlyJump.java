package Game;

public class EnemyOnlyJump extends Enemy {

	public EnemyOnlyJump(float x, float y) {
		super(x, y, "enemy only jump", 2, 0);
	}
	
	public void move() {
		if(Main.isWPressed) {
			for(int i = 1 + (int)(-1 * Main.SPRITE_WIDTH); i < Main.SPRITE_WIDTH; i++) {
				for(int j = -1 * Main.SPRITE_HEIGHT; j <= Main.SPRITE_HEIGHT; j++) {
					Thing a = Main.getFromMap(i + this.x, j + this.y);
					if(a != null) {
						if(!a.equals(this) && (a.id.equals("wall") || a.id.contains("enemy"))) {
							if(this.dy > 0) {
								this.dy = -2;
								break;
							}
						}
					}
				}
			}
		}
		super.move();
	}
}
