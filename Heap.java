import java.util.Comparator;
/**
 * 
 * @author Kane Scott 
 * Student ID:1298685
 *
 */
public class Heap {
	private Node[] heap;
	private int size;
	private final int FRONT = 0;
	private int maxSize;
	private Boolean CMark;

	/**
	 * Create a heap with given maxsize
	 * 
	 * @param maxsize
	 */
	public Heap(int maxsize) {
		maxSize = maxsize;
		size = 0;
		heap = new Node[maxsize];
		CMark = true;
	}

	/**
	 * 
	 * @return The current size of the heap
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Push a node onto the heap, then sort the heap.
	 * 
	 * @param n
	 */
	public void push(Node n) {
		heap[size] = n;
		sortHeap(FRONT);
		size++;
	}

	/**
	 * Removes the first node within heap for which the mark is equal to true.
	 * 
	 * @return The key of removed node
	 */
	public String pop() {
		Node n = heap[FRONT];
		int count = FRONT;
		while (n.getMark() == false && count < maxSize) {
			count++;
			n = heap[count];
		}
		if (count <= maxSize) {
			heap[count] = null;
			size--;
			sortHeap(FRONT);
			return n.getKey();
		} else {
			return null;
		}

	}

	/**
	 * 
	 * @return Returns the first node whose mark is true.
	 */
	public String peek() {
		Node n = heap[FRONT];
		int count = FRONT;
		if (size == 1 && n.getMark() == false) {
			System.out.println("One left and need new run");
		}
		while (n.getMark() == false && count < maxSize - 1) {
			count++;
			n = heap[count];
		}
		if (count <= maxSize) {
			return n.getKey();
		} else {
			return null;
		}
	}

	public class StringLengthComparator implements Comparator<String> {
		@Override
		public int compare(String x, String y) {
			// Assume neither string is null. Real code should
			// probably be more robust
			// You could also just return x.length() - y.length(),
			// which would be more efficient.
			if (x.length() < y.length()) {
				return -1;
			}
			if (x.length() > y.length()) {
				return 1;
			}
			return 0;
		}
	}

	/**
	 * Compare two given nodes based on string length
	 * 
	 * @param n1
	 * @param n2
	 * @return
	 */
	private int compare(Node n1, Node n2) {
		Comparator<String> comparator = new StringLengthComparator();
		if (n1.getMark() == n2.getMark())
			return comparator.compare(n1.getKey(), n2.getKey());
		else if (n1.getMark() == CMark)
			return 1;
		else
			return -1;
	}

	/**
	 * Set all nodes mark to true.
	 */
	public void unMarkAll() {
		for (Node n : heap) {
			if (n != null) {
				n.setMark(true);
				setCMark(true);
			}
		}
	}

	/**
	 * Sets the mark of first node whose mark is true.
	 * 
	 * @param mark
	 */
	public void setNodeMark(boolean mark) {
		int count = FRONT;
		boolean allFalse = true;
		boolean firstMark = true;
		for (int i = 0; i < size; i++) {
			if (heap[count] != null) {
				if (heap[count].getMark()) {
					if (firstMark) {
						firstMark = false;
						heap[count].setMark(mark);
					} else
						allFalse = false;
				}
			}
		}
		if (allFalse)
			setCMark(false);
	}

	/**
	 * Swap the two nodes at given indexes.
	 * 
	 * @param index1
	 * @param index2
	 */
	private void swap(int index1, int index2) {
		Node temp = heap[index1];
		heap[index1] = heap[index2];
		heap[index2] = temp;
	}

	/**
	 * Sorts the heap from given index to the end of the heap
	 * 
	 * @param i
	 *            Index to begin sorting heap
	 */
	private void sortHeap(int i) {
		while (i < maxSize - 1) {
			if (heap[i] == null && heap[i + 1] != null) {
				swap(i, i + 1);
			} else if (heap[i] != null && heap[i + 1] != null) {
				if (compare(heap[i], heap[i + 1]) > 0) {
					swap(i, i + 1);
				}
			}
			i++;
		}
	}

	/**
	 * Prints the content of the heap
	 */
	public void printHeap() {
		System.out.println("--");
		for (int i = 0; i < heap.length; i++) {
			if (heap[i] != null) {
				System.out.println(i + ": " + heap[i].getKey());
			}
		}
		System.out.println("--");
	}

	/**
	 * @return Returns true if heap is empty
	 */
	public boolean isEmpty() {
		if (size == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return Returns true if heap is full
	 */
	public boolean isFull() {
		if ((size) == maxSize)
			return true;
		return false;
	}

	/**
	 * Gets whether new run is needed.
	 * 
	 * @return
	 */
	public Boolean getCMark() {
		return CMark;
	}

	/**
	 * Set mark to signify whether new run is needed.
	 * 
	 * @param cMark
	 */
	public void setCMark(Boolean cMark) {
		CMark = cMark;
	}

}
