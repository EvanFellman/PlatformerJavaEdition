package Game;

public class BlueGate extends Thing {

	public BlueGate(float x, float y) {
		super(x, y, Main.isBlueGateOpen ? "open blue gate" : "wall blue gate", 2, Main.isBlueGateOpen ? 2 : 1);
	}
	
	public boolean move() {
		Main.removeFromMap(this);
		if(Main.isBlueGateOpen && this.id.equals("wall blue gate")) {
			this.id = "open blue gate";
			this.pic = Main.texturedImg.getSubimage(2 * Main.SPRITE_WIDTH, 2 * Main.SPRITE_HEIGHT, Main.SPRITE_WIDTH, Main.SPRITE_HEIGHT);
		} else if(!Main.isBlueGateOpen && this.id.equals("open blue gate")){
			this.id = "wall blue gate";
			this.pic = Main.texturedImg.getSubimage(2 * Main.SPRITE_WIDTH, 1 * Main.SPRITE_HEIGHT, Main.SPRITE_WIDTH, Main.SPRITE_HEIGHT);
		}
		Main.putInMap(this);
		return false;
	}
}
