package collinear;

import java.util.Comparator;

import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {

	private final int x;
	private final int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void draw() {
		StdDraw.point(x, y);
	}

	public void drawTo(Point that) {
		StdDraw.line(this.x, this.y, that.x, that.y);
	}

	public double slopeTo(Point that) {
		if (this.x == that.x && this.y == that.y)
			return Double.NEGATIVE_INFINITY;
		if (this.x == that.x)
			return Double.POSITIVE_INFINITY;
		if (this.y == that.y)
			return 0;
		return ((double) that.y - this.y) / ((double) that.x - this.x);
	}

	@Override
	public int compareTo(Point that) {
		if (this.x == that.x && this.y == that.y)
			return 0;
		return (this.y < that.y || (this.y == that.y && this.x < that.x)) ? -1 : 1;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public Comparator<Point> slopeOrder() {
		return new Comparator<Point>() {
			@Override
			public int compare(Point o1, Point o2) {
				double res = slopeTo(o1) - slopeTo(o2);
				if (res == 0)
					return 0;
				return (res > 0) ? 1 : -1;
			}
		};
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

}