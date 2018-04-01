package tools;

import java.util.ArrayList;

public class InputCombo {
	private final int[] keys;
	private final int[] mouse;

	public InputCombo(int[] keys, int[] mouse) {
		this.keys = keys;
		this.mouse = mouse;
	}

	public boolean isPressed(ArrayList<Integer> keyboardIn, ArrayList<Integer> mouseIn) {
		for (int i : keys) {
			if (!keyboardIn.contains(i)) {
				return false;
			}
		}

		for (int i : mouse) {
			if (!mouseIn.contains(i)) {
				return false;
			}
		}

		return true;
	}

	public int size() {
		return keys.length + mouse.length;
	}
}
