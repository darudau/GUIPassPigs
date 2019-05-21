package application;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

/**
 * The game model for Pass The Pigs. This model allows the player to interact
 * with it through the playerAction function.
 * <p>
 * The first player to score 100 or more points wins the game. Each player will
 * roll the pigs to attempt to gain points. The player may roll the pigs as many
 * times as they would like, however with each roll the points you lose
 * increases. The player’s turn is done if they roll a Pig Out (where the pigs
 * land on opposite side), an Oinker (where the pigs land touching), or the
 * player decides to pass the pigs to the next player and record their total
 * number of points. If the player rolls an Oinker, their score for the game is
 * reset to zero. Players can pass the pigs without rolling them, though this is
 * not recommended as no points will be gained. 
 * 
 * @author Douglas Rudau
 * @version April 20, 2019
 *
 */
public class PassThePigsGame extends Observable
{
	/** The total number players in the game */
	private int numPlayers;

	/** ArrayList that stores the scores for each player */
	private ArrayList<Integer> scoreboard;

	/** Represents the current player */
	private int playerTurn;

	/** Pig 1 in the game */
	private Pig pig1;

	/** Pig 2 in the game */
	private Pig pig2;

	/** The Winner of the game */
	private int winner;

	/** Previous position for each of the pigs */
	private String pig1PrevPos;
	private String pig2PrevPos;

	/** the minimum number of points needed to win */
	public final static int POINTS_TO_WIN = 100;

	/**
	 * Array of integer scores for each possible pig position for a single pig
	 * Values for special positions are determined in the rollPigs() function.
	 */
	private int[] pigPositionScores;

	/** Constants used to represent states for the pigs */
	public final static int PLAIN_SIDE = 0;
	public final static int DOTTED_SIDE = 1;
	public final static int RAZORBACK = 2;
	public final static int TROTTER = 3;
	public final static int SNOUTER = 4;
	public final static int LEANING_JOWLER = 5;
	public final static int PIG_OUT = -1;

	/** Constants that are the scores for each pig's roll */
	public final static int SIDE_SCORE = 1;
	public final static int RAZORBACK_SCORE = 5;
	public final static int TROTTER_SCORE = 5;
	public final static int SNOUTER_SCORE = 10;
	public final static int LEANING_JOWLER_SCORE = 15;

	/** int representations for the Pigs in pairs */
	// these are also the scores for these rolls
	public final static int DOUBLE_RAZORBACK = 20;
	public final static int DOUBLE_TROTTER = 20;
	public final static int DOUBLE_SNOUTER = 40;
	public final static int DOUBLE_LEANING_JOWLER = 60;
	public final static int OINKER = -2;
	public final static int DOUBLE_SIDER = 2;

	/** Possibility of an Oinker */
	// higher number means lower chance of an oinker.
	public final static int CHANCE_OF_OINKER = 100;

	/** Constants used to represent player actions */
	public final static int ROLL = 1;
	public final static int PASS_TO_NEXT = 2;

	/** Score for the current player's turn */
	private int turnScore;

	/**
	 * Constructor
	 * 
	 * @param numPlayers
	 *            the number of players in the game, must be greater than or
	 *            equal to one
	 */
	public PassThePigsGame(int newNumPlayers)
	{
		numPlayers = newNumPlayers;

		// initialize the scoreboard
		scoreboard = new ArrayList<Integer>();
		for (int i = 0; i < numPlayers; i++)
		{
			scoreboard.add(0);
		}

		// initialize the pigs
		pig1 = new Pig();
		pig2 = new Pig();

		pigPositionScores = new int[6];
		pigPositionScores[PLAIN_SIDE] = SIDE_SCORE;
		pigPositionScores[DOTTED_SIDE] = SIDE_SCORE;
		pigPositionScores[RAZORBACK] = RAZORBACK_SCORE;
		pigPositionScores[TROTTER] = TROTTER_SCORE;
		pigPositionScores[SNOUTER] = SNOUTER_SCORE;
		pigPositionScores[LEANING_JOWLER] = LEANING_JOWLER_SCORE;

		// -1 means the game is not over yet
		winner = -1;

	}

	/**
	 * Determines if the game is over. The game is over if a player has reached
	 * a score greater than or equal to 100. The winner attribute for the game
	 * is set after to that player's number
	 * 
	 * @return true if the the game is over, false if the game is not over.
	 */
	public boolean isGameOver()
	{
		for (int i = 0; i < scoreboard.size(); i++)
		{
			if (scoreboard.get(i) >= POINTS_TO_WIN)
			{
				winner = i;
				return true;
			}
		}
		return false;
	}

	/**
	 * Method to get the winner of the game.
	 * 
	 * @return the player that won the game's ID number, -1 if the game is not
	 *         over
	 */
	public int getWinner()
	{
		return winner;
	}

