import javax.swing.*;
import java.util.*;
import java.io.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;

public class Admin {
	public static ArrayList<Facilities> facilities = new ArrayList<Facilities>();
	public static int numOfFacilities = 0;
	
	
	/** 
	* The menu method allows the admin to pick what they would like to do,
	* it does this by providing a drop down menu where the admin has 4 options.
	* These 4 options then bring the admin to separate drop down menus based on 
	* what they chose. It then calls the valid methods that are needed to carry 
	* out the function of the chosen option.
	*/
	public static void menu() throws IOException {
		try {
			facilities = Facilities.scanFacilities();
			numOfFacilities = facilities.size();
			String[]mainOptions = {"Register a User","Facility Functions","Bookings and Accounts","Log-out"};
			String choice = (String) JOptionPane.showInputDialog(null,"Choose an option","Main Menu",1,null,mainOptions,mainOptions[0]);
			if(choice.equals(mainOptions[0]))
				registerUser();
			else if(choice.equals(mainOptions[1])) {
				String[]facilityOptions = {"Add a facility","Delete a facility","Decommission a facility","Recommission a facility","Check facility availability","Return to main menu"};
				choice = (String) JOptionPane.showInputDialog(null,"Choose an option","Facilities Menu",1,null,facilityOptions,facilityOptions[0]);
			
				if(choice.equals(facilityOptions[0])) //Add Facility
					addFacility();
				else if(choice.equals(facilityOptions[1])) { //Delete Facility
					if(facilities.size() > 0)
						deleteFacility();
					else {
						JOptionPane.showMessageDialog(null,"ERROR: There are no facilities recorded in the system. Deletion cannot be performed. Returning admin to main menu.");
						menu();
					}
				}
				else if(choice.equals(facilityOptions[2])) { //Decommission
					decommission();
				}
				else if(choice.equals(facilityOptions[3])) { //Recommission
					recommission();
				}
				else if(choice.equals(facilityOptions[4])) { //Availability
					String[]fNames = Facilities.getFacNames();
					choice = (String) JOptionPane.showInputDialog(null,"Which facility would you like to check the availability for?","Input",1,null,fNames,fNames[0]);
					int index = Facilities.findIDInArray(fNames,choice);
					checkAvailability(index+1);
				}
				else if(choice.equals(facilityOptions[5]))
					menu();
			}
			else if(choice.equals(mainOptions[2])) {
				String[]fbOptions = {"Add a Booking","Log a Payment","View Bookings","View Accounts","Return to Main Menu"};
				choice = (String) JOptionPane.showInputDialog(null,"Choose an option","Facilities Menu",1,null,fbOptions,fbOptions[0]);
			
				if(choice.equals(fbOptions[0])) { //Add Booking
					ArrayList<String> usernames = (Driver.scanUsers()).get(1);
					usernames.remove(0);
					String[] finalUsers = new String[usernames.size()];
					finalUsers = usernames.toArray(finalUsers);
					choice = (String) JOptionPane.showInputDialog(null,"What user are you making this booking for?","Input",1,null,finalUsers,finalUsers[0]);
					int userID = usernames.indexOf(choice);
					addBooking((userID+2));
				}
				else if(choice.equals(fbOptions[1])) { //Log Payment
					ArrayList<String> usernames = (Driver.scanUsers()).get(1);
					usernames.remove(0);
					String[] finalUsers = new String[usernames.size()];
					finalUsers = usernames.toArray(finalUsers);
					choice = (String) JOptionPane.showInputDialog(null,"What account would you like to make a payment for?","Input",1,null,finalUsers,finalUsers[0]);
					int userID = (usernames.indexOf(choice))+2;
					makePayment(userID);
				}
				else if(choice.equals(fbOptions[2])) { //View bookings
					String[]fNames = Facilities.getFacNames();
					choice = (String) JOptionPane.showInputDialog(null,"Which facility would you like to view the bookings for?","Input",1,null,fNames,fNames[0]);
					int index = Facilities.findIDInArray(fNames,choice);
					Bookings.viewBookings((index+1),1);
				}
				else if(choice.equals(fbOptions[3])) { //View Account
					ArrayList<String> usernames = (Driver.scanUsers()).get(1);
					usernames.remove(0);
					String[] finalUsers = new String[usernames.size()];
					finalUsers = usernames.toArray(finalUsers);
					choice = (String) JOptionPane.showInputDialog(null,"Who's account would you like to view?","Input",1,null,finalUsers,finalUsers[0]);
					int userID = (usernames.indexOf(choice))+2;
					Bookings.viewAccount(userID,1);
				}
				else if(choice.equals(fbOptions[4]))
					menu();
			}
			else if(choice.equals(mainOptions[3]))
				Driver.menu();	
		}
		catch(NullPointerException nullException) {
			System.exit(0);
		}
		catch(Exception anException) {
			System.out.print(anException);
			JOptionPane.showMessageDialog(null,"The system has encountered an unexpected error. Please contact your systems technician. System shutdown initiated.");
			System.exit(0);
		} 
	}
	
