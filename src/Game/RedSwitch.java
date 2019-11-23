package Game;

public class RedSwitch extends Thing {
	private int touchedLast;
	public RedSwitch(float x, float y) {
		super(x, y, "red switch", 3, 0);
		this.touchedLast = 0;
	}
	
	public void move() {
		if(Main.player.isTouching(this)) {
			if(this.touchedLast <= 0) {
				Main.isRedGateOpen = !Main.isRedGateOpen;
			}
			this.touchedLast = 5;
		} else {
			this.touchedLast -= 1;
		}
	}
}
