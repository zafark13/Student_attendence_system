package com.example.gogreen.attendencesystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class OTP extends AppCompatActivity {
    private DatabaseReference mdata;
    private FirebaseUser mcurrentuser;
    private Firebase  mrootref;
    Button submit;
    String newurl;
    int no;int i=0;
    private ProgressDialog mprogress;
    String pin,subject,date,uid;
    DatabaseReference databaseReference;
    String branch_section,name;

    EditText otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_otp);
        subject = getIntent().getStringExtra("subject");
        date = getIntent().getStringExtra("date");
        Toolbar toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("QR Details");
        toolbar.setTitleTextColor(getResources().getColor(R.color.BLACK));

        TextView subcode=(TextView)findViewById(R.id.name);
        TextView ddate=(TextView)findViewById(R.id.date);
        subcode.setText(subject);
        ddate.setText(date.substring(0,2)+"/"+date.substring(2,4)+"/"+date.substring(4,8));

        sharedpref yourPrefrence = sharedpref.getInstance(getApplicationContext());
        branch_section=yourPrefrence.getdata("branch_section");
        name=yourPrefrence.getdata("nameS");


        getotp();
        i=0;


        if (check_connectivity.isInternetAvailable(OTP.this)) {
            otp = (EditText) findViewById(R.id.otp);
            mcurrentuser = FirebaseAuth.getInstance().getCurrentUser();
            uid = mcurrentuser.getUid();
            //Toast.makeText(getApplicationContext(), date, Toast.LENGTH_LONG).show();
            submit = (Button) findViewById(R.id.submit);

            //String url = "https://attendence-system-d4dba.firebaseio.com/students/"+uid+"/"+subject;
            //String url = "https://attendence-system-d4dba.firebaseio.com/students/"+uid+"/"+"attendence/"+subject;
            databaseReference =FirebaseDatabase.getInstance().getReference().child("students").child(uid).child("attendence").child(subject);
            String url = databaseReference.getRef().toString();
            //Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
            mrootref = new Firebase(url);


            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String onetp = otp.getText().toString();
                   /* mprogress.setTitle("Please wait");
                    mprogress.setMessage("we are marking your attendence..");
                    mprogress.setCanceledOnTouchOutside(false);
                    mprogress.show();*/

                    //getotp();

                    // Toast.makeText(OTP.this,pin,Toast.LENGTH_LONG).show();
                    if (pin.equals(onetp)) {

                        Firebase childref = mrootref.child(date);
                        childref.setValue("present");
                        Toast.makeText(getApplicationContext(), "your attendence is marked present successfully", Toast.LENGTH_LONG).show();
                        //next();
                       // mprogress.dismiss();

                        databaseReference =FirebaseDatabase.getInstance().getReference().child("students").child(uid).child("total_attendence");
                        databaseReference.child("no_of_present_classes").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                no=Integer.parseInt(dataSnapshot.getValue().toString());
                                no=no+1;
                                dataSnapshot.getRef().setValue(no);
                                /*newurl = databaseReference.getRef().toString();
                                mrootref = new Firebase(newurl);
                                //Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();

                                    Firebase childreff = mrootref.child("no_of_present_classes");
                                    childreff.setValue(no);
                                    i++;*/

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(),databaseError.getMessage().toString(),Toast.LENGTH_LONG).show();
                            }
                        });

                        //databaseReference =FirebaseDatabase.getInstance().getReference().child("students").child(uid).child("total_attendence");


                        Intent i = new Intent(OTP.this, loged_in.class);
                        i.putExtra("name",name);
                        startActivity(i);
                        finish();



                   /* mcurrentuser=FirebaseAuth.getInstance().getCurrentUser();
                    String uid = mcurrentuser.getUid();
                    present present = new present(date,"present");
                    Toast.makeText(getApplicationContext(),present.toString(),Toast.LENGTH_LONG).show();
                    mdata=FirebaseDatabase.getInstance().getReference().child(uid).child(subject);
                    mdata.setValue(present);*/


                  /*  mcurrentuser=FirebaseAuth.getInstance().getCurrentUser();
                    String uid = mcurrentuser.getUid();
                    present present = new present(date);
                    mdata= FirebaseDatabase.getInstance().getReference().child(uid).child(subject);
                    Toast.makeText(OTP.this,"in if statement",Toast.LENGTH_LONG).show();
                    HashMap<String,String> map = new HashMap<>();
                    map.put(date,"present");
                    mdata.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(OTP.this,"attendence marked as present",Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(OTP.this, "Error", Toast.LENGTH_LONG).show();
                                }
                        }
                    });*/

                    } else {
                       // mprogress.hide();
                        //Toast.makeText(OTP.this, pin, Toast.LENGTH_SHORT).show();
                        Toast.makeText(OTP.this, "Enter correct otp", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }

        else {
            check_connectivity.dialog(OTP.this);
        }
    }


    private void getotp() {

        mdata= FirebaseDatabase.getInstance().getReference().child("admin").child(branch_section);
        mdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pin=dataSnapshot.child("otp").getValue().toString();
                //Toast.makeText(OTP.this, pin.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(OTP.this, "Failed to retrieve otp", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this,scanner.class);
        startActivity(intent);
    }
}

