package com.diewland.android.notispeaker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    private String list_text;

    public static final String PREFERENCES_TODO = "TODO_List_Shared_Preferences";
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrayList = new ArrayList<String>();
        list = (ListView) findViewById(R.id.list);
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.mylist, arrayList);
        list.setAdapter(adapter);

        // Get intent object sent from the SMSBroadcastReceiver
        Intent sms_intent=getIntent();
        Bundle b=sms_intent.getExtras();
        if(b!=null){
            String s = b.getString("sms_sender");
            String m = b.getString("sms_message");
            String t = b.getString("sms_time");

            list_text = t + " " + m;

            list.post(new Runnable() {
                @Override
                public void run() {
                    arrayList.add(0, list_text);
                    adapter.notifyDataSetChanged();
                    list.smoothScrollToPosition(0);
                }
            });

            speak(m);
        }

        // keep listview state
        prefs =  getSharedPreferences(PREFERENCES_TODO, MODE_PRIVATE);
        editor = prefs.edit();
    }

    private void speak(String text){
        MyTTS.getInstance(getApplicationContext())
             .setLocale(new Locale("th"))
             .speak(text);
    }

    @Override
    protected void onPause() {
        editor.clear();
        for (int i = 0; i < adapter.getCount(); ++i){
            editor.putString(String.valueOf(i), adapter.getItem(i));
        }
        editor.commit();

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        for (int i = 0;; ++i){
            final String str = prefs.getString(i+"", "");
            if (!str.equals("")){
                adapter.add(str);
            } else {
                break; // Empty String means the default value was returned.
            }
        }
    }
}
