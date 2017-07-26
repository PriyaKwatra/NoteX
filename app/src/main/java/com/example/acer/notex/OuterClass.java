package com.example.acer.notex;

import java.util.ArrayList;

/**
 * Created by acer on 24.07.2017.
 */

public class OuterClass {

    ArrayList<Tasks> tasks;
    public OuterClass()
    {

    }

    public OuterClass(ArrayList<Tasks> tasks) {
        this.tasks = tasks;
    }

    public ArrayList<Tasks> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Tasks> tasks) {
        this.tasks = tasks;
    }
}
