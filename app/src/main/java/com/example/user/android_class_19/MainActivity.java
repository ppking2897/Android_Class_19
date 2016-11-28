package com.example.user.android_class_19;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private String stringpath;
    private MediaPlayer mp;
    private MediaRecorder mr;
    private File sdroot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                 Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                 Manifest.permission.RECORD_AUDIO},
                    123);
        }
        sdroot = Environment.getExternalStorageDirectory();
    }

    public void intent(View v){
        Intent intent =
                new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        startActivityForResult(intent, 1);
    }

    public void start(View v) {
        if(stringpath ==null){return;}

        mp = new MediaPlayer();
        try {
            mp.setDataSource(stringpath);
            mp.prepare();
            mp.start();
        } catch (IOException e) {
            Log.v("ppking", e.toString());
        }
    }
    public void recording(View v){
        mr = new MediaRecorder();
        mr.setAudioSource(MediaRecorder.AudioSource.MIC);
        mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mr.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mr.setOutputFile(sdroot.getAbsolutePath() + "/ppking.3gp");

        try {
            mr.prepare();
            mr.start();
        }catch (Exception ee){
            Log.v("brad", ee.toString());
        }
    }

    public void play(View v){
        try {
            mp = new MediaPlayer();
            mp.setDataSource(sdroot.getAbsolutePath() + "/ppking.3gp");
            mp.prepare();
            mp.start();
        }catch(Exception e){
            Log.v("ppking", e.toString());
        }
    }
    public void stop(View v){
        if (mr!=null){
            mr.stop();
            mr.release();
            mr = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK){
            Uri uri = data.getData();
            //Log.v("ppking",getRealPathFromURI(uri));
            stringpath = getRealPathFromURI(uri);
        }
    }
    public String getRealPathFromURI(Uri contentUri) {
        ContentResolver resolver = getContentResolver();
        String[] proj = { MediaStore.Audio.Media.DATA };
        Cursor cursor = resolver.query(contentUri, proj, null,
                null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}
