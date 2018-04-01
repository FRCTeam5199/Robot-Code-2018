package tools;

import java.awt.Graphics;
import java.util.ArrayList;

import gfx.Display;
import maths.Vector2;
import path.Path;
import path.PathNode;

public class SingleSelect implements Tool {

	private final int[] keyboardIn = {};
	private final int[] mouseIn = { 3 };
	private final InputCombo shortcut = new InputCombo(keyboardIn, mouseIn);

	private final AddSelect addSelect;

	public SingleSelect() {
		addSelect = new AddSelect();
	}

	@Override
	public InputCombo getShortcut() {
		return shortcut;
	}

	@Override
	public void init(Vector2 center, Path path, ArrayList<PathNode> selected, Display display) {
		addSelect.init(center, path, selected, display);
	}

	@Override
	public void update(Vector2 center, double typedNumber, double smoothRadius, Path path, ArrayList<PathNode> selected,
			Display display, ArrayList<Integer> keys, ArrayList<Integer> mouse) {
		selected.clear();
		addSelect.update(center, typedNumber, smoothRadius, path, selected, display, keys, mouse);
	}

	@Override
	public void draw(Graphics g, Display display) {
		// TODO Auto-generated method stub

	}

	@Override
	public void cleanup(Path path, ArrayList<PathNode> selected, Display display) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDone() {
		return addSelect.isDone();
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
