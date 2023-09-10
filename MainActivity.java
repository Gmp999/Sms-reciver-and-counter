package com.example.smsreplied;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
public class MainActivity extends AppCompatActivity {
 private static final int MY_PERMISSION_REQUEST_RECEIVE_SMS = 0;
 TextView txtnumber, txtmessage, txtTimestamp;
 private int smsCount = 0;
 private TextView messageTextView; // Declare messageTextView
 private BroadcastReceiver smsReceiver = new BroadcastReceiver() {
 @Override
 public void onReceive(Context context, Intent intent) {
 if (intent.getAction() != null && 
intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
 Bundle bundle = intent.getExtras();
 if (bundle != null) {
 Object[] pdus = (Object[]) bundle.get("pdus");
 if (pdus != null) {
 for (Object pdu : pdus) {
 SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
 String sender = smsMessage.getDisplayOriginatingAddress();
 String message = smsMessage.getDisplayMessageBody();
 displayMessage(sender, message);
 }
 }
 }
 }
 }
 };
 @Override
 protected void onCreate(Bundle savedInstanceState) {
 super.onCreate(savedInstanceState);
 setContentView(R.layout.activity_main);
 if (ContextCompat.checkSelfPermission(MainActivity.this, 
Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
 if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.RECEIVE_SMS)) {
 // Do not do anything
 } else {
 ActivityCompat.requestPermissions(MainActivity.this, new 
String[]{Manifest.permission.RECEIVE_SMS}, MY_PERMISSION_REQUEST_RECEIVE_SMS);
 }
 }
 // Retrieve the SMS count from SharedPreferences
 SharedPreferences sharedPreferences = getSharedPreferences("SMSCount", 
MODE_PRIVATE);
 smsCount = sharedPreferences.getInt("count", 0);
 Bundle b = getIntent().getBundleExtra("data");
 txtnumber = findViewById(R.id.txtnumber);
 txtmessage = findViewById(R.id.txtmessage);
 txtTimestamp = findViewById(R.id.txtTimestamp);
 messageTextView = findViewById(R.id.messageTextView); // Initialize messageTextView 
with the appropriate view ID from your layout
 if (b != null) {
 String number = b.getString("number");
 String content = b.getString("content");
 txtnumber.setText(number);
 txtmessage.setText(content);
 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", 
Locale.getDefault());
 // Get the current system date and time
 String currentDateTimeString = dateFormat.format(new Date());
 txtTimestamp.setText(currentDateTimeString);
 }
 // Display the SMS count
 String displayText = "SMS Count: " + smsCount;
 messageTextView.setText(displayText);
 // Register the SMS receiver
 IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
 registerReceiver(smsReceiver, filter);
 }
 @Override
 protected void onDestroy() {
 super.onDestroy();
 // Unregister the SMS receiver
 unregisterReceiver(smsReceiver);
 }
 @Override
 public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) 
{
 switch (requestCode) {
 case MY_PERMISSION_REQUEST_RECEIVE_SMS: {
 if (grantResults.length > 0 && grantResults[0] == 
PackageManager.PERMISSION_GRANTED) {
 Toast.makeText(this, "Thank you for permitting", Toast.LENGTH_LONG).show();
 } else {
 Toast.makeText(this, "Can't do anything until you permit me", 
Toast.LENGTH_LONG).show();
 }
 }
 }
 }
 private void displayMessage(String sender, String message) {
 smsCount++;
 String displayText = "From: " + sender + "\n\n" + message + "\n\nSMS Count: " + smsCount;
 messageTextView.setText(displayText);
 // Save the updated SMS count in SharedPreferences
 SharedPreferences sharedPreferences = getSharedPreferences("SMSCount", 
MODE_PRIVATE);
 SharedPreferences.Editor editor = sharedPreferences.edit();
 editor.putInt("count", smsCount);
 editor.apply();
 }
}

