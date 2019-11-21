package Game;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Wall extends Thing {
	public Wall(float x, float y) throws IOException {
		super(x, y, "wall", ImageIO.read(new File("wall.png")));
	}
}
