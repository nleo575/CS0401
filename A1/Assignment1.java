/**	Nicolas Leo, nll21
	CS 401 Fall 2017
	Assigment 1

	The purpose of this program is to simluate shopping in a Quidditch supply 
	shop in the fictional Harry Potter universe. The program will ask the user 
	which items that they would like to buy from the store, ask for payment,
	and then give them change back. 
*/
import java.util.Scanner; //Scanner is used for all data entry

public class Assignment1
{
	public static void main(String[] args)
	{
		System.out.print("\033[H\033[2J"); //clears the console window

		final String PASSWORD = "lumos maxima"; //Password for discount
		int selection = 0,	//Customer selection
			quaffles = 0,	//# quaffles to purchase
			broomstickSK = 0; 	//# of broomstick service kits to purchase
		int[] pins = {0,0,0,0}; /*Array containing # of Gryffindor, 
			Slytherin, Hufflepuff, and Ravenclaw house pins (respectively) to purchase */
		int[] prices = {0, 20, 20, 145, 638, 986};
			/*	
				Array to keep track of the prices. 
				Index 0= discount y/n (1/0), 1=house pins qty <10 cost (knuts), 
				2= house pins qty >=10 cost (knuts), 3=quaffles qty <5 cost (knuts), 
				4=quaffles (boxes of 5) cost (knuts), 
				5=broomstick service kits cost (knuts)
			*/
		Scanner keyboard = new Scanner(System.in); //Scanner to hold keyboard input
		String input; //String to hold customer's input from the keyboard
		char customer; //Char to hold if there a customer in line (y/n)
		
		//Opening program greeting.
		System.out.println("Hello and welcome to the Hogsmeade "+
							"Quality Quidditch Supplies!");
		customer = greeting(); //calls the greeting method 
		while (customer == 'Y')
		{	
			//This loops prompts the user the password. 
			//If the user gusses the password, they receive a discount.
			for(int i =0; i < 2 && prices[0] <1; i++)
			{
				System.out.print("What is the password? ");
				input = keyboard.nextLine();
				
				if (input.equalsIgnoreCase(PASSWORD))
				{
					prices[0] = 1; //Password was correct & discount applied.
					i = 1; // exits the loops
				}
				else
				{
					System.out.println("\nSorry but that is not correct.\n"+
							"We will give you one more chance.\n");
				}
			}
			
			System.out.print("\033[H\033[2J"); //clears the console window
			
			if (prices[0] == 0) 
				prices = (normalPrices()).clone();
			else
				prices = (discountPrices()).clone();
			
			while (selection !=5) //This loop runs until the user decides to checkout.
			{
				selection = options(); //Gets the customer action selection
				System.out.print("\033[H\033[2J"); //clears the console window
				
				if (selection == 1) //Update pins order
				{
					pins = (updatePins(pins, prices)).clone();
					System.out.print("\033[H\033[2J"); //clears the console window
				}
				else if (selection ==2) //Update quaffles order
				{
					quaffles = updateQuaffles(quaffles, prices);
				}
				else if (selection == 3) //Update Broomstick SK order
				{
					broomstickSK = updateBroomstick(broomstickSK, prices);
				}
				else if (selection == 4) //Displays appropiate prices
				{
					if (prices[0] == 1)
						discountPrices();
					else
						normalPrices();
				}		
			}
			
			//Checkout & payment sequence
			
			int totalNumPins = pins[0]+pins[1]+pins[2]+pins[3];
			
			System.out.println("Here is your subtotal:");
			
			if ((totalNumPins + quaffles + broomstickSK) == 0)
				System.out.println("\tNo items purchased. Thanks anyway for stopping!");
			else
			{
				int totalCost = 0;
				
				//House pins subtotal section
				if (totalNumPins > 0)
				{
					System.out.print("\t"+totalNumPins + " House Pins at ");
					if (totalNumPins <10)
					{
						System.out.println(prices[1]+" Knuts ea.:\t\t\t"+
							prices[1]*totalNumPins);
						totalCost = prices[1]*totalNumPins;
					}
					else
					{
						System.out.println(prices[2]+" Knuts ea.:\t\t\t"+
							prices[2]*totalNumPins);
						totalCost = prices[2]*totalNumPins;
					}
					
					if (pins[0]>0)
						System.out.println("\t\t\t"+ pins[0] + " Gryffindor");
					if (pins[1]>0)
						System.out.println("\t\t\t"+ pins[1] + " Slytherin");
					if (pins[2]>0)
						System.out.println("\t\t\t"+ pins[2] + " Hufflepuff");
					if (pins[3]>0)
						System.out.println("\t\t\t"+ pins[3] + " Ravenclaw");
				}
				
				//Quaffles subtotal section
				if (quaffles >0)
				{
					int quafflesBoxes, quafflesSingles = quaffles%5;
					if (quaffles >=5)
					{
						quafflesBoxes = quaffles/5;
						System.out.println("\t"+quafflesBoxes + " box(es) of Quaffles at "+
							prices[4] + " Knuts per box:\t"+ prices[4]* quafflesBoxes);
						totalCost+=prices[4]*quafflesBoxes;
					}
					
					if (quaffles %5 >0)
					{
						System.out.println("\t"+quafflesSingles + " Quaffles at "+
							prices[3] + " Knuts each:\t\t\t"+ prices[3]* quafflesSingles);
						totalCost+=prices[3]*quafflesSingles;
					}
				}

				//Broomstick service kits subtotal section
				if (broomstickSK > 0)
				{
					System.out.println("\t"+ broomstickSK + " Broomstick Service kits "+
						"at " + prices[5]+ " each:\t\t" +	prices[5]*broomstickSK);
					totalCost+=prices[5]*broomstickSK;
				}
				
				System.out.println("\t\t\t\t\t\t\t----\n\tTotal:\t\t\t\t\t\t" + totalCost);
				
				int discount = 0;
				int totalCostBeforeBonus = totalCost;
				if (totalCost >= 1479 && prices[0]==1)
				{
					discount = totalCost/10;
					if (totalCost%10>=5)
						discount++;
					System.out.println("\tBonus discount of 10%\t\t\t\t-" + discount);
					totalCost-=discount;
					System.out.println("\t\t\t\t\t\t\t----\n\tNew Total:\t\t\t\t\t" + totalCost);
				}
				
				//This section add an intemized discount after the "new total" section 	
				//for people who use the discount code.
				if (prices[0]==1)
				{
					int savings = 0;
					
					System.out.println("\n\tUsing your discount you saved:");
					if (totalNumPins > 9)
					{
						int x = 2*(totalNumPins);
						savings += x;
						System.out.println("\t\t"+ x + " Knuts on House Pins");
					}
					
					if (quaffles > 4)
					{
						int x = 58*(quaffles/5);
						savings += x;
						System.out.println("\t\t"+ x + " Knuts on boxes of Quaffles");
					}
					
					if (broomstickSK > 0)
					{
						int x = 87*broomstickSK;
						savings += x;
						System.out.println("\t\t"+ x + " Knuts on Broomstick Service Kits");
					}
					
					if (totalCost >= 1479)
					{
						discount = totalCostBeforeBonus/10;
						if (totalCostBeforeBonus%10>=5)
							discount++;
						System.out.println("\tSince your total bill exceeded 1478 Knuts,\n"+
							"\tyou qualified for a 10% overall discount of " + discount + " Knuts");
						savings+=discount;
					}
					
					System.out.println("\tYour saved " + savings + " Knuts in total with your discount.");
					
				}
				
				
				//Payment section.
				System.out.println("\nPlease enter a payment amount in the following format:\n"+
        								"\t<amount><space><currency>\n"+
               								"\t\tWhere <amount> = an integer\n"+
                							"\t\tWhere <space> = a blank space\n"+
                							"\t\tWhere <currency> = {Knuts, Sickles, Galleons}\n"+
        								"\tYou may enter as many times as you like. Each entry will be\n"+
        								"\tadded to your total until sufficient funds have been obtained.\n"+
      									"\tRecall the currency exchange:\n"+
                							"\t\t29 Knuts = 1 Sickle\n"+
                							"\t\t493 Knuts = 17 Sickles = 1 Galleon");
				int totalPaid = 0; //keeps track of the toal paid by the user.
				char currency; //Holds type of currency entered by the user.
				while (totalPaid < totalCost) //Loops until the total paid >= total cost
				{
					System.out.print("\n\tPayment: ");
					selection = keyboard.nextInt(); //reused selection variable for 
											//amount of currency entered by the user
					currency = Character.toLowerCase(keyboard.next().charAt(0));
					if (currency == 'k' || currency == 's' || currency == 'g' || selection < 0)
					{
						if (currency == 's') //converts Sickles to Knuts
							selection *=29;
						else if (currency == 'g') //converts Galleons to Knuts
							selection *=493;
						
						System.out.println("\t\tYou have added "+ selection +
														" Knuts to your total");
						totalPaid +=selection;
						
						System.out.println("\t\tYou have paid "+totalPaid+
							" out of " + totalCost +" Knuts");
						
						if (totalPaid < totalCost)
							System.out.println("\t\tYou still owe " + (totalCost - totalPaid)
								+ " Knuts");
					}
					else //invalid currency type entered
						System.out.println("\tInvalid amount or currency type.");
				}
				
				System.out.println("\n\tThank you!");
				
				//Calculate change
				if (totalPaid > totalCost)
				{
					int change = totalPaid - totalCost;
					System.out.println("\tYou have overpaid by " + change +
						" Knuts\n\tHere is your change:\n\t\t");
					if (change >=493) //Calculates Galleons to return in change
					{
						System.out.println("\t\t"+change/493 + " Galleons");
						change %=493;
					}
					if (change >=29) //Calculated Sickles to return in change
					{
						System.out.println("\t\t"+change/29 + " Sickles");
						change %=29;
					}
					
					if (change <29)
						System.out.println("\t\t"+change + " Knuts\n\n"+
						"\tThank you for shopping with us!");			
				}	
			}
			
			
			//Restarts the loop and asks if there is another customer.	
			customer = greeting();
			
			if (customer == 'Y')
			{
				System.out.print("\033[H\033[2J"); //clears the console window
				selection = 0; 		//resets cusomter selection for new customer.
				prices[0] = 0; 		//resets discount for new customer.
				selection = 0;		//resets customer selection.
				quaffles = 0;		//resets quaffles for new customer.
				broomstickSK = 0; 	//resets broomstick SK for new customer
				int [] x = {0,0,0,0};
				pins = x.clone(); 	//resets pins purchase order for new customer
				int [] y = {0,0,0,0,0,0};
				prices = y.clone(); //resets prices for new customer
			}
		}
		

	}
	
