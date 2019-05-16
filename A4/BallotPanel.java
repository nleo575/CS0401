/*
	Nicolas Leo, nll21
	CS 401 Fall 2017 Assignment 4
*/

import java.util.*; // For Scanner class
import java.io.*;	// For file input/output
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
	This class extends the JPanel class. It allows the main voting program to display 
	instructions and buttons to the user to cast his/her ballot in the election.
*/
public class BallotPanel extends JPanel
{
	private int numOffices;	//Int to keep track of the number of offices to vote for
	private VoteInterface vi; //VoteInterface from the main program
	private JButton castVote; //Button to cast vote
	private VoteListenType voteListener; //Action listener for the vote button
	private String [] vote; /*Array to hold the user's votes. Each element represents
							the voter's choice for a different office.*/
	private Ballot [] bals; //Array to hold the ballot objects.

	/**
		Private inner class to store individual offices and candidates running or 
		the office.
		This class also creates individual files for each office to store the results
		of the election and makes a JPanel object for the office which will be displayed
		to the user.
	*/
	private class Ballot
	{
		int ballotID, //Ballot ID which will be used to store the results in a file
			numCandidates = 0, //Number of candidates running for this office.
			panNum;	//Number of office's particular sub-panel.
		String office, temp, tempName; //Name of the particular office

		/*Array list containing all of the buttons that will be presented to the user 
			and allow them to vote*/
		ArrayList<JButton> buttons = new ArrayList<JButton>(0);
		JLabel L; //Label to store the name of the office
		JPanel P;	//Sub-panel to store all of the buttons.
		ActionListener candidateListener; //Listener to capture button clicks
		File f; //File object to store the results of this office's election.
		
		/**
			Constructor for an ballot object. The object is used to temporarily store 
			the name of the office, the candidates running for the office, create buttons 
			and a sub-panel which will allow them to vote for a candidate, and creates a 
			file in which to store the results of the election for this particular office.

			@param  line String to store the line of text which is read in by the 
			BallotPanel object from the ballots text file.

			@param num Integer to store the number of this unique panel. Necessary to get
			panel details in other parts of the class.
		*/
		public Ballot (String line, int num)
		{
			Scanner sc = new Scanner(line).useDelimiter(":|,|\n");
			ballotID = sc.nextInt();
			panNum = num;

			candidateListener = new  CandidateListenType();

			f = new File(ballotID + ".txt");
			try
			{
				PrintWriter pw = new PrintWriter(f);	
				office = sc.next();

				L = new JLabel(office); //Label for the office that is displayed to the user
				L.setHorizontalAlignment(SwingConstants.CENTER);
				L.setForeground(Color.BLUE);
				L.setFont(new Font(L.getFont().getName(), Font.BOLD,
							L.getFont().getSize()));

				//Loops through each candidate running for this office
				while (sc.hasNext())
				{
					numCandidates++;
					tempName = sc.next(); //Reads in the candidate's name

					temp = tempName+":0"; //Initializes the vote count to 0.

					//Writes the candidate's name & vote count to the result file
					pw.println(temp); 

					//Creates a button for the candidate and adds it to the buttons array
					JButton tempb = new JButton(tempName);
					tempb.addActionListener(candidateListener);
					buttons.add(tempb);
				}

				//Creates the actual office sub-sub-panel and adds the label and candidates
				P = new JPanel(new GridLayout((numCandidates+1), 1));
				P.add(L);
				for(JButton b:buttons)
					P.add(b);

				pw.close(); 
			}
			catch (IOException e)
			{
				JOptionPane.showMessageDialog(null, "The specified ballot file, \""+ 
					f.getName()+",\" was not found.");
			}
		}

		/**
			Returns the JPanel object for this office
			@return Returns the JPanel for this office.
		*/
		public JPanel getPanel()
		{
			return P;
		}

		/**
			Returns the number of candidates running for this office.
			@return Returns an integer that represents the number of candidates running 
			for this office.
		*/
		public int getNumCandidates()
		{
			return numCandidates;
		}

		/**
			Returns the number designation of this particular office's sub-panel
			@return Returns the number designation of this particular office's sub-panel
		*/
		public int getPanelNum()
		{
			return panNum;
		}

		/**
			Returns the ballot ID. This will be used to save the results to file.
			@return Returns the ballot ID. 
		*/
		public int getBallotID()
		{
			return ballotID;
		}

		/**
			Returns the ArrayList containing all of the candidate buttons in this 
			sub-sub-panel. Necessary to reset the panel after voting.
			@return Returns the ArrayList object containing the candidate's buttons. 
		*/
		public ArrayList<JButton> getButtons()
		{
			return buttons;
		}

		/**
			Private implementation of the ActionListener class. This allows the program's 
			buttons to perform actions. The main two functions are indicate when a user
			has selected a candidate for this office, and to mark their selection in the
			"vote" array which will be used to cast their vote later.
		*/
		private class CandidateListenType implements ActionListener
		{
			/**
				Implements the action performed method.
				@param e An action event provided by the component that called the listener 
			*/
			public void actionPerformed(ActionEvent e)
			{
				JButton tempButton = (JButton) e.getSource();

				//Changes the candidate's button's color when a candidate is selected.
				for(JButton tempb: buttons)
				{
					if (tempButton == tempb)
					{	
						if (vote[panNum] != tempButton.getText())
						{	//Marks that the user wanted to select this candidate for office
							vote[panNum] = tempButton.getText();
							tempButton.setForeground(Color.RED);
						}
						else if (vote[panNum]==tempButton.getText())
						{	//Marks that the user didn't want to select any candidates
							//for this office.
							vote[panNum] = null; 
							tempButton.setForeground(Color.BLACK);	
						}
						else 
						{
							vote[panNum] = tempButton.getText();
							tempButton.setForeground(Color.BLACK);
						}
					}
					else //Sets all of the other candidates' buttons to black
						tempb.setForeground(Color.BLACK);
				}					
			}
		}
	}

