package Game;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFrame;

public class Main {
	public static ArrayList<Thing> level = new ArrayList<Thing>();
	public static ArrayList<ArrayList<Thing>> map = new ArrayList<ArrayList<Thing>>();
	public static int cameraX = 0;
	public static int cameraY = 0;
	public static boolean isWPressed = false;
	public static boolean isAPressed = false;
	public static boolean isDPressed = false;
	public static final float GRAVITY = 0.005f;
	public static final int SPRITE_HEIGHT = 25;
	public static final int SPRITE_WIDTH = 25;
	public static void main(String[] args) throws IOException, InterruptedException {
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(new Dimension(500,500));
		Player player = new Player(50, 200);
		level.add(player);
		level.add(new Wall(50, 150));
		for(int i = 0; i < 500; i+=25) {
			level.add(new Wall(i, 250));
		}
		level.add(new Wall(300, 225));
		level.add(new Wall(300, 200));
		level.add(new Wall(300, 175));
		for(Thing i: level) {
			insertToMap(i);
		}
		GamePanel gp = new GamePanel();
		window.addKeyListener(new MKeyListener());
		window.add(gp);
		window.setVisible(true);
		while(true) {
			Thread.sleep(1 / 15);
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
	
	public static void insertToMap(Thing u) {
		while(map.size() <= (int) u.getX()) {
			map.add(new ArrayList<Thing>());
		}
		while(map.get((int) u.getX()).size() <= (int) u.getY()) {
			map.get((int) u.getX()).add(null);
		}
		map.get((int) u.getX()).set((int) u.getY(), u);
	}
	
	public static void removeFromMap(Thing u) {
		map.get((int) u.getX()).set((int) u.getY(), null);
	}
	
	public static Thing getFromMap(int x, int y) {
		if(map.size() <= x) {
			return null;
		} else if(map.get(x).size() <= y) {
			return null;
		} else {
			return map.get(x).get(y);
		}
	}
	
	public static Thing getFromMap(float x, float y) {
		if(map.size() <= ((int) x)) {
			return null;
		} else if(map.get((int) x).size() <= ((int) y)) {
			return null;
		} else {
			return map.get((int) x).get((int) y);
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
