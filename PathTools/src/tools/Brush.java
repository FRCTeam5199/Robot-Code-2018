package tools;

import java.awt.Graphics;
import java.util.ArrayList;

import gfx.Display;
import maths.Vector2;
import path.Path;
import path.PathNode;

public class Brush implements Tool {

	private final int[] keyboardIn = { 17, 'B' };
	private final int[] mouseIn = {};
	private final InputCombo shortcut = new InputCombo(keyboardIn, mouseIn);

	private Vector2 offset;
	private int newStart;
	private boolean isDone;

	@Override
	public InputCombo getShortcut() {
		return shortcut;
	}

	@Override
	public void init(Vector2 center, Path path, ArrayList<PathNode> selected, Display display) {
		offset = Vector2.subtract(path.getCheckpoint(path.getLength() - 1).getPos(),
				display.toField(new Vector2(display.getMousePosition())));
		isDone = false;

		newStart = path.getLength();

		display.lockScale();
	}

	@Override
	public void update(Vector2 center, double typedNumber, double smoothRadius, Path path, ArrayList<PathNode> selected,
			Display display, ArrayList<Integer> keys, ArrayList<Integer> mouse) {
		Vector2 newPos = Vector2.add(offset, display.toField(new Vector2(display.getMousePosition())));

		if (Vector2.distance(newPos, path.getCheckpoint(path.getLength() - 1).getPos()) > 6) {
			path.addCheckpoint(new PathNode(newPos, 0));
		}

		// path.getCheckpoint(path.getLength() - 1).setPos(newPos);

		if (keys.contains(10)) {
			isDone = true;
		}
		if (mouse.contains(1)) {
			isDone = true;
		}
	}

	@Override
	public void cleanup(Path path, ArrayList<PathNode> selected, Display display) {
		display.unlockScale();
	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return isDone;
	}

	@Override
	public void draw(Graphics g, Display display) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean clearKeys() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean clearMouse() {
		// TODO Auto-generated method stub
		return false;
	}

}
