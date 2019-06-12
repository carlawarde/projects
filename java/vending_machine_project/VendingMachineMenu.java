import java.util.*;
import java.io.*;
import javafx.application.Application;
public class VendingMachineMenu {
	
	public void run() throws Exception {
		boolean more = true;
		Scanner in = new Scanner(System.in);
		while(more) {
			System.out.println("Welcome to VC Vending Machine V2.0");
			System.out.println("Please choose Interface Mode: C)ommand Line Interface G)raphical User Interface Q)uit");
			String input = in.nextLine().toUpperCase();
			
			if(input.equals("C")) {
				VendingMachineCLI CLI = new VendingMachineCLI();
				CLI.run();
				more = false;
			}
			else if(input.equals("G")) {
				javafx.application.Application.launch(VendingMachineGUI.class);
				more = false;
			}
			else if(input.equals("Q")) {
				more = false;
			}
		}
	}
}