	/** 
	* addFacility method allows the admin to add another facility 
	* it does this by having an input box asking for the name of the 
	* facility. The method then checks the other facilities to make 
	* sure the name isn't already used. When a name that hasn't been
	* used before is entered, it the asks for the price of the facility
	* per hour. Finally it writes the new facility to the facilites text file.
	*/
	public static void addFacility() throws IOException {
		double price = 0.0;
		String userInput, name = "", pattern = "[0-9]{1,}|[0-9]{1,}.[0-9]{1,2}";
		boolean valid= false, found = false;
		
		try {
			while(!valid) {
				name = JOptionPane.showInputDialog(null,"Please enter the name of the facility");
				for(int i = 0; i < facilities.size() && !found; i++) {
					if(facilities.get(i).getName().equals(name)) {
						found = true;
						JOptionPane.showMessageDialog(null,"A facility with this name already exists. Please enter a different name");
					}
				}
				if(!found)
					valid = true;
			}
			valid = false;
			while(!valid) {
				userInput = JOptionPane.showInputDialog(null,"Please enter the price per hour of this facility. (Numerical input only)");
				if(userInput.matches(pattern)) {
					valid = true;
					price = Double.parseDouble(userInput);
				}
			}
			valid = false;
		
			FileWriter facilitiesWriter = new FileWriter("facilities.txt",true);
			PrintWriter facilitiesPrinter = new PrintWriter(facilitiesWriter);
			numOfFacilities++;
			Facilities aFacility = new Facilities(numOfFacilities,name,price);
			facilities.add(aFacility);
			facilitiesPrinter.println(aFacility);
			facilitiesPrinter.close();
			facilitiesWriter.close();
			
			JOptionPane.showMessageDialog(null,name+" has successfully been added to the system. You will now be returned to the main menu.");
			menu();
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
	* This method allows the admin to delete one of the existng facilities.
	* It calls the getFacNames method and puts the names of the facilities in
	* an array which is then displayed in a drop down list so the admin can choose
	* which facility they would like to delete. Once the facility has been deleted
	* it returns the admin to their main menu.
	*/
	public static void deleteFacility() throws IOException {
		try {
			String[]fNames = Facilities.getFacNames();
			String choice = (String) JOptionPane.showInputDialog(null,"Which facility would you like to remove?","Input",1,null,fNames,fNames[0]);
			int index = Facilities.findIDInArray(fNames,choice);
			if(facilities.get(index).getDecomDate().equals("null")) {
				facilities.remove(index);
				numOfFacilities--;
		
				FileWriter facilitiesWriter = new FileWriter("facilities.txt");
				PrintWriter facilitiesPrinter = new PrintWriter(facilitiesWriter);
				for(int i = 0; i < numOfFacilities; i++) {
					facilities.get(i).setID(i+1);
					facilitiesPrinter.println(facilities.get(i));
				}
				facilitiesPrinter.close();
				facilitiesWriter.close();
				JOptionPane.showMessageDialog(null,choice+ " has been successfully removed from the system. You will now be returned to the main menu.");
				menu();
			}
		}
		catch(NullPointerException nullException) {
			System.exit(0);
		}
		catch(Exception anException) {
			System.out.print(anException);
			JOptionPane.showMessageDialog(null,"The system has encountered an unexpected error. Please contact your systems technician. System shutdown initiated.");
			System.exit(0);
		} 
	}
	
	/** 
	* This method allows the admin to input an email address for 
	* a new user. If the email is valid the system will generate a 
	* random password for the new user that must be used when they 
	* are logging in. It writes the new users information to the text 
	* file before returning the admin to the main menu.
	*/
	public static void registerUser() throws IOException
	{
		try {
			ArrayList<ArrayList<String>> allUsers = Driver.scanUsers();
			int numOfUsers = allUsers.get(0).size();
			FileWriter Users = new FileWriter ("Users.txt", true);
			PrintWriter userWriter = new PrintWriter(Users);
			String user = "";
			String error = "The email provided is invalid";
			boolean successful = false;
		
			user = JOptionPane.showInputDialog(null, "Enter new users email");
			
			if (user == null)
				System.exit(0);
			if(user.indexOf("@") == -1)
				JOptionPane.showMessageDialog(null, error);
			else if(user.indexOf(".", user.indexOf("@")) == -1)
				JOptionPane.showMessageDialog(null, error);			
			else if(!(allUsers.get(2).contains(user)))
			{
				allUsers.get(2).add(user);
				numOfUsers++;
				successful = true;
			}
			else
				JOptionPane.showMessageDialog(null, "This user is already signed up.");
		
			if(successful == true)
			{
				String characters = "ABCDEFGHIJKLMOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456789";
				String password = "";
				boolean exists = false;
				int charLength = characters.length();
				if (exists == false)
				{
					for(int i = 0; i < 8; i++)
					{
						int randChar = (int) (Math.random() * charLength);
						password += characters.charAt(randChar);
					}
			
					if(allUsers.get(2).contains(password));
						exists = true;
				}
				userWriter.println(numOfUsers + "," + user + "," + password + ",2");
				JOptionPane.showMessageDialog(null, "New user has been successfully registered. Their password is " + password+"\n You will now be returned to the main menu.");
			}
			userWriter.close();
			Users.close();
			menu();
		}
		catch(NullPointerException nullException) {
			System.exit(0);
		}
		catch(Exception anException) {
			System.out.print(anException);
			JOptionPane.showMessageDialog(null,"The system has encountered an unexpected error. Please contact your systems technician. System shutdown initiated.");
			System.exit(0);
		} 
	}
	
	/** 
	* addBooking method allows the admin to add a booking, it does this
	* by providing a drop down list of the facilities in the system which can 
	* be booked. It then prompts the admin to enter the date which they would like 
	* to make the booking for. It makes sure the date is valid and in the future.
	* It then goes on to ask for the time slot. When all the information is inputted
	* and valid it adds the new booking to the bookings text file before returning the
	* admin to the main menu.
	*/
	public static void addBooking(int userID) throws IOException {
		try {
			String[]fNames = Facilities.getFacNames();
			String choice = (String) JOptionPane.showInputDialog(null,"Which facility would you like to book?","Input",1,null,fNames,fNames[0]);
			int index = Facilities.findIDInArray(fNames,choice);
			Facilities facility = facilities.get(index);
			validateDecomissions();
			if(!(facility.getDecomDate().equals("null"))) {
				String date = standardFormatDate(facility.getDecomDate());
				String result = facility.getName()+" is decomissioned during this time. A booking cannot be made until "+date;
				JOptionPane.showMessageDialog(null,result);
			}
			else {
				boolean validDate = false;
				double comparison = 0;
				while(!validDate) {
					choice = JOptionPane.showInputDialog(null,"On what day would you like to make this booking? (Format: DD/MM/YYYY))");
					while(!validDate) {
						if(validateDate(choice) == false)
							choice = JOptionPane.showInputDialog(null,"ERROR: This date is invalid. Please enter date in the format \"DD/MM/YYYY\".");
						else
							validDate = true; 
					}
					choice = formatDate(choice);
					comparison = compareDates(choice);
					if(!(comparison > 0)) {
						JOptionPane.showInputDialog(null,"The date you have selected is invalid. The date must be after the current date");
						validDate = false;
					}
				}
				String date = choice;
				ArrayList<String> slots = new ArrayList<String>();
				slots.add("1");slots.add("2");slots.add("3");slots.add("4");slots.add("5");slots.add("6");slots.add("7");slots.add("8");slots.add("9");slots.add("10");slots.add("11");slots.add("12");
				String[] times = {"9:00-10:00","10:00-11:00","11:00-12:00","12:00-13:00","13:00-14:00","14:00-15:00","15:00-16:00","16:00-17:00","17:00-18:00","18:00-19:00","19:00-20:00","20:00-21:00"};
				ArrayList<Bookings> bookings = Bookings.scanBookings();
				int slot = 0,slotIndex = 0;
				for(int i = 0; i < bookings.size(); i++) {
					if(date.equals(bookings.get(i).getDate())) {
						slot = bookings.get(i).getTimeSlot();
						slotIndex = slots.indexOf(""+slot);
						slots.remove(slotIndex);
					}
				}
				String[]availableSlots = new String[slots.size()];
				for(int i = 0; i < slots.size(); i++) {
					slot = Integer.parseInt(slots.get(i));
					availableSlots[i] = times[slot-1];
				}
				choice = (String) JOptionPane.showInputDialog(null,"At what time would you like to make this booking? (Booked slots will not be displayed)","Input",1,null,availableSlots,availableSlots[0]);
				slotIndex = Facilities.findIDInArray(times,choice);
				int numOfBookings = bookings.size();
			
				FileWriter bookingsWriter = new FileWriter("bookings.txt",true);
				PrintWriter bookingsPrinter = new PrintWriter(bookingsWriter);
				int facID = facility.getID();
				Bookings aBooking = new Bookings((numOfBookings+1),facID,userID,date,(slotIndex+1),false);
				bookingsPrinter.println(aBooking);
			
				bookingsPrinter.close();
				bookingsWriter.close();
				JOptionPane.showMessageDialog(null,"The bookings has been logged to the system. You will now be returned to the main menu");
			}
			menu();
		}
		catch(NullPointerException nullException) {
			System.exit(0);
		}
		catch(Exception anException) {
			System.out.print(anException);
			JOptionPane.showMessageDialog(null,"The system has encountered an unexpected error. Please contact your systems technician. System shutdown initiated.");
			System.exit(0);
		} 	
	}
	
	/** 
	* This method means the admin can see if a facility that has been passed to this method 
	* is available on a certain day or over a period of time. The admin first picks if they 
	* want to check availability on a certain day or a period of time. Then they pick the 
	* date or dates they would like to see. The method then displays a table with the times 
	* the facility is available. It then brings the admin back to the main menu.
	*/
	public static void checkAvailability(int facID) throws IOException {
		try {
			ArrayList<Bookings> bookings = Bookings.scanBookings();
			String[]options = {"On a particular date","For a period of time"};
			String option = (String) JOptionPane.showInputDialog(null,"Would you like to view the availability of a faciility:","Input",1,null,options,options[0]);
			if(option.equals(options[0])) {
				boolean validDate = false;
				double comparison = 0;
				String choice = "",date;
				
				while(!validDate) {
					choice = JOptionPane.showInputDialog(null,"On what day would you like to check the availability of the faciility? (Format: DD/MM/YYYY))");
					if(validateDate(choice) == false)
						JOptionPane.showMessageDialog(null,"ERROR: This date is invalid. Please enter date in the format \"DD/MM/YYYY\".");
					else
						validDate = true; 
					
					if(validDate) {	
						choice = formatDate(choice);
						comparison = compareDates(choice);
						if(!(comparison > 0)) {
							JOptionPane.showMessageDialog(null,"The date you have selected is invalid. The date must be after the current date.");
							validDate = false;
						}
					}
				}
				date = choice;
				ArrayList<String> occupiedSlots = new ArrayList<String>();
				for(int i = 0; i < bookings.size(); i++) {
					if((bookings.get(i).getFacID() == facID) && date.equals((bookings.get(i).getDate()))) {
						occupiedSlots.add(""+bookings.get(i).getTimeSlot());
					}
				}
				if(occupiedSlots.size() < 12) {
					String result ="";
					int slot = 0;
					ArrayList<String> slots = new ArrayList<String>();
					slots.add("1");slots.add("2");slots.add("3");slots.add("4");slots.add("5");slots.add("6");slots.add("7");slots.add("8");slots.add("9");slots.add("10");slots.add("11");slots.add("12");
			
					String[] times = {"9:00-10:00","10:00-11:00","11:00-12:00","12:00-13:00","13:00-14:00","14:00-15:00","15:00-16:00","16:00-17:00","17:00-18:00","18:00-19:00","19:00-20:00","20:00-21:00"};
					int slotIndex = 0;
					for(int i = 0; i < occupiedSlots.size(); i++) {
						slotIndex = slots.indexOf(occupiedSlots.get(i));
						slots.remove(slotIndex);
					}
					String[]availableSlots = new String[slots.size()];
					for(int i = 0; i < slots.size(); i++) {
						slot = Integer.parseInt(slots.get(i));
						availableSlots[i] = times[slot-1];
					}
					for(int i = 0; i < availableSlots.length; i++) {
						result+= availableSlots[i]+"\n";
					}
					JOptionPane.showMessageDialog(null,"The time slots available on this date are:\n"+result);
				}
				else 
				JOptionPane.showMessageDialog(null,"There are no available slots on this date. You will now be returned to the main menu.");
			}
			else if(option.equals(options[1])) {
				boolean isValid = false,overallValid = false;
				String startDate = "",endDate = "";
				long numOfDays = 0;
				int dayNum = 1,startYear = 0;
				LocalDate start = LocalDate.now();
				LocalDate end = LocalDate.now();
				LocalDate nextDay = LocalDate.now();
				
				while(!overallValid) {
					isValid = false;
					while(!isValid) {
						startDate = JOptionPane.showInputDialog(null,"Enter the start date (Format: dd/mm/yyyy)");
						isValid = Admin.validateDate(startDate);
					}
					isValid = false;
					while(!isValid) {
						endDate = JOptionPane.showInputDialog(null,"Enter the end date (Format: dd/mm/yyyy)");
						isValid = Admin.validateDate(endDate);
					}
					
					startDate = Admin.formatDate(startDate);
					endDate = Admin.formatDate(endDate);
					start = LocalDate.parse(startDate);
					end = LocalDate.parse(endDate);
					int comparison = start.compareTo(end);
					if(comparison > 0)
						JOptionPane.showMessageDialog(null,"ERROR: Start date is after end date");
					else if(comparison == 0)
						JOptionPane.showMessageDialog(null,"ERROR: Start date is equal to end date");
					else if(!(compareDates(startDate) > 0))
						JOptionPane.showMessageDialog(null,"ERROR: Date entered is before current date.");
					else 
						overallValid = true;
				}
				numOfDays =  ChronoUnit.DAYS.between(start,end)+1;
				startYear = start.getYear();
				dayNum = start.getDayOfYear();
				nextDay = LocalDate.of(startYear,01,01);
				
				boolean leapYear;
				int timeSlot = 0;
				ArrayList<String> slots = new ArrayList<String>();
				DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				String result = String.format("%-20s %-25s\n","Date","Time Slots Available");
				String timeSlots = "";
				for(int i = 0; i < numOfDays; i++) {
					slots.add("1");slots.add("2");slots.add("3");slots.add("4");slots.add("5");slots.add("6");slots.add("7");slots.add("8");slots.add("9");slots.add("10");slots.add("11");slots.add("12");
					leapYear = nextDay.isLeapYear();
						
					if((dayNum > 365) && !leapYear) {
						startYear++;
						dayNum = dayNum/365;
					}
					else if((dayNum > 366) && leapYear) {
						startYear++;
						dayNum = dayNum/366;
					}
						
					nextDay = LocalDate.ofYearDay(startYear, dayNum);
					String currentDate = df.format(nextDay);
					String reformattedDate = standardFormatDate(currentDate);
					for (int j =0; j < bookings.size(); j++) {
							
						if(((bookings.get(j).getFacID()) == facID) && (currentDate.equals(bookings.get(j).getDate()))) {
							timeSlot = bookings.get(j).getTimeSlot();
							slots.remove(timeSlot-1);
						}
					}
					for(int j = 0; j < slots.size(); j++) {
						timeSlots+= " "+slots.get(j)+" ";
					}
					result+= String.format("%-20s %20s\n",reformattedDate,timeSlots);
					dayNum++;
					timeSlots = "";
					slots.clear();
					if((i % 20 == 0 && i > 0) || i == (numOfDays-1)) {
						JOptionPane.showMessageDialog(null,result,"Availability",1);
						result = String.format("%-20s %-25s\n","Date","Time Slots Available");
					}
				}
			}
			menu();
		}
		catch(NullPointerException nullException) {
			System.exit(0);
		}
		catch(Exception anException) {
			System.out.print(anException);
			JOptionPane.showMessageDialog(null,"The system has encountered an unexpected error. Please contact your systems technician. System shutdown initiated.");
			System.exit(0);
		} 
	}
	
	/**
	* This method is called by various other methods to make sure the date 
	* inputted by the user is valid. It makes sure that the right number of 
	* days are inputted in relation to the month. If the date is valid it 
	* returns true, if not false is returned.
	*/
	public static boolean validateDate(String date)
	{
		try {
			String pattern = "[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}";
			String dateParts[];
			int daysInMonth [] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
			boolean isValid = true;
			boolean isLeapYear = false;
			if(!(date.matches(pattern)))
				JOptionPane.showMessageDialog(null, "ERROR: Invalid input");
			else
			{
				dateParts = date.split("/");
				int day = Integer.parseInt(dateParts[0]);
				int month = Integer.parseInt(dateParts[1]);
				int year = Integer.parseInt(dateParts[2]);

				if(day == 0 || month == 0 || year == 0)
					isValid = false;
				else if (month > 12)
					isValid = false;
				else if ( month == 02 && day == 29 && !((year % 4 ==0) && (year % 100 != 0) || ( year % 400 == 0)))
					isValid = false;
				else if(day > daysInMonth[month -1])
					isValid = false;
			
				return isValid;
			}
		}
		catch(NullPointerException nullException) {
			System.exit(0);
		}
		catch(Exception anException) {
			System.out.print(anException);
			JOptionPane.showMessageDialog(null,"The system has encountered an unexpected error. Please contact your systems technician. System shutdown initiated.");
			System.exit(0);
		} 
		return false;
	}
	
	/**
	* compareDates takes an input date in String format and converts it 
	* to a LocalDate. It then returns the difference between the input 
	* date and todays date.
	*/
	public static int compareDates(String userDate) {
		try {
			LocalDate chosenDate = LocalDate.parse(userDate);
			return chosenDate.compareTo(LocalDate.now());
		}
		catch(NullPointerException nullException) {
			System.exit(0);
		}
		catch(Exception anException) {
			System.out.print(anException);
			JOptionPane.showMessageDialog(null,"The system has encountered an unexpected error. Please contact your systems technician. System shutdown initiated.");
			System.exit(0);
		} 
		return -1;
	}
	
	/**
	* formatDate is passed a date in the form of a String. This string
	* is then split up into day, month and year and then returned in the
	* format year-month-day due to the LocalDates been given in this format.
	*/
	public static String formatDate(String date) {
		try {
			String[]dateArray = date.split("/");
			String day = dateArray[0];
			String month = dateArray[1];
			String year = dateArray[2];
		
			return year+"-"+month+"-"+day;
		}
		catch(NullPointerException nullException) {
			System.exit(0);
		}
		catch(Exception anException) {
			System.out.print(anException);
			JOptionPane.showMessageDialog(null,"The system has encountered an unexpected error. Please contact your systems technician. System shutdown initiated.");
			System.exit(0);
		} 
		return null;
	}
	
	/**
	* This method is passed a date in the form of a string, then it is split
	* into day, month and year. After this it is returned in the form of 
	* day/month/year for the user to see.
	*/
	public static String standardFormatDate(String date) {
		try {
			String[]dateArray = date.split("-");
			String day = dateArray[2];
			String month = dateArray[1];
			String year = dateArray[0];
		
			return day+"/"+month+"/"+year;
		}
		catch(NullPointerException nullException) {
			System.exit(0);
		}
		catch(Exception anException) {
			System.out.print(anException);
			JOptionPane.showMessageDialog(null,"The system has encountered an unexpected error. Please contact your systems technician. System shutdown initiated.");
			System.exit(0);
		} 
		return null;
	}
	
	/**
	* The validateDecomissions method goes through the facilities text file
	* getting the decommission date. If the date doesn't equal null, it changes 
	* the formate to a localDate by calling the formatDate method. It then compares 
	* the two dates using the compareDates method. Finally if the decommission date
	* is in the past it rewrites it to null.
	*/
	public static void validateDecomissions() throws IOException {
		try {
			double comparison = 0;
			facilities = Facilities.scanFacilities();
			numOfFacilities = facilities.size();
			FileWriter facilitiesWriter = new FileWriter("facilities.txt");
			PrintWriter facilitiesPrinter = new PrintWriter(facilitiesWriter);
			String formattedDate ="";
			for(int i =0; i < numOfFacilities; i++){
				if(!((facilities.get(i).getDecomDate()).equals("null"))) {
					formattedDate = facilities.get(i).getDecomDate();
					comparison = compareDates(formattedDate);
					if(comparison < 0)
						facilities.get(i).setDecomDate("null");
				}
				facilitiesPrinter.println(facilities.get(i));
			}
			facilitiesPrinter.close();
			facilitiesWriter.close();
		}
		catch(NullPointerException nullException) {
			System.exit(0);
		}
		catch(Exception anException) {
			System.out.print(anException);
			JOptionPane.showMessageDialog(null,"The system has encountered an unexpected error. Please contact your systems technician. System shutdown initiated.");
			System.exit(0);
		} 
	}
	
	/**
	* makePayment method allows the admin to pick one of the users
	* bookings and say that they have paid for that booking. It is passed 
	* the ID of the user and they are therefore given a drop down list 
	* of the bookings that user has.
	*/
	public static void makePayment(int ID) throws IOException //Vincent
	{
		try {
			ArrayList<Bookings> bookings = Bookings.scanBookings();
			ArrayList<Bookings> found = new ArrayList<Bookings>();
			ArrayList<Facilities> facilities = Facilities.scanFacilities();
			String[]times = {"9:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00"};
			for(int i = 0; i < bookings.size(); i++) 
			{
				if(((bookings.get(i).getUserID()) == ID) && !(bookings.get(i).getPaymentStatus()))
					found.add(bookings.get(i));
			}
		
			if(found.size() > 1)
			{
				String[]needPayment = new String[found.size()];
				String input = "Which booking would you like to pay?\n";
				input += String.format("%20s %20s %20s","Facility","Date","Time");
				String facName = "",date = "",time = "";
				for(int i = 0; i < found.size(); i++) 
				{
					facName = facilities.get((found.get(i).getFacID())-1).getName();
					date = standardFormatDate(found.get(i).getDate());
					time = times[(found.get(i).getTimeSlot())-1];
					needPayment[i] = String.format("%-20s %-20s %-20s",facName,date,time);
				}
				String choice = (String) JOptionPane.showInputDialog(null,input,"Input",1,null,needPayment,needPayment[0]);
				int needPay = Facilities.findIDInArray(needPayment,choice);
				int bookingNum = found.get(needPay).getBookingNo();
				bookings.get(bookingNum-1).setPaymentStatus(true);
				FileWriter bookingsWriter = new FileWriter("bookings.txt");
				PrintWriter bookingsPrinter = new PrintWriter(bookingsWriter);
				for(int i = 0; i <bookings.size(); i++)
				{
					bookingsPrinter.println(bookings.get(i));
				}
			
				bookingsPrinter.close(); 
				bookingsWriter.close();
			
				JOptionPane.showMessageDialog(null,"The payment was made successfully");
			}
			else if(found.size() < 1)
				JOptionPane.showMessageDialog(null,"There are no bookings that require payments in this account");
			else
			{
				FileWriter bookingsWriter = new FileWriter("bookings.txt");
				PrintWriter bookingsPrinter = new PrintWriter(bookingsWriter);
			
				int needPayment = found.get(0).getBookingNo();
				bookings.get(needPayment-1).setPaymentStatus(true);
			
				for(int i = 0; i <bookings.size(); i++)
				{
					bookingsPrinter.println(bookings.get(i));
				}
			
				bookingsPrinter.close(); 
				bookingsWriter.close();
				JOptionPane.showMessageDialog(null,"There is only one unpaid booking on this account. It will now be paid for automatically.");
			}
			JOptionPane.showMessageDialog(null,"You will now be returned to the main menu");
			menu();
		}
		catch(NullPointerException nullException) {
			System.exit(0);
		}
		catch(Exception anException) {
			System.out.print(anException);
			JOptionPane.showMessageDialog(null,"The system has encountered an unexpected error. Please contact your systems technician. System shutdown initiated.");
			System.exit(0);
		} 
	}
	
	/** 
	* This method allows the admin to decommission a facility for a certain
	* period of time. The method first checks to make sure there is a facility
	* eligible to decommission. Then the admin chooses which facility they want
	* to decommission, before they pick the date. The method then writes the 
	* end date of the decommission period to the text file. Lastly the admin is 
	* brought back to the main menu.
	*/
	public static void decommission() throws IOException
	{	
		try {
			boolean validDate = false;
			ArrayList<Facilities> facilities = Facilities.scanFacilities();
			ArrayList<Bookings> bookings = Bookings.scanBookings();
			ArrayList<Facilities> found = new ArrayList<Facilities>();
			String endDecom = "";
	
			for(int i = 0; i < facilities.size(); i++) 
			{
				if(facilities.get(i).getDecomDate().equals("null"))
					found.add(facilities.get(i));
			}
		
			if(!(found.size() > 0))
				JOptionPane.showMessageDialog(null,"All facilities are currently decomissioned. You will now be returned to the main menu");
			else 
			{	
				String[]fNames = new String[found.size()];
				for(int i = 0; i < fNames.length; i++) 
					fNames[i] = found.get(i).getName();
			
				String choice = (String) JOptionPane.showInputDialog(null,"Which facility would you like to decommission?","Input",1,null,fNames,fNames[0]);
				int index = Facilities.findIDInArray(fNames,choice);
				int theID = found.get(index).getID()-1;
						
				LocalDate today = LocalDate.now();
				int dayNum = today.getDayOfYear();
				int diffInDates = 0;
				boolean facilBooked = false; 
				boolean valid = true;
				int indexOfBooking =0;
			
				while(!validDate) 
				{
					endDecom = JOptionPane.showInputDialog(null, "Enter the end date of the decommission period. (Format: dd/mm/yyyy)");
					validDate = validateDate(endDecom);
					if (validDate)
					{
						endDecom = formatDate(endDecom);
						diffInDates = compareDates(endDecom);
						if(diffInDates < 0)
						{
							JOptionPane.showMessageDialog(null, "ERROR: This date is before the current date");
							validDate = false;
						}
					}
				}	

				if(diffInDates == 0)
				{
					for(int i = 0; i < bookings.size() && valid; i++)
					{
						if(bookings.get(i).getDate().equals(today))
						{
							indexOfBooking = i;
							valid = false;
						}
					}
				}
				else if(diffInDates > 0)
				{
					LocalDate endDate = LocalDate.parse(endDecom);
					long numOfDays =  ChronoUnit.DAYS.between(today,endDate);
					DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					for(int i = 0; (i < (numOfDays+1)) && valid; i++)
					{
						LocalDate nextDay = LocalDate.ofYearDay(today.getYear(), (dayNum));
						String currentDate = df.format(nextDay);
						for (int j =0; (j < bookings.size()) && valid; j++)
						{
							if(bookings.get(j).getDate().equals(currentDate))
								valid = false;
						}
						dayNum++;
					}
				}
				
				if(!valid)
					JOptionPane.showMessageDialog(null, "ERROR: Bookings exist for this facility within that time period. You will now be returned to the main menu");
				else if (valid)
				{
					facilities.get(theID).setDecomDate(endDecom);
					FileWriter decom = new FileWriter("facilities.txt");
					PrintWriter decomWriter = new PrintWriter(decom);
				
					for(int i = 0; i < facilities.size(); i++)
						decomWriter.println(facilities.get(i));
					
					decomWriter.close();
					decom.close();
					JOptionPane.showMessageDialog(null, "Decommission was successful. You will now be returned to the main menu");
				}
			}
			menu();
		}
		catch(NullPointerException nullException) {
			System.exit(0);
		}
		catch(Exception anException) {
			System.out.print(anException);
			JOptionPane.showMessageDialog(null,"The system has encountered an unexpected error. Please contact your systems technician. System shutdown initiated.");
			System.exit(0);
		} 
	}
	
	/** 
	* recommission method allows the admin to recommission a facility
	* after is has been decommissioned. It has a drop down list of all
	* the facilities that have a decommission date. It then writes over 
	* the decommission date in the text file with null. admin is then 
	* returned to the main menu.
	*/
	public static void recommission() throws IOException
	{
		try {
			ArrayList<Facilities> facilities = Facilities.scanFacilities();
			ArrayList<Facilities> found = new ArrayList<Facilities>();
		
			for(int i =0; i < facilities.size(); i++)
			{
				if(!(facilities.get(i).getDecomDate().equals("null")))
					found.add(facilities.get(i));
			}
			if(!(found.size() > 0)) 
				JOptionPane.showMessageDialog(null,"All facilities are currently running and do not need to be recommissioned. You will now be returned to the main menu.");
			else {
				String[]fNames = new String[found.size()];
				for(int i = 0; i < fNames.length; i++) {
					fNames[i] = found.get(i).getName();
				}
				String choice = (String) JOptionPane.showInputDialog(null,"Which facility would you like to recommission?","Input",1,null,fNames,fNames[0]);
				int index = Facilities.findIDInArray(fNames,choice);
				int theID = found.get(index).getID()-1;
	    
				facilities.get(theID).setDecomDate("null");
				FileWriter decom = new FileWriter("facilities.txt");
				PrintWriter decomWriter = new PrintWriter(decom);
				
				for(int i = 0; i < facilities.size(); i++)
				{
					decomWriter.println(facilities.get(i));
				}
				decomWriter.close();
				decom.close();
				JOptionPane.showMessageDialog(null,"Recomission was successful. You will now be returned to the main menu.");
			}
			menu();
		}
		catch(NullPointerException nullException) {
			System.exit(0);
		}
		catch(Exception anException) {
			System.out.print(anException);
			JOptionPane.showMessageDialog(null,"The system has encountered an unexpected error. Please contact your systems technician. System shutdown initiated.");
			System.exit(0);
		} 
	}
}