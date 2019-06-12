import java.util.*;
import java.io.*;

public class ReadWrite {
	private static Scanner in;
	
	/**
		This method reads input from products.txt into an arraylist and returns it
		@return ArrayList<Product> : an arraylist of products from the product.txt file
		@throws IOException e
	*/
	public static ArrayList<Product> scanProducts() throws IOException {
		File aFile = new File("products.txt");
		if(!(aFile.exists())) {
			System.out.println("ERROR: " +aFile+" cannot be found.");
			System.exit(0);
		}
		else {
			ArrayList<Product> list = new ArrayList<Product>();
			in = new Scanner(aFile);
			String[]split;
			while(in.hasNext()) {
				split = in.nextLine().split(",");
				Product o = new Product(new String(split[0]),Double.parseDouble(split[1]));
				list.add(o);
			}
			in.close();
			return list;
		}
		return null;
	}
	
	/**
		This method writes input to products.txt from an arraylist
		@param ArrayList<Product> list
		@throws IOException e
	*/
	public static void writeProducts(ArrayList<Product> list) throws IOException {
		FileWriter fileWriter = new FileWriter("products.txt",false);
		PrintWriter printer = new PrintWriter(fileWriter);
		
		for(Product o : list) {
			printer.println(o.format());
		}
		printer.close();
		fileWriter.close();
	}
	
	/**
		This method reads input coins.txt into an arraylist and returns it
		@return ArrayList<Coin> : an arraylist of products from the coins.txt file
		@throws IOException e
	*/
	public static ArrayList<Coin> scanCoins() throws IOException {
		File aFile = new File("coins.txt");
		if(!(aFile.exists())) {
			System.out.println("ERROR: " +aFile+" cannot be found.");
			System.exit(0);
		}
		else {
			ArrayList<Coin> list = new ArrayList<Coin>();
			in = new Scanner(aFile);
			String[]split;
			while(in.hasNext()) {
				split = in.nextLine().split(",");
				Coin o = new Coin(Double.parseDouble(split[0]),new String(split[1]));
				list.add(o);
			}
			in.close();
			return list;
		}
		return null;
	}
	
	/**
		This method writes input to coins.txt from an arraylist
		@param ArrayList<Coin> list
		@throws IOException e
	*/
	public static void writeCoins(ArrayList<Coin> list) throws IOException {
		FileWriter fileWriter = new FileWriter("coins.txt",false);
		PrintWriter printer = new PrintWriter(fileWriter);
		
		for(Coin o : list) {
			printer.println(o.format());
		}
		printer.close();
		fileWriter.close();
	}
	
	/**
		This method reads input from a chosen file into an integer arraylist and returns it
		@param String fileName
		@return ArrayList<Integer> : an arraylist of integers from the chosen file
		@throws IOException e
	*/
	public static ArrayList<Integer> scanIntegers(String fileName) throws IOException {
		File aFile = new File(fileName);
		if(!(aFile.exists())) {
			System.out.println("ERROR: " +aFile+" cannot be found.");
			System.exit(0);
		}
		else {
			ArrayList<Integer> list = new ArrayList<Integer>();
			in = new Scanner(aFile);
			int next;
			while(in.hasNext()) {
				next = Integer.parseInt(in.nextLine());
				list.add(next);
			}
			in.close();
			return list;
		}
		return null;
	}
	
	/**
		This method writes integers to a chosen file from an arraylist
		@param ArrayList<Integer> list, String fileName
		@throws IOException e
	*/
	public static void writeQuantity(ArrayList<Integer> list, String fileName) throws IOException {
		FileWriter fileWriter = new FileWriter(fileName,false);
		PrintWriter printer = new PrintWriter(fileWriter);
		
		for(int i = 0; i < list.size(); i++) {
			printer.println(list.get(i));
		}
		printer.close();
		fileWriter.close();
	}
}