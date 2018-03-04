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

	private double scale;
	private double maxSpeed;
	private double colorScale;
	private double dWidth;
	private double dHeight;
	private Vector2 origin;

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

		configGraph();
		drawGrid(g);
		drawPath(g);
		drawScale(g);
		drawRobot(g);
		return output;
	}

	private void configGraph() {
		int xMin = Integer.MAX_VALUE;
		int xMax = Integer.MIN_VALUE;
		int yMin = Integer.MAX_VALUE;
		int yMax = Integer.MIN_VALUE;

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

		dWidth = xMax - xMin;
		dHeight = yMax - yMin;

		scale = Math.min((getWidth() - 2d * borderSize) / dWidth, (getHeight() - 2d * borderSize) / dHeight);
		colorScale = 700d / maxSpeed;

		origin = new Vector2(-xMin * scale, -yMin * scale);

	}

	private void drawGrid(Graphics g) {
		double height = getHeight() / scale;
		double width = getWidth() / scale;

		g.setColor(Color.GRAY);

		for (int y = toScreenY(0); y < getHeight(); y += (toScreenY(0) - toScreenY(12))) {
			g.drawLine(0, y, getWidth(), y);
		}

		for (int y = toScreenY(0); y > 0; y -= (toScreenY(0) - toScreenY(12))) {
			g.drawLine(0, y, getWidth(), y);
		}

		for (int x = toScreenX(0); x < getWidth(); x += (toScreenX(12) - toScreenX(0))) {
			g.drawLine(x, 0, x, getHeight());
		}

		for (int x = toScreenX(0); x > 0; x -= (toScreenX(12) - toScreenX(0))) {
			g.drawLine(x, 0, x, getHeight());
		}

	}

	private void drawPath(Graphics g) {
		int[] xPoints = new int[path.getLength()];
		int[] yPoints = new int[path.getLength()];
		int[] color = new int[path.getLength()];

		for (int i = 0; i < path.getLength(); i++) {
			Vector2 pos = path.getCheckpoint(i).getPos();
			xPoints[i] = (int) ((scale * pos.getX()) + origin.getX()) + borderSize;
			yPoints[i] = (int) ((scale * (dHeight - pos.getY()) - origin.getY())) + borderSize;

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
			Vector2 pos = path.getCheckpoint(i).getPos();
			plot(pos.getX(), pos.getY(), g);
		}

		g.setColor(Color.WHITE);
		g.drawString("Start", xPoints[0], yPoints[0]);

	}

	private void drawScale(Graphics g) {
		for (int i = 0; i < 350; i++) {
			g.setColor(colorCycle(2 * i));
			g.drawLine(10, i + 40, 30, i + 40);
		}

		g.setColor(Color.WHITE);
		g.drawString("0 in/s", 40, 45);
		g.drawString(maxSpeed + " in/s", 40, 395);
	}

	private void drawRobot(Graphics g) {
		Vector2 robotPos = robotInterface.getPosition();

		g.setColor(Color.RED);
		plot(robotPos.getX(), robotPos.getY(), g);
		g.drawLine((int) robotPos.getX(), (int) robotPos.getY(),
				(int) (robotPos.getX() + 20 * Math.sin(robotInterface.getRot())),
				(int) (robotPos.getY() + 20 * Math.cos(robotInterface.getRot())));
	}

	private void plot(double x, double y, Graphics g) {
		g.fillOval(toScreenX(x) - 5, toScreenY(y) - 5, 10, 10);
	}

	private int toScreenX(double x) {
		return (int) (x * scale + origin.getX()) + borderSize;
	}

	private int toScreenY(double y) {
		return (int) ((dHeight - y) * scale - origin.getY()) + borderSize;
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
		maxSpeed = 0;
		this.path = path;
	}

}
