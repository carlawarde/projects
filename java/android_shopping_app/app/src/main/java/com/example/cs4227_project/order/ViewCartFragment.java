package com.example.cs4227_project.order;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs4227_project.R;
import com.example.cs4227_project.interceptor_pattern.dispatchers.PostMarshallDispatcher;
import com.example.cs4227_project.interceptor_pattern.InterceptorApplication;
import com.example.cs4227_project.interceptor_pattern.InterceptorContext;
import com.example.cs4227_project.interceptor_pattern.InterceptorFramework;
import com.example.cs4227_project.interceptor_pattern.Target;
import com.example.cs4227_project.interceptor_pattern.interceptors.LogInAuthenticationInterceptor;
import com.example.cs4227_project.interceptor_pattern.interceptors.LoggingInterceptor;
import com.example.cs4227_project.util.ui_controllers.FragmentController;
import com.example.cs4227_project.util.LogTags;
import com.example.cs4227_project.order.command_pattern.Stock;
import com.example.cs4227_project.products.abstract_factory_pattern.Product;
import com.example.cs4227_project.products.ProductInterfaceAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewCartFragment extends Fragment implements Target {
    private final Cart cart = Cart.getInstance();
    private RecyclerView recyclerView;
    private ProductInterfaceAdapter adapter;
    private InterceptorApplication interceptorApplication;

    public ViewCartFragment() {
        // Required empty public constructor
    }

    public static ViewCartFragment newInstance() {
        return new ViewCartFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayList<Product> products = (ArrayList<Product>)getArguments().getSerializable("Products");
        final View view = inflater.inflate(R.layout.fragment_cart, container, false);
        setUpInterceptor();
        Button emptyCartBtn = view.findViewById(R.id.clearCart);
        emptyCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cart.removeAllProductsFromCart();
                refreshCart();
                Log.d(LogTags.CHECK_CARD, "Products have been removed from the cart");
            }
        });

        Button checkoutBtn = view.findViewById(R.id.checkout);
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LogTags.CHECK_CARD, "Preparing to check out cart");
                Map<Product, Stock> products = cart.getCart();
                if (products.isEmpty()){
                    Toast.makeText(getActivity(), "There are no items in your cart", Toast.LENGTH_LONG).show();
                    Log.d(LogTags.CHECK_CARD, "Failed to check out. No items currently in cart");
                }
                else {
                    InterceptorContext context = new InterceptorContext("You must be logged-in to purchase products!");
                    interceptorApplication.sendRequest(context);
                }
            }
        });
        adapter = new ProductInterfaceAdapter(products);
        Log.d(LogTags.CHECK_CARD, "" + adapter);
        // Add the following lines to create RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);
        return view;
    }

    public void setUpInterceptor() {
        //Set up interceptor framework with LogInContext
        InterceptorFramework interceptorFramework = new InterceptorFramework(new PostMarshallDispatcher(this));
        interceptorFramework.addInterceptor(new LogInAuthenticationInterceptor());
        interceptorFramework.addInterceptor(new LoggingInterceptor());

        interceptorApplication = InterceptorApplication.getInstance();
        interceptorApplication.setInterceptorFramework(interceptorFramework);
    }

    public void refreshCart(){
        //updates the recycler view with the empty cart
        Cart newCart = Cart.getInstance();
        List<Product> products = new ArrayList<>();
        products = newCart.productArrayList(products);
        adapter = new ProductInterfaceAdapter(products);
        recyclerView.setAdapter(adapter);
        if(products.isEmpty()) {
            Toast toast = Toast.makeText(getActivity(), "The cart has been emptied", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void goToCheckout() {
        ViewCheckoutInputFragment fragment = new ViewCheckoutInputFragment();
        FragmentController fragmentController = FragmentController.getInstance();
        fragmentController.startFragment(fragment, R.id.content, "viewCheckout");
    }

    @Override
    public void execute(InterceptorContext context) {
        Log.d(LogTags.INTERCEPTOR, "executing target");
        switch (context.getMessage()) {
            case "You must be logged-in to purchase products!":
                goToCheckout();
                break;
            default:
                Log.d(LogTags.INTERCEPTOR, "no request found under \""+context.getMessage()+"\"");
        }
    }
}
