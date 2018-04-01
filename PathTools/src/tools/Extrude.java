package tools;

import java.awt.Graphics;
import java.util.ArrayList;

import gfx.Display;
import maths.Vector2;
import path.Path;
import path.PathNode;

public class Extrude implements Tool {

	private final int[] keyboardIn = { 'E' };
	private final int[] mouseIn = {};
	private final InputCombo shortcut = new InputCombo(keyboardIn, mouseIn);

	private final Grab grab;

	public Extrude() {
		grab = new Grab();
	}

	@Override
	public InputCombo getShortcut() {
		return shortcut;
	}

	@Override
	public void init(Vector2 center, Path path, ArrayList<PathNode> selected, Display display) {
		path.addCheckpoint(new PathNode(path.getCheckpoint(path.getLength() - 1).getPos(), 0));
		selected.clear();
		selected.add(path.getCheckpoint(path.getLength() - 1));
		grab.init(center, path, selected, display);
	}

	@Override
	public void update(Vector2 center, double typedNumber, double smoothRadius, Path path, ArrayList<PathNode> selected,
			Display display, ArrayList<Integer> keys, ArrayList<Integer> mouse) {
		grab.update(center, typedNumber, smoothRadius, path, selected, display, keys, mouse);
	}

	@Override
	public void cleanup(Path path, ArrayList<PathNode> selected, Display display) {
		// TODO Auto-generated method stub
		grab.cleanup(path, selected, display);
	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return grab.isDone();
	}

	@Override
	public void draw(Graphics g, Display display) {
		// TODO Auto-generated method stub
		grab.draw(g, display);
	}

	@Override
	public boolean clearKeys() {
		// TODO Auto-generated method stub
		return grab.clearKeys();
	}

	@Override
	public boolean clearMouse() {
		// TODO Auto-generated method stub
		return grab.clearMouse();
	}

}
