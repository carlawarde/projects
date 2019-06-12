/**
*	LeagueConfigurator is a program the allows administrators to login to the league management system and perform a variety of functions such as: 
*   	- Creating a new league
*		- Editing an existing league that they are the admin of
*		- Deleting a league
*	@author Carla Warde, Aine Reynolds, Vincent Kiely
*	@version 1.0
*	@since 28/3/18
*/
	
import javax.swing.*;
import java.util.*;
import java.io.*;

public class LeagueConfigurator{
	public static int numOfLeagues=0,numOfSports=0,winPoints = 0, drawPoints = 0, lossPoints= 0, numOfTeams = 0;
	public static String sportName = "",adminID="",leagueName="";
	public static boolean awayGames = false,additionalTeam = false;
	
	/** main method calls the admin method, the leagueCheckMethod 
	  * and teamsSetUp method.
	*/
	public static void main(String[] args) throws IOException {
		adminID = adminLogin();
		leagueCheck(adminID);
		teamsSetUp(leagueName);	
	}

	/** adminLogin method shows an input dialog box on the screen asking
	  * for the user id and password of the admin. if either of these don't match
	  * the text file with the admin id an error message will be displayed and the 
	  * admin has 2 more attempts.
	*/
	
	public static String adminLogin() throws IOException {
		File adminFile = new File("admins.txt");
		if(!(adminFile.exists())) {
			System.out.print("ERROR: Administrators' details cannot be recovered.");
			System.exit(1);
		}
		else {
			Scanner adminScanner = new Scanner(adminFile);
			String[]splitArray;
			ArrayList<ArrayList<String>> admins = new ArrayList<ArrayList<String>>();
			admins.add(new ArrayList<String>());
			admins.add(new ArrayList<String>());
			admins.add(new ArrayList<String>());	
			
			while(adminScanner.hasNext()) {
				splitArray = (adminScanner.nextLine()).split(",");
				admins.get(0).add(splitArray[0]);
				admins.get(1).add(splitArray[1]);
				admins.get(2).add(splitArray[2]);
			}
			
			boolean found = false;
			int attempts=0, i=0;
			String userName = JOptionPane.showInputDialog(null,"Welcome to League Generator 1.0\nPlease enter your administrator user name");
			if(userName == null )		System.exit(0);
			String userPassword = JOptionPane.showInputDialog(null,"Please enter your administrator password");
			if(userPassword == null)	System.exit(0);
			
			for(attempts = 0; attempts < 3 && !found; attempts++) {
				if(attempts > 0) {
					JOptionPane.showMessageDialog(null,"ERROR: Invalid administrator user name and password combination. You have "+(3-attempts)+" attempt(s) remaining.");
					userName = JOptionPane.showInputDialog(null,"Please enter your administrator user name");
					if(userName == null )		System.exit(0);
					userPassword = JOptionPane.showInputDialog(null,"Enter your administrator password");
					if(userPassword == null)	System.exit(0);
				}

				for(i = 0 ;i < admins.get(0).size() && !found;i++) {
					if ((admins.get(1).get(i)).equals(userName) && (admins.get(2).get(i)).equals(userPassword))
						found = true;   
				}
			}
			if(found)
				adminID = admins.get(0).get(i-1);
			else {
				System.out.print("Unauthorised access detected. Program shut down initiated.");
				System.exit(1);
			}
			adminScanner.close();
			return adminID;
		}
		return "Return statements are fucking annoying";
	}

	/** leagueCheck method calls the leagueScan method which scans the 
	  * league text file. Then goes through the array list with the leagues
	  * and checks if the admin id matches the leagues. It then calls the leagueSetUp with the adminID and their leagues
	*/ 
	
	public static void leagueCheck(String adminID) throws IOException {
		ArrayList<ArrayList<String>> leagues = leagueScan();
			
		ArrayList<String> thisAdminsLeagues = new ArrayList<String>();
		int i = 0;
		while(i < leagues.get(0).size()) {
			if((leagues.get(3).get(i)).equals(adminID)) 
				thisAdminsLeagues.add(leagues.get(1).get(i));
			i++;
		}
		String[]adminsLeagues = thisAdminsLeagues.toArray(new String[thisAdminsLeagues.size()]);
		leagueSetUp(adminsLeagues, adminID);	
	}
	
	/** league set up gives the user the option of editing an existing league or
	  * creating a new one. If you choose to create a new league it allows you to 
	  * choose the type of sport or input a new one. It calls the getRuleForASport 
	  * method if you choose one of the existing sports. If you create a new sport it calls
	  * sportSetUp method. Editing an existing league will give the user options of their 
	  * existing leagues to edit. And from there you can either delete the league, or edit the league configuration
	*/ 
	
