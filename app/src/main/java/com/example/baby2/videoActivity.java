package com.example.baby2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

public class videoActivity extends AppCompatActivity {


    VideoView vv;
    MediaController mediaController;
    LinearLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        mediaController = new MediaController(this);

        layout=findViewById(R.id.video);

        // 세로로만 실행
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        vv=(VideoView)findViewById(R.id.videoView);

        vv.setMediaController(mediaController);
        String uriPath="android.resource://"+getPackageName()+"/raw/babyv";
        Uri uri=Uri.parse(uriPath);
        vv.setVideoURI(uri);
//        vv.requestFocus();
//        vv.start();

        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // 준비 완료되면 비디오 재생
                mp.start();
            }
        });

        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                PreferenceManager.setInt(getApplicationContext(),"video",0);
                finish();
            }
        });

    }
}