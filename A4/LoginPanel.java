/*
	Nicolas Leo, nll21
	CS 401 Fall 2017 Assignment 4
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
	This class extends the JPanel class. It allows the main voting program to display 
	instructions, buttons, and a text field to the user and allow them to login to the 
	voting machine.
*/
public class LoginPanel extends JPanel
{
	private String voterFile;	//String to store the path of the voters file.
	private LoginInterface login; //Login to store the main program interface
	private JButton b1;		/*Button to allow the user to submit a voter ID 
							and search the voter file.*/
	private JLabel l1, l2; 	//Labels to display information to the user.
	private JTextField t1; 	//Text field for user input.
	private Voter v;		//Voter object to store the voter's details
	private LoginListener theListener; //Listener to get action events
	private JPanel middle; //Panel to store contents in the middle of the frame
	/**
		Constructor for creating a login panel
		@param fn String of the path where a properly formatted voter file is located
		@param L LoginInterface object. Should be the main program interface
	*/
	public LoginPanel(String fn, LoginInterface L)
	{
		voterFile = fn; //stores the voter file name in the Login panel object
		login = L;		//stores the LoginInterface object for later use

		setLayout(new GridLayout(3,1));

		theListener = new LoginListener(); 

		b1 = new JButton("Submit"); //Button to allow the user to submit and search
		//the voter file using their ID number
		b1.setHorizontalAlignment(SwingConstants.CENTER);
		b1.setFont(new Font("Serif", Font.PLAIN,30));
		b1.addActionListener(theListener);

		l1 = new JLabel("Please log into the site");
		l1.setHorizontalAlignment(SwingConstants.LEFT);
		l1.setFont(new Font("Serif", Font.BOLD, 30));
		
		l2 = new JLabel("Voter ID:");
		l2.setHorizontalAlignment(SwingConstants.LEFT);
		l2.setFont(new Font("Serif",Font.BOLD, 30));

		t1 = new JTextField();
		t1.setHorizontalAlignment(SwingConstants.RIGHT);
		t1.setFont(new Font("Serif", Font.PLAIN, 30));

		middle = new JPanel (new GridLayout(1,2));
		
		add(l1);
		add(middle);
		middle.add(l2);
		middle.add(t1);
		add(b1);
	}
		
	/**
		Private implementation of the ActionListener class. This allows the program's 
		buttons to perform actions. The main two functions are to allow the user to 
		search the voter file for the voter ID they entered in the text field, and 
		if a registered voter is found, and they have not yet voted, it will 
		return focus to the main program allowing the user to vote.
	*/
	private class LoginListener implements ActionListener
	{
		/**
			Implements the action performed method.
			@param e An action event provided by the component that called the listener 
		*/
		public void actionPerformed(ActionEvent e)
		{
			v = Voter.getVoter(voterFile, t1.getText()); //Searches the voter file
			//for the specified voter ID number found the in the text field.

			if (v == null) //If the voter isn't found it alerts the user.
				JOptionPane.showMessageDialog(null, "You are not registered to vote.");
			else
			{	//If the user already voted, it prevents them from voting again
				if (v.hasVoted()) 
					JOptionPane.showMessageDialog(null, v.getName()+ 
						", you already voted.");
				else
				{
					login.setVoter(v); 	//If the registered voter hasn't voted, focus is
										//returned to the main program.
				}
			}
			
			t1.setText(null); //resets the ID field so that a new voter ID can be entered
			t1.requestFocus();
		}
	}
}