	public static void leagueSetUp(String[]thisAdminsLeagues, String adminID) throws IOException {
		ArrayList<String> options = new ArrayList<String>();
		options.add("Create a new league");
		if(thisAdminsLeagues.length > 0)
			options.add("Edit existing league(s)");
		
		String[]option = options.toArray(new String[options.size()]);
		String choice = (String) JOptionPane.showInputDialog(null,"Select an option","Input",1,null,option,option[0]);
		if(choice == null)
			System.exit(1);
		else {
			ArrayList<ArrayList<String>> rules = sportScanner();
			if(choice.equals(options.get(0))) {

				FileWriter leagueWriter = new FileWriter("leagues.txt",true);
				PrintWriter leaguePrinter = new PrintWriter(leagueWriter);

				String[]sportsNames = rules.get(1).toArray(new String[rules.get(1).size()+1]);
				numOfSports = rules.get(0).size();
					
				sportsNames[sportsNames.length-1] = "Other";
				leagueName = (String) JOptionPane.showInputDialog(null,"Enter league name");
				if(leagueName == null)					System.exit(0);
				sportName = (String) JOptionPane.showInputDialog(null,"What sports category is the League in?","Input",1,null,sportsNames,sportsNames[0]);
				if(sportName == null)					System.exit(0);	
				else if(sportName.equals("Other"))		sportName = sportSetUp(numOfSports,sportsNames);
				else {
					getRuleForASport(rules,sportName);
					String result = "The rules for "+sportName+" are:\nWin points: "+winPoints+"\nDraw points: "+drawPoints+"\nLoss points: "+lossPoints+"\nAway games: "+awayGames;
					JOptionPane.showMessageDialog(null,result);
					boolean validInput = false;
					String pattern = "[y|n]";
					String userInput = JOptionPane.showInputDialog(null,"Do you wish to use these settings? (y/n)");
					int attempts = 0;
					while(!validInput) {
						if(attempts > 0)
							userInput = JOptionPane.showInputDialog(null,"ERROR: Invalid input detected. Please enter \"y\" to use these settings, or \"n\" to set up new settings");
						if(userInput == null)					System.exit(0);
						else if(userInput.matches(pattern))		validInput = true;
						attempts++;
					}
					if(userInput.equals("n"))
						sportSetUp(numOfSports,sportsNames);
				}
				leaguePrinter.println((numOfLeagues+1)+","+leagueName+","+sportName+","+adminID);
				numOfLeagues++;
				leaguePrinter.close(); leagueWriter.close();
			}
			else if(choice.equals(options.get(1))) {
				leagueName = (String) JOptionPane.showInputDialog(null,"Choose a league","Input",1,null,thisAdminsLeagues,thisAdminsLeagues[0]);
				if(leagueName == null)		System.exit(0);
				
				ArrayList<ArrayList<String>> leagues = leagueScan();
				String[]userChoices = {("Edit "+leagueName+" configuration"),("Delete "+leagueName)};
				String userInput = (String) JOptionPane.showInputDialog(null,"Choose an option","Input",1,null,userChoices,userChoices[0]);
				if(userInput.equals(userChoices[1]))
					deleteLeague(leagues);
				
				int sportIndex = leagues.get(1).indexOf(leagueName);
				sportName = leagues.get(2).get(sportIndex);
				getRuleForASport(rules,sportName);
			}	
		}
	}

	/** This method gives the admin the option to delete one of their leagues.
	 * after it deletes the league the programme exits.
	 */ 
	
	public static void deleteLeague(ArrayList<ArrayList<String>>leagues) throws IOException {
		int index = leagues.get(1).indexOf(leagueName);

		leagues.get(0).remove(index);		leagues.get(1).remove(index);		leagues.get(2).remove(index);		leagues.get(3).remove(index);
		numOfLeagues--;
		
		String[]files = {" Teams"," Fixtures"," Results"," Unordered Leaderboard"," Leaderboard"};
		File aFile = new File(".");
		for(int i = 0; i < files.length; i++) {
			aFile = new File(leagueName+files[i]+".txt");
			if(aFile.exists())
				aFile.delete();
		}
		
		FileWriter leagueWriter = new FileWriter("leagues.txt");
		PrintWriter leaguePrinter = new PrintWriter(leagueWriter);
		for(int i = 0; i< leagues.get(0).size(); i++) {
			leaguePrinter.println((i+1)+","+leagues.get(1).get(i)+","+leagues.get(2).get(i)+","+leagues.get(3).get(i));
		}
		leaguePrinter.close();
		leagueWriter.close();
		numOfLeagues--;
		
		int attempts = 0;
		boolean validInput = false;
		String pattern = "[y|n]";
		JOptionPane.showMessageDialog(null,"League successfully deleted. The program will now shut down.");
		System.exit(0);
	}

	/** sportSetUp method allows admin to set up a sport of their choice.
	  * It checks whether that sport name already exists. They can add their
	  * own points for a win, lose or draw and can choose whether or not there's away games.
	*/
	
	public static String sportSetUp(int sportCount,String[]sportsNames) throws IOException {
		
		FileWriter sportWriter = new FileWriter("sports.txt",true);
		PrintWriter sportPrinter = new PrintWriter(sportWriter);
		boolean found = false, overallFound = true;
		sportName = JOptionPane.showInputDialog(null,"Enter the name of the sport");
		while(overallFound) {
			if(found)
				sportName = JOptionPane.showInputDialog(null,"This sport already exists. Please enter a different sport.");
			if(sportName == null)	System.exit(0);
			for(int i =0; i< sportsNames.length && !found; i++) {
				if(sportName.equals(sportsNames[i]))
					found = true;
			}
			overallFound = found;
		}
		String userInput="", pattern1 = "[0-9]{1,}";
		boolean valid = false;
		int attempts = 0;
		userInput = JOptionPane.showInputDialog(null,"Enter the number of points for a win in this League");
		while(!valid) {
			if(attempts > 0)
				userInput = JOptionPane.showInputDialog(null,"ERROR: Invalid input detected. Natural numerical input is required.\nPlease enter the number of points for a win in this League");
			if(userInput.matches(pattern1))	 {		winPoints = Integer.parseInt(userInput);		attempts = 0;		valid = true;}
			else if(userInput == null) 				System.exit(0);
			else 									attempts++;
		}
		valid = false;
		userInput = JOptionPane.showInputDialog(null,"Enter the number of points for a draw in this League");
		while(!valid) {
			if(attempts > 0)
				userInput = JOptionPane.showInputDialog(null,"ERROR: Invalid input detected. Natural numerical input is required.\nPlease enter the number of points for a draw in this League");
			if(userInput.matches(pattern1))	 {		drawPoints = Integer.parseInt(userInput);		attempts = 0;		valid = true;}
			else if(userInput == null) 				System.exit(0);
			else 									attempts++;
		}
		valid = false;
		userInput = JOptionPane.showInputDialog(null,"Enter the number of points for a loss in this League");
		while(!valid) {
			if(attempts > 0)
				userInput = JOptionPane.showInputDialog(null,"ERROR: Invalid input detected. Natural numerical input is required.\nPlease enter the number of points for a loss in this League");
			if(userInput.matches(pattern1))	 {		lossPoints = Integer.parseInt(userInput);		attempts = 0;		valid = true;}
			else if(userInput == null) 				System.exit(0);
			else 									attempts++;
		}
		valid = false;
		userInput = JOptionPane.showInputDialog(null, "Are there away games in this league? (true/false)");
		while(!valid) {
			if(attempts > 0)
				userInput = JOptionPane.showInputDialog(null,"ERROR: Invalid input detected. \"true\" or \"false\" is required.\nAre there away fixtues in this league?");
			if(userInput.equalsIgnoreCase("true") || userInput.equalsIgnoreCase("false"))	{	awayGames = Boolean.parseBoolean(userInput);	valid = true;}
			else if(userInput == null) 				System.exit(0);
			else 									attempts++;
		}	
		sportPrinter.println((sportCount+1)+","+sportName+","+winPoints+","+drawPoints+","+lossPoints+","+awayGames);
		numOfSports++;
		sportPrinter.close(); sportWriter.close();
		return sportName;	
	}

