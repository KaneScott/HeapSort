import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
/**
 * 
 * @author Kane Scott 
 * Student ID:1298685
 *
 */
public class MergeRuns {
	private int size;
	private String filename;
	private BufferedReader br;
	private ArrayList<ArrayList<String>> runList;
	private int[] runLengths;
	private PrintWriter writer;
	private int outputFile;
	
	public static void main(String[] args) {
		try {
			MergeRuns mergeRuns = new MergeRuns(Integer.parseInt(args[0]), args[1]);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Uses external polyphase sorting to sort a given .runs file by string
	 * length.
	 * 
	 * @param size
	 *            Specifies amount of temp files
	 * @param filename
	 *            Specifies the .runs file created previously by execution of
	 *            the MakeRuns class.
	 * @throws IOException
	 */
	public MergeRuns(int size, String filename) throws IOException {
		this.size = size;
		this.filename = filename;
		br = new BufferedReader(new FileReader(filename));
		distributeRuns();
		mergeSort();
	}

	/**
	 * Distributes the runs between temporary files. The amount of temporary
	 * files is specified by size during class creation.
	 * 
	 * @throws IOException
	 */
	public void distributeRuns() throws IOException {
		outputFile = -1;
		String line = br.readLine();
		runList = new ArrayList<ArrayList<String>>();
		ArrayList<String> currList = new ArrayList<String>();
		// Read lines of the .runs file until all lines have been read.
		while (line != null) {
			// EOF signifies end of run
			if (line.equals("EOL")) {
				// Add the run to the runList
				runList.add(currList);
				// Create an empty list for the next run
				currList = new ArrayList<String>();
			} else {
				// Add string to current run
				currList.add(line);
			}
			line = br.readLine();
		}
		System.err.println("Total amount of runs: "+runList.size());
		// Create a fibonacci sequence for run distribution
		FibList fib = new FibList(size, runList.size());
		runLengths = fib.getRunLengths();
		int j = 0;
		// Create temp files for runs based on the created fibonacci sequence
		for (int i = 0; i < runLengths.length; i++) {
			if (runLengths[i] > 0) {
				writer = new PrintWriter((filename + "-temp" + i), "UTF-8");
				int count = 0;
				for (int z = 0; z < runLengths[i]; z++) {
					if (j < runList.size()) {
						for (String s : runList.get(j)) {
							writer.println(s);
						}
						writer.println("EOF");
						j++;
						count++;
					} else {
						runLengths[i] = count;
					}
				}
				writer.close();
			} else {
				writer = new PrintWriter((filename + "-temp" + i), "UTF-8");
				writer.close();
				outputFile = i;
			}
		}
	}

	/**
	 * Sorts the temporary files once runs have been distributed via the
	 * distributeRuns() method.
	 * 
	 * @throws IOException
	 */
	private void mergeSort() throws IOException {
		int lastOutput = -1;
		// Continue to sort runs until only one run remains. This indicates a
		// fully sorted file.
		System.err.println("Run distribution between temporary files");
		for(int i = 0; i < runLengths.length; i++){
			System.err.println("File "+i+": "+runLengths[i]);
		}
		int outputCount = 0;
		while (totalRuns(runLengths) > 1) {
			// Iterate through each of the temporary files.
			for (int i = 0; i < runLengths.length; i++) {
				// Check that temporary file is not the specified output file
				// and that it contains runs to be sorted.
				if (i != outputFile && runLengths[i] > 0) {
					// Find the next temporary file to merge runs with
					for (int j = i + 1; j < runLengths.length; j++) {
						// Check that temporary file is not the specified output
						// file
						// and that it contains runs to be sorted.
						if (j != i && j != outputFile && runLengths[j] > 0) {
							int maxCount = 0;
							int otherFile = -1;
							int newOutput = -1;
							ArrayList<ArrayList<String>> runsSmall;
							ArrayList<ArrayList<String>> runsBig;
							// Check which file has the smallest amount of runs.
							// Set newOutput to the smaller file, and otherFile
							// to the larger file. Set maxCount to the number of
							// runs within the smaller file, this indicates the
							// number of runs to be merged during current loop.
							if (runLengths[i] > runLengths[j]) {
								maxCount = runLengths[j];
								newOutput = j;
								otherFile = i;
								runsSmall = getRuns(filename + "-temp" + j);
								runsBig = getRuns(filename + "-temp" + i);
							} else {
								maxCount = runLengths[i];
								newOutput = i;
								otherFile = j;
								runsSmall = getRuns(filename + "-temp" + i);
								runsBig = getRuns(filename + "-temp" + j);
							}
							// Open writer to output file
							outputCount++;
							writer = new PrintWriter((filename + "-temp" + outputFile), "UTF-8");
							// Merge runs between two files via Heap
							for (int z = 0; z < maxCount; z++) {
								Heap sorted = new Heap(runsSmall.get(z).size() + runsBig.get(z).size());
								for (String s : runsSmall.get(z))
									sorted.push(new Node(s));
								for (String s : runsBig.get(z))
									sorted.push(new Node(s));
								// Write sorted runs to output file
								while (!sorted.isEmpty()) {
									writer.println(sorted.pop());
								}
								// Indicate end of run via EOF string
								writer.println("EOF");
							}
							// Close writer to output file and decrement run
							// size of both files.
							writer.close();
							runLengths[outputFile] = maxCount;
							runLengths[otherFile] -= maxCount;
							lastOutput = outputFile;
							// Set new output file to the now empty smaller
							// file.
							outputFile = newOutput;
							// Remove sorted runs from the larger file.
							writer = new PrintWriter((filename + "-temp" + otherFile), "UTF-8");
							for (int z = maxCount; z < runsBig.size(); z++) {
								for (String s : runsBig.get(z)) {
									writer.println(s);
								}
								writer.println("EOF");
							}
							writer.close();
							// Empty file for the smaller file
							writer = new PrintWriter((filename + "-temp" + outputFile), "UTF-8");
							runLengths[outputFile] = 0;
							writer.close();
							break;
						}
					}
				}
			}
		}
		// Runs are now sorted, print the last output file to a .sorted file
		File f = new File(filename + "-temp" + lastOutput);
		String renamed = filename.substring(0, filename.length() - 5);
		f.renameTo(new File(renamed + ".sorted"));
		// Delete temporary files.
		for (int i = 0; i < runLengths.length; i++) {
			File tempFile = new File(filename + "-temp" + i);
			tempFile.delete();
		}
		System.err.println("Amount of times temporary output files opened: "+outputCount);
	}

	/**
	 * Calculate the totalRuns left within all the files
	 * 
	 * @param runList
	 * @return
	 */
	private int totalRuns(int[] runList) {
		int total = 0;
		for (int i : runList)
			total += i;
		return total;
	}

	/**
	 * Return a list of runs given specified filename(a .temp file). Where each
	 * run is a list of strings.
	 * 
	 * @param filename
	 *            .temp file to read runs from
	 * @return A list of runs. A run is a list of strings.
	 * @throws IOException
	 */
	private ArrayList<ArrayList<String>> getRuns(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line = br.readLine();
		ArrayList<ArrayList<String>> returnList = new ArrayList<ArrayList<String>>();
		ArrayList<String> currList = new ArrayList<String>();
		while (line != null) {
			if (line.equals("EOF")) {
				returnList.add(currList);
				currList = new ArrayList<String>();
			} else {
				currList.add(line);
			}
			line = br.readLine();
		}
		br.close();
		return returnList;
	}

}
