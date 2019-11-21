package Game;

import java.awt.Graphics;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public void paint(Graphics g) {
		for(int i = 0; i < Main.level.size(); i++) {
			Main.level.get(i).move();
			Main.level.get(i).display(g);
		}
	}
}
