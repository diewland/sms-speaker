package com.diewland.android.notispeaker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

// http://www.worldbestlearningcenter.com/tips/Android-receive-sms-programmatically.htm
public class SMSBReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        // Get Bundle object contained in the SMS intent passed in
        Bundle bundle = intent.getExtras();
        SmsMessage[] smsm = null;
        String sms_sender  = "";
        String sms_message = "";
        String sms_time    = "";
        if (bundle != null)
        {
            // Get the SMS message
            Object[] pdus = (Object[]) bundle.get("pdus");
            smsm = new SmsMessage[pdus.length];
            for (int i=0; i<smsm.length; i++){
                smsm[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                sms_sender  += smsm[i].getOriginatingAddress();
                sms_message += smsm[i].getMessageBody().toString();

                Date date = new Date(smsm[i].getTimestampMillis());
                DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                String dateFormatted = formatter.format(date);
                sms_time += dateFormatted;
            }

            // Start Application's  MainActivty activity
            Intent smsIntent = new Intent(context,MainActivity.class);
            smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            smsIntent.putExtra("sms_sender", sms_sender);
            smsIntent.putExtra("sms_message", sms_message);
            smsIntent.putExtra("sms_time", sms_time);
            context.startActivity(smsIntent);
        }
    }
}
