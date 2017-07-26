package com.example.acer.notex;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.acer.notex.db.NoteDatabase;
import com.example.acer.notex.db.testmain;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Arrays;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
    TextView name;
    TextView email;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);






        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    protected void onResume() {
        super.onResume();
        c=this;

        fbAuth= FirebaseAuth.getInstance();
        try{
            if(fbAuth.getCurrentUser()==null)
            {       Intent signInIntent= AuthUI.getInstance().createSignInIntentBuilder().setIsSmartLockEnabled(false).setAvailableProviders(Arrays.asList(
                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                    new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build())).setTheme(R.style.AppTheme_AppBarOverlay).build();
                    startActivityForResult(signInIntent,135);
            }
            else{
                name=(TextView) findViewById(R.id.name);
                email=(TextView) findViewById(R.id.email);
                imageView=(ImageView) findViewById(R.id.imageView);
                SharedPreferences sharedPreferences=getPreferences(MODE_PRIVATE);
                name.setText(sharedPreferences.getString("name"," "));
                Log.e("name",sharedPreferences.getString("name"," "));
                email.setText(sharedPreferences.getString("email"," "));
                Log.e("email",sharedPreferences.getString("email"," "));
                Picasso.with(c).load(sharedPreferences.getString("url"," ")).into(imageView);
                Log.e("url",sharedPreferences.getString("url",""));
                FragmentManager fragmentManager = getSupportFragmentManager();
                 testmain fragment=new testmain();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame,fragment);
                fragmentTransaction.commit();}

        }
        catch(Exception e)
        {
            finishActivity(135);
        }




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {

            final FragmentManager fragmentManager = getSupportFragmentManager();
            testmain test=new testmain();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.frame,test);

            fragmentTransaction.commit();

        } else if (id == R.id.important) {
            final FragmentManager fragmentManager = getSupportFragmentManager();
            NoteDatabase database=new NoteDatabase(c);
            ArrayList<Tasks> task=database.getImportantTasks();


            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.frame,testimportant.newInstance(task));

            fragmentTransaction.commit();


        } else if (id == R.id.reminder) {

            NoteDatabase database=new NoteDatabase(c);
           ArrayList<Tasks> task=database.getReminders();
            final FragmentManager fragmentManager = getSupportFragmentManager();
            testimportant test=new testimportant();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.frame,testimportant.newInstance(task));

            fragmentTransaction.commit();



        } else if (id == R.id.Log) {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if(requestCode==135)
            {   username= FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                fbDatabase = FirebaseDatabase.getInstance();
                fbReference = fbDatabase.getReference().child(fbAuth.getCurrentUser().getUid());
                TextView name=(TextView) findViewById(R.id.name);
                TextView email=(TextView) findViewById(R.id.email);
                fbAuth= FirebaseAuth.getInstance();
                ImageView imageView=(ImageView) findViewById(R.id.imageView);
                name.setText(fbAuth.getCurrentUser().getDisplayName());
                SharedPreferences sharedPreferences=getPreferences(MODE_PRIVATE);
                final SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("name",fbAuth.getCurrentUser().getDisplayName());

                email.setText(fbAuth.getCurrentUser().getEmail());
                editor.putString("email",fbAuth.getCurrentUser().getEmail());
                Picasso.with(c).load(fbAuth.getCurrentUser().getPhotoUrl()).into(imageView);
                editor.putString("url",fbAuth.getCurrentUser().getPhotoUrl().toString());
                editor.apply();

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

        ProgressDialog progressDialog;

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog =new ProgressDialog(Main2Activity.this);
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
            {
                FragmentManager fragmentManager = getSupportFragmentManager();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, testmain.newInstance(task));
                fragmentTransaction.commit();}


            else{
                FragmentManager fragmentManager = getSupportFragmentManager();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, new testmain());
                fragmentTransaction.commit();

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
            progressDialog = new ProgressDialog(Main2Activity.this);
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
