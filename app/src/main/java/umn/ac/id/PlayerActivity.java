package umn.ac.id;

import android.annotation.SuppressLint;
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

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    static MediaPlayer mp;
    int position;
    SeekBar sb;
    ArrayList<File> mySongs;
    Thread updateSeekBar;
    Button pause, next, previous;
    TextView songNameText;

    String sname;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_player_ui);

        songNameText = (TextView) findViewById(R.id.txtSongLabel);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Now Playing");

        pause = (Button) findViewById(R.id.pause);

        previous = (Button) findViewById(R.id.previous);
        next = (Button) findViewById(R.id.next);

        sb = (SeekBar) findViewById(R.id.seekBar);


        updateSeekBar = new Thread() {
            @Override
            public void run() {
                int totalDuration = mp.getDuration();
                int currentPosition = 0;
                while (currentPosition < totalDuration) {
                    try {
                        sleep(500);
                        currentPosition = mp.getCurrentPosition();
                        sb.setProgress(currentPosition);
                    } catch (InterruptedException e) {

                    }
                }
            }
        };

        if (mp != null) {
            mp.stop();
            mp.release();
        }
        Intent i = getIntent();
        Bundle b = i.getExtras();


        mySongs = (ArrayList) b.getParcelableArrayList("songs");

        sname = mySongs.get(position).getName().toString();

        String SongName = i.getStringExtra("songname");
        songNameText.setText(SongName);
        songNameText.setSelected(true);

        position = b.getInt("pos", 0);
        Uri u = Uri.parse(mySongs.get(position).toString());

        mp = MediaPlayer.create(getApplicationContext(), u);
        mp.start();
        sb.setMax(mp.getDuration());
        updateSeekBar.start();
        sb.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        sb.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        sb.setOnSeekBarChangeListener(new
                                              SeekBar.OnSeekBarChangeListener() {
                                                  @Override
                                                  public void onProgressChanged(SeekBar seekBar, int i,
                                                                                boolean b) {
                                                  }

                                                  @Override
                                                  public void onStartTrackingTouch(SeekBar seekBar) {
                                                  }

                                                  @Override
                                                  public void onStopTrackingTouch(SeekBar seekBar) {
                                                      mp.seekTo(seekBar.getProgress());

                                                  }
                                              });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.setMax(mp.getDuration());
                if (mp.isPlaying()) {
                    pause.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
                    mp.pause();

                } else {
                    pause.setBackgroundResource(R.drawable.pause);
                    mp.start();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                mp.release();
                position = ((position + 1) % mySongs.size());
                Uri u = Uri.parse(mySongs.get(position).toString());
                // songNameText.setText(getSongName);
                mp = MediaPlayer.create(getApplicationContext(), u);

                sname = mySongs.get(position).getName().toString();
                songNameText.setText(sname);

                try {
                    mp.start();
                } catch (Exception e) {
                }

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (position < mySongs.size() -1) {
                    position++;
                } else {
                    position = 0;
                }
                initializeMusicPlayer(position);
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position <= 0) {
                    position = mySongs.size();
                } else {
                    position--;
                }

                initializeMusicPlayer(position);
            }
        });


    }

    private void initializeMusicPlayer(int position) {

        // if mediaplayer is not null and playing reset it at the launch of activity

        if (mp != null && mp.isPlaying()) {
            mp.reset();
        }

        // getting out the song name
        String name = mySongs.get(position).getName();
        songNameText.setText(name);

        // accessing the songs on storage

        Uri uri = Uri.parse(mySongs.get(position).toString());

        // creating a mediaplayer
        // passing the uri

        mp = MediaPlayer.create(this, uri);

        // SETTING ON PREPARED MEDIAPLAYER

        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                // seekbar
                sb.setMax(mp.getDuration());

                // while mediaplayer is playing the play button should display pause
                pause.setBackgroundResource(R.drawable.pause);
                // start the mediaplayer
                mp.start();
            }
        });
    }
}