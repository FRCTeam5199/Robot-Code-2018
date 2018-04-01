package tools;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import gfx.Display;
import maths.Vector2;
import path.Path;
import path.PathNode;

public class Scale implements Tool {

	private final int[] keyboardIn = { 'S' };
	private final int[] mouseIn = {};
	private final InputCombo shortcut = new InputCombo(keyboardIn, mouseIn);

	private final double kScale = .01;
	private final double kScaleSlow = .001;

	private ArrayList<PathNode> oldNodes;
	private Vector2 center;
	private double startDist;
	private boolean isDone;

	private int axisLock;

	@Override
	public InputCombo getShortcut() {
		return shortcut;
	}

	@Override
	public void init(Vector2 center, Path path, ArrayList<PathNode> selected, Display display) {
		double totalX = 0;
		double totalY = 0;

		oldNodes = new ArrayList<PathNode>();

		for (PathNode node : selected) {
			oldNodes.add(node.clone());
		}

		this.center = center;
		startDist = Vector2.distance(center, display.toField(new Vector2(display.getMousePosition())));

		isDone = selected.isEmpty();

		axisLock = 0;

		display.lockScale();
	}

	@Override
	public void update(Vector2 center, double typedNumber, double smoothRadius, Path path, ArrayList<PathNode> selected,
			Display display, ArrayList<Integer> keys, ArrayList<Integer> mouse) {
		double scale;

		if (!Double.isNaN(typedNumber)) {
			scale = typedNumber;
		} else if (keys.contains(16)) {
			scale = kScaleSlow
					* (Vector2.distance(center, display.toField(new Vector2(display.getMousePosition()))) - startDist)
					+ 1;
		} else {
			scale = kScale
					* (Vector2.distance(center, display.toField(new Vector2(display.getMousePosition()))) - startDist)
					+ 1;
		}

		switch (axisLock) {

		case 0:
			for (int i = 0; i < selected.size(); i++) {
				selected.get(i).setPos(Vector2
						.add(Vector2.multiply(Vector2.subtract(oldNodes.get(i).getPos(), center), scale), center));
			}
			break;
		case 1:
			for (int i = 0; i < selected.size(); i++) {
				selected.get(i).getPos().setY(oldNodes.get(i).getPos().getY());
				selected.get(i).getPos()
						.setX((oldNodes.get(i).getPos().getX() - center.getX()) * scale + center.getX());
			}
			break;
		case 2:
			for (int i = 0; i < selected.size(); i++) {
				selected.get(i).getPos().setX(oldNodes.get(i).getPos().getX());
				selected.get(i).getPos()
						.setY((oldNodes.get(i).getPos().getY() - center.getY()) * scale + center.getY());
			}
			break;
		}

		if (keys.contains((int) 'X')) {
			axisLock = 1;
		} else if (keys.contains((int) 'Y')) {
			axisLock = 2;
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
		g.setColor(Color.LIGHT_GRAY);

		Vector2 center = display.toScreen(this.center);
		Vector2 mousePos = new Vector2(display.getMousePosition());
		g.drawLine((int) center.getX(), (int) center.getY(), (int) mousePos.getX(), (int) mousePos.getY());

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
