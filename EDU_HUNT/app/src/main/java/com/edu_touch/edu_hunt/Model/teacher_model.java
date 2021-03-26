package com.edu_touch.edu_hunt.Model;

public class teacher_model {
    public String id;
    public String google_lat;

    public int distance;
    public float dis;

    public float getDis() {
        return dis;
    }

    public void setDis(float dis) {
        this.dis = dis;
    }

    public teacher_model(String id, String google_lat, int distance, float dis, String google_long, String teacher_code, String city, String qualification, String experience, String t_image, String address, String subjects, String boards, String class_name, String fees, String teacher_name, String class_id, String subjects_id, String boards_id) {
        this.id = id;
        this.google_lat = google_lat;
        this.distance = distance;
        this.dis = dis;
        this.google_long = google_long;
        this.teacher_code = teacher_code;
        this.city = city;
        this.qualification = qualification;
        this.experience = experience;
        this.t_image = t_image;
        this.address = address;
        this.subjects = subjects;
        this.boards = boards;
        this.class_name = class_name;
        this.fees = fees;
        this.teacher_name = teacher_name;
        this.class_id = class_id;
        this.subjects_id = subjects_id;
        this.boards_id = boards_id;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getGoogle_lat() {
        return google_lat;
    }

    public void setGoogle_lat(String google_lat) {
        this.google_lat = google_lat;
    }

    public String getGoogle_long() {
        return google_long;
    }

    public void setGoogle_long(String google_long) {
        this.google_long = google_long;
    }

    public String google_long;
    public String teacher_code;
    public String city;
    public String qualification;
    public String experience;
    public String t_image;
    public String address;
    public String subjects;
    public String boards;
    public String class_name;
    public String fees;
    public String teacher_name;

    public String class_id;
    public String subjects_id;
    public String boards_id;

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

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public teacher_model() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeacher_code() {
        return teacher_code;
    }

    public void setTeacher_code(String teacher_code) {
        this.teacher_code = teacher_code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getT_image() {
        return t_image;
    }

    public void setT_image(String t_image) {
        this.t_image = t_image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
}
