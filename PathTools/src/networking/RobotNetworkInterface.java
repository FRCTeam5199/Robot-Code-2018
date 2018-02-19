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
	private boolean isAlive;

	private boolean newPath;
	private Path path;
	private Vector2 position;
	private int checkpointIndex;

	public RobotNetworkInterface(int port) {
		t = new Thread(this, "Robot network interface");
		socket = null;
		position = Vector2.ZERO.clone();
		newPath = false;
		path = RecordedPaths.main2();
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
			case 1:
				position.setX(ByteUtils.toDouble(ByteUtils.portionOf(data, 4, 12)));
				position.setY(ByteUtils.toDouble(ByteUtils.portionOf(data, 12, 20)));
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

	public int getCheckpointIndex() {
		return checkpointIndex;
	}
}
