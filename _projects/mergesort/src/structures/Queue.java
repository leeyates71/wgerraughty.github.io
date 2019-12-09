package structures;

import java.util.NoSuchElementException;
import java.util.Iterator;

public class Queue<T> implements UnboundedQueueInterface<T>, Iterable<T> {

    /**
     * The {@code Node} which links to the back of the queue.
     */
	private Node<T> head;

    /**
     * The {@code Node} which links to the front of the queue.
     */
	private Node<T> tail;

    /**
     * The size of the queue.
     */
	private int size = 0;

    /**
     * Default constructor
     */
	public Queue() {

	}

    /**
     * Copy constructor, does not mutate original queue
     * @param queue
     *              an input queue
     */
	public Queue(Queue<T> queue) {
		Node<T> cur = queue.head;
		while (cur != null) {
			enqueue(cur.getData());
			cur = cur.getNext();
		}
	}

    /**
     * Implements iterable interface
     * @return Queue Iterator object
     */
	public Iterator<T> iterator() {
	    return new QueueIterator<T>(this);
    }

    /**
     * {@inheritDoc}
     */
	@Override
	public boolean isEmpty() {
		return head == null;
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public int size() {
		return size;
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public void enqueue(T element) {
		Node<T> tmp = new Node<T>(element);
		if (isEmpty()) {
			head = tmp;
			tail = head;
		} else {
			tail.setNext(tmp);
			tail = tmp;
		}
		size++;

	}

    /**
     * Enqueues the entirety of the input queue.
     * Does not modify the input queue.
     * @param queue
     */
	public void enqueue(Queue<T> queue) {
        Node<T> cur = queue.head;
        while (cur != null) {
            enqueue(cur.getData());
            cur = cur.getNext();
        }
    }

    /**
     * Destroys the queue, removing all elements
     */
    public void setEmpty() {
	    head = null;
	    tail = head;
	    size = 0;
    }

    /**
     * {@inheritDoc}
     */
	@Override
	public T dequeue() throws NoSuchElementException {
		if (isEmpty())
			throw new NoSuchElementException("Empty Queue!");
		T ret = head.getData();
		head = head.getNext();
		size--;
		if (size == 0) {
			head = null;
			tail = null;
		}
		return ret;
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public T peek() throws NoSuchElementException {
		if (isEmpty())
			throw new NoSuchElementException("Empty Queue!");
		return head.getData();
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public UnboundedQueueInterface<T> reversed() {
		LinkedStack<T> stack = new LinkedStack<T>();
		Node<T> cur = head;
		while (cur != null) {
			stack.push(cur.getData());
			cur = cur.getNext();
		}
		Queue<T> output = new Queue<T>();
		while (!stack.isEmpty()) {
			output.enqueue(stack.pop());
		}
		return output;
	}

    /**
     * Returns (but does not remove) the last element in the queue.
     * @return Last element in the queue
     * @throws NoSuchElementException
     */
	public T peekTail() throws NoSuchElementException {
	    if (isEmpty())
	        throw new NoSuchElementException("Empty Queue!");
        return tail.getData();
    }

    /**
     * Implements the Iterator class for the Queue object
     * @param <T>
     */
    class QueueIterator<T> implements Iterator<T> {
	    Node<T> cursor;
	    public QueueIterator(Queue<T> queue) {
	        cursor = queue.head;
        }

        /**
         * {@inheritDoc}
         */
        public boolean hasNext() {
	        return cursor != null;
        }

        /**
         * {@inheritDoc}
         */
        public T next() {
            if (cursor == null) throw new NoSuchElementException();
	        T data = cursor.getData();
	        cursor = cursor.getNext();
	        return data;
        }
    }

}
