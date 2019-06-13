import javax.swing.*;
public class VowelsAndConsonants {
	public static void main(String[]args) {
		String input = JOptionPane.showInputDialog(null,"Enter a word");
		int vowelsNo = 0, consonantsNo = 0;
		String pattern = "[a-zA-Z]+";
		String error = "Input should only have alphabetical characters.";
		String vowels = "aeiou";
		String copyInput,result,currentChar;
		
		if(input!= null) {
			if(!(input.matches(pattern)))
				JOptionPane.showMessageDialog(null,error);
			else {
				copyInput = input;
				input = input.toLowerCase();
				for(int i = 0; i < input.length(); i++) {
					currentChar = input.substring(i,i+1);
					if(vowels.contains(currentChar))
						vowelsNo++;
					else
						consonantsNo++;
				}
				result = copyInput +" has "+vowelsNo+" vowels and "+consonantsNo+" consonants.";
				JOptionPane.showMessageDialog(null,result,"Result",1);
			}
		}
	}
}