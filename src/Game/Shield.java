package Game;

public class Shield extends Thing{
	public Shield(double x, double y) {
		super(x, y, "shield", 5, 1);
	}
	
	@Override
	public boolean move() {
		for(Player p: Main.player) {
			if(p.isTouching(this)) {
				p.playerState.setValue(PlayerState.SHIELD);
				this.die();
			}
		}
		return false;
	}
	
	@Override
	public void die() {
		Main.removeFromMap(this);
		Main.level.remove(this);
	}
}
