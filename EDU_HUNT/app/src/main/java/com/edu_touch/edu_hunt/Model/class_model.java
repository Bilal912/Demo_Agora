package com.edu_touch.edu_hunt.Model;

public class class_model {
    String id,name,group_id;

    public class_model() {
    }

    public class_model(String id, String name, String group_id) {
        this.id = id;
        this.name = name;
        this.group_id = group_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }
}
