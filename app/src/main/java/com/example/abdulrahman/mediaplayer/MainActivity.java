package com.example.abdulrahman.mediaplayer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
SeekBar seekBar;
MediaPlayer md;
ArrayList <SoundInfo>sound;
ListView listView;
int SeekValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekBar=(SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                SeekValue=i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                md.seekTo(SeekValue);

            }
        });
        sound=new ArrayList<>();
//        getAll();
        listView=(ListView) findViewById(R.id.sou);
          Check();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              //  Toast.makeText(getApplicationContext(),"NAME "+sound.get(i).Soung_name,Toast.LENGTH_LONG).show();
                SoundInfo soundInfo=sound.get(i);
                md=new MediaPlayer();

                try {
                    md.setDataSource(soundInfo.path);
                    md.prepare();
                    md.start();

                    seekBar.setMax(md.getDuration());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        myThrad thrad=new myThrad();
        thrad.start();

    }


    final int STR=21;
    public void Check(){
        if(Build.VERSION.SDK_INT>=23){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(new  String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE
                },STR);
            }
        }
        Load();
    }


    public  ArrayList<SoundInfo> getAll()
    //onlin connect media
     /*
    {
sound.clear();
sound.add(new SoundInfo("http://server6.mp3quran.net/thubti/001.mp3","001","quran","1"));
sound.add(new SoundInfo("http://server6.mp3quran.net/3siri/003.mp3","003","quran","3"));
sound.add(new SoundInfo("http://www.mp3quran.net/newMedia.php?id=1&file=http://server10.mp3quran.net/IbrahemSadan/001.mp3","004","quran","4"));
//sound.add(new SoundInfo("http://server6.mp3quran.net/3siri/001.mp3","001","quran","1"));

        /*sound.clear();

        sound.add(new SoundInfo("http://server6.mp3quran.net/thubti/001.mp3","Fataha","bakar","quran"));

        sound.add(new SoundInfo("http://server6.mp3quran.net/thubti/002.mp3","Bakara","bakar","quran"));

        sound.add(new SoundInfo("http://server6.mp3quran.net/thubti/003.mp3","Al-Imran","bakar","quran"));

        sound.add(new SoundInfo("http://server6.mp3quran.net/thubti/004.mp3","An-Nisa'","bakar","quran"));

        sound.add(new SoundInfo("http://server6.mp3quran.net/thubti/005.mp3","Al-Ma'idah","bakar","quran"));

        sound.add(new SoundInfo("http://server6.mp3quran.net/thubti/006.mp3","Al-An'am","bakar","quran"));

        sound.add(new SoundInfo("http://server6.mp3quran.net/thubti/007.mp3","Al-A'raf","bakar","quran"));

return sound;
    }
    */
    // local media
    {
       /* Uri allsongsuri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection=MediaStore.Audio.Media.IS_MUSIC + " != 0";
        Cursor cursor= getContentResolver().query(allsongsuri, null, selection, null, null);
        if (cursor !=null){
            if (cursor.moveToFirst()){
                do {
                    String path=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    String name_so=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String alboum=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    String art=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    sound.add(new SoundInfo(path,name_so,alboum,art));
                }while (cursor.moveToNext());
            }
            cursor.close();
        }
        return sound;
        */
        Uri allsongsuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";



        Cursor   cursor = getContentResolver().query(allsongsuri, null, selection, null, null);



        if (cursor != null) {

            if (cursor.moveToFirst()) {

                do {

                    String    song_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));

                    String fullpath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

                    String    album_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));

                    String   artist_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));

                    sound.add(new SoundInfo(fullpath,song_name,album_name,album_name));



                } while (cursor.moveToNext());



            }

            cursor.close();



        }



        return sound;
    }
    public void butStart(View view) {
        md.start();
    }

    public void butStop(View view) {
        md.stop();
    }

    public void butPush(View view) {
        md.pause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case STR:
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Load();
                }
                else {
                    Toast.makeText(getApplicationContext(),"do not allow ",Toast.LENGTH_LONG).show();

                }
                break;
                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void Load(){
        ListAd ad=new ListAd(getAll());
        listView.setAdapter(ad);
    }
    public class myThrad extends Thread{
        @Override
        public void run() {
          while (true){

              runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                      //seekBar.setProgress();
                      if(md!=null)
                      seekBar.setProgress(md.getCurrentPosition());
                  }
              });
              try {
                  Thread.sleep(1000);
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
          }
        }
    }

    public class ListAd extends BaseAdapter{
ArrayList<SoundInfo> arrayList;

        public ListAd(ArrayList<SoundInfo> arrayList) {
            this.arrayList = arrayList;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater layoutInflater=getLayoutInflater();
            View view1=layoutInflater.inflate(R.layout.list_s,null);
            TextView name_so=(TextView) view1.findViewById(R.id.textView);
            TextView albom=(TextView) view1.findViewById(R.id.textView2);
            SoundInfo soundInfo=arrayList.get(i);
            name_so.setText(soundInfo.Soung_name);
            albom.setText(soundInfo.alboum_name);
            return view1;
        }
    }
}
