package com.example.cs4227_project.order.builder_pattern;

public class Address {
    private String line1;
    private String city;
    private String county;

    public Address(){

    }

    public Address(String line, String city, String county){
        this.line1 = line;
        this.city = city;
        this.county = county;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String toString() { return line1 + ", \n" + city + ", \n" + county; }
}
