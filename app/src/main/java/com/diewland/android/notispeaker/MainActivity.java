package com.diewland.android.notispeaker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TableLayout tab;
    private EditText text;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //speakTH("คุณมี 1 ข้อความใหม่");
        //speakEN("You got new message");

        tab = (TableLayout)findViewById(R.id.tab);
        text = (EditText)findViewById(R.id.text);
        send = (Button)findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNotification(text.getText().toString());
            }
        });
    }

    // https://alvinalexander.com/android/how-to-create-android-notifications-notificationmanager-examples
    public void showNotification(String s){
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        Notification notification = new NotificationCompat.Builder(this)
            .setTicker("Demo")
            .setSmallIcon(android.R.drawable.ic_menu_help)
            .setContentTitle("Demo Notification")
            .setContentText(s)
            .setContentIntent(pi)
            .setAutoCancel(true)
            .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    private void speakTH(String text){
        MyTTS.getInstance(getApplicationContext())
             .setLocale(new Locale("th"))
             .speak(text);
    }

    private void speakEN(String text){
        MyTTS.getInstance(getApplicationContext())
                .setLocale(Locale.ENGLISH)
                .speak(text);
    }
}
