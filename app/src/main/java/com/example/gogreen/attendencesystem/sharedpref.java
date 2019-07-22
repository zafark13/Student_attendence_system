package com.example.gogreen.attendencesystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class sharedpref {

    private static sharedpref yourPreference;
    private SharedPreferences sharedPreferences;

    public static sharedpref getInstance(Context context) {
        if (yourPreference == null) {
            yourPreference = new sharedpref(context);
        }
        return yourPreference;
    }

    private sharedpref(Context context) {
        sharedPreferences = context.getSharedPreferences("YourCustomNamedPreference",Context.MODE_PRIVATE);
    }

    public void storeS(String name,String previous,String branch_section) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor .putString("nameS",name);
        prefsEditor.putString("branch_section",branch_section);
        prefsEditor.putString("previous",previous);
        prefsEditor.commit();
    }
    public void storeF(String name,String proctor,String id,String previous) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString("previous",previous);
        prefsEditor .putString("nameF",name);
        prefsEditor .putString("proctor",proctor);
        prefsEditor .putString("id",id);
        prefsEditor.commit();
    }

    public String getdata(String key ) {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(key, "not ");
        }
        return "not found";
    }

    public void packagesharedPreferences(ArrayList<String> arrPackage) {
        SharedPreferences.Editor prefseditor = sharedPreferences.edit();
        Set<String> set = new HashSet<String>();
        set.addAll(arrPackage);
        prefseditor.putStringSet("DATE_LIST", set);
        prefseditor.apply();
        //Log.d("storesharedPreferences",""+set);
    }

    public Set<String> retriveSharedValue() {
        //SharedPreferences.Editor prefseditor = sharedPreferences.edit();
        Set<String> set = sharedPreferences.getStringSet("DATE_LIST", null);
        return set;
        //arrPackage.addAll(set);
        //Log.d("retrivesharedPreferences",""+set);
    }

}