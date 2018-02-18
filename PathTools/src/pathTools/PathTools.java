package pathTools;

import gfx.Display;
import path.Path;
import path.RecordedPaths;
import util.ClockRegulator;

public class PathTools {
	public static void main(String[] args) {
		ClockRegulator clockRegulator = new ClockRegulator(60);
		Path path = RecordedPaths.main2();
		Display display = new Display(path);
		
		while(true){
			display.update();
			
			clockRegulator.sync();
		}
	}
}
