package Game;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RedReverseGate extends Thing {
	public RedReverseGate(float x, float y) {
		super(x, y, Main.isRedGateOpen ? "wall red reverse gate" : "open red reverse gate", 3, Main.isRedGateOpen ? 1 : 2);
	}
	
	public boolean move() {
		Main.removeFromMap(this);
		if(Main.isRedGateOpen && this.id.equals("open red reverse gate")) {
			this.id = "wall red reverse gate";
			try {
				this.pic = ImageIO.read(new File("config/textures.png")).getSubimage(3 * Main.SPRITE_WIDTH, 1 * Main.SPRITE_HEIGHT, Main.SPRITE_WIDTH, Main.SPRITE_HEIGHT);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if(!Main.isRedGateOpen && this.id.equals("wall red reverse gate")){
			this.id = "open red reverse gate";
			try {
				this.pic = ImageIO.read(new File("config/textures.png")).getSubimage(3 * Main.SPRITE_WIDTH, 2 * Main.SPRITE_HEIGHT, Main.SPRITE_WIDTH, Main.SPRITE_HEIGHT);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Main.putInMap(this);
		return false;
	}
}
