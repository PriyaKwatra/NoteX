package com.example.acer.notex;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.acer.notex.db.NoteDatabase;
import com.example.acer.notex.db.testmain;

import java.util.ArrayList;

/**
 * Created by acer on 25.07.2017.
 */

public class testimportant extends Fragment{
    RecyclerView recycler;
    ArrayList<Tasks> notes;
    public static testmain newInstance(ArrayList<Tasks> notes) {

        Bundle args = new Bundle();
        args.putParcelableArrayList("abc",notes);

        testmain fragment = new testmain();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
          Bundle b=getArguments();

        View v =inflater.inflate(R.layout.testimportantfragment,container,false);

        recycler=(RecyclerView)v.findViewById(R.id.recyclerv);
        RecyclerView.LayoutManager manager =new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        recycler.setLayoutManager(manager);
        recycler.addItemDecoration(decoration);
        ArrayList<Tasks> importantTasks=b.getParcelableArrayList("abc");
        recycler.setAdapter(new RecyclerClass(importantTasks,getContext(),recycler));
        return  v;
    }
}
