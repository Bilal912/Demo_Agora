package com.edu_touch.edu_hunt.Model;

public class payment_history_model {
    public String id;
    public String std_id;
    public String teacher_id;
    public String subject_id;
    public String class_group_id;
    public String amount;
    public String booking_date;
    public String Teacher_Name;
    public String Teacher_Code;
    public String Teacher_PhoneNumber;
    public String Student_Name;
    public String Student_Email;
    public String Class_Groups;
    public String t_image;

    public String getT_image() {
        return t_image;
    }

    public void setT_image(String t_image) {
        this.t_image = t_image;
    }

    public payment_history_model(String id, String std_id, String teacher_id, String subject_id, String class_group_id, String amount, String booking_date, String teacher_Name, String teacher_Code, String teacher_PhoneNumber, String student_Name, String student_Email, String class_Groups, String t_image, String starting_date, String teacher_otp, String starting_status, String created_date, String subject_Name) {
        this.id = id;
        this.std_id = std_id;
        this.teacher_id = teacher_id;
        this.subject_id = subject_id;
        this.class_group_id = class_group_id;
        this.amount = amount;
        this.booking_date = booking_date;
        Teacher_Name = teacher_Name;
        Teacher_Code = teacher_Code;
        Teacher_PhoneNumber = teacher_PhoneNumber;
        Student_Name = student_Name;
        Student_Email = student_Email;
        Class_Groups = class_Groups;
        this.t_image = t_image;
        this.starting_date = starting_date;
        this.teacher_otp = teacher_otp;
        this.starting_status = starting_status;
        this.created_date = created_date;
        Subject_Name = subject_Name;
    }

    public String getStarting_date() {
        return starting_date;
    }

    public void setStarting_date(String starting_date) {
        this.starting_date = starting_date;
    }

    public String getTeacher_otp() {
        return teacher_otp;
    }

    public void setTeacher_otp(String teacher_otp) {
        this.teacher_otp = teacher_otp;
    }

    public String getStarting_status() {
        return starting_status;
    }

    public void setStarting_status(String starting_status) {
        this.starting_status = starting_status;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getSubject_Name() {
        return Subject_Name;
    }

    public void setSubject_Name(String subject_Name) {
        Subject_Name = subject_Name;
    }

    public String starting_date;
    public String teacher_otp;
    public String starting_status;
    public String created_date;
    public String Subject_Name;

    public payment_history_model() {
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

    public String getTeacher_Name() {
        return Teacher_Name;
    }

    public void setTeacher_Name(String teacher_Name) {
        Teacher_Name = teacher_Name;
    }

    public String getTeacher_Code() {
        return Teacher_Code;
    }

    public void setTeacher_Code(String teacher_Code) {
        Teacher_Code = teacher_Code;
    }

    public String getTeacher_PhoneNumber() {
        return Teacher_PhoneNumber;
    }

    public void setTeacher_PhoneNumber(String teacher_PhoneNumber) {
        Teacher_PhoneNumber = teacher_PhoneNumber;
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
