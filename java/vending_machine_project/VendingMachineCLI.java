import java.util.*;
import java.io.*;
public class VendingMachineCLI {
	private VendingMachine machine;
	private static Coin[] coins;
	
	/**
		This method initialises the vending machine and allows the user to select which Operation Mode they want to use
		@throws IOException e
	*/
	public void run() throws IOException {
		boolean more = true;
		Scanner in = new Scanner(System.in);
		machine = new VendingMachine();
		coins = ((ReadWrite.scanCoins()).toArray(new Coin[0]));
		
		while(more) {
			System.out.println("Please choose Operation Mode: U)ser Mode O)perator Mode Q)uit");
			String input = in.nextLine().toUpperCase();
					
			if(input.equals("U")) {
				userMenu();
				more = false;
			}
			else if(input.equals("O")) {
				operatorValidation();
				more = false;
			}
			else if(input.equals("Q")) {
				more = false;
			}
		}
	}
	
	/**
		The userMenu allows the user to choose an option from a menu. The user can Show Products, Insert Coin, Buy Product, Remove Coins or Quit. These options
		then call a method from the VendingMachine class
		@throws IOException e
	*/
	public void userMenu() throws IOException {
		boolean more = true;
		Scanner in = new Scanner(System.in);
      
		while (more)
		{ 
			System.out.println("S)how products  I)nsert coin  B)uy   R)emove coins  Q)uit");
			String command = in.nextLine().toUpperCase();

			if (command.equals("S"))
			{  
				for (Product p : machine.getProductTypes())
					System.out.println(p);
			}
			else if (command.equals("I"))
			{ 
				System.out.println(machine.addCoin((Coin) getChoice(coins)));
			}
			else if (command.equals("R"))
			{  
				System.out.println("Removed: " + machine.removeMoney());
			}
			else if (command.equals("B"))
			{              
				try
				{
					Product p = (Product) getChoice(machine.getProductTypes());
					machine.buyProduct(p);
					System.out.println("Purchased: " + p);
				}
				catch (VendingException ex)
				{
					System.out.println(ex.getMessage());
				}
			}
			else if (command.equals("Q"))
			{ 
				more = false;
			}
        }
	}
	
	/**
		This is the operator menu which allows the operator to Add Products, Remove Items from the machine or Quit
		@throws IOException e
	*/
	public void operatorMenu() throws IOException {
		boolean more = true;
		Scanner in = new Scanner(System.in);
		
		while(more) {
			System.out.println("A)dd Product R)emove Money Q)uit");
			String input = in.nextLine().toUpperCase();
			
			if(input.equals("A")) {
				System.out.println("Description:");
				String description = in.nextLine();
				System.out.println("Price:");
				double price = in.nextDouble();
				System.out.println("Quantity:");
				int quantity = in.nextInt();
				in.nextLine(); // read the new-line character
				machine.addProduct(new Product(description, price), quantity);
			}
			else if(input.equals("R")) {
				System.out.println("Pick which type of coin you would like to remove: ");
				Coin c = ((Coin) getChoice(coins));
				try {
					int quantity = machine.removeMoneyOp(c);
					System.out.println("You have removed "+(quantity*(c.getValue()))+" euro from the machine.");
				}
				catch(VendingException e) {
					System.out.println(e.getMessage());
				}
			}
			else if(input.equals("Q")) {
				more = false;
			}
		}
	}
	
	/** This method reads in input (the operator PINS) from the operators.txt file and then asks the Operator to enter their Pin. If it matches one
		of the existing pins, the Operator can proceed to the Operator Menu
		@throws IOException e
	*/
	public void operatorValidation() throws IOException {
		String pattern = "[0-9]{4}";
		ArrayList<Integer> passcodes = ReadWrite.scanIntegers("operators.txt");
		Scanner aScanner = new Scanner(System.in);
		System.out.println("Please enter your operator passcode: ");
		String input = aScanner.nextLine();
		
		if(!(input.matches(pattern))) {
			System.out.println("ERROR: Invalid input detected. Please enter your four digit operator code.");
			operatorValidation();
		}
		else {
			int opInput = Integer.parseInt(input);
			if(passcodes.indexOf(opInput) != -1)
				operatorMenu();
			else
				System.out.println("ERROR: Incorrect PIN entered.");
		}
	}
	
	private Object getChoice(Object[] choices)
    {
		Scanner in = new Scanner(System.in);
		while (true)
		{
			char c = 'A';
			for (Object choice : choices)
			{
				System.out.println(c + ") " + choice); 
				c++;
			}
			String input = in.nextLine();
			int n = input.toUpperCase().charAt(0) - 'A';
			if (0 <= n && n < choices.length)
			return choices[n];
		}      
    }
}