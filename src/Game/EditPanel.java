package Game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public class EditPanel extends JPanel implements MouseMotionListener {
	private static final long serialVersionUID = 1L;
	public EditPanel() {
		super();
		super.addMouseMotionListener(this);
		super.setOpaque(false);
		super.setBackground(Color.BLUE);
	}
	public void paint(Graphics g) {
		for(int i = 0; i < Main.level.size(); i++) {
			float x = Main.level.get(i).getX() - Main.cameraX;
			float y = Main.level.get(i).getY() - Main.cameraY;
			if(x <= Main.window.getWidth() && x >= - 1* Main.window.getWidth() && y <= Main.window.getHeight() && y >= -1 * Main.window.getHeight()) {
				try {
					Main.level.get(i).display(g);
				} catch(Exception e) {
					
				}
			}
		}
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		System.out.println(e.getY());
		// TODO Auto-generated method stub
		
	}
}
