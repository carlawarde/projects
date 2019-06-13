import javax.swing.JOptionPane;
import java.util.ArrayList;
public class Pangram {
	public static void main(String[]args) {
		String input = JOptionPane.showInputDialog(null, "Enter a sentence","Pangram Checker",1);
		ArrayList<String> alphabet = new ArrayList<String>();
		String pattern = "[a-z A-Z|.|,|;|:|'|!|\"|?|-|(|)]+";
		String copyInput,result,currentChar;
		int currentIndex=0,pangramIndex=0;
	
		if(input != null){
			if(!(input.matches(pattern)))
				result = "ERROR: Input should only contain alphabetic characters and punctuation.";
			else {
				copyInput = input;
				input = input.toLowerCase().replaceAll("[^a-z]","");

				for(int i=0; i< input.length() && alphabet.size() <= 26 ;i++) {
					currentChar = input.substring(i,i+1);
					if(!(alphabet.contains(currentChar))) {
						alphabet.add(currentChar);	
					}
				}
				if(alphabet.size() == 26)
					result = "\""+copyInput+"\" is a pangram.";
				else
					result = "\""+copyInput+"\" is not a pangram.";
			}
			JOptionPane.showMessageDialog(null,result,"Pangram Checker",1);
		}
	}
}