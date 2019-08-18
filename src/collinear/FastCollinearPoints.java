package collinear;

import java.util.Arrays;
import java.util.LinkedList;

public class FastCollinearPoints {

	private int count = 0;
	private LinkedList<LineSegment> lines;

	public FastCollinearPoints(Point[] points) {

		if (points == null || anyMemberIsNull(points))
			throw new IllegalArgumentException();

		Point[] arrCopy = points.clone();
		lines = new LinkedList<>();
		Arrays.sort(arrCopy);
		for (int i = 0; i < arrCopy.length - 1; i++)
			if (isSampePoint(arrCopy[i], arrCopy[i + 1]))
				throw new IllegalArgumentException();

		for (int i = 0; i < arrCopy.length; i++) {
			Point x = points[i];
			Arrays.sort(arrCopy, x.slopeOrder());
			getSegments(x, arrCopy);
		}
	}

	private void getSegments(Point x, Point[] points) {

		Point max = x;
		Point min = x;
		int c = 0;
		for (int i = 2; i < points.length; i++) {
			double slope1 = x.slopeTo(points[i]);
			double slope2 = x.slopeTo(points[i - 1]);
			if (slope1 == slope2) {
				c++;
				max = getMax(max, getMax(points[i - 1], points[i]));
				min = getMin(min, getMin(points[i - 1], points[i]));
				if (i == points.length - 1 && c >= 2 && isSampePoint(x, min)) {
					lines.add(new LineSegment(min, max));
					count++;
				}
			} else if (c >= 2) {
				if (isSampePoint(x, min)) {
					lines.add(new LineSegment(min, max));
					count++;
				}
				c = 0;
				max = x;
				min = x;
			} else {
				c = 0;
				max = x;
				min = x;
			}

		}
	}

	private Point getMax(Point a, Point b) {
		return (a.compareTo(b) > 0) ? a : b;
	}

	private Point getMin(Point a, Point b) {
		return (a.compareTo(b) < 0) ? a : b;
	}

	private boolean isSampePoint(Point a, Point b) {
		return a.compareTo(b) == 0;
	}

	private boolean anyMemberIsNull(Point[] points) {
		for (Point point : points) {
			if (point == null)
				return true;
		}
		return false;
	}

	public int numberOfSegments() {
		return count;
	}

	public LineSegment[] segments() {
		return lines.toArray(new LineSegment[lines.size()]);
	}

}