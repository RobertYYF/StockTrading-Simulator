package com.example.stocksimulator.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.stocksimulator.R;
import com.example.stocksimulator.handler.ConnectionHandler;
import com.example.stocksimulator.model.Port;
import com.example.stocksimulator.model.User;
import com.example.stocksimulator.threads.LoginThread;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LoginActivity extends AppCompatActivity {

    Button register;
    Button login;
    EditText username;
    EditText password;
    String loginStatus = "false";
    VideoView videoView;
    private final BlockingQueue queue = new LinkedBlockingQueue<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        register = findViewById(R.id.registerButton);
        login = findViewById(R.id.loginButton);
        username = findViewById(R.id.loginUsername);
        password = findViewById(R.id.loginPassword);
        videoView = findViewById(R.id.videoView2);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ResigterActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (login()) {

                    Toast.makeText(v.getContext(), "Login Success", Toast.LENGTH_SHORT).show();
                    System.out.println("switching");
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    v.getContext().startActivity(intent);


                } else {
                    Toast.makeText(v.getContext(), "Wrong username or password", Toast.LENGTH_SHORT).show();
                }

            }

        });

        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.loginbackground);
        videoView.setVideoURI(video);
        videoView.setOnPreparedListener (new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

        videoView.start();

    }

    private boolean login() {

        if (username.getText().toString().isEmpty()) {
            Toast.makeText(this, "username cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.getText().toString().isEmpty()) {
            Toast.makeText(this, "password cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else {

            String user = username.getText().toString();
            String pwd = password.getText().toString();

            Thread t2 = new Thread(new LoginThread(queue, user, pwd));
            t2.start();

            try {
                List<String> userdata = (List<String>) queue.take();

                String[] data = userdata.get(0).split(" ");

                if (data[0].equals("failed"))
                    return false;

                User.getInstance().setName(data[0]);
                User.getInstance().setEmail(data[1]);
                User.getInstance().setPhone(data[2]);
                User.getInstance().setBalance(data[3]);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return true;
        }

    }

}