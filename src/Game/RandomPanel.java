package Game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class RandomPanel extends javax.swing.JPanel {
	private static final long serialVersionUID = 1L;
	private ArrayList<BufferedImage> templates;
	public RandomPanel() {
		super();
		templates = new ArrayList<BufferedImage>();
		int i = 0;
		File f = new File("./templates/" + Integer.toString(i) + ".png");
		while(f.exists()) {
			try {
				templates.add(ImageIO.read(f));
			} catch (IOException e) {
				break;
			}
			i++;
			f = new File("./templates/" + Integer.toString(i) + ".png");
		}
	}
	public int nextX = 0;
	public int nextY = 0;
	public void paint(Graphics g) {
		g.setColor(Color.CYAN);
		g.fillRect(0, 0, (int)g.getClipBounds().getWidth(), (int)g.getClipBounds().getHeight());
		boolean playerDied = false;
		for(int i = 0; i < Main.level.size(); i++) {
			Thing a = Main.level.get(i);
			double x = a.getX() - Main.cameraX;
			double y = a.getY() - Main.cameraY;
			if(x <= Main.window.getWidth() && x >= -1 * Main.window.getWidth() && y <= Main.window.getHeight() && y >= -1 * Main.window.getHeight()) {
				if(!a.equals(Main.getFromMap(a.getX(), a.getY()))) {
					Main.putInMap(a);
				}
				if(!playerDied) {
					playerDied = Main.level.get(i).move();
				}
				try {
					if(Main.level.get(i).getX() >= nextX) {
						loadTemplate(nextX, nextY);
					} else {
						Main.level.get(i).display(g);
					}
				} catch(Exception e) {
					System.out.println(e);
				}
			}
		}
		Main.player.display(g);
	}
	
	public void loadTemplate(int startX, int startY) {
		BufferedImage img = templates.get((int) (templates.size() * Math.random()));
		int tStartY = 0;
		ArrayList<Thing> templateLevel = new ArrayList<Thing>();
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				Color pixel = new Color(img.getRGB(x, y));
				if (pixel.getRed() == 0 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
					templateLevel.add(new Wall(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y));
				} else if (pixel.getRed() == 0 && pixel.getGreen() == 255 && pixel.getBlue() == 0) {
					nextX = Main.SPRITE_WIDTH * x;
					nextY = Main.SPRITE_HEIGHT * y;
				} else if (pixel.getRed() == 0 && pixel.getGreen() == 0 && pixel.getBlue() == 255) {
					tStartY = Main.SPRITE_HEIGHT * y;
				} else if (pixel.getRed() == 255 && pixel.getGreen() == 255 && pixel.getBlue() == 2) {
					templateLevel.add(new EnemyNoJump(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y, Main.FAST_SPEED));
				} else if (pixel.getRed() == 255 && pixel.getGreen() == 255 && pixel.getBlue() == 1) {
					templateLevel.add(new EnemyNoJump(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y, Main.SLOW_SPEED));
				} else if (pixel.getRed() == 255 && pixel.getGreen() == 255 && pixel.getBlue() == 3) {
					templateLevel.add(new EnemyOnlyJump(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y));
				} else if (pixel.getRed() == 0 && pixel.getGreen() == 0 && pixel.getBlue() == 254) {
					templateLevel.add(new BlueGate(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y));
				} else if (pixel.getRed() == 0 && pixel.getGreen() == 0 && pixel.getBlue() == 253) {
					templateLevel.add(new BlueReverseGate(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y));
				} else if (pixel.getRed() == 0 && pixel.getGreen() == 0 && pixel.getBlue() == 252) {
					templateLevel.add(new BlueSwitch(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y));
				} else if (pixel.getRed() == 254 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
					templateLevel.add(new RedGate(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y));
				} else if (pixel.getRed() == 253 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
					templateLevel.add(new RedReverseGate(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y));
				} else if (pixel.getRed() == 252 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
					templateLevel.add(new RedSwitch(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y));
				} else if (pixel.getRed() == 251 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
					templateLevel.add(new EnemyDumb(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y, true, Main.FAST_SPEED));
				} else if (pixel.getRed() == 250 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
					templateLevel.add(new EnemyDumb(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y, true, Main.SLOW_SPEED));
				} else if (pixel.getRed() == 248 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
					templateLevel.add(new EnemyDumb(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y, false, Main.FAST_SPEED));
				} else if (pixel.getRed() == 247 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
					templateLevel.add(new EnemyDumb(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y, false, Main.SLOW_SPEED));
				} else if (pixel.getRed() == 0 && pixel.getGreen() == 254 && pixel.getBlue() == 0) {
					templateLevel.add(new DisappearingWall(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y));
				} else if (pixel.getRed() == 245 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
					templateLevel.add(new Spike(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y));
				} else if (pixel.getRed() == 244 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
					templateLevel.add(new EnemySmart(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y, Main.FAST_SPEED));
				} else if (pixel.getRed() == 243 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
					templateLevel.add(new EnemySmart(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y, Main.SLOW_SPEED));
				}
			}
		}
		for(Thing i: templateLevel) {
			i.x += startX;
			i.y += startY - tStartY;
			if(i.y + Main.SPRITE_WIDTH > Main.DEATH_BELOW) {
				Main.DEATH_BELOW = (int) (i.y + Main.SPRITE_WIDTH);
			}
			Main.putInMap(i);
			Main.level.add(i);
		}
		nextX += startX;
		nextY += startY - tStartY;
	}
}