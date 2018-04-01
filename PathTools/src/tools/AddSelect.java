package tools;

import java.awt.Graphics;
import java.util.ArrayList;

import gfx.Display;
import maths.Vector2;
import path.Path;
import path.PathNode;

public class AddSelect implements Tool {

	private final int[] keyboardIn = { 16 };
	private final int[] mouseIn = { 3 };
	private final InputCombo shortcut = new InputCombo(keyboardIn, mouseIn);

	private final int maxDist = 20;

	private boolean isDone;

	@Override
	public InputCombo getShortcut() {
		return shortcut;
	}

	@Override
	public void update(Vector2 center, double typedNumber, double smoothRadius, Path path, ArrayList<PathNode> selected,
			Display display, ArrayList<Integer> keys, ArrayList<Integer> mouse) {
		PathNode node = null;
		double dist = Double.POSITIVE_INFINITY;
		for (int i = 1; i < path.getLength(); i++) {
			Vector2 screenPos = display.toScreen(path.getCheckpoint(i).getPos());
			double nodeMouseDist = Vector2.distance(screenPos, new Vector2(display.getMousePosition()));
			if (nodeMouseDist < maxDist && nodeMouseDist < dist) {
				node = path.getCheckpoint(i);
				dist = nodeMouseDist;
			}
		}

		if (node != null) {
			if (selected.contains(node)) {
				selected.remove(node);
			} else {
				selected.add(node);
			}
		}

		isDone = true;
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
		return isDone;
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

	@Override
	public void init(Vector2 center, Path path, ArrayList<PathNode> selected, Display display) {
		// TODO Auto-generated method stub
		isDone = false;
	}

}
