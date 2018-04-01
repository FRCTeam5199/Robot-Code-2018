package tools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import gfx.Display;
import maths.Vector2;
import path.Path;
import path.PathNode;

public class ToolHandler implements KeyListener, MouseListener, MouseWheelListener {

	private final Display display;

	private final int kSmoothRadius = 5;

	private ArrayList<PathNode> selected;
	private ArrayList<PathNode> oldPath;
	private ArrayList<Tool> tools;
	private Path path;
	private Vector2 center;
	private int smoothRadius = 75;
	private int activeTool;
	private boolean smoothing;
	private String typeNumber;

	private ArrayList<Integer> pressedKeys;
	private ArrayList<Integer> pressedMouse;

	public ToolHandler(Path path, Display display) {
		this.display = display;
		selected = new ArrayList<PathNode>();
		tools = new ArrayList<Tool>();

		pressedKeys = new ArrayList<Integer>();
		pressedMouse = new ArrayList<Integer>();

		this.path = path;

		smoothing = false;
		typeNumber = "";

		center = Vector2.ZERO.clone();

		activeTool = -1;
	}

	public ArrayList<PathNode> getSelected() {
		return selected;
	}

	public void add(Tool tool) {
		tools.add(tool);
	}

	public void update(Graphics g) {
		if (!pressedKeys.isEmpty()) {
			System.out.println("\"" + (int) pressedKeys.get(0) + "\"");
		}

		if (pressedKeys.contains((int) 'O')) {
			smoothing = !smoothing;
			pressedKeys.remove(new Integer('O'));
		}

		if (pressedKeys.contains(17) && pressedKeys.contains((int) 'N')) {
			path = display.clearPath();
		}

		if (activeTool == -1) {
			for (int i = 0; i < tools.size(); i++) {
				if (tools.get(i).getShortcut().isPressed(pressedKeys, pressedMouse)) {
					activeTool = i;
					i = tools.size();

					double totalX = 0;
					double totalY = 0;
					for (PathNode n : selected) {
						totalX += n.getPos().getX();
						totalY += n.getPos().getY();
					}

					if (smoothing) {
						oldPath.clear();
						for (int ip = 0; ip < path.getLength(); ip++) {
							oldPath.add(path.getCheckpoint(ip).clone());
						}
					}

					center = new Vector2(totalX / selected.size(), totalY / selected.size());

					tools.get(activeTool).init(center, path, selected, display);
				}
			}
		} else {
			Tool actvTool = tools.get(activeTool);
			if (actvTool.isDone()) {
				actvTool.cleanup(path, selected, display);
				typeNumber = "";

				if (actvTool.clearKeys()) {
					pressedKeys.clear();
				}
				if (actvTool.clearMouse()) {
					pressedMouse.clear();
				}

				activeTool = -1;
			} else {
				double fieldRadius;

				if (smoothing) {
					fieldRadius = display.toFieldXD(smoothRadius) - display.toFieldXD(0);
					if (!selected.isEmpty()) {
						g.setColor(Color.LIGHT_GRAY);
						g.drawOval(display.toScreenX(center.getX()) - smoothRadius / 2,
								display.toScreenY(center.getY()) - smoothRadius / 2, smoothRadius, smoothRadius);
					}
				} else {
					fieldRadius = 0;
				}

				char typed = numberTyped();
				if (typed != (char) -1) {
					pressedKeys.remove(0);
					if (typed == '-') {
						if (!typeNumber.isEmpty()) {
							if (typeNumber.charAt(0) == '-') {
								typeNumber = typeNumber.substring(1, typeNumber.length());
							} else {
								typeNumber = '-' + typeNumber;
							}
						}
					} else if (typed == '.') {
						if (!typeNumber.contains(".")) {
							typeNumber += ".";
						}
					} else if (typed == 'b') {
						if (!typeNumber.isEmpty()) {
							typeNumber = typeNumber.substring(0, typeNumber.length() - 1);
						}
					} else {
						typeNumber += typed;
					}
				}

				Double parseTypeNumber;
				if (!typeNumber.isEmpty() && !typeNumber.equals(".") && !typeNumber.equals("-")
						&& !typeNumber.equals("-.")) {
					parseTypeNumber = Double.parseDouble(typeNumber);
				} else {
					parseTypeNumber = Double.NaN;
				}

				actvTool.update(center, parseTypeNumber, fieldRadius, path, selected, display, pressedKeys,
						pressedMouse);
				actvTool.draw(g, display);

				g.setColor(Color.WHITE);
				g.drawString(typeNumber, display.getWidth() - 50, display.getHeight() - 30);
				path.recalculate();
			}
		}
	}

	private char numberTyped() {
		if (pressedKeys.size() == 1) {
			int key = pressedKeys.get(0);

			if (96 <= key && key <= 105) {
				key -= 48;
			}

			if (48 <= key && key <= 57) {
				return (char) key;
			}

			switch (key) {
			case 8:
				return 'b';
			case 46:
			case 110:
				return '.';
			case 45:
			case 109:
				return '-';
			}
			return (char) -1;
		} else {
			return (char) -1;
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if (!pressedKeys.contains(arg0.getKeyCode())) {
			pressedKeys.add(arg0.getKeyCode());
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if (pressedKeys.contains(arg0.getKeyCode())) {
			pressedKeys.remove(new Integer(arg0.getKeyCode()));
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		if (!pressedMouse.contains(arg0.getButton())) {
			pressedMouse.add(arg0.getButton());
		}

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (pressedMouse.contains(arg0.getButton())) {
			pressedMouse.remove(new Integer(arg0.getButton()));
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		smoothRadius += arg0.getWheelRotation() * kSmoothRadius;

		if (smoothRadius < 0) {
			smoothRadius = 0;
		}

		System.out.println(smoothRadius);

	}

}
