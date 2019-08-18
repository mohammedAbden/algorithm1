package puzzle;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Board {

	private final int[][] boardData;
	private int blankRow;
	private int blankCol;
	private int hamming;
	private int manhattan;
	private final boolean isGoal;

	public Board(int[][] blocks) {
		this.boardData = blocks;
		for (int r = 0; r < boardData.length; r++) {
			for (int c = 0; c < boardData.length; c++) {
				if (isBlank(r, c)) {
					this.blankRow = r;
					this.blankCol = c;
				} else {
					updateHammingAndManhattan(getDistanceBetweenValueAndGoal(boardData[r][c], r, c));
				}
			}
		}
		isGoal = manhattan == 0;
	}

	private boolean isBlank(int r, int c) {
		return this.boardData[r][c] == 0;
	}

	private Board(int[][] blocks, int blankRow, int blankCol, int hamming, int manhattan) {
		this.boardData = blocks;
		this.blankRow = blankRow;
		this.blankCol = blankCol;
		this.hamming = hamming;
		this.manhattan = manhattan;
		isGoal = manhattan == 0;
	}

	private void updateHammingAndManhattan(int distance) {
		if (distance > 0) {
			hamming++;
			manhattan += distance;
		}
	}

	public int dimension() {
		return boardData.length;
	}

	public int hamming() {
		return hamming;
	}

	public int manhattan() {
		return manhattan;
	}

	public boolean isGoal() {
		return this.isGoal;
	}

	public Board twin() {
		int r1 = 0;
		int c1 = 0;
		int r2 = 0;
		int c2 = 0;
		boolean firstvalue = true;
		boolean continueLooping = true;
		for (int r = 0; r < boardData.length && continueLooping; r++) {
			for (int c = 0; c < boardData.length && continueLooping; c++) {
				if (!isBlank(r, c) && firstvalue) {
					r1 = r;
					c1 = c;
					firstvalue = false;
				} else if (!isBlank(r, c) && (!firstvalue)) {
					r2 = r;
					c2 = c;
					continueLooping = false;
				}
			}
		}
		int[][] newBoard = copyOfBoard();
		int dummy = newBoard[r1][c1];
		newBoard[r1][c1] = newBoard[r2][c2];
		newBoard[r2][c2] = dummy;
		return new Board(newBoard);
	}

	private int getDistanceBetweenValueAndGoal(int value, int row, int col) {
		// should start from 0
		int index = value - 1;
		int rightRow = index / boardData.length;
		int rightCol = index % boardData.length;
		return Math.abs(rightRow - row) + Math.abs(rightCol - col);
	}

	@Override
	public boolean equals(Object y) {
		if (y == this) {
			return true;
		}
		if (y == null) {
			return false;
		}
		if (y.getClass() != this.getClass()) {
			return false;
		}
		Board that = (Board) y;
		return Arrays.deepEquals(this.boardData, that.boardData);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public Iterable<Board> neighbors() {
		return new NeighborsIterable(this);
	}

	@Override
	public String toString() {
		StringBuilder ou = new StringBuilder();
		ou.append(boardData.length).append("\n");
		for (int[] x : boardData) {
			for (int xx : x) {
				ou.append(xx).append(" ");
			}
			ou.append("\n");
		}
		return ou.toString();
	}

	private int[][] copyOfBoard() {
		int[][] copy = new int[dimension()][dimension()];
		for (int r = 0; r < dimension(); r++) {
			for (int c = 0; c < dimension(); c++) {
				copy[r][c] = this.boardData[r][c];
			}
		}
		return copy;
	}

	private class NeighborsIterable implements Iterable<Board> {
		private final Board parent;

		public NeighborsIterable(Board board) {
			this.parent = board;
		}

		@Override
		public Iterator<Board> iterator() {
			return new MyIterator(parent);
		}

		private class MyIterator implements Iterator<Board> {
			private final Board parent;
			private int count = 0;

			private MyIterator(Board board) {
				this.parent = board;
			}

			@Override
			public Board next() {
				switch (count) {
				case 1:
					return moveLeft();
				case 2:
					return moveright();
				case 3:
					return moveUp();
				case 4:
					return moveDown();
				default:
					throw new NoSuchElementException();
				}
			}

			@Override
			public boolean hasNext() {
				switch (count) {
				case 0:
					// return (canMoveLeft()) ? true : hasNext();
					return canMoveLeft() || hasNext();
				case 1:
					// return (canMoveRight()) ? true : hasNext();
					return canMoveRight() || hasNext();
				case 2:
					// return (canMoveUp()) ? true : hasNext();
					return canMoveUp() || hasNext();
				case 3:
					// return (canMoveDown()) ? true : hasNext();
					return canMoveDown() || hasNext();
				default:
					return false;
				}

			}

			private Board move(int newRowBlank, int newColBlank) {
				int[][] newBoard = copyOfBoard();
				swapBlank(newBoard, newRowBlank, newColBlank);
				int value = newBoard[this.parent.blankRow][this.parent.blankCol];
				int newDistance = getDistanceBetweenValueAndGoal(value, this.parent.blankRow, this.parent.blankCol);
				int oldDistance = getDistanceBetweenValueAndGoal(value, newRowBlank, newColBlank);
				int newManhattan = this.parent.manhattan + newDistance - oldDistance;
				int newHamming = updateHammingAftreChange(oldDistance, newDistance);
				return new Board(newBoard, newRowBlank, newColBlank, newHamming, newManhattan);
			}

			private int updateHammingAftreChange(int oldDistance, int newDistance) {
				if (oldDistance == 0 && newDistance != 0) { // redandancy
					return hamming + 1;
				} else if (oldDistance != 0 && newDistance == 0) {
					return hamming - 1;
				}
				return hamming;
			}

			private void swapBlank(int[][] array, int newRow, int newCol) {
				int dummy = array[this.parent.blankRow][this.parent.blankCol];
				array[this.parent.blankRow][this.parent.blankCol] = array[newRow][newCol];
				array[newRow][newCol] = dummy;
			}

			private Board moveright() {
				return move(this.parent.blankRow, this.parent.blankCol + 1);
			}

			private boolean canMoveDown() {
				count++;
				return this.parent.blankRow < dimension() - 1;
			}

			private boolean canMoveUp() {
				count++;
				return this.parent.blankRow > 0;
			}

			private boolean canMoveRight() {
				count++;
				return this.parent.blankCol < dimension() - 1;
			}

			private boolean canMoveLeft() {
				count++;
				return this.parent.blankCol > 0;
			}

			private Board moveDown() {
				return move(this.parent.blankRow + 1, this.parent.blankCol);
			}

			private Board moveUp() {
				return move(this.parent.blankRow - 1, this.parent.blankCol);
			}

			private Board moveLeft() {
				return move(this.parent.blankRow, this.parent.blankCol - 1);
			}
		}

	}

}
