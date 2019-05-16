/*
	Nicolas Leo, nll21
	CS 401 Fall 2017 Assignment 3
*/
import java.util.*;
import java.io.*;

public class Assig3
{
	public static void main (String [] args) throws IOException
	{
		//Declares a new Scramble2 variable for getting new anagrams.
		Scramble2 scram = new Scramble2("words.txt");

		Player player;		//Player variable to store user info(name/rounds/wins/losses)
		int rounds = 0, wins = 0, losses = 0; 	//Stores the stats for this particular game.
		
		PlayerList pl = new PlayerList("players.txt");//PlayerList database object
													  //to store game data

		Scanner sc = new Scanner (System.in); //Scanner for keyboard input
		String 	real,			//String of the real word from the word list file.
				scrambled, 		//String of the scrambled word from the word list file.
				guess = null, 	//Users guess of the word.
				name;			//Name of the program user.
		char yn = 'Y';	//Char to hold users response to continue or terminate program.
		boolean validInput = true; //Boolean to hold if user input was valid
		
		//Welcome message
		System.out.print("\033[H\033[2J"); //Clears the console window
		System.out.println("Are you ready to play Scrambler?");
		System.out.print("Please enter your name to play or <Enter> to quit: ");
		name = sc.nextLine();

		while (name.length() > 0)
		{
			// getPlayer will return a Player object if the player's name is within the
			// PlayerList or null otherwise
			player = pl.getPlayer(name);

			if (player != null) //Indicates a returning player
			{
				System.out.print("\033[H\033[2J"); //Clears the console window
				System.out.println("Welcome back " + name + ".");
			}
			else	//The name entered above was not found in the database.
			{

				System.out.print("\033[H\033[2J"); //Clears the console window
				System.out.print("\""+ name + "\" was not found in the database.\n");

				do //Prompts the user to enter a valid answer.
				{
					System.out.print("Do you wish to add your name to the database and"+
						" start playing (Y/N)? ");
					yn = Character.toUpperCase(sc.next().charAt(0));

					//Validates the input.
					if (yn == 'Y' || yn == 'N')
						validInput = true;
					else
						validInput = false;
				}
				while(validInput == false);

				if (yn == 'Y') //If the new user wants to play the game
				{
					player = new Player(name);
					System.out.print("\033[H\033[2J"); //Clears the console window
					System.out.println("Welcome "+ name+".\n");
					pl.addPlayer(player);//Adds the player to the database
				}
				else
					System.out.print("\033[H\033[2J"); //Clears the console window

			}
			
			real = scram.getRealWord().toUpperCase(); 			//Get 1st real word.
			scrambled = scram.getScrambledWord().toUpperCase(); //Get 1st scrambled word.
			
			while(yn == 'Y' && real != null) //Game loop
			{
				System.out.println("You have 3 tries to guess the scrambled word.\n"+
							"Good luck!\n");

				System.out.print("Scrambled: " + scrambled + "\nYour guess: ");
				guess = sc.next().toUpperCase();
				
				int counter = 0; //Count to keep track of guesses made
				while(guess.equals(real)== false && counter < 3)
				{
					System.out.print("\nSorry " + name + ", that isn't correct.\n"+
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
					System.out.println("\nYou have " + (3 - counter)+ " guess"+ ((3-counter) > 1? "es": "")
						+" remaining.");
					
					if (counter < 3) //If guesses remain
					{
						System.out.print("\nScramble: "+ scrambled +"\nYour guess: ");
						guess = sc.next().toUpperCase();
					}
					else //If the user used all 3 guesses & didn't get the answer correct
					{
						System.out.print("\033[H\033[2J"); //Clears the console window
						System.out.println("Round over! Better luck next time.\n"+
							"The actual word was \"" + real.toLowerCase() + ".\"");
						pl.lost(); //Updates the player and playerList total rounds/wins/losses
						rounds++; losses++; //Updates the player's stats for this game.
					}
				}
				
				if (guess.equals(real))	//User guessed correctly.
				{
					System.out.println("Great job " + name +"! You got it!");
					pl.won();	//Updates the player and playerList total rounds/wins/losses
					wins++; rounds++; //Updates the player's stats for this game.
				}
					
				do //Prompts the user to play again
				{
					System.out.print("\nWould you like to play another round (Y/N)? ");
					yn = Character.toUpperCase(sc.next().charAt(0));
					if (yn == 'Y' || yn == 'N')
						validInput = true;
					else
						validInput = false;
				}
				while(validInput == false);
				
				if (yn == 'Y') //The user chose to play again
				{
					real = scram.getRealWord();
					
					if (real == null) //There were no unused real words left.
					{
						System.out.print("\033[H\033[2J"); //Clears the console window
						System.out.println("Unfortunately there are no more words in the"+
							" list.");
					}
					else	//A real word was left to use.
					{
						real = real.toUpperCase();
						System.out.print("\033[H\033[2J"); //Clears the console window
						scrambled = scram.getScrambledWord().toUpperCase();
					}
						
				}
				
				/*This will display the results if the player doesn't wish to continue, or 
				if there are no more words in the file.*/
				if (yn == 'N' || real == null) 
				{
					if (yn == 'N')
						System.out.print("\033[H\033[2J"); //Clears the console window
					System.out.println("Thanks for playing "+ name +"!\n"+
									"In today's game you had the following results:\n"+
									"\tRounds played:\t"+ rounds + 
									"\n\tWins:\t\t"+ wins +
									"\n\tLosses:\t\t"+ losses +
									"\n\nHere are your cumulative stats:\n" + player.toString());
				}
			}
			
			System.out.println("Are you ready to play Scrambler?");
			System.out.print("Please enter your name to play or <Enter> to quit: ");
			sc.nextLine();	//Removes the end of line character from the previous call.
			name = sc.nextLine(); //Gets the name of the next user.
			scram.reset();	//Resets the words so that a new player can play.
			yn = 'Y'; //Defaults yn back to Y so that the game loop can proceed.
			rounds = 0; wins = 0; losses = 0; //Defaults current game stats for new user.
		}

		//The game is over
		System.out.print("\033[H\033[2J"); //Clears the console window
		System.out.println("Game Over!\nHere are the player stats:\n" + pl.toString());

		// Write the players back to a text file so that they can be retrieved later
		pl.saveList();

	}
}