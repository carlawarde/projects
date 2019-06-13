public class SnakeEyes {
	public static void main(String[] args) {
		int times, frequencyOfSnakeEyes;
		if(validateInput(args)) {
			times = Integer.parseInt(args[0]);
			frequencyOfSnakeEyes= simulateDie(times);
			reportOnSuccess(frequencyOfSnakeEyes,times);
		}
	}
	
	public static boolean validateInput(String[] args) {
		boolean validateInput = false;
		
		String e1 = "Usage: SnakeEyes numberOfSimulations";
		String e2 = "Format of input is incorrect, a whole number in the rang 1 to 500 is required.";
		String e3 = "Number supplied must be in range 1 to 500.";
		int value;
		
		String pattern = "[0-9]{1,3}";
		if(args.length!=1) 								System.out.print(e1); 
		else if(!(args[0].matches(pattern))) 			System.out.print(e2);
		else {
			value = Integer.parseInt(args[0]);
			if(value <1 || value>500) 				System.out.print(e3);
			else 									validateInput= true;
		}
		return validateInput;
		
	}
	
	public static int simulateDie(int times){
		int dieOne=0, dieTwo=0,success= 0;
		for(int i = 0; i< times; i++) {
		dieOne = (int) (Math.random()*5)+1;
		dieTwo = (int) (Math.random()*5)+1;
		
		if(i%10 == 0 && i!= 0 )
			System.out.print("\n");
		
		if(dieOne == 1 && dieTwo == 1) 
			success++;
		
		System.out.print(dieOne +" "+ dieTwo+"\t");
		}
		return success;
		
	}
	
	public static void reportOnSuccess(double frequencyOfSnakeEyes, double times) {
		double success = frequencyOfSnakeEyes/times;
		if(frequencyOfSnakeEyes < 1) 
			System.out.format("\n No Snake Eyes from %.0f simulations.", times);
		else 
			System.out.format("\n %.2f percent success from %.0f times i.e. %.0f occurrence(s) of Snake Eyes.",success, times, frequencyOfSnakeEyes);
	}
	
}