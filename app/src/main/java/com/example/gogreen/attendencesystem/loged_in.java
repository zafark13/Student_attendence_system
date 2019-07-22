package com.example.gogreen.attendencesystem;

import android.Manifest;
import android.app.ActionBar;
import android.app.PictureInPictureParams;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class loged_in extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mauth;
    String mail;
    private ProgressDialog mprogress;
    String nam;
    TextView name,name1,email1;
    Button scan;
    int count,present=0,absent=0,i=0,total;
    public float pre=-1f,abs=-1f;
    public PieChart pieChart;
    final int REQUEST_CODE_ASK_PERMISSIONS=123;
    //float percent;
    //ArrayList<String> subjects=new ArrayList<>();
    //Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loged_in);
        mprogress=new ProgressDialog(this);
        Toolbar toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        toolbar.setTitleTextColor(getResources().getColor(R.color.BLACK));
        if(getIntent().getStringExtra("name")!=null){
        nam = getIntent().getStringExtra("name").toString();}
        else{
            sharedpref yourPrefrence = sharedpref.getInstance(getApplicationContext());
            nam=yourPrefrence.getdata("nameS");
        }
        name = (TextView) findViewById(R.id.name);
        pieChart=(PieChart)findViewById(R.id.chart);
        name.setText("Welcome "+nam);
        pieChart.setNoDataText("Please wait while we load data");
        pre=-1f;abs=-1f;

        if (check_connectivity.isInternetAvailable(loged_in.this)) {
            scan = (Button) findViewById(R.id.scan);
            if (Build.VERSION.SDK_INT >= 23) {
                checkUserpermission();
            }
            getdata();

           /* Handler h=new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    peidata();
                }
            }, 3000);*/

            Thread thread=new Thread(new Runnable() {
                @Override
                public void run() {
                    while (pre==-1f||abs==-1f){

                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            peidata();
                        }
                    });



                }
            });thread.start();



            scan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        Intent i = new Intent(loged_in.this, scanner.class);
                        startActivity(i);
                        finish();
                }
            });



            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            View header=navigationView.getHeaderView(0);
            ImageView imageView=(ImageView)findViewById(R.id.imageView);
            /*View view=navigationView.inflateHeaderView(R.layout.nav_header);*/
            name1 = (TextView)header.findViewById(R.id.name1);
            email1 = (TextView)header.findViewById(R.id.email1);
            name1.setText(nam);
            email1.setText(email);

        }
        else {
            check_connectivity.dialog(loged_in.this);
        }







    }






    private void getdata() {
        mauth=FirebaseAuth.getInstance();
        String uid=mauth.getCurrentUser().getUid();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("students").child(uid).child("total_attendence").child("no_of_absent_classes");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                absent=Integer.parseInt(dataSnapshot.getValue().toString());
                abs=Float.parseFloat(Integer.toString(absent)+"f");
                //Toast.makeText(getApplicationContext(),abs,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage().toString(),Toast.LENGTH_LONG).show();

            }
        });
        DatabaseReference mreference=FirebaseDatabase.getInstance().getReference().child("students").child(uid).child("total_attendence").child("no_of_present_classes");
        mreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                present=Integer.parseInt(dataSnapshot.getValue().toString());
                pre=Float.parseFloat(Integer.toString(present)+"f");
                //Toast.makeText(getApplicationContext(),pre,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage().toString(),Toast.LENGTH_LONG).show();

            }
        });
    }

    private void peidata() {
        ArrayList<PieEntry> pieEntryList=new ArrayList<PieEntry>();
        pieEntryList.add(new PieEntry(abs,"Absent"));
        pieEntryList.add(new PieEntry(pre,"Present"));

        PieDataSet pieDataSet=new PieDataSet(pieEntryList,"Attendence");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData=new PieData(pieDataSet);

        pieChart.setUsePercentValues(true);
        pieChart.setCenterText("Attendence");
        pieChart.setCenterTextSize(20);
        pieChart.getDescription().setEnabled(false);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }


    @Override
    public void onBackPressed() {
        count++;
        Toast.makeText(getApplicationContext(),"Press back again to quit",Toast.LENGTH_SHORT).show();
        if (count>=2) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }

   /* public void logout(View view) {
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        mAuth.signOut();
        startActivity(new Intent(getApplicationContext(),login.class));

    }

    public void attendence(View view) {
        Intent intent=new Intent(loged_in.this,check_attendence.class);
        startActivity(intent);
    }*/





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawable_layout, menu);
        return false;
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        if (item.getItemId()==R.id.check) {
            Intent intent=new Intent(loged_in.this,check_attendence.class);
            startActivity(intent);

           // Toast.makeText(loged_in.this,"check attendence",Toast.LENGTH_LONG).show();
            // Handle the camera action
        } else if (item.getItemId() == R.id.logout) {
            //Toast.makeText(loged_in.this,"Logout",Toast.LENGTH_LONG).show();
            FirebaseAuth mAuth=FirebaseAuth.getInstance();
            mAuth.signOut();
            startActivity(new Intent(getApplicationContext(),login.class));


        }
        else if (item.getItemId() == R.id.changepass) {
            //Toast.makeText(loged_in.this,"Logout",Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(),change_pass.class));


        }
        else if (item.getItemId() == R.id.detailed) {
            //Toast.makeText(loged_in.this,"Logout",Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(),listview_attendence.class));


        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }











    void checkUserpermission(){

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED  ){

            requestPermissions(new String[]{
                            Manifest.permission.CAMERA},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return ;
        }


    }





    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    break;
                } else {
                    // Permission Denied
                    Toast.makeText( this,"permission denied" , Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }










}
