import javax.swing.*;
import java.util.*;
import java.io.*;

public class Driver
{
	public static ArrayList<ArrayList<String>> allUsers = new ArrayList<ArrayList<String>>();
	public static int numOfUsers = 0;
	
	/**
	* This is the main method that shows a message dialog box
	* welcoming the user to the system before it calls the 
	* menu method.
	*/
	public static void main(String[] args) throws IOException
	{
		JOptionPane.showMessageDialog(null,"Welcome to our system!");
		menu();
	}
	
	/**
	* menu method gives the user 2 options. They can chose to log in
	* or shut the system down. If they choose to log in the userLogIn
	* method is called otherwise the system is exited.
	*/
	public static void menu() throws IOException
	{
		try
		{
			allUsers = scanUsers();
			numOfUsers = allUsers.get(0).size();
			String options[] = {"Log-In","Shut Down"};
			String choice = (String) JOptionPane.showInputDialog(null,"Choose an option","Log-In Menu",1,null,options,options[0]);
			if(choice.equals(options[0]))
			{ //Log-In
				userLogIn();
			}
			else if(choice.equals(1))
			{ //Shut Down 
				System.exit(0);
			}
		}
		catch(NullPointerException nullException) 
		{
			System.exit(0);
		}
		catch(Exception anException) 
		{
			JOptionPane.showMessageDialog(null,"The system has encountered an unexpected error. Please contact your systems technician. System shutdown initiated.");
			System.exit(0);
		}
	}
	
	/**
	* This method asks the user to enter their email as their username.
	* It then asks for their password. The method then scans through the
	* user text file to see if the username and password are valid. The 
	* user has 3 attempts to enter a valid username and password before
	* the system exits.I a valid details are entered the system then checks
	* the text file to see if the user is an administrator or just a normal 
	* user. If it is an admin the admin menu is called but if it is a user 
	* the user menu is called.
	*/
	public static void userLogIn() throws IOException
	{
		try {
			ArrayList<ArrayList<String>> allUsers = scanUsers();
			boolean found = false;
			String accountType = "";
			int attempts = 0, i = 0, userId = 0;
			
			String userName = JOptionPane.showInputDialog(null,"You must log in to continue\nPlease enter your Email Address:");
			if(userName == null )		System.exit(0);
			String userPassword = JOptionPane.showInputDialog(null,"Please enter your password");
			if(userPassword == null)	System.exit(0);
			
			for(attempts = 0; attempts < 3 && !found; attempts++) 
			{
				if(attempts > 0) 
				{
					JOptionPane.showMessageDialog(null,"ERROR: Invalid user name and password combination. You have "+(3-attempts)+" attempt(s) remaining.");
					userName = JOptionPane.showInputDialog(null,"Please enter your user name");
					if(userName == null )		System.exit(0);
					userPassword = JOptionPane.showInputDialog(null,"Enter your password");
					if(userPassword == null)	System.exit(0);
				}
	
				for(i = 0 ;i < numOfUsers && !found;i++) 
				{
					if ((allUsers.get(1).get(i)).equals(userName) && (allUsers.get(2).get(i)).equals(userPassword))
					{
						found = true;   
						userId = i + 1;
					}
				}
			}
			if(found)
			{
				accountType = allUsers.get(3).get(i-1);
				if (accountType.equals("1"))
					Admin.menu();
				else if (accountType.equals("2"))
				{
					User.menu(userId);
				}
			}
			else {
				System.out.print("Unauthorised access detected. Program shut down initiated.");
				System.exit(1);
			}
		}
		catch(NullPointerException nullException) {
			System.exit(0);
		}
		catch(Exception anException) {
			JOptionPane.showMessageDialog(null,"The system has encountered an unexpected error. Please contact your systems technician. System shutdown initiated.");
			System.exit(0);
		}
	}
	
	/**
	* scanUsers method goes through the user text file and enters the details 
	* into an array list. This array list is then returned.
	*/
	public static ArrayList<ArrayList<String>> scanUsers() throws IOException 
	{
		try
		{
			File fileName = new File("Users.txt");
	
			if(!(fileName.exists()))
			{
				System.out.print("ERROR: File cannot be recovered.");
				System.exit(1);
			}
			else 
			{
				Scanner userScanner = new Scanner(fileName);
			
				ArrayList<ArrayList<String>> allUsers = new ArrayList<ArrayList<String>>();
				allUsers.add(new ArrayList<String>());
				allUsers.add(new ArrayList<String>());
				allUsers.add(new ArrayList<String>());
				allUsers.add(new ArrayList<String>());
				String[]splitArray;
			
				while(userScanner.hasNext()) {
					splitArray = (userScanner.nextLine()).split(",");
					allUsers.get(0).add(splitArray[0]);
					allUsers.get(1).add(splitArray[1]);
					allUsers.get(2).add(splitArray[2]);
					allUsers.get(3).add(splitArray[3]);
				}
				return allUsers;
			}
			return null;
		}
		catch(NullPointerException nullException) 
		{
			System.exit(0);
		}
		catch(Exception anException) 
		{
			JOptionPane.showMessageDialog(null,"The system has encountered an unexpected error. Please contact your systems technician. System shutdown initiated.");
			System.exit(0);
		}
		return null;
	}
}