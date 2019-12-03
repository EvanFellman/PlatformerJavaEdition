package Game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public class EditPanel extends JPanel implements MouseMotionListener, MouseListener {
	private static final long serialVersionUID = 1L;
	public EditPanel() {
		super();
		super.addMouseMotionListener(this);
		super.addMouseListener(this);
		super.setOpaque(true);
		super.setBackground(Color.BLUE);
	}
	public void paint(Graphics g) {
		for(int i = 0; i < Main.level.size(); i++) {
			double x = Main.level.get(i).getX() - Main.cameraX;
			double y = Main.level.get(i).getY() - Main.cameraY;
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
		
		int mouseX = (e.getX() + Main.cameraX) - ((e.getX() + Main.cameraX) % Main.SPRITE_WIDTH);
		int mouseY = (e.getY() + Main.cameraY) - ((e.getY() + Main.cameraY) % Main.SPRITE_HEIGHT);
		if(e.getY() + Main.cameraY > -1 * Main.SPRITE_HEIGHT && e.getY() + Main.cameraY <= 0) {
			mouseY = -25;
		} else if(mouseY < 0) {
			mouseY -= Main.SPRITE_HEIGHT;
		}
		if(e.getX() + Main.cameraX > -1 * Main.SPRITE_WIDTH && e.getX() + Main.cameraX <= 0) {
			mouseX = -25;
		} else if(mouseX < 0) {
			mouseX -= Main.SPRITE_WIDTH;
		}
		Thing toInsert = null;
		switch(Main.paint) {
		case "wall":
			toInsert = new Wall(mouseX, mouseY);
			break;
		case "blue gate":
			toInsert = new BlueGate(mouseX, mouseY);
			break;
		case "blue reverse gate":
			toInsert = new BlueReverseGate(mouseX, mouseY);
			break;
		case "blue switch":
			toInsert = new BlueSwitch(mouseX, mouseY);
			break;
		case "enemy dumb left":
			toInsert = new EnemyDumb(mouseX, mouseY, true, Main.enemySpeed);
			break;
		case "enemy dumb right":
			toInsert = new EnemyDumb(mouseX, mouseY, false, Main.enemySpeed);
			break;
		case "enemy no jump":
			toInsert = new EnemyNoJump(mouseX, mouseY, Main.enemySpeed);
			break;
		case "enemy only jump":
			toInsert = new EnemyOnlyJump(mouseX, mouseY);
			break;
		case "next level":
			toInsert = new NextLevel(mouseX, mouseY);
			break;
		case "start":
			toInsert = new Player(mouseX, mouseY);
			break;
		case "red gate":
			toInsert = new RedGate(mouseX, mouseY);
			break;
		case "red reverse gate":
			toInsert = new RedReverseGate(mouseX, mouseY);
			break;
		case "red switch":
			toInsert = new RedSwitch(mouseX, mouseY);
			break;
		default:
			System.out.println("uh oh");
			System.exit(0);
		}
		Main.putInMap(toInsert);
		Main.level.add(toInsert);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//I want the behavior of dragging and clicking to be the same
		mouseDragged(e);
	}
	
	//I don't need it but if I remove it then MouseMotionListener and MouseListener gets sad
	@Override public void mouseMoved(MouseEvent e) { }
	@Override public void mousePressed(MouseEvent e) { }
	@Override public void mouseReleased(MouseEvent e) { }
	@Override public void mouseEntered(MouseEvent e) { }
	@Override public void mouseExited(MouseEvent e) { }
}
