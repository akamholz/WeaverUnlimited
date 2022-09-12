import java.util.ArrayList;

/**
 * This class represents a sequence of Nodes that are used in finding the
 * optimal sequence for the game.
 * 
 * @author adamkamholz
 *
 */
public class Path implements Comparable<Path> {
	private Node start;
	private Node end;
	private ArrayList<String> wordSequence;
	private int numWords;

	/**
	 * Initial constructor for the Path object.
	 * 
	 * @param start the Node that will be the start of this Path object
	 */
	public Path(Node start) {
		this.start = start;
		this.end = start;
		this.wordSequence = new ArrayList<String>();
		wordSequence.add(start.toString());
		this.numWords = 1;
	}

	/**
	 * Constructor that makes a new Path object that adds the provided Node to the
	 * end of the provided Path.
	 * 
	 * @param path    the Path object that will be used to make the new Path object
	 * @param newNode the Node that will be added to the end of the provided Path
	 *                object.
	 */
	public Path(Path path, Node newNode) {
		this.start = path.start;
		this.end = newNode;
		this.wordSequence = new ArrayList<String>();
		for (String word : path.wordSequence) {
			wordSequence.add(word);
		}
		wordSequence.add(newNode.toString());
		this.numWords = path.numWords + 1;
	}

	/**
	 * Compares this Path object with the provided Path object. Creates the natural
	 * ordering for the priority queue used when finding the optimal sequence.
	 * 
	 * @param o the other Path object that will be compared to this Path object
	 * @return the difference between each Path object's number of words. i.e. a
	 *         negative number will be returned if this Path object has less words
	 *         than the other Path object, a positive number will be returned if
	 *         this Path object has more words than the other Path object, or 0 will
	 *         be returned if the two Path objects have the same amount of words.
	 */
	@Override
	public int compareTo(Path o) {
		return numWords - o.numWords;
	}

	/**
	 * Getter for numWords instance variable.
	 * 
	 * @return the number of words in this Path object.
	 */
	public int getNumWords() {
		return numWords;
	}

	/**
	 * Getter for the wordSequence instance variable.
	 * 
	 * @return an ArrayList of the words in this Path object (in order).
	 */
	public ArrayList<String> getWordSequence() {
		return wordSequence;
	}

	/**
	 * Getter for end instance variable.
	 * 
	 * @return the last Node in this Path object.
	 */
	public Node getEnd() {
		return end;
	}

	/**
	 * Getter for start instance variable.
	 * 
	 * @return the first Node in this Path object.
	 */
	public Node getStart() {
		return start;
	}
}
