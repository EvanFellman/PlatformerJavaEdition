package Game;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Main {
	public static ArrayList<Thing> level = new ArrayList<Thing>();
	public static Hashtable<Integer, Hashtable<Integer, Thing>> levelMap; 
	public static int startX;
	public static int startY;
	public static int levelNumber;
	public static int cameraX = 0;
	public static int cameraY = 0;
	public static int DEATH_BELOW;
	public static boolean isWPressed = false;
	public static boolean isAPressed = false;
	public static boolean isDPressed = false;
	public static boolean isEscapePressed = false;
	public static final float GRAVITY = 0.02f;
	public static final int SPRITE_HEIGHT = 25;
	public static final int SPRITE_WIDTH = 25;
	public static Player player;
	public static String STATE = "menu";
	public static JFrame window;
	public static boolean isBlueGateOpen = false;
	public static boolean isRedGateOpen = false;
	public static float enemySpeed = 0.25f;
	private static GamePanel gp;
	private static EditPanel ep = new EditPanel();
	private static MKeyListener keyListener;
	private static String paint = "wall";
	public static void main(String[] args) throws IOException, InterruptedException {
		window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel menuPanel = new JPanel();
		JLabel playLabel = new JLabel("Play your levels");
		playLabel.addMouseListener(new MouseAdapter() {
	        public void mouseClicked(MouseEvent e) {
	        	window.remove(menuPanel);
	        	STATE="play";
	        }
		});
		playLabel.setBounds(0, 0, 50,50);
		menuPanel.add(playLabel);
		JLabel editLabel = new JLabel("Edit levels");
		editLabel.addMouseListener(new MouseAdapter() {
	        public void mouseClicked(MouseEvent e) {
	        	window.remove(menuPanel);
	        	STATE="edit";
	        }
		});
		editLabel.setBounds(0,0,50,50);
		menuPanel.add(editLabel);
		JLabel quitGameLabel = new JLabel("Quit");
		quitGameLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
		});
		menuPanel.add(quitGameLabel);
		JPanel editPanel = new JPanel();
		JPanel editButtonPanel = new JPanel();
		editPanel.add(editButtonPanel);
		JButton backEdit = new JButton("Back");
		backEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				window.remove(editPanel);
				window.remove(ep);
				STATE="menu";
			}
		});
		editButtonPanel.add(backEdit);
		JButton enemySpeedEdit = new JButton("medium speed");
		enemySpeedEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(enemySpeed == 0.4f) {
					enemySpeed = 0.1f;
					enemySpeedEdit.setText("slow speed");
				} else if(enemySpeed == 0.25f) {
					enemySpeed = 0.4f;
					enemySpeedEdit.setText("fast speed");
				} else {
					enemySpeed = 0.25f;
					enemySpeedEdit.setText("medium speed");
				}
			}
		});
		editButtonPanel.add(enemySpeedEdit);
		JButton wallEdit = new JButton("Wall");
		wallEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				paint = "wall";
			}
		});
		editButtonPanel.add(wallEdit);
		editPanel.add(Box.createVerticalGlue());
		editPanel.add(ep);
		window.setVisible(true);
		keyListener = new MKeyListener();
		while(true) {
			Thread.sleep(1/30);
			switch(STATE) {
			case "menu":
				window.setSize(200,100);
				window.add(menuPanel);
				STATE="menu0";
				break;
			case "edit":
				window.setSize(500, 625);
				window.addKeyListener(keyListener);
				STATE="edit0";
				levelNumber=1;
				loadLevel();
				System.out.println(cameraX);
				editButtonPanel.setMaximumSize(new Dimension(500,100));
				editButtonPanel.setPreferredSize(new Dimension(500,100));
				editButtonPanel.setMinimumSize(new Dimension(500,100));
				ep.setMaximumSize(new Dimension(500,500));
				ep.setPreferredSize(new Dimension(500, 500));
				ep.setMinimumSize(new Dimension(500,500));
				window.add(editPanel);
				BoxLayout bl = new BoxLayout(editPanel, BoxLayout.PAGE_AXIS);
				editPanel.setLayout(bl);
				window.setVisible(true);
				while(STATE.equals("edit0")) {
					Date before = new Date();
					
					window.repaint();
					Date after = new Date();
					while(after.getTime() - before.getTime() <= 3) {
						Thread.sleep(1);
						after = new Date();
					}
				}
				break;
			case "play":
				window.setSize(500,500);
				STATE="play0";
				levelNumber = 1;
				loadLevel();
				gp = new GamePanel();
				window.addKeyListener(keyListener);
				window.add(gp);
				window.setVisible(true);
				while(STATE.equals("play0")) {
					Date before = new Date();
					if(isEscapePressed) {
						JLabel quitLabel = new JLabel("paused - Press w to quit or escape to continue");
						window.add(quitLabel);
						window.remove(gp);
						window.setVisible(true);
						while(isEscapePressed) { Thread.sleep(50); }
						while(!isEscapePressed && !isWPressed) { Thread.sleep(50); }
						if(isWPressed) {
							window.remove(quitLabel);
							window.remove(gp);
							window.removeKeyListener(keyListener);
							STATE = "menu";
							isWPressed = false;
							isAPressed = false;
							isDPressed = false;
							isEscapePressed = false;
							break;
						} else if(isEscapePressed) {
							window.remove(quitLabel);
							window.add(gp);
							window.repaint();
							window.setVisible(true);
							isEscapePressed = false;
							continue;
						}
					}
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
					Date after = new Date();
					while(after.getTime() - before.getTime() <= 3) {
						Thread.sleep(1);
						after = new Date();
					}
				}				
			}
		}
	}
	
	public static Thing getFromMap(int x, int y) {
		if(levelMap.get((int) (x / SPRITE_WIDTH)) == null) {
			return null;
		} else {
			return levelMap.get((int) (x / SPRITE_WIDTH)).get((int) (y / SPRITE_HEIGHT));
		}
	}
	
	public static Thing getFromMap(float x, float y) {
		return getFromMap((int) x, (int) y);
	}
	
	public static void putInMap(Thing a) {
		if(levelMap.get((int) a.getX() / SPRITE_WIDTH) == null) {
			levelMap.put((int) (a.getX() / SPRITE_WIDTH), new Hashtable<Integer, Thing>());
		}
		levelMap.get((int) (a.getX() / SPRITE_WIDTH)).put((int) (a.getY() / SPRITE_HEIGHT), a);
	}
	
	public static void removeFromMap(Thing a) {
		levelMap.get((int) (a.getX() / SPRITE_HEIGHT)).remove((int) (a.getY() / SPRITE_WIDTH));
	}
	
	//resets level
	public static void loadLevel() {
		isBlueGateOpen = false;
		isRedGateOpen = false;
		File imgFile = new File("./level" + Integer.toString(levelNumber) + ".png");
		level = new ArrayList<Thing>();
		levelMap = new Hashtable<Integer, Hashtable<Integer, Thing>>();
		if(imgFile.exists()) {
			try {
				BufferedImage img = ImageIO.read(imgFile);
				DEATH_BELOW = img.getHeight() * SPRITE_HEIGHT;
				for(int x = 0; x < img.getWidth(); x++) {
					for(int y = 0; y < img.getHeight(); y++) {
						Color pixel = new Color(img.getRGB(x, y));
						if(pixel.getRed() == 0 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new Wall(SPRITE_WIDTH * x, SPRITE_HEIGHT * y));
						} else if(pixel.getRed() == 0 && pixel.getGreen() == 255 && pixel.getBlue() == 0) {
							level.add(new NextLevel(SPRITE_WIDTH * x, SPRITE_HEIGHT * y));
						} else if(pixel.getRed() == 0 && pixel.getGreen() == 0 && pixel.getBlue() == 255) {
							startX = SPRITE_WIDTH * x;
							startY = SPRITE_HEIGHT * y;
							player = new Player(SPRITE_WIDTH * x, SPRITE_HEIGHT * y);
							level.add(player);
						} else if(pixel.getRed() == 255 && pixel.getGreen() == 255 && pixel.getBlue() == 2) {
							level.add(new EnemyNoJump(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, 0.4f));
						} else if(pixel.getRed() == 255 && pixel.getGreen() == 255 && pixel.getBlue() == 1) {
							level.add(new EnemyNoJump(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, 0.25f));
						} else if(pixel.getRed() == 255 && pixel.getGreen() == 255 && pixel.getBlue() == 0) {
							level.add(new EnemyNoJump(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, 0.1f));
						} else if(pixel.getRed() == 255 && pixel.getGreen() == 255 && pixel.getBlue() == 3) {
							level.add(new EnemyOnlyJump(SPRITE_WIDTH * x, SPRITE_HEIGHT * y));
						} else if(pixel.getRed() == 0 && pixel.getGreen() == 0 && pixel.getBlue() == 254) {
							level.add(new BlueGate(SPRITE_WIDTH * x, SPRITE_HEIGHT * y));
						} else if(pixel.getRed() == 0 && pixel.getGreen() == 0 && pixel.getBlue() == 253) {
							level.add(new BlueReverseGate(SPRITE_WIDTH * x, SPRITE_HEIGHT * y));
						} else if(pixel.getRed() == 0 && pixel.getGreen() == 0 && pixel.getBlue() == 252) {
							level.add(new BlueSwitch(SPRITE_WIDTH * x, SPRITE_HEIGHT * y));
						} else if(pixel.getRed() == 254 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new RedGate(SPRITE_WIDTH * x, SPRITE_HEIGHT * y));
						} else if(pixel.getRed() == 253 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new RedReverseGate(SPRITE_WIDTH * x, SPRITE_HEIGHT * y));
						} else if(pixel.getRed() == 252 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new RedSwitch(SPRITE_WIDTH * x, SPRITE_HEIGHT * y));
						} else if(pixel.getRed() == 251 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new EnemyDumb(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, true, 0.4f));
						} else if(pixel.getRed() == 250 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new EnemyDumb(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, true, 0.25f));
						} else if(pixel.getRed() == 249 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new EnemyDumb(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, true, 0.1f));
						} else if(pixel.getRed() == 248 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new EnemyDumb(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, false, 0.4f));
						} else if(pixel.getRed() == 247 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new EnemyDumb(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, false, 0.25f));
						} else if(pixel.getRed() == 246 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new EnemyDumb(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, false, 0.1f));
						}
					}
				}
				for(Thing i: level) {
					putInMap(i);
				}
				cameraX = (int) (player.getX() - (window.getWidth() * 0.5));
				cameraY = (int) (player.getY() - (window.getHeight() * 0.5));
			} catch (IOException e) {
				window.remove(gp);
				window.removeKeyListener(keyListener);
				STATE = "menu";
				isWPressed = false;
				isAPressed = false;
				isDPressed = false;
				isEscapePressed = false;
				STATE = "menu";
			}			
		} else {
			window.remove(gp);
			window.removeKeyListener(keyListener);
			STATE = "menu";
			isWPressed = false;
			isAPressed = false;
			isDPressed = false;
			isEscapePressed = false;
			STATE = "menu";
		}
	}
}

class MKeyListener extends KeyAdapter {
	@Override
	public void keyPressed(KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.VK_ESCAPE) {
			Main.isEscapePressed = true;
		} else {
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
	}
	
	@Override
	public void keyReleased(KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.VK_ESCAPE) {
			Main.isEscapePressed = false;
		} else {
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
}
