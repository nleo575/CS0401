/*
	Nicolas Leo, nll21
	CS 401 Fall 2017 Assignment 2
*/
import java.util.*; //For random and scanner classes
import java.io.*;	//For file input/output

/**
	The purpose of the Scramble class is to read in a list of words from a file specified
	by the main program. The methods in the class will then scramble the letters in each
	of the words. 
*/
public class Scramble 
{
	private Scanner inFile;				//Scanner used to read input from a file
	private String 	real, 				//String of the real word read from the file
					scrambled;			//String of the scrambled word
	private File file;					//File object used to open the list of words
	private StringBuilder word;			//Stringbuilder used to scramble the words
	private Random rand = new Random();	/*Random number generator used to scramble 
										the words*/
	private boolean gsw = true;			/*Boolean to signal if the getScrambledWord 
										method was called by the main program. 
										Defaulted to true so that the inital getRealWord 
										method can function properly. */
	private boolean grw = false;		/*Boolean to signal if the getRealWord 
										method was called by the main program. 
										Defaulted to false so that the inital 
										getScrambledWord method can function properly. */
	
	/**
		Default constructor for the Scramble class. The constructor takes the name of 
		a text file as an argument. The file is used as the source of the words, 
		and should have one word per line.
		@param fileName is the name of the file where the words are stored.
	*/
	public Scramble(String fileName) throws IOException
	{
		file = new File (fileName);
		inFile = new Scanner(file);
	}

	/**
		Gets the next word from the input file. 
		It has 3 possible return results:
			1) The next word in the file. This will occur if there are words 
			in the list and this is the first call to getRealWord, or if a word 
			is left in the file and getScrambledWord() has been called since the 
			most recent call to getRealWord(). 
			
			2) The same word returned as in 1) above. This will occur if 
			getRealWord() has already been called and then is called again 
			before a call to getScrambledWord().
			
			3) null. This will occur if 1) would apply except that no words 
			remain in the file.
		@return The real word from the input file.
	*/
	public String getRealWord()
	{
		if(inFile.hasNext()== true && gsw == true) 
		{
			word = new StringBuilder(inFile.nextLine());
			real = word.toString();
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
		Takes the real word that is read from the input file and scrambles 
		it in a random way. This method will loop until the resulting scrambled word is
		different from the real word. 
		
		If getRealWord has never been called, or if no words remain in the file, 
		this method will return null. 
		
		Successive calls to this method will not scramble the same real word in 
		a different way. It will only return the same scrambled word.
	
		@return The scrambled word is returned to the main program.
	*/
	public String getScrambledWord()
	{
		if (grw == true) //If a new real word was retrieved from the file
		{
			if (gsw == false) //If the real word hasn't been scrambled yet.
			{
				gsw = true;	/*Sets the gsw boolean to True so that the next time getRealWord 
							is called, it will read a new word from the input file.*/
				word = new StringBuilder(real); /*Sets the StringBuilder object to 
												the real word*/
		
				/*The following loop runs until the randomly scrambled word is not euqal to the 
					real word*/
				while(real.equals(word.toString()))
				{
					int num; /*Keeps track of the current random number. The number will be used
						to move a single character of the word to a new position.*/
					char temp; /*Temporarily stores the character in position "num" of the 
						StringBuilder "word".*/
				
					/*This loop generates a random number and swaps a single character at index
						'i' of the StringBuilder object, with the character stored in 
						index 'num.' This reapeates for each character of the entire word*/
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
}