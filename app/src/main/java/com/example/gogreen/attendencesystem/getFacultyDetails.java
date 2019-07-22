package com.example.gogreen.attendencesystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class getFacultyDetails extends AppCompatActivity {
    EditText fullname, proctorC, identification;
    Button sub;
    private FirebaseAuth mAuth;
    private DatabaseReference mdatabase;
    private ProgressDialog mprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_faculty_details);

        if(check_connectivity.isInternetAvailable(getFacultyDetails.this)) {

            mprogress = new ProgressDialog(this);
            fullname = (EditText) findViewById(R.id.name);
            proctorC = (EditText) findViewById(R.id.proctor);
            identification = (EditText) findViewById(R.id.id);
            sub = (Button) findViewById(R.id.submit);
            //abh();

            sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = fullname.getText().toString();
                    String proctorOf = proctorC.getText().toString();
                    String id = identification.getText().toString();

                    if (TextUtils.isEmpty(name) || TextUtils.isEmpty(proctorOf) || TextUtils.isEmpty(id)) {
                        Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_LONG).show();
                    } else {
                        mprogress.setTitle("Saving");
                        mprogress.setMessage("please wait");
                        mprogress.setCanceledOnTouchOutside(false);
                        mprogress.show();
                        Save(name, proctorOf, id);
                    }
                }
            });
        }
        else {
            check_connectivity.dialog(getFacultyDetails.this);
        }
    }


    private void Save(final String name, final String proctorOf, final String id) {

        FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
        String ud = currentuser.getUid();
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Faculty").child(ud);

        HashMap<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("Proctor", proctorOf);
        map.put("id", id);

        mdatabase.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    mprogress.dismiss();
                    Intent i = new Intent(getFacultyDetails.this, Faculty_logedin.class);
                    i.putExtra("clas","getfacultydetails");
                    i.putExtra("name",name);
                    i.putExtra("proctor",proctorOf);
                    i.putExtra("id",id);
                    startActivity(i);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Something went wrong,Please try again",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}


