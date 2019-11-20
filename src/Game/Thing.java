package Game;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

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
	
	float x, y, dx, dy;
	String id;
	Image pic;
	
	float getX() { return this.x; }
	float getY() { return this.y; }
	float getDx() {	return this.dx; }
	float getDy() {	return this.dy;	}	
	void setDx(float dx) { this.dx = dx; }
	void setDy(float dy) { this.dy = dy; }
	
	boolean isTouching(Thing other) {
		return Math.abs(this.getX() - other.getX()) < 1 && Math.abs(this.getY() - other.getY()) < 1;
	}
	
	void display(Graphics g) {
		g.drawImage(this.pic, ((int)this.x) - Main.cameraX, ((int)this.y) - Main.cameraY, null);
	}
	
	abstract void move(ArrayList<ArrayList<Thing>> map);
}