	/** 
		The discountPrices method displays the discount prices if the customer enters 
		the correct password.		
		@return The array x will contain the discounted prices and can be used to
				change the content of the prices array used in other parts of the 
				program.		
	*/
	private static int[] discountPrices()
	{
		System.out.println("Welcome Dumbledore's Army member!\n"+
			"You qualify for the following specials:\n"+
				"\tDiscount on 10 or more House Pins\n"+
				"\tDisount on Boxes of Quaffles\n"+
				"\tDiscount on Broomstick Service Kits\n"+
				"\tOverall discount of 10% off over & above any other discounts if the\n"+
				"\toverall order (after other discounts) is 3 Galleons\n"+
				"\tor more, rounded to the nearest Knut.\n\n"+
			"Here is our discounted price list:\n"+
				"\tHouse Pins (each)\t\t20 Knuts\n"+
						"\t\tAvailable in Gryffindor, Slytherin, HufflePuff"+
						" and Ravenclaw\n"+
						"\t\t[only 18 Knuts if you buy 10 or more]\n"+
				"\tQuaffles (each)\t\t\t145 Knuts\n"+
					        "\tQuaffles (box of 5)\t\t20 Sickles (580 Knuts)\n"+
					        "\tBroomstick Service Kits (each)\t31 Sickles (899 Knuts)\n");
		
		/*	Array of the discount prices. 
			Index 0= discount y/n (1/0), 1=house pins qty <10 cost (knuts), 
			2= house pins qty >=10 cost (knuts), 3=quaffles qty <5 cost (knuts), 
			4=quaffles (boxes of 5) cost (knuts), 
			5=broomstick service kits cost (knuts) */
		int[] x = {1, 20, 18, 145, 580, 899};

		return x;
	}
	
	
	/** 
		The greeting method displays the opening greeting to new customers.
		@return Returns y if there is a customer, n if there isn't a customer.	
	*/
	private static char greeting()
	{
		System.out.print("\nIs there a customer in line? (Y/N) ");
		Scanner keyboard = new Scanner(System.in); //Scanner for text entry.
		char c = Character.toUpperCase((keyboard.nextLine()).charAt(0));
		boolean isYN; //Tests if Y or N was entered by the user.
		if (c == 'Y' || c == 'N')
			isYN = true;
		else
			isYN = false;
		while (isYN ==false)
		{	//If an invalid character is entered, the user is prompted to try again.
			System.out.println("You hae entered an invalid character,\n"+
								"Please try again.");
			c = Character.toUpperCase((keyboard.nextLine()).charAt(0));
			if (c == 'Y' || c == 'N')
				isYN = true;
		}
		System.out.println();
		return c; 
	}
	
