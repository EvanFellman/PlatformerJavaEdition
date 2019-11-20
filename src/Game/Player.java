package Game;

import java.awt.Rectangle;
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
		if(this.dy > 5) {
			this.dy = 5;
		}
		this.dx = 0;
		if(Main.isAPressed) {
			this.dx = -1f;
		}
		if(Main.isDPressed) {
			this.dx = 1f;
		}
		this.x += this.dx;
		for(Thing i: Main.level){
			if(i.id.equals("wall") && (new Rectangle((int)this.getX(), (int)this.getY(), Main.SPRITE_WIDTH, Main.SPRITE_HEIGHT)).intersects(new Rectangle((int)i.getX(), (int)i.getY(), Main.SPRITE_WIDTH, Main.SPRITE_HEIGHT))) {
				if(this.dx > 0) {
					this.dx = 0;
					this.x = i.x - Main.SPRITE_WIDTH;
					break;
				} else if(this.dx < 0) {
					this.dx = 0;
					this.x = i.x + Main.SPRITE_WIDTH;
					break;
				}
			}
		}
		this.dy += Main.GRAVITY;
		this.y += this.dy;
		for(Thing i: Main.level){
			if(i.id.equals("wall") && (new Rectangle((int)this.getX(), (int)this.getY(), Main.SPRITE_WIDTH, Main.SPRITE_HEIGHT)).intersects(new Rectangle((int)i.getX(), (int)i.getY(), Main.SPRITE_WIDTH, Main.SPRITE_HEIGHT))) {
				if(this.dy > 0) {
					if(Main.isWPressed) {
						this.dy = -2;
					} else {
						this.dy = 0;
					}
					this.y = i.y - Main.SPRITE_HEIGHT;
					break;
				} else if(this.dy < 0) {
					this.dy = 0;
					this.y = i.y + Main.SPRITE_HEIGHT;
					break;
				}
			}
		}
		Main.insertToMap(this);
	}
}
