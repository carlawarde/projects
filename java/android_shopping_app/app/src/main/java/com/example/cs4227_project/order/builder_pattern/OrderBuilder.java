package com.example.cs4227_project.order.builder_pattern;

import com.example.cs4227_project.order.command_pattern.Stock;

import java.util.List;

public interface OrderBuilder {

    public void setProductInfo(List<Stock> productInfo);

    public void setPrice(double price);

    public void setDetails(CardDetails details);

    public void setAddress(Address address);

    public void setEmail(String email);

    public void setTime();
}