	/** It reads the rules (winPoints,drawPoints,lossPoints,awayGames) for the sport the user selects.
	*/
	
	public static void getRuleForASport(ArrayList<ArrayList<String>> rules, String sportName) throws IOException {
		int sportIndex = 0;
		sportIndex = rules.get(1).indexOf(sportName);
		if(sportIndex == -1) {
			JOptionPane.showMessageDialog(null,"ERROR: Sport not found. Returning to League menu");
			leagueCheck(adminID);	
		}
		winPoints = Integer.parseInt(rules.get(2).get(sportIndex));
		drawPoints = Integer.parseInt(rules.get(3).get(sportIndex));
		lossPoints = Integer.parseInt(rules.get(4).get(sportIndex));
		awayGames = Boolean.parseBoolean(rules.get(5).get(sportIndex));
	}
	
	/** the admin can input the names of their teams and the method 
	  * calls the scanTeams method to make sure they don't already exists.
	  * They can then finish adding teams when there's a minimum of two teams. the method then calls the fixtureGenerationMethod
	  * which generate the fixtures.
	*/
	public static void teamsSetUp(String leagueName) throws IOException
	{
		ArrayList<ArrayList<String>> teams = scanTeams(leagueName);
		String team ="";
		
		if(teams == null || !(teams.get(0).size() > 0)) 
		{
			FileWriter aFileWriter = new FileWriter(leagueName+" Teams.txt",true);
			PrintWriter output = new PrintWriter(aFileWriter);
			boolean exists = true;
			String[]theseAmazingOptions = {"Add team","Team addition completed"};
			String choice="";
			while(choice.equals("Add team") || numOfTeams < 2) 
			{
				choice = (String) JOptionPane.showInputDialog(null,"Choose an option (There must be a minimum of two teams)","Input",1,null,theseAmazingOptions,theseAmazingOptions[0]);
				if(choice == null)		System.exit(0);
				while(exists && choice.equals("Add team")) 
				{
					team =  JOptionPane.showInputDialog(null,"Please enter name of team "+(numOfTeams+1));
					if(team == null)	System.exit(0);
					if (teams == null || !(teams.get(1).contains(team)))
					{
						teams.get(1).add(team);
						output.println((numOfTeams+1)+","+team);
						numOfTeams++;
						exists = false;
					}
					else 
						JOptionPane.showMessageDialog(null,"Team name already exists. Please try again.");
				}
				exists = true;
			}
			output.close();
			aFileWriter.close();
		}	
		else 
		{
			String result = "The teams for the "+leagueName+" are as follows:\n";
			for(int i = 0; i < teams.get(0).size();i++)
			{
				result+=(i+1)+". "+teams.get(1).get(i)+"\n";
			}
			JOptionPane.showMessageDialog(null,result);
		}
		fixtureGeneration(leagueName,teams.get(1));
	}

	/** This method generates the fixtures for the league and writes them to a text 
	  * file. it then calls the diplayFixtures method to output fixtures on screen.
	*/ 
	
