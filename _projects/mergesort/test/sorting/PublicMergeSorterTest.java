package sorting;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;


import structures.Queue;

public class PublicMergeSorterTest {
	private MergeSorter<Integer> ms = new MergeSorter<Integer>();
	private Queue<Integer> empty;
	private Queue<Integer> one;
	private Queue<Integer> unsorted;
	private Queue<Integer> output1;
	private Queue<Integer> output2;
	private Queue<Integer> unsortedRandomLarge;
	private Queue<Integer> sortedLarge;
	private Random random;

	/**
	 * Sets up:
     * an empty queue,
     * a queue with one element,
	 * an unsorted queue with 10 elements,
     * a sorted queue with 10,000 elements,
     * an unsorted queue with 10,000 elements
	 */
	@Before
	public void before() {
		empty = new Queue<Integer>();
		one = new Queue<Integer>();

		output1 = new Queue<Integer>();		
		output2 = new Queue<Integer>();

		one.enqueue(1);
		unsorted = new Queue<Integer>();
		for (int i : new int[] {8, 4, 0, 3, 6, 1, 7, 9, 5, 2}) {
			unsorted.enqueue(i);
		}

        sortedLarge = new Queue<Integer>();
        for (int i = 0; i < 10000; i++) {
            sortedLarge.enqueue(i);
        }

		random = new Random();
		unsortedRandomLarge = new Queue<Integer>();
        for (int i = 1; i < 10000; i++) {
            unsortedRandomLarge.enqueue(random.nextInt(i));
        }
	}

	/**
	 * Tests the divide function on an empty queue
	 *
	 * @throws Exception
	 */
	@Test
	public void testDivideEmpty() throws Exception {
		ms.divide(empty, output1, output2);
		assertTrue(empty.isEmpty());
		assertTrue(output1.isEmpty());
		assertTrue(output2.isEmpty());
	}

	/**
	 * Tests the divide function on a queue with one element
	 *
	 * @throws Exception
	 */
	@Test
	public void testDivideOne() throws Exception {
		ms.divide(one, output1, output2);
		assertTrue(one.isEmpty());
		assertTrue(output1.isEmpty() || output2.isEmpty());
		assertFalse(output1.isEmpty() && output2.isEmpty());
	}

	/**
	 * Tests that the divide function splits queues evenly
	 *
	 * @throws Exception
	 */
	@Test
	public void testDivideNSplitsEvenly() throws Exception {
		Queue<Integer> q = new Queue<Integer>();
		for (int i = 0; i < 11; i++) {
			q.enqueue(i);
		}
		ms.divide(q, output1, output2);
		assertTrue(((output1.size() == 5) && (output2.size() == 6)) ||
				   ((output1.size() == 6) && (output2.size() == 5)));;
	}

	/**
	 * Tests that the divide function places all elements into outputs
	 *
	 * @throws Exception
	 */
	@Test
	public void testDivideNContainsAll() throws Exception {
		Queue<Integer> q = new Queue<Integer>();
		for (int i = 0; i < 11; i++) {
			q.enqueue(i);
		}
		ms.divide(q, output1, output2);
		int[] t = new int[11];
		int i = 0;
		while (!output1.isEmpty()) {
			t[i] = output1.dequeue();
			i++;
		}
		while (!output2.isEmpty()) {
			t[i] = output2.dequeue();
			i++;
		}
		Arrays.sort(t);
		for (int j = 0; j < 11; j++) {
			assertEquals(j, t[j]);
		}
	}

	/**
	 * Tests the merge function on empty inputs
	 *
	 * @throws Exception
	 */
	@Test
	public void testMergeTwoEmpty() throws Exception {		
		Queue<Integer> result = ms.merge(empty, empty);
		assertTrue(result.isEmpty());
	}

	/**
	 * Tests the merge function with one element in the left queue
	 *
	 * @throws Exception
	 */
	@Test
	public void testMergeOneAndEmpty() throws Exception {
		Queue<Integer> result = ms.merge(one, empty);
		assertEquals(1, result.size());
		assertEquals(1, (int)result.dequeue());		
	}

	/**
	 * Tests the merge function with one element in the right queue
	 *
	 * @throws Exception
	 */
	@Test
	public void testMergeEmptyAndOne() throws Exception {
		Queue<Integer> result = ms.merge(empty, one);
		assertEquals(1, result.size());
		assertEquals(1, (int)result.dequeue());		
	}

