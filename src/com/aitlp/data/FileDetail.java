package com.aitlp.data;

public class FileDetail {

    private String id;
    private int mtime;
    private String type;
    private String name;
    private int size;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setMtime(int mtime) {
        this.mtime = mtime;
    }

    public int getMtime() {
        return mtime;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

}