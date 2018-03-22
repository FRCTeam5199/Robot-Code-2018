package networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import maths.Vector2;
import path.Path;
import path.RecordedPaths;

public class RobotNetworkInterface implements Runnable {
	private final Thread t;
	private final int port;
	private DatagramSocket socket;

	private final Vector2 robotDim = new Vector2(34, 39);
	private final Vector2 pivotPos = new Vector2(robotDim.getX() / 2, 4.625);

	private boolean isAlive;

	private Path path;
	private Vector2 position;
	private double rotation;
	private double velocity;
	private int checkpointIndex;
	private boolean newPath;

	public RobotNetworkInterface(int port) {
		t = new Thread(this, "Robot network interface");
		socket = null;
		position = Vector2.ZERO.clone();
		newPath = false;
		path = RecordedPaths.scaleLtoR();
		rotation = 0;
		velocity = 0;
		checkpointIndex = 0;
		this.port = port;
	}

	public void start() {
		isAlive = true;
		t.start();
	}

	public void stop() {
		isAlive = false;
	}

	@Override
	public void run() {

		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			System.out.println("Failed to initialize socket");
			e.printStackTrace();
			System.exit(69);
		}

		byte[] data;
		DatagramPacket packet;

		while (isAlive) {

			data = new byte[20000];
			packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				System.out.println("Failed to receive packet");
				e.printStackTrace();
			}

			int dataID = ByteUtils.toInt(ByteUtils.portionOf(data, 0, 4));

			switch (dataID) {
			case 0:
				newPath = true;
				path = new Path(ByteUtils.portionOf(data, 4, packet.getLength()));
				checkpointIndex = 0;
			case 1:
				position.setX(ByteUtils.toDouble(ByteUtils.portionOf(data, 4, 12)));
				position.setY(ByteUtils.toDouble(ByteUtils.portionOf(data, 12, 20)));
				rotation = ByteUtils.toDouble(ByteUtils.portionOf(data, 20, 28));
				velocity = ByteUtils.toDouble(ByteUtils.portionOf(data, 28, 36));
				break;
			case 2:
				checkpointIndex = ByteUtils.toInt(ByteUtils.portionOf(data, 4, 8));
				break;
			}

		}

	}

	public boolean isNewPath() {
		if (newPath) {
			newPath = false;
			return true;
		} else {
			return false;
		}
	}

	public Path getPath() {
		return path;
	}

	public Vector2 getPosition() {
		return position;
	}

	public double getRot() {
		return rotation;
	}

	public double getVelocity() {
		return velocity;
	}

	public int getCheckpointIndex() {
		return checkpointIndex;
	}

	public Vector2 getDim() {
		return robotDim;
	}

	public Vector2 getPivotPos() {
		return pivotPos;
	}
}
