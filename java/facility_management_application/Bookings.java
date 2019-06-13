import javax.swing.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;

public class Bookings {
	public int bookingNo,facID,userID,timeSlot;
	public String date;
	public boolean paymentStatus;
	
	/**
	* bookings method creates the booking object that has
	* a booking number, a facility ID, a user ID, the date, 
	* the time slot and the payment status. There is also 
	* set and get methods for each of the factors needed.
	*/
	public Bookings(int bookingNo,int facID,int userID,String date,int timeSlot,boolean paymentStatus) {
		this.bookingNo = bookingNo;
		this.facID = facID;
		this.userID = userID;
		this.date = date;
		this.timeSlot = timeSlot;
		this.paymentStatus = paymentStatus;
	}

	public void setBookingNo(int bookingNo) {
		this.bookingNo = bookingNo;
	}
	public int getBookingNo() {
		return this.bookingNo;
	}
	
	public void setFacID(int facID) {
		this.facID = facID;
	}
	public int getFacID() {
		return this.facID;
	}
	
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public int getUserID() {
		return this.userID;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	public String getDate() {
		return this.date;
	}
	
	public void setTimeSlot(int timeSlot) {
		this.timeSlot = timeSlot;
	}
	public int getTimeSlot() {
		return this.timeSlot;
	}
	
	public void setPaymentStatus(boolean paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public boolean getPaymentStatus() {
		return this.paymentStatus;
	}
	
	public String toString() {
		return bookingNo+","+facID+","+userID+","+date+","+timeSlot+","+paymentStatus;
	}
	
	/**
	* scanBookings method goes through the bookings text file
	* and puts all the information into an object array list.
	*/
	public static ArrayList<Bookings> scanBookings() throws IOException {
		File aFile = new File("bookings.txt");
		
		if(!(aFile.exists())) {
			JOptionPane.showMessageDialog(null,"ERROR: bookings could not be recovered.");
			System.exit(0);
		}
		else {
			ArrayList<Bookings> list = new ArrayList<Bookings>();
		
			Scanner aScanner = new Scanner(aFile);
			String[]splitArray;
			
			while(aScanner.hasNext()) {
				splitArray = (aScanner.nextLine()).split(",");
				Bookings aBooking;
			
				aBooking = new Bookings((Integer.parseInt(splitArray[0])),(Integer.parseInt(splitArray[1])),(Integer.parseInt(splitArray[2])),splitArray[3],(Integer.parseInt(splitArray[4])),(Boolean.parseBoolean(splitArray[5])));
				list.add(aBooking);
			}
			aScanner.close();
			return list;
		}
		return null;
	}
	
	/**
	* This method is called by the user menu to allow the user to view their
	* own bookings. They can choose to view all their bookings or just bookings
	* within a certain period of time. If the user chooses to see bookings in a 
	* certain period of time, the system will make sure the dates supplied are
	* valid and that the start date is before the end date it then gets all the 
	* bookings the user has during period and displays them in a table with the 
	* user, date, time and payment status. The system does the same if the user 
	* wants to view all their bookings.
	*/
	public static void viewBookings(int ID,int accountType) throws IOException {
		try 
		{
			ArrayList<Bookings> bookings = scanBookings();
			ArrayList<Bookings> found = new ArrayList<Bookings>();
			String[]times = {"9:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00"};
			ArrayList<Facilities> facilities = Facilities.scanFacilities();
			ArrayList<ArrayList<String>> users = Driver.scanUsers();
			double compare = 0.0;
			long numOfDays = 0;
			int dayNum = 1,startYear = 0;
			LocalDate start = LocalDate.now();
			LocalDate end = LocalDate.now();
			LocalDate nextDay = LocalDate.now();
			String[]options = {"View all bookings","View bookings within a certain period of time"};
			String option = (String) JOptionPane.showInputDialog(null,"Choose an option","Input",1,null,options,options[0]);
			
			if(option.equals(options[1])) {
				boolean isValid = false,overallValid = false;
				String startDate = "",endDate = "";
				
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
					else if(!(Admin.compareDates(startDate) > 0))
						JOptionPane.showMessageDialog(null,"ERROR: Date entered is before current date");
					else 
						overallValid = true;
				}
				numOfDays =  ChronoUnit.DAYS.between(start,end)+1;
				startYear = start.getYear();
				dayNum = start.getDayOfYear();
				nextDay = LocalDate.of(startYear,01,01);
			}
			
			if(accountType == 1) {
				String result = "No bookings exist for this facility";
				
				if(option.equals(options[1])) {
					boolean leapYear;
					result += " during this time period.";
					DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					
					for(int i = 0; i < numOfDays; i++) {
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
						for (int j =0; j < bookings.size(); j++) {
							
							if(((bookings.get(j).getFacID()) == ID) && (currentDate.equals(bookings.get(j).getDate())))
								found.add(bookings.get(j));
						}
						dayNum++;
					}
				}
				else {
					for(int i = 0; i < bookings.size(); i++) {
						compare = Admin.compareDates(bookings.get(i).getDate());
						if(((bookings.get(i).getFacID()) == ID) && (compare > 0.0))
							found.add(bookings.get(i));
					}
				}
				
				if(found.size() > 0) {
					String[][]rows = new String[found.size()][4];
					String[]columns = {"User","Date","Time","Payment Status"};
					String standardFormatDate ="";
					for(int i = 0; i < found.size(); i++) {					
						rows[i][0] = ""+users.get(1).get((found.get(i).getUserID())-1);
						standardFormatDate = Admin.standardFormatDate(found.get(i).getDate());	
						rows[i][1] = ""+standardFormatDate;
						rows[i][2] = ""+times[found.get(i).getTimeSlot()-1];
						rows[i][3] = ""+found.get(i).getPaymentStatus();
					}
					JTable table = new JTable(rows,columns);
					JScrollPane scroll = new JScrollPane(table);
					scroll.setPreferredSize(new Dimension( 500,100 ));
					JOptionPane.showMessageDialog(null,scroll,"Current bookings for "+facilities.get(ID-1).getName(),1);
				}
				else
					JOptionPane.showMessageDialog(null,result);
				
				JOptionPane.showMessageDialog(null,"You will now be returned to the main menu");
				Admin.menu();
			}
			if(accountType == 2) {
				String result = "You have no bookings";
				
				if(option.equals(options[1])) {
					boolean leapYear;
					result += " during this time period.";
					DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					
					for(int i = 0; i < numOfDays; i++) {
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
						for (int j =0; j < bookings.size(); j++) {
							
							if(((bookings.get(j).getUserID()) == ID) && (currentDate.equals(bookings.get(j).getDate())))
								found.add(bookings.get(j));
						}
						dayNum++;
					}
				}
				else {
					for(int i = 0; i < bookings.size(); i++) {
						compare = Admin.compareDates(bookings.get(i).getDate());
						if((bookings.get(i).getUserID()) == ID && compare > 0)
							found.add(bookings.get(i));
					}
				}
				if(found.size() > 0) {
					String[][]rows = new String[found.size()][4];
					String[]columns = {"Facility","Date","Time","Payment Status"};
					String standardFormatDate ="";
					for(int i = 0; i < found.size(); i++) {
						rows[i][0] = ""+facilities.get((found.get(i).getFacID())-1).getName();
						standardFormatDate = Admin.standardFormatDate(found.get(i).getDate());	
						rows[i][1] = ""+standardFormatDate;
						rows[i][2] = ""+times[found.get(i).getTimeSlot()-1];
						rows[i][3] = ""+found.get(i).getPaymentStatus();
					}
					JTable table = new JTable(rows,columns);
					JScrollPane scroll = new JScrollPane(table);
					scroll.setPreferredSize(new Dimension( 500,100 ));
					JOptionPane.showMessageDialog(null,scroll,"Current bookings for "+users.get(1).get(ID-1),1);
				}
				else
					JOptionPane.showMessageDialog(null,result);
				
				JOptionPane.showMessageDialog(null,"You will now be returned to the main menu");
				User.menu(ID);
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
	* viewAccount method allows the user to vies their account. It is 
	* passed the user ID and the account type. The system them generates 
	* a table with the facility, date, time, price and payment status.
	* the user can also see the amount of money they have paid and the
	* amount of money still due. 
	*/
	public static void viewAccount(int ID,int accountType) throws IOException
	{
		try
		{
			ArrayList<Bookings> bookings = scanBookings();
			ArrayList<Bookings> found = new ArrayList<Bookings>();
			String[]times = {"9:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00"};
			ArrayList<Facilities> facilities = Facilities.scanFacilities();
			ArrayList<ArrayList<String>> users = Driver.scanUsers();
			double totalAmountDue = 0,totalAmountPaid= 0;
			for(int i = 0; i < bookings.size(); i++) {
				if(((bookings.get(i).getUserID()) == ID))
					found.add(bookings.get(i));
			}
			if(found.size() > 0) 
			{
			String[][]rows1 = new String[found.size()][5];
			String[]columns1 = {"Facility","Date","Time","Price","Payment Status",};
			String[][]rows2 = new String[1][2];
			String[]columns2 = {"Total Amount Paid","Total Amount Due",};
				
			for(int i = 0; i < found.size(); i++) 
			{
				rows1[i][0] = ""+facilities.get((found.get(i).getFacID())-1).getName();
				rows1[i][1] = ""+found.get(i).getDate();
				rows1[i][2] = ""+times[found.get(i).getTimeSlot()-1];
				rows1[i][3] = ""+facilities.get((found.get(i).getFacID())-1).getPrice();
				rows1[i][4] = ""+found.get(i).getPaymentStatus();
				if(found.get(i).getPaymentStatus())
					totalAmountPaid += facilities.get((found.get(i).getFacID())-1).getPrice();
				if(!(found.get(i).getPaymentStatus()))
					totalAmountDue += facilities.get((found.get(i).getFacID())-1).getPrice();
			}
			for(int i = 0; i < 1; i++)
			{
				rows2[i][0] = ""+totalAmountPaid;
				rows2[i][1] = ""+totalAmountDue;
			}
			JTable table = new JTable(rows1,columns1);
			JScrollPane scroll = new JScrollPane(table);
			JTable tablet = new JTable(rows2,columns2);
			JScrollPane printThisBinch = new JScrollPane(tablet);
			scroll.setPreferredSize(new Dimension( 500,100 ));
			printThisBinch.setPreferredSize(new Dimension( 500,100 ));
			JOptionPane.showMessageDialog(null,scroll,"All bookings for "+users.get(1).get(ID-1),1);
			JOptionPane.showMessageDialog(null,printThisBinch,"Statement for "+users.get(1).get(ID-1)+"'s account",1);
		}
		else
			JOptionPane.showMessageDialog(null,"There are no outstanding payments in this account.");
			
		JOptionPane.showMessageDialog(null,"You will now be returned to the main menu");
		
		if(accountType == 1)
			Admin.menu();
		else if(accountType == 2)
			User.menu(ID);
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