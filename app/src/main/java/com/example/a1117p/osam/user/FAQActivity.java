package com.example.a1117p.osam.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class FAQActivity extends YouTubeBaseActivity {
    private YouTubePlayerView youtubeViewer;
    YouTubePlayer.OnInitializedListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        youtubeViewer = findViewById(R.id.youtubeViewer);

        listener = new YouTubePlayer.OnInitializedListener(){
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                /* 초기화 성공하면 유튜브 동영상 ID를 전달하여 동영상 재생*/
                youTubePlayer.loadVideo("mrAIqeULUL0"); // 동영상 ID는 URL 상단의 마지막 부분이다.
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(FAQActivity.this, "재생 실패", Toast.LENGTH_LONG).show();
            }
        };
        youtubeViewer.initialize(getResources().getString(R.string.youtube), listener);
    }
}
