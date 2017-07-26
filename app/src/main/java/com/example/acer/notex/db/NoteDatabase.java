package com.example.acer.notex.db;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.acer.notex.Tasks;
import java.util.ArrayList;


/**
 * Created by acer on 18.07.2017.
 */

public class NoteDatabase extends SQLiteOpenHelper implements TableTask{
    public static final String DATABASE_NAME="tasks.db";
    public static final int VERSION=1;

    public NoteDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, VERSION);
    }
    public NoteDatabase(Context context)
    {

        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {


        String CREATE_TABL= CREATE+ TABLE_NAME+ LBR+ COLUMN_ID+ INT_PK_AUTOIC+
                COMMA+ COLUMN_TASK+ TYPE_TEXT+ COMMA+ COLUMN_IS_DONE+ TYPE_TEXT+COMMA+COLUMN_IS_ALARMSET+TYPE_INTEGER+
                COMMA+COLUMN_IS_IMPORTANT+TYPE_INTEGER
                + RBR+ TERMINATE;
        db.execSQL(CREATE_TABL);

    }
    public void insertTask(Tasks task)
    {
        SQLiteDatabase sqlDb=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_TASK,task.getTaskName());
        cv.put(COLUMN_IS_DONE,task.isDone());
        cv.put(COLUMN_IS_ALARMSET,task.isAlarmSet());
        cv.put(COLUMN_IS_IMPORTANT,task.getImportant());
        sqlDb.insert(TABLE_NAME,null,cv);


    }
    public void updateTable(Integer id,Tasks task)
    {    String m=id.toString();
        SQLiteDatabase sqlDBW =getWritableDatabase();


        SQLiteDatabase sqlDBR =getReadableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_IS_DONE,task.isDone());
        sqlDBW.update(TABLE_NAME,cv,COLUMN_ID+ " = "+id,null);
    }
    public void updateNote(Integer id,String s)
    {    String m=id.toString();
        SQLiteDatabase sqlDBW =getWritableDatabase();


        SQLiteDatabase sqlDBR =getReadableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_TASK,s);
        sqlDBW.update(TABLE_NAME,cv,COLUMN_ID+ " = "+id,null);
    }
    public void updateIsAlarm(Integer id,int update)
    {    String m=id.toString();
        SQLiteDatabase sqlDBW =getWritableDatabase();


        SQLiteDatabase sqlDBR =getReadableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_IS_ALARMSET,update);
        sqlDBW.update(TABLE_NAME,cv,COLUMN_ID+ " = "+id,null);
    }
    public void updateIsImportant(Integer id,int update)
    {    String m=id.toString();
        SQLiteDatabase sqlDBW =getWritableDatabase();


        SQLiteDatabase sqlDBR =getReadableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_IS_IMPORTANT,update);
        sqlDBW.update(TABLE_NAME,cv,COLUMN_ID+ " = "+id,null);
    }



    public void deleteRow(Integer id)
    {   Log.e("TAG",id+"");
        SQLiteDatabase sqlDBW =getWritableDatabase();
        sqlDBW.delete(TABLE_NAME,COLUMN_ID + " = ?",new String[]{id.toString()});
        sqlDBW.close();
    }


    public Tasks getOneTask(Integer idi)
    {
        SQLiteDatabase database=getReadableDatabase();
        Log.e("Databaseeeeee",database.toString());
        Cursor c= database.query(TABLE_NAME,null,COLUMN_ID+" = "+idi,null,null,null,null);
        Tasks taskm=null;
        while(c.moveToNext())
        {
            String task;
            int id;
            boolean status;
            Integer isAlarm;
            Integer important;
            int taskcolumn= c.getColumnIndex(COLUMN_TASK);
            int idColumn =c.getColumnIndex(COLUMN_ID);
            int idStatus=c.getColumnIndex(COLUMN_IS_DONE);
            int isAlarmSet=c.getColumnIndex(COLUMN_IS_ALARMSET);
            int isImp=c.getColumnIndex(COLUMN_IS_IMPORTANT);
            task=c.getString(taskcolumn);
            id=c.getInt(idColumn);
            status= (1==c.getInt(idStatus)) ;
            isAlarm= (c.getInt(isAlarmSet)) ;
           important=(c.getInt(isImp));
            taskm=(new Tasks(task,id,status,isAlarm,important));
        }
        return taskm;

    }
    public ArrayList<Tasks> getAllTasks()
    {


        SQLiteDatabase database=getReadableDatabase();
        Log.e("Databaseeeeee",database.toString());
        Cursor c= database.query(TABLE_NAME,null,null,null,null,null,COLUMN_ID+" DESC ");
        ArrayList<Tasks> tasks=new ArrayList<>();
        while(c.moveToNext())
        {

            String task;
            int id;
            boolean status;
            Integer isAlarm;
            Integer imp;
            int taskcolumn= c.getColumnIndex(COLUMN_TASK);
            int idColumn =c.getColumnIndex(COLUMN_ID);
            int idStatus=c.getColumnIndex(COLUMN_IS_DONE);
            int isAlarmSet=c.getColumnIndex(COLUMN_IS_ALARMSET);
            int isImportant=c.getColumnIndex(COLUMN_IS_IMPORTANT);
            task=c.getString(taskcolumn);
            id=c.getInt(idColumn);
            status= (1==c.getInt(idStatus)) ;
            isAlarm= (c.getInt(isAlarmSet)) ;
            imp=(c.getInt(isImportant));
            tasks.add(new Tasks(task,id,status,isAlarm,imp));

        }
        c.close();
        return tasks;

    }
    public ArrayList<Tasks> getImportantTasks()
    {


        SQLiteDatabase database=getReadableDatabase();
        Log.e("Databaseeeeee",database.toString());
        Cursor c= database.query(TABLE_NAME,null,COLUMN_IS_IMPORTANT+" = "+1,null,null,null,COLUMN_ID+" DESC ");
        ArrayList<Tasks> tasks=new ArrayList<>();
        while(c.moveToNext())
        {

            String task;
            int id;
            boolean status;
            Integer isAlarm;
            Integer imp;
            int taskcolumn= c.getColumnIndex(COLUMN_TASK);
            int idColumn =c.getColumnIndex(COLUMN_ID);
            int idStatus=c.getColumnIndex(COLUMN_IS_DONE);
            int isAlarmSet=c.getColumnIndex(COLUMN_IS_ALARMSET);
            int isImportant=c.getColumnIndex(COLUMN_IS_IMPORTANT);
            task=c.getString(taskcolumn);
            id=c.getInt(idColumn);
            status= (1==c.getInt(idStatus)) ;
            isAlarm= (c.getInt(isAlarmSet)) ;
            imp=c.getInt(isImportant);
            tasks.add(new Tasks(task,id,status,isAlarm,imp));

        }
        c.close();
        return tasks;

    }
    public ArrayList<Tasks> getReminders()
    {


        SQLiteDatabase database=getReadableDatabase();
        Log.e("Databaseeeeee",database.toString());
        Cursor c= database.query(TABLE_NAME,null,COLUMN_IS_ALARMSET+" = "+1,null,null,null,COLUMN_ID+" DESC ");
        ArrayList<Tasks> tasks=new ArrayList<>();
        while(c.moveToNext())
        {

            String task;
            int id;
            boolean status;
            Integer isAlarm;
            Integer imp;
            int taskcolumn= c.getColumnIndex(COLUMN_TASK);
            int idColumn =c.getColumnIndex(COLUMN_ID);
            int idStatus=c.getColumnIndex(COLUMN_IS_DONE);
            int isAlarmSet=c.getColumnIndex(COLUMN_IS_ALARMSET);
            int isImportant=c.getColumnIndex(COLUMN_IS_IMPORTANT);
            task=c.getString(taskcolumn);
            id=c.getInt(idColumn);
            status= (1==c.getInt(idStatus)) ;
            isAlarm= (c.getInt(isAlarmSet)) ;
            imp=c.getInt(isImportant);
            tasks.add(new Tasks(task,id,status,isAlarm,imp));

        }
        c.close();
        return tasks;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(WHEN_UPGRADING_FROM_1_2);
    }





}
