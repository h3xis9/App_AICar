package com.example.h3xis9.app_aicar;

import android.os.Build;
import android.os.Handler;
import android.os.Bundle;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import android.widget.ImageView;
import android.widget.Toast;

import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.hanks.htextview.base.HTextView;



public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ImageView voiceRecBtn;

    boolean flag = false;
    View.OnTouchListener mOnTouchListener;

    int index;
    private HTextView textView1;
    private HTextView textView2;

    String[] sentences = {
            "どこに行きますか？",
            "「新宿駅」ですね。",
            "では、薬局に寄る経路で案内します。"
    };

    String[] sentences2 = {
            "",
            "よく行く薬局を通るようですが、寄りますか？",
            "最終目的地はこちらでよろしいでしょうか？",
    };


    MediaPlayer mpChime;
    MediaPlayer mpDestination;
    MediaPlayer mpDewa;

    Intent mIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT < 16){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else{
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
            try{
                getActionBar().hide();
                this.getSupportActionBar().hide();
            }catch (Exception e){
                Log.e("getActionBar>>", e.getMessage());}
        }
        setContentView(R.layout.activity_main);

        //Menu
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // VoiceRecBtn
        voiceRecBtn = (ImageView) findViewById(R.id.voiceRec);
        setGIF('w');

        //初期設定として、新宿駅を目的地に
        mpChime = MediaPlayer.create(getApplicationContext(), R.raw.chime);
        mpDestination = MediaPlayer.create(getApplicationContext(), R.raw.shinjuku);
        mpDewa = MediaPlayer.create(getApplicationContext(), R.raw.dewa);

        mIntent = new Intent(MainActivity.this, MapActivity.class);
        mIntent.putExtra("geo", 1);

        mOnTouchListener = new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if(!flag){

                            mpChime.start();
                            setGIF('p');

                        }else{

                            mpChime.start();
                            setGIF('p');
                        }
                        return true;

                    case MotionEvent.ACTION_UP:
                        if(!flag){

                            //******* 重複タッチ防止（あとで戻すこと）*******
                            voiceRecBtn.setOnTouchListener(new View.OnTouchListener() {@Override public boolean onTouch(View view, MotionEvent motionEvent) {return false;}});

                            mpChime.start();
                            //new Toast(MainActivity.this).makeText(MainActivity.this, "released111", Toast.LENGTH_SHORT).show();

                            setGIF('l');
                            setDelay('s', 2000, 6800, 'w');  //loading 3 + announce 6.8 -> wait

                            flag = true;

                        }else{

                            //******* 重複タッチ防止（あとで戻すこと）*******
                            voiceRecBtn.setOnTouchListener(new View.OnTouchListener() {@Override public boolean onTouch(View view, MotionEvent motionEvent) {return false;}});

                            mpChime.start();

                            setGIF('l');


                            //Original process
                            setDelay('d', 2000, 6300, 'i'); //loading 2 + announce 3.8 -> open MapActivity

                            //Demo process
                            //Intent mIntent = new Intent(MainActivity.this, MapActivity.class);
                            //startActivity(mIntent);
                            //setGIF('w');

                            flag = false;
                        }

                        return true;

                }

                return false;
            }


            //
            private void setDelay(final char announce, long toPrcss, final long toWait, final char mode){

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //announce
                        setGIF('p');

                        switch (announce){
                            case 's':
                                mpDestination.start();
                                animateNextText();
                                break;

                            case 'd':
                                mpDewa.start();
                                animateNextText();
                                break;
                        }


                        //mode変数によって待機モードに戻るか、他のアクティビティを始めるかを決める
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                switch(mode){
                                    case 'w':   //go to waiting mode at the end
                                        //******* 重複タッチ防止（あとで戻すこと）*******
                                        voiceRecBtn.setOnTouchListener(mOnTouchListener);
                                        setGIF('w');
                                        break;

                                    case 'i':   //start other Activity at the end
                                        //******* 重複タッチ防止（あとで戻すこと）*******
                                        voiceRecBtn.setOnTouchListener(mOnTouchListener);

                                        //mIntent.setData();

                                        startActivity(mIntent);
                                        setGIF('w');
                                        animateNextText();
                                        break;
                                }
                            }
                        }, toWait);   //loading delay

                    }

                }, toPrcss);   //announce delay

            }

        };
        voiceRecBtn.setOnTouchListener(mOnTouchListener);



        // TextView
        textView1 = (HTextView) findViewById(R.id.textview1);
        textView2 = (HTextView) findViewById(R.id.textview2);

        View.OnClickListener animOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof HTextView) {
                    animateNextText();
                }
            }
        };

        textView1.animateText(sentences[index]);
        textView2.animateText(sentences2[index++]);

    }

    private void animateNextText(){

        if (index + 1 > sentences.length) {
            index = 0;
        }
        textView1.animateText(sentences[index]);
        textView2.animateText(sentences2[index++]);

    }

    private void setGIF(char stat){

        switch (stat){
            case 'p':

                Glide.with(getApplicationContext()).clear(voiceRecBtn);
                Glide.get(getApplicationContext()).clearMemory();
                Glide.with(getApplicationContext()).load(R.raw.processing).into(voiceRecBtn);
                break;

            case 'w':

                Glide.with(getApplicationContext()).clear(voiceRecBtn);
                Glide.get(getApplicationContext()).clearMemory();
                Glide.with(getApplicationContext()).load(R.raw.waiting_new).into(voiceRecBtn);
                break;

            case 'l':

                Glide.with(getApplicationContext()).clear(voiceRecBtn);
                Glide.get(getApplicationContext()).clearMemory();
                Glide.with(getApplicationContext()).load(R.raw.loading).into(voiceRecBtn);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

            //reset
            index = 0;
            flag = false;
            setGIF('w');
            textView1.animateText(sentences[index]);
            textView2.animateText(sentences2[index++]);


            //change mp source
            MediaPlayer.create(getApplicationContext(), R.raw.shinjuku);

            //change setences[1]
            sentences[1] = "「新宿駅」ですね。";

            //putExtra
            mIntent.putExtra("geo", 1);

            voiceRecBtn.setOnTouchListener(mOnTouchListener);


        } else if (id == R.id.nav_gallery) {

            //reset
            index = 0;
            flag = false;
            setGIF('w');
            textView1.animateText(sentences[index]);
            textView2.animateText(sentences2[index++]);

            //change mp source
            MediaPlayer.create(getApplicationContext(), R.raw.shibuya);

            //change setences[1]
            sentences[1] = "「渋谷のハチ公前の広場」ですね。";

            //putExtra
            mIntent.putExtra("geo", 2);


            voiceRecBtn.setOnTouchListener(mOnTouchListener);

        } else if (id == R.id.nav_slideshow) {

            //reset
            index = 0;
            flag = false;
            setGIF('w');
            textView1.animateText(sentences[index]);
            textView2.animateText(sentences2[index++]);

            //change mp source
            MediaPlayer.create(getApplicationContext(), R.raw.fune);

            //change setences[1]
            sentences[1] = "「船の科学館駅」ですね。";

            //putExtra
            mIntent.putExtra("geo", 3);

            voiceRecBtn.setOnTouchListener(mOnTouchListener);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
