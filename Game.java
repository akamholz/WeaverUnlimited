import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import java.util.Scanner;
import java.util.Iterator;
import java.io.File;

/**
 * This class will represent the actual Weaver game.
 * 
 * @author adamkamholz
 *
 */
public class Game {
	private static Graph graph = new Graph();
	private static int streak = 0; // keeps track of how many games the user won in a row
	private static int score = 0; // the score of the current game
	protected static ArrayList<Integer> scores = new ArrayList<Integer>(); // keeps track of the score
	// of each game the user completed
	protected static int time = 0; // the time it took to complete the current game
	protected static ArrayList<Integer> times = new ArrayList<Integer>(); // keeps track of the amount
	// of time each game took to complete (in seconds)
	private static int puzzlesSolved = 0; // keeps track of how many puzzles the user solved
	private static String startWord = "";
	private static String endWord = "";
	private static ArrayList<String> optimal = new ArrayList<String>();
	protected static Stack<String> words = new Stack<String>(); // holds all the words the user
																// already input into the game
	private static Scanner scanner = new Scanner(System.in); // scanner that gets user input
	private static boolean isWordGuessed = false; // true if the user has guessed the word in the
													// current // game, false otherwise
	private static int optimalsFound = 0; // keeps track of many times the user completed a puzzle
											// with an optimal solution
	private static String[] startEndWords = new String[1880]; // Array that contains all the possible start and end
																// words

	/**
	 * Calculates the average score of completed puzzles.
	 * 
	 * @return the average score of completed puzzles rounded to one decimal.
	 */
	protected static String averageScore() {
		int total = 0;
		for (int i = 0; i < scores.size(); i++) {
			total += scores.get(i);
		}
		double averageScore = total / ((double) scores.size());
		return "" + Math.round(averageScore * 10.0) / 10.0;
	}

	/**
	 * Calculates the average time of completed puzzles.
	 * 
	 * @return the average time it takes the user to complete a puzzle rounded to
	 *         the nearest whole number.
	 */
	protected static String averageTime() {
		if (times.size() == 0) {
			return "0:00"; // if there are no times in the list so far, the method returns "0:00"
		}
		int total = 0;
		for (int i = 0; i < times.size(); i++) {
			total += times.get(i);
		}
		int averageTime = total / times.size();
		int minutes = averageTime / 60;
		int seconds = averageTime % 60;
		// String secondsString = String.format("%.2g%n", seconds);
		if (seconds == 0) {
			return minutes + ":00"; // ensures the time gets two zeros at the end if the seconds is 0
		} else if (seconds < 10) {
			return minutes + ":0" + seconds; // ensures the time gets a zero before the single digit
												// second in the case that seconds is less than 10
		}
		return minutes + ":" + seconds;
	}

	/**
	 * Takes the time instance variable and creates a String that better represents
	 * the time it took to complete a puzzle. Ex: 71 seconds would create 1:11
	 * 
	 * @return a String that represents how long it took to complete the puzzle.
	 */
	protected static String formatTime() {
		int minutes = time / 60;
		int seconds = time % 60;
		if (seconds == 0) {
			return minutes + ":00"; // ensures the time gets two zeros at the end if the seconds is 0
		} else if (seconds < 10) {
			return minutes + ":0" + seconds; // ensures the time gets a zero before the single digit
												// second in the case that seconds is less than 10
		}
		return minutes + ":" + seconds;
	}

	/**
	 * Randomly selects the starting and ending word for the game and updates the
	 * optimal solution. This method does not return unless the two words have a
	 * solution that is greater than 3 words and less than 8 words.
	 */
	protected static void randomizeWords() {
		boolean areWordsValid = false;
		Random random = new Random();
		while (!areWordsValid) {
			// startWord = graph.getWords()[random.nextInt(graph.getWords().length)];
			// endWord = graph.getWords()[random.nextInt(graph.getWords().length)];
			startWord = startEndWords[random.nextInt(startEndWords.length)];
			endWord = startEndWords[random.nextInt(startEndWords.length)];
			optimal = graph.findOptimal(startWord, endWord);
			if (optimal != null && optimal.size() > 4 && optimal.size() < 7
					&& graph.getNodes().get(startWord).getEdgesLeaving().size() > 3
					&& graph.getNodes().get(endWord).getEdgesLeaving().size() > 3) {
				areWordsValid = true;
			}
		}
	}

