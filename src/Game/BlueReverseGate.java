package Game;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BlueReverseGate extends Thing {
	public BlueReverseGate(float x, float y) {
		super(x, y, Main.isBlueGateOpen ? "wall blue reverse gate" : "open blue reverse gate", 2, Main.isBlueGateOpen ? 1 : 2);
	}
	
	public boolean move() {
		Main.removeFromMap(this);
		if(Main.isBlueGateOpen && this.id.equals("open blue reverse gate")) {
			this.id = "wall blue reverse gate";
			try {
				this.pic = ImageIO.read(new File("textures.png")).getSubimage(2 * Main.SPRITE_WIDTH, 1 * Main.SPRITE_HEIGHT, Main.SPRITE_WIDTH, Main.SPRITE_HEIGHT);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if(!Main.isBlueGateOpen && this.id.equals("wall blue reverse gate")){
			this.id = "open blue reverse gate";
			try {
				this.pic = ImageIO.read(new File("textures.png")).getSubimage(2 * Main.SPRITE_WIDTH, 2 * Main.SPRITE_HEIGHT, Main.SPRITE_WIDTH, Main.SPRITE_HEIGHT);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Main.putInMap(this);
		return false;
	}
}
