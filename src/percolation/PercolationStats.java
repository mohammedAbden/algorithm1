package percolation;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

	private static final double CONFIDENCE_95 = 1.96d;
	private final double mean;
	private final double stddev;
	private final int trials;

	public PercolationStats(int n, int trials) {
		if (n <= 0 || trials <= 0)
			throw new IllegalArgumentException();

		double[] results = new double[trials];
		this.trials = trials;

		for (int i = 0; i < trials; i++) {
			results[i] = performOneTrial(n);
		}
		this.mean = StdStats.mean(results);
		this.stddev = StdStats.stddev(results);
	}

	private double performOneTrial(int n) {
		Percolation percolation = new Percolation(n);
		double countOpend = 0;
		while (!percolation.percolates()) {
			openNewCell(percolation, n);
			countOpend++;
		}
		return countOpend / (n * n);
	}

	private void openNewCell(Percolation percolation, int n) {
		int row = 0;
		int col = 0;
		do {
			row = StdRandom.uniform(n) + 1;
			col = StdRandom.uniform(n) + 1;
		} while (percolation.isOpen(row, col));
		percolation.open(row, col);
	}

	public double mean() {
		return this.mean;
	}

	public double stddev() {
		return this.stddev;
	}

	public double confidenceLo() {
		return this.mean - CONFIDENCE_95 * this.stddev() / Math.sqrt(this.trials);
	}

	public double confidenceHi() {
		return this.mean + CONFIDENCE_95 * this.stddev() / Math.sqrt(this.trials);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("mean = " + this.mean() + "\n");
		sb.append("stddev = " + this.stddev() + "\n");
		sb.append("95% confidence interval = [" + this.confidenceLo() + ", " + this.confidenceHi() + "]");
		return sb.toString();

	}

	public static void main(String[] args) {
		int n = Integer.parseInt(args[0]);
		int trials = Integer.parseInt(args[1]);
		PercolationStats pStatus = new PercolationStats(n, trials);
		StdOut.println(pStatus);
	}

}
