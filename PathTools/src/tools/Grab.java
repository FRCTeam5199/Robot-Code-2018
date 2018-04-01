package tools;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import gfx.Display;
import maths.Vector2;
import path.Path;
import path.PathNode;

public class Grab implements Tool {

	private final int[] keyboardIn = { 'G' };
	private final int[] mouseIn = {};
	private final InputCombo shortcut = new InputCombo(keyboardIn, mouseIn);
	private final double kSlow = .1;

	private ArrayList<PathNode> oldNodes;

	private Vector2 startPos;
	private Vector2 center;

	private boolean isDone;

	private int axisLock;

	@Override
	public InputCombo getShortcut() {
		return shortcut;
	}

	@Override
	public void init(Vector2 center, Path path, ArrayList<PathNode> selected, Display display) {
		display.lockScale();
		startPos = display.toField(new Vector2(display.getMousePosition()));
		oldNodes = new ArrayList<PathNode>();

		for (PathNode node : selected) {
			oldNodes.add(node.clone());
		}

		this.center = center;

		axisLock = 0;

		isDone = selected.isEmpty();
	}

	@Override
	public void update(Vector2 center, double typedNumber, double smoothRadius, Path path, ArrayList<PathNode> selected,
			Display display, ArrayList<Integer> keys, ArrayList<Integer> mouse) {

		Vector2 delta = Vector2.subtract(display.toField(new Vector2(display.getMousePosition())), startPos);

		if (keys.contains(16)) {
			delta = Vector2.multiply(delta, kSlow);
		}

		switch (axisLock) {
		case 0:
			for (int i = 0; i < selected.size(); i++) {
				selected.get(i).setPos(Vector2.add(oldNodes.get(i).getPos(), delta));
			}
			break;
		case 1:
			for (int i = 0; i < selected.size(); i++) {
				if (!Double.isNaN(typedNumber)) {
					delta.setX(typedNumber);
				}

				selected.get(i).getPos().setX(oldNodes.get(i).getPos().getX() + delta.getX());
				selected.get(i).getPos().setY(oldNodes.get(i).getPos().getY());
			}
			break;
		case 2:
			for (int i = 0; i < selected.size(); i++) {
				if (!Double.isNaN(typedNumber)) {
					delta.setY(typedNumber);
				}
				selected.get(i).getPos().setY(oldNodes.get(i).getPos().getY() + delta.getY());
				selected.get(i).getPos().setX(oldNodes.get(i).getPos().getX());
			}
			break;
		}

		if (keys.contains((int) 'X')) {
			axisLock = 1;
		} else if (keys.contains((int) 'Y')) {
			axisLock = 2;
		}

		if (smoothRadius > 0) {

		}

		if (keys.contains(27)) {
			for (int i = 0; i < selected.size(); i++) {
				selected.get(i).setPos(oldNodes.get(i).getPos());
			}
			isDone = true;
		}

		if (keys.contains(10) || mouse.contains(1)) {
			isDone = true;
		}

	}

	@Override
	public void cleanup(Path path, ArrayList<PathNode> selected, Display display) {
		// TODO Auto-generated method stub
		display.unlockScale();
	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return isDone;
	}

	@Override
	public void draw(Graphics g, Display display) {
		Vector2 center = display.toScreen(this.center);

		g.setColor(Color.LIGHT_GRAY);

		switch (axisLock) {
		case 1:
			g.drawLine(0, (int) center.getY(), display.getWidth(), (int) center.getY());
			break;
		case 2:
			g.drawLine((int) center.getX(), 0, (int) center.getX(), display.getHeight());
			break;
		}

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
