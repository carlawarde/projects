import javafx.application.Application; 
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.geometry.*;
import java.util.ArrayList;
import java.io.*;
import javafx.stage.Modality;
import javafx.collections.ObservableList;

public class VendingMachineGUI extends Application {
	private VendingMachine machine;
	static boolean found;
	private static Coin[] coins;
	
	/**
		The main menu of the GUI program where you can decide whether to use the Vending Machine as a User or log-in as an Operator. After deciding which option they will call another method in this class. 
		@param Stage primaryStage
		@throws Exception e
	*/
	@Override
	public void start(Stage primaryStage) throws Exception {
		machine = new VendingMachine();
		coins = ((ReadWrite.scanCoins()).toArray(new Coin[0]));
		//Stage setup
		primaryStage.setTitle("Vending Machine Menu");
		primaryStage.setResizable(false);
		
		VBox root = new VBox(20);
		root.setAlignment(Pos.CENTER);
		Label choose = new Label("Please choose operation mode:");
		
		HBox buttons = new HBox(8);
		buttons.setAlignment(Pos.CENTER);
		
		Button user = 		new Button("  User Mode  ");
		Button operator = 	new Button("Operator Mode");
		Button quit = 		new Button("     Quit     ");
		
		//Button operations
		user.setOnAction(e -> {
			userMenu(primaryStage);
		});
		operator.setOnAction(e -> {
			try {
				if(operatorValidation())
					operatorMenu(primaryStage);
			} 
			catch (IOException i) {
				
			}
		});
		quit.setOnAction(e -> primaryStage.close());
		
		buttons.getChildren().addAll(user, operator, quit);
		root.getChildren().addAll(choose, buttons);
		Scene scene = new Scene(root,600,300);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
		Creates a new scene for the primaryStage. The User Menu of the vending machine. The user can show products, insert coins, buy products or remove coins. After deciding which option they will call another method in this class.
		@param Stage primaryStage
	*/
	public void userMenu(Stage primaryStage) {
		primaryStage.setTitle("Vending Machine Menu");
		primaryStage.setResizable(false);
		StackPane root = new StackPane();
		
		HBox pane = new HBox(10);
		Label label = new Label("Which option would you like to use?");
		pane.setAlignment(Pos.CENTER);
		label.setTranslateY(-40);
		pane.getChildren().addAll(label);
		
		HBox pane1 = new HBox(10);
		pane1.setAlignment(Pos.CENTER);
		Button bt1 = new Button("Show products");
		Button bt2 = new Button(" Insert coin ");
		Button bt3 = new Button("  Buy Item   ");
		Button bt4 = new Button("Remove coins ");
		Button bt5 = new Button("    Quit     ");
		pane1.getChildren().addAll(bt1, bt2, bt3, bt4, bt5);
		
		bt1.setOnAction(e -> {
			showProduct();
		});
		
		bt2.setOnAction(e -> {
			insertCoin();
		});
		
		bt3.setOnAction(e -> {
			buyItem();
		});
		
		bt4.setOnAction(e -> {
			removeCoins();
		});
		
		bt5.setOnAction(e -> {
			primaryStage.close();
		});
		
		root.getChildren().addAll(pane, pane1);
		Scene scene = new Scene(root,600,300);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
		Creates a new scene for the primaryStage. The Operator Menu of the vending machine. The operator can add products or remove money. After deciding which option they will call another method in this class.
		@param Stage primaryStage
	*/
	public void operatorMenu(Stage primaryStage) {
		primaryStage.setTitle("Vending Machine Operator Menu");
		primaryStage.setResizable(false);
		
		VBox root = new VBox(20);
		root.setAlignment(Pos.CENTER);
		Label choose = new Label("Select an option:");
		
		HBox buttons = new HBox(8);
		buttons.setAlignment(Pos.CENTER);
		
		Button bt1 = 	new Button(" Add Product ");
		Button bt2 = 	new Button(" Remove Money");
		Button bt3 = 	new Button("    Quit     ");
		
		//Button operations
		bt1.setOnAction(e -> {
			addProduct();
		});
		bt2.setOnAction(e -> {
			removeMoneyOp();
		});
		bt3.setOnAction(e -> primaryStage.close());
		
		buttons.getChildren().addAll(bt1,bt2,bt3);
		root.getChildren().addAll(choose, buttons);
		Scene scene = new Scene(root, 600, 300);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	
	/**
		Opens a pop-up in which the operator can enter a PIN and it is then validated.
		@return boolean : returns found if the operator is successfully validated
		@throws IOException e
	*/
	public boolean operatorValidation() throws IOException {
		String pattern = "[0-9]{4}";
		//Read in operator PINs from text file
		ArrayList<Integer> passcodes = ReadWrite.scanIntegers("operators.txt");
		
		//Stage set-up
		Stage passwordStage = new Stage();
		passwordStage.setResizable(false);
		passwordStage.initModality(Modality.APPLICATION_MODAL);
		passwordStage.setTitle("Operator Log-in");
		VBox root = new VBox(10);
		root.setAlignment(Pos.CENTER);
		
		Label label = new Label("Enter your four-digit operator pin");
		Label message = new Label("");
		
		HBox passwordContainer = new HBox(2);
		passwordContainer.setAlignment(Pos.CENTER);
		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Enter PIN");
		Button button = new Button("Submit");
		passwordContainer.getChildren().addAll(passwordField, button);
		
		button.setOnAction(e -> {
			if(!(passwordField.getText().matches(pattern))) {
				message.setText("Invalid input: Please enter your four digit operator PIN.");
				message.setTextFill(Color.rgb(210, 39, 30));
				passwordField.clear();
			}
			else {
				int opInput = Integer.parseInt(passwordField.getText());
				if(passcodes.indexOf(opInput) != -1) {
					found = true;
					passwordStage.close();
				}	
				else {
					found = false;
					message.setText("Incorrect PIN entered!");
					message.setTextFill(Color.rgb(210, 39, 30));
				}
				passwordField.clear();
			}
		});
		
		root.getChildren().addAll(label,passwordContainer,message);
		Scene scene = new Scene(root, 300, 150);
		passwordStage.setScene(scene);
		passwordStage.showAndWait();
		
		return found;
	}
	
	/**
		Opens a pop-up which lets the operator create a new product by entering values into text fields. Then using a method from the VendingMachine class it adds the product to the vending machine.
		returns boolean : returns found if the product is created successfully
	*/
	public boolean addProduct() {
		String pattern1 = "[0-9]{1,}", pattern2 = "[0-9]{1,}.[0-9]{2}", pattern3 = "[a-zA-Z0-9 ]+";
		
		//Stage set up
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("Add Product");
		stage.setResizable(false);
		
		//grid set up
		GridPane grid = new GridPane();
		grid.setHgap(15);
		grid.setVgap(10);
		grid.setAlignment(Pos.CENTER);
		grid.setPadding(new Insets(10, 10, 0, 10));
		
		//0,0 contents - Name label
		Label labelOne = new Label("   Product Name:    ");
		grid.add(labelOne, 0, 0); 
		
		//1,0 contents - name text field
		TextField fieldOne = new TextField();
		fieldOne.setPromptText("E.g.: Chocolate Bar 45g");
		grid.add(fieldOne, 1,0);
		
		//0,1 contents - price label
		Label labelTwo = new Label("   Product Price:   ");
		grid.add(labelTwo, 0, 1); 
		
		//1,1 contents - price text field
		TextField fieldTwo = new TextField();
		fieldTwo.setPromptText("E.g.: 1.50");
		grid.add(fieldTwo, 1,1);
		
		//0,2 contents - quantity text label
		Label labelThree = new Label("Product Quantity: ");
		grid.add(labelThree,0,2);
		
		//1,2 contents - quantity text field
		TextField fieldThree = new TextField();
		fieldThree.setPromptText("E.g.: 30");
		grid.add(fieldThree,1,2);
		
		//0,3 contents - error message
		Label message = new Label("");
		message.setFont(new Font(9));
		grid.add(message,0,3);
		
		//1,3 contents - submit button
		Button button = new Button("Submit");
		grid.add(button,1,3);
		grid.setHalignment(button,HPos.RIGHT);
		
		button.setOnAction(e -> {
			if(fieldOne.getText().matches(pattern3) && fieldTwo.getText().matches(pattern2) && fieldThree.getText().matches(pattern1)) {
				String description = fieldOne.getText();
				double price = Double.parseDouble(fieldTwo.getText());
				int quantity = Integer.parseInt(fieldThree.getText());
				
				try {
					machine.addProduct(new Product(description, price), quantity);
				}
				catch(IOException i) {

				}
				finally {
				stage.close();
				}
				
				found = true;
			}		
			else {
				message.setText("Incorrect input entered!");
				message.setTextFill(Color.rgb(210, 39, 30));
				
				found = false;
			}
			fieldOne.clear();
			fieldTwo.clear();
			fieldThree.clear();
		});
		
		Scene scene = new Scene(grid, 500, 250);
		stage.setScene(scene);
		stage.showAndWait();
		
		return found;
	}
	
	/**
		Creates a new pop-up which lets the operator select a coin available from the vending machine. Then it removes all of the specified coin from the vending machine using the removeMoneyOp method from the VendingMachine class
	*/
	public void removeMoneyOp() {
		Stage stage = new Stage();
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("Remove Money");
		
		VBox vbox = new VBox(10);
		vbox.setAlignment(Pos.CENTER);
		Button btn = new Button("OK");
		btn.setTranslateY(-5);
		Label label = new Label("Pick which type of coin you would like to remove: ");
		Label labelChoice = new Label("");
		ListView<Coin> listChoices = new ListView<>();
		for(int i = 0; i < coins.length; i++){
			listChoices.getItems().add(coins[i]);
		}
		listChoices.setMinHeight(150);
		listChoices.setMinWidth(250);
		listChoices.setMaxHeight(250);
		listChoices.setMaxWidth(350);
		
		btn.setOnAction(e -> {
			ObservableList<Coin> choice;
			choice = listChoices.getSelectionModel().getSelectedItems();
			int quantity = 0;
			Coin b = new Coin(0.0,"");
			try {
				for(Coin c : choice){
					
					quantity = machine.removeMoneyOp(c);
					b = c;
				}
				label.setText("You have removed "+(quantity*(b.getValue()))+" euro from the machine.");
			}
			catch(IOException i) {
				
			}
			catch(VendingException v) {
				label.setText(v.getMessage());
			}
			VBox vbox2 = new VBox(10);
			vbox2.setAlignment(Pos.CENTER);
			Button btn2 = new Button("Ok");
			btn2.setOnAction(f -> stage.close());
			vbox2.getChildren().addAll(label,btn2);
			Scene scene2 = new Scene(vbox2,500,250);
			stage.setScene(scene2);
		});
		
		vbox.getChildren().addAll(label, listChoices, btn);
		Scene scene = new Scene(vbox,500,250);
		stage.setScene(scene);
		stage.showAndWait();
	}
	
	/**
		Creates a new pop-up that shows a scrolable list of all the current proucts in the vending machine
	*/
	public void showProduct(){
		Stage window = new Stage();
		window.setResizable(false);
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Show Products");
		StackPane rootSP = new StackPane();
		
		VBox paneSP = new VBox(10);
		paneSP.setAlignment(Pos.CENTER);
		Button spBt1 = new Button("OK");
		
		ListView<Product> listProducts = new ListView<>();
		Product [] prod = machine.getProductTypes();
		for(int i = 0; i < prod.length; i++){
			listProducts.getItems().add(prod[i]);
		}
		listProducts.setMinHeight(150);
		listProducts.setMinWidth(250);
		listProducts.setMaxHeight(250);
		listProducts.setMaxWidth(350);
		
		spBt1.setOnAction(x -> window.close());
		
		paneSP.getChildren().addAll(listProducts, spBt1);
		Scene showProducts = new Scene(paneSP,500,250);
		window.setScene(showProducts);
		window.showAndWait();
	}
	
	
	/**
		Creates a new pop-up which lets the user choose a coin from a list which will then be added to the vending machine for the user by using the addCoin method from the VendingMachine class
	*/
	public void insertCoin(){
		Stage window = new Stage();
		window.setResizable(false);
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Insert Coins");
		
		VBox paneIC = new VBox(10);
		paneIC.setAlignment(Pos.CENTER);
		Button icBt1 = new Button("OK");
		
		ChoiceBox<Coin> choiceBox = new ChoiceBox<>();
		for(int i = 0; i < coins.length; i++){
			choiceBox.getItems().add(coins[i]);
		}
		choiceBox.setValue(coins[0]);
		paneIC.getChildren().addAll(choiceBox, icBt1);
		
		icBt1.setOnAction(x -> {
			Coin coinChoice = choiceBox.getValue();
			machine.addCoin((Coin) coinChoice);
			window.close();
		});
		
		Scene insertCoins = new Scene(paneIC,500,250);
		window.setScene(insertCoins);
		window.showAndWait();
	}
	
	/**
		Creates a new pop-up that shows a scrolable list of all the current proucts in the vending machine and lets the user select only one. The selected product is then bought using the buyPrduct method form the VendingMachine class.
	*/
	public void buyItem(){
		Stage window = new Stage();
		window.setResizable(false);
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Buy Item");
		
		VBox paneB = new VBox(10);
		paneB.setAlignment(Pos.CENTER);
		Button bBt1 = new Button("OK");
		
		ListView<Product> listChoices = new ListView<>();
		Product [] prod = machine.getProductTypes();
		for(int i = 0; i < prod.length; i++){
			listChoices.getItems().add(prod[i]);
		}
		listChoices.setMinHeight(150);
		listChoices.setMinWidth(250);
		listChoices.setMaxHeight(250);
		listChoices.setMaxWidth(350);
		
		bBt1.setOnAction(x -> {
			Label labelChoice = new Label();
			labelChoice.setText("No product selected");
			labelChoice.setTextFill(Color.rgb(210, 39, 30));
			ObservableList<Product> choice;
			choice = listChoices.getSelectionModel().getSelectedItems();
			try {
				for(Product p: choice){
					machine.buyProduct((Product) p);
					labelChoice.setText("Purchased: " + p);
				
				}
			}
			catch(IOException i) {
				
			}
			catch(VendingException v) {
				labelChoice.setText(v.getMessage());
			}

			
			VBox paneB2 = new VBox(10);
			paneB2.setAlignment(Pos.CENTER);
			Button bBt2 = new Button("OK");
			
			paneB2.getChildren().addAll(labelChoice, bBt2);
			Scene bought = new Scene(paneB2, 500, 250);
			window.setScene(bought);
			
			bBt2.setOnAction(v -> {
				window.close();
			});
		});
		
		
		paneB.getChildren().addAll(listChoices, bBt1);
		Scene buy = new Scene(paneB,500,250);
		window.setScene(buy);
		window.showAndWait();
	}
	
	/**
		Calls the removeMoney method in the VendingMachine class and then creates a new pop-up that shows the user the value of coins that was removed from the machine.
	*/
	public void removeCoins(){
		Stage window = new Stage();
		window.setResizable(false);
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Remove Coins");
		StackPane rootRC = new StackPane();
		
		HBox paneLabelRC = new HBox(10);
		Label labelRC = new Label();
		labelRC.setText("Removed: " + machine.removeMoney());
		paneLabelRC.setAlignment(Pos.CENTER);
		labelRC.setTranslateY(-40);
		paneLabelRC.getChildren().addAll(labelRC);
		
		HBox paneRC = new HBox(10);
		paneRC.setAlignment(Pos.CENTER);
		Button rcBt1 = new Button("OK");
		paneRC.getChildren().addAll(rcBt1);
		
		rcBt1.setOnAction(x -> window.close());
		
		rootRC.getChildren().addAll(labelRC, paneRC);
		Scene removeCoins = new Scene(rootRC,500,250);
		window.setScene(removeCoins);
		window.showAndWait();
	}
}