	/** 
		The normalPrices method displays the normal prices if the customer enters 
		an incorrect password.		
		@return The array x will contain the normal prices and can be used to
				change the content of the prices array used in other parts of the 
				program.		
	*/
	private static int[] normalPrices()
	{
		System.out.println("Please enjoy our items at their regular prices.\n\n"+
			"Here is our price list:\n"+
				"\tHouse Pins (each)\t\t20 Knuts\n"+
				"\t\tAvailable in Gryffindor, Slytherin, HufflePuff"+
					" and Ravenclaw\n"+
		        "\tQuaffles (each)\t\t\t145 Knuts\n"+
		        "\tQuaffles (box of 5)\t\t638 Knuts\n"+
		        "\tBroomstick Service Kits (each)\t986 Knuts\n");
		
		/*	Array of the noral prices. 
			Index 0= discount y/n (1/0), 1=house pins qty <10 cost (knuts), 
			2= house pins qty >=10 cost (knuts), 3=quaffles qty <5 cost (knuts), 
			4=quaffles (boxes of 5) cost (knuts), 
			5=broomstick service kits cost (knuts) */
		int[] x = new int[] {0, 20, 20, 145, 638, 986};

		return x;
	}
	
	/**
		The options method displays the available actions that the customer can do
		while at the store.
		@return Returns the cusotmer selection so the program can proceed.
	*/	
	private static int options()
	{	
		System.out.println("Please choose an option:\n"+
		        "\t1) Update House Pins Order\n"+
		        "\t2) Update Quaffles Order\n"+
		        "\t3) Update Broomstick Kits Order\n"+
		        "\t4) Show price list\n"+
		       	"\t5) Check Out");
		Scanner keyboard = new Scanner(System.in);
		int x;
		x = keyboard.nextInt();
		
		while (x<1 || x > 5) //Loop to verify that a valid selection was made.
		{
			System.out.print("Please enter a valid selection. ");
			x = keyboard.nextInt();
		}
		
		return x;		
	}
	
	
	/**
		The pinsSelection method displays the house pins available to purchase.
	*/
	public static void pinsSelection()
	{
		System.out.println("\nFor which team would you like to order pins?\n"+
							        "\t1) Gryffindor\n"+
							        "\t2) Slytherin\n"+
							        "\t3) Hufflepuff\n"+
							        "\t4) Ravenclaw\n"+
							        "\t5) Finished with Pin order");
	}
	
