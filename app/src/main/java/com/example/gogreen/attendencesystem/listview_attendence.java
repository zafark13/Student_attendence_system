package com.example.gogreen.attendencesystem;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
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
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class listview_attendence extends AppCompatActivity {
    TextView from ,to;
    ArrayList<String> dates = new ArrayList<String>();
    ArrayList<String> state=new ArrayList<String>();
    Button submit;
    int i=0,a=0;
    String[] date,stat;
    ArrayAdapter arrayAdapter;
    public ArrayList<String> subjects=new ArrayList<String>();
    String day,currentDate,uid, month,Fdate,Tdate,subcode;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatePickerDialog.OnDateSetListener DateSetListener;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_attendence);

        Toolbar toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Attendence details");
        progressDialog=new ProgressDialog(this);
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        uid=firebaseUser.getUid();
        sharedpref yourPrefrence = sharedpref.getInstance(getApplicationContext());
        subjects.addAll(yourPrefrence.retriveSharedValue());
        databaseReference= FirebaseDatabase.getInstance().getReference();







        Spinner spinner = findViewById(R.id.spinner6);
        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,subjects);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // .makeText(getApplicationContext(),Integer.toString(position),Toast.LENGTH_LONG).show();
                ((TextView)parent.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView)parent.getChildAt(0)).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                ((TextView)parent.getChildAt(0)).setTextSize(21);



                subcode=parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        from = (TextView) findViewById(R.id.from);
        to = (TextView) findViewById(R.id.to);


        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        listview_attendence.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }

        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int m, int d) {
                m = m + 1;
                if (d < 10) {
                    day = "0" + Integer.toString(d);
                } else day = Integer.toString(d);
                if (m < 10) {
                    month = "0" + Integer.toString(m);
                } else month = Integer.toString(m);
                //Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
                Fdate = day + month + year;
                //Toast.makeText(getApplicationContext(),Fdate,Toast.LENGTH_LONG).show();
                String date = day + "/" + month + "/" + year;
                from.setText(date);
            }
        };


        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        listview_attendence.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        DateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }

        });

        DateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int m, int d) {
                m = m + 1;
                if (d < 10) {
                    day = "0" + Integer.toString(d);
                } else day = Integer.toString(d);
                if (m < 10) {
                    month = "0" + Integer.toString(m);
                } else month = Integer.toString(m);
                //Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
                Tdate = day + month + year;
                //Toast.makeText(getApplicationContext(),Fdate,Toast.LENGTH_LONG).show();
                String date = day + "/" + month + "/" + year;
                to.setText(date);
            }
        };


        submit=(Button)findViewById(R.id.getdetails);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Please wait");
                progressDialog.setMessage("we getting required data");
                progressDialog.show();


                final DateFormat df1 = new SimpleDateFormat("ddMMyyyy");
                final DateFormat df2 = new SimpleDateFormat("ddMMyyyy");


                Date date1 = null;
                Date date2 = null;

                try {
                    date1 = df1.parse(Fdate);
                    date2 = df1.parse(Tdate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long diff=date2.getTime()-date1.getTime();
                int d=(int)diff/(1000*60*60*24);
                date=new String[d+1];
                stat=new String[d+1];


                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(date1);


                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(date2);

                while (!cal1.after(cal2)) {

                    dates.add(df2.format(cal1.getTime()));
                    cal1.add(Calendar.DATE, 1);

                }

                 databaseReference=databaseReference.child("students").child(uid).child("attendence").child("RAS502");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (i = 0; i < dates.size(); i++) {
                               try{
                                   //state.add(dataSnapshot.child(dates.get(i)).getValue().toString());
                                   stat[a]=dataSnapshot.child(dates.get(i)).getValue().toString();
                                   date[a]=dates.get(i);
                                   //Toast.makeText(getApplicationContext(),date[a]+stat[a],Toast.LENGTH_LONG).show();
                                   a++;
                               }
                               catch (Exception e){
                                   Toast.makeText(getApplicationContext(),"not found",Toast.LENGTH_LONG).show();

                               }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_LONG).show();

                        }
                    });


                Thread thread= new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {

                            if (dates.size()==i) {

                                Intent intent = new Intent(listview_attendence.this, list_view.class);
                                Bundle bundle = new Bundle();
                                bundle.putStringArray("dates", date);
                                bundle.putStringArray("state", stat);
                                bundle.putInt("no", a);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                progressDialog.dismiss();
                                break;
                            }
                        }


                    }
                });thread.start();











            }
        });







    }


}
