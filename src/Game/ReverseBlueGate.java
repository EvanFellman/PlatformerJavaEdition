package Game;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ReverseBlueGate extends Thing {
	public ReverseBlueGate(float x, float y) {
		super(x, y, Main.isBlueGateOpen ? "wall blue gate" : "open blue gate", 2, Main.isBlueGateOpen ? 1 : 2);
	}
	
	public void move() {
		if(Main.isBlueGateOpen && this.id.equals("open blue gate")) {
			this.id = "wall blue gate";
			try {
				this.pic = ImageIO.read(new File("textures.png")).getSubimage(2 * Main.SPRITE_WIDTH, 1 * Main.SPRITE_HEIGHT, Main.SPRITE_WIDTH, Main.SPRITE_HEIGHT);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if(!Main.isBlueGateOpen && this.id.equals("wall blue gate")){
			this.id = "open blue gate";
			try {
				this.pic = ImageIO.read(new File("textures.png")).getSubimage(2 * Main.SPRITE_WIDTH, 2 * Main.SPRITE_HEIGHT, Main.SPRITE_WIDTH, Main.SPRITE_HEIGHT);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
