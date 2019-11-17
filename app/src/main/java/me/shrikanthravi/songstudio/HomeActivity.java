package me.shrikanthravi.songstudio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.LinearGradient;
import android.graphics.Point;
import android.graphics.Shader;
import android.graphics.drawable.Animatable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import me.shrikanthravi.songstudio.adapters.ViewPagerAdapter;
import me.shrikanthravi.songstudio.fragments.LibraryFragment;
import me.shrikanthravi.songstudio.fragments.SongsFragment;
import me.shrikanthravi.songstudio.services.MusicController;
import me.shrikanthravi.songstudio.services.MusicService;
import me.shrikanthravi.songstudio.utils.SimpleTabProvider;

import static me.shrikanthravi.songstudio.adapters.SongsAdapter.songList;

public class HomeActivity extends AppCompatActivity implements MediaController.MediaPlayerControl{

    String TAG = "HomeActivity";

    //Views
    ViewPager viewPager;
    TextView titleTV;
    ImageView slidingPanelImageView;
    CardView slidingPanelCardView;
    SlidingUpPanelLayout slidingUpPanelLayout;
    public TextView songName,artistName,newSongName,newArtistName,leftDuration,rightDuration;
    CardView slcviv;
    ImageView playPauseIV,newPlayPauseIV,previousIV,nextIV;
    LinearLayout slideLL;
    public SeekBar mSeekBar;

