import javax.swing.*;
import java.util.*;
import java.io.*;

public class User
{	
	public static int userId = 0;
	
	/**
	* This menu method is the menu for the user. It gives them the options 
	* they are allowed to use in a drop down list. The user is allowed to 
	* pick one of three options and then the relevant methods are called
	* from other classes.
	*/
	public static void menu(int Id) throws IOException
	{
		try
		{
			userId = Id;
			String options[] = {"View Account","View Booking(s)","Log-Out"};
			String choice = (String) JOptionPane.showInputDialog(null,"Choose an option","Menu",1,null,options,options[0]);
			if(choice.equals(options[0]))  //View Account
			{
				Bookings.viewAccount(userId,2);
			}
			else if(choice.equals(options[1])) //View Bookings
			{ 
				Bookings.viewBookings(userId,2);
			}
			else if(choice.equals(options[2]))  //Log-Out
			{ 
				Driver.menu();
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
}
	