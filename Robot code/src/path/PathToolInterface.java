package path;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.usfirst.frc.team5199.robot.Robot;

import maths.Vector2;
import networking.ByteUtils;
import sensors.Location;

import static networking.ByteUtils.addArray;

public class PathToolInterface {
	private final Location location;
	private InetAddress address;
	private int port;

	private DatagramSocket socket;

	public PathToolInterface(String address, int port, Location location) {
		this.location = location;

		try {
			this.address = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			Robot.nBroadcaster.println("Failed to initialize address");
			e.printStackTrace();
			System.exit(69);
		}
		this.port = port;

		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			Robot.nBroadcaster.println("Failed to initialize socket");
			e.printStackTrace();
			System.exit(420);
		}
	}

	public void sendPath(Path path) {
		Robot.nBroadcaster.println("Parsing  path " + path.getLength() + " nodes long");

		byte[] data = ByteUtils.toByteArray(0);

		for (int i = 0; i < path.getLength(); i++) {
			Vector2 checkpoint = path.getCheckpoint(i).getPos();
			double speed = path.getCheckpoint(i).getSpeed();
			data = ByteUtils.addArray(data, ByteUtils.toByteArray(checkpoint.getX()));
			data = ByteUtils.addArray(data, ByteUtils.toByteArray(checkpoint.getY()));
			data = ByteUtils.addArray(data, ByteUtils.toByteArray(speed));
		}

		Robot.nBroadcaster.println("sent " + data.length + " bytes");

		sendData(data);
	}

	public void sendPos() {
		Vector2 pos = location.getLocation();
		byte[] data = ByteUtils.toByteArray(1);
		data = addArray(data, ByteUtils.toByteArray(pos.getX()));
		data = addArray(data, ByteUtils.toByteArray(pos.getY()));
		data = addArray(data, ByteUtils.toByteArray(location.getRot()));
		data = addArray(data, ByteUtils.toByteArray(location.getBaseVelocity()));
		sendData(data);
	}

	public void setCheckpointIndex(int checkpointIndex) {
		byte[] data = ByteUtils.toByteArray(2);
		data = addArray(data, ByteUtils.toByteArray(checkpointIndex));
		sendData(data);
	}

	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, address, port);

		try {
			socket.send(packet);
		} catch (IOException e) {
			Robot.nBroadcaster.println("Failed to send packet");
			e.printStackTrace();
		}
	}

	public void cleanUp() {
		socket.close();
	}

}
