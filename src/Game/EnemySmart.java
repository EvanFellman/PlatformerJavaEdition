package Game;

public class EnemySmart extends Enemy {
	public double speed;
	public EnemySmart(double x, double y, double speed) {
		super(x, y, "enemy smart", 2, 3);
		this.speed = speed;
	}
	
	public boolean move() {
		//set dx
		if(this.dx == 0) {
			this.dx = Main.player.getX() < this.x ? this.speed * -1 : this.speed;
		} else if(Main.player.getX() < this.x - (Main.SPRITE_WIDTH * 2)) {
			this.dx = this.speed * -1;
		} else if(Main.player.getX() > this.x + (Main.SPRITE_WIDTH * 2)) {
			this.dx = this.speed;
		}
		//Figure out if allowed to jump
		boolean wallDirectlyBelow = false;
		for(int i = -1; i <= 1; i++) {
			for(int j = 0; j <= 1; j++) {
				Thing a = Main.getFromMapStable(this.x + (i * Main.SPRITE_WIDTH), this.y + (j * Main.SPRITE_HEIGHT));
				if(a != null && this.isNextTo(a) && !this.equals(a) && a.id.contains("wall") && this.y < a.y && this.x - a.x < Main.SPRITE_WIDTH && a.x - this.x < Main.SPRITE_WIDTH) {
					wallDirectlyBelow = true;
				}
				a = Main.getFromMapMoving(this.x + (i * Main.SPRITE_WIDTH), this.y + (j * Main.SPRITE_HEIGHT));
				if(a != null && this.isNextTo(a) && !this.equals(a) && a.id.contains("enemy") && this.y < a.y && this.x - a.x < Main.SPRITE_WIDTH && a.x - this.x < Main.SPRITE_WIDTH) {
					wallDirectlyBelow = true;
				}
			}
		}
		if(wallDirectlyBelow) {
			//Can jump
			boolean wallNextToMe = false;
			if(this.dx < 0) {
				for(int i = -1; i <= 0; i++) {
					Thing a = Main.getFromMapStable(this.x + (i * Main.SPRITE_WIDTH), this.y);
					if(a != null && this.isNextTo(a) && !this.equals(a) && a.id.contains("wall")) {
						wallNextToMe = true;
						break;
					}
					a = Main.getFromMapMoving(this.x + (i * Main.SPRITE_WIDTH), this.y);
					if(a != null && this.isNextTo(a) && !this.equals(a) && a.id.contains("enemy")) {
						wallNextToMe = true;
						break;
					}
				}
			} else if(this.dx > 0) {
				for(int i = 0; i <= 1; i++) {
					Thing a = Main.getFromMapStable(this.x + (i * Main.SPRITE_WIDTH), this.y);
					if(a != null && this.isNextTo(a) && !this.equals(a) && a.id.contains("wall")) {
						wallNextToMe = true;
						break;
					}
					a = Main.getFromMapMoving(this.x + (i * Main.SPRITE_WIDTH), this.y);
					if(a != null && this.isNextTo(a) && !this.equals(a) && a.id.contains("enemy")) {
						wallNextToMe = true;
						break;
					}
				}
			}
			//Now I know if there is a wall directly next to me
			boolean floorLeft = false;
			if(this.dx < 0) {
				for(int i = -1; i <= 0; i++) {
					for(int j = 0; j <= 1; j++) {
						Thing a = Main.getFromMapMoving(this.x + (i * Main.SPRITE_WIDTH), this.y + (j * Main.SPRITE_HEIGHT));
						if (a != null && this.isNextTo(a) && !this.equals(a) && a.id.contains("enemy") && this.y < a.y) {
							floorLeft = true;
							break;
						}
						a = Main.getFromMapStable(this.x + (i * Main.SPRITE_WIDTH), this.y + (j * Main.SPRITE_HEIGHT));
						if (a != null && this.isNextTo(a) && !this.equals(a) && a.id.contains("wall") && this.y < a.y) {
							floorLeft = true;
							break;
						}
					}
				}
			} else if(this.dx > 0) {
				for(int j = 0; j <= 1; j++) {
					Thing a = Main.getFromMapMoving(this.x + Main.SPRITE_WIDTH, this.y + (j * Main.SPRITE_HEIGHT));
					if (a != null && this.isNextTo(a) && !this.equals(a) && a.id.contains("enemy") && this.y < a.y) {
						floorLeft = true;
						break;
					}
					a = Main.getFromMapStable(this.x + Main.SPRITE_WIDTH, this.y + (j * Main.SPRITE_HEIGHT));
					if (a != null && this.isNextTo(a) && !this.equals(a) && a.id.contains("wall") && this.y < a.y) {
						floorLeft = true;
						break;
					}
				}
			}
			//Now I know if there is no floor left
			if(wallNextToMe || !floorLeft) {
				boolean somethingToJumpTo = false;
				boolean somethingToFallTo = false;
				if(this.dx > 0) {
					for(int i = 1; i <= (this.speed == Main.FAST_SPEED ? 3 : 2); i++) {
						for(int j = -2; j <= 1; j++) {
							Thing a = Main.getFromMapStable(this.x + (i * Main.SPRITE_WIDTH), this.y + (j * Main.SPRITE_HEIGHT));
							Thing aboveA = Main.getFromMapStable(this.x + (i * Main.SPRITE_WIDTH), this.y + ((j - 1) * Main.SPRITE_HEIGHT));
							if((aboveA == null || !aboveA.id.contains("wall")) && a != null && a.id.contains("wall")) {
								somethingToJumpTo = true;
							}
							a = Main.getFromMapMoving(this.x + (i * Main.SPRITE_WIDTH), this.y + (j * Main.SPRITE_HEIGHT));
							if((aboveA == null || !aboveA.id.contains("wall")) && a != null && a.id.contains("enemy")) {
								somethingToJumpTo = true;
							}
						}
					}
					for(int i = 0; i <= (this.speed == Main.FAST_SPEED ? 3 : 2); i++) {
						for(int j = 2; j <= 5; j++) {
							Thing a = Main.getFromMapStable(this.x + (i * Main.SPRITE_WIDTH), this.y + (j * Main.SPRITE_HEIGHT));
							Thing aboveA = Main.getFromMapStable(this.x + (i * Main.SPRITE_WIDTH), this.y + ((j - 1) * Main.SPRITE_HEIGHT));
							if((aboveA == null || !aboveA.id.contains("wall")) && a != null && a.id.contains("wall")) {
								somethingToFallTo = true;
							}
							a = Main.getFromMapMoving(this.x + (i * Main.SPRITE_WIDTH), this.y + (j * Main.SPRITE_HEIGHT));
							if((aboveA == null || !aboveA.id.contains("wall")) && a != null && a.id.contains("enemy")) {
								somethingToFallTo = true;
							}
						}
					}
				} else {
					for(int i = (this.speed == Main.FAST_SPEED ? -3 : -2); i <= -1; i++) {
						for(int j = -2; j <= 1; j++) {
							Thing a = Main.getFromMapStable(this.x + (i * Main.SPRITE_WIDTH), this.y + (j * Main.SPRITE_HEIGHT));
							Thing aboveA = Main.getFromMapStable(this.x + (i * Main.SPRITE_WIDTH), this.y + ((j - 1) * Main.SPRITE_HEIGHT));
							if((aboveA == null || !aboveA.id.contains("wall")) && a != null && a.id.contains("wall")) {
								somethingToJumpTo = true;
							}
							a = Main.getFromMapMoving(this.x + (i * Main.SPRITE_WIDTH), this.y + (j * Main.SPRITE_HEIGHT));
							if((aboveA == null || !aboveA.id.contains("wall")) && a != null && a.id.contains("enemy")) {
								somethingToJumpTo = true;
							}
						}
					}
					for(int i = (this.speed == Main.FAST_SPEED ? -3 : -2); i <= 0; i++) {
						for(int j = 2; j <= 5; j++) {
							Thing a = Main.getFromMapStable(this.x + (i * Main.SPRITE_WIDTH), this.y + (j * Main.SPRITE_HEIGHT));
							Thing aboveA = Main.getFromMapStable(this.x + (i * Main.SPRITE_WIDTH), this.y + ((j - 1) * Main.SPRITE_HEIGHT));
							if((aboveA == null || !aboveA.id.contains("wall")) && a != null && a.id.contains("wall")) {
								somethingToFallTo = true;
							}
							a = Main.getFromMapMoving(this.x + (i * Main.SPRITE_WIDTH), this.y + (j * Main.SPRITE_HEIGHT));
							if((aboveA == null || !aboveA.id.contains("wall")) && a != null && a.id.contains("enemy")) {
								somethingToFallTo = true;
							}
						}
					}
				}
				if(!somethingToFallTo && somethingToJumpTo) {
					this.dy = -2;
				} else if(somethingToFallTo && somethingToJumpTo && (wallNextToMe || this.y >= Main.player.getY())) {
					this.dy = -2;
				} else if(!somethingToFallTo && !somethingToJumpTo) {
					this.dx = 0;
				}
			}
		}
		return super.move();
	}

}