	public static void fixtureGeneration(String leagueName, ArrayList<String> teams ) throws IOException {
		int totalRounds,totalNumRounds, matchesPerRound, roundNum, matchNum, teamOne, teamTwo, even, odd, matchCount = 1;
		String [][] fixtures;
		String [][] revisedFixtures;
		String [] elementsOfFixture;
		String[][]awayFixtures;
		String reversedFixtureArray[];
		String temp ="";
		
		ArrayList<ArrayList<String>>listFixtures = scanFixtures(leagueName);
		
		if(teams.size()%2 == 1)	 {	
				numOfTeams++;
				teams.add("Bye-Team");
				JOptionPane.showMessageDialog(null,"A Bye-Team has been added due to the odd number of teams in the league.");
		}
		
		totalRounds = numOfTeams-1;
		totalNumRounds = totalRounds;
		matchesPerRound = numOfTeams/2;
		
		if(listFixtures == null || !(listFixtures.get(0).size() > 0)) {
			FileWriter fixtureWriter = new FileWriter(leagueName+" Fixtures.txt");
			PrintWriter fixturePrinter = new PrintWriter(fixtureWriter);
		
			if(awayGames) 	totalNumRounds *= 2;
		
			fixtures = new String[totalRounds][matchesPerRound];
		
			for(roundNum = 0; roundNum < totalRounds; roundNum++) {
				for(matchNum = 0; matchNum < matchesPerRound; matchNum++) {
					teamOne = (roundNum + matchNum) % (numOfTeams-1);
					teamTwo = (numOfTeams- 1 - matchNum + roundNum) % (numOfTeams-1);
				
					if(matchNum == 0) 		
						teamTwo = numOfTeams-1;
				
					fixtures[roundNum][matchNum] = (teamOne)+","+(teamTwo);
				}
			}
			revisedFixtures = new String[totalRounds][matchesPerRound];
			even = 0;
			odd = numOfTeams/2;
		
			for( int i =0; i < fixtures.length; i++) {
				if(i%2 == 0)
					revisedFixtures[i] = fixtures[even++];
				else
					revisedFixtures[i] = fixtures[odd++];
			}
		
			awayFixtures = new String[totalRounds][matchesPerRound];
			String[][] finalisedFixtures = new String[totalNumRounds][matchesPerRound];
		
			for(roundNum = 0; roundNum < totalRounds; roundNum++) {
				for(matchNum = 0; matchNum < matchesPerRound; matchNum++) {
					finalisedFixtures[roundNum][matchNum] = revisedFixtures[roundNum][matchNum];
				}
			}
			for(roundNum = 0; roundNum < totalRounds && awayGames ; roundNum++) {
				for(matchNum = 0; matchNum < matchesPerRound; matchNum++) {
					reversedFixtureArray = revisedFixtures[roundNum][matchNum].split(",");
					finalisedFixtures[roundNum+(totalNumRounds/2)][matchNum] = reversedFixtureArray[1]+","+reversedFixtureArray[0];
				}
			}
			for(roundNum = 0; roundNum < totalNumRounds; roundNum++) {
				for(matchNum = 0; matchNum < matchesPerRound; matchNum++) {
					fixturePrinter.println(matchCount+","+finalisedFixtures[roundNum][matchNum]);
					matchCount++;
				}
			}
			fixturePrinter.close();
			fixtureWriter.close();
			listFixtures = scanFixtures(leagueName);
			JOptionPane.showMessageDialog(null,"Fixtures have been successfully generated.");
		}
		displayFixtures(listFixtures,teams,matchesPerRound);
	}

	/** This method displays allows the admin to choose if they would like to view the fixtures
	  * and if so which round. when they choose a round it displays the fixures for that round. When the admin selects that they are finished viewing fixtures, it calls the 
	  * displayResults method.
	*/
	
	public static void displayFixtures(ArrayList<ArrayList<String>>fixtures,ArrayList<String>teams,int matchesPerRound) throws IOException {
		String[]roundNumbers = new String[(fixtures.get(0).size()/matchesPerRound)];
		String[]split;
		String result="",teamOne,teamTwo,match,userChoice="",pattern = "[y|n]";
		int teamOneIndex,teamTwoIndex;
		boolean invalid = true, finished = false;
		for(int i = 0; i < roundNumbers.length; i++)
			roundNumbers[i] = ""+(i+1);
		
		while(!finished) {
			while(invalid) {
				userChoice = JOptionPane.showInputDialog(null,"Would you like to view the fixtures? (y/n)");
				if(userChoice == null)						System.exit(0);
				else if(!(userChoice.matches(pattern))) 	JOptionPane.showMessageDialog(null,"ERROR: Invalid input detected.\"y\" or \"n\" is the required input.");
				else										invalid = false;
			}
			if(userChoice.equalsIgnoreCase("n"))		finished = true;
			else if(userChoice.equalsIgnoreCase("y")) {
				
				userChoice = (String) JOptionPane.showInputDialog(null,"Which round would you like to view the fixtures for?","Input",1,null,roundNumbers,roundNumbers[0]);
				if(userChoice == null) System.exit(0);
				
				int roundChoice = Integer.parseInt(userChoice);
		
				for(int i = 0; i < matchesPerRound; i++) {
					teamOneIndex = Integer.parseInt(fixtures.get(1).get(i+(matchesPerRound*(roundChoice-1))));
					teamTwoIndex = Integer.parseInt(fixtures.get(2).get(i+(matchesPerRound*(roundChoice-1))));
					teamOne = teams.get(teamOneIndex);
					teamTwo = teams.get(teamTwoIndex);
					result+= (i+1)+". "+teamOne+" vs "+teamTwo+"\n";
				}
				JOptionPane.showMessageDialog(null,result,"Round "+roundChoice+" Fixtures",1,null);
				invalid = true;
				result = "";
			}
		}
		displayResults(fixtures,teams, matchesPerRound);
	}

	/** displayResults gives the admin the option of viewing the results if the results of that league have been previously entered.
	  * if they would like to view them they can choose which round they 
	  * would like to view.it then calls the editResults method when the admin is finished viewing the results.
	*/
	
