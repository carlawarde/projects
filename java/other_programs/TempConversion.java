import java.util.Scanner;

public class TempConversion {
	public static void main(String[]args) {
		Scanner console = new Scanner(System.in);
		System.out.print("What type of conversion do you want to make?\nEnter 1 for Celsius to Fahrenheit, or 2 for Fahrenheit to Celsius.\t\t");
		String userInput = console.nextLine();
		String pattern = "[0-9]+";
		String result="", error1="ERROR: Invalid input detected. Input of 1 or 2 required.", error2="ERROR: Invalid input detected. Numerical input only.";
		if(userInput.equals("1")) {
			System.out.print("Enter Celsius value:\t\t");
			userInput = console.nextLine();
			if(!(userInput.matches(pattern))) result=error2;
			else {
				double inCelTemp = Double.parseDouble(userInput);
				double outFahrTemp = ((9.0/5.0)*inCelTemp)+32;
				result = inCelTemp+"degrees Celsius is the equivalent to "+ outFahrTemp+" degrees Fahrenheit.";
			}
		}
		else if(userInput.equals("2")) {
			System.out.print("Enter Fahrenheit value:\t\t");
			userInput = console.nextLine();
			if(!(userInput.matches(pattern))) result=error2;
			else {
				double inFahrTemp = Double.parseDouble(userInput);
				double outCelTemp = (5.0/9.0)*(inFahrTemp-32);
				result = inFahrTemp+" degrees Fahrenheit is the equivalent to "+outCelTemp+" degrees Celsius.";
			}
		}
		else {
			result=error1;
		}
		System.out.print(result);
	}
}