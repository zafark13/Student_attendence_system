package com.example.gogreen.attendencesystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class Faculty_logedin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public TextView tv1, tv2, tv3;
    public Button code;
    private FirebaseUser mcurrentuser;
    private DatabaseReference mdata;
    public String name, proctor, id;
    public String[] p;
    int auth,count=0;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_logedin);
        Toolbar toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer1);
        if (check_connectivity.isInternetAvailable(Faculty_logedin.this)) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            email = user.getEmail();
            code = (Button) findViewById(R.id.qr);
            tv1 = (TextView) findViewById(R.id.name);
            tv2 = (TextView) findViewById(R.id.proctor);
            tv3 = (TextView) findViewById(R.id.id);
            tv1.setVisibility(View.GONE);
            tv2.setVisibility(View.GONE);
            tv3.setVisibility(View.GONE);
            code.setVisibility(View.GONE);
            Bundle extras = getIntent().getExtras();
            String clas = extras.getString("clas");
            if(clas.equals("login")){
                //
                //Toast.makeText(getApplicationContext(),"inside login",Toast.LENGTH_LONG).show();
                auth=extras.getInt("auth");
                //Toast.makeText(getApplicationContext(),Integer.toString(auth),Toast.LENGTH_LONG).show();
                if(auth==0){
                    //Toast.makeText(getApplicationContext(),"inside 0",Toast.LENGTH_LONG).show();
                                      new AlertDialog.Builder(Faculty_logedin.this)
                            .setTitle("Please upload your details")
                            .setMessage("tap on OK to upload details")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    Intent in=new Intent(Faculty_logedin.this,getFacultyDetails.class);
                                    startActivity(in);
                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();



                }
                if(auth==1){
                    //Toast.makeText(getApplicationContext(),"inside 1",Toast.LENGTH_LONG).show();
                    name=extras.getString("name");
                    proctor=extras.getString("proctor");
                    id=extras.getString("id");
                    tv1.setVisibility(View.VISIBLE);
                    tv2.setVisibility(View.VISIBLE);
                    tv3.setVisibility(View.VISIBLE);
                    code.setVisibility(View.VISIBLE);
                    p = proctor.split("_");
                    tv1.setText("Welcome "+name);
                    tv2.setText("Proctor of section "+p[1].toUpperCase()+" branch "+p[0]);
                    tv3.setText("your ID is "+id);
                    sharedpref yourPrefrence = sharedpref.getInstance(getApplicationContext());
                    yourPrefrence.storeF(name,proctor,id,"faculty");
                }
            }

            if (clas.equals("getfacultydetails")){
                name=getIntent().getStringExtra("name").toString();
                id=getIntent().getStringExtra("id").toString();
                proctor=getIntent().getStringExtra("proctor").toString();
                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
                tv3.setVisibility(View.VISIBLE);
                code.setVisibility(View.VISIBLE);
                p = proctor.split("_");
                tv1.setText("Welcome "+name);
                tv2.setText("Proctor of section "+p[1].toUpperCase()+" branch "+p[0]);
                tv3.setText("your ID is "+id);
                sharedpref yourPrefrence = sharedpref.getInstance(getApplicationContext());
                yourPrefrence.storeF(name,proctor,id,"faculty");
            }

        }

        else {
            check_connectivity.dialog(Faculty_logedin.this);
        }



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        /*View view=navigationView.inflateHeaderView(R.layout.nav_header);*/
       TextView name1 = (TextView)header.findViewById(R.id.name1);
       TextView email1 = (TextView)header.findViewById(R.id.email1);
        name1.setText(name);
        email1.setText(email);





    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        Toast.makeText(getApplicationContext(),"Press back again to quit",Toast.LENGTH_SHORT).show();
        count+=1;
        if (count>=2){
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }

    }
    public void change() {
        Intent intent=new Intent(Faculty_logedin.this,Faculty_change_pass.class);
        startActivity(intent);
    }

    public void generateqr(View view) {
        Intent intent=new Intent(Faculty_logedin.this,qr_details.class);
        startActivity(intent);
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.faculty_menu, menu);
        return false;
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        if (item.getItemId()==R.id.logout) {
            FirebaseAuth mAuth=FirebaseAuth.getInstance();
            mAuth.signOut();
            startActivity(new Intent(getApplicationContext(),Faculty_Login.class));

            // Toast.makeText(loged_in.this,"check attendence",Toast.LENGTH_LONG).show();
            // Handle the camera action
        } else if (item.getItemId() == R.id.changepass) {
            //Toast.makeText(loged_in.this,"Logout",Toast.LENGTH_LONG).show();
            change();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer1);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





}