	/**
		The updateBroomstick method displays the current broomstick service kit
		order and the appropriate price, and then askes the user if they would 
		like to update their order.
		@param brooms hold the current broom order.
		@param prices Array the of current prices. The value is dependent upon whether 
					  the user correctly entered the discount password at the beginning 
					  of the program loop.
		@return Returns the number of brooms that the user would like to purchase
	*/
	public static int updateBroomstick (int brooms, int[] prices)
	{
		System.out.println("Here is your current order:");
		if (brooms >0)
			System.out.println(brooms + " Broomstick Service Kits ordered.");
		else	
			System.out.println("\tNo Broomstick Service Kits ordered.");
		
		System.out.print("\nHow many Broomstick Service Kits would you like for " +
		        prices[5]+ " Knuts each? ");
		
		Scanner kb = new Scanner(System.in);
		brooms = kb.nextInt();
		
		if (brooms <0) //input validation for negative numbers
		{
			brooms = 0;
			System.out.println("Negative number taken as 0.");
		}
		else
			System.out.print("\033[H\033[2J"); //clears the console window
		return brooms;
	}
	
	/**
		The updatePins method displays the current house pins order and the 
		current appropriate price (based on # of pins and discount), and then 
		askes the user if they would like to update their order.
		@param pins hold the current pins order.
		@param prices Array the of current prices. The value is dependent upon whether 
					  the user correctly entered the discount password at the beginning 
					  of the program loop.
		@return Returns the array of the number of pins that the user would like 
				to purchase
	*/
	public static int[] updatePins (int[] pins, int[] prices)
	{
		int selection = 0, //holds the customer's selection 
			sum = 0;//holds the total # of pins ordered
		Scanner kb = new Scanner(System.in);

		while (selection !=5)
		{
			System.out.println("Here is your current order:");
			sum = pins[0]+pins[1]+pins[2]+pins[3];
			if (sum >0)
			{	
				System.out.println("\t"+ sum + " Team Pins:");
			
				if(pins[0]>0)
					System.out.println("\t\t"+pins[0]+" Gryffindor");
				if(pins[1]>0)
					System.out.println("\t\t"+pins[1]+" Slytherin");
				if(pins[2]>0)
					System.out.println("\t\t"+pins[2]+" Hufflepuff");
				if(pins[3]>0)
					System.out.println("\t\t"+pins[3]+" Ravenclaw");
			}
			else	
				System.out.println("\tNo Team Pins ordered.");				
						
			pinsSelection(); //Prints the selection of vailable pins.
			selection = kb.nextInt();
			
			//Asks the user how many of the specific type of pin they 
			//would like to purchase
			if (selection >0 && selection < 5)
			{	
				System.out.print("\nHow many ");
			
				if (selection == 1)
				{
					System.out.print("Gryffindor");
				}
				else if (selection ==2)
				{
					System.out.print("Slytherin");
				}
				else if (selection == 3)
				{
					System.out.print("Hufflepuff");
				}
				else
				{
					System.out.print("Ravenclaw");
				}
				
				System.out.print(" pins would you like? ");
				
				int tempNum = kb.nextInt(); //used to hold customer input
				
				if (tempNum <0) //Checks to see if a negative number was entered
				{
					System.out.println("Negative number taken as 0.");
					tempNum = 0;
				}
				pins[selection-1]= tempNum; //Assigns the customer input to the array
				/*Array containing # of Gryffindor, 
				Slytherin, Hufflepuff, and Ravenclaw house pins (respectively) to 
				purchase */
			}
			
			if (selection != 5)
			{
				sum = pins[0]+pins[1]+pins[2]+pins[3]; //Caclulates updated # of pins
				System.out.print("\033[H\033[2J"); //clears the console window
				
				if (prices[0]==1 && sum >9) //Checks to see whether the user purchased 10
					//or more pins and the correct discount password.
					System.out.println("Congratulations! You have earned the discount "+
					"price of 18 Knuts.");
				else
					System.out.println("You will pay the regular price of "+ prices[1]+
					" Knuts each.");
			}
		}	
		
		System.out.println();			
		return pins;					
	}
	
