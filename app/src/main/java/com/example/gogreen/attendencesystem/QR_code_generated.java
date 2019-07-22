package com.example.gogreen.attendencesystem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.security.SecureRandom;
import java.util.HashMap;

public class QR_code_generated extends AppCompatActivity {

    private ImageView imageView;
    private int range = 9, length = 4;
    public int i;
    private String one = "", otp, branch, section, date,sub,url,branch_section;
    String newurl;
    DatabaseReference ref;
    private FirebaseAuth mAuth;
    private DatabaseReference mdatabase;
    private Firebase mrootref;
    public TextView tv1;
    public Button genotp;
    public ProgressDialog mprogess;
    public int absent=0,present=0;
    Query query1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_generated);
        mprogess=new ProgressDialog(this);
        Firebase.setAndroidContext(this);
        tv1 = (TextView) findViewById(R.id.otptv);
        tv1.setVisibility(View.GONE);





        if (check_connectivity.isInternetAvailable(QR_code_generated.this)) {
            imageView = (ImageView) findViewById(R.id.image);
            String QR = getIntent().getStringExtra("qr").toString();
            section = getIntent().getStringExtra("section").toString();
            branch = getIntent().getStringExtra("branch").toString();
            date = getIntent().getStringExtra("date").toString();
            String[] q=QR.split(",");
            sub=q[0];

            //Toast.makeText(getApplicationContext(), sub+date, Toast.LENGTH_LONG).show();

            if (!TextUtils.isEmpty(QR)) {
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(QR, BarcodeFormat.QR_CODE, 500, 500);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    imageView.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "something went wrong,Please try again", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            check_connectivity.dialog(QR_code_generated.this);
        }


    }

    public void genotp(View view) {

        final String otp = Integer.toString(generateRandomNumber());
        mdatabase = FirebaseDatabase.getInstance().getReference().child("admin").child(branch+"_"+section);
        HashMap<String, String> map = new HashMap<>();
        map.put("otp", otp);
        mdatabase.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                genotp = (Button) findViewById(R.id.otp1);

                genotp.setVisibility(View.GONE);
                tv1.setVisibility(View.VISIBLE);
                tv1.setText(otp);
                Handler h = new Handler();

                h.postDelayed(new Runnable() {

                    public void run() {
                        one = Integer.toString(generateRandomNumber());
                        mdatabase = FirebaseDatabase.getInstance().getReference().child("admin").child(branch+"_"+section);
                        HashMap<String, String> map = new HashMap<>();
                        map.put("otp", one);
                        mdatabase.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                runOnUiThread(new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv1.setText("OTP EXPIRED");
                                    }
                                }));

                            }
                        });

                        mprogess.setTitle("Taking Attendence");
                        mprogess.setMessage("Please wait while we mark all attendence");
                        mprogess.setCanceledOnTouchOutside(false);
                        mprogess.show();
                       /* PrimeThread p = new PrimeThread(branch, section, date);
                        p.start();*/

                        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                        final Query query = reference.child("students").orderByChild("branch_section").equalTo(branch+"_"+section);
                                //equalTo(branch + "_" + section);
                        //Toast.makeText(getApplicationContext(),"found it",Toast.LENGTH_LONG).show();
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {


                                    // dataSnapshot is the "issue" node with all children with id 0
                                    for (DataSnapshot students : dataSnapshot.getChildren()) {
                                        //z=0;
                                        //Toast.makeText(getApplicationContext(),students.getRef().toString(),Toast.LENGTH_LONG).show();
                                        //Toast.makeText(getApplicationContext(),Boolean.toString(!students.child(sub).hasChild(date)),Toast.LENGTH_LONG).show();//
                                        // Toast.makeText(getApplicationContext(),students.getKey().toString(),Toast.LENGTH_LONG).show();


                                        if (!(students.child("attendence").child(sub).hasChild(date))) {
                                             url=students.child("attendence").getRef().toString();

                                             mrootref = new Firebase(url);
                                             Firebase childref = mrootref.child(sub).child(date);
                                             childref.setValue("absent");
                                             //no=0;
                                             absent++;
                                                newurl = students.child("total_attendence").getRef().toString();
                                               // Toast.makeText(getApplicationContext(),newurl,Toast.LENGTH_LONG).show();
                                                ref=FirebaseDatabase.getInstance().getReferenceFromUrl(newurl+"/"+"no_of_absent_classes");
                                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                       /* absentno[i]=Integer.parseInt(dataSnapshot.getValue().toString());
                                                        urll[i]=newurl;
                                                        i++;*/

                                                       // Toast.makeText(getApplicationContext(),"data snapshot",Toast.LENGTH_LONG).show();
                                                           // Toast.makeText(getApplicationContext(),"absent"+dataSnapshot.getValue().toString(),Toast.LENGTH_LONG).show();
                                                            i = Integer.parseInt(dataSnapshot.getValue().toString());
                                                            i = i + 1;

                                                            dataSnapshot.getRef().setValue(i);
                                                            //DatabaseReference refer = FirebaseDatabase.getInstance().getReferenceFromUrl(newurl+"/"+"no_of_absent_classes/");


                                                           /* getMrootref = new Firebase(newurl);
                                                            //Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
                                                                // Toast.makeText(getApplicationContext(),"inside if ",Toast.LENGTH_LONG).show();
                                                                Firebase childreff = getMrootref.child("no_of_absent_classes");
                                                                //Toast.makeText(getApplicationContext(), Integer.toString(no), Toast.LENGTH_LONG).show();
                                                                childreff.setValue(i);*/
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                        Toast.makeText(getApplicationContext(),databaseError.getMessage().toString(),Toast.LENGTH_LONG).show();
                                                    }
                                                });




                                            //Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();


                                            /* Thread thread=new Thread(){
                                                 @Override
                                                 public void run() {
                                                     mrootref = new Firebase(url);
                                                     Firebase childref = mrootref.child(sub).child(date);
                                                     childref.setValue("absent");
                                                 }
                                             };
                                             thread.start();*/




                                         }


                                     }
                                    /*query1 =reference.child("students").orderByChild("branch_section").equalTo(branch+"_"+section);
                                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()){
                                                for (DataSnapshot students : dataSnapshot.getChildren()) {
                                                    if(students.child(sub).child(date).getValue().equals("present")){
                                                        present++;
                                                    }

                                                }
                                            }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Toast.makeText(getApplicationContext(),"Something went wrong ,Please try again",Toast.LENGTH_LONG).show();
                                            mprogess.hide();

                                        }
                                    });*/




                                    mprogess.dismiss();
                                    AlertDialog alertDialog = new AlertDialog.Builder(
                                            QR_code_generated.this).create();

                                    // Setting Dialog Title
                                    alertDialog.setTitle("Attendence details");

                                    // Setting Dialog Message
                                    alertDialog.setMessage("Number of absent student : "+absent);

                                    // Setting Icon to Dialog
                                    //alertDialog.setIcon(R.drawable.tick);

                                    // Setting OK Button
                                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent in =new Intent(QR_code_generated.this,qr_details.class);
                                            startActivity(in);
                                            // Write your code here to execute after dialog closed
                                            // Toast.makeText(context, "You clicked on OK", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    // Showing Alert Message
                                    alertDialog.show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(),"Something went wrong ,Please try again",Toast.LENGTH_LONG).show();
                                mprogess.hide();
                            }
                        });


                    }

                }, 10000);

            }
        });



    }


    public static int generateRandomNumber() {
        int randomNumber;

        SecureRandom secureRandom = new SecureRandom();
        String s = "";
        for (int i = 0; i < 4; i++) {
            int number = secureRandom.nextInt(9);
            if (number == 0 && i == 0) { // to prevent the Zero to be the first number as then it will reduce the length of generated pin to three or even more if the second or third number came as zeros
                i = -1;
                continue;
            }
            s = s + number;
        }

        randomNumber = Integer.parseInt(s);

        return randomNumber;
    }


    /*class PrimeThread extends Thread {
        String branch, section, date;

        PrimeThread(String branch, String section, String date) {
            this.branch = branch;
            this.section = section;
            this.date = date;
        }

        public void run() {
            Toast.makeText(getApplicationContext(),"found",Toast.LENGTH_LONG).show();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

            Query query = reference.child("students").orderByChild("branch_section").equalTo(branch + "_" + section);
            Toast.makeText(getApplicationContext(),"found it",Toast.LENGTH_LONG).show();
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Toast.makeText(getApplicationContext(),"found",Toast.LENGTH_LONG).show();
                        mprogess.dismiss();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }*/
}

