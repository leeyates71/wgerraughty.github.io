package sorting;

import structures.Queue;

/**
 * A class containing methods to sort queues and merge sorted queues.
 * 
 * "Sorted" means in ascending order: the front of the queue is the smallest
 * element, and the rear of the queue is the largest.
 * 
 * e1 is less than or equal to e2 if and only if (e1.compareTo(e2) <= 0)
 *
 * You may not use loops (for, while, do, etc.) in this class. You must instead
 * use recursion.
 */
public class MergeSorter<T extends Comparable<T>> {
	/**
	 * Returns a new queue containing the elements from the input queue in
	 * sorted order.
	 * 
	 * Do not modify the input queue! Work on a copy of the input.
	 * 
	 * Implement this method recursively:
	 * 
	 * In the base case, return the sorted queue.
	 *
	 * Otherwise:
	 * 
	 * First, divide the input queue into two smaller output queues.
	 * 
	 * Then, recursively mergeSort each of these smaller queues.
	 * 
	 * Finally, return the result of merging these two queues.
	 * 
	 * @param queue
	 *            an input queue
	 * @return a sorted copy of the input queue
	 */
	public Queue<T> mergeSort(Queue<T> queue) {
		Queue<T> in = new Queue<T>(queue);
		if (in.size() < 2) {
			return in;
		} else {
			Queue<T> out1 = new Queue<T>();
			Queue<T> out2 = new Queue<T>();
			divide(in, out1, out2);
			out1 = mergeSort(out1);
			out2 = mergeSort(out2);
			return merge(out1, out2);
		}
	}

	/**
	 * Places elements from the input queue into the output queues, roughly half
	 * and half.
	 * 
	 * Implement this method recursively:
	 * 
	 * In the base case, there's nothing left to do.
	 * 
	 * Otherwise:
	 * 
	 * Make progress on moving elements from the input to the output.
	 * 
	 * Then make a recursive call to divide.
	 * 
	 * @param input
	 *            a queue
	 * @param output1
	 *            a queue into which about half of the elements in input should
	 *            go
	 * @param output2
	 *            a queue into which the other half of the elements in input
	 *            should go
	 */
	void divide(Queue<T> input, Queue<T> output1, Queue<T> output2) {
		if (!input.isEmpty()) {
			output1.enqueue(input.dequeue());
			if (!input.isEmpty())
				output2.enqueue(input.dequeue());
			if (!input.isEmpty())
				divide(input, output1, output2);
		}
	}

	/**
	 * Merges sorted input queues into an output queue in sorted order, and
	 * returns that queue.
	 * 
	 * Use mergeHelper to accomplish this goal.
	 * 
	 * @param input1
	 *            a sorted queue
	 * @param input2
	 *            a sorted queue
	 * @return a sorted queue consisting of all elements from input1 and input2
	 */
	Queue<T> merge(Queue<T> input1, Queue<T> input2) {
		Queue<T> sorted = new Queue<T>();
		mergeHelper(input1, input2, sorted);
		return sorted;
	}

	/**
	 * Merges the sorted input queues into the output queue in sorted order.
	 * 
	 * Implement this method recursively:
	 * 
	 * In the base case, there's nothing left to do.
	 * 
	 * Otherwise:
	 * 
	 * Make progress on moving elements from an input to the output.
	 * 
	 * Then make a recursive call to mergeHelper.
	 * 
	 * @param input1
	 *            a sorted queue
	 * @param input2
	 *            a sorted queue
	 * @param output
	 *            a sorted queue containing the accumulated progress so far
	 */
	void mergeHelper(Queue<T> input1, Queue<T> input2, Queue<T> output) {
		if (!input1.isEmpty() && !input2.isEmpty()) {
            T leftHead = input1.peek(), rightHead = input2.peek(),
                    leftTail = input1.peekTail(), rightTail = input2.peekTail();
			if (leftHead.compareTo(rightHead) < 0 && leftTail.compareTo(rightHead) < 0) {
                output.enqueue(input1); // If all of input1 is less than input2, enqueue all of input1
                input1.setEmpty();
            } else if (leftHead.compareTo(rightHead) < 0) {
				output.enqueue(input1.dequeue()); // If the tail is not less than input2, only enqueue the head
			} else if (leftHead.compareTo(rightHead) > 0 && leftHead.compareTo(rightTail) > 0) {
                output.enqueue(input2); // If all of input2 is less than input1, enqueue all of input2
                input2.setEmpty();
            } else {
				output.enqueue(input2.dequeue()); // If the tail is not less than input1, only enqueue the head
			}
		} else if (!input1.isEmpty()) {
			output.enqueue(input1); // If input2 is empty, enqueue input1
            input1.setEmpty();
		} else if (!input2.isEmpty()) {
			output.enqueue(input2); // If input1 is empty, enqueue input2
            input2.setEmpty();
		}
		if (!input1.isEmpty() || !input2.isEmpty())
			mergeHelper(input1, input2, output); // If either queue still has elements, call mergeHelper again
	}
}