	/**
	 * Rolls the both pigs and determines the score for the pigs The total score
	 * for the roll is returned.
	 * 
	 * @return total score for the roll, return -1 if the player loses all
	 *         current points
	 */
	private int rollPigs()
	{
		if (isOinker())
		{
			return OINKER;
		}

		int pig1Pos = pig1.rollPig();
		int pig2Pos = pig2.rollPig();

		pig1PrevPos = pig1.intPigPositionToString(pig1Pos);
		pig2PrevPos = pig2.intPigPositionToString(pig2Pos);

		if (pig1Pos == pig2Pos)
		{
			// special cases were the pigs are in the same position.
			if (pig1Pos == PLAIN_SIDE)
			{
				return DOUBLE_SIDER;
			}
			else if (pig1Pos == DOTTED_SIDE)
			{
				return DOUBLE_SIDER;
			}
			else if (pig1Pos == RAZORBACK)
			{
				return DOUBLE_RAZORBACK;
			}
			else if (pig1Pos == TROTTER)
			{
				return DOUBLE_TROTTER;
			}
			else if (pig1Pos == SNOUTER)
			{
				return DOUBLE_SNOUTER;
			}
			else if (pig1Pos == LEANING_JOWLER)
			{
				return DOUBLE_LEANING_JOWLER;
			}
		}

		// check for pig out
		if ((pig1Pos == PLAIN_SIDE && pig2Pos == DOTTED_SIDE)
				|| (pig1Pos == DOTTED_SIDE && pig2Pos == PLAIN_SIDE))
		{
			// the pigs landed on opposite sides (one dotted and one plain), the
			// player's turn is up and
			// all points that were not banked are lost.
			return PIG_OUT;
		}

		// roll score is equal to the sum of the pigs value.
		return pigPositionScores[pig1Pos] + pigPositionScores[pig2Pos];
	}

	/**
	 * Function to determine if the pigs landed touching each other, which is an
	 * oinker. When this occurs the player loses all of their points. An oinker
	 * is a 1/CHANGE_OF_OINKER chance per roll. This number is arbitrary.
	 * 
	 * @return true if the roll was an Oinker, false if the roll was not an
	 *         Oinker.
	 */
	private boolean isOinker()
	{
		Random rand = new Random();
		int randInt = rand.nextInt(CHANCE_OF_OINKER);

		if (randInt == 0)
		{
			return true;
		}

		return false;
	}

	/**
	 * Function to allow the player to make a move. This function takes in an
	 * integer that represents the desired action by the player. The player may
	 * either roll the pigs by passing in a 1 or pass the pigs and bank their
	 * points by passing in a two. An IllegalArgument Exception is thrown if an
	 * invalid action is entered.
	 * <p>
	 * True is returned by the function if it is still the current player's turn
	 * and false is returned if it is now the next player's turn.
	 * 
	 * @param action
	 *            integer to select whether or not to roll or pass the pigs
	 * @return true if it is the current players turn still, and false if it is
	 *         the next players turn
	 * @throws IllegalArgumentException
	 *             if an invalid option is entered.
	 */
	public String playerAction(int action) throws IllegalArgumentException
	{
		if (action == ROLL)
		{
			int rollScore = rollPigs();
			if (rollScore == PIG_OUT)
			{
				// pigs landed on opposite sides.
				// no score added for turn
				turnScore = 0; // reset turn score;
				playerTurn = (playerTurn + 1) % numPlayers; // move to next
															// player
				this.setChanged();
				this.notifyObservers();
				return "Pig Out, loose points for turn.  Passing Pigs to Player "
						+ playerTurn + ".  \n";
			}
			else if (rollScore == OINKER)
			{
				// pigs landed touching
				// player's total score is reset to zero.
				// player's turn is over
				scoreboard.set(playerTurn, 0); // rest score to zero.
				turnScore = 0; // reset turn score
				playerTurn = (playerTurn + 1) % numPlayers; // move to next //
															// player
				this.setChanged();
				this.notifyObservers();
				return "Oinker, loose all points for game.  Passing Pigs to Player "
						+ playerTurn + ".  \n";
			}

			this.setChanged();
			this.notifyObservers();
			turnScore += rollScore;
			return rollToString();
		}
		else if (action == PASS_TO_NEXT)
		{
			// pass pigs to next player and change the turn to the next player

			int previousScore = scoreboard.get(playerTurn);

			scoreboard.set(playerTurn, previousScore + turnScore);
			turnScore = 0; // reset turn score.
			// provides circular queue behavior for the players
			playerTurn = (playerTurn + 1) % numPlayers;
			this.setChanged();
			this.notifyObservers();
			return "Passing pigs to player " + playerTurn + ". \n";
		}
		else
		{
			throw new IllegalArgumentException(
					"Invalid choice:  Please use " + ROLL + " to roll the pigs,\nor "
							+ PASS_TO_NEXT + " to bank points and pass the pigs");
		}
	}

	/**
	 * Converts the roll of a pig to a string
	 * 
	 * @return string describing the roll.
	 */
	private String rollToString()
	{
		return pig1PrevPos + " and " + pig2PrevPos;
	}

	/**
	 * Function to start a new game. Resets the scores to zero.
	 */
	public void newGame()
	{
		// reset scoreboard
		for (int i = 0; i < scoreboard.size(); i++)
		{
			scoreboard.set(i, 0);
		}

		turnScore = 0;
	}

	/**
	 * Generates a string containing the scoreboard
	 * 
	 * @return scorebaord representation as a string.
	 */
	public String scoreboardToString()
	{
		String string = "Score Board\n";

		for (int i = 0; i < scoreboard.size(); i++)
		{
			string += "Player " + i + ": " + scoreboard.get(i) + "\n";
		}

		return string;
	}

	/**
	 * Gets the number of players in the game
	 * 
	 * @return the total number of players in the game.
	 */
	public int getNumPlayers()
	{
		return numPlayers;
	}

	/**
	 * Gets the current players turn
	 * 
	 * @return the number associated with the current player.
	 */
	public int getPlayerTurn()
	{
		return playerTurn;
	}

	/**
	 * Gets the current score for the turn
	 * 
	 * @return the score for the current turn
	 */
	public int getTurnScore()
	{
		return turnScore;
	}

	/**
	 * Gets the specified player's score
	 * 
	 * @param playerNumber
	 *            specified player's number
	 * @return score for the specified player.
	 */
	public int getPlayerScore(int playerNumber)
	{
		return scoreboard.get(playerNumber);
	}
}
