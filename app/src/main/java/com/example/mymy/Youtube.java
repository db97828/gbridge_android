package com.example.mymy;

public class Youtube {
    private String id;
    private String key;
    private String name;
    private String size;

    public Youtube(String id, String key, String name, String size) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }
}