	public static void displayResults(ArrayList<ArrayList<String>>fixtures,ArrayList<String>teams,int matchesPerRound) throws IOException {
		ArrayList<ArrayList<Integer>> results = resultsScanner(leagueName);
		if(results.get(0).size() > 0) {
			String[]roundNumbers = new String[(fixtures.get(0).size()/matchesPerRound)];
			String[]split;
			String result="",teamOne,teamTwo,match,userChoice="",pattern = "[y|n]";
			int teamOneIndex,teamTwoIndex,teamOneScore,teamTwoScore;
			boolean invalid = true, finished = false;
			for(int i = 0; i < roundNumbers.length; i++)
				roundNumbers[i] = ""+(i+1);
		
			while(!finished) {
				while(invalid) {
					userChoice = JOptionPane.showInputDialog(null,"Would you like to view the results? (y/n)");
					if(userChoice == null)						System.exit(0);
					else if(!(userChoice.matches(pattern))) 	JOptionPane.showMessageDialog(null,"ERROR: Invalid input detected.\"y\" or \"n\" is the required input.");
					else										invalid = false;
				}
				if(userChoice.equalsIgnoreCase("n"))		
					finished = true;
				else if(userChoice.equalsIgnoreCase("y")) {
				
					userChoice = (String) JOptionPane.showInputDialog(null,"Which round would you like to view the results for?","Input",1,null,roundNumbers,roundNumbers[0]);
					if(userChoice == null) System.exit(0);
				
					int roundChoice = Integer.parseInt(userChoice);
		
					for(int i = 0; i < matchesPerRound; i++) {
						teamOneIndex = Integer.parseInt(fixtures.get(1).get(i+(matchesPerRound*(roundChoice-1))));
						teamTwoIndex = Integer.parseInt(fixtures.get(2).get(i+(matchesPerRound*(roundChoice-1))));
						teamOne = teams.get(teamOneIndex);
						teamTwo = teams.get(teamTwoIndex);
						result+= (i+1)+". "+teamOne+" vs "+teamTwo+"\n";
						teamOneScore = results.get(1).get(i+(matchesPerRound*(roundChoice-1)));
						teamTwoScore = results.get(2).get(i+(matchesPerRound*(roundChoice-1)));
						result+="         "+teamOneScore+"   -   "+teamTwoScore+"\n";
					}
					JOptionPane.showMessageDialog(null,result,"Round "+roundChoice+" Fixtures",1,null);
					invalid = true;
					result = "";
				}
			}
		}
		editResults(fixtures,results,teams);
	}

	/** The admin can choose if they would like to edit the results of the fixture.
	  * they can then enter the scores scored by both the home team and away team.
	  * then it calls the resultsCalculator method.
	*/
	public static void editResults(ArrayList<ArrayList<String>>fixtures, ArrayList<ArrayList<Integer>>results, ArrayList<String>teams) throws IOException
	{
		String userChoice = "";
		if(results.get(0).size() > 0) {
			boolean validInput = false;
			String pattern = "[y|n]";
			String userInput = JOptionPane.showInputDialog(null,"Do you want to edit the results of these fixtures? (y/n)");
			int attempts = 0;
			while(!validInput) {
				if(attempts > 0)
					userInput = JOptionPane.showInputDialog(null,"ERROR: Invalid input detected. Please enter \"y\" to edit the results, or \"n\" to generate continue.");
				if(userInput == null)					System.exit(0);
				else if(userInput.matches(pattern))		validInput = true;
					attempts++;
			}
		}
		
		if(results == null || !(results.get(0).size() > 0) || userChoice.equalsIgnoreCase("y")) 
		{
			String homeTeam,awayTeam; 
			int matchNumber = 1,homeTeamIndex,awayTeamIndex,homePoints,awayPoints,attempts = 0;
		
			FileWriter resultsWriter = new FileWriter(leagueName + " Results.txt",true);
			PrintWriter resultsPrinter = new PrintWriter(resultsWriter);
			boolean valid = false;
			String pattern = "[0-9]{1,}",userInput;
			for(int i = 0; i < fixtures.get(0).size(); i++) 
			{
				homeTeamIndex = Integer.parseInt(fixtures.get(1).get(i));
				awayTeamIndex = Integer.parseInt(fixtures.get(2).get(i));
				homeTeam = teams.get(homeTeamIndex);
				awayTeam = teams.get(awayTeamIndex);
				
				JOptionPane.showMessageDialog(null,"Fixture "+matchNumber+": "+homeTeam+ " vs "+awayTeam);
				userInput = JOptionPane.showInputDialog(null,"Please enter "+homeTeam+"'s score");
				while(!valid) 
				{
					if(attempts > 0)
						userInput = JOptionPane.showInputDialog(null,"ERROR: Invalid input detected. Natural numerical input is required.\nPlease enter "+homeTeam+"'s score.");
					if(userInput == null) 				System.exit(0);
					else if(userInput.matches(pattern))	 
					{		
						attempts = 0;		
						valid = true;
					}
					else 									attempts++;
				}
				valid = false;
				homePoints = Integer.parseInt(userInput);
				
				userInput = JOptionPane.showInputDialog(null,"Please enter "+awayTeam+"'s score");
				while(!valid) 
				{
					if(attempts > 0)
						userInput = JOptionPane.showInputDialog(null,"ERROR: Invalid input detected. Natural numerical input is required.\nPlease enter "+awayTeam+"'s score");
					if(userInput == null) 				System.exit(0);
					else if(userInput.matches(pattern))	 
					{		
						attempts = 0;		
						valid = true;
					}
					else 									attempts++;
				}
				valid = false;
				awayPoints = Integer.parseInt(userInput);
				results.get(0).add(matchNumber);
				results.get(1).add(homePoints);
				results.get(2).add(awayPoints);
				resultsPrinter.println(matchNumber + "," + homePoints + "," + awayPoints);
				matchNumber++;
			}
		
			resultsPrinter.close();
			resultsWriter.close();
		}
		resultsCalculator(fixtures,results,teams);
	}

	/** Sequentially scans through the results and calculates which team won or lost 
	  * or if they drew, and then updates that teams score/gd etc. It then calls the orderLeaderBoard method
	*/
	
