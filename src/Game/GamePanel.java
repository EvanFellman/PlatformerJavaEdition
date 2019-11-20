package Game;

import java.awt.Graphics;

import javax.swing.JPanel;

public class GamePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public void paint(Graphics g) {
		for(Thing i: Main.thingsToDraw) {
			i.move(Main.map);
			i.display(g);
		}
	}
}
