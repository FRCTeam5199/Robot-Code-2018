package tools;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import gfx.Display;
import maths.Vector2;
import path.Path;
import path.PathNode;

public class SelectBox implements Tool {

	private final int[] keyboardIn = { 'B' };
	private final int[] mouseIn = {};
	private final InputCombo shortcut = new InputCombo(keyboardIn, mouseIn);

	private Vector2 v1;
	private Vector2 v2;

	private boolean isDone;
	private boolean clear;

	@Override
	public InputCombo getShortcut() {
		return shortcut;
	}

	@Override
	public void init(Vector2 center, Path path, ArrayList<PathNode> selected, Display display) {
		v1 = null;
		isDone = false;
	}

	@Override
	public void update(Vector2 center, double typedNumber, double smoothRadius, Path path, ArrayList<PathNode> selected,
			Display display, ArrayList<Integer> keys, ArrayList<Integer> mouse) {
		v2 = display.toField(new Vector2(display.getMousePosition()));
		if (keys.contains(27)) {
			isDone = true;
			v1 = null;
		}
		if (mouse.contains(1) || mouse.contains(3)) {
			if (v1 == null) {
				v1 = display.toField(new Vector2(display.getMousePosition()));
			}
		} else {
			if (v1 != null) {
				if (keys.contains(16)) {
					clear = false;
				} else {
					clear = true;
				}
				isDone = true;

			}
		}

	}

	@Override
	public void cleanup(Path path, ArrayList<PathNode> selected, Display display) {
		if (v1 == null) {
			return;
		}

		if (clear) {
			selected.clear();
		}

		double xMax;
		double yMax;
		double xMin;
		double yMin;

		if (v1.getX() > v2.getX()) {
			xMax = v1.getX();
			xMin = v2.getX();
		} else {
			xMax = v2.getX();
			xMin = v1.getX();
		}

		if (v1.getY() > v2.getY()) {
			yMax = v1.getY();
			yMin = v2.getY();
		} else {
			yMax = v2.getY();
			yMin = v1.getY();
		}

		for (int i = 0; i < path.getLength(); i++) {
			Vector2 pos = path.getCheckpoint(i).getPos();

			if (pos.getX() < xMax && pos.getX() > xMin && pos.getY() < yMax && pos.getY() > yMin
					&& !selected.contains(path.getCheckpoint(i))) {
				selected.add(path.getCheckpoint(i));
			}
		}
	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return isDone;
	}

	@Override
	public void draw(Graphics g, Display display) {
		g.setColor(Color.LIGHT_GRAY);
		if (v1 == null) {
			Vector2 mouse = new Vector2(display.getMousePosition());

			g.drawLine((int) mouse.getX(), 0, (int) mouse.getX(), display.getHeight());
			g.drawLine(0, (int) mouse.getY(), display.getWidth(), (int) mouse.getY());

		} else {
			int x1 = display.toScreenX(v1.getX());
			int y1 = display.toScreenY(v1.getY());
			int x2 = display.toScreenX(v2.getX());
			int y2 = display.toScreenY(v2.getY());

			// g.drawRect(display.toScreenX(p1.getX()),
			// display.toScreenY(p1.getY()),
			// display.toScreenX(p2.getX()) - display.toScreenX(p1.getX()),
			// display.toScreenY(p2.getY()) - display.toScreenY(p1.getY()));

			int[] xCoords = new int[5];
			int[] yCoords = new int[5];

			xCoords[0] = x1;
			xCoords[1] = x2;
			xCoords[2] = x2;
			xCoords[3] = x1;
			xCoords[4] = x1;

			yCoords[0] = y2;
			yCoords[1] = y2;
			yCoords[2] = y1;
			yCoords[3] = y1;
			yCoords[4] = y2;

			g.drawPolyline(xCoords, yCoords, 5);
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
