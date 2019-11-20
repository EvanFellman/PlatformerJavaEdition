package Game;
import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFrame;

public class Main {
	public static Thing[] thingsToDraw = new Thing[2];
	public static ArrayList<ArrayList<Thing>> map = new ArrayList<ArrayList<Thing>>();
	public static int cameraX = 0;
	public static int cameraY = 0;
	public static final float GRAVITY = 0.005f;
	public static void main(String[] args) throws IOException, InterruptedException {
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(new Dimension(500,500));
		Thing wall = new Wall(50, 200);
		Thing player = new Player(50, 50);
		thingsToDraw[1] = player;
		thingsToDraw[0] = wall;
		window.add(new GamePanel());
		window.setVisible(true);
		while(true) {
			Thread.sleep(1 / 15);
			window.repaint();
			System.out.println(player.y);
		}
	}
	
	public static void insertToMap(Thing u) {
		while(map.size() <= (int) u.x) {
			map.add(new ArrayList<Thing>());
		}
		while(map.get((int) u.x).size() <= (int) u.y) {
			map.get((int) u.x).add(null);
		}
		map.get((int) u.x).set((int) u.y, u);
	}
	
	public static void removeFromMap(Thing u) {
		map.get((int) u.x).set((int) u.y, null);
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
}
