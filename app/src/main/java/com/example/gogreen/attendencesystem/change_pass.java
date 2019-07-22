package com.example.gogreen.attendencesystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class change_pass extends AppCompatActivity{
    EditText newpass, oldpass,confirmpass;
    Button Bconfirm;
    private FirebaseUser user;
    private ProgressDialog mprogress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassowd);
        mprogress=new ProgressDialog(this);
        newpass=(EditText)findViewById(R.id.passnew);
        oldpass=(EditText)findViewById(R.id.passold);
        confirmpass=(EditText)findViewById(R.id.conPass);
        Bconfirm= (Button) findViewById(R.id.conbutton);
        Toolbar toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.changepass);



        Bconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mprogress.setTitle("Please wait");
                mprogress.setMessage("while we make changes in database");
                mprogress.show();
                String oldpassword = oldpass.getText().toString();
                final String newpassword = newpass.getText().toString();
                String confirmpassord = confirmpass.getText().toString();
                if (newpassword.equals(confirmpassord)) {

                    user = FirebaseAuth.getInstance().getCurrentUser();
                    final String email = user.getEmail();
                    AuthCredential credential = EmailAuthProvider.getCredential(email, oldpassword);

                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updatePassword(newpassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Something went wrong,Please try again later", Toast.LENGTH_LONG).show();

                                        } else {
                                            Toast.makeText(getApplicationContext(), "Password changed successfully,Please login again", Toast.LENGTH_LONG).show();
                                            mprogress.dismiss();
                                            nextactivity();
                                        }
                                    }
                                });
                            } else {
                                mprogress.hide();
                                Toast.makeText(getApplicationContext(), "Authentication Failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }


        });
    }

    private void nextactivity() {
        Intent intent=new Intent(change_pass.this,login.class);
        startActivity(intent);
    }
}
