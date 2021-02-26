package com.edu_touch.edu_hunt.Model;

public class fee_model {
    public String id;
    public String std_id;
    public String teacher_id;
    public String subject_id;
    public String class_group_id;
    public String amount;
    public String booking_date;
    public String status;
    public String booking_id;
    public String Subject_Name;
    public String t_image;
    public String Teacher_Name;
    public String Student_Name;
    public String Student_Email;
    public String Class_Groups;

    public fee_model() {
    }

    public fee_model(String id, String std_id, String teacher_id, String subject_id, String class_group_id, String amount, String booking_date, String status, String booking_id, String subject_Name, String t_image, String teacher_Name, String student_Name, String student_Email, String class_Groups) {
        this.id = id;
        this.std_id = std_id;
        this.teacher_id = teacher_id;
        this.subject_id = subject_id;
        this.class_group_id = class_group_id;
        this.amount = amount;
        this.booking_date = booking_date;
        this.status = status;
        this.booking_id = booking_id;
        Subject_Name = subject_Name;
        this.t_image = t_image;
        Teacher_Name = teacher_Name;
        Student_Name = student_Name;
        Student_Email = student_Email;
        Class_Groups = class_Groups;
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

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getClass_group_id() {
        return class_group_id;
    }

    public void setClass_group_id(String class_group_id) {
        this.class_group_id = class_group_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getSubject_Name() {
        return Subject_Name;
    }

    public void setSubject_Name(String subject_Name) {
        Subject_Name = subject_Name;
    }

    public String getT_image() {
        return t_image;
    }

    public void setT_image(String t_image) {
        this.t_image = t_image;
    }

    public String getTeacher_Name() {
        return Teacher_Name;
    }

    public void setTeacher_Name(String teacher_Name) {
        Teacher_Name = teacher_Name;
    }

    public String getStudent_Name() {
        return Student_Name;
    }

    public void setStudent_Name(String student_Name) {
        Student_Name = student_Name;
    }

    public String getStudent_Email() {
        return Student_Email;
    }

    public void setStudent_Email(String student_Email) {
        Student_Email = student_Email;
    }

    public String getClass_Groups() {
        return Class_Groups;
    }

    public void setClass_Groups(String class_Groups) {
        Class_Groups = class_Groups;
    }
}
