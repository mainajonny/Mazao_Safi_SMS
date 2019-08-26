package com.tradecareafrica.mazaosafisms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button reset;
    Button send;
    EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reset = findViewById(R.id.resetbtn);
        send = findViewById(R.id.sendbtn);
        message = findViewById(R.id.etmessage);

        String sms = message.getText().toString();

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message.getText().clear();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
