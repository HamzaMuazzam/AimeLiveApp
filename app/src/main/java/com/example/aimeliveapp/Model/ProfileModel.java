package com.example.aimeliveapp.Model;

public class ProfileModel {

    private Integer icon;
    private String title;
    private String desc;

    public ProfileModel(Integer icon, String title, String desc) {
        this.icon = icon;
        this.title = title;
        this.desc = desc;
    }

    public Integer getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }
}
