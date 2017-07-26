package com.example.acer.notex;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.acer.notex.db.NoteDatabase;

import java.util.Calendar;

public class ModifyNote extends AppCompatActivity {
     EditText notem;
    FloatingActionButton modifyNote;
    int k;
    Context c;
    int important;
    long notifyTime;
    Intent i;
    PendingIntent pi;
    ImageView imp;
    NoteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_note);
        notem=(EditText)findViewById(R.id.notem);
        c=this;

        modifyNote=(FloatingActionButton)findViewById(R.id.modifynote);
        if(savedInstanceState!=null)
        {
            notem.setText(savedInstanceState.getString("NOTE",""));
            k=savedInstanceState.getInt("NOTENO");
        }
        else{
            Intent i=getIntent();
            notem.setText(i.getStringExtra("NOTE"));

            k=i.getIntExtra("ID",0);
        }
         database=new NoteDatabase(this);
       Tasks t= database.getOneTask(k);
       important= t.getImportant();

      imp=(ImageView)findViewById(R.id.important);
        c=this;
        if(important==0)
        {
            imp.setImageResource(R.mipmap.notimportant);

        }
        else{

            imp.setImageResource(R.mipmap.important);
        }
        imp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(important==0)
                {
                    important=1;
                    imp.setImageResource(R.mipmap.important);
                    database.updateIsImportant(k,important);
                    Toast.makeText(c,"marked as important",Toast.LENGTH_SHORT).show();


                }
                else{
                    important=0;
                    imp.setImageResource(R.mipmap.notimportant);
                    database.updateIsImportant(k,important);


                }
            }
        });

        modifyNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteDatabase database=new NoteDatabase(c);
                database.updateNote(k,notem.getText().toString());
                Toast.makeText(c,"saved",Toast.LENGTH_SHORT).show();



            }
        });



    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("NOTE",notem.getText().toString());
        outState.putInt("NOTENO",k);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        NoteDatabase database=new NoteDatabase(this);
        Tasks task=database.getOneTask(k);
       if( task.isAlarmSet()==0)

        inflater.inflate(R.menu.newnotemenu, menu);
        else{
           inflater.inflate(R.menu.modifymenu,menu);
       }
        return true;

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.taskReminder||item.getItemId()==R.id.ModifyReminder)
        {

            final TimePicker timePicker;
            final DatePicker datePicker;
            LayoutInflater inflater=LayoutInflater.from(this);
            View v=inflater.inflate(R.layout.timepicker,null);
            timePicker=(TimePicker) v.findViewById(R.id.tpicker);
            datePicker=(DatePicker) v.findViewById(R.id.dpicker);
            datePicker.updateDate(2017,7,19);
            datePicker.setMinDate(System.currentTimeMillis()-1000);

            AlertDialog alertDialog=new AlertDialog.Builder(this).setTitle("Set time to notify").setView(v).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    Calendar calendar = Calendar.getInstance();
                    calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                            timePicker.getCurrentHour(), timePicker.getCurrentMinute(), 0);
                    notifyTime = calendar.getTimeInMillis();
                    i = new Intent(getBaseContext(),TaskReceiver.class);

                    i.putExtra("TASK",notem.getText().toString());
                    i.putExtra("ID",notem.getId());

                    pi = PendingIntent.getBroadcast(getBaseContext(),
                            k,
                            i,
                            PendingIntent.FLAG_UPDATE_CURRENT);

                    final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP,
                            notifyTime,
                            pi);
                    NoteDatabase noteDatabase=new NoteDatabase(c);
                    noteDatabase.updateIsAlarm(k,1);
                }
            }).create();
            alertDialog.show();

        }

        if(item.getItemId()==R.id.share)
        {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, notem.getText().toString());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
        else if(item.getItemId()==R.id.DeleteReminder){
            Toast.makeText(c,"reminder deleted",Toast.LENGTH_SHORT).show();
            i = new Intent(getBaseContext(),TaskReceiver.class);
            i.putExtra("TASK",notem.getText().toString());
            i.putExtra("ID",notem.getId());
            PendingIntent.getBroadcast(getBaseContext(),k,i,PendingIntent.FLAG_UPDATE_CURRENT).cancel();
            NoteDatabase noteDatabase=new NoteDatabase(this);
            noteDatabase.updateIsAlarm(k,0);


        }
        return super.onOptionsItemSelected(item);

    }







    }

