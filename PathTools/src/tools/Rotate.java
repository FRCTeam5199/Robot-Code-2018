package tools;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import gfx.Display;
import maths.Vector2;
import path.Path;
import path.PathNode;

public class Rotate implements Tool {

	private final double degToRad = Math.PI / 180d;

	private final int[] keyboardIn = { 'R' };
	private final int[] mouseIn = {};
	private final InputCombo shortcut = new InputCombo(keyboardIn, mouseIn);

	private ArrayList<PathNode> oldNodes;
	private Vector2 center;
	private double startAngle;
	private boolean isDone;

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

		Vector2 mousePos = display.toField(new Vector2(display.getMousePosition()));

		startAngle = Math.atan2(mousePos.getY() - center.getY(), mousePos.getX() - center.getX());

		isDone = selected.isEmpty();

		display.lockScale();
	}

	@Override
	public void update(Vector2 center, double typedNumber, double smoothRadius, Path path, ArrayList<PathNode> selected,
			Display display, ArrayList<Integer> keys, ArrayList<Integer> mouse) {

		Vector2 mousePos = display.toField(new Vector2(display.getMousePosition()));

		double rotation;
		if (Double.isNaN(typedNumber)) {
			rotation = Math.atan2(mousePos.getY() - center.getY(), mousePos.getX() - center.getX()) - startAngle;
		} else {
			rotation = typedNumber * degToRad;
		}

		for (int i = 0; i < selected.size(); i++) {
			selected.get(i).setPos(
					Vector2.add(Vector2.rotate(Vector2.subtract(oldNodes.get(i).getPos(), center), rotation), center));
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

	public void draw(Graphics g, Display display) {
		g.setColor(Color.LIGHT_GRAY);
		Vector2 center = display.toScreen(this.center);
		Vector2 mousePos = new Vector2(display.getMousePosition());
		g.drawLine((int) center.getX(), (int) center.getY(), (int) mousePos.getX(), (int) mousePos.getY());
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
