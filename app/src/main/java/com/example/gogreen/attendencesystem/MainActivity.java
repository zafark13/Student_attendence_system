package com.example.gogreen.attendencesystem;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    EditText pass,mail,unid,sec,name;
    private FirebaseAuth mAuth;
    private Firebase mrootref;
    String email,password,section,uid,branch,nam,ud;
    Button signin;
    RadioGroup radioGroup;
    RadioButton rb;
    ArrayAdapter arrayAdapter;
    private DatabaseReference mdatabase;
    private ProgressDialog mprogress;
    final int REQUEST_CODE_ASK_PERMISSIONS=123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(check_connectivity.isInternetAvailable(MainActivity.this)) {


            pass = (EditText) findViewById(R.id.pass);
            unid = (EditText) findViewById(R.id.unid);
            mail = (EditText) findViewById(R.id.mail);
            name = (EditText) findViewById(R.id.name);
            radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
            mAuth = FirebaseAuth.getInstance();
            signin = (Button) findViewById(R.id.button);
            radioGroup.clearCheck();
            mprogress = new ProgressDialog(this);
            signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    email = mail.getText().toString();
                    password = pass.getText().toString();
                    section = sec.getText().toString();
                    uid = unid.getText().toString();
                    nam = name.getText().toString();
                    int selectedid = radioGroup.getCheckedRadioButtonId();
                    rb = (RadioButton) findViewById(selectedid);
                    branch = rb.getText().toString();
                    //  Toast.makeText(getApplicationContext(),branch,Toast.LENGTH_LONG).show();
                    if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                        Toast.makeText(getApplicationContext(), "Fields are empty", Toast.LENGTH_LONG).show();
                    } else {
                        mprogress.setTitle("Registering");
                        mprogress.setMessage("please wait");
                        mprogress.setCanceledOnTouchOutside(false);
                        mprogress.show();
                        if (section.equals("Select your section")){
                            Toast.makeText(getApplicationContext(),"please select your section",Toast.LENGTH_LONG).show();
                        }
                        else {
                        sign(email, password);}

                    }
                }
            });



            Spinner spinner = findViewById(R.id.spinner2);
            final ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.sec, android.R.layout.simple_spinner_item);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                    ((TextView) parent.getChildAt(0)).setTextSize(18);
                    ((TextView)parent.getChildAt(0)).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));


                   // Toast.makeText(getApplicationContext(), Integer.toString(position), Toast.LENGTH_LONG).show();
                        section = parent.getItemAtPosition(position).toString();


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });



        }
        else {
            check_connectivity.dialog(MainActivity.this);
        }

    }

  /*  void checkUserpermission(){

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED  ){

            requestPermissions(new String[]{
                            Manifest.permission.CAMERA},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return ;
        }


    }





    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    break;
                } else {
                    // Permission Denied
                    Toast.makeText( this,"permission denied" , Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }*/


    public void sign(String email,String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
                            ud=currentuser.getUid();

                            mdatabase= FirebaseDatabase.getInstance().getReference().child("students").child(ud);

                            HashMap<String ,String> map=new HashMap<>();
                            map.put("branch_section",branch+"_"+section);
                            map.put("uid",uid);
                            map.put("branch",branch);
                            map.put("section",section);
                            map.put("name",nam);
                            mdatabase.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    HashMap<String,String> hashMap=new HashMap<>();
                                    hashMap.put("no_of_present_classes","0");
                                    hashMap.put("no_of_absent_classes","0");
                                    mdatabase=FirebaseDatabase.getInstance().getReference().child("students").child(ud).child("total_attendence");
                                    mdatabase.setValue(hashMap);

                                    if (task.isSuccessful()) {
                                        mprogress.dismiss();
                                        Intent i = new Intent(MainActivity.this, login.class);
                                        startActivity(i);
                                    }
                                }
                            });

                            // Sign in success, update UI with the signed-in user's information
                            Log.d("", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            mprogress.hide();
                            // If sign in fails, display a message to the user.
                            Log.w("", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void login(View view) {
        Intent intent=new Intent(this,login.class);
        startActivity(intent);
    }
}