    //Vars
    int widthHeight ;
    int sWidth;
    public static boolean isSlidePanelOpen=false;
    public static boolean isSlidePanelOpen1=false;
    public static MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;
    public static MusicController controller;
    public static boolean paused=false, playbackPaused=false;
    boolean playBool=true;
    private static final int[] STATE_SET_PLAY =
            {R.attr.state_play, -R.attr.state_pause, -R.attr.state_stop};
    private static final int[] STATE_SET_PAUSE =
            {-R.attr.state_play, R.attr.state_pause, -R.attr.state_stop};
    int previouspos1=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //UI Init
        initUI();


    }

    //UI init
    void initUI(){
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        titleTV = findViewById(R.id.titleTV);
        titleTV.getPaint().setShader(new LinearGradient(0,0,0,titleTV.getLineHeight(),getResources().getColor(R.color.colorAccent),getResources().getColor(R.color.colorAccent1), Shader.TileMode.MIRROR));
        viewPager = findViewById(R.id.viewPager);
        setupViewPager();
        slidingPanelImageView = findViewById(R.id.slidingUpPanelImageView);
        slidingPanelCardView = findViewById(R.id.slidingUpPanelCardView);
        slidingUpPanelLayout = findViewById(R.id.sliding_layout);
        slcviv = findViewById(R.id.slcviv);
        playPauseIV = findViewById(R.id.playPauseIV);
        newPlayPauseIV = findViewById(R.id.newPlayPauseIV);
        previousIV = findViewById(R.id.previousIV);
        nextIV = findViewById(R.id.nextIV);
        songName = findViewById(R.id.songNameTV1);
        artistName = findViewById(R.id.artistNameTV1);
        newSongName = findViewById(R.id.newSongName);
        newArtistName = findViewById(R.id.newSongArtist);
        leftDuration = findViewById(R.id.leftDuration);
        rightDuration = findViewById(R.id.rightDuration);
        slideLL = findViewById(R.id.slideLL);
        mSeekBar = findViewById(R.id.seekbar);
        setupSlidingPanel();
        setupPlayerControls();
    }

    void setupPlayerControls(){
        playPauseIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(musicSrv.isPng()){

                    musicSrv.pausePlayer();
                    playPauseIV.setImageState(STATE_SET_PAUSE,true);
                    newPlayPauseIV.setImageState(STATE_SET_PAUSE,true);
                    playBool=false;
                }
                else{

                    musicSrv.play();
                    playPauseIV.setImageState(STATE_SET_PLAY,true);
                    newPlayPauseIV.setImageState(STATE_SET_PLAY,true);
                    playBool=true;
                }
            }
        });

        newPlayPauseIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(musicSrv.isPng()){
                    musicSrv.pausePlayer();
                    playPauseIV.setImageState(STATE_SET_PAUSE,true);
                    newPlayPauseIV.setImageState(STATE_SET_PAUSE,true);
                    playBool=false;
                }
                else{
                    musicSrv.play();
                    playPauseIV.setImageState(STATE_SET_PLAY,true);
                    newPlayPauseIV.setImageState(STATE_SET_PLAY,true);
                    playBool=true;
                }
            }
        });

        previousIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicSrv.playPrev();
                songName.setText(songList.get(musicSrv.getSong()).getSongName());
                newSongName.setText(songList.get(musicSrv.getSong()).getSongName());
                artistName.setText(songList.get(musicSrv.getSong()).getArtists());
                newArtistName.setText(songList.get(musicSrv.getSong()).getArtists());
            }
        });

        nextIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicSrv.playNext();
                songName.setText(songList.get(musicSrv.getSong()).getSongName());
                newSongName.setText(songList.get(musicSrv.getSong()).getSongName());
                artistName.setText(songList.get(musicSrv.getSong()).getArtists());
                newArtistName.setText(songList.get(musicSrv.getSong()).getArtists());
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(musicSrv != null && fromUser){
                    musicSrv.seek(progress*1000);
                }
            }
        });
        setController();
        final Handler mHandler = new Handler();
        HomeActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(musicSrv != null){
                    try {
                        if(musicSrv.isPng()) {


                            if(isSlidePanelOpen==true){
                                isSlidePanelOpen = false;
                                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                            }
                            SongsFragment.songsAdapter.setPlayingSongPostion(musicSrv.getSong());
                            if(previouspos1!=musicSrv.getSong()) {
                                SongsFragment.songsAdapter.notifyDataSetChanged();
                            }
                            previouspos1 = musicSrv.getSong();
                            /*
                            previouspos1 = musicSrv.getSong();
                            songList.get(previouspos).setPlaying(false);
                            songList.get(musicSrv.getSong()).setPlaying(true);*/
                            playPauseIV.setImageState(STATE_SET_PAUSE,true);
                            newPlayPauseIV.setImageState(STATE_SET_PAUSE,true);


                        int mCurrentPosition = musicSrv.getPosn() / 1000;

                        mSeekBar.setProgress(mCurrentPosition);
                        final long minutes = (mCurrentPosition) / 60;
                        int min = (int) minutes;
                        final int seconds = (int) ((mCurrentPosition) % 60);
                        boolean isTwoDigit3 = Integer.toString(Math.abs(min)).trim().length() == 2;
                        boolean isTwoDigit4 = Integer.toString(Math.abs(seconds)).trim().length() == 2;
                        if (!isTwoDigit3 && !isTwoDigit4) {

                            leftDuration.setText("0" + minutes + ":" + "0" + seconds);
                        } else {
                            if (!isTwoDigit3) {
                                leftDuration.setText("0" + minutes + ":" + seconds);
                            } else {
                                if (!isTwoDigit4) {

                                    leftDuration.setText(minutes + ":" + "0" + seconds);
                                }
                            }
                        }
                        final long minutes1 = (musicSrv.getDur() / 1000) / 60;
                        final int seconds1 = (int) ((musicSrv.getDur() / 1000) % 60);
                        int newminutes = (int) (minutes1 - minutes);
                        int newseconds = (seconds1 - seconds);

                        if (newseconds < 0) {
                            newminutes = newminutes - 1;
                            newseconds = 60 + newseconds;
                        }

                        boolean isTwoDigit1 = Integer.toString(Math.abs(newminutes)).trim().length() == 2;
                        boolean isTwoDigit2 = Integer.toString(Math.abs(newseconds)).trim().length() == 2;

                        if (!isTwoDigit1 && !isTwoDigit2) {
                            rightDuration.setText("-" + "0" + newminutes + ":" + "0" + newseconds);
                        } else {
                            if (!isTwoDigit1) {

                                rightDuration.setText("-" + "0" + newminutes + ":" + newseconds);
                            } else {
                                if (!isTwoDigit2) {

                                    rightDuration.setText("-" + newminutes + ":" + "0" + newseconds);
                                }
                            }
                        }
                        if (mCurrentPosition == 0 && musicSrv.isPng()) {
                            //System.out.println("song id in main activity " + songList.get(musicSrv.getSong()).getSongName());
                            //System.out.println("duration " + musicSrv.getDur());
                            mSeekBar.setMax(musicSrv.getDur() / 1000);
                            Picasso.get().load(songList.get(musicSrv.getSong()).getCoverImage()).into(slidingPanelImageView);
                            songName.setText(songList.get(musicSrv.getSong()).getSongName());
                            newSongName.setText(songList.get(musicSrv.getSong()).getSongName());
                            artistName.setText(songList.get(musicSrv.getSong()).getArtists());
                            newArtistName.setText(songList.get(musicSrv.getSong()).getArtists());

                        }
                        }
                        else{

                            playPauseIV.setImageState(STATE_SET_PLAY,true);
                            newPlayPauseIV.setImageState(STATE_SET_PLAY,true);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                mHandler.postDelayed(this, 1000);
            }
        });
    }

    void setupSlidingPanel(){
        slidingUpPanelLayout.setEnabled(true);
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        sWidth = point.x;
        System.out.println("Screen Width "+point.x);
        ViewGroup.LayoutParams params = slidingPanelImageView.getLayoutParams();
        widthHeight = params.width;
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {
                ViewGroup.LayoutParams params = slidingPanelImageView.getLayoutParams();
                int temp = ((int) (widthHeight + (widthHeight * v * sWidth/100)));
                if(temp<sWidth) {
                    params.width = params.height = ((int) (widthHeight + (widthHeight * v * sWidth/100)));
                    System.out.println("radius "+slcviv.getRadius()+" width "+params.width);
                    slidingPanelImageView.setLayoutParams(params);
                }
                slideLL.setAlpha(1-v);
                playPauseIV.setAlpha(1-v);
                if(v<1.0){
                    slideLL.setVisibility(View.VISIBLE);
                    playPauseIV.setVisibility(View.VISIBLE);
                }
                else{
                    if(v==1.0){
                        isSlidePanelOpen1=true;
                        slideLL.setVisibility(View.GONE);
                        playPauseIV.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onPanelStateChanged(View view, SlidingUpPanelLayout.PanelState panelState, SlidingUpPanelLayout.PanelState panelState1) {
                System.out.println("panel state " +panelState1);
                if(panelState.equals("EXPANDED")){

                    isSlidePanelOpen1=true;
                    ViewGroup.LayoutParams params = slidingPanelImageView.getLayoutParams();
                    params.width = params.height = ((int) (widthHeight + (widthHeight * sWidth/100)));
                    slidingPanelImageView.setLayoutParams(params);
                }
                if(panelState.equals("COLLAPSED")){
                    isSlidePanelOpen1=false;
                    slideLL.setVisibility(View.INVISIBLE);
                    playPauseIV.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SongsFragment(), "Songs");
        adapter.addFragment(new LibraryFragment(),"Library");
        viewPager.setAdapter(adapter);
        SmartTabLayout viewPagerTab = findViewById(R.id.viewpagertab);
        SimpleTabProvider tabProvider = new SimpleTabProvider(getApplicationContext(),R.layout.custom_tab,R.id.CustomTabText);
        viewPagerTab.setCustomTabView(tabProvider);
        viewPagerTab.setViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);
    }

    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }
    @Override
    protected void onPause(){
        super.onPause();
        paused=true;
    }
    @Override
    protected void onResume(){
        super.onResume();
        if(paused){
            setController();
            paused=false;
        }
    }
    @Override
    protected void onStop() {
        controller.hide();

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(musicConnection);
    }

    private void playNext(){
        musicSrv.playNext();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }

    private void playPrev(){
        musicSrv.playPrev();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }
    private void setController(){
        //set the controller up
        controller = new MusicController(this);
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });
        controller.setMediaPlayer(this);
        //controller.setAnchorView(findViewById(R.id.slideContent));
        controller.setEnabled(true);
    }
    @Override
    public boolean canPause() {
        return true;
    }
    @Override
    public boolean canSeekBackward() {
        return true;
    }
    @Override
    public boolean canSeekForward() {
        return true;
    }
    @Override
    public int getCurrentPosition() {
        if(musicSrv!=null && musicBound && musicSrv.isPng())
            return musicSrv.getPosn();
        else return 0;
    }
    @Override
    public int getBufferPercentage(){
        return 0;
    }
    @Override
    public int getAudioSessionId(){
        return 0;
    }
    @Override
    public int getDuration() {
        if(musicSrv!=null && musicBound && musicSrv.isPng())
            return musicSrv.getDur();
        else return 0;
    }
    @Override
    public boolean isPlaying() {
        if(musicSrv!=null && musicBound)
            return musicSrv.isPng();
        return false;
    }
    @Override
    public void pause() {
        playbackPaused=true;
        musicSrv.pausePlayer();
    }

    @Override
    public void seekTo(int pos) {
        musicSrv.seek(pos);
    }

    @Override
    public void start() {
        musicSrv.go();
    }

    @Override
    public void onBackPressed(){
        if(isSlidePanelOpen1==true){
            System.out.println("slidepanel back");
            isSlidePanelOpen1=false;
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
        else{
        }
    }


}
