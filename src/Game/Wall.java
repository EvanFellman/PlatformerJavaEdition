package Game;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

public class Wall extends Thing {

	public Wall(float x, float y) throws IOException {
		super(x, y, "wall", ImageIO.read(new File("wall.png")));
		// TODO Auto-generated constructor stub
	}

	@Override
	void move(Hashtable<Integer, Hashtable<Integer, Thing>> map) {
		//It's a wall. Can't move.
	}

}
