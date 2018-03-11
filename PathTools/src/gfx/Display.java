package gfx;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import maths.Vector2;
import maths.Vector2I;
import networking.RobotNetworkInterface;
import path.Path;
import path.PathNode;

public class Display extends JFrame {

	private final double inPerMeter = 39.3701;

	private BufferedImage fieldImage;
	private final double fieldImageScale = 299.65 / 568;
	private final Vector2I fieldImageCenter = new Vector2I(240, 1477);
	private final Vector2 startingPosition = new Vector2(29.67, 4);
	// private final Vector2I fieldImageCenter = new Vector2I(35, 35);

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

		try {
			fieldImage = ImageIO.read(getClass().getClassLoader().getResource("Field.png"));
		} catch (IOException e) {
			fieldImage = null;
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1280, 720);
		setMinimumSize(new Dimension(320, 240));
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
		drawField(g);
		drawGrid(g);
		drawPath(g);
		drawScale(g);
		drawRobot(g);
		mouseOverInfo(g);

		return output;
	}

	private void configGraph() {

		Vector2 robotPos = robotInterface.getPosition();

		int xMin = (int) robotPos.getX();
		int xMax = xMin;
		int yMin = (int) robotPos.getY();
		int yMax = yMin;

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

	private void drawField(Graphics g) {
		// g.drawImage(fieldImage, toScreenX(fie) * (1 - fieldImageScale)),
		// toScreenY(fieldImage.getHeight() * (1 - fieldImageScale)),
		// toScreenX(fieldImage.getWidth() * fieldImageScale) - toScreenX(0),
		// toScreenY(0) - toScreenY(fieldImage.getHeight() * fieldImageScale),
		// this);

		// g.drawImage(fieldImage, toScreenX(-fieldImageCenter.getX() *
		// fieldImageScale),
		// toScreenY((fieldImage.getHeight() - fieldImageCenter.getY()) *
		// fieldImageScale),
		// toScreenX(fieldImage.getWidth()) - toScreenX(0),
		// toScreenY(fieldImage.getHeight()) - toScreenY(0),
		// this);

		g.drawImage(fieldImage, toScreenX(-fieldImageCenter.getX() * fieldImageScale),
				toScreenY(fieldImageCenter.getY() * fieldImageScale),
				toScreenX(fieldImage.getWidth() * fieldImageScale) - toScreenX(0),
				toScreenY(0) - toScreenY(fieldImage.getHeight() * fieldImageScale), this);
	}

	private void drawGrid(Graphics g) {

		g.setColor(Color.GRAY);

		for (double y = toScreenY(0); y < getHeight(); y += (toScreenYD(0) - toScreenYD(12))) {
			g.drawLine(0, (int) y, getWidth(), (int) y);
		}

		for (double y = toScreenY(0); y > 0; y -= (toScreenYD(0) - toScreenYD(12))) {
			g.drawLine(0, (int) y, getWidth(), (int) y);
		}

		for (double x = toScreenX(0); x < getWidth(); x += (toScreenXD(12) - toScreenXD(0))) {
			g.drawLine((int) x, 0, (int) x, getHeight());
		}

		for (double x = toScreenX(0); x > 0; x -= (toScreenXD(12) - toScreenXD(0))) {
			g.drawLine((int) x, 0, (int) x, getHeight());
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
			g.drawLine(10, 350 - i + 40, 30, 350 - i + 40);
		}

		g.setColor(Color.WHITE);
		g.drawString("0 in/s", 40, 395);
		g.drawString(maxSpeed + " in/s", 40, 45);
	}

	private void drawRobot(Graphics g) {
		Vector2 robotPos = robotInterface.getPosition();
		Vector2 dim = robotInterface.getDim();
		Vector2 pivot = robotInterface.getPivotPos();
		double rot = robotInterface.getRot();

		g.setColor(Color.WHITE);
		plot(robotPos.getX(), robotPos.getY(), g);

		double front = dim.getY() - pivot.getY();
		double back = -pivot.getY();
		double left = -pivot.getX();
		double right = dim.getX() - pivot.getX();

		Vector2 fl = Vector2.rotateCW(new Vector2(left, front), rot);
		Vector2 bl = Vector2.rotateCW(new Vector2(left, back), rot);
		Vector2 fr = Vector2.rotateCW(new Vector2(right, front), rot);
		Vector2 br = Vector2.rotateCW(new Vector2(right, back), rot);

		fl = Vector2.add(fl, robotPos);
		bl = Vector2.add(bl, robotPos);
		fr = Vector2.add(fr, robotPos);
		br = Vector2.add(br, robotPos);

		int[] baseX = new int[5];
		int[] baseY = new int[5];

		baseX[0] = toScreenX(bl.getX());
		baseX[1] = toScreenX(fl.getX());
		baseX[2] = toScreenX(fr.getX());
		baseX[3] = toScreenX(br.getX());
		baseX[4] = toScreenX(bl.getX());

		baseY[0] = toScreenY(bl.getY());
		baseY[1] = toScreenY(fl.getY());
		baseY[2] = toScreenY(fr.getY());
		baseY[3] = toScreenY(br.getY());
		baseY[4] = toScreenY(bl.getY());

		robotPos = toScreen(robotPos);

		g.setColor(Color.WHITE);
		g.drawPolyline(baseX, baseY, 5);

		// g.drawLine((int) robotPos.getX(), (int) robotPos.getY(),
		// (int) (robotPos.getX() + 20 * Math.sin(robotInterface.getRot())),
		// (int) (robotPos.getY() - 20 * Math.cos(robotInterface.getRot())));

		g.drawString("Speed: " + robotInterface.getVelocity(), 30, getHeight() - 30);
	}

	private void mouseOverInfo(Graphics g) {
		if (getMousePosition() == null) {
			return;
		}
		Vector2 mousePos = new Vector2(getMousePosition());

		for (int i = 0; i < path.getLength(); i++) {
			Vector2 disPos = toScreen(path.getCheckpoint(i).getPos());
			int x = (int) disPos.getX();
			int y = (int) disPos.getY();

			if (Vector2.distance(mousePos, disPos) < 10) {
				PathNode node = path.getCheckpoint(i);

				g.setColor(flashCycle(colorCycle(node.getSpeed() * colorScale), 1000));
				g.fillOval(x - 10, y - 10, 20, 20);
				g.setColor(Color.WHITE);
				g.drawOval(x - 10, y - 10, 20, 20);
				g.drawString("Node " + i + " : " + node.getPos(), x + 20, y + 20);
				g.drawString("Speed: " + node.getSpeed() + " in/s", x + 20, y + 35);
				g.drawString("              " + node.getSpeed() / inPerMeter + " m/s", x + 20, y + 50);
				return;
			}
		}
	}

	private void plot(Vector2 pos, Graphics g) {
		plot(pos.getX(), pos.getY(), g);
	}

	private void plot(double x, double y, Graphics g) {
		g.fillOval(toScreenX(x) - 5, toScreenY(y) - 5, 10, 10);
	}

	private double toScreenXD(double x) {
		return (x * scale + origin.getX()) + borderSize;
	}

	private double toScreenYD(double y) {
		return ((dHeight - y) * scale - origin.getY()) + borderSize;
	}

	private int toScreenX(double x) {
		return (int) toScreenXD(x);
	}

	private int toScreenY(double y) {
		return (int) toScreenYD(y);
	}

	private Vector2 toScreen(Vector2 p) {
		return new Vector2(toScreenX(p.getX()), toScreenY(p.getY()));
	}

	private Color colorCycle(double n) {
		n += 250;
		n *= 2 * Math.PI / 1000;
		int r = (int) (Math.sin(n) * 127.5 + 127.5);
		int g = (int) (Math.sin(n + 2 / 3d * Math.PI) * 127.5 + 127.5);
		int b = (int) (Math.sin(n + 4 / 3d * Math.PI) * 127.5 + 127.5);

		return new Color(r, g, b);
	}

	private Color flashCycle(Color input, double period) {
		int brightness = (int) (40 + 40 * Math.sin((2 * Math.PI * System.currentTimeMillis()) / period));
		return new Color(clampInt(brightness + input.getRed(), 255), clampInt(brightness + input.getGreen(), 255),
				clampInt(brightness + input.getBlue(), 255));
	}

	private int clampInt(int input, int max) {
		if (input > max) {
			return max;
		} else {
			return input;
		}
	}

	public void update() {
		repaint();
	}

	public void setPath(Path path) {
		maxSpeed = 0;
		this.path = path;
	}

}
