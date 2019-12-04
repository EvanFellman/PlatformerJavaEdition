package Game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public class EditPanel extends JPanel implements MouseMotionListener, MouseListener {
	private static final long serialVersionUID = 1L;
	private boolean isMouseDown = false;
	public EditPanel() {
		super();
		super.addMouseMotionListener(this);
		super.addMouseListener(this);
		super.setOpaque(true);
		super.setBackground(Color.BLUE);
	}
	public void paint(Graphics g) {
		g.setColor(Color.CYAN);
		g.fillRect(0, 0, (int)g.getClipBounds().getWidth(), (int)g.getClipBounds().getHeight());
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
		if(isMouseDown) {
			g.setColor(Color.DARK_GRAY);
			int mx = 0, my = 0;
			try {
				mx = getMousePosition().x;
				my = getMousePosition().y;
			} catch(Exception e) {
				return;
			}
			int x = (mx + Main.cameraX) - ((mx + Main.cameraX) % Main.SPRITE_WIDTH);
			int y = (my + Main.cameraY) - ((my + Main.cameraY) % Main.SPRITE_HEIGHT);
			x -= Main.cameraX;
			y -= Main.cameraY;
			if(mx + Main.cameraX < 0) {
				x -= Main.SPRITE_WIDTH;
			} 
			if(my + Main.cameraY < 0) {
				y -= Main.SPRITE_HEIGHT;
			}
			if(Main.isShiftPressed) {
				g.drawRect(x - Main.SPRITE_WIDTH, y - Main.SPRITE_HEIGHT, 3 * Main.SPRITE_WIDTH, 3 * Main.SPRITE_HEIGHT);
			} else {
				g.drawRect(x, y, Main.SPRITE_WIDTH, Main.SPRITE_HEIGHT);
			}
		}
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		int mouseXLoc = (e.getX() + Main.cameraX) - ((e.getX() + Main.cameraX) % Main.SPRITE_WIDTH);
		int mouseYLoc = (e.getY() + Main.cameraY) - ((e.getY() + Main.cameraY) % Main.SPRITE_HEIGHT);
		if(e.getY() + Main.cameraY > -1 * Main.SPRITE_HEIGHT && e.getY() + Main.cameraY <= 0) {
			mouseYLoc = -25;
		} else if(mouseYLoc < 0) {
			mouseYLoc -= Main.SPRITE_HEIGHT;
		}
		if(e.getX() + Main.cameraX > -1 * Main.SPRITE_WIDTH && e.getX() + Main.cameraX <= 0) {
			mouseXLoc = -25;
		} else if(mouseXLoc < 0) {
			mouseXLoc -= Main.SPRITE_WIDTH;
		}
		if(Main.isAltPressed) {
			Thing a = Main.getFromMap(mouseXLoc, mouseYLoc);
			if(a != null) {
				switch(a.id) {
				case "enemy dumb":
					if(((EnemyDumb) a).goLeft) {
						Main.paint = "enemy dumb left";
					} else {
						Main.paint = "enemy dumb right";
					}
					break;
				case "open red gate":
				case "wall red gate":
					Main.paint = "red gate";
					break;
				case "open red reverse gate":
				case "wall red reverse gate":
					Main.paint = "red reverse gate";
					break;
				case "open blue gate":
				case "wall blue gate":
					Main.paint = "blue gate";
					break;
				case "open blue reverse gate":
				case "wall blue reverse gate":
					Main.paint = "blue reverse gate";
					break;
				default:
					Main.paint = a.id;
					break;
				}
			}
			Main.updateEditButtons();
		} else {
			for (int mouseX = mouseXLoc - (Main.isShiftPressed ? Main.SPRITE_WIDTH : 0); mouseX <= mouseXLoc + (Main.isShiftPressed ? Main.SPRITE_WIDTH : 0); mouseX += Main.SPRITE_WIDTH) {
				for (int mouseY = mouseYLoc - (Main.isShiftPressed ? Main.SPRITE_HEIGHT : 0); mouseY <= mouseYLoc + (Main.isShiftPressed ? Main.SPRITE_HEIGHT : 0); mouseY += Main.SPRITE_HEIGHT) {
					if (Main.getFromMap(mouseX, mouseY) != null && Main.paint != "erase" && !Main.isCtrlPressed) {
						continue;
					}
					Thing toInsert = null;
					switch (Main.isCtrlPressed ? "erase" : Main.paint) {
					case "erase":
						Thing toRemove = Main.getFromMap(mouseX, mouseY);
						if (toRemove != null) {
							Main.level.remove(toRemove);
							Main.removeFromMap(toRemove);
						}
						toInsert = null;
						break;
					case "spike":
						toInsert = new Spike(mouseX, mouseY);
						break;
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
					case "red gate":
						toInsert = new RedGate(mouseX, mouseY);
						break;
					case "red reverse gate":
						toInsert = new RedReverseGate(mouseX, mouseY);
						break;
					case "red switch":
						toInsert = new RedSwitch(mouseX, mouseY);
						break;
					case "player":
						toInsert = new Player(mouseX, mouseY);
						break;
					case "disappearing wall":
						toInsert = new DisappearingWall(mouseX, mouseY);
						break;
					default:
						System.out.println("uh oh");
						System.exit(0);
					}
					if (toInsert != null) {
						Main.putInMap(toInsert);
						Main.level.add(toInsert);
					}
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//I want the behavior of dragging and clicking to be the same
		mouseDragged(e);
	}
	@Override public void mouseEntered(MouseEvent e) {
		isMouseDown = true;
	}
	@Override public void mouseExited(MouseEvent e) {
		isMouseDown = false;
	}
	
	//I don't need it but if I remove it then MouseMotionListener and MouseListener gets sad
	@Override public void mouseMoved(MouseEvent e) { }
	@Override public void mousePressed(MouseEvent e) { }
	@Override public void mouseReleased(MouseEvent e) { }
}
