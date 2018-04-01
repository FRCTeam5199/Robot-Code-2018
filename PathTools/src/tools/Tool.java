package tools;

import java.awt.Graphics;
import java.util.ArrayList;

import gfx.Display;
import maths.Vector2;
import path.Path;
import path.PathNode;

public interface Tool {
	public InputCombo getShortcut();

	public void init(Vector2 center, Path path, ArrayList<PathNode> selected, Display display);

	public void update(Vector2 center, double typedNumber, double smoothRadius, Path path, ArrayList<PathNode> selected, Display display,
			ArrayList<Integer> keys, ArrayList<Integer> mouse);

	public void cleanup(Path path, ArrayList<PathNode> selected, Display display);

	public boolean isDone();

	public boolean clearKeys();

	public boolean clearMouse();

	void draw(Graphics g, Display display);

}
