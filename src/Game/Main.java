package Game;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Main {
	private static boolean editTemplates = false;
	private static final double MAX_FRAMERATE = 65;
	public static ArrayList<Thing> level = new ArrayList<Thing>();
	public static Hashtable<Integer, Hashtable<Integer, Thing>> levelMapStable;
	public static Hashtable<Integer, Hashtable<Integer, Thing>> levelMapMoving;
	public static int startX;
	public static int startY;
	public static int levelNumber = 1;
	public static int cameraX = 0;
	public static int cameraY = 0;
	public static int DEATH_BELOW;
	public static boolean fullscreen = false;
	public static boolean isWPressed = false;
	public static boolean isAPressed = false;
	public static boolean isSPressed = false;
	public static boolean isDPressed = false;
	public static boolean isZPressed = false;
	public static boolean isSpacePressed = false;
	public static boolean isEscapePressed = false;
	public static boolean isShiftPressed = false;
	public static boolean isAltPressed = false;
	public static boolean isCtrlPressed = false;
	public static Hashtable<Integer, Hashtable<Integer, Boolean>> clouds = new Hashtable<Integer, Hashtable<Integer, Boolean>>();
	public static final double GRAVITY = 0.5;
	public static final int SPRITE_HEIGHT = 25;
	public static final int SPRITE_WIDTH = 25;
	public static final double SLOW_SPEED = 1.5;
	public static final double FAST_SPEED = 2;
	public static ArrayList<Player> player;
	public static String STATE = "menu";
	public static JFrame window;
	public static boolean isBlueGateOpen = false;
	public static boolean isRedGateOpen = false;
	public static double enemySpeed = SLOW_SPEED;
	private static GamePanel gp;
	public static RandomPanel rp;
	public static EditPanel ep = new EditPanel();
	private static MKeyListener keyListener = new MKeyListener();
	public static String paint = "wall";
	private static JPanel editButtonPanel;
	public static  JButton enemySpeedEdit;
	private static JButton eraseEdit;
	private static JButton wallEdit;
	private static JButton blueGateEdit;
	private static JButton redGateEdit;
	private static JButton spikeEdit;
	private static JButton enemyEdit;
	private static JButton bulletEdit;
	private static JButton goalEdit;
	private static JButton startEdit;
	private static JButton powerUpEdit;
	private static JButton levelNumberDisplayEdit;
	private static JButton movingWallEdit;
	private static BufferedImage background;
	public static boolean deadPlayer = false;
	public static int deadPlayerCounter;
	public static Clip clip;
	public static JPanel menuPanel;
	public static JPanel menuPanelFull;
	public static BufferedImage texturedImg;
	public static int highscore;
	public static JPanel scorePanel;
	public static void main(String[] args) throws IOException, InterruptedException {
		try {
			texturedImg = ImageIO.read(new File("config/textures.png"));
		} catch(Exception e) {	}
		try {
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("config/music.wav").getAbsoluteFile()); 
	        clip = AudioSystem.getClip(); 
	        clip.open(audioInputStream); 
	        clip.loop(Clip.LOOP_CONTINUOUSLY); 
	        clip.stop();
		} catch(Exception e) {
			System.out.println(e);
		}
		background = ImageIO.read(new File("./config/background.png"));
		window = new JFrame();
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.addKeyListener(keyListener);
		window.setTitle("Platformer");
		window.setIconImage(texturedImg.getSubimage(Main.SPRITE_WIDTH, 0, Main.SPRITE_WIDTH, Main.SPRITE_HEIGHT));
		menuPanel = new JPanel();
		menuPanel.setBackground(Color.GRAY);
		menuPanel.setLayout(new BoxLayout(menuPanel, 1));
		JLabel menuTitle = new JLabel("Platformer");
		menuTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		menuTitle.setFont(new Font("TimesRoman", Font.BOLD, 23));
		menuTitle.setForeground(Color.WHITE);
		menuPanel.add(menuTitle);
		menuPanel.add(Box.createRigidArea(new Dimension(1,50)));
		JButton playMenu = new JButton("Play your levels");
		playMenu.setBackground(Color.DARK_GRAY);
		playMenu.setForeground(Color.WHITE);
		playMenu.setFocusable(false);
		playMenu.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				window.remove(menuPanel);
				STATE="play";
			}
		});
		playMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
		playMenu.setBounds(0, 0, 50,50);
		menuPanel.add(playMenu);
		menuPanel.add(Box.createVerticalGlue());
		JButton randomMenu = new JButton("Play a random level");
		randomMenu.setBackground(Color.DARK_GRAY);
		randomMenu.setForeground(Color.WHITE);
		randomMenu.setFocusable(false);
		randomMenu.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				window.remove(menuPanel);
				STATE="random";
			}
		});
		randomMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
		randomMenu.setBounds(0,0,50,50);
		menuPanel.add(randomMenu);
		menuPanel.add(Box.createVerticalGlue());
		JButton editMenu = new JButton("Edit levels");
		editMenu.setBackground(Color.DARK_GRAY);
		editMenu.setForeground(Color.WHITE);
		editMenu.setFocusable(false);
		editMenu.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				window.remove(menuPanel);
				STATE="edit";
			}
		});
		editMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
		editMenu.setBounds(0,0,50,50);
		menuPanel.add(editMenu);
		menuPanel.add(Box.createVerticalGlue());
		JButton optionsMenu = new JButton("Options");
		optionsMenu.setBackground(Color.DARK_GRAY);
		optionsMenu.setForeground(Color.WHITE);
		optionsMenu.setFocusable(false);
		optionsMenu.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				window.remove(menuPanel);
				STATE="options";
			}
		});
		optionsMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
		optionsMenu.setBounds(0,0,50,50);
		menuPanel.add(optionsMenu);
		menuPanel.add(Box.createVerticalGlue());
		JButton quitMenu = new JButton("Quit");
		quitMenu.setBackground(Color.DARK_GRAY);
		quitMenu.setForeground(Color.WHITE);
		quitMenu.setFocusable(false);
		quitMenu.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				System.exit(0);
			}
		});
		quitMenu.setBounds(0,0,50,50);
		quitMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
		menuPanel.add(quitMenu);
		
		menuPanelFull = new JPanel();
		menuPanelFull.setBackground(Color.GRAY);
		menuPanelFull.setLayout(new BoxLayout(menuPanelFull, 1));
		menuPanelFull.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
		menuPanelFull.setMinimumSize(Toolkit.getDefaultToolkit().getScreenSize());
		JLabel menuTitleFull = new JLabel("Platformer");
		menuTitleFull.setAlignmentX(Component.CENTER_ALIGNMENT);
		menuTitleFull.setFont(new Font("TimesRoman", Font.BOLD, 100));
		menuTitleFull.setForeground(Color.WHITE);
		menuPanelFull.add(menuTitleFull);
		menuPanelFull.add(Box.createVerticalGlue());
		JButton playMenuFull = new JButton("Play your levels");
		playMenuFull.setBackground(Color.DARK_GRAY);
		playMenuFull.setForeground(Color.WHITE);
		playMenuFull.setFocusable(false);
		playMenuFull.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				window.remove(menuPanelFull);
				STATE="play";
			}
		});
		playMenuFull.setAlignmentX(Component.CENTER_ALIGNMENT);
		playMenuFull.setFont(new Font("TimesRoman", Font.PLAIN, 30));
		updateSize(playMenuFull, 500, 100);
		menuPanelFull.add(playMenuFull);
		menuPanelFull.add(Box.createVerticalGlue());
		JButton randomMenuFull = new JButton("Play a random level");
		randomMenuFull.setBackground(Color.DARK_GRAY);
		randomMenuFull.setForeground(Color.WHITE);
		randomMenuFull.setFocusable(false);
		randomMenuFull.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				window.remove(menuPanelFull);
				STATE="random";
			}
		});
		randomMenuFull.setAlignmentX(Component.CENTER_ALIGNMENT);
		randomMenuFull.setFont(new Font("TimesRoman", Font.PLAIN, 30));
		updateSize(randomMenuFull, 500, 100);
		menuPanelFull.add(randomMenuFull);
		menuPanelFull.add(Box.createVerticalGlue());
		JButton editMenuFull = new JButton("Edit levels");
		editMenuFull.setBackground(Color.DARK_GRAY);
		editMenuFull.setForeground(Color.WHITE);
		editMenuFull.setFocusable(false);
		editMenuFull.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				window.remove(menuPanelFull);
				STATE="edit";
			}
		});
		editMenuFull.setAlignmentX(Component.CENTER_ALIGNMENT);
		editMenuFull.setFont(new Font("TimesRoman", Font.PLAIN, 30));
		updateSize(editMenuFull, 500, 100);
		menuPanelFull.add(editMenuFull);
		menuPanelFull.add(Box.createVerticalGlue());
		JButton optionsMenuFull = new JButton("Options");
		optionsMenuFull.setBackground(Color.DARK_GRAY);
		optionsMenuFull.setForeground(Color.WHITE);
		optionsMenuFull.setFocusable(false);
		optionsMenuFull.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				window.remove(menuPanelFull);
				STATE="options";
			}
		});
		optionsMenuFull.setAlignmentX(Component.CENTER_ALIGNMENT);
		optionsMenuFull.setFont(new Font("TimesRoman", Font.PLAIN, 30));
		updateSize(optionsMenuFull, 500, 100);
		menuPanelFull.add(optionsMenuFull);
		menuPanelFull.add(Box.createVerticalGlue());
		JButton quitMenuFull = new JButton("Quit");
		quitMenuFull.setBackground(Color.DARK_GRAY);
		quitMenuFull.setForeground(Color.WHITE);
		quitMenuFull.setFocusable(false);
		quitMenuFull.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				System.exit(0);
			}
		});
		quitMenuFull.setAlignmentX(Component.CENTER_ALIGNMENT);
		quitMenuFull.setFont(new Font("TimesRoman", Font.PLAIN, 30));
		updateSize(quitMenuFull, 500, 100);
		menuPanelFull.add(quitMenuFull);
		
		final JPanel editPanel = new JPanel();
		editButtonPanel = new JPanel();
		editButtonPanel.setLayout(new BoxLayout(editButtonPanel, 1));
		JPanel speedAndErasePanel = new JPanel();
		speedAndErasePanel.setLayout(new GridLayout(1, 2));
		editButtonPanel.add(speedAndErasePanel);
		JPanel otherButtonsPanel = new JPanel();
		otherButtonsPanel.setLayout(new GridLayout(2, 6));
		editButtonPanel.add(otherButtonsPanel);
		JPanel editNavButtonPanel = new JPanel();
		editPanel.add(editNavButtonPanel);
		editPanel.add(editButtonPanel);
		JButton backEdit = new JButton("Back");
		backEdit.setFocusable(false);
		backEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				window.remove(editPanel);
				STATE="menu";
				window.repaint();
				window.setVisible(true);
			}
		});
		editNavButtonPanel.add(backEdit);
		editNavButtonPanel.add(Box.createRigidArea(new Dimension(100, 10)));
		levelNumberDisplayEdit = new JButton("level " + Integer.toString(levelNumber));
		levelNumberDisplayEdit.setFocusable(false);
		JButton previousLevelEdit = new JButton("<");
		previousLevelEdit.setFocusable(false);
		previousLevelEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(isShiftPressed) {
					if(levelNumber < 6) {
						levelNumber = 1;
					} else {
						levelNumber -= 5;
					}
				} else if(levelNumber > 1) {
					levelNumber --;
				}
				levelNumberDisplayEdit.setText("level " + Integer.toString(levelNumber));
				loadLevel();
			}
		});
		editNavButtonPanel.add(previousLevelEdit);
		editNavButtonPanel.add(levelNumberDisplayEdit);
		JButton nextLevelEdit = new JButton(">");
		nextLevelEdit.setFocusable(false);
		nextLevelEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(isShiftPressed) {
					levelNumber+=5;
				} else {
					levelNumber++;
				}
				levelNumberDisplayEdit.setText("level " + Integer.toString(levelNumber));
				loadLevel();
			}
		});
		editNavButtonPanel.add(nextLevelEdit);
		editNavButtonPanel.add(Box.createRigidArea(new Dimension(100, 10)));
		enemySpeedEdit = new JButton("slow speed");
		enemySpeedEdit.setFocusable(false);
		enemySpeedEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(enemySpeed == FAST_SPEED) {
					enemySpeed = SLOW_SPEED;
					enemySpeedEdit.setText("slow speed");
				} else if(enemySpeed == SLOW_SPEED) {
					enemySpeed = FAST_SPEED;
					enemySpeedEdit.setText("fast speed");
				}
			}
		});
		speedAndErasePanel.add(enemySpeedEdit);
		eraseEdit = new JButton("Erase");
		eraseEdit.setFocusable(false);
		eraseEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				paint = "erase";
				updateEditButtons();				
			}
		});
		speedAndErasePanel.add(eraseEdit);
		wallEdit = new JButton("Wall");
		wallEdit.setFocusable(false);
		wallEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(paint.equals("wall")){
					paint = "wall disappearing";
				} else {
					paint = "wall";
				}
				updateEditButtons();
			}
		});
		otherButtonsPanel.add(wallEdit);
		movingWallEdit = new JButton("Moving Wall (L)");
		movingWallEdit.setFocusable(false);
		movingWallEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(paint.equals("wall moving left")) {
					paint = "wall moving right";
				} else if(paint.equals("wall moving right")){
					paint = "wall moving up";
				} else if(paint.equals("wall moving up")) {
					paint = "wall moving down";
				} else {
					paint = "wall moving left";
				}
				updateEditButtons();
			}
		});
		otherButtonsPanel.add(movingWallEdit);
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
		otherButtonsPanel.add(blueGateEdit);
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
		otherButtonsPanel.add(redGateEdit);
		enemyEdit = new JButton("Enemy (Dumb L)");
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
				} else if(paint.equals("enemy only jump")){
					paint = "enemy smart";
				} else {
					paint = "enemy dumb left";
				}
				updateEditButtons();
			}
		});
		otherButtonsPanel.add(enemyEdit);
		bulletEdit = new JButton("Bullet (L)");
		bulletEdit.setFocusable(false);
		bulletEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(paint.equals("enemy bullet left")) {
					paint = "enemy bullet right";
				} else if(paint.equals("enemy bullet right")) {
					paint = "enemy bullet up";
				} else if(paint.equals("enemy bullet up")) {
					paint = "enemy bullet down";
				} else if(paint.equals("enemy bullet down")) {
					paint = "wall shooter left";
				} else if(paint.equals("wall shooter left")) {
					paint = "wall shooter right";
				} else if(paint.equals("wall shooter right")) {
					paint = "wall shooter up";
				} else if(paint.equals("wall shooter up")) {
					paint = "wall shooter down";
				} else {
					paint = "enemy bullet left";
				}
				updateEditButtons();
			}
		});
		otherButtonsPanel.add(bulletEdit);
		spikeEdit = new JButton("Spike");
		spikeEdit.setFocusable(false);
		spikeEdit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				paint = "spike";
				updateEditButtons();
			}
		});
		otherButtonsPanel.add(spikeEdit);
		goalEdit = new JButton("Goal");
		goalEdit.setFocusable(false);
		goalEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				paint = "next level";
				updateEditButtons();
			}
		});
		otherButtonsPanel.add(goalEdit);
		startEdit = new JButton("Start");
		startEdit.setFocusable(false);
		startEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				paint = "player";
				updateEditButtons();
			}
		});
		otherButtonsPanel.add(startEdit);
		powerUpEdit = new JButton("Shield");
		powerUpEdit.setFocusable(false);
		powerUpEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(paint.equals("shield")) {
					paint = "double jump";
				} else if(paint.equals("double jump")) {
					paint = "spike destroyer";
				} else {
					paint = "shield";
				}
				updateEditButtons();
			}
		});
		otherButtonsPanel.add(powerUpEdit);
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
						if(((EnemyNoJump) a).speed == FAST_SPEED) {
							image.setRGB(x, y, (new Color(255, 255, 2).getRGB()));
						} else if(((EnemyNoJump) a).speed == SLOW_SPEED) {
							image.setRGB(x, y, (new Color(255, 255, 1).getRGB()));
						}
						break;
					case "enemy bullet left":
						if(((EnemyBullet) a).speed == FAST_SPEED) {
							image.setRGB(x, y, (new Color(242, 0, 0).getRGB()));
						} else {
							image.setRGB(x, y, (new Color(241, 0, 0).getRGB()));							
						}
						break;
					case "enemy bullet right":
						if(((EnemyBullet) a).speed == FAST_SPEED) {
							image.setRGB(x, y, (new Color(240, 0, 0).getRGB()));
						} else {
							image.setRGB(x, y, (new Color(239, 0, 0).getRGB()));							
						}
						break;
					case "enemy bullet up":
						if(((EnemyBullet) a).speed == FAST_SPEED) {
							image.setRGB(x, y, (new Color(238, 0, 0).getRGB()));
						} else {
							image.setRGB(x, y, (new Color(237, 0, 0).getRGB()));							
						}
						break;
					case "enemy bullet down":
						if(((EnemyBullet) a).speed == FAST_SPEED) {
							image.setRGB(x, y, (new Color(236, 0, 0).getRGB()));
						} else {
							image.setRGB(x, y, (new Color(235, 0, 0).getRGB()));							
						}
						break;
					case "wall shooter left":
						if(((Shooter) a).speed == FAST_SPEED) {
							image.setRGB(x, y, (new Color(234, 0, 0).getRGB()));
						} else {
							image.setRGB(x, y, (new Color(233, 0, 0).getRGB()));							
						}
						break;
					case "wall shooter right":
						if(((Shooter) a).speed == FAST_SPEED) {
							image.setRGB(x, y, (new Color(232, 0, 0).getRGB()));
						} else {
							image.setRGB(x, y, (new Color(231, 0, 0).getRGB()));							
						}
						break;
					case "wall shooter up":
						if(((Shooter) a).speed == FAST_SPEED) {
							image.setRGB(x, y, (new Color(230, 0, 0).getRGB()));
						} else {
							image.setRGB(x, y, (new Color(229, 0, 0).getRGB()));							
						}
						break;
					case "wall shooter down":
						if(((Shooter) a).speed == FAST_SPEED) {
							image.setRGB(x, y, (new Color(228, 0, 0).getRGB()));
						} else {
							image.setRGB(x, y, (new Color(227, 0, 0).getRGB()));							
						}
						break;
					case "enemy smart":
						if(((EnemySmart) a).speed == FAST_SPEED) {
							image.setRGB(x, y, (new Color(244, 0, 0).getRGB()));
						} else if(((EnemySmart) a).speed == SLOW_SPEED) {
							image.setRGB(x, y, (new Color(243, 0, 0).getRGB()));
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
					case "blue wall switch":
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
					case "red wall switch":
						image.setRGB(x, y, (new Color(252, 0, 0).getRGB()));
						break;
					case "wall disappearing":
						image.setRGB(x, y, (new Color(0, 254, 0).getRGB()));
						break;
					case "wall moving":
						int speed = 0;
						if(((WallMoving) a).speed == FAST_SPEED) {
							speed = 4;
						}
						switch(((WallMoving) a).direction) {
						case WallMoving.UP:
							image.setRGB(x, y, (new Color(0, 253 - speed, 0).getRGB()));
							break;
						case WallMoving.DOWN:
							image.setRGB(x, y, (new Color(0, 252 - speed, 0).getRGB()));
							break;
						case WallMoving.LEFT:
							image.setRGB(x, y, (new Color(0, 251 - speed, 0).getRGB()));
							break;
						case WallMoving.RIGHT:
							image.setRGB(x, y, (new Color(0, 250 - speed, 0).getRGB()));
							break;
						}
						break;
					case "enemy dumb":
						if(((EnemyDumb) a).goLeft) {
							if(((EnemyDumb) a).speed == FAST_SPEED) {
								image.setRGB(x, y, (new Color(251, 0, 0).getRGB()));
								break;
							} else if(((EnemyDumb) a).speed == SLOW_SPEED) {
								image.setRGB(x, y, (new Color(250, 0, 0).getRGB()));
								break;
							}
						} else {
							if(((EnemyDumb) a).speed == FAST_SPEED) {
								image.setRGB(x, y, (new Color(248, 0, 0).getRGB()));
								break;
							} else if(((EnemyDumb) a).speed == SLOW_SPEED) {
								image.setRGB(x, y, (new Color(247, 0, 0).getRGB()));
								break;
							}
						}
					case "shield":
						image.setRGB(x, y, (new Color(226, 0, 0).getRGB()));
						break;
					case "double jump":
						image.setRGB(x, y, (new Color(225, 0, 0).getRGB()));
						break;
					case "spike destroyer":
						image.setRGB(x,  y, (new Color(224, 0, 0).getRGB()));
						break;
					}
				}
				if(existsAPlayer) {
					File outFile;
					if(!editTemplates) {
						outFile = new File("level" + Integer.toString(levelNumber) + ".png");
					} else {
						outFile = new File("config/" + Integer.toString(levelNumber) + ".png");
					}
					try {
						ImageIO.write(image, "png", outFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		editNavButtonPanel.add(saveEdit);
		editPanel.add(Box.createVerticalGlue());
		editPanel.add(ep);
		highlightButton(wallEdit, editButtonPanel);		
		loadOptions();
		window.setVisible(true);
		while(true) {
			Thread.sleep(1/30);
			switch(STATE) {
			case "menu":
				STATE="menu0";
				if(fullscreen) {
					window.setExtendedState(JFrame.MAXIMIZED_BOTH);
					window.add(menuPanelFull);
				} else {
					window.setSize(200,300);
					window.add(menuPanel);
				}
				window.repaint();
				window.setVisible(true);
				break;
			case "score":
				STATE="score0";
				scorePanel = new JPanel();
				scorePanel.setBackground(Color.GRAY);
				scorePanel.setLayout(new BoxLayout(scorePanel, 1));
				JLabel scoreLabel = new JLabel("Score: " + Integer.toString(rp.score) + " pts");
				scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
				scoreLabel.setForeground(Color.WHITE);
				JLabel highscoreLabel = new JLabel("Highscore: " + Integer.toString(highscore) + " pts");
				highscoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
				highscoreLabel.setForeground(Color.WHITE);
				JPanel exitBtnsPanel = new JPanel();
				exitBtnsPanel.setBackground(Color.GRAY);
				exitBtnsPanel.setLayout(new BoxLayout(exitBtnsPanel, BoxLayout.X_AXIS));
				JButton exitToMenuBtn = new JButton("Exit to Menu");
				exitToMenuBtn.setBackground(Color.DARK_GRAY);
				exitToMenuBtn.setForeground(Color.WHITE);
				exitToMenuBtn.setFocusable(false);
				exitToMenuBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						STATE = "menu";
						window.remove(scorePanel);
					}
				});
				JButton playAgainBtn = new JButton("Play Again");
				playAgainBtn.setBackground(Color.DARK_GRAY);
				playAgainBtn.setForeground(Color.WHITE);
				playAgainBtn.setFocusable(false);
				playAgainBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						STATE = "random";
						window.remove(scorePanel);
					}
				});
				if(fullscreen) {
					Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
					updateSize(exitToMenuBtn, screen.getWidth() / 4, screen.getHeight() / 8);
					updateSize(playAgainBtn, screen.getWidth() / 4, screen.getHeight() / 8);
					exitToMenuBtn.setFont(new Font("TimesRoman", Font.BOLD, 40));
					playAgainBtn.setFont(new Font("TimesRoman", Font.BOLD, 40));
					scoreLabel.setFont(new Font("TimesRoman", Font.BOLD, 100));
					highscoreLabel.setFont(new Font("TimesRoman", Font.BOLD, 50));					
				} else {
					window.setSize(250, 250);
					scoreLabel.setFont(new Font("TimesRoman", Font.BOLD, 25));
					highscoreLabel.setFont(new Font("TimesRoman", Font.BOLD, 20));
				}
				window.add(scorePanel);
				scorePanel.add(Box.createVerticalGlue());  
				scorePanel.add(scoreLabel);
				scorePanel.add(highscoreLabel);
				scorePanel.add(Box.createVerticalGlue());
				scorePanel.add(exitBtnsPanel);
				scorePanel.add(Box.createVerticalGlue());
				exitBtnsPanel.add(Box.createHorizontalGlue());
				exitBtnsPanel.add(exitToMenuBtn);
				exitBtnsPanel.add(Box.createHorizontalGlue());
				exitBtnsPanel.add(playAgainBtn);
				exitBtnsPanel.add(Box.createHorizontalGlue());
				window.repaint();
				window.setVisible(true);
				break;
			case "random":
				STATE="random0";
				isBlueGateOpen = false;
				isRedGateOpen = false;
				if (level==null) {
					level = new ArrayList<Thing>();
				} else {
					level.clear();
				}
				levelMapStable = new Hashtable<Integer, Hashtable<Integer, Thing>>();
				levelMapMoving = new Hashtable<Integer, Hashtable<Integer, Thing>>();
				rp = new RandomPanel();
				window.add(rp);
				window.setVisible(true);
				if(fullscreen) {
					window.setExtendedState(JFrame.MAXIMIZED_BOTH);
				} else {
					window.setSize(700, 500);
				} 
				while(STATE.equals("random0")) {
					Date before = new Date();
					if(isEscapePressed) {
						final JPanel pausePanel = new JPanel();
						pausePanel.setBackground(Color.DARK_GRAY);
						pausePanel.setLayout(new BoxLayout(pausePanel, 1));
						JLabel pauseLabel = new JLabel("Paused");
						pauseLabel.setForeground(Color.WHITE);
						final InterThreadFlag flag = new InterThreadFlag(true);
						pauseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
						pausePanel.add(Box.createVerticalGlue());
						pausePanel.add(pauseLabel);
						pausePanel.add(Box.createVerticalGlue());
						JButton continueButton = new JButton("Return To Game");
						continueButton.setFocusable(false);
						continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
						continueButton.setBackground(Color.LIGHT_GRAY);
						continueButton.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								window.remove(pausePanel);
								window.add(rp);
								window.repaint();
								window.setVisible(true);
								isEscapePressed = false;
								flag.flag = false;
								window.setVisible(true);
							}
						});
						pausePanel.add(continueButton);
						pausePanel.add(Box.createVerticalGlue());
						JButton exitButton = new JButton("Exit To Menu");
						exitButton.setFocusable(false);
						exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
						exitButton.setBackground(Color.LIGHT_GRAY);
						exitButton.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								window.remove(pausePanel);
								STATE = "menu";
								isEscapePressed = false;
								flag.flag = false;
								window.setVisible(true);
							}
						});
						pausePanel.add(exitButton);
						if(fullscreen) {
							Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
							pauseLabel.setFont(new Font("TimesRoman", Font.BOLD, 100));
							updateSize(continueButton, 3 * screen.getWidth() / 4, screen.getHeight() / 8);
							continueButton.setFont(new Font("TimesRoman", Font.PLAIN, 50));
							updateSize(exitButton, 3 * screen.getWidth() / 4, screen.getHeight() / 8);
							exitButton.setFont(new Font("TimesRoman", Font.PLAIN, 50));
							pausePanel.add(Box.createVerticalGlue());
							pausePanel.add(Box.createRigidArea(new Dimension((int) (3 * screen.getWidth() / 4), (int) (screen.getHeight() / 8))));
						} else {
							pauseLabel.setFont(new Font("TimesRoman", Font.BOLD, 30));
							updateSize(continueButton, 200, 50);
							updateSize(exitButton, 200, 50);
							pausePanel.add(Box.createVerticalGlue());
							pausePanel.add(Box.createRigidArea(new Dimension(200, 50)));
						}
						window.add(pausePanel);
						Thread.yield();
						window.remove(rp);
						window.setVisible(true);
						while(flag.flag) {
							Thread.sleep(1000 / 30);
						}
					}
					for(Player i: player) {
						while(i.getX() - cameraX < window.getWidth() / 4) {
							cameraX --;
						}
						while(i.getX() - cameraX > 3 * window.getWidth() / 4) {
							cameraX ++;
						}
						while(i.getY() - cameraY < window.getHeight() / 4) {
							cameraY --;
						}
						while(i.getY() - cameraY > 3 * window.getHeight() / 4) {
							cameraY ++;
						}
					}
					window.repaint();
					Date after = new Date();
					while(after.getTime() - before.getTime() < 1000 / MAX_FRAMERATE) {
						Thread.sleep(1);
						after = new Date();
					}
				}
				break;
			case "edit":
				STATE="edit0";
				levelNumber=1;
				loadLevel();
				levelNumberDisplayEdit.setText("level 1");
				BoxLayout bl = new BoxLayout(editPanel, BoxLayout.PAGE_AXIS);
				if(fullscreen) {
					window.setExtendedState(JFrame.MAXIMIZED_BOTH);
					Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
					updateSize(editButtonPanel, screen.getWidth(), screen.getHeight() / 8);
					updateSize(ep, screen.getWidth(), 7 * screen.getHeight() / 8);
				} else {
					editButtonPanel.setMaximumSize(new Dimension(700,120));
					editButtonPanel.setPreferredSize(new Dimension(700,120));
					editButtonPanel.setMinimumSize(new Dimension(700,120));
					ep.setMaximumSize(new Dimension(700,455));
					ep.setPreferredSize(new Dimension(700, 455));
					ep.setMinimumSize(new Dimension(700,455));
					window.setSize(700, 645);
				}
				editPanel.setLayout(bl);
				editPanel.add(ep);
				for(Component i : editNavButtonPanel.getComponents()) {
					i.setBackground(new Color(150, 187, 255));
				}
				window.add(editPanel);
				window.setVisible(true);
				while(STATE.equals("edit0")) {
					Date before = new Date();
					window.repaint();
					window.setVisible(true);
					if(isWPressed) {
						cameraY -= SPRITE_HEIGHT / 25;
					} 
					if(isSPressed) {
						cameraY += SPRITE_HEIGHT / 25;
					}
					if(isAPressed) {
						cameraX -= SPRITE_WIDTH / 25;
					}
					if(isDPressed) {
						cameraX += SPRITE_WIDTH / 25;
					}
					if(isEscapePressed) {
						cameraX = startX - (int)(window.getWidth() * 0.5);
						cameraY = startY - (int)(window.getHeight() * 0.5);
					}
					Date after = new Date();
					while(after.getTime() - before.getTime() < 1000 / (MAX_FRAMERATE * 2)) {
						after = new Date();
					}
				}
				break;
			case "play":
				STATE="play0";
				final JPanel loadingPanel = new JPanel();
				loadingPanel.setLayout(new BoxLayout(loadingPanel, 1));
				loadingPanel.setBackground(Color.DARK_GRAY);
				loadingPanel.add(Box.createVerticalStrut(200));
				final JLabel loadingLabel = new JLabel("Loading...");
				loadingLabel.setFont(new Font("TimesRoman", Font.BOLD, 30));
				loadingLabel.setForeground(Color.WHITE);
				loadingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
				loadingPanel.add(loadingLabel);
				window.add(loadingPanel);
				window.repaint();
				levelNumber = 1;
				loadLevel();
				gp = new GamePanel();
				if(fullscreen) {
					window.setSize(Toolkit.getDefaultToolkit().getScreenSize());
				} else {
					window.setSize(700,500);
				}
				window.remove(loadingPanel);
				window.add(gp);
				window.setVisible(true);
				cameraY = (int) (player.get(0).getY() - (window.getHeight() / 2));
				while(STATE.equals("play0")) {
					Date before = new Date();
					if(isEscapePressed) {
						final JPanel pausePanel = new JPanel();
						pausePanel.setBackground(Color.DARK_GRAY);
						pausePanel.setLayout(new BoxLayout(pausePanel, 1));
						JLabel pauseLabel = new JLabel("Paused");
						pauseLabel.setForeground(Color.WHITE);
						final InterThreadFlag flag = new InterThreadFlag(true);
						pauseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
						pausePanel.add(Box.createVerticalGlue());
						pausePanel.add(pauseLabel);
						pausePanel.add(Box.createVerticalGlue());
						JButton continueButton = new JButton("Return To Game");
						continueButton.setFocusable(false);
						continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
						continueButton.setBackground(Color.LIGHT_GRAY);
						continueButton.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								window.remove(pausePanel);
								window.add(gp);
								window.repaint();
								window.setVisible(true);
								isEscapePressed = false;
								flag.flag = false;
								window.setVisible(true);
							}
						});
						pausePanel.add(continueButton);
						pausePanel.add(Box.createVerticalGlue());
						JButton exitButton = new JButton("Exit To Menu");
						exitButton.setFocusable(false);
						exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
						exitButton.setBackground(Color.LIGHT_GRAY);
						exitButton.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								window.remove(pausePanel);
								STATE = "menu";
								isEscapePressed = false;
								flag.flag = false;
								window.setVisible(true);
							}
						});
						pausePanel.add(exitButton);
						if(fullscreen) {
							Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
							pauseLabel.setFont(new Font("TimesRoman", Font.BOLD, 100));
							updateSize(continueButton, 3 * screen.getWidth() / 4, screen.getHeight() / 8);
							continueButton.setFont(new Font("TimesRoman", Font.PLAIN, 50));
							updateSize(exitButton, 3 * screen.getWidth() / 4, screen.getHeight() / 8);
							exitButton.setFont(new Font("TimesRoman", Font.PLAIN, 50));
							pausePanel.add(Box.createVerticalGlue());
							pausePanel.add(Box.createRigidArea(new Dimension((int) (3 * screen.getWidth() / 4), (int) (screen.getHeight() / 8))));
						} else {
							pauseLabel.setFont(new Font("TimesRoman", Font.BOLD, 30));
							updateSize(continueButton, 200, 50);
							updateSize(exitButton, 200, 50);
							pausePanel.add(Box.createVerticalGlue());
							pausePanel.add(Box.createRigidArea(new Dimension(200, 50)));
						}
						window.add(pausePanel);
						Thread.yield();
						window.remove(gp);
						window.setVisible(true);
						while(flag.flag) {
							Thread.sleep(1000 / 30);
						}
					}
					for(Player i: player) {
						while((i.getX() - cameraX) < window.getWidth() / 4) {
							cameraX --;
						}
						while((i.getX() - cameraX) > 3 * window.getWidth() / 4) {
							cameraX ++;
						}
						while((i.getY() - cameraY) < window.getHeight() / 4) {
							cameraY --;
						}
						while((i.getY() - cameraY) > 3 * window.getHeight() / 4) {
							cameraY ++;
						}
					}
					window.repaint();
					Date after = new Date();
					while(after.getTime() - before.getTime() < 1000 / MAX_FRAMERATE) {
						Thread.sleep(1);
						after = new Date();
					}
				}
				break;
			 case "options":
				STATE = "options0";
				final JPanel optionsPanel = new JPanel();
				optionsPanel.setBackground(Color.GRAY);
				optionsPanel.setLayout(new BoxLayout(optionsPanel, 1));
				window.add(optionsPanel);
				optionsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
				final JLabel optionsLabel = new JLabel("Options");
				optionsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
				optionsLabel.setForeground(Color.WHITE);
				optionsPanel.add(optionsLabel);
				optionsPanel.add(Box.createVerticalGlue());
				final JButton windowSizeBtn = new JButton();
				windowSizeBtn.setBackground(Color.DARK_GRAY);
				windowSizeBtn.setForeground(Color.WHITE);
				windowSizeBtn.setFocusable(false);
				final JButton musicBtn = new JButton();
				musicBtn.setBackground(Color.DARK_GRAY);
				musicBtn.setForeground(Color.WHITE);
				musicBtn.setFocusable(false);
				musicBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						if(Main.clip.isActive()) {
							Main.clip.stop();
							musicBtn.setText("Music off");
						} else {
							Main.clip.start();
					        Main.clip.loop(Clip.LOOP_CONTINUOUSLY); 
					        musicBtn.setText("Music on");
						}
						writeOptions();
					}
				});
				musicBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
				if(Main.clip.isActive()) {
					musicBtn.setText("Music on");
				} else {
					musicBtn.setText("Music off");
				}
				optionsPanel.add(musicBtn);
				optionsPanel.add(Box.createVerticalGlue());
				final JButton backOptionsBtn = new JButton("Back");
				windowSizeBtn.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0){
						fullscreen = !fullscreen;
						Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
						if(fullscreen) {
							window.setSize(Toolkit.getDefaultToolkit().getScreenSize());
							windowSizeBtn.setText("Fullscreen");
							optionsLabel.setFont(new Font("TimesRoman", Font.BOLD, 100));
							updateSize(windowSizeBtn, screen.getWidth() / 4, screen.getHeight() / 8);
							updateSize(backOptionsBtn, screen.getWidth() / 4, screen.getHeight() / 8);
							updateSize(musicBtn, screen.getWidth() / 4, screen.getHeight() / 8);
							windowSizeBtn.setFont(new Font("TimesRoman", Font.PLAIN, 50));
							backOptionsBtn.setFont(new Font("TimesRoman", Font.PLAIN, 50));
							musicBtn.setFont(new Font("TimesRoman", Font.PLAIN, 50));
						} else {
							window.setSize(200, 300);
							optionsLabel.setFont(new Font("TimesRoman", Font.BOLD, 30));
							updateSize(windowSizeBtn, 120, 40);
							updateSize(backOptionsBtn, 120, 40);
							updateSize(musicBtn, 120, 40);
							windowSizeBtn.setFont(new Font("TimesRoman", Font.PLAIN, 12));
							backOptionsBtn.setFont(new Font("TimesRoman", Font.PLAIN, 12));
							musicBtn.setFont(new Font("TimesRoman", Font.PLAIN, 12));
							windowSizeBtn.setText("Windowed");
						}
						writeOptions();
					}
				});
				windowSizeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
				optionsPanel.add(windowSizeBtn);
				optionsPanel.add(Box.createVerticalGlue());
				backOptionsBtn.setBackground(Color.DARK_GRAY);
				backOptionsBtn.setForeground(Color.WHITE);
				backOptionsBtn.setFocusable(false);
				backOptionsBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						window.remove(optionsPanel);
						STATE = "menu";
						window.repaint();
						window.setVisible(true);
					}
				});
				backOptionsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
				optionsPanel.add(backOptionsBtn);
				optionsPanel.add(Box.createVerticalGlue());
				Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
				if(fullscreen) {
					windowSizeBtn.setText("Fullscreen");
					optionsLabel.setFont(new Font("TimesRoman", Font.BOLD, 100));
					updateSize(windowSizeBtn, screen.getWidth() / 4, screen.getHeight() / 8);
					updateSize(backOptionsBtn, screen.getWidth() / 4, screen.getHeight() / 8);
					updateSize(musicBtn, screen.getWidth() / 4, screen.getHeight() / 8);
					windowSizeBtn.setFont(new Font("TimesRoman", Font.PLAIN, 50));
					backOptionsBtn.setFont(new Font("TimesRoman", Font.PLAIN, 50));
					musicBtn.setFont(new Font("TimesRoman", Font.PLAIN, 50));
				} else {
					optionsLabel.setFont(new Font("TimesRoman", Font.BOLD, 30));
					updateSize(windowSizeBtn, 120, 40);
					updateSize(backOptionsBtn, 120, 40);
					updateSize(musicBtn, 120, 40);
					windowSizeBtn.setFont(new Font("TimesRoman", Font.PLAIN, 12));
					backOptionsBtn.setFont(new Font("TimesRoman", Font.PLAIN, 12));
					musicBtn.setFont(new Font("TimesRoman", Font.PLAIN, 12));
					windowSizeBtn.setText("Windowed");
				}
				break;
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
		case "shield":
		case "double jump":
		case "spike destroyer":
			highlightButton(powerUpEdit, editButtonPanel);
			break;
		case "spike":
			highlightButton(spikeEdit, editButtonPanel);
			break;
		case "wall moving left":
		case "wall moving right":
		case "wall moving up":
		case "wall moving down":
			highlightButton(movingWallEdit, editButtonPanel);
			break;
		case "wall disappearing":
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
		case "enemy bullet left":
		case "enemy bullet right":
		case "enemy bullet up":
		case "enemy bullet down":
		case "wall shooter left":
		case "wall shooter right":
		case "wall shooter up":
		case "wall shooter down":
			highlightButton(bulletEdit, editButtonPanel);
			break;
		case "enemy dumb left":
		case "enemy dumb right":
		case "enemy no jump":
		case "enemy only jump":
		case "enemy smart":
			highlightButton(enemyEdit, editButtonPanel);
			break;
		case "player":
			highlightButton(startEdit, editButtonPanel);
			break;
		case "next level":
			highlightButton(goalEdit, editButtonPanel);
			break;
		}
		if(paint.equals("wall moving right")){
			movingWallEdit.setText("Moving Wall (R)");
		} else if(paint.equals("wall moving up")){
			movingWallEdit.setText("Moving Wall (U)");
		} else if(paint.equals("wall moving down")){
			movingWallEdit.setText("Moving Wall (D)");
		} else {
			movingWallEdit.setText("Moving Wall (L)");
		}
		if(paint.equals("wall disappearing")){
			wallEdit.setText("Temporary Wall");
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
		if(paint.equals("enemy smart")){
			enemyEdit.setText("Enemy (Smart)");
		} else if (paint.equals("enemy dumb right")) {
			enemyEdit.setText("Enemy (Dumb R)");
		} else if (paint.equals("enemy no jump")) {
			enemyEdit.setText("Enemy (No Jump)");
		} else if(paint.equals("enemy only jump")){
			enemyEdit.setText("Enemy (Jump)");
		} else {
			enemyEdit.setText("Enemy (Dumb L)");
		}
		
		if(paint.equals("enemy bullet down")) {
			bulletEdit.setText("Bullet (D)");
		} else if(paint.equals("enemy bullet right")) {
			bulletEdit.setText("Bullet (R)");
		} else if(paint.equals("enemy bullet up")) {
			bulletEdit.setText("Bullet (U)");
		} else if(paint.equals("wall shooter down")) {
			bulletEdit.setText("Shooter (D)");
		} else if(paint.equals("wall shooter right")) {
			bulletEdit.setText("Shooter (R)");
		} else if(paint.equals("wall shooter up")) {
			bulletEdit.setText("Shooter (U)");
		} else if(paint.equals("wall shooter left")) {
			bulletEdit.setText("Shooter (L)");
		} else {
			bulletEdit.setText("Bullet (L)");
		}
		
		if(paint.equals("double jump")) {
			powerUpEdit.setText("Double Jump");
		} else if(paint.equals("spike destroyer")) {
			powerUpEdit.setText("Spike Destroyer");
		} else {
			powerUpEdit.setText("Shield");
		}
	}
	
	public static Thing getFromMapMoving(int x, int y) {
		if(levelMapMoving.get((int) (x / SPRITE_WIDTH)) == null) {
			return null;
		} else {
			Thing a = levelMapMoving.get((int) (x / SPRITE_WIDTH)).get((int) (y / SPRITE_HEIGHT));
			if(!level.contains(a)) {
				return null;
			}
			return a;
		}
	}
	
	public static Thing getFromMapMoving(double x, double y) {
		return getFromMapMoving((int) x, (int) y);
	}
	
	public static Thing getFromMapStable(int x, int y) {
		if(levelMapStable.get((int) (x / SPRITE_WIDTH)) == null) {
			return null;
		} else {
			Thing a = levelMapStable.get((int) (x / SPRITE_WIDTH)).get((int) (y / SPRITE_HEIGHT));
			if(!level.contains(a)) {
				return null;
			}
			return a;
		}
	}
	
	public static Thing getFromMapStable(double x, double y) {
		return getFromMapStable((int) x, (int) y);
	}
	
	public static void putInMap(Thing a) {
		if(a.id.contains("enemy") || a.id.equals("player")) {
			if(levelMapMoving.get((int) a.getX() / SPRITE_WIDTH) == null) {
				levelMapMoving.put((int) (a.getX() / SPRITE_WIDTH), new Hashtable<Integer, Thing>());
			}
			levelMapMoving.get((int) (a.getX() / SPRITE_WIDTH)).put((int) (a.getY() / SPRITE_HEIGHT), a);
		} else {
			if(levelMapStable.get((int) a.getX() / SPRITE_WIDTH) == null) {
				levelMapStable.put((int) (a.getX() / SPRITE_WIDTH), new Hashtable<Integer, Thing>());
			}
			levelMapStable.get((int) (a.getX() / SPRITE_WIDTH)).put((int) (a.getY() / SPRITE_HEIGHT), a);
		}
	}
	
	public static void removeFromMap(Thing a) {
		try {
			if(a.id.contains("enemy") || a.id.equals("player")) {
				levelMapMoving.get((int) (a.getX() / SPRITE_HEIGHT)).remove((int) (a.getY() / SPRITE_WIDTH));
			} else {
				levelMapStable.get((int) (a.getX() / SPRITE_HEIGHT)).remove((int) (a.getY() / SPRITE_WIDTH));
			}
		} catch (Exception e) {
			
		}
	}
	
	public static void resetMap() {
		levelMapMoving = new Hashtable<Integer, Hashtable<Integer, Thing>>();
		levelMapStable = new Hashtable<Integer, Hashtable<Integer, Thing>>();
		level = new ArrayList<Thing>();
	}
	
	//resets level
	synchronized public static void loadLevel() {
		isBlueGateOpen = false;
		isRedGateOpen = false;
		File imgFile;
		deadPlayer = false;
		deadPlayerCounter = 100;
		if(!editTemplates) {
			imgFile = new File("./level" + Integer.toString(levelNumber) + ".png");
		} else {
			imgFile = new File("config/" + Integer.toString(levelNumber) + ".png");
		}
		resetMap();
		player = new ArrayList<Player>();
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
							Player p = new Player(SPRITE_WIDTH * x, SPRITE_HEIGHT * y);
							player.add(p);
							level.add(p);
						} else if(pixel.getRed() == 255 && pixel.getGreen() == 255 && pixel.getBlue() == 2) {
							level.add(new EnemyNoJump(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, FAST_SPEED));
						} else if(pixel.getRed() == 255 && pixel.getGreen() == 255 && pixel.getBlue() == 1) {
							level.add(new EnemyNoJump(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, SLOW_SPEED));
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
							level.add(new EnemyDumb(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, true, FAST_SPEED));
						} else if(pixel.getRed() == 250 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new EnemyDumb(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, true, SLOW_SPEED));
						} else if(pixel.getRed() == 248 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new EnemyDumb(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, false, FAST_SPEED));
						} else if(pixel.getRed() == 247 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new EnemyDumb(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, false, SLOW_SPEED));
						} else if(pixel.getRed() == 0 && pixel.getGreen() == 254 && pixel.getBlue() == 0) {
							level.add(new DisappearingWall(SPRITE_WIDTH * x, SPRITE_HEIGHT * y));
						} else if(pixel.getRed() == 0 && pixel.getGreen() == 253 && pixel.getBlue() == 0) {
							level.add(new WallMoving(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, WallMoving.UP, SLOW_SPEED));
						} else if(pixel.getRed() == 0 && pixel.getGreen() == 252 && pixel.getBlue() == 0) {
							level.add(new WallMoving(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, WallMoving.DOWN, SLOW_SPEED));
						} else if(pixel.getRed() == 0 && pixel.getGreen() == 251 && pixel.getBlue() == 0) {
							level.add(new WallMoving(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, WallMoving.LEFT, SLOW_SPEED));
						} else if(pixel.getRed() == 0 && pixel.getGreen() == 250 && pixel.getBlue() == 0) {
							level.add(new WallMoving(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, WallMoving.RIGHT, SLOW_SPEED));
						} else if(pixel.getRed() == 0 && pixel.getGreen() == 249 && pixel.getBlue() == 0) {
							level.add(new WallMoving(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, WallMoving.UP, FAST_SPEED));
						} else if(pixel.getRed() == 0 && pixel.getGreen() == 248 && pixel.getBlue() == 0) {
							level.add(new WallMoving(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, WallMoving.DOWN, FAST_SPEED));
						} else if(pixel.getRed() == 0 && pixel.getGreen() == 247 && pixel.getBlue() == 0) {
							level.add(new WallMoving(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, WallMoving.LEFT, FAST_SPEED));
						} else if(pixel.getRed() == 0 && pixel.getGreen() == 246 && pixel.getBlue() == 0) {
							level.add(new WallMoving(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, WallMoving.RIGHT, FAST_SPEED));
						} else if(pixel.getRed() == 245 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new Spike(SPRITE_WIDTH * x, SPRITE_HEIGHT * y));
						} else if(pixel.getRed() == 244 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new EnemySmart(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, FAST_SPEED));
						} else if(pixel.getRed() == 243 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new EnemySmart(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, SLOW_SPEED));
						} else if(pixel.getRed() == 242 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new EnemyBullet(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, EnemyBullet.LEFT, FAST_SPEED));
						} else if(pixel.getRed() == 241 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new EnemyBullet(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, EnemyBullet.LEFT, SLOW_SPEED));
						} else if(pixel.getRed() == 240 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new EnemyBullet(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, EnemyBullet.RIGHT, FAST_SPEED));
						} else if(pixel.getRed() == 239 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new EnemyBullet(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, EnemyBullet.RIGHT, SLOW_SPEED));
						} else if(pixel.getRed() == 238 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new EnemyBullet(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, EnemyBullet.UP, FAST_SPEED));
						} else if(pixel.getRed() == 237 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new EnemyBullet(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, EnemyBullet.UP, SLOW_SPEED));
						} else if(pixel.getRed() == 236 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new EnemyBullet(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, EnemyBullet.DOWN, FAST_SPEED));
						} else if(pixel.getRed() == 235 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new EnemyBullet(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, EnemyBullet.DOWN, SLOW_SPEED));
						} else if(pixel.getRed() == 234 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new Shooter(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, Shooter.LEFT, FAST_SPEED));
						} else if(pixel.getRed() == 233 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new Shooter(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, Shooter.LEFT, SLOW_SPEED));
						} else if(pixel.getRed() == 232 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new Shooter(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, Shooter.RIGHT, FAST_SPEED));
						} else if(pixel.getRed() == 231 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new Shooter(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, Shooter.RIGHT, SLOW_SPEED));
						} else if(pixel.getRed() == 230 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new Shooter(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, Shooter.UP, FAST_SPEED));
						} else if(pixel.getRed() == 229 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new Shooter(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, Shooter.UP, SLOW_SPEED));
						} else if(pixel.getRed() == 228 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new Shooter(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, Shooter.DOWN, FAST_SPEED));
						} else if(pixel.getRed() == 227 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new Shooter(SPRITE_WIDTH * x, SPRITE_HEIGHT * y, Shooter.DOWN, SLOW_SPEED));
						} else if(pixel.getRed() == 226 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new Shield(SPRITE_WIDTH * x, SPRITE_HEIGHT * y));
						} else if(pixel.getRed() == 225 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new DoubleJump(SPRITE_WIDTH * x, SPRITE_HEIGHT * y));
						} else if(pixel.getRed() == 224 && pixel.getGreen() == 0 && pixel.getBlue() == 0) {
							level.add(new SpikeDestroyer(SPRITE_WIDTH * x, SPRITE_HEIGHT * y));
						}
					}
				}
				for(Thing i: level) {
					putInMap(i);
				}
				cameraX = (int) (player.get(0).getX() - (window.getWidth() * 0.5));
				cameraY = (int) (player.get(0).getY() - (window.getHeight() * 0.5));
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
	
	public static void drawBackground(Graphics g, int x, int y) {
		int i = (int) (-0.25 * x);
		if(i < g.getClipBounds().getWidth()) {
			i -= background.getWidth();
			while(i < g.getClipBounds().getWidth()) {
				int j = (int) (-0.25 * y);
				if(j < g.getClipBounds().getHeight()) {
					j -= background.getHeight();
					while(j < g.getClipBounds().getHeight()) {
						g.drawImage(background, i, j, null);
						j += background.getHeight();
					}
				} else {
					while(j > 0) {
						g.drawImage(background, i, j, null);
						j -= background.getHeight();
					}
				}
				i += background.getWidth();
			}
		} else {
			while(i > 0) {
				int j = (int) (-0.25 * y);
				if(j < g.getClipBounds().getHeight()) {
					j -= background.getHeight();
					while(j < g.getClipBounds().getHeight()) {
						g.drawImage(background, i, j, null);
						j += background.getHeight();
					}
				} else {
					while(j > 0) {
						g.drawImage(background, i, j, null);
						j -= background.getHeight();
					}
				}
				i -= background.getWidth();
			}
		}
	}
	
	public static void updateSize(Component c, Dimension d) {
		c.setMinimumSize(d);
		c.setMaximumSize(d);
		c.setPreferredSize(d);
	}
	
	public static void updateSize(Component c, int width, int height) {
		updateSize(c, new Dimension(width, height));
	}
	
	public static void updateSize(Component c, double width, double height) {
		updateSize(c, new Dimension((int) width, (int) height));
	}
	
	public static void loadOptions() {
		try {
			FileReader optionsReader = new FileReader(new File("./config/options.bin"));
			if((char)optionsReader.read() == '1') {
				fullscreen = true;
			} else {
				fullscreen = false;
			}
			if((char)optionsReader.read() == '1') {
				if(!Main.clip.isActive()) {
					Main.clip.start();
			        Main.clip.loop(Clip.LOOP_CONTINUOUSLY); 
				}
			} else {
				if(Main.clip.isActive()) {
					Main.clip.stop();
				}
			}
			highscore = 0;
			int c;
			while((c = optionsReader.read()) != -1) {
				highscore *= 10;
				highscore += Character.getNumericValue((char) c);
			}
			optionsReader.close();
		} catch (IOException e) {	}
	}
	
	public static void writeOptions() {
		try {
			FileWriter optionsWriter = new FileWriter(new File("./config/options.bin"));
			String output = "";
			if(fullscreen) {
				output += "1";
			} else {
				output += "0";
			}
			if(Main.clip.isActive()) {
				output += "1";
			} else {
				output += "0";
			}
			output += Integer.toString(highscore);
			optionsWriter.write(output);
			optionsWriter.close();
		} catch(Exception e) {	}
	}
}

