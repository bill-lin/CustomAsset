package com.linguo.customasset.model;

/**
 * Created by bin on 03/09/2016.
 */
public class Greeting {

    private  long count;
    private  String message;

    public Greeting(long count, String message) {
        this.count = count;
        this.message = message;
    }

    public long getCount() {
        return count;
    }

    public String getMessage() {
        return message;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
