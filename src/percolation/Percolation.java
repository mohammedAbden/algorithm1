package percolation;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

	private int numberOfOpens;
	private final int[] isOpen;
	private final int[] size;
	private final int n;
	private final WeightedQuickUnionUF quickFind;

	public Percolation(int n) {
		if (n <= 0)
			throw new IllegalArgumentException();
		this.n = n;
		this.numberOfOpens = 0;
		this.isOpen = new int[(n + 2) * (n + 2) + 2];
		this.size = new int[(n + 2) * (n + 2) + 2];
		for (int i = 0; i < size.length; i++)
			size[i] = 1;

		this.quickFind = new WeightedQuickUnionUF((n + 2) * (n + 2) + 2);
	}

	public void open(int row, int col) {
		if (!isOpen(row, col)) {
			int index = convertFrom2DimTo1Dim(row, col);
			isOpen[index] = index;
			this.numberOfOpens++;

			// for up
			tryToConnect(index, row - 1, col); 
			// for down
			tryToConnect(index, row + 1, col); 
			// for left
			tryToConnect(index, row, col - 1); 
			// for right
			tryToConnect(index, row, col + 1); 

			if (shouldConnetToTop(row))
				this.unionWithTop(index);

			if (shouldConnetToDown(row))
				this.unionWithDown(index);
		}

	}

	private void tryToConnect(int index, int row, int col) {
		if (!isBadIndexing(row, col) && isOpen(row, col)) {
			quickFind.union(index, convertFrom2DimTo1Dim(row, col));
			union(index, convertFrom2DimTo1Dim(row, col));
		}
	}

	public boolean isOpen(int row, int col) {
		return isOpen[convertFrom2DimTo1Dim(row, col)] > 0;
	}

	public boolean isFull(int row, int col) {
		return isOpen(row, col) && directConnectWithTop(convertFrom2DimTo1Dim(row, col));
	}

	public int numberOfOpenSites() {
		return this.numberOfOpens;
	}

	public boolean percolates() {
		return quickFind.connected(getTopIndex(), getDownIndex());
	}

	private int find(int parent) {
		int p = parent;
		while (p != isOpen[p]) {
			isOpen[p] = isOpen[isOpen[p]];
			p = isOpen[p];
		}
		return p;
	}

	private void union(int p, int q) {
		int rootP = find(p);
		int rootQ = find(q);
		if (rootP == rootQ)
			return;

		if (size[rootP] < size[rootQ]) {
			isOpen[rootP] = rootQ;
			size[rootQ] += size[rootP];
		} else {
			isOpen[rootQ] = rootP;
			size[rootP] += size[rootQ];
		}

	}

	private boolean directConnectWithTop(int p) {
		return find(p) == find(getTopIndex());
	}

	private int convertFrom2DimTo1Dim(int row, int col) {
		if (isBadIndexing(row, col))
			throw new IllegalArgumentException("Row = " + row + " Col = " + col);
		return row * n + col;
	}

	private boolean isBadIndexing(int row, int col) {
		return row < 1 || row > this.n || col < 1 || col > this.n;
	}

	private void unionWithTop(int index) {
		quickFind.union(index, getTopIndex());
		union(index, getTopIndex());
	}

	private void unionWithDown(int index) {
		quickFind.union(index, getDownIndex());
	}

	private boolean shouldConnetToTop(int row) {
		return row == 1;
	}

	private boolean shouldConnetToDown(int row) {
		return row == this.n;
	}

	private int getTopIndex() {
		return isOpen.length - 2;
	}

	private int getDownIndex() {
		return isOpen.length - 1;
	}

}
