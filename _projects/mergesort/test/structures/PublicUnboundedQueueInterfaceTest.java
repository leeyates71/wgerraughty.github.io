package structures;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;

import org.junit.Test;


public class PublicUnboundedQueueInterfaceTest {
	UnboundedQueueInterface<Integer> q;

    /**
     * Tests the copy constructor on an empty queue
     *
     * @throws Exception
     */
	@Test
	public void testCopyConstructorEmpty() throws Exception  {
		Queue<Integer> q = new Queue<Integer>();
		UnboundedQueueInterface<Integer> r;
		r = new Queue<Integer>(q);
		assertTrue(r.isEmpty());
		assertTrue(q.isEmpty());		
	}

    /**
     * Tests whether the copy constructor mutates the input
     * while the queue is empty
     *
     * @throws Exception
     */
	@Test
	public void testCopyConstructorEmptyNotAliased() throws Exception  {
		Queue<Integer> q = new Queue<Integer>();
		UnboundedQueueInterface<Integer> r;
		r = new Queue<Integer>(q);
		assertTrue(r.isEmpty());
		assertTrue(q.isEmpty());		

		q.enqueue(1);
		q.enqueue(2);
		assertEquals(2, q.size());
		assertTrue(r.isEmpty());
				
		r.enqueue(3);
		r.enqueue(4);
		r.enqueue(5);
		assertEquals(2, q.size());
		assertEquals(3, r.size());
		
		r.dequeue();
		r.dequeue();
		r.dequeue();
		assertTrue(r.isEmpty());
		assertEquals(2, q.size());
		
		q.dequeue();
		q.dequeue();
		assertTrue(q.isEmpty());
	}

    /**
     * Tests whether the copy constructor successfully copies
     * with one element
     *
     * @throws Exception
     */
	@Test
	public void testCopyConstructorOneElement() throws Exception  {
		Queue<Integer> q = new Queue<Integer>();
		UnboundedQueueInterface<Integer> r;
		q.enqueue(1);
		r = new Queue<Integer>(q);
		
		assertEquals(1, q.size());
		assertEquals(1, r.size());
	}

    /**
     * Tests whether the copy constructor mutates the input
     * while the queue contains one element
     *
     * @throws Exception
     */
	@Test
	public void testCopyConstructorOneElementNotAliased() throws Exception  {
		Queue<Integer> q = new Queue<Integer>();
		UnboundedQueueInterface<Integer> r;
		q.enqueue(1);
		r = new Queue<Integer>(q);

		q.enqueue(2);
		assertEquals(1, (int)r.dequeue());
		assertTrue(r.isEmpty());
		assertEquals(2, q.size());
	}

    /**
     * Tests whether the copy constructor successfully copies
     * with more than one element
     *
     * @throws Exception
     */
	@Test
	public void testCopyConstructorTwoElements() throws Exception  {
		Queue<Integer> q = new Queue<Integer>();
		UnboundedQueueInterface<Integer> r;
		q.enqueue(1);
		q.enqueue(2);
		r = new Queue<Integer>(q);
		
		assertEquals(2, q.size());
		assertEquals(2, r.size());
	}

    /**
     * Tests whether the copy constructor mutates the input
     * while the queue contains one element
     *
     * @throws Exception
     */
	@Test
	public void testCopyConstructorTwoElementsNotAliased() throws Exception  {
		Queue<Integer> q = new Queue<Integer>();
		UnboundedQueueInterface<Integer> r;
		q.enqueue(1);
		q.enqueue(2);
		r = new Queue<Integer>(q);

		q.enqueue(3);
		assertEquals(1, (int)r.dequeue());
		assertEquals(3, q.size());
		assertEquals(1, r.size());
	}

    /**
     * Tests the functionality of the reverse function
     *
     * @throws Exception
     */
	@Test
	public void testReverse() throws Exception  {
		UnboundedQueueInterface<Integer> q = new Queue<Integer>();
		q.enqueue(1);
		q.enqueue(2);
		q.enqueue(3);
		q.enqueue(4);
		q.enqueue(5);
		q.enqueue(6);
		q = q.reversed();
		for (int i = 6; i > 0; i--) {
			assertEquals(i, (int)q.dequeue());
		}
	}
	
	@Test
	public void testReverseEmpty() throws Exception  {
		UnboundedQueueInterface<Integer> q = new Queue<Integer>();
		UnboundedQueueInterface<Integer> r = q.reversed();
		assertFalse(r == q);
		assertTrue(r.isEmpty() && q.isEmpty());
	}

    /**
     * Tests that the copy constructor contains the same elements
     * without mutating the input queue
     *
     * @throws Exception
     */
	@Test
	public void testCopyAlias() throws Exception  {
		Queue<Integer> q = new Queue<Integer>();
		q.enqueue(1);
		q.enqueue(2);
		q.enqueue(3);
		UnboundedQueueInterface<Integer> r = new Queue<Integer>(q);
		assertFalse(r == q);
		int k = q.size();
		for (int i = 0; i < k; i++) {
			assertTrue(q.dequeue() == r.dequeue());
		}
	}

    /**
     * Tests whether the dequeue function properly throws a NoSuchElementException
     */
	@Test(expected=NoSuchElementException.class)
	public void testEmptyDequeue() {
		UnboundedQueueInterface<Integer> q = new Queue<Integer>();
		int i = q.dequeue();
		System.out.println(i);
	}

    /**
     * Tests whether the peek function properly throws a NoSuchElementException
     */
	@Test(expected=NoSuchElementException.class)
	public void testEmptyPeek() {
		UnboundedQueueInterface<Integer> q = new Queue<Integer>();
		System.out.println(q.peek());
	}

}
