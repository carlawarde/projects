package com.example.cs4227_project.order.builder_pattern;

import com.example.cs4227_project.order.command_pattern.Stock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomerOrderBuilder implements OrderBuilder {

    private final Order order;

    public CustomerOrderBuilder(){
        this.order = new Order();
    }

    public void setProductInfo(List<Stock> products){
        order.productInfo = products;
    }

    public void setPrice(double price){
        order.price = price;
    }

    public void setDetails(CardDetails details) {
        order.details = details;
    }

    public void setAddress(Address address){
        order.address = address;
    }

    public void setEmail(String email){
        order.email = email;
    }

    public void setTime(){
        Date timeNow = new Date();
        SimpleDateFormat sfd = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault());
        order.time = sfd.format(timeNow);
    }

    public void setTime(String time) {
        order.time = time;
    }


    public Order getOrder() {
        return order;
    }
}
