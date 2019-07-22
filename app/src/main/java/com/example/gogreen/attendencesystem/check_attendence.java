package com.example.gogreen.attendencesystem;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.support.v7.widget.Toolbar;

public class check_attendence extends AppCompatActivity {
    private TextView from;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatePickerDialog.OnDateSetListener DateSetListener;
    String day,currentDate,uid, month,Fdate,Tdate,subcode;
    Button getdetails;
    FirebaseAuth mAuth;
    public ArrayList<String> subjects=new ArrayList<String>();
    private ProgressDialog mprogress;
    ArrayAdapter arrayAdapter;
    TextView state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_attendence);
        Toolbar toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Check your attendence");
        mprogress=new ProgressDialog(this);
        from=(TextView)findViewById(R.id.from);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        currentDate = sdf.format(new Date());
        from.setText(currentDate);
        SimpleDateFormat sd = new SimpleDateFormat("ddMMyyyy");
        Fdate=sd.format(new Date());
        state=(TextView)findViewById(R.id.state);
        state.setVisibility(View.GONE);

        getdetails=(Button)findViewById(R.id.getdetails);
        sharedpref yourPrefrence = sharedpref.getInstance(getApplicationContext());
        try{
        subjects.addAll(yourPrefrence.retriveSharedValue());}
        catch (Exception e){
            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();

            // Setting Dialog Title
            alertDialog.setTitle("No data found in data base");

            // Setting Dialog Message
            alertDialog.setMessage("Please contact concern authority");

            // Setting Icon to Dialog
            //alertDialog.setIcon(R.drawable.tick);

            // Setting OK Button
            alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onBackPressed();

                }
            });

            // Showing Alert Message
            alertDialog.show();
        }

        mAuth=FirebaseAuth.getInstance();
        uid =mAuth.getCurrentUser().getUid();


        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        check_attendence.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }

        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int m, int d) {
                m = m + 1;
                if(d<10){
                    day="0"+Integer.toString(d);
                }
                else day=Integer.toString(d);
                if(m<10){
                    month="0"+Integer.toString(m);
                }
                else month=Integer.toString(m);
                //Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
                Fdate=day+month+year;
                //Toast.makeText(getApplicationContext(),Fdate,Toast.LENGTH_LONG).show();
                String date =  day+"/"+month+"/"+year;
                from.setText(date);
            }
        };


       /* to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        check_attendence.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        DateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }

        });

        DateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int m, int d) {
                m = m + 1;
                if(d<10){
                    day="0"+Integer.toString(d);
                }
                else day=Integer.toString(d);
                if(m<10){
                    month="0"+Integer.toString(m);
                }
                else month=Integer.toString(m);
                //Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
                 Tdate=day+month+year;
                 Toast.makeText(getApplicationContext(),Tdate,Toast.LENGTH_LONG).show();
                String date =  day+"/"+month+"/"+year;
                to.setText(date);
            }
        };*/



        Spinner spinner = findViewById(R.id.spinner3);
        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,subjects);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // .makeText(getApplicationContext(),Integer.toString(position),Toast.LENGTH_LONG).show();
                ((TextView)parent.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView)parent.getChildAt(0)).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                ((TextView)parent.getChildAt(0)).setTextSize(25);



                subcode=parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        getdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference =    mFirebaseDatabase.getReference().child("students").child(uid).child("attendence").child(subcode);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){
                                state.setVisibility(View.VISIBLE);
                                try{

                                if(dataSnapshot.child(Fdate).getValue().toString().equals("present")){
                                    state.setTextColor(getResources().getColor(R.color.Green));
                                }
                                if (dataSnapshot.child(Fdate).getValue().toString().equals("absent")){
                                    state.setTextColor(getResources().getColor(R.color.RED));
                                }
                                state.setText(dataSnapshot.child(Fdate).getValue().toString());}
                                catch (Exception e){
                                    state.setTextColor(getResources().getColor(R.color.WHITE));
                                    state.setText("attendence not found");
                                }


                                //Log.v(TAG,""+ childDataSnapshot.getKey()); //displays the key for the node
                                //Log.v(TAG,""+ childDataSnapshot.child(--ENTER THE KEY NAME eg. firstname or email etc.--).getValue());   //gives the value for given keyname

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_LONG).show();
                    }
                });
               /* Intent intent=new Intent(check_attendence.this,attendence_details.class);
                intent.putExtra("Fdate",Fdate);
                intent.putExtra("subcode",subcode);
                startActivity(intent);*/


            }
        });

    }


}
