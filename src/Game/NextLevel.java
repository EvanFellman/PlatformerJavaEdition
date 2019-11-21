package Game;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class NextLevel extends Thing {

	public NextLevel(float x, float y) throws IOException {
		super(x, y, "next level", ImageIO.read(new File("next level.png")));
	}

}
