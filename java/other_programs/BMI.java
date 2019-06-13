import java.util.Scanner;

public class BMI {
	public static void main(String[]args) {
		Scanner aScanner= new Scanner(System.in);
		String userInput="";
		double weight = 0.0, height = 0.0;
		
		System.out.print("Enter your weight in kilograms: ");
		userInput = aScanner.nextLine();
		weight = validateInput(userInput,true);
		
		System.out.print("Enter your height in centimeters: ");
		userInput = aScanner.nextLine();
		height = validateInput(userInput,false);
		
		calculateBMI(weight, height);
		
	}
	
	public static double validateInput(String userInput, boolean progress) {
		String pattern = "[0-9]{1,3}";
		String e1 = "Error: Invalid Input detected.";
		String e2 = "Error: Value between 2 and 650 is required.";
		String e3 = "Error: Value between 30 and 300 is required";
		double result=0;
		userInput = userInput.replaceAll("[kg|Kg|KG|cm|Cm|CM]","");
		
		if(userInput.matches(pattern)){
			result = Double.parseDouble(userInput);
			if(progress && result<2 || result>650) {
				System.out.print(e2);
				System.exit(0);
			}
			else if(!progress && result <30 || result > 300) {
				System.out.print(e3);
				System.exit(0);
			}
		}
		else {
			System.out.print(e1);
			System.exit(0);
		}
		return result;
	}
	
	public static void calculateBMI(double weight, double height) {
		String category="";
		double BMI = (double) (weight/(height*height));
		BMI *=10000;
		
		if(BMI < 19)				category = "Underweight";
		else if(BMI>=19&&BMI<25)	category = "Acceptable";
		else if(BMI>=25&&BMI<30)	category = "Overweight";
		else if(BMI>=30&&BMI<40)	category = "Obese";
		else if(BMI>40)				category = "Morbidly Obese";
		
		System.out.printf("BMI:\t\t%.2f\nCategory:\t%s",BMI,category);
		
	}
}