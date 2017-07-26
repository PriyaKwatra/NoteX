package com.example.acer.notex.db;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.acer.notex.Main2Activity;
import com.example.acer.notex.NoteActivity;
import com.example.acer.notex.R;
import com.example.acer.notex.RecyclerClass;
import com.example.acer.notex.SpacesItemDecoration;
import com.example.acer.notex.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.support.v7.widget.StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS;

/**
 * Created by acer on 25.07.2017.
 */

public class testmain extends Fragment {
    RecyclerView recycler;
    FloatingActionButton newNote;
    ArrayList<Tasks> notes;
    DatabaseReference fbReference;
    FirebaseDatabase fbDatabase;
    Context c;
    FirebaseAuth fbAuth;
    String username="";
    ArrayList<Tasks> importantTasks;
    ArrayList<Tasks> tasks;

    NoteDatabase database;

    @Nullable

    public static testmain newInstance(ArrayList<Tasks> notes) {

        Bundle args = new Bundle();
        args.putParcelableArrayList("abc",notes);

        testmain fragment = new testmain();
        fragment.setArguments(args);
        return fragment;
    }
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.testmainfragment,container,false);
        Bundle args=getArguments();
        ArrayList<Tasks>m=new ArrayList<>();
        recycler=(RecyclerView) v.findViewById(R.id.recyclerv);
        newNote=(FloatingActionButton)v.findViewById(R.id.newnote);
        StaggeredGridLayoutManager manager =new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        manager.setGapStrategy(GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);


        recycler.setLayoutManager(manager);
        SpacesItemDecoration decoration = new SpacesItemDecoration(16);

        recycler.addItemDecoration(decoration);
        try{
            m= args.getParcelableArrayList("abc");
            recycler.setAdapter(new RecyclerClass(m,getContext(),recycler));}
        catch(Exception e)
        {
            NoteDatabase noteDatabase=new NoteDatabase(getContext());
            notes= noteDatabase.getAllTasks();
            recycler.setAdapter(new RecyclerClass(notes,getContext(),recycler));

        }




        newNote.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i=new Intent(getContext(),NoteActivity.class);
            startActivity(i);
        }});

        return v;
    }
}
