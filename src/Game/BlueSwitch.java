package Game;

public class BlueSwitch extends Thing {
	private int touchedLast;
	public BlueSwitch(float x, float y) {
		super(x, y, "blue switch", 1, 2);
		this.touchedLast = 0;
	}
	
	public void move() {
		if(Main.player.isTouching(this)) {
			if(this.touchedLast <= 0) {
				Main.isBlueGateOpen = !Main.isBlueGateOpen;
			}
			this.touchedLast = 5;
		} else {
			this.touchedLast -= 1;
		}
	}
}
