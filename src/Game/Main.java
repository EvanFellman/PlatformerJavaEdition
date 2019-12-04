package Game;
import java.awt.Color;
import java.awt.Component;
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
	public static int levelNumber = 1;
	public static int cameraX = 0;
	public static int cameraY = 0;
	public static int DEATH_BELOW;
	public static boolean isWPressed = false;
	public static boolean isAPressed = false;
	public static boolean isSPressed = false;
	public static boolean isDPressed = false;
	public static boolean isSpacePressed = false;
	public static boolean isEscapePressed = false;
	public static boolean isShiftPressed = false;
	public static boolean isAltPressed = false;
	public static boolean isCtrlPressed = false;
	public static final double GRAVITY = 0.02f;
	public static final int SPRITE_HEIGHT = 25;
	public static final int SPRITE_WIDTH = 25;
	public static Player player;
	public static String STATE = "menu";
	public static JFrame window;
	public static boolean isBlueGateOpen = false;
	public static boolean isRedGateOpen = false;
	public static double enemySpeed = 0.25f;
	private static GamePanel gp;
	public static EditPanel ep = new EditPanel();
	private static MKeyListener keyListener = new MKeyListener();
	public static String paint = "wall";
	private static JPanel editButtonPanel;
	private static JButton eraseEdit;
	private static JButton wallEdit;
	private static JButton blueGateEdit;
	private static JButton redGateEdit;
	private static JButton spikeEdit;
	private static JButton enemyEdit;
	private static JButton goalEdit;
	private static JButton startEdit;
	public static void main(String[] args) throws IOException, InterruptedException {
		window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.addKeyListener(keyListener);
		final JPanel menuPanel = new JPanel();
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
		final JPanel editPanel = new JPanel();
		editButtonPanel = new JPanel();
		JPanel editNavButtonPanel = new JPanel();
		editButtonPanel.add(editNavButtonPanel);
		editPanel.add(editButtonPanel);
		JButton backEdit = new JButton("Back");
		backEdit.setFocusable(false);
		backEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				window.remove(editPanel);
				STATE="menu";
				Thread.yield();
			}
		});
		editNavButtonPanel.add(backEdit);
		editNavButtonPanel.add(Box.createRigidArea(new Dimension(100, 10)));
		final JButton levelNumberDisplayEdit = new JButton("level " + Integer.toString(levelNumber));
		levelNumberDisplayEdit.setFocusable(false);
		JButton previousLevelEdit = new JButton("<");
		previousLevelEdit.setFocusable(false);
		previousLevelEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(levelNumber > 1) {
					levelNumber --;
					levelNumberDisplayEdit.setText("level " + Integer.toString(levelNumber));
					loadLevel();
				}
			}
		});
		editNavButtonPanel.add(previousLevelEdit);
		editNavButtonPanel.add(levelNumberDisplayEdit);
		JButton nextLevelEdit = new JButton(">");
		nextLevelEdit.setFocusable(false);
		nextLevelEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				levelNumber++;
				levelNumberDisplayEdit.setText("level " + Integer.toString(levelNumber));
				loadLevel();
			}
		});
		editNavButtonPanel.add(nextLevelEdit);
		editNavButtonPanel.add(Box.createRigidArea(new Dimension(150, 10)));
		final JButton enemySpeedEdit = new JButton("medium speed");
		enemySpeedEdit.setFocusable(false);
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
		eraseEdit = new JButton("Erase");
		eraseEdit.setFocusable(false);
		eraseEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				paint = "erase";
				updateEditButtons();				
			}
		});
		editButtonPanel.add(eraseEdit);
		wallEdit = new JButton("Wall");
		wallEdit.setFocusable(false);
		wallEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(paint.equals("wall")){
					paint = "disappearing wall";
				} else {
					paint = "wall";
				}
				updateEditButtons();
			}
		});
		editButtonPanel.add(wallEdit);
		blueGateEdit = new JButton("Blue Gate");
		blueGateEdit.setFocusable(false);
		blueGateEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(paint.equals("blue gate")) {
					paint = "blue reverse gate";
				} else if(paint.equals("blue reverse gate")) {
					paint = "blue switch";
				} else {
					paint = "blue gate";
				}
				updateEditButtons();
			}
		});
		editButtonPanel.add(blueGateEdit);
		redGateEdit = new JButton("Red Gate");
		redGateEdit.setFocusable(false);
		redGateEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(paint.equals("red gate")) {
					paint = "red reverse gate";
				} else if(paint.equals("red reverse gate")) {
					paint = "red switch";
				} else {
					paint = "red gate";
				}
				updateEditButtons();
			}
		});
		editButtonPanel.add(redGateEdit);
		enemyEdit = new JButton("Enemy (D L)");
		enemyEdit.setFocusable(false);
		enemyEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(paint.equals("enemy dumb left")) {
					paint = "enemy dumb right";
				} else if(paint.equals("enemy dumb right")) {
					paint = "enemy no jump";
				} else if(paint.equals("enemy no jump")) {
					paint = "enemy only jump";
				} else {
					paint = "enemy dumb left";
				}
				updateEditButtons();
			}
		});
		editButtonPanel.add(enemyEdit);
		spikeEdit = new JButton("Spike");
		spikeEdit.setFocusable(false);
		spikeEdit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				paint = "spike";
				updateEditButtons();
			}
		});
		editButtonPanel.add(spikeEdit);
		goalEdit = new JButton("Goal");
		goalEdit.setFocusable(false);
		goalEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				paint = "next level";
				updateEditButtons();
			}
		});
		editButtonPanel.add(goalEdit);
		startEdit = new JButton("Start");
		startEdit.setFocusable(false);
		startEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				paint = "player";
				updateEditButtons();
			}
		});
		editButtonPanel.add(startEdit);
		JButton saveEdit = new JButton("Save");
		saveEdit.setFocusable(false);
		saveEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int minX = Integer.MAX_VALUE;
				int maxX = Integer.MIN_VALUE;
				int minY = Integer.MAX_VALUE;
				int maxY = Integer.MIN_VALUE;
				boolean existsAPlayer = false;
				for(Thing a: level) {
					if(a.getX() < minX) {
						minX = (int) a.getX();
					}
					if(a.getX() > maxX) {
						maxX = (int) a.getX();
					}
					if(a.getY() < minY) {
						minY = (int) a.getY();
					}
					if(a.getY() > maxY) {
						maxY = (int) a.getY();
					}
				}
				BufferedImage image = new BufferedImage(1 + (maxX - minX) / SPRITE_WIDTH, 1 + (maxY - minY) / SPRITE_HEIGHT, BufferedImage.TYPE_INT_RGB);
				for(int i = 0; i < 1 + (maxX - minX) / SPRITE_WIDTH; i++) {
					for(int j = 0; j < 1 + (maxY - minY) / SPRITE_HEIGHT; j++) {
						image.setRGB(i, j, (new Color(255, 255, 255)).getRGB());
					}
				}
				for(Thing a: level) {
					int x = ((int)a.getX() - minX) / SPRITE_WIDTH;
					int y = ((int)a.getY() - minY) / SPRITE_HEIGHT;
					switch(a.id) {
					case "player":
						image.setRGB(x, y, (new Color(0, 0, 255).getRGB()));
						existsAPlayer = true;
						break;
					case "wall":
						image.setRGB(x, y, (new Color(0, 0, 0).getRGB()));
						break;
					case "next level":
						image.setRGB(x, y, (new Color(0, 255, 0).getRGB()));
						break;
					case "enemy no jump":
						if(((EnemyNoJump) a).speed == 0.4f) {
							image.setRGB(x, y, (new Color(255, 255, 2).getRGB()));
						} else if(((EnemyNoJump) a).speed == 0.25f) {
							image.setRGB(x, y, (new Color(255, 255, 1).getRGB()));
						} else if(((EnemyNoJump) a).speed == 0.1f) {
							image.setRGB(x, y, (new Color(255, 255, 0).getRGB()));
						}
						break;
					case "spike":
						image.setRGB(x, y, (new Color(245, 0, 0)).getRGB());
						break;
					case "enemy only jump":
						image.setRGB(x, y, (new Color(255, 255, 3).getRGB()));
						break;
					case "open blue gate":
					case "wall blue gate":
						image.setRGB(x, y, (new Color(0, 0, 254).getRGB()));
						break;
					case "open blue reverse gate":
					case "wall blue reverse gate":
						image.setRGB(x, y, (new Color(0, 0, 253).getRGB()));
						break;
					case "blue switch":
						image.setRGB(x, y, (new Color(0, 0, 252).getRGB()));
						break;
					case "open red gate":
					case "wall red gate":
						image.setRGB(x, y, (new Color(254, 0, 0).getRGB()));
						break;
					case "open red reverse gate":
					case "wall red reverse gate":
						image.setRGB(x, y, (new Color(253, 0, 0).getRGB()));
						break;
					case "red switch":
						image.setRGB(x, y, (new Color(252, 0, 0).getRGB()));
						break;
					case "wall disappearing":
						image.setRGB(x, y, (new Color(0, 254, 0).getRGB()));
						break;
					case "enemy dumb":
						if(((EnemyDumb) a).goLeft) {
							if(((EnemyDumb) a).speed == 0.4f) {
								image.setRGB(x, y, (new Color(251, 0, 0).getRGB()));
								break;
							} else if(((EnemyDumb) a).speed == 0.25f) {
								image.setRGB(x, y, (new Color(250, 0, 0).getRGB()));
								break;
							} else if(((EnemyDumb) a).speed == 0.1f) {
								image.setRGB(x, y, (new Color(249, 0, 0).getRGB()));
								break;
							}
						} else {
							if(((EnemyDumb) a).speed == 0.4f) {
								image.setRGB(x, y, (new Color(248, 0, 0).getRGB()));
								break;
							} else if(((EnemyDumb) a).speed == 0.25f) {
								image.setRGB(x, y, (new Color(247, 0, 0).getRGB()));
								break;
							} else if(((EnemyDumb) a).speed == 0.1f) {
								image.setRGB(x, y, (new Color(246, 0, 0).getRGB()));
								break;
							}
						}
					}
				}
				if(existsAPlayer) {
					File outFile = new File("level" + Integer.toString(levelNumber) + ".png");
					try {
						ImageIO.write(image, "png", outFile);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		editNavButtonPanel.add(saveEdit);
		editPanel.add(Box.createVerticalGlue());
		editPanel.add(ep);
		highlightButton(wallEdit, editButtonPanel);
		window.setVisible(true);
		while(true) {
			Thread.sleep(1/30);
			switch(STATE) {
			case "menu":
				window.setSize(200,100);
				window.add(menuPanel);
				STATE="menu0";
				break;
			case "edit":
				window.setSize(550, 645);
				STATE="edit0";
				levelNumber=1;
				loadLevel();
				editButtonPanel.setMaximumSize(new Dimension(550,120));
				editButtonPanel.setPreferredSize(new Dimension(550,120));
				editButtonPanel.setMinimumSize(new Dimension(550,120));
				ep.setMaximumSize(new Dimension(550,500));
				ep.setPreferredSize(new Dimension(550, 500));
				ep.setMinimumSize(new Dimension(550,500));
				BoxLayout bl = new BoxLayout(editPanel, BoxLayout.PAGE_AXIS);
				editPanel.setLayout(bl);
				window.add(editPanel);
				window.setVisible(true);
				while(STATE.equals("edit0")) {
					Date before = new Date();
					window.repaint();
					window.setVisible(true);
					if(isWPressed) {
						cameraY -= SPRITE_HEIGHT / 4;
					} 
					if(isSPressed) {
						cameraY += SPRITE_HEIGHT / 4;
					}
					if(isAPressed) {
						cameraX -= SPRITE_WIDTH / 4;
					}
					if(isDPressed) {
						cameraX += SPRITE_WIDTH / 4;
					}
					if(isSpacePressed) {
						cameraX = startX - (int)(window.getWidth() * 0.5);
						cameraY = startY - (int)(window.getHeight() * 0.5);
					}
					Date after = new Date();
					while(after.getTime() - before.getTime() <= 3) {
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
				window.add(gp);
				window.setVisible(true);
				while(STATE.equals("play0")) {
					Date before = new Date();
					if(isEscapePressed) {
						JLabel quitLabel = new JLabel("paused - Press w to quit or escape to continue");
						quitLabel.setFocusable(false);
						window.add(quitLabel);
						window.remove(gp);
						window.setVisible(true);
						while(isEscapePressed) { Thread.sleep(50); }
						while(!isEscapePressed && !isWPressed) { Thread.sleep(50); }
						if(isWPressed) {
							window.remove(quitLabel);
							window.remove(gp);
							STATE = "menu";
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
					while(player.getX() - cameraX < 150) {
						cameraX --;
					}
					while(player.getX() - cameraX > 350) {
						cameraX ++;
					}
					while(player.getY() - cameraY < 150) {
						cameraY --;
					}
					while(player.getY() - cameraY > 350) {
						cameraY ++;
					}
					window.repaint();
					Date after = new Date();
					while(after.getTime() - before.getTime() < 3) {
						Thread.sleep(1);
						after = new Date();
					}
				}				
			}
		}
	}

	private static void highlightButton(JButton btn, JPanel p) {
		for(Component c : p.getComponents()) {
			if(c.getClass().getName().equals("javax.swing.JPanel")) {
				JPanel j = (JPanel) c;
				for(Component i: j.getComponents()) {
					i.setBackground(Color.LIGHT_GRAY);
				}
			} else {
				c.setBackground(Color.GRAY);
			}
		}
		btn.setBackground(Color.green);
	}
	
	public static void updateEditButtons() {
		switch(Main.paint){
		case "spike":
			highlightButton(spikeEdit, editButtonPanel);
			break;
		case "disappearing wall":
		case "wall":
			highlightButton(wallEdit, editButtonPanel);
			break;
		case "erase":
			highlightButton(eraseEdit, editButtonPanel);
			break;
		case "blue gate":
		case "blue reverse gate":
		case "blue switch":
			highlightButton(blueGateEdit, editButtonPanel);
			break;
		case "red gate":
		case "red reverse gate":
		case "red switch":
			highlightButton(redGateEdit, editButtonPanel);
			break;
		case "enemy dumb left":
		case "enemy dumb right":
		case "enemy no jump":
		case "enemy only jump":
			highlightButton(enemyEdit, editButtonPanel);
			break;
		case "player":
			highlightButton(startEdit, editButtonPanel);
			break;
		case "next level":
			highlightButton(goalEdit, editButtonPanel);
			break;
		}
		if(paint.equals("disappearing wall")){
			wallEdit.setText("disappearing wall");
		} else {
			wallEdit.setText("Wall");
		}
		if(paint.equals("blue reverse gate")) {
			blueGateEdit.setText("Blue Gate (R)");
		} else if(paint.equals("blue switch")){
			blueGateEdit.setText("Blue Switch");
		} else {
			blueGateEdit.setText("Blue Gate");
		} 
		if(paint.equals("red reverse gate")) {
			redGateEdit.setText("Red Gate (R)");
		} else if(paint.equals("red switch")){
			redGateEdit.setText("Red Switch");
		} else {
			redGateEdit.setText("Red Gate");
		}
		if (paint.equals("enemy dumb right")) {
			enemyEdit.setText("Enemy (D R)");
		} else if (paint.equals("enemy no jump")) {
			enemyEdit.setText("Enemy (NJ)");
		} else if(paint.equals("enemy only jump")){
			enemyEdit.setText("Enemy (OJ)");
		} else {
			enemyEdit.setText("Enemy (D L)");
		}
	}
	
	public static Thing getFromMap(int x, int y) {
		if(levelMap.get((int) (x / SPRITE_WIDTH)) == null) {
			return null;
		} else {
			return levelMap.get((int) (x / SPRITE_WIDTH)).get((int) (y / SPRITE_HEIGHT));
		}
	}
	
	public static Thing getFromMap(double x, double y) {
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
	synchronized public static void loadLevel() {
		isBlueGateOpen = false;
		isRedGateOpen = false;
		File imgFile = new File("./level" + Integer.toString(levelNumber) + ".png");
		if (level==null) {
			level = new ArrayList<Thing>();
		} else {
			level.clear();
		}
		if (levelMap == null) {
			levelMap = new Hashtable<Integer, Hashtable<Integer, Thing>>();
		} else {
			levelMap.clear();
		}
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
						} else if(pixel.getRed() == 0 && pixel.getGreen() == 254 && pixel.getBlue() == 0) {
							level.add(new DisappearingWall(SPRITE_WIDTH * x, SPRITE_HEIGHT * y));
						} else if(pixel.getRed() == 245 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new Spike(SPRITE_WIDTH * x, SPRITE_HEIGHT * y));
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
				STATE = "menu";
				isWPressed = false;
				isAPressed = false;
				isSPressed = false;
				isDPressed = false;
				isSpacePressed = false;
				isEscapePressed = false;
				STATE = "menu";
			}			
		} else if(STATE.equals("play0")){
			window.remove(gp);
			STATE = "menu";
			isWPressed = false;
			isAPressed = false;
			isSPressed = false;
			isDPressed = false;
			isSpacePressed = false;
			isEscapePressed = false;
			STATE = "menu";
		} else if(STATE.equals("edit0")){
			
		}
	}
}

class MKeyListener extends KeyAdapter {
	@Override
	public void keyPressed(KeyEvent event) {
		switch(event.getKeyCode()) {
		case KeyEvent.VK_SPACE:
			Main.isSpacePressed = true;
			break;
		case KeyEvent.VK_ESCAPE:
			Main.isEscapePressed = true;
			break;
		case KeyEvent.VK_SHIFT:
			Main.isShiftPressed = true;
			break;
		case KeyEvent.VK_ALT:
			Main.isAltPressed = true;
			break;
		case KeyEvent.VK_CONTROL:
			Main.isCtrlPressed = true;
			String tempPaint = Main.paint;
			Main.paint = "erase";
			Main.updateEditButtons();
			Main.paint = tempPaint;
			break;
		case KeyEvent.VK_W:
	    	Main.isWPressed = true;
	    	break;
	    case KeyEvent.VK_A:
	    	Main.isAPressed = true;
	    	break;
	    case KeyEvent.VK_S:
	    	Main.isSPressed = true;
	    	break;
	    case KeyEvent.VK_D:
	    	Main.isDPressed = true;
	    	break;
	    default:
	    	break;
	    }
	}
	
	@Override
	public void keyReleased(KeyEvent event) {
		switch(event.getKeyCode()) {
		case KeyEvent.VK_SPACE:
			Main.isSpacePressed = false;
			break;
		case KeyEvent.VK_ESCAPE:
			Main.isEscapePressed = false;
			break;
		case KeyEvent.VK_SHIFT:
			Main.isShiftPressed = false;
			break;
		case KeyEvent.VK_ALT:
			Main.isAltPressed = false;
			break;
		case KeyEvent.VK_CONTROL:
			Main.isCtrlPressed = false;
			Main.updateEditButtons();
			break;
		case KeyEvent.VK_W:
	    	Main.isWPressed = false;
	    	break;
	    case KeyEvent.VK_A:
	    	Main.isAPressed = false;
	    	break;
	    case KeyEvent.VK_S:
	    	Main.isSPressed = false;
	    	break;
	    case KeyEvent.VK_D:
	    	Main.isDPressed = false;
	    	break;
	    default:
	    	break;
	    }
	}
}