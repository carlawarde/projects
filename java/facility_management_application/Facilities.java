import java.util.*;
import java.io.*;
import javax.swing.*;

public class Facilities {
	private int ID;
	private String name,endDecomDate;
	private double price;
	
	/**
	* This method creates the object of the facility. It has an
	* ID number, a name, a price and a decommission status. There 
	* is also set and get methods for each factor needed.
	*/
	public Facilities(int ID, String name, double price, String endDecomDate) {
		this.ID = ID;
		this.name = name;
		this.price = price;
		this.endDecomDate = endDecomDate;
	}
	public Facilities(int ID, String name, double price) {
		this.ID = ID;
		this.name = name;
		this.price = price;
		this.endDecomDate = "null";
	}
	
	public void setID(int ID) {
		this.ID = ID;
	}
	public int getID() {
		return this.ID;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	public double getPrice() {
		return this.price;
	}
	
	public void setDecomDate(String endDecomDate) {
		this.endDecomDate = endDecomDate;
	}
	public String getDecomDate() {
		return this.endDecomDate;
	}
	
	public String toString() {
		return getID()+","+getName()+","+getPrice()+","+getDecomDate();
	}
	
	/**
	* This method goes through the facilities text file and stores 
	* the data in an object array list which it then returns.
	*/
	public static ArrayList<Facilities> scanFacilities() throws IOException {
		try
		{
			File aFile = new File("facilities.txt");
				
				if(!(aFile.exists())) {
				JOptionPane.showMessageDialog(null,"ERROR: facilities could not be recovered.");
				System.exit(0);
			}
			else {
				ArrayList<Facilities> list = new ArrayList<Facilities>();
		
				Scanner aScanner = new Scanner(aFile);
				String[]splitArray;
				
				while(aScanner.hasNext()) {
					splitArray = (aScanner.nextLine()).split(",");
					Facilities aFacility;
					
					aFacility = new Facilities((Integer.parseInt(splitArray[0])),splitArray[1],(Double.parseDouble(splitArray[2])),splitArray[3]);
					list.add(aFacility);
				}
				aScanner.close();
				return list;
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
	
	/**
	* getFacNames goes through a array list of the facilities. 
	* It then stores the all the names of the facilities in a
	* one dimensional array. It then returns that array with all
	* the facility names.
	*/
	public static String[] getFacNames() throws IOException {
		ArrayList<Facilities> facilities = scanFacilities();
		String[]fNames = new String[facilities.size()];
		int count = 0;	
		for(int i = 0; i < facilities.size(); i++) {
			fNames[count] = facilities.get(i).getName();
			count++;
		}
		return fNames;
	}
	
	/**
	* findIDInArray method is passed a one dimensional array and a string.
	* The method goes through the array looking for String. It then stores 
	* the position where is found the string and returns it.
	*/
	public static int findIDInArray(String[]array,String choice) {
		boolean found = false;
		int index = 0;
		for(int i = 0; i < array.length && !found; i++) {
			if(array[i].equals(choice)) {
				index = i;
				found = true;
			}	
		}
		if(found)
			return index;
		else
			return -1;
	}
}