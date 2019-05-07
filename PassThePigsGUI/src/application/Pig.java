package application;
import java.util.ArrayList;
import java.util.Random;

/**
 * Pig class is a representation of a Pig within the game Pass the Pigs. The pig
 * is treated as a weighted die. The Pig can be rolled to generate an outcome.
 * 
 * @version 0.1 April 30, 2019
 * @author Douglas Rudau
 */
public class Pig
{
	/** Percentage weights for each postition the pig can land in */
	private int[] weights;

	/** names of each possible outcome for the pig to land in */
	private String[] outcomeNames;

	/** array of integer values for each possible outcome */
	private ArrayList<Integer> sampleSpace;

	/** Random number generator */
	private Random rand;

	/**
	 * Constructor for the Pig Class. Creates a default Pig with the default
	 * weights.
	 */
	public Pig()
	{
		// initializing weights. Weights are from wikipedia's article on Pass
		// the Pigs
		// the weights have been rounded to the nearest percent.
		weights = new int[6];
		weights[0] = 35;
		weights[1] = 30;
		weights[2] = 22;
		weights[3] = 9;
		weights[4] = 3;
		weights[5] = 1;

		// initializing the array of strings that contains the names for each
		// outcome.
		outcomeNames = new String[6];
		outcomeNames[0] = "Plain Side";
		outcomeNames[1] = "Dotted Side";
		outcomeNames[2] = "Razorback";
		outcomeNames[3] = "Trotter";
		outcomeNames[4] = "Snouter";
		outcomeNames[5] = "Leaning Jowler";

		sampleSpace = new ArrayList<Integer>();
		initializeSampleSpace();

		rand = new Random(); // set seed to 15 for now, can be changed later.
	}

	/**
	 * Initializes the ArrayList that contains the possible outcomes for the
	 * pass the pigs.
	 */
	private void initializeSampleSpace()
	{
		for (int i = 0; i < weights.length; i++)
		{
			for (int j = 0; j < weights[i]; j++)
			{
				sampleSpace.add(new Integer(i));
			}
		}
	}

	/**
	 * "Rolls" the pig and returns an integer value for the outcome.
	 * 
	 * @return int between 0 and 5 showing the outcome for the pig that was
	 *         rolled
	 */
	public int rollPig()
	{
		System.out.println(sampleSpace.get(rand.nextInt(100)));
		return sampleSpace.get(rand.nextInt(100));
	}

	/**
	 * "Rolls" the pig and returns the string name of the outcome.
	 * 
	 * @return string that gives the name of the position of the pig.
	 */
	public String rollPigString()
	{
		return outcomeNames[sampleSpace.get(rand.nextInt(100))];
	}

	/**
	 * Converts an integer to the position name for a pig that has been rolled.
	 * 
	 * @param position
	 *            integer representtion of the position the pig is in
	 * @return a string describing the position the pig is in
	 * @throws IllegalARgumetnException
	 *             if a postion that is out of bounds is entered.
	 */
	public String intPigPositionToString(int position)
			throws IllegalArgumentException
	{
		if (!(position < 6 && position >= 0))
		{
			throw new IllegalArgumentException(
					"Invalid position, position must be between 0 and 5");
		}

		return outcomeNames[position];
	}

	// debugging method may remove later
	// TODO
	public void printSampleSpace()
	{
		System.out.println("Smaple Space");
		System.out.println("SampleSpace Size = " + sampleSpace.size());
		for (int i = 0; i < sampleSpace.size(); i++)
		{
			System.out.println(i + ": " + sampleSpace.get(i).intValue());
		}
	}
}
