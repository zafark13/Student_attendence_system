package com.example.gogreen.attendencesystem;

import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SCAN extends AppCompatActivity {
    public static EditText sub,date;
    Button submit;
    String subject,dat;
    private DatabaseReference mdata;
    private FirebaseUser mcurrentuser;
    final int REQUEST_CODE_ASK_PERMISSIONS=123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        if (check_connectivity.isInternetAvailable(SCAN.this)) {
            sub = (EditText) findViewById(R.id.sub);
            date = (EditText) findViewById(R.id.date);
            submit = (Button) findViewById(R.id.submit);
            subject = getIntent().getStringExtra("subject").toString();
            dat = getIntent().getStringExtra("date").toString();
            sub.setText(subject);
            date.setText(dat);


            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //subject=sub.getText().toString();
                    //dat=date.getText().toString();
                    Intent i = new Intent(SCAN.this, OTP.class);
                    i.putExtra("subject", subject);
                    i.putExtra("date", dat);
                    startActivity(i);
                }
            });


        }
        else {
            check_connectivity.dialog(SCAN.this);
        }
    }


}
