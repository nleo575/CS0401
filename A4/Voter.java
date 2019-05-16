/*
	Nicolas Leo, nll21
	CS 401 Fall 2017 Assignment 4
*/

import java.util.*; // For Scanner class
import java.io.*;	// For file input/output
import javax.swing.*;

/**
	The purpose of the Voter class is to encapsulate voter information including the voter's
	identification number, his/her full name, and whether he/she successfully 
	voted.
*/
public class Voter
{
	private String id,		// Voter's identification number 
				   name;	// Voter's full name
	private boolean voted;	// Boolean to track whether the voter voted.

	/**
		Constructor -- takes 2 arguments and makes a new Voter. Initializs his/her
		voting status to "not voted."
		@param iNum	String containing the voter's voter ID number.
		@param n 	String containing the voter's name.
	*/
	public Voter(String iNum, String n)
	{
		id = new String(iNum);
		name = new String(n);
		voted = false;
	}

	/**
		Constructor -- takes 3 arguments and makes a new Voter. Used by and administrator 
		or election official to create a new voter and allow them to set the voting status.
		@param iNum	String containing the voter's voter ID number.
		@param n 	String containing the voter's name.
		@param b 	Boolean of whether the voter has voted yet.
	*/
	public Voter(String iNum, String n, boolean b)
	{
		id = new String(iNum);
		name = new String(n);
		voted = b;
	}

	/**
		Constructor -- takes 3 arguments and makes a new Voter. Used when reading
		in voter data from a file.
		@param iNum	String containing the voter's voter ID number.
		@param n 	String containing the voter's name.
		@param b 	String of whether the voter has voted yet.
	*/
	public Voter(String iNum, String n, String b)
	{
		id = new String(iNum);
		name = new String(n);
		voted = Boolean.parseBoolean(b);
	}

	/**
		Accessor to get the voter's identification number.
		@return Returns a string containing the voter's ID number.
	*/
	public String getId()
	{
		return id;
	}

	/**
		Accessor to get the voter's name.
		@return Returns a string containing the voter's name
	*/
	public String getName()
	{
		return name;
	}

	/**
		Method for finding if a voter is registered.
		@param fileName String of the file name where the voters are stored.
		@param	vID 	String of the voter identification number to check for.
		@return Returns one of two values: null if the voter is not registered, otherwise 
				a new Voter object is returned if the voter is found.
	*/
	static Voter getVoter(String fileName, String vID) 
	{
		Voter V = null;
		try
		{
			//Opens the specified file containing voter data
			File file = new File(fileName); 
			
			//If the file exists, it reads in the data of registered voters
			if (file.exists()) 
			{
				Scanner sc = new Scanner(file).useDelimiter(":|\n"); 
				String tempID = new String();
				boolean found = false;
				while(found == false && sc.hasNext())
				{
					tempID = sc.next();
					if (tempID.equals(vID)) 
					{
						found = true;
						V = new Voter(vID, sc.next(), sc.next()); 
					}
					else
					{
						sc.nextLine();
					}
				}
			}
		}
		catch (IOException ex)
		{
			return null;

		}

		return V;
	}

	/**
		Accessor to get the voter's voting status.
		@return Voter's voting status. True indicates that the voter successfully voted.
	*/
	public boolean hasVoted()
	{
		return voted;
	}
	
	/**
		Method used to update the voter's voting status to "voted" in the voter data
		file.
		@param fileName String of the file name where the voters are stored.
	x	@param	v Voter object of the voter that needs to be updated.
	*/
	static void saveVoter(String fileName, Voter v)
	{
		try
		{
			//Opens the specified file containing voter data.
			File originalFile = new File(fileName); 

			Scanner sc = new Scanner (originalFile).useDelimiter(":|\n");

			//Creates a new temporary file in which to write the updated data to.
			File tempFile = new File("_voters.txt");
			PrintWriter	pw = new PrintWriter(tempFile); /*Creates a PrintWriter 
													object to write to the  temp file.*/
			String tempID;
			boolean updated = false;

			//Reads each line of the file until the current voter is found
			while(sc.hasNext() && updated==false) 
			{
					tempID = sc.next();
					pw.print(tempID); //writes the voter ID to the temp file

					//Once the voter ID is located within the voters file,
					//the voter is marked as having voted and cannot vote again.
					if (tempID.equals(v.getId()))
					{
						pw.println(":"+sc.next()+ ":"+ v.hasVoted());
						sc.nextLine();
						updated = true;
					}
					else
						pw.println(sc.nextLine());
			}

			while(sc.hasNext()) //Then copies all remaining lines unchanged
				pw.println(sc.nextLine());
			
			pw.close();

			originalFile.delete();
			tempFile.renameTo(new File("voters.txt"));
		}
		catch (IOException ex)
		{
			JOptionPane.showMessageDialog(null, "The specified file was not found.");
		}
	}

	/**
		Return a formatted string version of the voter's information.
		@return String containig the voter's ID number, Name, and whether or not the
			voter voted yet.
	*/
	public String toString()
	{
		StringBuffer B = new StringBuffer();
		B.append("ID: " + id + ", ");
		B.append("Name: "+ name + ", ");
		B.append("Voted (true/false): " + voted);
		return B.toString();
	}

	/**
		Mutator to mark that the voter successfully voted in the election.
	*/
	public void vote()
	{			
		voted = true;
	}
}
