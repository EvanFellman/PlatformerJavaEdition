package Game;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public abstract class Thing {
	private static int nextID = 0;
	private int uniqueID;
	protected double x, y, dx, dy;
	public String id;
	protected Image pic;
	
	public Thing(double x, double y, String id, int picX, int picY) {
		this.x = x;
		this.y = y;
		this.dx = 0;
		this.dy = 0;
		this.id = id;
		this.uniqueID = nextID++;
		try {
			this.pic = ImageIO.read(new File("textures.png")).getSubimage(picX * Main.SPRITE_WIDTH, picY * Main.SPRITE_HEIGHT, Main.SPRITE_WIDTH, Main.SPRITE_HEIGHT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Main.putInMap(this);
	}
	
	public double getX() { return this.x; }
	public double getY() { return this.y; }
	public double getDx() {	return this.dx; }
	public double getDy() {	return this.dy;	}
	public void setDx(double dx) { this.dx = dx; }
	public void setDy(double dy) { this.dy = dy; }
	
	public int getUniqueID() {
		return this.uniqueID;
	}
	
	public boolean isTouching(Thing other) {
		if(other == null) {
			return false;
		}
		double x = this.x - other.getX();
		double y = this.y - other.getY();
		return x < Main.SPRITE_WIDTH && x > -1 * Main.SPRITE_WIDTH && y < Main.SPRITE_HEIGHT && y > -1 * Main.SPRITE_HEIGHT; 
	}
	
	public boolean isNextTo(Thing other) {
		if(other == null) {
			return false;
		}
		double x = this.x - other.getX();
		double y = this.y - other.getY();
		return x <= Main.SPRITE_WIDTH && x >= -1 * Main.SPRITE_WIDTH && y <= Main.SPRITE_HEIGHT && y >= -1 * Main.SPRITE_HEIGHT; 
	}
	
	public void display(Graphics g) {
		g.drawImage(this.pic, ((int)this.x) - Main.cameraX, ((int)this.y) - Main.cameraY, null);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Thing) {
			Thing other = (Thing) o;
			return other.uniqueID == this.uniqueID;
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return this.id + " (" + Double.toString(this.x) + ", " + Double.toString(this.y) + ")";  
	}
	
	public boolean move() { return false; }
	public void die() {}
}
