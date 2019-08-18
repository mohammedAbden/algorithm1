package kdtree;

import java.util.Comparator;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
	private Node root;
	private int size;
	private Point2D currentNearestPoint = null;

	// construct an empty set of points
	public KdTree() {
		root = null;
		size = 0;
	}

	// is the set empty?
	public boolean isEmpty() {
		return root == null;
	}

	// number of points in the set
	public int size() {
		return size;
	}

	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		if (p == null)
			throw new IllegalArgumentException();
		root = insert(root, p, 0, 0, 0, 1, 1);
	}

	private Node insert(Node x, Point2D p, int level, double xmin, double ymin, double xmax, double ymax) {// level

		if (x == null) {
			size++;
			return new Node(p, new RectHV(xmin, ymin, xmax, ymax));
		}

		if (x.point.equals(p)) {
			return x;
		}
		
		// according x
		if ((level & 1) == 0) {
			if (Point2D.X_ORDER.compare(p, x.point) < 0)
				x.left = insert(x.left, p, ++level, xmin, ymin, x.point.x(), ymax);
			else
				x.right = insert(x.right, p, ++level, x.point.x(), ymin, xmax, ymax);
		}
		// according to y
		else { 
			if (Point2D.Y_ORDER.compare(p, x.point) < 0)
				x.left = insert(x.left, p, ++level, xmin, ymin, xmax, x.point.y());
			else
				x.right = insert(x.right, p, ++level, xmin, x.point.y(), xmax, ymax);
		}
		return x;
	}

	// does the set contain point p?
	public boolean contains(Point2D p) {
		if (p == null)
			throw new IllegalArgumentException();
		return contains(root, p, 0);
	}

	private boolean contains(Node x, Point2D p, int level) {
		if (x == null)
			return false;
		if (x.point.equals(p))
			return true;
		// according x
		if ((level & 1) == 0) {
			return handleContains(Point2D.X_ORDER, x, p, level);
		} else {
			 // according to y
			return handleContains(Point2D.Y_ORDER, x, p, level);
		}

	}

	private boolean handleContains(Comparator<Point2D> comparator, Node x, Point2D p, int level) {
		if (comparator.compare(p, x.point) < 0)
			return contains(x.left, p, level + 1);
		else
			return contains(x.right, p, level + 1);
	}

	// draw all points to standard draw
	public void draw() {
		if (isEmpty())
			return;
		draw(root, true, 0, 0f, 1f, 1f);
	}

	private void draw(Node x, boolean vert, double xmin, double ymin, double xmax, double ymax) {

		// draw the point first
		if (x == null)
			return;
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(.01);
		StdDraw.point(x.point.x(), x.point.y());

		if (vert) {
			StdDraw.setPenRadius();
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.line(x.point.x(), ymin, x.point.x(), ymax);
			draw(x.left, !vert, xmin, ymin, x.point.x(), ymax);
			draw(x.right, !vert, x.point.x(), ymin, xmax, ymax);

		} else {
			StdDraw.setPenRadius();
			StdDraw.setPenColor(StdDraw.BLUE);
			StdDraw.line(xmin, x.point.y(), xmax, x.point.y());
			draw(x.left, !vert, xmin, ymin, xmax, x.point.y());
			draw(x.right, !vert, xmin, x.point.y(), xmax, ymax);
		}

	}

	// all points that are inside the rectangle (or on the boundary)
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null)
			throw new IllegalArgumentException();

		Queue<Point2D> set = new Queue<>();
		double xmin = Math.min(rect.xmax(), rect.xmin());
		double ymin = Math.min(rect.ymax(), rect.ymin());
		double xmax = Math.max(rect.xmax(), rect.xmin());
		double ymax = Math.max(rect.ymax(), rect.ymin());
		getRange(root, set, new RectHV(xmin, ymin, xmax, ymax), 0);
		return set;
	}

	private void getRange(Node x, Queue<Point2D> set, RectHV rect, int level) {

		if (x == null)
			return;

		if (rect.contains(x.point)) {
			set.enqueue(x.point);
			getRange(x.left, set, rect, level + 1);
			getRange(x.right, set, rect, level + 1);
		} else if ((level & 1) == 0) {
			// even level
			handleRangeByX(x, set, rect, level);
		} else {
			handleRangeByY(x, set, rect, level);
		}
	}

	private void handleRangeByX(Node x, Queue<Point2D> set, RectHV rect, int level) {

		if (x.point.x() > rect.xmin())
			getRange(x.left, set, rect, level + 1);
		if (x.point.x() <= rect.xmax())
			getRange(x.right, set, rect, level + 1);
	}

	private void handleRangeByY(Node x, Queue<Point2D> set, RectHV rect, int level) {

		if (x.point.y() > rect.ymin())
			getRange(x.left, set, rect, level + 1);
		if (x.point.y() <= rect.ymax())
			getRange(x.right, set, rect, level + 1);
	}

	public Point2D nearest(Point2D p) {
		if (p == null)
			throw new IllegalArgumentException();

		if (isEmpty())
			return null;
		currentNearestPoint = root.point;
		return nearest(p, root, 0);
	}

	private Point2D nearest(Point2D p, Node currentNode, int level) {
		if (currentNode == null)
			return currentNearestPoint;

		double distance = p.distanceSquaredTo(currentNode.point);
		double minDistance = p.distanceSquaredTo(currentNearestPoint);

		if (distance < minDistance)
			currentNearestPoint = currentNode.point;

		if ((level & 1) == 0) {
			handleNearest(Point2D.X_ORDER, p, currentNode, level);
		} else {
			handleNearest(Point2D.Y_ORDER, p, currentNode, level);
		}

		return currentNearestPoint;
	}

	private void handleNearest(Comparator<Point2D> comparator, Point2D p, Node currentNode, int level) {
		if (comparator.compare(p, currentNode.point) < 0) {
			nearest(p, currentNode.left, level + 1);
			if (currentNode.right != null
					&& p.distanceSquaredTo(currentNearestPoint) > currentNode.right.rect.distanceSquaredTo(p))
				nearest(p, currentNode.right, level + 1);
		} else {
			nearest(p, currentNode.right, level + 1);
			if (currentNode.left != null
					&& p.distanceSquaredTo(currentNearestPoint) > currentNode.left.rect.distanceSquaredTo(p))
				nearest(p, currentNode.left, level + 1);
		}
	}

	private class Node {

		private final Point2D point;
		private final RectHV rect;
		private Node left = null;
		private Node right = null;

		public Node(Point2D p, RectHV rect) {
			this.point = p;
			this.rect = rect;
		}
	}

}