	public static void resultsCalculator(ArrayList<ArrayList<String>>fixtures,ArrayList<ArrayList<Integer>>results,ArrayList<String>teams) throws IOException {
		ArrayList<ArrayList<Integer>>resultsCalculations = new ArrayList<ArrayList<Integer>>();
		resultsCalculations.add(new ArrayList<Integer>(teams.size()));	//team#
		resultsCalculations.add(new ArrayList<Integer>(teams.size()));	//Wins
		resultsCalculations.add(new ArrayList<Integer>(teams.size()));	//Draws
		resultsCalculations.add(new ArrayList<Integer>(teams.size()));	//Losses
		resultsCalculations.add(new ArrayList<Integer>(teams.size()));	//GoalDifference
		resultsCalculations.add(new ArrayList<Integer>(teams.size()));	//TotalPoints
		
		for(int i = 0; i < resultsCalculations.size(); i++) {
			for(int j = 0; j < teams.size(); j++) {
				resultsCalculations.get(i).add(0);
			}
		}
		
		int teamOneIndex,teamTwoIndex,teamOneScore,teamTwoScore,homeGD,awayGD,homePoints,awayPoints,win,loss,draw;
		String teamOne,teamTwo;
		
		File aFile = new File(leagueName+" Unordered Leaderboard.txt");
		if(!(aFile.exists()))
			aFile.createNewFile();
			
		FileWriter calculationsWriter = new FileWriter(aFile);
		PrintWriter calculationsPrinter = new PrintWriter(calculationsWriter);
		
		for(int i = 0; i < fixtures.get(0).size(); i++) {
			
			teamOneIndex = Integer.parseInt(fixtures.get(1).get(i));
			teamTwoIndex = Integer.parseInt(fixtures.get(2).get(i));
			
			teamOneScore = results.get(1).get(i);
			teamTwoScore = results.get(2).get(i);
			
			homeGD = teamOneScore-teamTwoScore;
			homeGD+= resultsCalculations.get(4).get(teamOneIndex);
			resultsCalculations.get(4).set(teamOneIndex,homeGD);
			awayGD = teamTwoScore-teamOneScore;
			awayGD+= resultsCalculations.get(4).get(teamTwoIndex);
			resultsCalculations.get(4).set(teamTwoIndex,awayGD);
			
			if(teamOneScore > teamTwoScore) {
				win = resultsCalculations.get(1).get(teamOneIndex) +1;
				resultsCalculations.get(1).set(teamOneIndex,win);
				loss = resultsCalculations.get(3).get(teamTwoIndex) + 1;
				resultsCalculations.get(3).set(teamTwoIndex,loss);
				
				homePoints = resultsCalculations.get(5).get(teamOneIndex)+winPoints;
				resultsCalculations.get(5).set(teamOneIndex,homePoints);
				awayPoints = resultsCalculations.get(5).get(teamTwoIndex)+lossPoints;
				resultsCalculations.get(5).set(teamTwoIndex,awayPoints);
			}
			else if( teamOneScore == teamTwoScore) {
				draw = resultsCalculations.get(2).get(teamOneIndex) +1;
				resultsCalculations.get(2).set(teamOneIndex,draw);
				draw = resultsCalculations.get(2).get(teamTwoIndex) + 1;
				resultsCalculations.get(2).set(teamTwoIndex,draw);
				
				homePoints = resultsCalculations.get(5).get(teamOneIndex)+drawPoints;
				resultsCalculations.get(5).set(teamOneIndex,homePoints);
				awayPoints = resultsCalculations.get(5).get(teamTwoIndex)+drawPoints;
				resultsCalculations.get(5).set(teamTwoIndex,awayPoints);
			}
			else if(teamTwoScore > teamOneScore) {
				win = resultsCalculations.get(1).get(teamTwoIndex) +1;
				resultsCalculations.get(1).set(teamTwoIndex,win);
				loss = resultsCalculations.get(3).get(teamOneIndex) + 1;
				resultsCalculations.get(3).set(teamOneIndex,loss);
				
				homePoints = resultsCalculations.get(5).get(teamOneIndex)+lossPoints;
				resultsCalculations.get(5).set(teamOneIndex,homePoints);
				awayPoints = resultsCalculations.get(5).get(teamTwoIndex)+winPoints;
				resultsCalculations.get(5).set(teamTwoIndex,awayPoints);
			}
		}
		
		for(int i = 0; i < teams.size(); i++) {
			calculationsPrinter.println((i)+","+resultsCalculations.get(1).get(i)+","+resultsCalculations.get(2).get(i)+","+resultsCalculations.get(3).get(i)+","+resultsCalculations.get(4).get(i)+","+resultsCalculations.get(5).get(i));
		}
		calculationsPrinter.close();
		calculationsWriter.close();
		resultsCalculations = scanUnorderedLeaderboard(leagueName);
		orderLeaderboard(resultsCalculations,teams);
	}

	/** Orders the the unorderedLeaderboard based on Total points > Goal Difference > Alphabetical Order
	* Then it calls the scanLeaderboard() method
	*/
	
