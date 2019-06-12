/**
	A coin with a monetary value.
*/
public class Coin
{
	private double value;
	private String name;

    /**
		Constructs a coin.
		@param aValue the monetary value of the coin.
		@param aName the name of the coin
    */
    public Coin(double aValue, String aName) 
    { 
		value = aValue; 
		name = aName;
    }

    /**
		Returns the monetary value of the coin
	*/
    public double getValue() {
		return this.value;
    }
	
	/**
		Returns the name of the coin
	*/
	public String getName() {
		return this.name;
	}
	
	/**
		Format for the text files
	*/
	public String format() {
		return this.value+","+this.name;
	}
	
	/**
		What will be returned when you want to print a coin
	*/
    public String toString() {
	    return "Coin: "+this.value;
    }
}