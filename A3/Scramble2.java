/*
	Nicolas Leo, nll21
	CS 401 Fall 2017 Assignment 3
*/
import java.util.*; //For Random, Scanner, and ArrayList classes
import java.io.*;	//For file input/output

/**
	The purpose of the Scramble2 class is to read in a list of words from a file specified
	by the main program. The methods in the class will then scramble the letters in each
	of the words. 
*/
public class Scramble2 
{
	private Scanner sc;					//Scanner used to read input from a file
	private String 	real, 				//String of the real word read from the file
					scrambled;			//String of the scrambled word
	private File file;					//File object used to open the list of words
	private StringBuilder word;			//String builder used to scramble the words
	private Random rand = new Random();	/*Random number generator used to scramble 
										the words*/
	private boolean gsw = true;			/*Boolean to signal if the getScrambledWord 
										method was called by the main program. 
										Defaulted to true so that the initial getRealWord 
										method can function properly. */
	private boolean grw = false,		/*Boolean to signal if the getRealWord 
										method was called by the main program. 
										Defaulted to false so that the initial 
										getScrambledWord method can function properly. */
					used [];			//Array used to keep track if a word was used.
	private ArrayList<String> wordList;	//Array list object to store all word from the file.
	private int numWords = 0, 			//Int to store total number of words in the input file.
				numWordsUsed =0;		//Int to store how many words were used in the game.

	/**
		Default constructor for the Scramble2 class. The constructor takes the name of 
		a text file as an argument. The file is used as the source of the words for the game, 
		and should have one word per line. Words are tracked and can only be used one time
		per game.
		@param fileName is the name of the file where the words are stored. 
	*/
	public Scramble2(String fileName) throws IOException
	{
		file = new File (fileName);
		sc = new Scanner(file);
		wordList = new ArrayList<String>(0);
		while(sc.hasNext()== true) //Reads all of the words from the file into wordList
		{
			wordList.add(sc.nextLine());
			numWords++;					//Counts the total words in the file.
		}
		used = new boolean[numWords];
	}

	/**
		Gets a randomly chosen word from the ArrayList wordList. 
		It has 3 possible return results:
			1) The next randomly chosen word in wordList. This will occur if there 
			are any unused words left in wordList and this is the first call to getRealWord, 
			or if a word is left in wordList and getScrambledWord() has been called since the 
			most recent call to getRealWord(). 
			
			2) The same word returned as in 1) above. This will occur if 
			getRealWord() has already been called and then is called again 
			before a call to getScrambledWord().
			
			3) null. All of the words in the wordList were used.
		@return The real word from wordList or null.
	*/
	public String getRealWord()
	{
		if(numWordsUsed < numWords && gsw == true) 
		{
			int num = rand.nextInt(numWords);; /*Keeps track of the current random number. 
			The number will be used to select a word from wordList.*/
			
			//Generates another random number until an unused word is found in wordList
			while(used[num] == true) 
			{
				num = rand.nextInt(numWords); 
			}

			used[num] = true; 	//Marks the word as used so it can't be used again.
			numWordsUsed++;	

			word = new StringBuilder(wordList.get(num));
			real = wordList.get(num);
			gsw = false;
			grw = true;
			return real;
		}
		else if (gsw == false) //If a real word was called but never scrambled.
			return real;
		else //No words are left in the file.
		{	
			grw = false;
			return null;
		}

	}
	
	/**
		Takes a real word from wordList and scrambles it in a random way. This method
		will loop until the resulting scrambled word is different from the real word. 
		
		If getRealWord has never been called, or if all words in wordList were used, 
		this method will return null. 
		
		Successive calls to this method will not scramble the same real word in 
		a different way. It will only return the same scrambled word.
	
		@return The scrambled word is returned to the main program.
	*/
	public String getScrambledWord()
	{
		if (grw == true) //If a new real word was retrieved from wordList
		{
			if (gsw == false) //If the real word hasn't been scrambled yet.
			{
				gsw = true;	/*Sets the gsw boolean to true so that the next time getRealWord 
							is called, it will read a new word from wordList.*/
				word = new StringBuilder(real); /*Sets the StringBuilder object to 
												the real word*/
		
				/*The following loop runs until the randomly scrambled word is not 
				equal to the real word*/
				while(real.equals(word.toString()))
				{
					int num; /*Keeps track of the current random number. The number will
						be used	to move a single character of the word to a new position.*/
					char temp; /*Temporarily stores the character in position "num" of the 
						StringBuilder "word".*/
				
					/*This loop generates a random number and swaps a single character at
						index 'i' of the StringBuilder object, with the character stored in 
						index 'num.' This repeats for each character of the entire word*/
					for (int i = 0; i < word.length(); i++)
					{
						num = rand.nextInt(word.length());
						temp = word.charAt(num);
						word.setCharAt(num, word.charAt(i));
						word.setCharAt(i, temp);	
					}
				}
				scrambled = word.toString();
				return scrambled;
			}
			else //If the real word was already scrambled
				return scrambled;
		}
		else
			return null;
	}

	/**
		Resets the Scramble2 object to its default values. Allowing the game to be played
		again using the same list of words but in a different order.
	*/
	public void reset()
	{
		numWordsUsed = 0;
		for(int x = 0; x < used.length; x++)
			used[x] = false;
		gsw = true;
		grw = false;
	}
}