package gfx;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import maths.Vector2;
import networking.RobotNetworkInterface;
import path.Path;

public class Display extends JFrame {

	private final int borderSize = 50;

	private final RobotNetworkInterface robotInterface;
	private Path path;

	public Display(RobotNetworkInterface robotInterface, Path path) {
		super("Path Tools");
		this.robotInterface = robotInterface;
		this.path = path;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1280, 720);
		getContentPane().setBackground(Color.DARK_GRAY);
		setVisible(true);
	}

	public void paint(Graphics g) {
		g.drawImage(draw(), 0, 0, null);

	}

	private BufferedImage draw() {
		BufferedImage output = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

		Graphics g = output.getGraphics();

		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());

		int xMin = Integer.MAX_VALUE;
		int xMax = Integer.MIN_VALUE;
		int yMin = Integer.MAX_VALUE;
		int yMax = Integer.MIN_VALUE;
		double maxSpeed = 0;

		for (int i = 0; i < path.getLength(); i++) {
			Vector2 pos = path.getCheckpoint(i).getPos();
			if (pos.getX() < xMin) {
				xMin = (int) pos.getX();
			}
			if (pos.getX() > xMax) {
				xMax = (int) pos.getX();
			}
			if (pos.getY() < yMin) {
				yMin = (int) pos.getY();
			}
			if (pos.getY() > yMax) {
				yMax = (int) pos.getY();
			}

			if (path.getCheckpoint(i).getSpeed() > maxSpeed) {
				maxSpeed = path.getCheckpoint(i).getSpeed();
			}
		}

		int width = xMax - xMin;
		int height = yMax - yMin;

		double scale = Math.min((getWidth() - 2d * borderSize) / width, (getHeight() - 2d * borderSize) / height);
		double colorScale = 700d / maxSpeed;

		int[] xPoints = new int[path.getLength()];
		int[] yPoints = new int[path.getLength()];
		int[] color = new int[path.getLength()];

		for (int i = 0; i < path.getLength(); i++) {
			Vector2 pos = path.getCheckpoint(i).getPos();
			xPoints[i] = (int) (scale * (pos.getX() - xMin)) + borderSize;
			yPoints[i] = (int) (scale * (height - pos.getY() + yMin)) + borderSize;

			color[i] = (int) (path.getCheckpoint(i).getSpeed() * colorScale);
		}

		g.setColor(Color.LIGHT_GRAY);

		g.drawPolyline(xPoints, yPoints, path.getLength());

		for (int i = 0; i < path.getLength(); i++) {
			if (robotInterface.getCheckpointIndex() < i + 1) {
				g.setColor(colorCycle(color[i]));
			} else {
				g.setColor(Color.GRAY);
			}
			g.fillOval(xPoints[i] - 5, yPoints[i] - 5, 10, 10);
		}

		for (int i = 0; i < 350; i++) {
			g.setColor(colorCycle(2 * i));
			g.drawLine(10, i + 40, 30, i + 40);
		}

		g.setColor(Color.white);
		g.drawString("0 in/s", 40, 45);
		g.drawString(maxSpeed + " in/s", 40, 395);
		g.drawString("Start", xPoints[0], yPoints[0]);

		Vector2 robotPos = robotInterface.getPosition();

		g.setColor(Color.RED);
		g.fillOval((int) ((robotPos.getX() - xMin) * scale) + borderSize - 5,
				(int) ((height - robotPos.getY() + yMin) * scale) + borderSize - 5, 10, 10);

		return output;
	}

	private Color colorCycle(double n) {
		n += 250;
		n *= 2 * Math.PI / 1000;
		int r = (int) (Math.sin(n) * 127.5 + 127.5);
		int g = (int) (Math.sin(n + 2 / 3d * Math.PI) * 127.5 + 127.5);
		int b = (int) (Math.sin(n + 4 / 3d * Math.PI) * 127.5 + 127.5);

		return new Color(r, g, b);
	}

	public void update() {
		repaint();
	}

	public void setPath(Path path) {
		this.path = path;
	}

}
