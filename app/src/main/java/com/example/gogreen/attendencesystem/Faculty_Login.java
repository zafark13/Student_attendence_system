package com.example.gogreen.attendencesystem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Faculty_Login extends AppCompatActivity {
    EditText mail, pass;
    //private DatabaseReference mdata;
    private FirebaseUser mcurrentuser;
    Button login;
    String email, password,name,proctor , id;
    private ProgressDialog mprogress;
    private FirebaseAuth mAuth;
    private DatabaseReference mdata;
     int auth =-1,count=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty__login);

        if(check_connectivity.isInternetAvailable(Faculty_Login.this)) {
            //final String[] email = new String[1];
            //final String[] password = new String[1];
            mail = (EditText) findViewById(R.id.mail);
            mprogress = new ProgressDialog(this);
            pass = (EditText) findViewById(R.id.pass);
            mAuth = FirebaseAuth.getInstance();


            login = (Button) findViewById(R.id.log);

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    email = mail.getText().toString();
                    password = pass.getText().toString();

                    if (!(TextUtils.isEmpty(password) || TextUtils.isEmpty(email))) {
                        mprogress.setTitle("Signing in");
                        mprogress.setMessage("please wait");
                        mprogress.setCanceledOnTouchOutside(false);
                        mprogress.show();
                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()){


                                    //Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
                                    mcurrentuser= FirebaseAuth.getInstance().getCurrentUser();
                                    final String uid = mcurrentuser.getUid();
                                    mdata= FirebaseDatabase.getInstance().getReference().child("Faculty").child(uid);
                                    mdata.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            //Toast.makeText(getApplicationContext(),"add value Listener",Toast.LENGTH_LONG).show();
                                            try {
                                                name = dataSnapshot.child("name").getValue().toString();
                                                proctor = dataSnapshot.child("Proctor").getValue().toString();
                                                id = dataSnapshot.child("id").getValue().toString();
                                                auth=1;
                                                mprogress.dismiss();
                                                nextactivity();
                                            } catch (Exception e) {
                                                auth=0;
                                                mprogress.dismiss();
                                                nextactivity();

                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                                            mprogress.dismiss();

                                        }
                                    });







                                }

                                else {
                                    mprogress.hide();
                                    pass.setText("");
                                    Toast.makeText(getApplicationContext(),task.getException().getMessage().toString(),Toast.LENGTH_LONG).show();
                                }

                            }
                        });

                    } else {
                        mprogress.hide();
                        Toast.makeText(getApplicationContext(), "Fields are empty", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        else {
            check_connectivity.dialog(Faculty_Login.this);
        }



    }

    private void getdata() {
       // Toast.makeText(getApplicationContext(),"inside getdata",Toast.LENGTH_LONG).show();

        mcurrentuser= FirebaseAuth.getInstance().getCurrentUser();
        final String uid = mcurrentuser.getUid();
        mdata= FirebaseDatabase.getInstance().getReference().child("Faculty").child(uid);
        mdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Toast.makeText(getApplicationContext(),"add value Listener",Toast.LENGTH_LONG).show();
                try {
                    name = dataSnapshot.child("name").getValue().toString();
                    proctor = dataSnapshot.child("Proctor").getValue().toString();
                    id = dataSnapshot.child("id").getValue().toString();
                    auth=1;
                } catch (Exception e) {
                    auth=0;

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                // mprogress.dismiss();

            }
        });


    }


    private void nextactivity() {
        String s="login";
        Intent i=new Intent(Faculty_Login.this,Faculty_logedin.class);
        Bundle extras = new Bundle();
        extras.putString("clas",s);
        extras.putString("name",name);
        extras.putString("proctor",proctor);
        extras.putString("id",id);
        extras.putInt("auth",auth);
        i.putExtras(extras);
        startActivity(i);
        finish();
    }

    public void Students(View view) {
        Intent newintent=new Intent(Faculty_Login.this, login.class);
        startActivity(newintent);
    }

    public void reset(View view) {
        Intent intent=new Intent(this,forgot_password.class);
        startActivity(intent);
    }
}

