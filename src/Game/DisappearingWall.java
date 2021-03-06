package Game;

public class DisappearingWall extends Thing {
	public int disappearCount;
	public DisappearingWall(double x, double y) {
		super(x, y, "wall disappearing", 0, 3);
		disappearCount = 10;
	}
	
	public boolean move() {
		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 1; j++) {
				Thing a = Main.getFromMapMoving(this.x + (i * Main.SPRITE_WIDTH), this.y + (j * Main.SPRITE_HEIGHT));
				if(a == null) {
					continue;
				}
				if(this.isNextTo(a)) {
					disappearCount--;
					if(disappearCount <= 0) {
						this.die();
					}
				}
			}
		}
		return false;
	}

	public void die() {
		Main.removeFromMap(this);
		Main.level.remove(this);
	}
}
