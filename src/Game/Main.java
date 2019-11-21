package Game;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Main {
	public static ArrayList<Thing> level = new ArrayList<Thing>();
	public static int startX;
	public static int startY;
	public static int levelNumber;
	public static int cameraX = 0;
	public static int cameraY = 0;
	public static int DEATH_BELOW;
	public static boolean isWPressed = false;
	public static boolean isAPressed = false;
	public static boolean isDPressed = false;
	public static final float GRAVITY = 0.02f;
	public static final int SPRITE_HEIGHT = 25;
	public static final int SPRITE_WIDTH = 25;
	public static Player player;
	public static void main(String[] args) throws IOException, InterruptedException {
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(new Dimension(500,500));
		levelNumber = 1;
		loadLevel();
		GamePanel gp = new GamePanel();
		window.addKeyListener(new MKeyListener());
		window.add(gp);
		window.setVisible(true);
		while(true) {
			Thread.sleep(1000 / 360);
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
	
	//resets level
	public static void loadLevel() {
		File imgFile = new File("./level" + Integer.toString(levelNumber) + ".png");
		level = new ArrayList<Thing>();
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
						}
					}
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
	
	@Override
	public void keyReleased(KeyEvent event) {
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
