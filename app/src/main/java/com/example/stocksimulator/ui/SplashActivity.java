package com.example.stocksimulator.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.VideoView;

import com.example.stocksimulator.R;
import com.example.stocksimulator.handler.ConnectionHandler;
import com.example.stocksimulator.model.MarketInfo;
import com.example.stocksimulator.service.RefreshService;

import java.io.IOException;

public class SplashActivity extends AppCompatActivity {

    VideoView videoView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        videoView = (VideoView) findViewById(R.id.videoView);

//        LaunchPrep launchPrep = new LaunchPrep();
//        launchPrep.execute();

        Intent i = new Intent(this, RefreshService.class);
        Bundle bundle = new Bundle();
        bundle.putString("taskName", "refresh");
        i.putExtras(bundle);
        startService(i);

        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash);
        videoView.setVideoURI(video);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                startNextActivity();
            }
        });

        videoView.start();
    }

    private void startNextActivity() {
        if (isFinishing())
            return;
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }


}