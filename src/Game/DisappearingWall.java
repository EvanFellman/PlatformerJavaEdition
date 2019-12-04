package Game;

public class DisappearingWall extends Thing {
	public int disappearCount;
	public DisappearingWall(double x, double y) {
		super(x, y, "wall disappearing", 0, 3);
		disappearCount = 50;
	}
	
	public boolean move() {
		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 1; j++) {
				Thing a = Main.getFromMap(this.x + (i * Main.SPRITE_WIDTH), this.y + (j * Main.SPRITE_HEIGHT));
				if(a == null) {
					continue;
				}
				boolean isTouch = true;
				if(abs(a.getX() - this.x) > Main.SPRITE_WIDTH) {
					isTouch = false;
				}
				if(abs(a.getY() - this.y) > Main.SPRITE_HEIGHT) {
					isTouch = false;
				}
				if(isTouch && (a.id.equals("player") || a.id.contains("enemy"))) {
					disappearCount--;
					if(disappearCount <= 0) {
						this.die();
					}
				}
			}
		}
		return false;
	}
	
	private double abs(double d) {
		if(d < 0) {
			return d * -1;
		} else {
			return d;
		}
	}

	public void die() {
		Main.removeFromMap(this);
		Main.level.remove(this);
	}
}
