package com.example.customer;

public class Cart_model {
    String category;
    String id;
    String img;
    String name;
    String pdescription;
    String pprice;
    String quantity;
    String vendor_id;
    String pname;

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPdescription() {
        return pdescription;
    }

    public void setPdescription(String pdescription) {
        this.pdescription = pdescription;
    }

    public String getPprice() {
        return pprice;
    }

    public void setPprice(String pprice) {
        this.pprice = pprice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }

    public Cart_model() {
    }

    public Cart_model(String category, String id, String img, String name, String pdescription, String pprice, String quantity, String vendor_id) {
        this.category = category;
        this.id = id;
        this.img = img;
        this.name = name;
        this.pdescription = pdescription;
        this.pprice = pprice;
        this.quantity = quantity;
        this.vendor_id = vendor_id;
    }
}
