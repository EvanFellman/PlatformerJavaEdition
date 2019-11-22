package Game;

import java.awt.Graphics;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public void paint(Graphics g) {
		for(int i = 0; i < Main.level.size(); i++) {
			
			if(Main.level.get(i).getX() <= Main.cameraX + Main.window.getWidth() && Main.level.get(i).getX() >= Main.cameraX - Main.window.getWidth() && Main.level.get(i).getY() <= Main.cameraY + Main.window.getHeight() && Main.level.get(i).getY() >= Main.cameraY - Main.window.getHeight()) {
				Main.level.get(i).move();
				Main.level.get(i).display(g);
			}
		}
	}
}
