package com.stxnext.intranet2.backend.model.project;

/**
 * Created by bkosarzycki on 02.11.15.
 */
public class Project implements Comparable<Project> {
    private int id;

    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Project another) {
        return this.getName().compareTo(another.getName());
    }

    public Project withName(String customName) {
        name = customName;
        return this;
    }
}
