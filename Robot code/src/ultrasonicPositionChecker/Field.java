package ultrasonicPositionChecker;

public class Field {
	private BoxCollider[] field;

	public Field() {
		field = new BoxCollider[18];
		field[0] = new BoxCollider(84, 140, 156, 56);
		field[1] = new BoxCollider(84, 196, 13, 13);
		field[2] = new BoxCollider(112.6, 196, 13, 13);
		field[3] = new BoxCollider(141.2, 196, 13, 13);
		field[4] = new BoxCollider(169.8, 196, 13, 13);
		field[5] = new BoxCollider(198.4, 196, 13, 13);
		field[6] = new BoxCollider(227, 196, 13, 13);
	}
}
