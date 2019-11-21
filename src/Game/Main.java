package Game;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Main {
	public static ArrayList<Thing> level = new ArrayList<Thing>();
	public static Hashtable<Integer, Hashtable<Integer, Thing>> levelMap; 
	public static int startX;
	public static int startY;
	public static int levelNumber;
	public static int cameraX = 0;
	public static int cameraY = 0;
	public static int DEATH_BELOW;
	public static boolean isWPressed = false;
	public static boolean isAPressed = false;
	public static boolean isDPressed = false;
	public static boolean isEscapePressed = false;
	public static final float GRAVITY = 0.02f;
	public static final int SPRITE_HEIGHT = 25;
	public static final int SPRITE_WIDTH = 25;
	public static Player player;
	public static String STATE = "menu";
	
	public static void main(String[] args) throws IOException, InterruptedException {
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel playLabel = new JLabel("Play");
		playLabel.addMouseListener(new MouseAdapter() {
	        public void mouseClicked(MouseEvent e) {
	        	window.remove(playLabel);
	        	STATE="play";
	        }
		});
		window.setVisible(true);
		MKeyListener keyListener = new MKeyListener();
		while(true) {
			Thread.sleep(1/30);
			switch(STATE) {
			case "menu":
				window.setSize(200,100);
				window.add(playLabel);
				STATE="menu0";
				break;
			case "play":
				window.setSize(500,500);
				STATE="play0";
				levelNumber = 1;
				loadLevel();
				GamePanel gp = new GamePanel();
				window.addKeyListener(keyListener);
				window.add(gp);
				window.setVisible(true);
				while(true) {
					if(isEscapePressed) {
						JLabel quitLabel = new JLabel("paused - Press w to quit or escape to continue");
						window.add(quitLabel);
						while(isEscapePressed) { Thread.sleep(50); }
						while(!isEscapePressed && !isWPressed) { Thread.sleep(50); }
						if(isWPressed) {
							window.remove(quitLabel);
							window.remove(gp);
							window.removeKeyListener(keyListener);
							STATE = "menu";
						}
					}
					Thread.sleep(1000 / 300);
					while(player.getX() - cameraX < 100) {
						cameraX --;
					}
					while(player.getX() - cameraX > 400) {
						cameraX ++;
					}
					while(player.getY() - cameraY < 100) {
						cameraY --;
					}
					while(player.getY() - cameraY > 400) {
						cameraY ++;
					}
					window.repaint();
				}				
			}
		}
	}
	
	public static Thing getFromMap(int x, int y) {
		if(levelMap.get(x) == null) {
			return null;
		} else {
			return levelMap.get(x).get(y);
		}
	}
	
	public static Thing getFromMap(float x, float y) {
		return getFromMap((int) x, (int) y);
	}
	
	public static void putInMap(Thing a) {
		if(levelMap.get((int) a.getX()) == null) {
			levelMap.put((int) a.getX(), new Hashtable<Integer, Thing>());
			levelMap.get((int) a.getX()).put((int) a.getY(), a);
		} else {
			levelMap.get((int) a.getX()).put((int) a.getY(), a);
		}
	}
	
	public static void removeFromMap(Thing a) {
		levelMap.get((int) a.getX()).remove((int) a.getY());
	}
	
	//resets level
	public static void loadLevel() {
		File imgFile = new File("./level" + Integer.toString(levelNumber) + ".png");
		level = new ArrayList<Thing>();
		levelMap = new Hashtable<Integer, Hashtable<Integer, Thing>>();
		if(imgFile.exists()) {
			try {
				BufferedImage img = ImageIO.read(imgFile);
				DEATH_BELOW = img.getHeight() * SPRITE_HEIGHT;
				for(int x = 0; x < img.getWidth(); x++) {
					for(int y = 0; y < img.getHeight(); y++) {
						Color pixel = new Color(img.getRGB(x, y));
						if(pixel.getRed() == 0 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new Wall(SPRITE_WIDTH * x, SPRITE_HEIGHT * y));
						} else if(pixel.getRed() == 0 && pixel.getGreen() == 255 && pixel.getBlue() == 0) {
							level.add(new NextLevel(SPRITE_WIDTH * x, SPRITE_HEIGHT * y));
						} else if(pixel.getRed() == 0 && pixel.getGreen() == 0 && pixel.getBlue() == 255) {
							startX = SPRITE_WIDTH * x;
							startY = SPRITE_HEIGHT * y;
							player = new Player(SPRITE_WIDTH * x, SPRITE_HEIGHT * y);
							level.add(player);
						} else if(pixel.getRed() == 255 && pixel.getGreen() == 255 && pixel.getBlue() == 2) {
							level.add(new EnemyNoJump(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, 0.4f));
						} else if(pixel.getRed() == 255 && pixel.getGreen() == 255 && pixel.getBlue() == 1) {
							level.add(new EnemyNoJump(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, 0.25f));
						} else if(pixel.getRed() == 255 && pixel.getGreen() == 255 && pixel.getBlue() == 0) {
							level.add(new EnemyNoJump(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, 0.1f));
						}
					}
				}
				for(Thing i: level) {
					putInMap(i);
				}
			} catch (IOException e) {
				System.exit(0);
			}			
		} else {
			System.exit(0);
		}
	}
}

class MKeyListener extends KeyAdapter {
	@Override
	public void keyPressed(KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.VK_ESCAPE) {
			Main.isEscapePressed = true;
		} else {
		    switch(event.getKeyChar()) {
		    case 'w':
		    	Main.isWPressed = true;
		    	break;
		    case 'a':
		    	Main.isAPressed = true;
		    	break;
		    case 'd':
		    	Main.isDPressed = true;
		    	break;
		    default:
		    	break;
		    }
		}
	}
	
	@Override
	public void keyReleased(KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.VK_ESCAPE) {
			Main.isEscapePressed = false;
		} else {
		    switch(event.getKeyChar()) {
		    case 'w':
		    	Main.isWPressed = false;
		    	break;
		    case 'a':
		    	Main.isAPressed = false;
		    	break;
		    case 'd':
		    	Main.isDPressed = false;
		    	break;
		    default:
		    	break;
		    }
		}
	}
}
