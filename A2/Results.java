/*
	Nicolas Leo, nll21
	CS 401 Fall 2017 Assignment 2
*/
import java.util.*; //For random class
import java.io.*;	//File file input/output

/**
	The purpose of the Results class is to store the results of a 
	game to a text file specified by the main program.
*/
public class Results
{
	private File result;		//File to store results
	private PrintWriter outFile;//Used to write to the results file
	private Scanner inFile;		//Scanner used to read input from the result file.
	int rounds, wins, losses;	//Cumulative number of rounds, 
								//wins, & losses for all players.
	
	/**	
		Default constructor to create a Results object. 
		A constructor that takes the name of a text file as an argument. 
		The file will be used to store the results, and has the following format:
		Cumulative ROUNDS, Cumulative WINS, Cumulative LOSSES (each on their own line).
		If the file doesn't exist, a new one will be created and the counts reset to 0.
		@param fileName Name of the results file specified by the main program.		
	*/
	public Results(String fileName) throws IOException
	{
		result = new File(fileName); //New file object created to store the results.
		
		if (result.exists()) //If the file exists, it reads in the existing values.
		{
			inFile = new Scanner(result);
			if (inFile.hasNext())
			{
				rounds = Integer.parseInt(inFile.nextLine());
				wins = Integer.parseInt(inFile.nextLine());
				losses = Integer.parseInt(inFile.nextLine());
			}
			else
			{
				rounds = 0;
				wins = 0;
				losses = 0;
			}

		}
		else //If the file doesn't exist, it resets the values to 0.
		{
			rounds = 0;
			wins = 0;
			losses = 0;
		}

		outFile = new PrintWriter(result); /*Creates a PrintWriter object to write 
											to the file.*/
	}

	/**
		Increments the total number of losses and rounds when it is called.
	*/
	public void lost()
	{
		losses++;
		rounds++;
	}

	/**
		Saves the current rounds, wins, and losses to the results file and then 
		closes the file.
	*/
	public void save()
	{
		outFile.println(rounds);
		outFile.println(wins);
		outFile.print(losses);
		outFile.close();
	}

	/**
		Returns the current number of rounds, wins, and losses as a 
		single string.
		@return String of cumulative rounds, wins, and losses
	*/
	public String toString()
	{
		return "\tRounds tried: " + rounds + "\n\tRounds won: " + wins + "\n\tRounds lost: " + losses;
	}
	
	/**
		Increments the total number of wins and rounds when it is called.
	*/
	public void won()
	{
		wins++;
		rounds++;
	}
}