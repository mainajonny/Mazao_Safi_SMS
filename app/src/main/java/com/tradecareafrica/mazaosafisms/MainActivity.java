package com.tradecareafrica.mazaosafisms;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.Manifest.permission.SEND_SMS;
import static com.tradecareafrica.mazaosafisms.SplashActivity.MY_PERMISSION_SEND_SMS;

public class MainActivity extends AppCompatActivity {
    Button reset;
    Button send;
    EditText message;
    EditText number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //check for storage permission
        if (ContextCompat.checkSelfPermission(this, SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{SEND_SMS}, MY_PERMISSION_SEND_SMS);
        }
        //if permission has already been granted
        else {

            reset = findViewById(R.id.resetbtn);
            send = findViewById(R.id.sendbtn);
            message = findViewById(R.id.etmessage);
            number = findViewById(R.id.etnumber);

            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    message.getText().clear();
                }
            });

            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(MainActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                    sendSMS();
                }
            });
        }

    }

    protected void sendSMS(){
        try {

            String sms = message.getText().toString();
            String phn = number.getText().toString();

            /*Intent send = new Intent(Intent.ACTION_VIEW);
            send.setData(Uri.parse("smsto:"));
            send.setType("vnd.android-dir/mms-sms");
            send.putExtra("address", new String(phn));
            send.putExtra("sms_body", new String(sms));
            startActivity(send);*/

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phn, null, sms, null, null);

            Toast.makeText(MainActivity.this, "SMS sent!", Toast.LENGTH_SHORT).show();
            finish();

            Log.e("Finished sending SMS...", "");
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "SMS failed, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_SEND_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted,
                    Intent i =new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                } else {
                    // permission denied, boo!
                    finish();
                }
            }
        }
    }


}
