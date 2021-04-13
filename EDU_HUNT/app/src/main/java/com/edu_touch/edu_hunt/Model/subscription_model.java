package com.edu_touch.edu_hunt.Model;

public class subscription_model {
    String id,std_id,amount,date;

    public subscription_model() {
    }

    public subscription_model(String id, String std_id, String amount, String date) {
        this.id = id;
        this.std_id = std_id;
        this.amount = amount;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStd_id() {
        return std_id;
    }

    public void setStd_id(String std_id) {
        this.std_id = std_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
