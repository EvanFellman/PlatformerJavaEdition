package Game;

public class Spike extends Thing {

	public Spike(double x, double y) {
		super(x, y, "spike", 1, 3);
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public boolean move() {
		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 1; j++) {
				Thing a = Main.getFromMapMoving(this.x + (i * Main.SPRITE_WIDTH), this.y + (j * Main.SPRITE_HEIGHT));
				if(a != null && this.isTouching(a)) {
					if(a.id.contains("enemy")) {
						a.die();
					} else if(a.id.equals("player")) {
						if(((Player) a).playerState.equals(PlayerState.SPIKEDESTROYER)) {
							this.die();
						} else {
							a.die();
							return Main.deadPlayer;
						}
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
