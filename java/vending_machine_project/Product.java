/**
	A product in a vending machine.
*/
public class Product
{  
	private String description;
	private double price;
	/**
		Constructs a Product object
		@param aDescription the description of the product
		@param aPrice the price of the product
	*/
	public Product(String aDescription, double aPrice)
    {  
      this.description = aDescription;
      this.price = aPrice;
    }
	
	/**
		Returns the prince of the prduct
	*/
	public double getPrice() {
		return this.price;
	}
	
	
	/**
		Returs the description of the product
	*/
	public String getDescription() {
		return this.description;
	}
	
	/**
		Format for the text files
	*/
	public String format() {
		return this.description+","+this.price;
	}
	
	/**
		What will be returned when you want to print a product
	*/
	public String toString() {
		return "\nDescription: "+this.description+"\nPrice: "+this.price;
	}
}
