package Game;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Hashtable;

public abstract class Thing {
	public Thing(float x, float y, String id, Image pic) {
		this.x = x;
		this.y = y;
		this.dx = 0;
		this.dy = 0;
		this.id = id;
		this.pic = pic;
		Main.insertToMap(this);
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
		return Math.abs(this.getX() - other.getX()) < 1 && Math.abs(this.getY() - other.getY()) < 1;
	}
	
	void display(Graphics g) {
		g.drawImage(this.pic, ((int)this.x) - Main.cameraX, ((int)this.y) - Main.cameraY, null);
	}
	
	void move() {
		
	}
	
	void die() {
		
	}
}
