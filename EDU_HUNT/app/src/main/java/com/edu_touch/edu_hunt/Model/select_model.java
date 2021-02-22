package com.edu_touch.edu_hunt.Model;

public class select_model {
    String subjects,boards,class_name,fees,id;

    public select_model() {
    }

    public select_model(String subjects, String boards, String class_name, String fees, String id) {
        this.subjects = subjects;
        this.boards = boards;
        this.class_name = class_name;
        this.fees = fees;
        this.id = id;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public String getBoards() {
        return boards;
    }

    public void setBoards(String boards) {
        this.boards = boards;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