	/**
	 * Prints the statistics of the gameplay to the screen.
	 */
	protected static void displayStats() {
		System.out.println("Score: " + score + "\nTime: " + formatTime() + "\nStreak: " + streak + "\nAverage Score: "
				+ averageScore() + "\nAverage Completion Time: " + averageTime() + "\nCompleted Puzzles: "
				+ puzzlesSolved + "\nOptimal Solutions: " + optimalsFound);
	}

	/**
	 * Getter for optimal instance variable
	 * 
	 * @return an ArrayList of the optimal solution to get from the start word to
	 *         the end word.
	 */
	protected static ArrayList<String> getOptimal() {
		return optimal;
	}

	/**
	 * Displays the words the user has guessed in a nice format.
	 */
	private static void displayWords() {
		System.out.print(startWord + " -> ");
		Iterator<String> iterator = words.iterator();
		while (iterator.hasNext()) {
			System.out.print(iterator.next() + " -> ");
		}
		if (!isWordGuessed) {
			System.out.print("_________ -> ");
		}
		System.out.println(endWord + "\n");
	}

	/**
	 * Determines if the given word is valid or not and prints the reason why. A
	 * word is valid if it is exactly four letters long, is in the array of valid
	 * words being used by this game, and has only one letter difference from the
	 * previously entered word.
	 * 
	 * @param word the word in all caps that is to be determined if it is valid or
	 *             not.
	 * @return
	 */
	protected static boolean isWordValid(String word) {
		if (word.length() != 4) {
			System.out.println("\nThe word you enter must be four letters");
			return false;
		}
		// if the user hasn't entered any words yet, the provided word needs to be
		// checked with the
		// starting word
		int differences = 0;
		if (words.size() == 0) {
			differences = letterDifferences(word, startWord);
		} else {
			differences = letterDifferences(word, words.peek());
		}
		if (differences > 1) {
			System.out.println("\nYou cannot change more than one letter");
			return false;
		} else if (differences == 0) {
			System.out.println("\nYou need to change at least one letter");
			return false;
		}
		for (int i = 0; i < graph.getWords().length; i++) {
			if (graph.getWords()[i].equals(word)) {
				return true; // if the word matches a word in the graph's array of valid words, the word is
								// valid
			}
		}
		// if the code reaches here, the word was not found in the graph's array of
		// valid words and is
		// therefore not a valid word
		System.out.println("\nThe word you entered is not in the word list");
		return false;
	}

	/**
	 * Compares the two provided words and finds how many letter differences there
	 * are. Ex: DOCK and DECK have one letter difference. This method assumes the
	 * provided words are both four letter words in all caps.
	 * 
	 * @param word1 the first word to look at.
	 * @param word2 the second word to look at.
	 * @return the amount of differences the two words have.
	 */
	protected static int letterDifferences(String word1, String word2) {
		int differences = 0;
		// Checks the character at each index and increments differences if they are not
		// the same
		for (int i = 0; i < word1.length(); i++) {
			String char1 = word1.substring(i, i + 1);
			String char2 = word2.substring(i, i + 1);
			if (!char1.equals(char2)) {
				differences++;
			}
		}
		return differences;
	}

