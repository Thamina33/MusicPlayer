package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    Button nextBtn, previousBtn,pauseBtn;
    TextView songlabel;
    SeekBar songSeekBar;
    String sName;

    static MediaPlayer myMediaPlayer;
    int position;

    ArrayList<File> mySongs;
    Thread updateSeekbar;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()== android.R.id.home){

            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        nextBtn = findViewById(R.id.nextBtn);
        previousBtn=findViewById(R.id.previousBtn);
        pauseBtn = findViewById(R.id.pause);
        songlabel =findViewById(R.id.songLabel);
        songSeekBar = findViewById(R.id.seekbar);


        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        updateSeekbar = new Thread(){

            @Override
            public void run() {
                super.run();

                int totalDuration = myMediaPlayer.getDuration();
                int currentPosition = 0;

                while (currentPosition<totalDuration){

                    try {
                        sleep(100000001);
                        currentPosition=myMediaPlayer.getCurrentPosition();
                        songSeekBar.setProgress(currentPosition);

                    }
                    catch (InterruptedException e){

                        e.printStackTrace();
                    }
                }

            }
        };


        if(myMediaPlayer !=null){

            myMediaPlayer.stop();
            myMediaPlayer.release();
        }

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        mySongs =(ArrayList) bundle.getParcelableArrayList("songs");
        sName = mySongs.get(position).getName().toString();
        String songName = i.getStringExtra("songname");

        songlabel.setText(songName);
        songlabel.setSelected(true);

        position = bundle.getInt("pos",0);

        Uri u = Uri.parse(mySongs.get(position).toString());

        myMediaPlayer = MediaPlayer.create(getApplicationContext(),u);
        myMediaPlayer.start();

        songSeekBar.setMax(myMediaPlayer.getDuration());

        updateSeekbar.start();

        songSeekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        songSeekBar.getThumb().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                myMediaPlayer.seekTo(seekBar.getProgress());

            }
        });


            pauseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    songSeekBar.setMax(myMediaPlayer.getDuration());

                    if (myMediaPlayer.isPlaying()){
                        pauseBtn.setBackgroundResource(R.drawable.play);
                        myMediaPlayer.pause();
                    }
                    else{
                        pauseBtn.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                        myMediaPlayer.start();

                    }
                }
            });


            nextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myMediaPlayer.stop();
                    myMediaPlayer.release();
                    position = ((position+1)%mySongs.size());

                    Uri u = Uri.parse(mySongs.get(position).toString());
                    myMediaPlayer = MediaPlayer.create(getApplicationContext(),u);

                    sName = mySongs.get(position).getName().toString();
                    songlabel.setText(sName);
                    myMediaPlayer.start();
                }
            });

        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMediaPlayer.stop();
                myMediaPlayer.release();

                position = ((position-1)<0)?(mySongs.size()-1):(position-1);

                Uri u = Uri.parse(mySongs.get(position).toString());
                myMediaPlayer = MediaPlayer.create(getApplicationContext(),u);

                sName = mySongs.get(position).getName().toString();
                songlabel.setText(sName);
                myMediaPlayer.start();
            }
        });




    }
}
