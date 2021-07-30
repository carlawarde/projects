package com.example.cs4227_project.order.builder_pattern;

public class CardDetails {
    private String cardNum;
    private String cardName;
    private String cvv;
    private String expiryDate;

    public CardDetails(){ }

    public CardDetails(String cardNum, String cardName,String cvv, String date){
        this.cardNum = cardNum;
        this.cardName = cardName;
        this.cvv = cvv;
        this.expiryDate = date;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}
