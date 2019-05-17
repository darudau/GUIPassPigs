package application;

/**
 * PassPigsPlayer is the class that contians the AI agent that can play the
 * game, PassThePigs. The PassPigsPlayer can make informed decisions about what
 * to do within the game and send it's selected control commands back to the
 * model of the game.
 * 
 * @author Douglas Rudau
 * @version May 17, 2019
 *
 */
public class PassPigsPlayer
{
	/** String that contians the AI's logic for the current move */
	private String aiReasoning;

	/** Number of moves the AI Agent has made this turn */
	private int numMoves;

	/**
	 * The expected value of rolling the pigs once, does not factor in points
	 * accumulated for the turn and points accumulated overall
	 */
	private double baseExpectedValue;

	/**
	 * Constructor
	 * 
	 */
	public PassPigsPlayer()
	{
		aiReasoning = "";
		numMoves = 0;
		baseExpectedValue = calculateBaseExpectedValue();
	}

	/**
	 * Calculates the Base Expected Value that does not change as the player's
	 * turn and overall score changes. This is done once in the constuctor since
	 * this part of the expected value does not change with the number of points
	 * accumulated for the turn or overall game.
	 * 
	 * @return base expected value
	 */
	private double calculateBaseExpectedValue()
	{
		double ev = 0;

		ev += 0.35 * 0.22
				* (PassThePigsGame.DOTTED_SIDE + PassThePigsGame.RAZORBACK);
		ev += 0.35 * 0.09 * (PassThePigsGame.DOTTED_SIDE + PassThePigsGame.TROTTER);
		ev += 0.35 * 0.03 * (PassThePigsGame.DOTTED_SIDE + PassThePigsGame.SNOUTER);
		ev += 0.35 * 0.01
				* (PassThePigsGame.DOTTED_SIDE + PassThePigsGame.LEANING_JOWLER);

		ev += 0.30 * 0.22
				* (PassThePigsGame.DOTTED_SIDE + PassThePigsGame.RAZORBACK);
		ev += 0.30 * 0.09 * (PassThePigsGame.DOTTED_SIDE + PassThePigsGame.TROTTER);
		ev += 0.30 * 0.03 * (PassThePigsGame.DOTTED_SIDE + PassThePigsGame.SNOUTER);
		ev += 0.30 * 0.01
				* (PassThePigsGame.DOTTED_SIDE + PassThePigsGame.LEANING_JOWLER);

		ev += 0.22 * 0.09 * (PassThePigsGame.RAZORBACK + PassThePigsGame.TROTTER);
		ev += 0.22 * 0.03 * (PassThePigsGame.RAZORBACK + PassThePigsGame.SNOUTER);
		ev += 0.22 * 0.01
				* (PassThePigsGame.RAZORBACK + PassThePigsGame.LEANING_JOWLER);

		ev += 0.09 * 0.03 * (PassThePigsGame.TROTTER + PassThePigsGame.SNOUTER);
		ev += 0.09 * 0.01
				* (PassThePigsGame.TROTTER + PassThePigsGame.LEANING_JOWLER);

		ev += 0.03 * 0.01
				* (PassThePigsGame.SNOUTER + PassThePigsGame.LEANING_JOWLER);

		ev += 0.35 * 0.35 * PassThePigsGame.DOUBLE_SIDER;
		ev += 0.30 * 0.30 * PassThePigsGame.DOUBLE_SIDER;
		ev += 0.22 * 0.22 * PassThePigsGame.DOUBLE_RAZORBACK;
		ev += 0.09 * 0.09 * PassThePigsGame.DOUBLE_TROTTER;
		ev += 0.03 * 0.03 * PassThePigsGame.DOUBLE_SNOUTER;
		ev += 0.01 * 0.01 * PassThePigsGame.DOUBLE_LEANING_JOWLER;

		return ev;
	}

