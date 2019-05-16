
/*
	Nicolas Leo, nll21
	CS 401 Fall 2017 Assignment 4
*/

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
	This class simulates an electronic voting machine. It has the following capabilities:
	1) 	It can accept an arbitrary number of ballots, each with an arbitrary number of 
		candidates, and allow users to vote for one candidate for each ballot.
	2)  It allows users to vote only if they are "registered" and allows them to vote only 
		one time.
	3)	It keeps track of the number of votes for each candidate in each office in a 
		well-organized way, by associating a file name with each ballot.
	4) 	It updates all data files (vote count files and voter file) as soon as possible and 
		in a way that (mostly) protects the data if the program crashes during the update.
*/
public class Assig4 implements LoginInterface, VoteInterface
{
	// A "one line" main here just creates a Assig4 object
	public static void main(String [] args) throws IOException
	{
		new Assig4(args);
	}

	private ActionListener theListener; //Listener to keep track of action events
	private BallotPanel ballots;	/*Ballot panel to display the candidates running
									in the election and allows the user to vote*/
	private JButton startButton,	/*Button to initialize the program. It is also
									displayed after the user finishes voting.*/
					login;			/*Button that opens a login window so that a user
									can login to the program*/
	private JFrame theWindow;		//Main window for displaying the components.
	private JLabel welcomeMsg;		/*Label that displays welcome messages to the user.
									this JLabel is reused/updated as the program is run*/
	private LoginPanel logPan;		//Login panel to allow the user to login to the program.
	private String voterFile, 		//File name of a file that contains registered voters
				ballotFile;			//File name of a properly formatted ballot file.
	private Voter P;				//Voter objects are used to keep track of voter details

	/**
		Constructor for an Assig4 object. The object is the main driver of the voting
		program and implements both the LoginInterface and VoteInterface classes. It allows
		a user to graphically login to the program, vote, and saves the results back in a 
		safe manner.
		@param s Array of strings containing 2 arguments passed in through the command line.
		The first argument is the path of a properly formatted voters file, the second is 
		the path of a properly formatted ballots file. 
	*/
	public Assig4(String[] s)
	{
		voterFile = s[0];	//Stores the name of the properly formatted voter file.
		ballotFile = s[1];	//Stores the name of the properly formatted ballot file.

		/*Initializes the login panel using the voter file. The Assig4 object is passed
		as an argument so that the login can eventually call the setVoter method listed
		below */
		logPan = new LoginPanel(voterFile, Assig4.this); 

		/*Initializes the ballot panel using the ballot file. The Assig4 object is passed
		as an argument so that the ballot can eventually call the voted method listed
		below */
		ballots = new BallotPanel(ballotFile, this);

		//Creates a new listener object to get new action events
		theListener = new MyListener();	

		theWindow = new JFrame("CS 401 E-Vote v. 1.0"); 
		theWindow.setLayout(new GridLayout(2,1));
		
		welcomeMsg = new JLabel("Welcome to E-Vote"); //Initializes the welcome message
		welcomeMsg.setFont(new Font("Serif", Font.ITALIC, 30));
		welcomeMsg.setHorizontalAlignment(SwingConstants.CENTER);

		login = new JButton("Click to Login"); //Initializes the login button
		login.setFont(new Font("Serif", Font.BOLD, 30));
		login.addActionListener(theListener);
		
		startButton = new JButton("Click to Vote"); //Initializes the start button
		startButton.setFont(new Font("Serif", Font.BOLD, 30));
		startButton.addActionListener(theListener);

		//Allows the user to terminate the program by using the standard window close button
		theWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Add the initial set of components and displays the main program window.
		theWindow.add(welcomeMsg);
		theWindow.add(login);
		theWindow.pack();
		theWindow.setVisible(true);
	}

	/**
		This method enables the login panel to pass focus back to the main program. 
		Program focus stays on the login panel until a registered voter who has not yet voted
		is found. Once this method is called, the LoginPanel is removed from the display and 
		the user is given the chance to see the ballot of candidates.

		@param newVoter Voter object passed back from the Login Panel if a registered
		voter is found and he/she has not yet voted.
	*/
	public void setVoter(Voter newVoter)
	{
		P = newVoter;
		theWindow.remove(logPan);
		welcomeMsg.setText("Welcome " + P.getName());

		theWindow.add(welcomeMsg);
		theWindow.add(startButton);
		theWindow.pack();
		theWindow.setVisible(true);
	}

	
	/**
		This method is be called from the ballot panel to indicate that the voter has
		finished finalizing their votes and wishes to submit them. When the method executes
		the ballot panel is removed from the window and the program is reinitialized. This
		allows a new voter to login and being the voting process.
	*/
	public void voted()
	{
		P.vote(); //Calls the vote method to indicate that the user voted.
		Voter.saveVoter(voterFile, P); //Safely saves the changes to the voters file.

		theWindow.remove(ballots); //Removes the ballot panel from the main window
		welcomeMsg.setText("");
		theWindow.pack();

		JOptionPane.showMessageDialog(null, P.getName() + " thanks for voting!");

		welcomeMsg.setText("Welcome to E-Vote"); //Resets the welcome message
		theWindow.add(login);
		theWindow.pack();
	}
	
	/**
		Private implementation of the ActionListener class. This allows the program's 
		buttons to perform actions. The main two functions are to allow the user to 
		navigate to the login pane, and to navigate to the ballot pane.
	*/
	private class MyListener implements ActionListener
	{
		/**
			Implements the action performed method.
			@param e An action event provided by the component that called the listener 
		*/
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == login)
			{
				theWindow.remove(login);
				theWindow.add(logPan);
				theWindow.pack();
			}
			else if (e.getSource() == startButton)
			{
				welcomeMsg.setText(P.getName() + " please make your choices.");
				theWindow.remove(startButton);
				theWindow.add(ballots);
				ballots.resetBallots();
				theWindow.pack();
			}
		}
	}
}




