package com.example.gogreen.attendencesystem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {
    EditText pass,mail;
    Button login;
    String email,password,nam,branch_section;
    private FirebaseAuth mAuth;
    private DatabaseReference mdata;
    private FirebaseUser mcurrentuser;
    private ProgressDialog mprogress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (check_connectivity.isInternetAvailable(login.this)) {


            pass = (EditText) findViewById(R.id.lpass);
            mail = (EditText) findViewById(R.id.lmail);
            login = (Button) findViewById(R.id.login);
            mAuth = FirebaseAuth.getInstance();
            mprogress = new ProgressDialog(this);
            // final sharedpref yourPrefrence = sharedpref.getInstance(getApplicationContext());

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    password = pass.getText().toString();
                    email = mail.getText().toString();
                    if (!(TextUtils.isEmpty(password) || TextUtils.isEmpty(email))) {
                        mprogress.setTitle("Signing in");
                        mprogress.setMessage("please wait");
                        mprogress.setCanceledOnTouchOutside(false);
                        mprogress.show();
                        login(email, password);

                    } else {
                        mprogress.hide();
                        Toast.makeText(getApplicationContext(), "Fields are empty", Toast.LENGTH_LONG).show();
                    }
                }
            });


        }
        else
        {
            check_connectivity.dialog(login.this);
          // Toast.makeText(getApplicationContext(),"check Internet connectivity",Toast.LENGTH_LONG).show();
        }
    }

    private void getname() {
        mcurrentuser= FirebaseAuth.getInstance().getCurrentUser();
        final String uid = mcurrentuser.getUid();
        mdata= FirebaseDatabase.getInstance().getReference().child("students").child(uid);
        mdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                nam=dataSnapshot.child("name").getValue().toString();
                branch_section=dataSnapshot.child("branch_section").getValue().toString();
                sharedpref yourPrefrence = sharedpref.getInstance(getApplicationContext());
                yourPrefrence.storeS(nam,"student",branch_section);
                //Toast.makeText(getApplicationContext(),yourPrefrence.getdata("previous"),Toast.LENGTH_LONG).show();
                nextactivity(); }
                catch (Exception e){
                    mprogress.dismiss();
                    Toast.makeText(getApplicationContext(),"Login from Faculty Login activity",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(login.this,Faculty_Login.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void login(String email, String password) {
       mAuth.signInWithEmailAndPassword(email,password)
               .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()) {
                           getname();


                       }
                       else {
                           Toast.makeText(getApplicationContext(),task.getException().getMessage()+" Check email and password again.",Toast.LENGTH_LONG).show();
                           pass.setText("");
                           mprogress.hide();
                       }
                   }
               });

    }


    private void nextactivity() {
        mprogress.dismiss();
        Intent i =new Intent(this,loged_in.class);
        i.putExtra("name",nam);
        startActivity(i);
        finish();
        ///mprogress.dismiss();

    }

    public void register(View view) {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void faculty(View view) {
        Intent in = new Intent(login.this,Faculty_Login.class);
        startActivity(in);
    }

    public void reset(View view) {
        Intent intent=new Intent(this,forgot_password.class);
        startActivity(intent);
    }
}