class MKeyListener extends KeyAdapter {
	@Override
	public void keyPressed(KeyEvent event) {
		switch(event.getKeyCode()) {
		case KeyEvent.VK_M:
			if(Main.clip.isActive()) {
				Main.clip.stop();
			} else {
				Main.clip.start();
		        Main.clip.loop(Clip.LOOP_CONTINUOUSLY); 
			}
			Main.writeOptions();
			break;
		case KeyEvent.VK_Z:
			Main.isZPressed = true;
			break;
		case KeyEvent.VK_SPACE:
			Main.isSpacePressed = true;
			if(Main.STATE.equals("score0")) {
				Main.window.remove(Main.scorePanel);
				Main.STATE="random";
			}
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
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
	    	Main.isWPressed = true;
	    	break;
	    case KeyEvent.VK_LEFT:
	    case KeyEvent.VK_A:
	    	Main.isAPressed = true;
	    	break;
	    case KeyEvent.VK_DOWN:
	    case KeyEvent.VK_S:
	    	Main.isSPressed = true;
	    	break;
	    case KeyEvent.VK_RIGHT:
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
		case KeyEvent.VK_Z:
			Main.isZPressed = false;
			break;
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
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
	    	Main.isWPressed = false;
	    	break;
	    case KeyEvent.VK_LEFT:
	    case KeyEvent.VK_A:
	    	Main.isAPressed = false;
	    	break;
	    case KeyEvent.VK_DOWN:
	    case KeyEvent.VK_S:
	    	Main.isSPressed = false;
	    	break;
	    case KeyEvent.VK_RIGHT:
	    case KeyEvent.VK_D:
	    	Main.isDPressed = false;
	    	break;
	    default:
	    	break;
	    }
	}
}

class InterThreadFlag{
	public boolean flag;
	InterThreadFlag(boolean flag){
		this.flag = flag;
	}
}