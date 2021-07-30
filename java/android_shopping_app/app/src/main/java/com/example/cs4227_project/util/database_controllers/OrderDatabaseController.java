package com.example.cs4227_project.util.database_controllers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cs4227_project.database.Database;
import com.example.cs4227_project.database.OrderReadListener;
import com.example.cs4227_project.order.builder_pattern.Address;
import com.example.cs4227_project.order.builder_pattern.CardDetails;
import com.example.cs4227_project.order.command_pattern.Stock;
import com.example.cs4227_project.order.builder_pattern.CustomerOrderBuilder;
import com.example.cs4227_project.order.Cart;
import com.example.cs4227_project.util.LogTags;
import com.example.cs4227_project.order.builder_pattern.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDatabaseController {
    private final Database db = Database.getInstance();
    private final Cart cart = Cart.getInstance();
    private final ArrayList<Order> orders = new ArrayList<>();
    private OrderReadListener myEventL;
    private final ArrayList<String> descStrings = new ArrayList<>();

    private static final String ORDER = "orders";

    public OrderDatabaseController() {}

    public OrderDatabaseController(OrderReadListener ml){
        this.myEventL = ml;
    }


    public void addOrderToDB(Order order) {
        db.post(ORDER, order);
        cart.removeAllProductsFromCart();
    }

    public void addOrderToDB(Order order, String id) {
        db.put(ORDER, id, order);
        cart.removeAllProductsFromCart();
    }

    public void deleteOrderFromDB(String id){
        db.delete(ORDER, id);
    }

    public void getOrderCollection() {
        orders.clear();
        //get reference to collection from database
        CollectionReference colRef = db.get(ORDER);
        colRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(LogTags.DB_GET, document.getId() + " => " + document.getData());
                                //convert document to Product and add to List of data
                                readOrderIntoList(document);
                            }
                            myEventL.orderCallback("success");
                            Log.d(LogTags.DB_GET, "Number of products: " + orders.size());
                        } else {
                            Log.d(LogTags.DB_GET, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public Order getOrder(Map<String, Object> order) {
        Log.d(LogTags.ORDER, order.get("productInfo").getClass().getName());
        HashMap<String, String> cardDetails = (HashMap<String, String>) order.get("details");
        HashMap<String, String> customerAddress = (HashMap<String, String>) order.get("address");
        ArrayList<HashMap<String, Object>> stock = (ArrayList<HashMap<String, Object>>) order.get("productInfo");

        //Converts hashmap from database to stock
        ArrayList<Stock> orderStock = new ArrayList<>();

        for(int i = 0; i < stock.size(); i++) {
            HashMap<String, Object> map = stock.get(i);
            Stock s = new Stock((String) map.get("id"), (HashMap<String, String>)map.get("sizeQuantity"), (String) map.get("type"), (boolean) map.get("female"));
            orderStock.add(s);
        }

        CardDetails details = new CardDetails(cardDetails.get("cardNum"), cardDetails.get("cardName"), cardDetails.get("cvv"), cardDetails.get("expiryDate"));
        Address address = new Address(customerAddress.get("line1"), customerAddress.get("city"), customerAddress.get("county"));

        CustomerOrderBuilder builder = new CustomerOrderBuilder();
        builder.setProductInfo(orderStock);
        builder.setAddress(address);
        builder.setDetails(details);
        builder.setEmail((String)order.get("email"));
        builder.setPrice((double)order.get("price"));
        builder.setTime((String)order.get("time"));

        return builder.getOrder();
    }

    public void readOrderIntoList(QueryDocumentSnapshot document) {
        //Generate product from product factory
        Order o = getOrder(document.getData());
        orders.add(o);
    }

    public List<Order> getAllOrders() {
        return orders;
    }

    public void getProduct(List<Stock> arr) {
        for(Stock s: arr) {
            String collection = s.getType() + s.isFemale();
            String id = s.getId();

            DocumentReference docRef = db.get(collection, id);
            docRef.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()) {
                                extractInfo(documentSnapshot);
                                Log.d("test", descStrings.toString());

                            } else {
                                Log.d("error", "Document does not exist");
                            }
                            myEventL.orderCallback("success");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("error", "Couldn't get document");
                        }
                    });
        }
    }

    public void extractInfo(DocumentSnapshot doc) {
        String description = doc.get("colour") + " " + doc.get("brand") + " " + doc.get("name") + "\n";
        descStrings.add(description);
        Log.d("desc", descStrings.toString());
    }

    public List<String> getDescStrings() { return descStrings;}

}
