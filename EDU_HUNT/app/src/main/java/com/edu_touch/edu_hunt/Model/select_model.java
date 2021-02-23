package com.edu_touch.edu_hunt.Model;

public class select_model {
    String subjects,boards,class_name,fees,id;
    String class_id;
    String subjects_id;
    String boards_id;

    public select_model(String subjects, String boards, String class_name, String fees, String id, String class_id, String subjects_id, String boards_id) {
        this.subjects = subjects;
        this.boards = boards;
        this.class_name = class_name;
        this.fees = fees;
        this.id = id;
        this.class_id = class_id;
        this.subjects_id = subjects_id;
        this.boards_id = boards_id;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getSubjects_id() {
        return subjects_id;
    }

    public void setSubjects_id(String subjects_id) {
        this.subjects_id = subjects_id;
    }

    public String getBoards_id() {
        return boards_id;
    }

    public void setBoards_id(String boards_id) {
        this.boards_id = boards_id;
    }

    public select_model() {
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
