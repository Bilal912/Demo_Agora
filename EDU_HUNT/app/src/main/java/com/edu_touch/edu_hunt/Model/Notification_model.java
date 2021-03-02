package com.edu_touch.edu_hunt.Model;

public class Notification_model {
    String message,date,id,std_id;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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

    public Notification_model() {
    }

    public Notification_model(String message, String date, String id, String std_id) {
        this.message = message;
        this.date = date;
        this.id = id;
        this.std_id = std_id;
    }
}
