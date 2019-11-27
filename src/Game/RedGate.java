package Game;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RedGate extends Thing {
	public RedGate(float x, float y) {
		super(x, y, Main.isRedGateOpen ? "open red gate" : "wall red gate", 3, Main.isRedGateOpen ? 2 : 1);
	}
	
	public void move() {
		Main.removeFromMap(this);
		if(Main.isRedGateOpen && this.id.equals("wall red gate")) {
			this.id = "open red gate";
			try {
				this.pic = ImageIO.read(new File("textures.png")).getSubimage(3 * Main.SPRITE_WIDTH, 2 * Main.SPRITE_HEIGHT, Main.SPRITE_WIDTH, Main.SPRITE_HEIGHT);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if(!Main.isRedGateOpen && this.id.equals("open red gate")){
			this.id = "wall red gate";
			try {
				this.pic = ImageIO.read(new File("textures.png")).getSubimage(3 * Main.SPRITE_WIDTH, 1 * Main.SPRITE_HEIGHT, Main.SPRITE_WIDTH, Main.SPRITE_HEIGHT);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Main.putInMap(this);
	}
}
