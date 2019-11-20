package Game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Player extends Thing {

	public Player(float x, float y) throws IOException {
		super(x, y, "player", ImageIO.read(new File("player.png")));
		// TODO Auto-generated constructor stub
	}

	@Override
	void move(ArrayList<ArrayList<Thing>> map) {
		Main.removeFromMap(this);
		this.x += this.dx;
		this.dy += Main.GRAVITY;
		if(this.dy > 5) {
			this.dy = 5;
		}
		for(int i = 0; i <= 10 * (this.dy + this.pic.getHeight(null)); i++) {
			Thing a = Main.getFromMap((int) this.x, (int) (this.y + (i / 10)));
			if(a != null) {
				this.y = a.y - a.pic.getHeight(null);
				this.dy = 0;
				break;
			}
		}
		if(Main.getFromMap((int) this.x, (int) (this.y + this.dy)) != null) {
			this.y = Main.getFromMap((int) this.x, (int) (this.y + this.dy)).y - Main.getFromMap((int) this.x, (int) (this.y + this.dy)).pic.getHeight(null);
			this.dy = 0;
		}
		this.y += this.dy;
		
		Main.insertToMap(this);
	}
}
