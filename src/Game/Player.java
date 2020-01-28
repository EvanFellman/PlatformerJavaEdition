package Game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Player extends Thing {
	private static final BufferedImage deadImage = Main.texturedImg.getSubimage(4 * Main.SPRITE_WIDTH, 4 * Main.SPRITE_HEIGHT, Main.SPRITE_WIDTH, Main.SPRITE_HEIGHT);
	public PlayerState playerState;
	private boolean didDoubleJump;
	private boolean wReleased;
	public Player(float x, float y) {
		super(x, y, "player", 1, 0);
		this.dx = 0;
		this.playerState = new PlayerState();
		this.didDoubleJump = false;
		this.wReleased = false;
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public boolean move() {
		if(!Main.isWPressed) {
			this.wReleased = true;
		}
		Main.removeFromMap(this);
		this.playerState.tick();
		if(!Main.deadPlayer) {
			if(this.dy > 15) {
				this.dy = 15;
			}
			if(Main.isAPressed) {
				if(this.dx > 0) {
					this.dx = 0;
				}
				if(this.dx > -4) {
					this.dx -= 0.5;
				}
			} else if(Main.isDPressed) {
				if(this.dx < 0) {
					this.dx = 0;
				}
				if(this.dx < 4) {
					this.dx += 0.5;
				}
			} else if(this.dx > 0){
				this.dx -= 0.5;
			} else if(this.dx < 0){
				this.dx += 0.5;
			}
			this.x += this.dx;
			boolean nearWalll = false, nearWallMovingl = false, nearWallr = false, nearWallMovingr = false;
			for(Thing a: Main.level) {
				if(a != null && ((this.playerState.equals(PlayerState.SHIELD) && a.id.equals("spike")) || a.id.contains("wall") || (!this.equals(a) && a.id.equals("player"))) && this.isTouching(a)) {
					if(a.x > this.x) {
						if(a.id.equals("wall moving")) {
							if(a.dx < 0) {
								nearWallMovingr = true;
							}
						} else {
							nearWallr = true;
						}
					} else {
						if(a.id.equals("wall moving")) {
							if(a.dx > 0) {
								nearWallMovingl = true;
							}
						} else {
							nearWalll = true;
						}
					}
					if((a.y - this.y < a.dy + Main.SPRITE_HEIGHT && this.y - a.y < Main.SPRITE_HEIGHT + a.dy)){
						if(this.x <= a.x - Main.SPRITE_WIDTH + this.dx && this.dx >= 0) {
							this.dx = 0;
							this.x = a.getX() - Main.SPRITE_WIDTH;
						} else if(this.x >= a.x + Main.SPRITE_WIDTH + this.dx && this.dx <= 0) {
							this.dx = 0;
							this.x = a.getX() + Main.SPRITE_WIDTH;
						}
					}
				}
			}
			if((nearWalll && nearWallMovingr) || (nearWallMovingl && nearWallr) || (nearWallMovingl && nearWallMovingr)) {
				this.die();
				return true;
			}
		}
		this.dy += Main.GRAVITY;
		this.y += this.dy;
		if(!Main.deadPlayer) {
			boolean nearWalla = false, nearWallMovinga = false, nearWallb = false, nearWallMovingb = false;
			boolean inAir = true;
			for(int i = 0; i < Main.level.size(); i++) {
				Thing a = Main.level.get(i);
				if(!this.equals(a) && this.isTouching(a)) {
					if(a.id.contains("wall") || (a.id.equals("spike") && !this.playerState.equals(PlayerState.SPIKEDESTROYER)) || (!this.equals(a) && a.id.equals("player"))) {
						if(a.y > this.y) {
							if(a.id.equals("wall moving")) {
								if(a.dy < 0) {
									nearWallMovinga = true;
								}
							} else {
								nearWalla = true;
							}
						} else {
							if(a.id.equals("wall moving")) {
								if(a.dy > 0) {
									nearWallMovingb = true;
								}
							} else {
								nearWallb = true;
							}
						}
						if(this.dy >= 0) {
							if(Main.isWPressed && a.getY() > this.y) {
								this.dy = -10;
								this.wReleased = false;
							} else if(this.dy > a.dy) {
								this.dy = 0;
							}
							if(a.getY() > this.y) {
								this.y = (a.y - Main.SPRITE_HEIGHT);
								this.didDoubleJump = false;
								inAir = false;
							} else if(a.getY() < this.y) {
								this.y = (a.y + Main.SPRITE_HEIGHT);
							}
						} else if(this.dy < 0) {
							this.dy = 0;
							if(this.x + Main.SPRITE_WIDTH > a.x && this.x - Main.SPRITE_WIDTH < a.x) {
								this.y = a.y + Main.SPRITE_HEIGHT;
							}
						}
						if(a.id.equals("spike")) {
							if(this.playerState.equals(PlayerState.SPIKEDESTROYER)) {
								a.die();
							} else {
								this.die();
							}
						}
					} else if(a.id.equals("next level")) {
						Main.levelNumber += 1;
						Main.loadLevel();
						Main.deadPlayer = false;
						return false;
					} else if(a.id.contains("enemy")) {
						if(this.y + 3 < a.y && this.dy >= 0) {
							if(Main.isWPressed) {
								this.dy = -15;
							} else {
								this.dy = 0;
							}
							a.die();
						} else {
							this.die();
							return Main.deadPlayer;
						}
					}
				}
			}
			if(Main.isWPressed && !this.didDoubleJump && inAir && this.wReleased && this.playerState.equals(PlayerState.DOUBLEJUMP)) {
				this.dy = -14;
				this.didDoubleJump = true;
			}
			if((nearWalla && nearWallMovingb) || (nearWallMovinga && nearWallb) || (nearWallMovinga && nearWallMovingb)) {
				this.die();
				return Main.deadPlayer;
			}
			if(this.y > Main.DEATH_BELOW) {
				this.die();
				return Main.deadPlayer;
			}
		}
		Main.putInMap(this);
		return false;
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public void die() {
		if(this.playerState.equals(PlayerState.SHIELD) && this.y <= Main.DEATH_BELOW) {
			if(this.playerState.timer == -1) {
				this.playerState.timer = 60;
			}
		} else {
			this.playerState.setValue(PlayerState.DEAD);
			this.playerState.timer = 75;
			Main.deadPlayerCounter = 75;
			Main.deadPlayer = true;
		}
	}
	
	@Override
	public void display(Graphics g) {
		if(Main.deadPlayer) {
			this.pic = deadImage;
		} else {
			this.pic = this.playerState.getImage();
		}
		super.display(g);
	}
}

class PlayerState{
	public final static Integer NONE = 0;
	public final static Integer DEAD = 1;
	public final static Integer SHIELD = 2;
	public final static Integer DOUBLEJUMP = 3;
	public final static Integer SPIKEDESTROYER = 4;
	private final static BufferedImage[] IMAGES = {
			Main.texturedImg.getSubimage(1 * Main.SPRITE_WIDTH, 0, Main.SPRITE_WIDTH, Main.SPRITE_HEIGHT),
			Main.texturedImg.getSubimage(4 * Main.SPRITE_WIDTH, 4 * Main.SPRITE_HEIGHT, Main.SPRITE_WIDTH, Main.SPRITE_HEIGHT),
			Main.texturedImg.getSubimage(5 * Main.SPRITE_WIDTH, 0, Main.SPRITE_WIDTH, Main.SPRITE_HEIGHT),
			Main.texturedImg.getSubimage(5 * Main.SPRITE_WIDTH, 2 * Main.SPRITE_HEIGHT, Main.SPRITE_WIDTH, Main.SPRITE_HEIGHT),
			Main.texturedImg.getSubimage(0 * Main.SPRITE_WIDTH, 5 * Main.SPRITE_HEIGHT, Main.SPRITE_WIDTH, Main.SPRITE_HEIGHT)
	};
	public int timer;
	private Integer value;
	
	public PlayerState(){
		this.value = NONE;
	}
	
	public PlayerState(Integer v) {
		this.value = v;
		this.timer = -1;
	}
	
	public void setValue(Integer v) {
		this.value = v;
		this.timer = -1;
	}
	
	public BufferedImage getImage() {
		if(this.timer > 0 && this.timer / 4 % 2 == 0) {
			return IMAGES[NONE];
		} else {
			return IMAGES[this.value];
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Integer) {
			return (Integer) o == this.value;
		} else if(!(o instanceof PlayerState)) {
			return false;
		} else {
			return ((PlayerState) o).value == this.value;
		}
	}
	
	public void tick() {
		if(this.timer > 0) {
			this.timer --;
		}
		if(this.timer == 0) {
			this.value = NONE;
			this.timer = -1;
		}
	}
}
