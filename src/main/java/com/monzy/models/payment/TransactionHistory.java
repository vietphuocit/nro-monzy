package com.monzy.models.payment;

import com.google.gson.Gson;

public class TransactionHistory {

    private String transactionID;
    private int amount;
    private String description;
    private String type;

    public TransactionHistory(String transactionID, int amount, String description, String type) {
        this.transactionID = transactionID;
        this.amount = amount;
        this.description = description;
        this.type = type;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
