package kdtree;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.TreeSet;

public class PointSET {

	private final TreeSet<Point2D> set;

	// construct an empty set of points
	public PointSET() {
		set = new TreeSet<>();
	}

	// is the set empty?
	public boolean isEmpty() {
		return set.isEmpty();
	}

	// number of points in the set
	public int size() {
		return set.size();
	}

	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		if (p == null) {
			throw new IllegalArgumentException();
		}
		if (!set.contains(p)) {
			set.add(p);
		}
	}

	// does the set contain point p?
	public boolean contains(Point2D p) {
		if (p == null) {
			throw new IllegalArgumentException();
		}
		return set.contains(p);
	}

	// draw all points to standard draw
	public void draw() {
		for (Point2D point2D : set) {
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.setPenRadius(.01);
			StdDraw.point(point2D.x(), point2D.y());
		}
	}

	// all points that are inside the rectangle (or on the boundary)
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null) {
			throw new IllegalArgumentException();
		}
		Queue<Point2D> que = new Queue<>();
		for (Point2D point2D : set) {
			if (rect.contains(point2D)) {
				que.enqueue(point2D);
			}
		}
		return que;
	}

	// a nearest neighbor in the set to point p; null if the set is empty
	public Point2D nearest(Point2D p) {
		if (p == null) {
			throw new IllegalArgumentException();
		}
		Point2D ceil = set.ceiling(p);
		Point2D floor = set.floor(p);
		if (ceil == null) {
			return floor;
		}
		if (floor == null) {
			return ceil;
		}
		return (p.distanceSquaredTo(ceil) < p.distanceSquaredTo(floor)) ? ceil : floor;
	}

	// unit testing of the methods
	public static void main(String[] args) {
		// need implementation
	}
}
