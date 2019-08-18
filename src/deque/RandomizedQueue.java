package deque;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<T> implements Iterable<T> {

	private T[] a;
	private int n;

	public RandomizedQueue() {
		a = (T[]) new Object[2];
		n = 0;
	}

	public boolean isEmpty() {
		return n == 0;
	}

	public int size() {
		return n;
	}

	public void enqueue(T item) {
		verifyNotNull(item);
		if (n == a.length)
			resize(2 * a.length);
		a[n++] = item;
	}

	public T dequeue() {
		if (isEmpty())
			throw new NoSuchElementException();

		int index = StdRandom.uniform(n);
		T item = a[index];
		a[index] = a[n - 1];
		a[n - 1] = null;
		n--;
		if (n > 0 && n == a.length / 4)
			resize(a.length / 2);
		return item;
	}

	public T sample() {
		if (isEmpty())
			throw new NoSuchElementException();
		return a[StdRandom.uniform(n)];
	}

	private void resize(int capacity) {
		T[] temp = (T[]) new Object[capacity];
		for (int i = 0; i < n; i++) {
			temp[i] = a[i];
		}
		a = temp;
	}

	private void verifyNotNull(T x) {
		if (x == null)
			throw new IllegalArgumentException();
	}

	@Override
	public Iterator<T> iterator() {
		return new ListIterator();
	}

	private class ListIterator implements Iterator<T> {

		T[] data;
		int index = 0;

		public ListIterator() {
			data = (T[]) new Object[n];
			for (int i = 0; i < n; i++) {
				data[i] = a[i];
			}
		}

		@Override
		public boolean hasNext() {
			return index < data.length;
		}

		@Override
		public T next() {
			if (!hasNext())
				throw new NoSuchElementException();
			int next = StdRandom.uniform(index, data.length);
			T x = data[next];
			data[next] = data[index];
			data[index++] = null;
			return x;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

}