	public static void orderLeaderboard(ArrayList<ArrayList<Integer>>resultsCalculations,ArrayList<String>teams) throws IOException {
		ArrayList<ArrayList<Integer>> ordered = scanUnorderedLeaderboard(leagueName);
		
		Collections.sort(ordered.get(5),Collections.reverseOrder());

		ArrayList<Integer>usedTeams = new ArrayList<Integer>();
		ArrayList<Integer>instances = new ArrayList<Integer>();
		int oldTeamIndex=0,currentScore=0,firstGD,secondGD,compare;
		String teamOne,teamTwo;
		
		for(int i = 0; i < numOfTeams; i++) {
			currentScore = ordered.get(5).get(i);
			
			for(int j = 0; j < teams.size(); j++) {
				if(resultsCalculations.get(5).get(j) == currentScore && !(usedTeams.contains(j)))
					instances.add(j);
			}
			
			if(instances.size() > 1) {
				firstGD = resultsCalculations.get(4).get(instances.get(0));
				secondGD = resultsCalculations.get(4).get(instances.get(1));
				
				if(firstGD > secondGD)			oldTeamIndex = instances.get(0);
				else if(secondGD > firstGD)		oldTeamIndex = instances.get(1);
				else if(firstGD == secondGD) {
					teamOne = teams.get(resultsCalculations.get(0).get(instances.get(0)));
					teamTwo = teams.get(resultsCalculations.get(0).get(instances.get(0)));
					
					compare = teamOne.compareTo(teamTwo);  

					if (compare < 0)  
						oldTeamIndex = instances.get(0);
					else if (compare > 0) 
						oldTeamIndex = instances.get(1);
				}
			}
			else
				oldTeamIndex = instances.get(0);
			
			ordered.get(0).set(i,resultsCalculations.get(0).get(oldTeamIndex));
			ordered.get(1).set(i,resultsCalculations.get(1).get(oldTeamIndex));
			ordered.get(2).set(i,resultsCalculations.get(2).get(oldTeamIndex));
			ordered.get(3).set(i,resultsCalculations.get(3).get(oldTeamIndex));
			ordered.get(4).set(i,resultsCalculations.get(4).get(oldTeamIndex));
			instances.clear();
			usedTeams.add(oldTeamIndex);
		}
		
		FileWriter leaderboardWriter = new FileWriter(leagueName+ " Leaderboard.txt");
		PrintWriter leaderboardPrinter = new PrintWriter(leaderboardWriter);
		String team;
		for(int i = 0; i < numOfTeams; i++){
			team = teams.get(ordered.get(0).get(i));
			leaderboardPrinter.println(team+","+ordered.get(1).get(i)+","+ordered.get(2).get(i)+","+ordered.get(3).get(i)+","+ordered.get(4).get(i)+","+ordered.get(5).get(i));
		}
		
		leaderboardPrinter.close();
		leaderboardWriter.close();
		
		scanLeaderboard(leagueName);
	}
	
	/** prints the leader board on the screen in an 
	* ordered way so people can read and understand it. 
	*(For an unknown reason it doesn't display the data correctly on the JOptionPane but does on the console screen)
	*/
	public static void displayLeaderBoard(ArrayList<ArrayList<String>>leaderboard) throws IOException {
		System.out.println(leagueName+" Leaderboard: \n");
		String result = String.format("%-20s %-20s %-20s %-20s %-20s %-20s \n","Team","Wins","Draws","Losses","Goal Diff","Total Points");
		for(int i = 0; i < leaderboard.get(0).size(); i++) {
			result+= String.format("%-20s %-20s %-20s %-20s %-20s %-20s \n",leaderboard.get(0).get(i),leaderboard.get(1).get(i),
			leaderboard.get(2).get(i),leaderboard.get(3).get(i),leaderboard.get(4).get(i),leaderboard.get(5).get(i));
		}
		System.out.print(result);
		JOptionPane.showMessageDialog(null,result);
		JOptionPane.showMessageDialog(null,"Thank you for using League Configurator. Administrator log-out initatied.");
		System.exit(0);
	}

	/** Scans the leagues text file and returen them in an array list
	*/
	
	public static ArrayList<ArrayList<String>> leagueScan() throws IOException {
		File leaguesFile = new File("leagues.txt");
		if(!(leaguesFile.exists())) 
			leaguesFile.createNewFile();
		
		Scanner leaguesScanner = new Scanner(leaguesFile);
		String[]splitArray;
	
		ArrayList<ArrayList<String>> leagues = new ArrayList<ArrayList<String>>();
		leagues.add(new ArrayList<String>());
		leagues.add(new ArrayList<String>());
		leagues.add(new ArrayList<String>());
		leagues.add(new ArrayList<String>());
			
		while(leaguesScanner.hasNext()) {
			splitArray = (leaguesScanner.nextLine()).split(",");
			leagues.get(0).add(splitArray[0]);
			leagues.get(1).add(splitArray[1]);
			leagues.get(2).add(splitArray[2]);
			leagues.get(3).add(splitArray[3]);
		}
		numOfLeagues = leagues.get(0).size();
		leaguesScanner.close();
		return leagues;
	}

	/** This method scans the sports the sports text files and inputs them into an arraylist.
	* it then returns the arraylist
	*/
	
	public static ArrayList<ArrayList<String>> sportScanner() throws IOException {
		File sportsRules = new File("sports.txt");
		if(!(sportsRules.exists())) 
			sportsRules.createNewFile();
		
		Scanner rulesScanner = new Scanner(sportsRules);
		ArrayList<ArrayList<String>> rules = new ArrayList<ArrayList<String>>();
		rules.add(new ArrayList<String>());
		rules.add(new ArrayList<String>());
		rules.add(new ArrayList<String>());
		rules.add(new ArrayList<String>());
		rules.add(new ArrayList<String>());
		rules.add(new ArrayList<String>());
		String[]splitArray;
		
		while(rulesScanner.hasNext()) {
			splitArray = (rulesScanner.nextLine()).split(",");
			rules.get(0).add(splitArray[0]);
			rules.get(1).add(splitArray[1]);
			rules.get(2).add(splitArray[2]);
			rules.get(3).add(splitArray[3]);
			rules.get(4).add(splitArray[4]);
			rules.get(5).add(splitArray[5]);
		}
		rulesScanner.close();
		return rules;
	}
		