	/**
	 * Runs one individual game.
	 */
	private static void oneGame() {
		// Resetting/initializing variables
		isWordGuessed = false;
		scanner = new Scanner(System.in);
		randomizeWords();
		boolean isGameOver = false;
		words.clear();
		// Opener to each individual game
		System.out.println("\nThe starting word is: " + startWord);
		System.out.println("The ending word is: " + endWord);

		long startTime = System.currentTimeMillis();
		// Loop the runs the gameplay
		while (!isGameOver) {
			boolean isEntryValid = false;
			String nextWord = "";
			// Loop that keeps running until the user has input a valid word
			while (!isEntryValid) {
				System.out.println(
						"Enter the next word, 'r' to remove your previous word, 's' to show your progress, or 'q' to quit: ");
				nextWord = scanner.next();
				nextWord = nextWord.toUpperCase();
				if (nextWord.equals("R")) {
					if (!words.isEmpty()) {
						words.pop();
					} else {
						System.out.print("\nYou have no words to remove");
					}
					System.out.println("\nYour progress: ");
					displayWords();
					continue;
				}
				if (nextWord.equals("Q")) {
					break;
				}
				if (nextWord.equals("S")) {
					System.out.println("\nYour progress: ");
					displayWords();
					continue;
				}
				isEntryValid = isWordValid(nextWord);
			}
			if (nextWord.equals(endWord)) {
				isWordGuessed = true;
				break;
			} else if (nextWord.equals("Q")) {
				break;
			}
			words.add(nextWord);
			System.out.println("Your progress: ");
			displayWords();
		}
		long endTime = System.currentTimeMillis();
		time = (int) ((endTime - startTime) / 1000);
		boolean wasCompletedOptimally = false; // true if the user found an optimal solution, false
												// otherwise
		// Updates appropriate stats and prints a nice message if the user completed the
		// game
		if (isWordGuessed) {
			times.add(time);
			score = words.size() + 1;
			if ((score + 1) == optimal.size()) {
				wasCompletedOptimally = true;
			}
			scores.add(score);
			streak++;
			puzzlesSolved++;
			System.out.println("\nGood job!");
			if (wasCompletedOptimally) {
				System.out.println("You found the optimal solution!");
				optimalsFound++;
			}
			System.out.println("\nYour solution: ");
		} else {
			// Updates appropriate stats and displays a neutral message if the user did not
			// complete the
			// game
			streak = 0;
			System.out.println("\nYour attempt: ");
		}

		// End message to each game
		displayWords();
		if (!wasCompletedOptimally) {
			System.out.println("Optimal solution: ");
			for (int i = 0; i < optimal.size(); i++) {
				System.out.print(optimal.get(i));
				if (i != optimal.size() - 1) {
					System.out.print(" -> ");
				}
			}
			System.out.println("\n");
		}
		displayStats();
	}

	/**
	 * This method runs the actual game.
	 */
	public static void run() {
		initializeStartEndWords();
		// Opening message
		System.out.println("Welcome to Weaver Unlimited!");
		System.out.println("============================");

		boolean keepPlaying = true;
		scanner = new Scanner(System.in);

		// Loop that runs as long as user would like to keep playing
		while (keepPlaying) {
			oneGame();
			String continueString = "";
			boolean isContinueStringValid = false;
			// Gets the user input on if they would like to continue the game
			while (!isContinueStringValid) { // Loop ensures a valid string is input ('y' or 'n')
				System.out.println("Would you like to keep playing (y/n): ");
				continueString = scanner.next();
				continueString = continueString.toUpperCase();
				if (continueString.equals("Y") || continueString.equals("N")) {
					isContinueStringValid = true;
				}
			}
			if (continueString.equals("N")) {
				keepPlaying = false;
			}
		}
		// Exiting message
		System.out.println("\nThanks for playing Weaver Unlimited!");
	}

	/**
	 * This method initializes all the words that could be used as the first or last
	 * word in the sequence by reading through a file called startEndWords.txt
	 */
	private static void initializeStartEndWords() {
		File startEndWordsFile = new File("startEndWords.txt");
		Scanner scanner = new Scanner(System.in);
		try {
			scanner = new Scanner(startEndWordsFile);
		} catch (Exception e) {
			System.out.println("Could not load startEndWords.txt");
		}
		int index = 0;
		while (scanner.hasNext()) {
			startEndWords[index] = scanner.next();
			index++;
		}
		scanner.close();
	}
}
