/**
 * 
 * @author Kane Scott 
 * Student ID:1298685
 *
 */
public class FibList {

	private int fileAmt;
	private int runSize;
	private int[] fibNum;
	private int depth;

	public int getDepth() {
		return depth;
	}

	public int[] getRunLengths() {
		return runLengths;
	}

	private int[] runLengths;

	public FibList(int fileAmt, int runSize) {
		this.fileAmt = fileAmt;
		this.runSize = runSize;
		runLengths = new int[fileAmt];
		fibNum = new int[2];
		fibNum[0] = 1;
		fibNum[1] = 1;
		this.depth = 1;
		for (int i = 1; i < fileAmt; i++) {
			runLengths[i] = 1;
		}
		runLengths[0] = 0;
		while (findConfig() < this.runSize) {
			this.depth++;
			nextLayer();
		}
	}

	private int findConfig() {
		int t = 0;
		for (int i : runLengths)
			t += i;
		return t;
	}

	/**
	 * Find the next two fibonacci numbers.
	 */
	private void nextFib() {
		int t = fibNum[1];
		fibNum[1] = fibNum[0] + fibNum[1];
		fibNum[0] = t;
	}

	/**
	 * Find the previous two fibonacci numbers.
	 */
	public void lastFib() {
		int t = fibNum[0];
		fibNum[0] = fibNum[1] - fibNum[0];
		fibNum[1] = t;
	}

	public void nextLayer() {
		int i = 0;
		int z = findZero();

		nextFib();
		int fib = fibNum[0];

		while (i < fileAmt) {
			runLengths[i] += fib;
			i++;
		}
		if (z + 1 == fileAmt)
			runLengths[0] = 0;
		else
			runLengths[z + 1] = 0;
		depth++;
	}

	
	public int findZero() {
		int i = 0;
		while (true) {
			if (runLengths[i] == 0)
				return i;
			i++;
		}
	}

}