	/**
		The updateQuaffles method displays the current quaffles order and the 
		appropriate price, and then askes the user if they would like to update their order.
		@param q Holds the current quaffles order.
		@param prices Array the of current prices. The value is dependent upon whether 
					  the user correctly entered the discount password at the beginning 
					  of the program loop.
		@return Returns the number of quaffles that the user would like to purchase.
	*/
	public static int updateQuaffles (int q, int[] prices)
	{
		System.out.println("Here is your current order:");
		if (q >0)
		{	
			if (q >=5)
			{
				System.out.println("\t" + (q/5) + " boxes of Quaffles");
				
				if (q%5 >0)
					System.out.println("\t" + (q%5) + " individual Quaffles");
			}
			else
				System.out.println(q+" individual Quaffles");
		}
		else	
			System.out.println("\tNo Quaffles ordered.");
		
		System.out.println("\nHow many Quaffles would you like for:\n" +
		        "\t"+prices[3]+ " Knuts each\n"+
		        "\t"+prices[4]+ " Knuts per box of 5\n" +
		"(Please indicate only the total number you would like)");
		
		Scanner kb = new Scanner(System.in);
		q = kb.nextInt();
		
		if (q <0) //Checks to see if a valid number was entered.
		{
			q = 0;
			System.out.println("Negative number taken as 0.\n");
		}
		else
			System.out.print("\033[H\033[2J"); //clears the console window
		
		return q;
	}
}