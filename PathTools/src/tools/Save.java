package tools;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTextField;

import gfx.Display;
import maths.Vector2;
import path.Path;
import path.PathNode;

public class Save extends JFrame implements Tool {

	private final int[] keyboardIn = { 17, 'S' };
	private final int[] mouseIn = {};
	private final InputCombo shortcut = new InputCombo(keyboardIn, mouseIn);

	JTextField text;

	private boolean isDone;

	public Save() {
		text = new JTextField();
		text.setEditable(false);
		add(text);
		setSize(320, 240);
	}

	@Override
	public InputCombo getShortcut() {
		return shortcut;
	}

	@Override
	public void init(Vector2 center, Path path, ArrayList<PathNode> selected, Display display) {
		setLocation(display.getLocation().x + display.getWidth() / 2 - getWidth() / 2,
				display.getLocation().y + display.getHeight() / 2 - getHeight() / 2);
		setVisible(true);

		String output = "";

		for (int i = 0; i < path.getLength(); i++) {
			output += "|" + path.getCheckpoint(i).getPos();
		}

		output += "|";

		text.setText(output);

		isDone = false;
	}

	@Override
	public void update(Vector2 center, double typedNumber, double smoothRadius, Path path, ArrayList<PathNode> selected,
			Display display, ArrayList<Integer> keys, ArrayList<Integer> mouse) {
		if (!isVisible()) {
			isDone = true;
		}

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
		return true;
	}

	@Override
	public boolean clearMouse() {
		// TODO Auto-generated method stub
		return false;
	}

}
