package Game;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public abstract class Thing {
	public Thing(float x, float y, String id, int picX, int picY) {
		this.x = x;
		this.y = y;
		this.dx = 0;
		this.dy = 0;
		this.id = id;
		try {
			this.pic = ImageIO.read(new File("textures.png")).getSubimage(picX * Main.SPRITE_WIDTH, picY * Main.SPRITE_HEIGHT, Main.SPRITE_WIDTH, Main.SPRITE_HEIGHT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Main.putInMap(this);
	}
	
	protected float x, y, dx, dy;
	String id;
	Image pic;
	
	public float getX() { return this.x; }
	public float getY() { return this.y; }
	public float getDx() {	return this.dx; }
	public float getDy() {	return this.dy;	}	
	public void setDx(float dx) { this.dx = dx; }
	public void setDy(float dy) { this.dy = dy; }
	
	boolean isTouching(Thing other) {
		return (new Rectangle((int)this.getX(), (int)this.getY(), Main.SPRITE_WIDTH, Main.SPRITE_HEIGHT)).intersects(new Rectangle((int)other.getX(), (int)other.getY(), Main.SPRITE_WIDTH, Main.SPRITE_HEIGHT));
	}
	
	void display(Graphics g) {
		g.drawImage(this.pic, ((int)this.x) - Main.cameraX, ((int)this.y) - Main.cameraY, null);
	}
	
	boolean equals(Thing other) {
		return other.id.equals(this.id) && (other.getX() == this.getX()) && (other.getDx() == this.getDx()) && (this.getY() == other.getY()) && (this.getDy() == other.getDy());
	}
	
	void move() { }
	void die() { }
}
