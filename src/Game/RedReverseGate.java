package Game;

public class RedReverseGate extends Thing {
	public RedReverseGate(float x, float y) {
		super(x, y, Main.isRedGateOpen ? "wall red reverse gate" : "open red reverse gate", 3, Main.isRedGateOpen ? 1 : 2);
	}
	
	public boolean move() {
		Main.removeFromMap(this);
		if(Main.isRedGateOpen && this.id.equals("open red reverse gate")) {
			this.id = "wall red reverse gate";
			this.pic = Main.texturedImg.getSubimage(3 * Main.SPRITE_WIDTH, 1 * Main.SPRITE_HEIGHT, Main.SPRITE_WIDTH, Main.SPRITE_HEIGHT);
		} else if(!Main.isRedGateOpen && this.id.equals("wall red reverse gate")){
			this.id = "open red reverse gate";
			this.pic = Main.texturedImg.getSubimage(3 * Main.SPRITE_WIDTH, 2 * Main.SPRITE_HEIGHT, Main.SPRITE_WIDTH, Main.SPRITE_HEIGHT);
		}
		Main.putInMap(this);
		return false;
	}
}
