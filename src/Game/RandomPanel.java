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
	private int nextX;
	private int nextY;
	public RandomPanel() {
		super();
		Main.deadPlayer = false;
		templates = new ArrayList<BufferedImage>();
		int i = 1;
		File f = new File("./config/" + Integer.toString(i) + ".png");
		while(f.exists()) {
			try {
				templates.add(ImageIO.read(f));
			} catch (IOException e) {
				break;
			}
			i++;
			f = new File("./config/" + Integer.toString(i) + ".png");
		}
		Player p = new Player(0, -25);
		Main.player = new ArrayList<Player>();
		Main.player.add(p);
		Main.putInMap(p);
		Main.level.add(p);
		Main.cameraY = (int) (p.getY() - (Main.window.getHeight() / 2));
		for(int j = 0; j < 7 * Main.SPRITE_WIDTH; j += Main.SPRITE_WIDTH) {
			Wall w = new Wall(j, 0);
			Main.putInMap(w);
			Main.level.add(w);
		}
		loadTemplate(7 * Main.SPRITE_WIDTH, -1 * Main.SPRITE_HEIGHT);		
	}
	public void paint(Graphics g) {
		Main.drawBackground(g, Main.cameraX, Main.cameraY);
		if(Main.deadPlayer) {
			for(int i = 0; i < Main.level.size(); i++) {
				if(Main.level.get(i).id.equals("player")) {
					continue;
				}
				Thing a = Main.level.get(i);
				double x = a.getX() - Main.cameraX;
				double y = a.getY() - Main.cameraY;
				if(x <= Main.window.getWidth() && x >= -1 * Main.window.getWidth() && y <= Main.window.getHeight() && y >= -1 * Main.window.getHeight()) {
					try {
						if(Main.level.get(i).getX() >= nextX) {
							loadTemplate(nextX, nextY);
						} else {
							Main.level.get(i).display(g);
						}
					} catch(Exception e) {
					}
				}
			}
			for(Player i: Main.player) {
				i.move();
				i.display(g);
			}
			Main.deadPlayerCounter --;
			if(Main.deadPlayerCounter == 0) {
				Main.STATE = "menu";
				Main.window.remove(Main.rp);
				Main.window.repaint();
			}
			g.setColor(new Color(255, 0, 0, 10 + (75 - Main.deadPlayerCounter)));
			g.fillRect(0, 0, (int) g.getClipBounds().getWidth(), (int) g.getClipBounds().getHeight());
		} else {
			boolean playerDied = false;
			for(int i = 0; i < Main.level.size(); i++) {
				Thing a = Main.level.get(i);
				double x = a.getX() - Main.cameraX;
				double y = a.getY() - Main.cameraY;
				if(x <= Main.window.getWidth() && x >= -1 * Main.window.getWidth() && y <= Main.window.getHeight() && y >= -1 * Main.window.getHeight()) {
					if(a.id.equals("player") || a.id.contains("enemy") && !a.equals(Main.getFromMapMoving(a.getX(), a.getY()))) {
						Main.putInMap(a);
					} else if(!a.equals(Main.getFromMapMoving(a.getX(), a.getY()))) {
						Main.putInMap(a);
					}
					playerDied = playerDied || Main.level.get(i).move();
					try {
						if(Main.level.get(i).getX() >= nextX) {
							loadTemplate(nextX + Main.SPRITE_WIDTH, nextY);
						} else {
							Main.level.get(i).display(g);
						}
					} catch(Exception e) {
					}
				}
			}
			for(Player i: Main.player) {
				i.display(g);
			}
			if(Main.deadPlayer) {
				for(Player i: Main.player) {
					i.dy = -15;
					i.dx = 0;
				}
			}
		}
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
				} else if(pixel.getRed() == 0 && pixel.getGreen() == 253 && pixel.getBlue() == 0) {
					templateLevel.add(new WallMoving(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y, WallMoving.UP, Main.SLOW_SPEED));
				} else if(pixel.getRed() == 0 && pixel.getGreen() == 252 && pixel.getBlue() == 0) {
					templateLevel.add(new WallMoving(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y, WallMoving.DOWN, Main.SLOW_SPEED));
				} else if(pixel.getRed() == 0 && pixel.getGreen() == 251 && pixel.getBlue() == 0) {
					templateLevel.add(new WallMoving(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y, WallMoving.LEFT, Main.SLOW_SPEED));
				} else if(pixel.getRed() == 0 && pixel.getGreen() == 250 && pixel.getBlue() == 0) {
					templateLevel.add(new WallMoving(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y, WallMoving.RIGHT, Main.SLOW_SPEED));
				} else if(pixel.getRed() == 0 && pixel.getGreen() == 249 && pixel.getBlue() == 0) {
					templateLevel.add(new WallMoving(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y, WallMoving.UP, Main.FAST_SPEED));
				} else if(pixel.getRed() == 0 && pixel.getGreen() == 248 && pixel.getBlue() == 0) {
					templateLevel.add(new WallMoving(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y, WallMoving.DOWN, Main.FAST_SPEED));
				} else if(pixel.getRed() == 0 && pixel.getGreen() == 247 && pixel.getBlue() == 0) {
					templateLevel.add(new WallMoving(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y, WallMoving.LEFT, Main.FAST_SPEED));
				} else if(pixel.getRed() == 0 && pixel.getGreen() == 246 && pixel.getBlue() == 0) {
					templateLevel.add(new WallMoving(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y, WallMoving.RIGHT, Main.FAST_SPEED));
				} else if (pixel.getRed() == 245 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
					templateLevel.add(new Spike(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y));
				} else if (pixel.getRed() == 244 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
					templateLevel.add(new EnemySmart(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y, Main.FAST_SPEED));
				} else if (pixel.getRed() == 243 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
					templateLevel.add(new EnemySmart(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y, Main.SLOW_SPEED));
				}else if(pixel.getRed() == 242 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
					templateLevel.add(new EnemyBullet(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y, EnemyBullet.LEFT, Main.FAST_SPEED));
				} else if(pixel.getRed() == 241 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
					templateLevel.add(new EnemyBullet(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y, EnemyBullet.LEFT, Main.SLOW_SPEED));
				} else if(pixel.getRed() == 240 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
					templateLevel.add(new EnemyBullet(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y, EnemyBullet.RIGHT, Main.FAST_SPEED));
				} else if(pixel.getRed() == 239 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
					templateLevel.add(new EnemyBullet(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y, EnemyBullet.RIGHT, Main.SLOW_SPEED));
				} else if(pixel.getRed() == 238 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
					templateLevel.add(new EnemyBullet(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y, EnemyBullet.UP, Main.FAST_SPEED));
				} else if(pixel.getRed() == 237 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
					templateLevel.add(new EnemyBullet(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y, EnemyBullet.UP, Main.SLOW_SPEED));
				} else if(pixel.getRed() == 236 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
					templateLevel.add(new EnemyBullet(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y, EnemyBullet.DOWN, Main.FAST_SPEED));
				} else if(pixel.getRed() == 235 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
					templateLevel.add(new EnemyBullet(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y, EnemyBullet.DOWN, Main.SLOW_SPEED));
				} else if(pixel.getRed() == 234 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
					templateLevel.add(new Shooter(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y, Shooter.LEFT, Main.FAST_SPEED));
				} else if(pixel.getRed() == 233 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
					templateLevel.add(new Shooter(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y, Shooter.LEFT, Main.SLOW_SPEED));
				} else if(pixel.getRed() == 232 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
					templateLevel.add(new Shooter(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y, Shooter.RIGHT, Main.FAST_SPEED));
				} else if(pixel.getRed() == 231 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
					templateLevel.add(new Shooter(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y, Shooter.RIGHT, Main.SLOW_SPEED));
				} else if(pixel.getRed() == 230 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
					templateLevel.add(new Shooter(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y, Shooter.UP, Main.FAST_SPEED));
				} else if(pixel.getRed() == 229 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
					templateLevel.add(new Shooter(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y, Shooter.UP, Main.SLOW_SPEED));
				} else if(pixel.getRed() == 228 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
					templateLevel.add(new Shooter(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y, Shooter.DOWN, Main.FAST_SPEED));
				} else if(pixel.getRed() == 227 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
					templateLevel.add(new Shooter(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y, Shooter.DOWN, Main.SLOW_SPEED));
				} else if(pixel.getRed() == 226 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
					templateLevel.add(new Shield(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y));
				} else if(pixel.getRed() == 225 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
					templateLevel.add(new DoubleJump(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y));
				} else if(pixel.getRed() == 224 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
					templateLevel.add(new SpikeDestroyer(Main.SPRITE_WIDTH * x, Main.SPRITE_HEIGHT * y));
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