	/**
		Constructor creates a JPanel for each office (ballot), and a button for every 
		candidate in the specified file. It also will create separate files for each 
		office 	and initialize the corresponding vote counts for each candidate to 0. 
	*/
	public BallotPanel(String fn, VoteInterface V)
	{
		vi = V; 		
		try 
		{	//Opens the specified file containing ballot data	
			File file = new File(fn); 		
			if (file.exists()) //If the file exists, it reads in the data
			{
				Scanner sc = new Scanner(file); 
				numOffices = Integer.parseInt(sc.nextLine());

				new GridLayout(1,(numOffices + 1));
				bals = new Ballot[numOffices]; //Initializes an array of ballot objects

				for(int i = 0; i < numOffices; i++)
				{
					Ballot tempb = new Ballot(sc.nextLine(), i);
					add(tempb.getPanel()); //Adds the office's sub-panel to ballot panel
					bals[i] = tempb; //Saves the office's ballot into an array
				}

				castVote = new JButton("VOTE");
				voteListener = new VoteListenType();
				castVote.addActionListener(voteListener);
				add(castVote);

				vote = new String[numOffices]; //String array to store the user's votes
			}
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, "The specified ballot file, \""+ 
				fn+",\" was not found.");
		}
	}
	
	/**
		Resets the ballots to their default state.
	*/
	public void resetBallots()
	{
		for (String x : vote)
			x = null;

		ArrayList<JButton> tempButtons;
		for (int y = 0; y < numOffices; y++)
		{
			for (JButton z: bals[y].getButtons())
			{
				z.setForeground(Color.BLACK);
			}
		}
		castVote.setForeground(Color.BLACK);

	}


	/**	
		The VoteListenType class implements an action listener that is executed when
		a user wishes to finalize his/her vote. Once the user confirms that his/her vote 
		is correct, the class checks each office to see if the user voted for a candidate 
		running for that particular office. 

		If the user selected a candidate, the listener will open the corresponding office 
		file, designated by <ballotID>.txt, scan for the candidate that the user voted for, 
		and then increment the corresponding vote count by one. It will then safely save 
		the change back to the file.

		If the user did not vote for a candidate running for a particular 
		office, it will skip that particular file. 

		The action listener will execute until all offices have been updated accordingly.
	*/
	private class VoteListenType implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			JButton tempButton = (JButton) e.getSource();
			tempButton.setForeground(Color.RED);

			int tempInt = JOptionPane.showConfirmDialog(null, "Confirm vote?", 
					   "Please Choose One", JOptionPane.YES_NO_OPTION, 
					   JOptionPane.INFORMATION_MESSAGE);
			// if tempInt is 0, then the user wants to submit his/her vote.

			if (tempInt == 0) // Voter confirmed his/her vote
			{
				File originalFile;
				File tempFile;
				PrintWriter writer;
				Scanner sc;
				String input;
				PrintWriter	pw;
				String tempString;
				boolean updated;

				for (int z = 0; z < numOffices; z++)
				{
					if (vote[z]!=null) //The person voted for a candidate in the office
					{	
						try
						{	//Open the specified office file containing vote totals.
							originalFile = new File(bals[z].getBallotID()+".txt"); 

							//creates a scanner to read in the file's contents.
							sc = new Scanner (originalFile).useDelimiter(":|\n");

							//Creates a temporary file to write the update vote count.
							tempFile = new File("_"+bals[z].getBallotID()+".txt");

							/*Creates a PrintWriter object to write to the temp file.*/
							writer = new PrintWriter(tempFile); 

							updated = false; //resets updated to false

							//Reads the original file until it finds the candidate that 
							//the user voted for and copies each line along the way.
							while(sc.hasNext() && updated==false) 
							{
									tempString = sc.next();

									//writes the candidate to tempFile
									writer.print(tempString); 

									//If the candidate matches the user's vote
									if (tempString.equals(vote[z]))
									{
										//increments the vote total by one
										writer.println(":"+(sc.nextInt()+1));
										sc.nextLine();
										updated = true; //Breaks out of this loop
									}
									else //Writes the vote count unchanged
										writer.println(sc.nextLine()); 
							}

							//Then copies all remaining lines unchanged
							while(sc.hasNext()) 
								writer.println(sc.nextLine());
							
							writer.close(); // Closes the temporary file

							originalFile.delete(); //Deletes the original file.

							//Renames the temp file the same as the original file.
							tempFile.renameTo(new File(bals[z].getBallotID()+".txt"));
						}
						catch (IOException ex)
						{
							JOptionPane.showMessageDialog(null, 
								"The specified file, \"" + bals[z].getBallotID() +
								".txt,\" was not found.");
						}
					}
				}

				vi.voted();
			}
			else
				castVote.setForeground(Color.BLACK);
		}
	}
}