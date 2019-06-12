import java.util.ArrayList;
import java.io.*;
/**
   A vending machine.
*/
public class VendingMachine
{  
   private ArrayList<Product> products;
   private ArrayList<Integer> productQuantities;
   private ArrayList<Coin> coins;
   private ArrayList<Integer> coinQuantities;
   private ArrayList<Coin> currentCoins;

   /**
      Constructs a VendingMachine object and reads in data from the text files to the datafield ArrayLists
	  @throws IOException e
   */
    public VendingMachine() throws IOException
    { 
		this.products = ReadWrite.scanProducts();
		this.coins = ReadWrite.scanCoins();
		this.currentCoins = new ArrayList<Coin>();
		this.productQuantities = ReadWrite.scanIntegers("productQuantities.txt");
		this.coinQuantities = ReadWrite.scanIntegers("coinQuantities.txt");
    }
 
	/**
		This method inserts a product into the products ArrayList, and adds its quantity to the productsQuantity arraylist
		providing the Product doesn't already exist. If it does, only the quantity is updated. Then the products.txt and productsQuantity.txt files are updated.
		@param Product aProduct int quantity
		@throws IOException e
	*/
	public void addProduct(Product aProduct, int quantity) throws IOException {
		if((indexOf(aProduct) == -1) || (products.size() == 0)) {
			products.add(aProduct);
			productQuantities.add(quantity);
		}
		else {
			int index = indexOf(aProduct);
			int currentQuantity = productQuantities.get(index);
			productQuantities.set(index, (currentQuantity+quantity));
		}
		ReadWrite.writeProducts(products);
		ReadWrite.writeQuantity(productQuantities,"productQuantities.txt");
	}
	
	/**
		This method returns the ArrayList of products as an array of products
		@return Product[]
	*/
	public Product[] getProductTypes() {
		Product[] array = products.toArray(new Product[products.size()]);
		return array;
	}
	
	/**
		This method takes a Coin as an input and adds it to the currentCoins arraylist, it then returns the current value of coins in the machine as a String
		@param Coin aCoin
		@return String currentCoinsValue
	*/
	public String addCoin(Coin aCoin) {
		currentCoins.add(aCoin);
		return "Current Coins:\n"+ currentCoinsValue();
	}
	
	/**
		This method removes all the money in the currentCoins arraylist, and returns the value of that money as a double
		@return double 
	*/
	public double removeMoney() {
		double values = currentCoinsValue();
		currentCoins.clear();
		return values;
	}
	
	/**
		This method is used to buy a product. It checks if the product is in stock, and then checks if the user has inserted enough money into the machine. 
		If not, it throws a VendingException. Otherwise it buys the product by removing coins from the currentCoins arraylist and inserting them into coinQuantities.
		Then, the coinQuantities and productQuantities text files are updated.
		@param Product p: Product the user is buying
		@throws IOException e
	*/
	public void buyProduct(Product p) throws IOException {
		int index = indexOf(p);
		
		if(productQuantities.get(index) > 0 ){
			if(currentCoinsValue() >= p.getPrice()) {
				productQuantities.set(index, (productQuantities.get(index) - 1));
			
				for(int i =0; i < currentCoins.size(); i++) {
					index = indexOf(currentCoins.get(i));
					coinQuantities.set(index, (coinQuantities.get(index) +1));
				}
				currentCoins.clear();
				ReadWrite.writeQuantity(coinQuantities,"coinQuantities.txt");
				ReadWrite.writeQuantity(productQuantities,"productQuantities.txt");
			}
			else {
				throw new VendingException("Insufficent money in machine. Please insert more coins into machine.");
			}
		}
		else {
			throw new VendingException("Product is out of stock in machine.");
		}
	}
	
	/** 
		This method 'removes' a pre-selected coin from the coins arraylist by setting it's parallel quantity in the coinQuantities arraylist to zero
		@param Coin c : The type of coin the operator wants to remove from the machine
		@return int : the amount of money removed
		@throws IOException e
	*/
	public int removeMoneyOp(Coin c) throws IOException {
		int index = indexOf(c);
		int quantity = coinQuantities.get(index);
		
		if(quantity > 0) {
			coinQuantities.set(index, 0);
			ReadWrite.writeQuantity(coinQuantities,"coinQuantities.txt");
		}
		else
			throw new VendingException("There are currently no coins of this type in the machine.");
		
		return quantity;
	}
	
	/**
		This method returns the current value of coins in the currentCoins arraylist
		@return double : current value of coins in the currentCoins arraylist
	*/
	public double currentCoinsValue() {
		double total = 0.0;
		for(Coin aCoin : currentCoins) {
			total += aCoin.getValue();
		}
		return total;
	}
	
	/**
		This coin returns the quantity of a Coin
		@param Coin c : the coin you want to get the quantity of
		@return int : the quantity of the coin
	*/
	public int getCoinQuantity(Coin c) {
		int index = indexOf(c);
		int quantity = coinQuantities.get(index);
		return quantity;
	}
	
	/**
		This method takes an object, checks whether its an instanceof Coin or Product, and then finds its index in coins or products respectively by comparing
		its datafields with the other objects in the arraylist
		@param Object o : the object you want to get the indexOf
		@return int : index of the Object in its respective arraylist
	*/
	public int indexOf(Object o) {
		boolean found = false;
		int index = -1;
		
		if(o instanceof Product) {
			Product p = ((Product)o);
			for(int i = 0; i < products.size() && !found; i++) {
				if((products.get(i).getDescription().equals(p.getDescription())) && (products.get(i).getPrice() == p.getPrice())) {
					found = true;
					index = i;
				}
			}
		}
		else if (o instanceof Coin) {
			Coin c = ((Coin)o);
			for(int i = 0; i < coins.size() && !found; i++) {
				if((coins.get(i).getName().equals(c.getName())) && (coins.get(i).getValue() == c.getValue())) {
					found = true;
					index = i;
				}
			}
		}
		return index;
	}
}