package com.example.cs4227_project.order.builder_pattern;

import com.example.cs4227_project.order.command_pattern.Stock;

import java.util.ArrayList;
import java.util.List;

public class Order{

    public List<Stock> productInfo;
    public Address address;
    public CardDetails details;
    public String email;
    public double price;
    public String time;

    public Order(){
        this.productInfo = new ArrayList<>();
        this.address = new Address();
        this.details = new CardDetails();
        this.email = "";
        this.price = 0.0;
        this.time ="";
    }

    public List<Stock> getProductInfo() {
        return productInfo;
    }

    public Address getAddress() {
        return address;
    }

    public CardDetails getDetails() {
        return details;
    }

    public String getEmail() {
        return email;
    }

    public double getPrice() {
        return price;
    }

    public String getTime() {
        return time;
    }
}