	/**
	 * AI's make move method. Takes in the human score and the player's score
	 * and is used to determine how much of a risk taker the AI will be.
	 * 
	 * When the AI and human player are tied, the AI will default to playing in
	 * a more aggressive manner, risking loosing more points. Whilst the AI is
	 * ahead of the human player, it will take fewer risks, playing more
	 * conservatively.
	 * 
	 * The expected value of a roll is used make the decision. This value
	 * changes with the number of points the AI earns for the current turn as
	 * well as for the total overall points earned in the game. Each roll means
	 * the AI will be taking a risk in losing the points earned for the turn.
	 * There is also a 1/100 chance that the AI could roll an Oinker, which is
	 * when the pigs land in such a way that they are touching. This means the
	 * AI would lose all the points earned for the turn and the game, reseting
	 * the score to zero.
	 * 
	 * @param aiScore
	 *            the AI's current score for the game
	 * @param humanScore
	 *            the Human players current score for the game
	 * @param turnscore
	 *            the number of points the AI has earned for the turn
	 * @return PassThePigsGame.ROLL (1) if the AI decides to roll the pigs or
	 *         PassThePigsGame.PASS_TO_NEXT (2) if the AI decides to pass the
	 *         pigs
	 */
	public int makeMove(int aiScore, int humanScore, int turnscore)
	{
		aiReasoning = "";
		int scoregap = aiScore - humanScore;
		double currentEV = getCurrentExpectedValue(turnscore, aiScore);

		// 1 is added to the numMoves, since the first roll is roll zero in
		// the indexing, and the AI always rolls at least one time
		aiReasoning += "Roll: " + (numMoves + 1) + " with ExpectedValue of: "
				+ currentEV;

		if (scoregap <= 0)
		{
			if (scoregap < PassThePigsGame.POINTS_TO_WIN / 5 && (int) currentEV > 0)
			{
				// AI will take more risks as it is behind the player
				aiReasoning += " AGGRESSIVE AI will Roll the Pigs\n";
				this.numMoves++;
				return PassThePigsGame.ROLL;
			}
			else
			{
				if (numMoves > 0)
				{
					// AI can pass the pigs if it would like since it has rolled
					// once
					aiReasoning += " AI will Pass the Pigs\n";
					return PassThePigsGame.PASS_TO_NEXT;
				}
				else
				{
					// AI must roll the pigs at least once
					aiReasoning += " AGGRESSIVE AI will Roll the Pigs\n";
					this.numMoves++;
					return PassThePigsGame.ROLL;
				}
			}

		}
		else
		{
			if (scoregap < PassThePigsGame.POINTS_TO_WIN / 10 && (int) currentEV > 1)
			{
				// AI is ahead of human player, so it will play more
				// conservatively here
				numMoves++;
				aiReasoning += " CONSERVATIVE AI will Roll the Pigs\n";
				return PassThePigsGame.ROLL;
			}
			else
			{
				if (numMoves > 0)
				{
					aiReasoning += " AI will Pass the Pigs\n";
					return PassThePigsGame.PASS_TO_NEXT;
				}
				else
				{
					aiReasoning += " CONSERVATIVE AI will Roll the Pigs\n";
					this.numMoves++;
					return PassThePigsGame.ROLL;
				}
			}
		}
	}

	/**
	 * Calculate the current expected value for the AI's turn. The AI's current
	 * total score for the roll and for their overall score for the game. A
	 * double value is returned that is calculated based off the likelihood of
	 * the pigs landing in the Pig Out position, where the AI will loose all the
	 * points accrued for the roll. The value is effected by the chance of
	 * getting an Oinker, which is when the pigs land in a position in which
	 * they are touching, meaning the player looses all the points they have
	 * Accrued for the game.
	 * 
	 * A double value is a returned. A positive value means the player is more
	 * likely to gain points from the roll than lose points; whereas a negative
	 * expected value means the player is more likely to lose points if they
	 * should roll. The magnitude of the value indicates the average number of
	 * points the player could earn.
	 * 
	 * @param turnScore
	 *            total points earned by the AI for this turn
	 * @param totalScore
	 *            total points earned for the whole game
	 * @return the expected value for this roll
	 */
	private double getCurrentExpectedValue(int turnScore, int totalScore)
	{
		double currentEV = this.baseExpectedValue;

		currentEV += (0.30) * (0.35) * (0 - turnScore);
		currentEV += ((1) / PassThePigsGame.CHANCE_OF_OINKER) * (0 - totalScore);

		return currentEV;
	}

	/**
	 * Used to reset the number of moves to zero. This method is called by the
	 * controller when the AI's turn is finished.
	 */
	public void resetNumMoves()
	{
		this.numMoves = 0;
	}

	/**
	 * Returns the string that contians the AI's logical description for its
	 * current move.
	 * 
	 * @return ai reasoning string
	 */
	public String getAIReasoning()
	{
		return this.aiReasoning;
	}

}
