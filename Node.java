/**
 * 
 * @author Kane Scott 
 * Student ID:1298685
 *
 */
public class Node {
	private String key;
	private Boolean mark;
	
	/**
	 * Create a node using a string, and a boolean to represent a mark. Used within MakeRuns.
	 * @param k
	 * @param m
	 */
	public Node (String k, Boolean m){
		key = k;
		mark = m;
	}
	
	/**
	 * Create a node using only a string. Used within MergeRuns.
	 * @param k
	 */
	public Node (String k){
		key = k;
		mark = true;
	}
	
	public String toString(){
		return "Key: "+key+"\tMark: "+mark;
	}
	
	public Boolean getMark() {
		return mark;
	}

	public void setMark(Boolean mark) {
		this.mark = mark;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
