package Game;
import java.awt.Graphics;
import java.awt.Image;
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
		if(other == null) {
			return false;
		}
		float x = this.x - other.getX();
		float y = this.y - other.getY();
		return x < Main.SPRITE_WIDTH && x > -1 * Main.SPRITE_WIDTH && y < Main.SPRITE_HEIGHT && y > -1 * Main.SPRITE_HEIGHT; 
	}
	
	void display(Graphics g) {
		g.drawImage(this.pic, ((int)this.x) - Main.cameraX, ((int)this.y) - Main.cameraY, null);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Thing) {
			Thing other = (Thing) o;
			return other.id.equals(this.id) && (other.getX() == this.getX()) && (other.getDx() == this.getDx()) && (this.getY() == other.getY()) && (this.getDy() == other.getDy());
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return this.id + " (" + Float.toString(this.x) + ", " + Float.toString(this.y) + ")";  
	}
	
	void move() {}
	void die() {}
}
