import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
/**
 * 
 * @author Kane Scott 
 * Student ID:1298685
 *
 */
public class MakeRuns {
	File f;
	int size;
	String filename;
	
	public static void main(String[] args) {
		MakeRuns testRuns = new MakeRuns(Integer.parseInt(args[0]), args[1]);
	}

	public MakeRuns(int size, String filename) {
		this.size = size;
		this.filename = filename;
		doHeaps();
	}

	public void doHeaps() {
		BufferedReader br;
		ArrayList<String> runList = new ArrayList<String>();
		Comparator<String> comparator = new StringLengthComparator();
		boolean finished = false;
		try {
			// Create writer for .runs file
			PrintWriter writer = new PrintWriter((filename + ".runs"), "UTF-8");
			// Open reader for specified file
			br = new BufferedReader(new FileReader(filename));
			String line = br.readLine();
			// Create a Heap with specified size
			Heap heap = new Heap(size);
			while (finished == false) {
				// If the heap is not full and there are more strings to be
				// read, push the next string onto the heap.
				if (!heap.isFull() && line != null) {
					heap.printHeap();
					heap.push(new Node(line, true));
					line = br.readLine();
					System.out.println(line);
				}
				// If heap is marked, it indicates that a new run is needed.
				if (heap.getCMark() == false) {
					runList.add(null);
					heap.unMarkAll();
				}
				// If heap is full, check first non-marked node within the heap.
				if (heap.isFull()) {
					String s = heap.peek();
					// If runList is empty, or last string is null(indicating
					// new run), pop node from heap and add to runList.
					if (runList.size() == 0 || runList.get(runList.size() - 1) == null) {
						runList.add(heap.pop());
					}
					// If node is greater than last node in current run, pop
					// node from heap and add to runList.
					else if (comparator.compare(runList.get(runList.size() - 1), s) <= 0) {
						runList.add(heap.pop());
					}
					// Otherwise the node is larger than last node in current
					// run, and is marked for next run.
					else {
						heap.setNodeMark(false);
					}
				}
				// If heap isn't full nor empty, but file has been completely
				// read. Continue to create runs with last nodes within the
				// heap.
				else if (line == null && !heap.isEmpty()) {
					String s = heap.peek();
					// If runList is empty, or last string is null(indicating
					// new run), pop node from heap and add to runList.
					if (runList.size() == 0 || runList.get(runList.size() - 1) == null) {
						runList.add(heap.pop());
					}
					// If node is greater than last node in current run, pop
					// node from heap and add to runList.
					else if (comparator.compare(runList.get(runList.size() - 1), s) <= 0) {
						runList.add(heap.pop());
					} 
					// Otherwise the node is larger than last node in current
					// run, and is marked for next run.
					else {
						heap.setNodeMark(false);
					}
				} 
				//This indicates that the heap is empty, and file has been completely read. We have finished creating runs.
				else if (line == null) {
					finished = true;
				}
			}
			//Print runs into .runs file. Each end of run is signified by EOL line.
			for (String out : runList) {
				if (out == null) {
					writer.println("EOL");
				} else {
					writer.println(out);
				}
			}
			//Write final EOL and close writer.
			writer.println("EOL");
			writer.close();
		} catch (IOException e) {

		} finally {
		}
	}
	
	public class StringLengthComparator implements Comparator<String> {
		@Override
		public int compare(String x, String y) {
			if (x.length() < y.length()) {
				return -1;
			}
			if (x.length() > y.length()) {
				return 1;
			}
			return 0;
		}
	}

}
