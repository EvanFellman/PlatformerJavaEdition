package Game;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public void paint(Graphics g) {
		Main.drawBackground(g, Main.cameraX, Main.cameraY);
		if(Main.deadPlayer) {
			for(int i = 0; i < Main.level.size(); i++) {
				if(Main.level.get(i).id.equals("player")) {
					continue;
				}
				double x = Main.level.get(i).getX() - Main.cameraX;
				double y = Main.level.get(i).getY() - Main.cameraY;
				if(x <= Main.window.getWidth() && x >= - 1* Main.window.getWidth() && y <= Main.window.getHeight() && y >= -1 * Main.window.getHeight()) {
					try {
						Main.level.get(i).display(g);
					} catch(Exception e) {
						
					}
				}
			}
			for(Player i: Main.player) {
				i.move();
				i.display(g);
			}
			Main.deadPlayerCounter --;
			g.setColor(new Color(255, 0, 0, 10 + (75 - Main.deadPlayerCounter)));
			g.fillRect(0, 0, (int) g.getClipBounds().getWidth(), (int) g.getClipBounds().getHeight());
			if(Main.deadPlayerCounter == 0) {
				Main.loadLevel();
			}
		} else {
			boolean playerDied = false;
			for(int i = 0; i < Main.level.size(); i++) {
				double x = Main.level.get(i).getX() - Main.cameraX;
				double y = Main.level.get(i).getY() - Main.cameraY;
				if(x <= Main.window.getWidth() && x >= - 1* Main.window.getWidth() && y <= Main.window.getHeight() && y >= -1 * Main.window.getHeight()) {
					if(!playerDied) {
						playerDied = Main.level.get(i).move();
					}
					try {
						Main.level.get(i).display(g);
					} catch(Exception e) { }
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
}
