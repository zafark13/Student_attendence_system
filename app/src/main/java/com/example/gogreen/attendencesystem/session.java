package com.example.gogreen.attendencesystem;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class session extends Application{
    public ArrayList<String> subjects=new ArrayList<>();
    int present,absent;
    @Override
    public void onCreate() {
        super.onCreate();




        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        sharedpref yourPrefrence = sharedpref.getInstance(getApplicationContext());
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();





        if (firebaseUser!=null){


            if (!(subjects.size()>=10)){

                String uid =firebaseAuth.getCurrentUser().getUid();
                DatabaseReference databaseReference =    mFirebaseDatabase.getReference().child("students").child(uid).child("attendence");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){
                            for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                                subjects.add(childDataSnapshot.getKey().toString());


                                //Log.v(TAG,""+ childDataSnapshot.getKey()); //displays the key for the node
                                //Log.v(TAG,""+ childDataSnapshot.child(--ENTER THE KEY NAME eg. firstname or email etc.--).getValue());   //gives the value for given keyname
                            }sharedpref yourPrefrence = sharedpref.getInstance(getApplicationContext());
                            yourPrefrence.packagesharedPreferences(subjects);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }


            //Toast.makeText(getApplicationContext(),"user not null",Toast.LENGTH_LONG).show();
            String s=yourPrefrence.getdata("previous");
            //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            if(s.equals("student")){


               /* for (int i = 0; i < subjects.size(); i++) {
                    mFirebaseDatabase.getReference().child("students").child(uid).child("attendence").child(subjects.get(i)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                                    Toast.makeText(getApplicationContext(), childDataSnapshot.getValue().toString(), Toast.LENGTH_LONG).show();
                                    if (childDataSnapshot.exists()) {
                                        if (childDataSnapshot.getValue().toString().equals("present")) {
                                            present++;

                                        }
                                        if (childDataSnapshot.getValue().toString().equals("absent")) {
                                            absent++;
                                        }
                                    }


                                    //Log.v(TAG,""+ childDataSnapshot.getKey()); //displays the key for the node
                                    //Log.v(TAG,""+ childDataSnapshot.child(--ENTER THE KEY NAME eg. firstname or email etc.--).getValue());   //gives the value for given keyname
                                }

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_LONG).show();
                        }
                    });
                }*/


                String name = yourPrefrence.getdata("nameS");
                //Toast.makeText(getApplicationContext(),name,Toast.LENGTH_LONG).show();
                Intent i = new Intent(session.this,loged_in.class);
                //i.putExtra("presen",present);
                //i.putExtra("absent",absent);
                i.addFlags(i.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("name",name);
                startActivity(i);
            }
            if (s.equals("faculty")){
                String name = yourPrefrence.getdata("nameF");
                String proctor = yourPrefrence.getdata("proctor");
                String id = yourPrefrence.getdata("id");
                Intent in = new Intent(session.this,Faculty_logedin.class);
                in.addFlags(in.FLAG_ACTIVITY_NEW_TASK);
                in.putExtra("clas","getfacultydetails");
                in.putExtra("name",name);
                in.putExtra("id",id);
                in.putExtra("proctor",proctor);
                startActivity(in);

            }
        }
    }
}
