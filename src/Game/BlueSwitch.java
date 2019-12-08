package Game;

public class BlueSwitch extends Thing {
	private int touchedLast;
	public BlueSwitch(float x, float y) {
		super(x, y, "blue wall switch", 2, 0);
		this.touchedLast = 0;
	}
	
	public boolean move() {
		boolean wasTouched = false;
		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 1; j++) {
				Thing a = Main.getFromMap(this.x + (i * Main.SPRITE_WIDTH), this.y + (j * Main.SPRITE_HEIGHT));
				if(a != null && this.isNextTo(a) && (a.id.equals("player") || a.id.contains("enemy"))) {
					if(this.touchedLast <= 0) {
						wasTouched = true;
						Main.isBlueGateOpen = !Main.isBlueGateOpen;
					}
					this.touchedLast = 5;
				}
			}
		}
		if(!wasTouched) {
			this.touchedLast -= 1;
		}
		return false;
	}
}
