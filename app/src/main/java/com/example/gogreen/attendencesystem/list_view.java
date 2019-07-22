package com.example.gogreen.attendencesystem;

import android.media.session.PlaybackState;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class list_view extends AppCompatActivity{
    static TextView textView1;
    String[] dates,state;
    int i=0;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        Bundle bundle=getIntent().getExtras();
        dates=bundle.getStringArray("dates");
        state=bundle.getStringArray("state");
        i=bundle.getInt("no");

        Toast.makeText(getApplicationContext(),Integer.toString(i)+dates[0]+state[0],Toast.LENGTH_LONG).show();

        listView=(ListView)findViewById(R.id.list);
        Baseadapter baseadapter=new Baseadapter();
        listView.setAdapter(baseadapter);








    }


    class Baseadapter extends BaseAdapter{

        @Override
        public int getCount() {
            return i;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
            convertView=getLayoutInflater().inflate(R.layout.listview,null);
            TextView tv1=(TextView)convertView.findViewById(R.id.date);
            TextView tv2=(TextView)convertView.findViewById(R.id.textView18);
            tv1.setText(dates[position].substring(0,2)+"/"+dates[position].substring(2,4)+"/"+dates[position].substring(4,8));
            //    tv1.setText(dates[position]);
                if(state[position].equals("present")){
                    tv2.setTextColor(getResources().getColor(R.color.Green));
                }
                if(state[position].equals("absent")){
                    tv2.setTextColor(getResources().getColor(R.color.RED));
                }
            tv2.setText(state[position]);

            return convertView;
        }
    }
}