	/**
	 * Tests the merge function with one element in both queues
	 *
	 * @throws Exception
	 */
	@Test
	public void testMergeOneAndOne() throws Exception {
		Queue<Integer> two = new Queue<Integer>();
		two.enqueue(2);
		Queue<Integer> result = ms.merge(two, one);
		assertEquals(2, result.size());
		assertEquals(1, (int)result.dequeue());
		assertEquals(2, (int)result.dequeue());
	}

	/**
	 * Tests the merge function with uneven inputs
	 *
	 * @throws Exception
	 */
	@Test
	public void testMergeThreeAndOne() throws Exception {
		Queue<Integer> three = new Queue<Integer>();
		three.enqueue(-1);
		three.enqueue(1);
		three.enqueue(2);
		
		Queue<Integer> result = ms.merge(three, one);
		assertEquals(4, result.size());
		assertEquals(-1, (int)result.dequeue());
		assertEquals(1, (int)result.dequeue());
		assertEquals(1, (int)result.dequeue());
		assertEquals(2, (int)result.dequeue());
	}

	/**
	 * Tests the merge function with uneven inputs
	 *
	 * @throws Exception
	 */
	@Test
	public void testMergeOneAndThree() throws Exception {
		Queue<Integer> three = new Queue<Integer>();
		three.enqueue(-1);
		three.enqueue(1);
		three.enqueue(2);
		
		Queue<Integer> result = ms.merge(one, three);
		assertEquals(4, result.size());
		assertEquals(-1, (int)result.dequeue());
		assertEquals(1, (int)result.dequeue());
		assertEquals(1, (int)result.dequeue());
		assertEquals(2, (int)result.dequeue());
	}

	/**
	 * Tests the mergesort function on an empty queue
	 *
	 * @throws Exception
	 */
	@Test
	public void testMergeSortEmpty() throws Exception {
		Queue<Integer> result = ms.mergeSort(empty);
		assertTrue(result.isEmpty());
	}

	/**
	 * Tests that the mergesort function does not mutate the input
	 * with an empty queue
	 *
	 * @throws Exception
	 */
	@Test
	public void testMergeSortEmptyUnaliased() throws Exception {
		Queue<Integer> result = ms.mergeSort(empty);
		assertTrue(result.isEmpty());
		empty.enqueue(1);
		assertTrue(result.isEmpty());
	}

	/**
	 * Tests that the mergesort returns a sorted queue
	 *
	 * @throws Exception
	 */
	@Test
	public void testMergeSort() throws Exception {
		Queue<Integer> sorted = ms.mergeSort(unsorted);
		assertEquals(10, sorted.size());
		for (int i = 0; i < 10; i++) {
			assertEquals(i, (int)sorted.dequeue());
		}
	}

	/**
	 * Tests that the mergesort doesn't mutate the input
	 * with a queue containing elements
	 *
	 * @throws Exception
	 */
	@Test
	public void testMergeSortUnaliased() throws Exception {
		Queue<Integer> sorted = ms.mergeSort(unsorted);
		sorted.dequeue();
		unsorted.enqueue(10);
		assertEquals(9, sorted.size());
		assertEquals(11, unsorted.size());
	}

	/**
	 * Tests the mergesort function with a more randomized queue
	 *
	 * @throws Exception
	 */
	@Test
	public void testMergeSort2() throws Exception {
		unsorted = new Queue<Integer>();
		for (int i : new int[] {24, 90, 82, 65, 2, 25, 48, 2, 67, 26}) {
			unsorted.enqueue(i);
		}
		Queue<Integer> sorted = ms.mergeSort(unsorted);
		assertEquals(10, sorted.size());
		for (int i : new int[] {2,2,24,25,26,48,65,67,82,90}) {
			assertEquals(i, (int)sorted.dequeue());
		}
	}

	/**
	 * Tests the mergesort function with large inputs
	 *
	 * @throws Exception
	 */
	@Test
	public void testMergeSortLarge() throws Exception {
		Queue<Integer> sorted = ms.mergeSort(sortedLarge);
		for (int i = 0; i < 10000; i++) {
			assertEquals(i, (int)sorted.dequeue());
		}
	}

    /**
     * Tests the mergesort function with a large, unsorted queue of randomly generated inputs
     *
     * @throws Exception
     */
	@Test
	public void testMergeSortRandom() throws Exception {
        Queue<Integer> sorted = ms.mergeSort(unsortedRandomLarge);
		int k = 0;
		for (int i : sorted) {
		    assertTrue(i >= k);
		    k = i;
        }
	}
	
}
