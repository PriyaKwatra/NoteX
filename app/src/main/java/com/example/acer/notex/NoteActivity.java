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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.acer.notex.db.NoteDatabase;

import java.util.Calendar;

public class NoteActivity extends AppCompatActivity {
  EditText noten;
    FloatingActionButton noteAdd;
    Context c;
    public static final String NOTE= "note";
    public static final String IMPORTANT="imp";
    Tasks task;
    Integer alarmSet=0;
    Integer isImportant=0;
    Intent i;
    PendingIntent pi;
    long notifyTime;
    ImageView important ;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(NOTE,noten.getText().toString());
        outState.putInt(IMPORTANT,isImportant);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        i=null;

        noten=(EditText)findViewById(R.id.noten);
        if(savedInstanceState!=null)
        {
            noten.setText(savedInstanceState.getString(NOTE,""));
            isImportant=savedInstanceState.getInt(IMPORTANT);
        }
        noteAdd=(FloatingActionButton)findViewById(R.id.addn);
        important=(ImageView)findViewById(R.id.important);
        c=this;
        if(isImportant==0)
        {
            important.setImageResource(R.mipmap.notimportant);
        }
        else{

            important.setImageResource(R.mipmap.important);
        }
        important.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isImportant==0)
                {
                    isImportant=1;
                    important.setImageResource(R.mipmap.important);
                    Toast.makeText(c,"marked as important",Toast.LENGTH_SHORT).show();

                }
                else{
                    isImportant=0;
                    important.setImageResource(R.mipmap.notimportant);

                }
            }
        });

        noteAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(noten.length()==0)
                {

                    Toast.makeText(c,"Nothing written",Toast.LENGTH_SHORT).show();
                }
                else{
                NoteDatabase noteDatabase =new NoteDatabase(c);
                task=new Tasks(noten.getText().toString(),false,alarmSet,isImportant);
                    if(i!=null)
                    {
                        final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP,
                                notifyTime,
                                pi);
                    }
                noteDatabase.insertTask(task);
                Intent i=new Intent(NoteActivity.this,Main2Activity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.newnotemenu, menu);
        return true;

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.taskReminder)
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
                            timePicker.getCurrentHour(), timePicker.getCurrentMinute(), 0);// TODO: 20.07.2017
                    notifyTime = calendar.getTimeInMillis();
                     i = new Intent(getBaseContext(),TaskReceiver.class);

                    i.putExtra("TASK",noten.getText().toString());
                    i.putExtra("ID",noten.getId());

                     pi = PendingIntent.getBroadcast(getBaseContext(),
                            1,
                            i,
                            PendingIntent.FLAG_UPDATE_CURRENT);




                    alarmSet=1;
                }
            }).create();
            alertDialog.show();



        }

        if(item.getItemId()==R.id.share)
        {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, noten.getText().toString());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
        return super.onOptionsItemSelected(item);

    }
}
