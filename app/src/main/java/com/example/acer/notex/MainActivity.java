package com.example.acer.notex;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.acer.notex.db.NoteDatabase;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        c=this;


    }

    @Override
    protected void onResume() {
        super.onResume();
        recycler=(RecyclerView)findViewById(R.id.recyclerv);
        newNote=(FloatingActionButton)findViewById(R.id.newnote);
        fbAuth= FirebaseAuth.getInstance();
        try{
            if(fbAuth.getCurrentUser()==null)
            {       Intent signInIntent= AuthUI.getInstance().createSignInIntentBuilder().setIsSmartLockEnabled(false).setAvailableProviders(Arrays.asList(
                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                    new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build())).setTheme(R.style.AppTheme_AppBarOverlay).build();
                startActivityForResult(signInIntent,135);
            }}
        catch(Exception e)
        {
            finishActivity(135);
        }


        { NoteDatabase noteDatabase=new NoteDatabase(this);
        RecyclerView.LayoutManager manager =new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recycler.setLayoutManager(manager);
        notes= noteDatabase.getAllTasks();

        recycler.setAdapter(new RecyclerClass(notes,this,recycler));}
        newNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,NoteActivity.class);
                startActivity(i);
                finish();
            }});
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.important, menu);
        return true;}
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId()==R.id.important)
        {  database=new NoteDatabase(this);
           importantTasks=database.getImportantTasks();
          if(importantTasks.size()==0)
          {Toast.makeText(this,"no important tasks to show",Toast.LENGTH_SHORT).show();
          }
          else{
               Intent i=new Intent(MainActivity.this,ImportantActivity.class);
               startActivity(i);}
               }


          if(item.getItemId()==R.id.export)
          {
          if(fbAuth.getCurrentUser()==null)
          {
              Toast.makeText(c,"please sign in to sync",Toast.LENGTH_SHORT);
          }
           if(fbAuth.getCurrentUser()!=null&&notes!=null)
          {
              MyAsyncTask myTask=new MyAsyncTask();
              myTask.execute();

          }




        }
        return super.onOptionsItemSelected(item);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
        if(requestCode==135)
        {   username= FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            fbDatabase = FirebaseDatabase.getInstance();
            fbReference = fbDatabase.getReference().child(fbAuth.getCurrentUser().getUid());
            fbReference.addValueEventListener(new ValueEventListener(){

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    MyAsyncTask1 task1=new MyAsyncTask1();
                    task1.execute(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });





        }}catch(Exception e){
            Toast.makeText(c,"Check internet connection",Toast.LENGTH_SHORT).show();



        }


    }
    //for importing notes
    class MyAsyncTask1 extends AsyncTask<DataSnapshot,Void,ArrayList<Tasks>> {

        ProgressDialog  progressDialog;

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog =new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("fetching notes ");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(ArrayList<Tasks>  task) {





                   NoteDatabase database=new NoteDatabase(c);
            for(Tasks m:task)
            {
                database.insertTask(m);
            }
                    progressDialog.hide();

                 if(task!=null)
                  recycler.setAdapter(new RecyclerClass(task,c,recycler));


                    else{

                     Toast.makeText(c,"no notes to show",Toast.LENGTH_SHORT).show();
                 }




        }

        @Override
        protected ArrayList<Tasks> doInBackground(DataSnapshot... params) {

                    DataSnapshot dataSnapshot=params[0];
                     tasks= new ArrayList<Tasks>();
                    for (DataSnapshot child: dataSnapshot.getChildren()) {
                        Tasks tas=child.getValue(Tasks.class);
                        tasks.add(child.getValue(Tasks.class));


        }
            return tasks;
    }}






    //for exporting notes to cloud
    class MyAsyncTask extends AsyncTask<Void,Void,Void> {

        ProgressDialog progressDialog;

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("please wait ");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


        }

        @Override
        protected Void doInBackground(Void... params) {
            fbDatabase = FirebaseDatabase.getInstance();
            fbReference = fbDatabase.getReference();

                fbReference.child(fbAuth.getCurrentUser().getUid()).setValue(notes).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.hide();

                    }
                });


            return null;
        }

    }




}
