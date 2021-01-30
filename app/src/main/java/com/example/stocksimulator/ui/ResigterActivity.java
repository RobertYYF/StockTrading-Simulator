package com.example.stocksimulator.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.stocksimulator.R;
import com.example.stocksimulator.handler.ConnectionHandler;

import java.io.IOException;

public class ResigterActivity extends AppCompatActivity {

    ImageButton returnButton;
    Button submit;
    EditText usernameField;
    EditText passwordField;
    EditText phoneField;
    EditText emailField;
    EditText balanceField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resigter);
        init();
    }

    private void init() {
        submit = findViewById(R.id.submit);
        returnButton = findViewById(R.id.returnButton);
        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
        phoneField = findViewById(R.id.phone);
        emailField = findViewById(R.id.email);
        balanceField = findViewById(R.id.initialCash);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResigterActivity.this, LoginActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = usernameField.getText().toString();
                String phoneNum = phoneField.getText().toString();
                String password = passwordField.getText().toString();
                String email = emailField.getText().toString();
                String balance = balanceField.getText().toString();

            RegisterProcess registerProcess = new RegisterProcess();
            registerProcess.execute(name, password, phoneNum, email, balance);

            }
        });

    }

    private final class RegisterProcess extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... args) {

            String name = args[0];
            String pwd = args[1];
            String phone = args[2];
            String email = args[3];
            String balance = args[4];

            ConnectionHandler client = new ConnectionHandler();

            try {
                client.startConnection("10.0.2.2", 6868);
                String resp = client.sendMessage("register " + name + " " + pwd + " " + phone + " " + email + " " + balance);
                return resp;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            System.out.println(result);

        }
    }

}