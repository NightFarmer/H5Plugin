package com.nightfarmer.h5plugin.sample;

/**
 * Created by zhangfan on 2016/5/10 0010.
 */
public class Person {
    private String name;
    private boolean isMan;

    public Person(String name) {
        this.name = name;
    }

    public Person(String name, boolean isMan) {
        this.name = name;
        this.isMan = isMan;
    }

    @Override
    public String toString() {
        return "name:" + name + ", isMain:" + isMan;
    }
}
