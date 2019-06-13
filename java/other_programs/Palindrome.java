import javax.swing.JOptionPane;
public class Palindrome {
	public static void main(String[] args){
		try {
			JOptionPane.showMessageDialog(null,"Welcome to the Palindrome Checker v1.0","Palindrome Checker",1);
			menu(0);
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
	
	public static void menu(int run) {
		try {
			String userInput="";
			int choice = 2;
			if(run != 0) {
				String[]options = {"Test again", "Quit"};
				userInput = (String) JOptionPane.showInputDialog(null,"Choose an option","Palindrome Checker",1,null,options,options[0]);
				if(userInput.equals("Quit")) System.exit(0);
			}
			
			if((userInput.equals("Test again")) || run == 0) {
				userInput = JOptionPane.showInputDialog(null,"Please enter a word or sentence","Palindrome Checker",1);
				
				if(!(validateInput(userInput))) {
					JOptionPane.showMessageDialog(null,"Input should not contain numeric or special characters besides punctuation","Palindrome Checker",1);
					menu(1);
				}
				else {
					palindromChecker(userInput);
				}	
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
	
	public static boolean validateInput(String userInput){
		String pattern = "[a-z A-Z|,|.|;|:|'|!|?|\"|-|(|)]+";
		boolean valid = false;
		
		if(userInput.matches(pattern)) valid = true;
		
		return valid;
	}
	
	public static void palindromChecker(String userInput) {
		try {
			boolean charPalindrome = true, wordPalindrome = true;
			int start = 0, end = 0, i=0;
			String result ="";
			String copyInput = userInput;
			userInput = userInput.toLowerCase().trim();
			userInput = userInput.replaceAll("[^a-z ]","");
			String[]wordArray = userInput.split(" ");
			if(wordArray.length == 1) wordPalindrome = false;
			
			userInput = userInput.replaceAll(" ","");
			end = userInput.length()-1;
			for(i=0; i <= userInput.length()/2 && charPalindrome; i++) {
				if(!(userInput.charAt(start) == userInput.charAt(end))) charPalindrome = false;
				start++;
				end--;
			}
			if(charPalindrome) wordPalindrome=false;
			
			if(wordPalindrome) {
				end = wordArray.length - 1;
				start = 0;
				for(i=0; i < wordArray.length/2 && wordPalindrome; i++) {
					if(!(wordArray[start].equals(wordArray[end]))) wordPalindrome = false;
					
					start++;
					end--;
				}
			}
			
			if(charPalindrome || wordPalindrome) 	result="\""+copyInput+"\""+" is a palindrome.";
			else									result="\""+copyInput+"\""+" is not a palindrome";

			JOptionPane.showMessageDialog(null,result,"Palindrome Checker",1);
			menu(1);
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