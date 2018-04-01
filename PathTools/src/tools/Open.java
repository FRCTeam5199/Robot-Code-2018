package tools;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;

import gfx.Display;
import maths.Vector2;
import path.Path;
import path.PathNode;

public class Open extends JFrame implements Tool {

	private final int[] keyboardIn = { 17, 'O' };
	private final int[] mouseIn = {};
	private final InputCombo shortcut = new InputCombo(keyboardIn, mouseIn);

	public Open() {
		setSize(320, 240);
	}

	@Override
	public InputCombo getShortcut() {
		return shortcut;
	}

	@Override
	public void init(Vector2 center, Path path, ArrayList<PathNode> selected, Display display) {

	}

	@Override
	public void update(Vector2 center, double typedNumber, double smoothRadius, Path path, ArrayList<PathNode> selected,
			Display display, ArrayList<Integer> keys, ArrayList<Integer> mouse) {
	}

	@Override
	public void cleanup(Path path, ArrayList<PathNode> selected, Display display) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return false;
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
		return false;
	}

}
