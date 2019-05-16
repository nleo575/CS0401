/*
	Nicolas Leo, nll21
	CS 401 Fall 2017 Assignment 2
*/
import java.util.*;
import java.io.*;

public class Assig2
{
	public static void main (String [] args) throws IOException
	{
		System.out.print("\033[H\033[2J"); //Clears the console window
		Scanner kb = new Scanner (System.in); //Scanner for keyboard input
		String 	real,	//String of the real word from the word list file.
				scrambled, //String of the scrambled word from the word list file.
				guess = null, //Users guess of the word.
				user;		//Name of the program user.
		char yn = 'Y';	//Char to hold users response to continue or terminate program.
		boolean validInput = true; //Boolean to hold if user input was valid
		
		//Welcome message
		System.out.println("Are you ready to play Scrambler?");
		System.out.print("Please enter your name: ");
		user = kb.next();
		
		System.out.print("\033[H\033[2J"); //Clears the console window
		
		//Start of game
		System.out.println(user +", you have 3 guesses to get the Scramble\n"+
						"Good luck!\n");
		
		Scramble scram = new Scramble("words.txt");
		
		Results result = new Results("results.txt");
		
		real = scram.getRealWord().toUpperCase(); //Get 1st real word.
		scrambled = scram.getScrambledWord().toUpperCase(); //Get 1st scrambled word.
		
		while(yn == 'Y' && real != null) //Game loop
		{
			System.out.print("Scramble: " + scrambled + "\nYour guess: ");
			guess = kb.next().toUpperCase();
			
			int counter = 0; //Count to keep track of guesses made
			while(guess.equals(real)== false && counter < 3)
			{
				System.out.print("\nSorry " + user + ", that isn't correct.\n"+
					"Here are the letters you got correct: ");
										
				//Displays which chars the user got correct
				for(int i = 0; i < real.length(); i++) 
				{
					if (i < guess.length() && real.charAt(i) == guess.charAt(i))
						System.out.print(real.charAt(i));
					else
						System.out.print('_');
				}
				
				counter++;
				System.out.println("\nYou have " + (3 - counter)+ " guesses remaining.");
				
				if (counter < 3) //If guesses remain
				{
					System.out.print("\nScramble: "+ scrambled +"\nYour guess: ");
					guess = kb.next().toUpperCase();
				}
				else //If the user used all 3 guesses & didn't get the answer correct
				{
					System.out.print("\033[H\033[2J"); //Clears the console window
					System.out.println("Round over! Better luck next time " + user+
						".\nThe actual word was \"" + real.toLowerCase() + ".\"");
					result.lost();
				}
			}
			
			if (guess.equals(real))
			{
				System.out.println("Great job " + user +"! You got it!");
				result.won();
			}
				
			do //Prompts the user to play again
			{
				System.out.print("\nWould you like to play another round (Y/N)? ");
				yn = Character.toUpperCase(kb.next().charAt(0));
				if (yn == 'Y' || yn == 'N')
					validInput = true;
				else
					validInput = false;
			}
			while(validInput == false);
			
			if (yn == 'Y')
			{
				real = scram.getRealWord();
				
				if (real == null)
				{
					System.out.print("\033[H\033[2J"); //Clears the console window
					System.out.println("Unfortunately there are no more words in the"+
						" list.");
				}
				else
				{
					real = real.toUpperCase();
					System.out.print("\033[H\033[2J"); /*Clears the console window
														between rounds*/
					System.out.println(user +", you have 3 guesses to get the Scramble\n"+
									"Good luck!\n");
					scrambled = scram.getScrambledWord().toUpperCase();
				}
					
			}
			
			/*This will display the results if the player doesn't wish to continue, or 
			if there are no more words in the file.*/
			if (yn == 'N' || real == null) 
			{
				if (yn == 'N')
					System.out.print("\033[H\033[2J"); //Clears the console window
				System.out.println("Thanks for playing "+ user +".\n"+ result.toString());
				result.save();
			}
		}
	}
}