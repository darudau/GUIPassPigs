package application;

public class PassPigsPlayer
{
	private int numMoves;

	/**
	 * Constructor
	 * 
	 */
	public PassPigsPlayer()
	{
		numMoves = 0;
	}

	/**
	 * AI's make move method. Takes in the human score and the player's score
	 * and is used to determine how much of a risk taker the AI will be.
	 * 
	 * @param aiScore
	 * @param humanScore
	 * @return
	 */
	public int makeMove(int aiScore, int humanScore)
	{
		
		//TODO make proper ai
		// this AI always makes 3 rolls and passes
		System.out.println("AI Moves: " + numMoves);
		if (numMoves == 4)
		{
			numMoves = 0;
			return 2;
		}
		numMoves++;
		return 1;
	}

}
