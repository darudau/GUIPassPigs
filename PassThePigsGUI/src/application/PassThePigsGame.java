package application;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

/**
 * The game model for Pass The Pigs. This model allows the player to interact
 * with it through the playerAction function.
 * 
 *
 * TODO Type Rules here
 * 
 * @author Douglas Rudau
 * @version 0.1 April 20, 2019
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

	private String pig1PrevPos;

	private String pig2PrevPos;

	/**
	 * Array of integer scores for each possible pig position for a single pig
	 * Values for special positions are determined in the rollPigs() function.
	 */
	private int[] pigPositionScores;

	/** Constants used to represent states for the pigs */
	// score values are here to allow for modification
	private final int PLAIN_SIDE = 0;
	private final int DOTTED_SIDE = 1;
	private final int RAZORBACK = 2;
	private final int TROTTER = 3;
	private final int SNOUTER = 4;
	private final int LEANING_JOWLER = 5;
	private final int PIG_OUT = -1;
	private final int DOUBLE_RAZORBACK = 20;
	private final int DOUBLE_TROTTER = 20;
	private final int DOUBLE_SNOUTER = 40;
	private final int DOUBLE_LEANING_JOWLER = 60;
	private final int OINKER = -2;
	private final int DOUBLE_SIDER = 2;

	/** Possibility of an Oinker */
	// higher number means lower chance of an oinker.
	private final int CHANCE_OF_OINKER = 100;

	/** Constants used to represent player actions */
	private final int ROLL = 1;
	private final int PASS_TO_NEXT = 2;

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
		pigPositionScores[PLAIN_SIDE] = 1;
		pigPositionScores[DOTTED_SIDE] = 1;
		pigPositionScores[RAZORBACK] = 5;
		pigPositionScores[TROTTER] = 5;
		pigPositionScores[SNOUTER] = 10;
		pigPositionScores[LEANING_JOWLER] = 15;

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
			if (scoreboard.get(i) >= 100)
			{
				winner = i;
				return true;
			}
		}
		return false;
	}

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
			// Pigs landed in a position were they touched, which is an Oinker.
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
				// double plain side
				return DOUBLE_SIDER;
			}
			else if (pig1Pos == DOTTED_SIDE)
			{
				// double dotted side
				return DOUBLE_SIDER;
			}
			else if (pig1Pos == RAZORBACK)
			{
				// double razorback
				return DOUBLE_RAZORBACK;
			}
			else if (pig1Pos == TROTTER)
			{
				// double trotter
				return DOUBLE_TROTTER;
			}
			else if (pig1Pos == SNOUTER)
			{
				// double snouter
				return DOUBLE_SNOUTER;
			}
			else if (pig1Pos == LEANING_JOWLER)
			{
				// double leaning jowler
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
