/*
	Nicolas Leo, nll21
	CS 401 Fall 2017 Assignment 3
*/
import java.util.*; //For Random, Scanner, and ArrayList classes
import java.io.*;	//For file input/output
import java.text.*; //For Decimal Format

/**
	The purpose of the PlayerList class is to organize/maintain Player objects
	 in a simple database.
*/
public class PlayerList 
{
	private File data;					//File to store database data
	private Player currentPlayer;		//Player variable to store the current player.
	private PrintWriter pw;				//Used to write to the data file
	private Scanner sc;					//Scanner used to read input from the result file.	
	private ArrayList<Player> players;	//ArrayList of all players of the game.
	private int numPlayers,				//Total number of players in the database.
				rounds,					//Cumulative number of rounds played for all players
				wins,					//Cumulative wins for all players.
				losses;					//Cumulative losses for all players.

	/**
		Default constructor for the PlayerList class.
		@param 	fileName Takes the name of a filename where the players names 
				and scores are stored. The file must be formatted as 
				<name>_<rounds>_<won>_<lost>.
	*/
	public PlayerList(String fileName) throws IOException
	{
		data = new File (fileName); //Creates a file object to read/write data
		 
		if (data.exists()) //If the file exists, it reads in the existing values.
		{
			sc = new Scanner(data);
			players = new ArrayList<Player>(0);

			while(sc.hasNext())
			{
				players.add(new Player(sc.next(), sc.nextInt(), 
									sc.nextInt(), sc.nextInt()));
				rounds += players.get(numPlayers).getRounds();
				wins += players.get(numPlayers).getWins();
				losses += players.get(numPlayers).getLosses();
				numPlayers++;
			}	
			quickSort(0, numPlayers - 1); //Sorts the players by name in an ascending order.
		}
	}

	/**
		Adds a new player to the database and updates the total rounds/wins/losses.
		@param p P is a player object that will be added to the database.
	*/
	public void addPlayer(Player p)
	{
		players.add(p);
		currentPlayer = p;
		rounds += p.getRounds();
		wins += p.getWins();
		losses += p.getLosses();
		numPlayers++;
		sort(); 
	}

	/**
		Searches the database for a particular player's name using binary search.
		@param	s Name of the player to search for.
		@return Returns a copy of the player's object, or returns null if the player wasn't
				found.
	*/
	public Player getPlayer(String s)
	{
		int l = 0, m, r = numPlayers - 1; //Left, middle, and right endpoints of players.
		m = (r+l)/2;

		while(l<= r)
		{
			if (players.get(m).getName().equalsIgnoreCase(s))
			{
				currentPlayer = players.get(m);
				return players.get(m); /*Returns the player info.*/
			}
			else if (s.compareToIgnoreCase(players.get(m).getName())<0)
				r = --m;
			else
				l = ++m;

			m = (l+r)/2;
		}

		return null;
	}

	/**
		Increments the total number of lost games and rounds, and also 
		the individual counts for the player.
	*/
	public void lost()
	{
		currentPlayer.lost();
		losses++;	rounds++;
	}

	/**
		Sorts the database by player name in ascending order using quick sort.
		This method was adapted from code found on 
		http://www.java2novice.com/java-sorting-algorithms/quick-sort/
		@param low The beginning of the sorting index, should usually be 0.
		@param high The end of the sorting index, should usually be the number 
				of items int the index - 1. 
	*/
	private void quickSort(int low, int high)
	{
		int l = low, h = high;

		//Setting the pivot number as the middle index.
		Player pivot = players.get(low + (high - low)/2),
				temp;	//Creating a temp Player object to swap later on.

		while(l <= h)
		{
			while(players.get(l).getName().compareToIgnoreCase(pivot.getName()) < 0)
				l++;
			while(players.get(h).getName().compareToIgnoreCase(pivot.getName()) > 0)
				h--;
			if(l <= h)
			{
				temp = players.get(l);
				players.set(l, players.get(h));
				players.set(h, temp);

				l++; h--;
			}
		}
		//Recursively calls quickSort() method
		if (low < h)
			quickSort(low, h);
		if(l < high)
			quickSort(l, high);
	}

