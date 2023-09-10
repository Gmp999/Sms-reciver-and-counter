package com.example.smsreplied;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.provider.Telephony.Sms;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
public class MySMSReceiver extends BroadcastReceiver {
 private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
 private static final String TAG = "SmsBroadcastReceiver";
 @Override
 public void onReceive(Context context, Intent intent) {
 if(intent.getAction() == SMS_RECEIVED)
 {
 Bundle bundle = intent.getExtras();
 SmsMessage[] smsm = null;
 String sms_str ="";
 if(bundle != null)
 {
 Object[] pdus = (Object[]) bundle.get("pdus");
 smsm = new SmsMessage[pdus.length];
 String senderNumber = null;
 String senderMessage = null;
 for(int i = 0; i < pdus.length; i++) {
 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
 String format = bundle.getString("format");
 smsm[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
 } else {
 smsm[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
 }
 Bundle b1 = new Bundle();
 b1.putString("number", smsm[i].getOriginatingAddress());
 b1.putString("content", smsm[i].getMessageBody());
 Intent smsIntent = new Intent(context, MainActivity.class);
 smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
 smsIntent.putExtra("data", b1);
 context.startActivity(smsIntent);
 }
 Toast.makeText(context,sms_str ,Toast.LENGTH_LONG).show();
 }
 }
 }
}
