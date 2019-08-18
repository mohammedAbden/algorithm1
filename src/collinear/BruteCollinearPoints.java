package collinear;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {

	private int count = 0;
	private final ArrayList<LineSegment> lines;

	public BruteCollinearPoints(final Point[] points) {
		if (points == null || anyMemberIsNull(points))
			throw new IllegalArgumentException();
		Point[] arrCopy = points.clone();
		Arrays.sort(arrCopy);
		lines = new ArrayList<>();
		checkArgument(arrCopy);
		solve(arrCopy);
	}

	private void solve(Point[] arrCopy) {
		for (int i = 0; i < arrCopy.length; i++)
			for (int j = i + 1; j < arrCopy.length; j++) {
				for (int k = j + 1; k < arrCopy.length; k++)
					for (int l = k + 1; l < arrCopy.length; l++) {
						checkLineAndCount(arrCopy, i, j, k, l);
					}
			}
	}

	private void checkLineAndCount(Point[] arrCopy, int i, int j, int k, int l) {
		double slope = arrCopy[i].slopeTo(arrCopy[j]);
		if (arrCopy[i].slopeTo(arrCopy[k]) == slope && arrCopy[i].slopeTo(arrCopy[l]) == slope) {
			lines.add(new LineSegment(arrCopy[i], arrCopy[l]));
			count++;
		}
	}

	private void checkArgument(Point[] arrCopy) {
		for (int i = 0; i < arrCopy.length - 1; i++)
			if (arrCopy[i].slopeTo(arrCopy[i + 1]) == Double.NEGATIVE_INFINITY)
				throw new IllegalArgumentException();
	}

	public int numberOfSegments() {
		return count;
	}

	private boolean anyMemberIsNull(Point[] points) {
		for (Point point : points) {
			if (point == null)
				return true;
		}
		return false;
	}

	public LineSegment[] segments() {
		LineSegment[] arr = new LineSegment[count];
		for (int i = 0; i < lines.size(); i++)
			arr[i] = lines.get(i);
		return arr;
	}
}
