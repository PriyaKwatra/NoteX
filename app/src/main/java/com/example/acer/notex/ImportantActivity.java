package com.example.acer.notex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.example.acer.notex.db.NoteDatabase;

import java.util.ArrayList;

public class ImportantActivity extends AppCompatActivity {
RecyclerView recycler;
    ArrayList<Tasks>notes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_important);
        recycler=(RecyclerView)findViewById(R.id.recyclerv);
        RecyclerView.LayoutManager manager =new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recycler.setLayoutManager(manager);
        NoteDatabase database=new NoteDatabase(this);
        ArrayList<Tasks> importantTasks=database.getImportantTasks();
        recycler.setAdapter(new RecyclerClass(importantTasks,this,recycler));
    }




}
