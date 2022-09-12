import java.util.Hashtable;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * This class represents the graph of words that are connected to other words with a one-letter
 * difference.
 * 
 * @author adamkamholz
 *
 */
public class Graph {
  private Hashtable<String, Node> nodes; // hashtable that contains all the Nodes with all the valid
  // words of the game, the word will be used as the key that maps to the Node (value)
  private String[] words = new String[5454]; // Array that stores all words

  /**
   * Creates the graph and accurately maps each word to their connected words.
   * 
   * @param wordsFile the name of the file that contains all the words.
   */
  public Graph() {
    nodes = new Hashtable<String, Node>();

    // Putting all the words from the file into Nodes in the hashtable
    File file = new File("words.txt");
    Scanner scanner = new Scanner(System.in);
    try {
      scanner = new Scanner(file);
    } catch (Exception e) {
      System.out.println("Could not load words.txt");
    }
    int index = 0;
    while (scanner.hasNext()) {
      String next = scanner.next();
      words[index] = next;
      index ++;
      nodes.put(next, new Node(next));
    }
    // Iterates through each word in the hash table and connects to other valid words that have a
    // one letter difference
    nodes.forEach((key, value) -> {
      String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
          "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
      String wordToCheck = "";
      for (int i = 0; i < 4; i++) {
        for (int l = 0; l < letters.length; l++) {
          // Constructing the word to check
          if (i != 3) {
            wordToCheck = key.substring(0, i) + letters[l] + key.substring(i + 1);
          } else {
            wordToCheck = key.substring(0, 3) + letters[l];
          }

          if (!wordToCheck.equals(key) && nodes.containsKey(wordToCheck)) {
            value.addLeavingEdge(nodes.get(wordToCheck)); // adds the Node to the
            // edgesLeaving if the word to check is a valid word that is different from the current
            // word and has a one letter difference from the current word
          }
        }
      }
    });
  }

  /**
   * Getter for nodes instance variable.
   * 
   * @return the hashtable of Node objects containing all the valid words to be used for the game.
   */
  public Hashtable<String, Node> getNodes() {
    return nodes;
  }

  /**
   * Uses dijkstra's algorithm to determine the shortest path to get from start to end. Helper
   * method for findOptimal().
   * 
   * @param start the starting word
   * @param end   the ending word
   * @return the Path object that contains the optimal path to get from start to end
   * @throws Exception when no path from the provided starting and ending words can be found.
   */
  private Path dijkstraOptimal(String start, String end) throws Exception {
    PriorityQueue<Path> pq = new PriorityQueue<Path>();
    pq.add(new Path(nodes.get(start)));
    ArrayList<Node> visitedNodes = new ArrayList<Node>(); // ArrayList to represented Nodes that
    // have already been visited
    while (!pq.isEmpty()) {
      Path currentPath = pq.remove();
      Node currentNode = currentPath.getEnd();
      if (currentNode.toString().equals(end)) {
        return currentPath; // if the ending node of the current path matches the ending word, then
                            // the shortest path has been found
      }
      if (visitedNodes.contains(currentNode)) {
        continue; // if the currentNode has already been visited, its edges are not added to the
                  // priority queue
      }
      visitedNodes.add(currentNode);
      for (Node nextNode : currentNode.getEdgesLeaving()) {
        if (visitedNodes.contains(nextNode)) {
          continue; // if the next potential Node has already been visited, that path is not added
                    // to the priority queue
        }
        pq.add(new Path(currentPath, nextNode));
      }
    }
    throw new Exception("No path from the provided starting and ending words was found/"); // if the
    // code reaches here, then there is no path from the starting word to the ending word
  }

  /**
   * Determines the optimal to get from the starting word to the ending word.
   * 
   * @param start the starting word.
   * @param end   the ending word.
   * @return an ArrayList of Strings that contain the words (in order) that make up the optimal or
   *         null if there is no path that can be made from the starting word to the ending word.
   */
  public ArrayList<String> findOptimal(String start, String end) {
    try {
      return dijkstraOptimal(start, end).getWordSequence();
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Getter for words instance variable.
   * 
   * @return an array of all the valid words.
   */
  public String[] getWords() {
    return words;
  }
}
