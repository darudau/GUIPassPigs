package application;

public class PassPigsPlayer
{
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
		int scoregap = aiScore - humanScore;
		double currentEV = getCurrentExpectedValue(turnscore, aiScore);

		System.out.print("Current ExpectedValue: " + currentEV);

		if (scoregap <= 0)
		{
			if (scoregap < 20 && currentEV > 0)
			{
				// AI will take more risks as it is behind the player
				numMoves++;
				System.out.print(" AI will Roll the Pigs\n");
				return PassThePigsGame.ROLL;
			}
			else
			{
				if (numMoves > 0)
				{
					// AI can pass the pigs if it would like since it has rolled
					// once
					System.out.print(" AI will Pass the Pigs\n");
					return PassThePigsGame.PASS_TO_NEXT;
				}
				else
				{
					// AI must roll the pigs at least once
					System.out.print(" AI will Roll the Pigs\n");
					return PassThePigsGame.ROLL;
				}
			}

		}
		else
		{
			if (scoregap < 10 && currentEV > 1)
			{
				// AI is ahead of human player, so it will play more
				// conservatively here
				numMoves++;
				System.out.print(" AI will Roll the Pigs\n");
				return PassThePigsGame.ROLL;
			}
			else
			{
				if (numMoves > 0)
				{
					System.out.print(" AI will Pass the Pigs\n");
					return PassThePigsGame.PASS_TO_NEXT;
				}
				else
				{
					System.out.print(" AI will Roll the Pigs\n");
					return PassThePigsGame.ROLL;
				}
			}

		}

		// // TODO make proper ai
		// // this AI always makes 3 rolls and passes
		// System.out.println("AI Moves: " + numMoves);
		// if (numMoves == 4)
		// {
		// numMoves = 0;
		// return PassThePigsGame.PASS_TO_NEXT;
		// }
		// numMoves++;
		// return PassThePigsGame.ROLL;
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

}
