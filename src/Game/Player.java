package Game;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

public class Player extends Thing {

	public Player(float x, float y) throws IOException {
		super(x, y, "player", ImageIO.read(new File("player.png")));
		// TODO Auto-generated constructor stub
	}

	@Override
	void move(Hashtable<Integer, Hashtable<Integer, Thing>> map) {
		Main.removeFromMap(this);
		this.dy += Main.GRAVITY;
		if(this.dy > 5) {
			this.dy = 5;
		}
		if(Main.isAPressed) {
			this.dx = -1;
			for(int i = (int) (this.dx - Main.SPRITE_WIDTH); i <= 0; i++) {
				for(int j = 0; j <= Main.SPRITE_HEIGHT; j++) {
					Thing a = Main.getFromMap(this.x + i, this.y - j);
					if(a != null && a.id.equals("wall")) {
						this.x = a.x + Main.SPRITE_WIDTH;
						this.dx = 0;
					}
				}
			}
		} else if(Main.isDPressed) {
			this.dx = 1;
			for(int i = 0; i <= this.dx + Main.SPRITE_WIDTH; i++) {
				for(int j = 0; j <= Main.SPRITE_HEIGHT; j++) {
					Thing a = Main.getFromMap(this.x + i, this.y - j);
					if(a != null && a.id.equals("wall")) {
						this.x = -1 + (a.x - Main.SPRITE_WIDTH);
						this.dx = 0;
					}
				}
			}
		} else {
			this.dx = 0;
		}
		this.x += this.dx;
		if(dy >= 0) {
			for(int i = 0; i <= (this.dy + Main.SPRITE_HEIGHT); i++) {
				for(int j = -1 * (int) Main.SPRITE_WIDTH; j < Main.SPRITE_WIDTH; j++) {
					Thing a = Main.getFromMap(this.x - j, this.y + i);
					if(a != null && a.id.equals("wall")) {
						this.y = a.y - Main.SPRITE_HEIGHT;
						if(Main.isWPressed) {
							this.dy = -1;
						} else {
							this.dy = 0;
						}
						break;
					}
				}				
			}
		} else {
			for(int i = (int) this.dy - Main.SPRITE_HEIGHT; i <= 0; i++) {
				for(int j = -1 * (int) ( Main.SPRITE_WIDTH); j < Main.SPRITE_WIDTH; j++) {
					Thing a = Main.getFromMap(this.x - j, this.y + i);
					if(a != null && a.id.equals("wall")) {
						this.y = a.y + Main.SPRITE_HEIGHT;
						break;
					}
				}
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
