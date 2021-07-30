package com.example.cs4227_project.products;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs4227_project.util.ui_controllers.ProductTypeController;
import com.example.cs4227_project.util.database_controllers.StockDatabaseController;
import com.example.cs4227_project.database.StockReadListener;
import com.example.cs4227_project.util.LogTags;
import com.example.cs4227_project.R;
import com.example.cs4227_project.order.command_pattern.Stock;
import com.example.cs4227_project.order.Cart;
import com.example.cs4227_project.order.command_pattern.UpdateStockActivity;
import com.example.cs4227_project.util.ui_controllers.UserController;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.example.cs4227_project.products.abstract_factory_pattern.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductInterfaceAdapter extends RecyclerView.Adapter implements AdapterView.OnItemSelectedListener, StockReadListener {
    private final Cart cart = Cart.getInstance();
    private List<Product> productList;
    private TextView textViewProductName;
    private TextView textViewProductPrice;
    private ImageView imageViewProductPic;
    private CardView cardViewProduct;
    private Dialog productDialog;
    private Button addBtn;
    private Button addStock;
    private String chosenSize;
    private Spinner sizeSpinner;
    private Product product;
    private Map<String, String> productSizeQuantities;

    private final StockDatabaseController stockDb;
    private  RecyclerView mRecyclerView;

    public ProductInterfaceAdapter(List<Product> products) {
        Log.d(LogTags.CHECK_CARD, products.toString());
        setProductList(products);
        stockDb = new StockDatabaseController(this);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView;
        productDialog = new Dialog(parent.getContext());

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product, parent, false);
        return new ProductHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos){
        ((ProductHolder)holder).bindView(pos);
    }

    class ProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ProductHolder(View itemView) {
            super(itemView);
            textViewProductName = itemView.findViewById(R.id.productName);
            textViewProductPrice = itemView.findViewById(R.id.productPrice);
            imageViewProductPic = itemView.findViewById(R.id.productViewImage);
            cardViewProduct = itemView.findViewById(R.id.productCard);
        }

        @SuppressLint("SetTextI18n")
        void bindView(int pos){
            final Product item = productList.get(pos);

            Log.d(LogTags.PRODUCT_INTERFACE_ADAPTER, "product: " + item.getId());

            textViewProductName.setText(item.getName());
            textViewProductPrice.setText("€" + item.getPrice());
            setPicture(imageViewProductPic, item);

            productDialog.setContentView(R.layout.product_detail_page);
            productDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            sizeSpinner = productDialog.findViewById(R.id.spinner);
            addBtn = productDialog.findViewById(R.id.addToCart);
            addStock = productDialog.findViewById(R.id.aS);

            textViewProductName = productDialog.findViewById(R.id.productName);
            textViewProductPrice = productDialog.findViewById(R.id.productPrice);

            addStock.setOnClickListener(this);

            cardViewProduct.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View productView) {
                    stockDb.getStockDoc(item.getId());
                    product = item;

                    Log.d(LogTags.PRODUCT_INTERFACE_ADAPTER, "getStockDoc: " + item.getId());

                    if(UserController.getUser() != null) {
                        if(UserController.getUser().isAdmin()){
                            addStock.setVisibility(View.VISIBLE);
                        }else{
                            addStock.setVisibility(View.INVISIBLE);
                        }
                        Log.d("CLICK", "Listener set");

                    }else{
                        addStock.setVisibility(View.INVISIBLE);
                    }

                    ImageView imageViewProductImage = productDialog.findViewById(R.id.productImage);
                    final EditText editQuantity = productDialog.findViewById(R.id.quantity);

                    inCart(item);

                    sizeSpinner.setOnItemSelectedListener(sizeSpinner.getOnItemSelectedListener());
                    textViewProductName.setText(item.getName());
                    textViewProductPrice.setText("€" + item.getPrice());
                    setPicture(imageViewProductImage, item);

                    addBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View productView) {
                            chosenSize = sizeSpinner.getSelectedItem().toString();
                            if(!(cart.inCart(item))) {
                                String quantity = editQuantity.getText().toString();
                                HashMap<String, String> sizeQ = new HashMap<>();
                                sizeQ.put(chosenSize, quantity);

                                // Add product type to stock using productTypeController.
                                Stock stock = new Stock(item.getId(), sizeQ, ProductTypeController.getType().getValue(), ProductTypeController.isFemale());
                                cart.addProductToCart(item, stock);
                                Log.d(LogTags.CHECK_CARD, "Added Product to cart");
                                Toast.makeText(productDialog.getContext(), "Selected size is " + chosenSize, Toast.LENGTH_SHORT).show();
                                Toast.makeText(productDialog.getContext(), "Added the item to cart", Toast.LENGTH_SHORT).show();
                                refreshInstance(item);
                            }
                            else {
                                cart.removeProductFromCart(item);
                                Log.d(LogTags.CHECK_CARD, "Added Product to cart");
                                Toast.makeText(productDialog.getContext(), "Selected size is " + chosenSize, Toast.LENGTH_SHORT).show();
                                Toast.makeText(productDialog.getContext(), "Removed the item from cart", Toast.LENGTH_SHORT).show();
                                refreshInstance(item);
                            }
                        }
                    });

                    productDialog.show();
                }
            });
        }

        void setPicture(final ImageView image, Product p){
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            String path = p.getImageURL();
            storageReference.child(path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Log.d(LogTags.PRODUCT_INTERFACE_ADAPTER, "SUCCESS" + uri);
                    Picasso.get().load(uri).fit().centerCrop().into(image);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }

        @Override
        public void onClick(View v) {
            int i = v.getId();
            Log.d(LogTags.PRODUCT_INTERFACE_ADAPTER, "In on click");
            if(i == R.id.aS){
                Log.d(LogTags.PRODUCT_INTERFACE_ADAPTER, "Add stock button clicked");
                Intent intent = new Intent(v.getContext(), UpdateStockActivity.class);
                intent.putExtra("product", product);
                v.getContext().startActivity(intent);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (productList == null) {
            return 0;
        } else {
            return productList.size();
        }
    }

    public void setProductList(List<? extends Product> productList) {
        if (this.productList == null){
            this.productList = new ArrayList<>();
        }
        this.productList.clear();
        this.productList.addAll(productList);
        notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();
        Log.d(LogTags.SELECTED_SIZE, "Selected:" +item);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //part of item selected interface
    }

    private void inCart(Product p) {
        if(cart.inCart(p)) {
            String remove = "Remove from Cart";
            addBtn.setText(remove);
        } else {
            String add = "Add to Cart";
            addBtn.setText(add);
        }
    }

    public void refreshInstance(Product item){
        List<Product> products = new ArrayList<>(productList);
        ProductInterfaceAdapter adapter = new ProductInterfaceAdapter(products);
        mRecyclerView.setAdapter(adapter);
        stockDb.getStockDoc(product.getId());
        inCart(item);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        mRecyclerView = recyclerView;
    }

    @Override
    public void stockCallback(String result){
        productSizeQuantities = new HashMap<>();
        Stock s = stockDb.getStockItem();
        productSizeQuantities = s.getSizeQuantity();
        setUpSpinner();
    }

    public void setUpSpinner(){
        List<String> sizes = new ArrayList<>();

        //Add each size from the stock of the product into the arraylist and set as adapter
        for(Map.Entry<String, String> entry : productSizeQuantities.entrySet()){
            String size = entry.getKey();
            sizes.add(size);
        }

        ArrayAdapter<String> aa = new ArrayAdapter<>(productDialog.getContext(), android.R.layout.simple_spinner_item, sizes);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(aa);
    }
}
