package tools;

import java.awt.Graphics;
import java.util.ArrayList;

import gfx.Display;
import maths.Vector2;
import path.Path;
import path.PathNode;

public class SelectAll implements Tool {

	private final int[] keyboardIn = { 'A' };
	private final int[] mouseIn = {};
	private final InputCombo shortcut = new InputCombo(keyboardIn, mouseIn);

	private boolean isDone;

	public SelectAll() {

	}

	@Override
	public InputCombo getShortcut() {
		return shortcut;
	}

	@Override
	public void init(Vector2 center, Path path, ArrayList<PathNode> selected, Display display) {
		isDone = false;
	}

	@Override
	public void update(Vector2 center, double typedNumber, double smoothRadius, Path path, ArrayList<PathNode> selected,
			Display display, ArrayList<Integer> keys, ArrayList<Integer> mouse) {
		if (selected.isEmpty()) {
			for (int i = 0; i < path.getLength(); i++) {
				selected.add(path.getCheckpoint(i));
			}
		} else {
			selected.clear();
		}
		isDone = true;
	}

	@Override
	public void draw(Graphics g, Display display) {
	}

	@Override
	public void cleanup(Path path, ArrayList<PathNode> selected, Display display) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return isDone;
	}

	@Override
	public boolean clearKeys() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean clearMouse() {
		// TODO Auto-generated method stub
		return false;
	}

}
