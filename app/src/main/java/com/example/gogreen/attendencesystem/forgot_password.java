package com.example.gogreen.attendencesystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgot_password extends AppCompatActivity {
    private EditText email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email=(EditText)findViewById(R.id.email);
        Toolbar toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("   Forgot password");


    }

    public void reset(View view) {
        String Email=email.getText().toString();
        FirebaseAuth.getInstance().sendPasswordResetEmail(Email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    forgot_password.this).create();

                            // Setting Dialog Title
                            alertDialog.setTitle("Password reset");

                            // Setting Dialog Message
                            alertDialog.setMessage("please check your inbox");

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
                        else {
                            Toast.makeText(getApplicationContext(),task.getException().getMessage().toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
