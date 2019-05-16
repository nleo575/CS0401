/*
	Nicolas Leo, nll21
	CS 401 Fall 2017 Assignment 3
*/
import java.util.*; //For Random, Scanner, and ArrayList classes
import java.io.*;	//For file input/output

/**
	The purpose of the Player class is to encapsulation the player's name, 
	win / loss information, and give methods for accessing and mutating the 
	player information.
*/
public class Player 
{
	private String name;
	private int rounds, wins, losses;//Cumulative number of rounds, wins, & losses


	/**
		Default constructor for the Player class. 
	*/
	public Player() 
	{
		name = "Default";
	}

	/**
		Name-only constructor for the Player class. 
	*/
	public Player(String nm) 
	{
		name = nm;
	}

	/**
		Main constructor for the Player class. 
	*/
	public Player(String nm, int r, int w, int l) 
	{
		name = nm;
		rounds = r;
		wins = w;
		losses = l;
	}	

	/**
		Deep copy constructor for the Player class. 
	*/
	public Player(Player p) 
	{
		name = new String(p.getName());
		rounds = p.getRounds();
		wins = p.getWins();
		losses = p.getLosses();
	}

	/**
		Returns the number of rounds the player lost.
		@return Player's losses.
	*/
	public int getLosses()
	{
		return losses;
	}

	/**
		Returns the name of the player
		@return Player's name.
	*/
	public String getName()
	{
		return name;
	}

	/**
		Returns the number of rounds the player played.
		@return Player's rounds played.
	*/
	public int getRounds()
	{
		return rounds;
	}

	/**
		Returns the number of rounds the player won.
		@return Player's rounds won.
	*/
	public int getWins()
	{
		return wins;
	}

	/**
		Increments the total number of losses and rounds when a player loses a round.
	*/
	public void lost()
	{
		losses++;
		rounds++;
	}

	/**
		Sets the number of losses and updates the total rounds played.
	*/
	public void setLosses(int l)
	{
		losses = l;
		rounds = wins + losses;
	}

	/**
		Sets the name of the player.
	*/
	public void setName(String n)
	{
		name = n;
	}

	/**
		Sets the number of wins and updates the total rounds played.
	*/
	public void setWins(int w)
	{
		wins = w;
		rounds = wins + losses;
	}

	/**
		Method for creating a formatted string of the player's info.
		@return Returns a formatted string version of the player info.
	*/
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		b.append("\tName: " + name + "\n");
		b.append("\tTotal Rounds Played:\t" + rounds + "\n");
		b.append("\tTotal Rounds Won:\t" + wins + "\n");
		b.append("\tTotal Rounds Lost:\t" + losses + "\n");
		return b.toString();
	}

	/** 
		Method for creating an unformatted string of the player's info. Useful for saving 
		the information to a file.
		@return Returns an unformatted string version of the players info.
	*/
	public String toStringFile()
	{
		StringBuilder b = new StringBuilder();
		b.append(name + " ");
		b.append(rounds + " ");
		b.append(wins + " ");
		b.append(losses);
		return b.toString();
	}

	/**
		Increments the total number of wins and rounds when a player wins a round.
	*/
	public void won()
	{
		wins++;
		rounds++;
	}
}