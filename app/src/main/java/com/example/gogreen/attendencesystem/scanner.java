package com.example.gogreen.attendencesystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class scanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView scannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView=new ZXingScannerView(this);
        setContentView(scannerView);
        if(!check_connectivity.isInternetAvailable(scanner.this)) {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    scanner.this).create();

            // Setting Dialog Title
            alertDialog.setTitle("No Internet Connection");

            // Setting Dialog Message
            alertDialog.setMessage("please check internet connection");

            // Setting Icon to Dialog
            //alertDialog.setIcon(R.drawable.tick);

            // Setting OK Button
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent i =new Intent(scanner.this,loged_in.class);
                    startActivity(i);
                    // Write your code here to execute after dialog closed
                    // Toast.makeText(context, "You clicked on OK", Toast.LENGTH_SHORT).show();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(getApplicationContext(),loged_in.class);
        sharedpref yourPrefrence = sharedpref.getInstance(getApplicationContext());
        intent.putExtra("name",yourPrefrence.getdata("nameS"));
        startActivity(intent);
    }

    @Override
    public void handleResult(Result result) {
        String res =result.getText().toString();
        String[] r = res.split(",");
        //Toast.makeText(getApplicationContext(),r[1],Toast.LENGTH_LONG).show();
        Intent i =new Intent(this,OTP.class);
        i.putExtra("subject",r[0]);
        i.putExtra("date",r[1]);
        startActivity(i);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(check_connectivity.isInternetAvailable(scanner.this)) {
            scannerView.stopCamera();
        }
        else {
            check_connectivity.dialog(scanner.this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(check_connectivity.isInternetAvailable(scanner.this)) {
            scannerView.setResultHandler(this);
            scannerView.startCamera();
        }
        else {
            check_connectivity.dialog(scanner.this);
        }
    }
}
