package com.example.yourtaskmanager.Model;

public class Data {
    private String title;
    private String node;
    private String date;
    private String id;


    public Data(String title, String node, String date, String id) {
        this.title = title;
        this.node = node;
        this.date = date;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
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
}