	/**
		Removes a specified player from the database. If the player is found, the total game
		statistics are updated as well.
		@param remName 	Is a string containing the name of the player you wish to remove 
						from the database.
		@return Returns a new deep copy of the player's object if the player was found in the 
				database, otherwise this method will return null.
	*/
	public Player removePlayer(String remName)
	{
		//Binary search of the entire database which is sorted by name in ascending order.
		int l = 0, m, r = numPlayers - 1; //Left, middle, and right endpoints of players.
		m = (r+l)/2;

		while(l<= r)
		{
			if (players.get(m).getName().equalsIgnoreCase(remName))
			{
				Player temp = new Player(players.get(m)); /*Returns a new Player object to 
													prevent data corruption.*/
				//Adjusts the total game statistics once the player is removed.
				rounds -= temp.getRounds();
				wins -= temp.getWins();
				losses -= temp.getLosses();
				numPlayers--;
				players.remove(m);
				return temp;
			}
			else if (remName.compareToIgnoreCase(players.get(m).getName())<0)
				r = --m;
			else
				l = ++m;

			m = (l+r)/2;
		}

		return null;
	}

	/**
		Saves the current rounds, wins, and losses to the data file and then 
		closes the file.
	*/
	public void saveList() throws IOException
	{
		pw = new PrintWriter(data); /*Creates a PrintWriter object to write to the file.*/
		for(int i = 0; i < numPlayers; i++)
		{
			pw.println(players.get(i).toStringFile());
		}
		pw.close();
	}

	/**
		Sorts the database by player name in ascending order using insertion sorting. This
		sort method is optimized for lists that are already almost sorted.
	*/
	private void sort()
	{	
		//Code adapted from SortInt.java hand out distributed in Dr. Ramierz's CS401 class 
		// Move each item (starting at index 1) over to its correct spot
		// on the "left", sorted side of the array.
		for (int index = 1; index < numPlayers; index++)
		{
			Player key = players.get(index);
			int position = index;

			// Move the current item to the left "past" any items that 
			// are larger than it.  This is accomplished by shifting the
			// larger items to the right.
			while (position > 0 && key.getName().compareTo(players.get(position-1).getName()) < 0)
			{
				players.set(position, players.get(position-1));
				position--;
			}

			players.set(position, key);
		}
	}

	/**
		Produces a formatted output listing of the total number of players, and cumulative
		rounds/wins/losses for all players combined.
	*/
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		b.append("Total Players: " + numPlayers + "\n");
		b.append("\tTotal Rounds Played:\t" + rounds + "\n");
		b.append("\tTotal Rounds Won:\t" + wins + "\n");
		b.append("\tTotal Rounds Lost:\t" + losses + "\n");

		//Gets percent of rounds won, then rounds & formats the result.
		double pct;
		if (rounds >0)
		{
			pct = ((double) wins)/rounds;
			pct *= 10000;
			if (pct%10 >=5)
				pct+=5;
			pct = pct/100;
		}
		else
			pct = 0.0;
		DecimalFormat df = new DecimalFormat("#.#");

		b.append("\tPct of Rounds Won:\t" + df.format(pct));
		return b.toString();
	}

	/**
		Produces a formatted output listing of the total number of players, and cumulative
		rounds/wins/losses for all players combined. Then lists the individual statistics
		for each player, sorted in ascending order by name.
	*/
	public String toStringAdmin()
	{
		StringBuilder sb = new StringBuilder(toString()+"\n\nPlayers:\n");
		for(int i = 0; i < numPlayers; i++)
			sb.append(players.get(i).toString()+"\n");
		return sb.toString();
	}

	/**
		Increments the total number of wins and rounds, and also 
		the individual counts for the player.
	*/
	public void won()
	{
		currentPlayer.won();
		wins++;	rounds++;
	}

}