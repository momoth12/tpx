import java.awt.Color;

public class Ball {

	double x, y, vx, vy, radius, mass;
	double trailx, traily;
	private int nbCollisions;
	private Color color;
	private Color trailColor;
	
	public Ball(double x, double y, double vx, double vy, double radius, double mass, Color color) {
		this.vx = vx;
		this.vy = vy;
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.mass = mass;

		trailx = x;
		traily = y;

		setColor(color);
	}
	
	void setColor(Color c) {
		color = c;
		this.trailColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()/3);
	}
	
	Color getColor() {
		return color;
	}

	int nbCollisions() {
		return nbCollisions;
	}

	void forward(double t) {
		x += vx * t;
		y += vy * t;
	}

	void collideWall(Direction dir) {
		if (dir == Direction.Horizontal)
			vy = -vy;
		else
			vx = -vx;
		nbCollisions++;
	}

	void collideBall(Ball other) {
		// Les deux boules subissent un choc dont la force est parallèle à la droite
		// joignant les deux centres.
		// La conservation de l'énergie cinétique permet de calculer la valeur de la
		// force.

		double dist = other.radius + radius; // L
		double dx = other.x - x, dy = other.y - y; // L
		double dvx = other.vx - vx, dvy = other.vy - vy; // L/T

		double a = 2 * (dx * dvx + dy * dvy) / (dist * dist * (other.mass + mass)); // 1/M/T

		// assert a < 0;
		double ax = a * dx, ay = a * dy; // L/M/T

		// La force de choc est donnée par le vector mass*other.mass*(ax, ay)

		vx += ax * other.mass;
		vy += ay * other.mass;
		other.vx -= ax * mass;
		other.vy -= ay * mass;

		nbCollisions++;
		other.nbCollisions++;
	}

	void draw() {
		StdDraw.setPenColor(trailColor);
		// 600 est la taille du canvas, et 512 la taille par défaut
		StdDraw.setPenRadius(2 * radius * 600 / 512);
		StdDraw.line(trailx, traily, x, y);

		StdDraw.setPenColor(color);
		StdDraw.filledCircle(x, y, radius);

		trailx = x;
		traily = y;
	}

	double nextBallCollision(Ball other) {
		if (this == other)
			return Double.NEGATIVE_INFINITY;

		double dmin = other.radius + radius;
		double dx = other.x - x, dy = other.y - y;
		double dvx = other.vx - vx, dvy = other.vy - vy;

		double b = dx * dvx + dy * dvy;
		if (b > 0.0)
			return Double.NEGATIVE_INFINITY;

		double a = dvx * dvx + dvy * dvy;
		if (a == 0.0)
			return Double.NEGATIVE_INFINITY;

		double c = dx * dx + dy * dy - dmin * dmin;
		if (c < 0) {
			System.out.println("Overlapping balls");
			return Double.NEGATIVE_INFINITY;
		}

		// On résout a*X^2 + 2*b*X + c = 0
		double discrim = b * b - a * c;
		if (discrim < 0.0)
			return Double.NEGATIVE_INFINITY;

		double res = -(b + Math.sqrt(discrim)) / a;
		return res;
	}

	double nextWallCollision(Direction dir) {
		if (dir == Direction.Horizontal) {
			if (vy > 0.0)
				return (1 - y - radius) / vy;
			if (vy < 0.0)
				return (radius - y) / vy;
		} else {
			if (vx > 0.0)
				return (1 - x - radius) / vx;
			if (vx < 0.0)
				return (radius - x) / vx;
		}
		return Double.NEGATIVE_INFINITY;
	}

	// @OFF
	void reverseVelocity() {
		vx = -vx;
		vy = -vy;
		nbCollisions++; // invalide les collisions prévues
	}
	// @ON
}
