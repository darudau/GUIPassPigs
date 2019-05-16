package application;

public class PassPigsPlayer
{
	private String aiReasoning;

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
	 * turn and overall score changes.
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
	 * @param aiScore
	 * @param humanScore
	 * @return
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

	private double getCurrentExpectedValue(int turnScore, int totalScore)
	{
		double currentEV = this.baseExpectedValue;

		currentEV += (0.30) * (0.35) * (0 - turnScore);
		currentEV += ((1) / PassThePigsGame.CHANCE_OF_OINKER) * (0 - totalScore);

		return currentEV;
	}

	public void resetNumMoves()
	{
		this.numMoves = 0;
	}

	public String getAIReasoning()
	{
		return this.aiReasoning;
	}

	public void resetAIReasoning()
	{
		this.aiReasoning = "";
	}

}
