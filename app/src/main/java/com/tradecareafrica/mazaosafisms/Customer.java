package com.tradecareafrica.mazaosafisms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.SEND_SMS;
import static com.tradecareafrica.mazaosafisms.SplashActivity.MY_PERMISSION_INTERNET;
import static com.tradecareafrica.mazaosafisms.SplashActivity.MY_PERMISSION_SEND_SMS;

public class Customer extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    final Context context = this;
    Button reset;
    Button send;
    EditText message;
    SweetAlertDialog pDialog;
    SweetAlertDialog sDialog;
    JSONObject jsonObject;
    JSONParser jsonParser=new JSONParser();
    String serverResponse;
    SharedPreferences ReceiverPrefs;
    String type;
    //public static final String TAG_RESPONSE_CODE="status";
    public static final String TAG_RESPONSE_CODE="responseCode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        //check internet connection
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(!isConnected){
            Toast.makeText(context, "No network connection", Toast.LENGTH_LONG).show();
        }

        //check for storage permission
        if(!(mayRequestSMS() && mayRequestInternet())){
            return;
        }
        //if permission has already been granted

        reset = findViewById(R.id.resetbtn);
        send = findViewById(R.id.sendbtn);
        message = findViewById(R.id.customermessage);
        Spinner spinner = (Spinner) findViewById(R.id.receiver_spinner);
        spinner.setOnItemSelectedListener(this);
        String[] Types = new String[]{
                "All",
                "Trader",
                "Consumer"
        };
        ArrayAdapter adapter = new ArrayAdapter(getBaseContext(),android.R.layout.simple_spinner_item, Types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message.getText().clear();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check internet connection
                ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

                assert cm != null;
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                if(!isConnected){
                    Toast.makeText(context, "No network connection", Toast.LENGTH_LONG).show();
                }

                new RequestSending().execute();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        type = parent.getItemAtPosition(position).toString();
        ReceiverPrefs = getSharedPreferences("com.tradecareafrica.mazaosafisms", MODE_PRIVATE);
        SharedPreferences.Editor editor = ReceiverPrefs.edit();
        editor.putString("type", type);
        editor.apply();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public class RequestSending extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new SweetAlertDialog(Customer.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.setTitleText("Sending please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        /**
         * getting All products from url
         */
        protected String doInBackground(String... args) {
            String sms = message.getText().toString();
            String customertype = type;


            List<NameValuePair> Details = new ArrayList<NameValuePair>();
            Details.add(new BasicNameValuePair("message", sms));
            Details.add(new BasicNameValuePair("type", type));

            jsonObject = jsonParser.makeHttpRequest1(Utility.url_sendcustomersms, "POST", Details);
            Log.e("##requests", ""+jsonObject);

            try {
                serverResponse = jsonObject.getString(TAG_RESPONSE_CODE);
                Log.e("##Response: ", "Server response: " + serverResponse);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismissWithAnimation();

            if (jsonObject == null) {

                Toast.makeText(Customer.this, "Server Error please try again later", Toast.LENGTH_SHORT).show();

            } else {

                if (serverResponse.equals("200")) {

                    sDialog = new SweetAlertDialog(Customer.this, SweetAlertDialog.SUCCESS_TYPE);
                    sDialog.setTitleText("SUCCESS");
                    sDialog.setContentText("Message sent.");
                    sDialog.setConfirmText("OK");
                    sDialog.setCancelable(true);
                    sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sDialog.dismiss();

                        }
                    });
                    sDialog.show();
                } else {

                    sDialog = new SweetAlertDialog(Customer.this, SweetAlertDialog.ERROR_TYPE);
                    sDialog.setTitleText("Oops");
                    sDialog.setContentText("Message not sent.");
                    sDialog.setConfirmText("OK");
                    sDialog.setCancelable(true);
                    sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sDialog.dismiss();
                        }
                    });
                    sDialog.show();

                }

            }
        }
    }

    private boolean mayRequestSMS() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }else {
            requestPermissions(new String[]{SEND_SMS}, MY_PERMISSION_SEND_SMS);
        }
        return false;
    }

    private boolean mayRequestInternet() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(INTERNET) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }else {
            requestPermissions(new String[]{INTERNET}, MY_PERMISSION_INTERNET);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_SEND_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted,
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                } else {
                    // permission denied, boo!
                    finish();
                }
            }
            case MY_PERMISSION_INTERNET: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted,
                } else {
                    // permission denied, boo!
                    finish();
                }
            }
        }
    }


}
