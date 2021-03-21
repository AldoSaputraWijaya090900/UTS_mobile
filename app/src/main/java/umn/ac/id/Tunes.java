package umn.ac.id;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.content.Intent;
import android.os.Environment;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class Tunes extends AppCompatActivity {
    ListView listView;
    String[] items;
    /*private Button btnProfile;*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu_profile: {
                Intent intent = new Intent(Tunes.this, ProfilDua.class);
                startActivity(intent);
                return true;
            }

            case R.id.action_menu_logout: {
                Intent logoutIntent = new Intent(Tunes.this,
                        AwalActivity.class);
                startActivity(logoutIntent);
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tunes);
        listView = (ListView) findViewById(R.id.listView);
        /*btnProfile = findViewById(R.id.button_profile);*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        display();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
        Login();
        /*btnProfile.setOnClickListener(v -> startActivity(new Intent(Tunes.this, ProfilDua.class)));*/
    }

    public ArrayList<File> findSong(File root) {
        ArrayList<File> at = new ArrayList<File>();
        File[] files = root.listFiles();
        for (File singleFile : files) {
            if (singleFile.isDirectory() && !singleFile.isHidden()) {
                at.addAll(findSong(singleFile));
            } else {
                if (singleFile.getName().endsWith(".mp3") ||
                        singleFile.getName().endsWith(".wav")) {
                    at.add(singleFile);
                }
            }
        }
        return at;
    }

    void display() {
        final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());
        items = new String[mySongs.size()];
        for (int i = 0; i < mySongs.size(); i++) {
            //toast(mySongs.get(i).getName().toString());
            items[i] = mySongs.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");
        }
        ArrayAdapter<String> adp = new
                ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adp);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int
                    position, long l) {

                String songName = listView.getItemAtPosition(position).toString();
                startActivity(new Intent(getApplicationContext(), PlayerActivity.class)

                        .putExtra("pos", position).putExtra("songs", mySongs).putExtra("songname", songName));
            }
        });
    }
    public void Login() {
        DialogueActivity dialogueactivity = new DialogueActivity();
        dialogueactivity.show(getSupportFragmentManager(), "dialogue activity");
    }

}
