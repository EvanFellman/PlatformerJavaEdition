package Game;
import java.awt.Graphics;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public void paint(Graphics g) {
		Main.drawBackground(g, Main.cameraX, Main.cameraY);
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
				} catch(Exception e) {
					
				}
			}
		}
		for(Player i: Main.player) {
			i.display(g);
		}
	}
}
