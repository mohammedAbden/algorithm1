package puzzle;

import java.util.LinkedList;

import edu.princeton.cs.algs4.MinPQ;

public class Solver {

	private Board initial;
	private LinkedList<Board> solutions;

	public Solver(Board initial) {
		if (initial == null) {
			throw new IllegalArgumentException();
		}
		this.initial = initial;
		solutions = new LinkedList<>();
		solve();
	}

	private void solve() {

		MinPQ<Node> que = new MinPQ<>();
		MinPQ<Node> queTwin = new MinPQ<>();
		que.insert(new Node(initial, null));
		queTwin.insert(new Node(initial.twin(), null));
		while (!que.min().current.isGoal() && (!queTwin.min().current.isGoal())) {
			Node x = que.delMin();

			for (Board xx : x.current.neighbors()) {
				Node child = new Node(xx, x);
				if (!child.isInTree()) {
					que.insert(child);
				}
			}

			x = queTwin.delMin();
			for (Board xx : x.current.neighbors()) {
				Node child = new Node(xx, x);
				if (!child.isInTree()) {
					queTwin.insert(child);
				}
			}
		}

		if (que.min().current.isGoal()) {
			Node goal = que.min();
			while (goal != null) {
				solutions.addFirst(goal.current);
				goal = goal.parent;
			}
		}
	}

	public boolean isSolvable() {
		return !solutions.isEmpty();
	}

	public int moves() {
		return (solutions.isEmpty()) ? -1 : solutions.size() - 1;
	}

	public Iterable<Board> solution() {
		if (isSolvable()) {
			return solutions;
		}
		return null;
	}

	private class Node implements Comparable<Node> {
		final Node parent;
		final Board current;
		final int move;
		final int priority;

		public boolean isInTree() {
			return this.current.equals(((this.parent.parent != null) ? this.parent.parent.current : null));
		}

		public Node(Board current, Node parent) {
			this.parent = parent;
			this.current = current;
			if (parent == null) {
				move = 0;
			} else {
				move = parent.move + 1;
			}
			priority = current.manhattan() + move;
		}

		@Override
		public int compareTo(Node o) {
			return priority - o.priority;
		}

		@Override
		public int hashCode() {
			return super.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return super.equals(obj);
		}
	}
}
