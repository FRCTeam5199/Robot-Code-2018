package tools;

import java.awt.Graphics;
import java.util.ArrayList;

import gfx.Display;
import maths.Vector2;
import path.Path;
import path.PathNode;

public class MoveCursor implements Tool {

	private final int[] keyboardIn = {};
	private final int[] mouseIn = { 1 };
	private final InputCombo shortcut = new InputCombo(keyboardIn, mouseIn);

	private boolean isDone;

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
		display.setCursorPos(display.toField(new Vector2(display.getMousePosition())));
		isDone = true;
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
		return true;
	}

}
