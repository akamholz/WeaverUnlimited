import java.util.ArrayList;

/**
 * This class represents the nodes within the graph
 * 
 * @author adamkamholz
 *
 */
public class Node {
  private String word;
  private ArrayList<Node> edgesLeaving;

  /**
   * Constructor for the Node object.
   * 
   * @param word then word contained in this Node object
   */
  public Node(String word) {
    this.word = word;
    edgesLeaving = new ArrayList<Node>();
  }

  /**
   * Getter for word instance variable
   * 
   * @return the word contained in this Node object
   */
  public String getWord() {
    return word;
  }

  /**
   * Getter for edgesLeaving instance variable.
   * 
   * @return an ArrayList of the Edges leaving this Node object.
   */
  public ArrayList<Node> getEdgesLeaving() {
    return edgesLeaving;
  }

  /**
   * Adds the provided edge to the ArrayList of leaving edges.
   * 
   * @param edge the Edge object that will be added to the ArrayList of leaving Edges
   */
  public void addLeavingEdge(Node node) {
    edgesLeaving.add(node);
  }
  
  /**
   * Returns a String representation of the Node object.
   * @return the word of the Node.
   */
  public String toString () {
    return word;
  }
}