	/** scans the teams text of the league in question and inputs them into an arraylist which
	*	is then return
	*/
	public static ArrayList<ArrayList<String>> scanTeams(String leagueName) throws IOException {
		File fileName = new File(leagueName+" Teams.txt");
	
		if(!(fileName.exists()))
			fileName.createNewFile();
		
		
		Scanner teamScanner = new Scanner(fileName);
			
		ArrayList<ArrayList<String>> teams = new ArrayList<ArrayList<String>>();
		teams.add(new ArrayList<String>());
		teams.add(new ArrayList<String>());
		String[]splitArray;
			
		while(teamScanner.hasNext()) {
			splitArray = (teamScanner.nextLine()).split(",");
			teams.get(0).add(splitArray[0]);
			teams.get(1).add(splitArray[1]);
		}
		numOfTeams = teams.get(0).size();
	
		return teams;
	}
	
	/** scans through the fixtures text file of a league and returns the fixtures.
	*/
	public static ArrayList<ArrayList<String>> scanFixtures(String leagueName) throws IOException {
		File fileName = new File(leagueName+" Fixtures.txt");
		int matchCount = 1;
		if(!(fileName.exists()))
			fileName.createNewFile();

		Scanner fixtureScanner = new Scanner(fileName);
		ArrayList<ArrayList<String>>fixtures = new ArrayList<ArrayList<String>>();
		fixtures.add(new ArrayList<String>());
		fixtures.add(new ArrayList<String>());
		fixtures.add(new ArrayList<String>());
	
		String[]splitArray;
		while(fixtureScanner.hasNext()) {
			splitArray = (fixtureScanner.nextLine()).split(",");
			fixtures.get(0).add(splitArray[0]);
			fixtures.get(1).add(splitArray[1]);
			fixtures.get(2).add(splitArray[2]);
		}
		return fixtures;
	}
	
	/** resultsScanner scans through the results text file and inputs it into an arraylist
	  * it the returns results.
	*/
	public static ArrayList<ArrayList<Integer>> resultsScanner (String leagueName) throws IOException 
	{
		File fileName = new File(leagueName + " Results.txt");
		
		if(!(fileName.exists()))
			fileName.createNewFile();
	
		Scanner resultsScanner = new Scanner(fileName);
			
		ArrayList<ArrayList<Integer>> results = new ArrayList<ArrayList<Integer>>();
		results.add(new ArrayList<Integer>());
		results.add(new ArrayList<Integer>());
		results.add(new ArrayList<Integer>());
		String[]splitArray;
			
		while(resultsScanner.hasNext()) {
			splitArray = (resultsScanner.nextLine()).split(",");
			results.get(0).add(Integer.parseInt(splitArray[0]));
			results.get(1).add(Integer.parseInt(splitArray[1]));
			results.get(2).add(Integer.parseInt(splitArray[2]));
		}
		return results;
	}

	/** Scans through the unordered leadboard text file and enters it into an arraylist. Then it returns this arraylist
	*/
	
	public static ArrayList<ArrayList<Integer>> scanUnorderedLeaderboard(String leagueName) throws IOException {
		File fileName = new File(leagueName+" Unordered Leaderboard.txt");
		if(!(fileName.exists()))
			fileName.createNewFile();

		Scanner calculationScanner = new Scanner(fileName);
		ArrayList<ArrayList<Integer>>unorderedLeaderboard = new ArrayList<ArrayList<Integer>>();
		unorderedLeaderboard.add(new ArrayList<Integer>());
		unorderedLeaderboard.add(new ArrayList<Integer>());
		unorderedLeaderboard.add(new ArrayList<Integer>());
		unorderedLeaderboard.add(new ArrayList<Integer>());
		unorderedLeaderboard.add(new ArrayList<Integer>());
		unorderedLeaderboard.add(new ArrayList<Integer>());

		String[]splitArray;
		while(calculationScanner.hasNext()) {
			splitArray = (calculationScanner.nextLine()).split(",");
			unorderedLeaderboard.get(0).add(Integer.parseInt(splitArray[0]));
			unorderedLeaderboard.get(1).add(Integer.parseInt(splitArray[1]));
			unorderedLeaderboard.get(2).add(Integer.parseInt(splitArray[2]));
			unorderedLeaderboard.get(3).add(Integer.parseInt(splitArray[3]));
			unorderedLeaderboard.get(4).add(Integer.parseInt(splitArray[4]));
			unorderedLeaderboard.get(5).add(Integer.parseInt(splitArray[5]));
		}
		calculationScanner.close();
		return unorderedLeaderboard;
	}

	/** This method scans the leader board text file and inputs it into an arraylist.
	  * it then calls the displayLeaderBoard method.
	*/
	
	public static void scanLeaderboard(String leagueName) throws IOException {
		File fileName = new File(leagueName+" Leaderboard.txt");
		if(!(fileName.exists()))
			fileName.createNewFile();

		Scanner leaderboardScanner = new Scanner(fileName);
		ArrayList<ArrayList<String>>leaderboard = new ArrayList<ArrayList<String>>();
		leaderboard.add(new ArrayList<String>());
		leaderboard.add(new ArrayList<String>());
		leaderboard.add(new ArrayList<String>());
		leaderboard.add(new ArrayList<String>());
		leaderboard.add(new ArrayList<String>());
		leaderboard.add(new ArrayList<String>());
			
		String[]splitArray;
		while(leaderboardScanner.hasNext()) {
			splitArray = (leaderboardScanner.nextLine()).split(",");
			leaderboard.get(0).add(splitArray[0]);
			leaderboard.get(1).add(splitArray[1]);
			leaderboard.get(2).add(splitArray[2]);
			leaderboard.get(3).add(splitArray[3]);
			leaderboard.get(4).add(splitArray[4]);
			leaderboard.get(5).add(splitArray[5]);
		}
		displayLeaderBoard(leaderboard);
	}
}