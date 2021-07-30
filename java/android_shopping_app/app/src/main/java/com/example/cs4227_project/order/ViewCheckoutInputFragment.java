package com.example.cs4227_project.order;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.cs4227_project.R;
import com.example.cs4227_project.util.database_controllers.OrderDatabaseController;
import com.example.cs4227_project.util.database_controllers.StockDatabaseController;
import com.example.cs4227_project.database.StockReadListener;
import com.example.cs4227_project.interceptor_pattern.dispatchers.PreMarshallDispatcher;
import com.example.cs4227_project.interceptor_pattern.InterceptorApplication;
import com.example.cs4227_project.interceptor_pattern.InterceptorContext;
import com.example.cs4227_project.interceptor_pattern.InterceptorFramework;
import com.example.cs4227_project.interceptor_pattern.Target;
import com.example.cs4227_project.interceptor_pattern.interceptors.LoggingInterceptor;
import com.example.cs4227_project.util.ui_controllers.FragmentController;
import com.example.cs4227_project.util.LogTags;
import com.example.cs4227_project.order.builder_pattern.Address;
import com.example.cs4227_project.order.builder_pattern.CardDetails;
import com.example.cs4227_project.order.command_pattern.CommandControl;
import com.example.cs4227_project.order.builder_pattern.Order;
import com.example.cs4227_project.order.command_pattern.SellStock;
import com.example.cs4227_project.order.command_pattern.Stock;
import com.example.cs4227_project.order.builder_pattern.CustomerOrderBuilder;
import com.example.cs4227_project.order.memento_pattern.CareTaker;
import com.example.cs4227_project.order.memento_pattern.Memento;
import com.example.cs4227_project.products.abstract_factory_pattern.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ViewCheckoutInputFragment extends Fragment implements StockReadListener, Target {
    private final Cart cart = Cart.getInstance();
    private FragmentActivity myContext;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final OrderDatabaseController orderDatabaseController = new OrderDatabaseController();
    private final StockDatabaseController stockDb = new StockDatabaseController(this);
    private final Map<Product, Stock> cartMap = new HashMap<>();
    private InterceptorApplication interceptorApplication;
    private Stock originator;
    private CareTaker careTaker;
    private String orderId;

    private static final String INVALID_ENTRY = "Invalid Entry";

    public ViewCheckoutInputFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpInterceptor();
    }

    /**
     * @deprecated Needs to be refactored in the future to fit new guidelines for android studio.
     */
    @Deprecated
    @Override
    public void onAttach(Activity activity) {
        myContext= (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout_input, container, false);

        final List<EditText> texts = createTexts(view);

        Button nextButton = view.findViewById(R.id.submitButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> inputtedTexts = new ArrayList<>();
                for(EditText text : texts){
                    if(text.getText().toString().matches("")) {
                        Toast.makeText(getActivity(), "No input detected", Toast.LENGTH_SHORT).show();
                        text.requestFocus();
                        text.setError("No Entry");
                    } else {
                        inputtedTexts.add(text.getText().toString());
                    }
                }

                boolean textsValid = true;
                boolean cardNumberValid = false;
                boolean cvvValid = false;
                boolean expiryDateValid = false;

                if (inputtedTexts.size() < 7) {
                    textsValid = false;
                }

                if (textsValid) {
                    cardNumberValid = queryCardNum(inputtedTexts.get(4), texts.get(4));
                    cvvValid = queryCVV(inputtedTexts.get(6), texts.get(6));
                    expiryDateValid = queryExpiryDate(inputtedTexts.get(5), texts.get(5));
                }

                if(cardNumberValid && cvvValid  && expiryDateValid) {
                    createOrder(inputtedTexts);
                }
            }
        });

        return view;
    }

    public void createOrder(List<String> texts) {
        Toast.makeText(getActivity(), "Your order has been confirmed", Toast.LENGTH_SHORT).show();

        double totalPrice = 0.0;
        ArrayList<Stock> productInfo = new ArrayList<>();
        for(Map.Entry<Product, Stock> entry: cart.getCart().entrySet()){
            Log.d(LogTags.ORDER, "Products to checkout =" + entry.getKey().toString());
            Map<String, String> sizeQ = entry.getValue().getSizeQuantity();
            Map.Entry<String,String> sizeEntry = sizeQ.entrySet().iterator().next();
            int quantity = Integer.parseInt(sizeEntry.getValue());
            totalPrice += (entry.getKey().getPrice() * quantity);
            cartMap.put(entry.getKey(), entry.getValue());
            productInfo.add(entry.getValue());
        }

        DecimalFormat df2 = new DecimalFormat("#####.##");
        totalPrice = Double.parseDouble(df2.format(totalPrice));

        Log.d(LogTags.ORDER, "Cart List" + cartMap.toString());

        Address address = new Address(texts.get(0), texts.get(1), texts.get(2));
        CardDetails cardDetails = new CardDetails(texts.get(4), texts.get(3), texts.get(6), texts.get(5));

        CustomerOrderBuilder orderBuilder = new CustomerOrderBuilder();
        orderBuilder.setProductInfo(productInfo);
        orderBuilder.setAddress(address);
        orderBuilder.setDetails(cardDetails);
        orderBuilder.setEmail(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail());
        orderBuilder.setPrice(totalPrice);
        orderBuilder.setTime();

        FirebaseFirestore dbref = FirebaseFirestore.getInstance();
        orderId = dbref.collection("orders").document().getId();

        updateStock();
        Order order = orderBuilder.getOrder();
        interceptorApplication.sendRequest(new InterceptorContext("adding order to db"));
        orderDatabaseController.addOrderToDB(order, orderId);
        createDialog(texts, totalPrice);
    }

    /**
     * Gets a list of document ids needed from stock collection in database
     * @author Aine Reynolds
     * Goes through user cart getting product ids then calls
     * StockDatabaseController to retrieve docs from databse.
     */
    public void updateStock(){
        Log.d(LogTags.ORDER, "Get Stock");
        ArrayList<String> productIds = new ArrayList<>();
        for(Map.Entry<Product, Stock> entry: cartMap.entrySet()){
            String productId = entry.getKey().getId();
            Log.d(LogTags.COMMAND_DP, "Reading ids into list:" + productId);
            productIds.add(productId);
        }
        interceptorApplication.sendRequest(new InterceptorContext("updating stocks"));
        stockDb.getStockDocs(productIds);
    }

    /**
     * When the StockDatabaseController has finished executing the getStockDocsCommand
     * @author Aine Reynolds
     * Retrieves an arraylist of type stock using the StockDatabaseController.
     * Then calls changeStock method.
     */
    @Override
    public void stockCallback(String result){
        List<Stock> stock = stockDb.getStockArray();
        Log.d(LogTags.ORDER, "Stocks in database " + stock.toString());
        changeStock(stock);
    }

    /**
     * Implements the Command DP
     * @author Aine Reynolds
     * @param stock - the arraylist of stock from the database.
     * Goes through each item in the user cart and gets the size and quantity
     * that needs to be changed in the database. Finds that stock from the list of stock that was
     * just read in from the database and implements the SellStock Command.
     */
    public void changeStock(List<Stock> stock){
        CommandControl commandController = new CommandControl();
        originator = new Stock();
        careTaker = new CareTaker();
        /*Go through each item in the customers cart to get the stock they would like to change
        get the current stock from the database and apply the sellStock command to update database*/
        for(Map.Entry<Product, Stock> entry: cartMap.entrySet()){
            //Id of product in customers cart
            String productId = entry.getKey().getId();
            //Stock object that stores the amounts and sizes of the product the customer wants.
            Stock stockToChange = entry.getValue();

            //Break down stock object to get the hashmap containing the sizes and quantities.
            Map.Entry<String,String> sizeQ = stockToChange.getSizeQuantity().entrySet().iterator().next();
            String size = sizeQ.getKey();
            int quantity = Integer.parseInt(sizeQ.getValue());

            //Go through the stock retrieved from database that will be updated
            for(Stock s : stock){
                if(s.getId().equals(productId)){

                    //get original stock from database and the hashmap of its sizes and quantities
                    Map<String,String> sizes = s.getSizeQuantity();

                    //create a deep copy of the stock and HashMap to use with memento - deep copy so state doesn't change when sellStock called.
                    final HashMap<String,String> tempSizes = new HashMap<>();
                    for(Map.Entry<String, String> item : sizes.entrySet()){
                        tempSizes.put(item.getKey(), item.getValue());
                    }

                    SellStock sellStock= new SellStock(s, quantity, size);
                    commandController.addCommand(sellStock);

                    //Set the HashMap of original sizes and quantities and pass to memento.
                    s.setSizeQuantity(tempSizes);
                    originator.setState(s);
                    careTaker.add(originator.saveStateToMemento());
                }
            }
        }
        commandController.executeCommands();
    }

    public void createDialog(List<String> texts, double price) {
        AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
        builder.setCancelable(true);
        if (price == 0) {
            builder.setTitle("Order Confirmation");
            builder.setMessage("Your order has already been processed!\nPlease press 'OK' to return to the main menu!");
        }
        else {
            String total = String.format("%.2f", price);
            builder.setTitle("Order Confirmation");
            builder.setMessage("Thank you " + texts.get(3) + "! \nYour order has been confirmed! \nYour card has been charged €" + total);
        }
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        popBackToHome();
                    }
                });

        builder.setNeutralButton("Undo Order",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<Memento> mementoList = careTaker.getMementoList();
                        for(int i = 0; i < mementoList.size(); i++){
                            //Getting stock state from memento list
                            originator.getStateFromMemento(careTaker.get(i));
                            Stock s = originator.getState();

                            //Update database with old stock state
                            Log.d("Memento", "Restored state " + s.getSizeQuantity());
                            stockDb.updateStock(s.getId(), "sizeQuantity", s.getSizeQuantity());
                            //Delete order that was created in database
                            orderDatabaseController.deleteOrderFromDB(orderId);
                        }
                        popBackToHome();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public List<EditText> createTexts(View view){
        List<EditText> texts = new ArrayList<>();
        texts.add((EditText) view.findViewById(R.id.townInput));
        texts.add((EditText) view.findViewById(R.id.cityInput));
        texts.add((EditText) view.findViewById(R.id.countyInput));
        texts.add((EditText) view.findViewById(R.id.cardNameInput));
        texts.add((EditText) view.findViewById(R.id.cardNumInput));
        texts.add((EditText) view.findViewById(R.id.expiryDateInput));
        texts.add((EditText) view.findViewById(R.id.cvvInput));
        return texts;
    }

    public boolean queryCardNum(String x, EditText text){
        boolean valid = true;
        if(x.length() != 16) {
            Toast.makeText(getActivity(), "Card Number must be 16 digits in length", Toast.LENGTH_SHORT).show();
            text.requestFocus();
            text.setError(INVALID_ENTRY);
            valid = false;
        }
        return valid;
    }

    public boolean queryCVV(String x, EditText text){
        boolean valid = true;
        if(x.length() != 3) {
            Toast.makeText(getActivity(), "CVV must be 3 digits in length", Toast.LENGTH_SHORT).show();
            text.requestFocus();
            text.setError(INVALID_ENTRY);
            valid = false;
        }
        return valid;
    }

    public boolean queryExpiryDate(String x, EditText text){
        boolean valid = true;
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("MM/yyyy");
            format.setLenient(false);
            format.parse(x);
        } catch (ParseException e) {
            Log.d(LogTags.PARSE_ERROR, e.getMessage());
            Toast.makeText(getActivity(), "The expiry date format is wrong", Toast.LENGTH_SHORT).show();
            text.requestFocus();
            text.setError(INVALID_ENTRY);
            valid = false;
        }
        return valid;
    }

    public void popBackToHome() {
        FragmentController fragmentController = FragmentController.getInstance();
        fragmentController.getCurrentFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void setUpInterceptor() {
        //Set up interceptor framework with LogInContext
        InterceptorFramework interceptorFramework = new InterceptorFramework(new PreMarshallDispatcher(this));
        interceptorFramework.addInterceptor(new LoggingInterceptor());

        interceptorApplication = InterceptorApplication.getInstance();
        interceptorApplication.setInterceptorFramework(interceptorFramework);
    }

    @Override
    public void execute(InterceptorContext context) {
        Log.d(LogTags.INTERCEPTOR, "executing target");
        switch (context.getMessage()) {
            default:
                Log.d(LogTags.INTERCEPTOR, "no request found under \""+context.getMessage()+"\"");
        }
    }
}
