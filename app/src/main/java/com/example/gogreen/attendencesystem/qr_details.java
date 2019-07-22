package com.example.gogreen.attendencesystem;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class qr_details extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView tv1;
    String qr,currentDate,date,branch,section;
    Spinner spinner,spinner1,spinner2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_details);

        if(check_connectivity.isInternetAvailable(qr_details.this)) {
            Firebase.setAndroidContext(this);
            //Calendar calendar = Calendar.getInstance();
            //currentDate = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());
            //currentDate.replace("/","-");
        /*Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy/mm/dd");
        String currentDate = "Current Date : " + mdformat.format(calendar.getTime());*/

            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
            currentDate = sdf.format(new Date());
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
            date = sdf1.format(new Date());


            tv1 = (TextView) findViewById(R.id.textView7);
            tv1.setText(date);

            spinner = findViewById(R.id.spinner);
            ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.subcodes, android.R.layout.simple_spinner_item);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);
            spinner.setOnItemSelectedListener(this);

            spinner1 = findViewById(R.id.sec);
            ArrayAdapter<CharSequence> Adapter = ArrayAdapter.createFromResource(this, R.array.section, android.R.layout.simple_spinner_item);
            Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner1.setAdapter(Adapter);
            spinner1.setOnItemSelectedListener(this);

            spinner2 = findViewById(R.id.branch);
            ArrayAdapter<CharSequence> array = ArrayAdapter.createFromResource(this, R.array.branch, android.R.layout.simple_spinner_item);
            array.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(array);
            spinner2.setOnItemSelectedListener(this);
        }

        else {
            check_connectivity.dialog(qr_details.this);
        }




    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spinner = (Spinner)parent;
        Spinner spinner1 = (Spinner)parent;
        Spinner spinner2=(Spinner)parent;

        if(spinner.getId() == R.id.spinner)
        {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            ((TextView) parent.getChildAt(0)).setTextSize(18);
            ((TextView)parent.getChildAt(0)).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

            qr=parent.getItemAtPosition(position).toString();
            //Toast.makeText(getApplicationContext(),"you have selected subject code "+qr,Toast.LENGTH_LONG).show();
            qr=qr+","+currentDate;
            //Toast.makeText(this, "Your choose :" + countries[position],Toast.LENGTH_SHORT).show();
        }
        if(spinner1.getId() == R.id.sec)
        {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            ((TextView) parent.getChildAt(0)).setTextSize(18);
            ((TextView)parent.getChildAt(0)).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            section=parent.getItemAtPosition(position).toString();
            //Toast.makeText(getApplicationContext(),"you have selected section "+section,Toast.LENGTH_LONG).show();
            //Toast.makeText(this, "Your choose :" + city[position],Toast.LENGTH_SHORT).show();
        }
        if(spinner2.getId() == R.id.branch)
        {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            ((TextView) parent.getChildAt(0)).setTextSize(18);
            ((TextView)parent.getChildAt(0)).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            branch=parent.getItemAtPosition(position).toString();
            //Toast.makeText(getApplicationContext(),"you have selected branch "+branch,Toast.LENGTH_LONG).show();
            //Toast.makeText(this, "Your choose :" + countries[position],Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void genqr(View view) {

        if (spinner.getSelectedItem().toString().equals("Select subject code"))
            Toast.makeText(this, "Please select subject code", Toast.LENGTH_SHORT).show();
        else if (spinner1.getSelectedItem().toString().equals("select section"))
            Toast.makeText(this, "Please select section", Toast.LENGTH_SHORT).show();
        else if (spinner2.getSelectedItem().toString().equals("Select branch"))
            Toast.makeText(this, "Please select branch", Toast.LENGTH_SHORT).show();
        else {


            DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference().child("admin").child(branch + "_" + section);
            Firebase rootref = new Firebase(mdatabase.getRef().toString());
            Firebase child = rootref.child("otp");
            child.setValue(Integer.toString(QR_code_generated.generateRandomNumber()));


            Intent intent = new Intent(this, QR_code_generated.class);
            intent.putExtra("qr", qr);
            intent.putExtra("branch", branch);
            intent.putExtra("section", section);
            intent.putExtra("date", currentDate);
            startActivity(intent);
        }
    }



}
