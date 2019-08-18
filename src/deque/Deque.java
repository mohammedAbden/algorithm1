package deque;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<T> implements Iterable<T> {

	private Node first;
	private Node last;

	private int size;

	public boolean isEmpty() {
		return size == 0;
	}

	public int size() {
		return this.size;
	}

	public void addFirst(T item) {
		verifyNotNull(item);
		if (this.size == 0)
			addFirstItem(item);
		else {
			first.previos = new Node(null, item, first);
			first = first.previos;
			this.size++;
		}
	}

	public void addLast(T item) {
		verifyNotNull(item);
		if (this.size == 0)
			addFirstItem(item);
		else {
			last.next = new Node(last, item, null);
			last = last.next;
			this.size++;
		}
	}

	public T removeFirst() {
		if (isEmpty())
			throw new NoSuchElementException();
		T x = first.item;
		first = first.next;
		// last item remove
		if (size > 1)
			first.previos = null;
		else
			first = last = null;
		this.size--;
		return x;
	}

	public T removeLast() {
		if (isEmpty())
			throw new NoSuchElementException();
		T x = last.item;
		last = last.previos;
		// last item remove
		if (size > 1)
			last.next = null;
		else
			first = last = null;
		this.size--;
		return x;
	} 

	private void verifyNotNull(T x) {
		if (x == null)
			throw new IllegalArgumentException();
	}

	private void addFirstItem(T x) {
		first = new Node(null, x, null);
		last = first;
		this.size = 1;
	}

	@Override
	public Iterator<T> iterator() {
		return new ListIterator();
	}

	private class Node {
		private T item;
		private Node next;
		private Node previos;

		public Node(final Node pre, final T x, final Node next) {
			item = x;
			this.next = next;
			this.previos = pre;
		}
	}

	private class ListIterator implements Iterator<T> {

		Node current = first;

		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public T next() {
			if (!hasNext())
				throw new NoSuchElementException();
			T x = current.item;
			current = current.next;
			return x;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

}