package me.shrikanthravi.songstudio.services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.util.ArrayList;
import me.shrikanthravi.songstudio.data.model.Song;

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener,MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener  {
    //media player
    private MediaPlayer player;
    //song list
    private ArrayList<Song> songs;
    //current position
    private int songPosn;
    private final IBinder musicBind = new MusicBinder();
    public boolean isMpPrepared = false;
    public MusicService() {
    }

    public void onCreate(){
        //create the service
        super.onCreate();
        System.out.println("service tested");
        //initialize position
        songPosn=0;
        //create player
        player = new MediaPlayer();
        initMusicPlayer();
    }
    public void initMusicPlayer(){
        //set player properties
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }
    public void setList(ArrayList<Song> theSongs){
        songs=theSongs;
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }
    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }


    public void playSong(){
        isMpPrepared=false;
        //play a song
        player.reset();
        //get song
        System.out.println("song position"+songPosn);
        Song playSong = songs.get(songPosn);
        //get id
        String currSong = playSong.getUrl();
        //set uri
        try{
            player.setDataSource(currSong);
        }
        catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }

        try {

            player.prepareAsync();
            /*isMpPrepared=true;
            player.start();
            System.out.println("mp prepared");*/
        }catch (Exception e){

            e.printStackTrace();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //start playback
        isMpPrepared=true;
        System.out.println("why");
        mp.start();
        System.out.println("mp prepared");
    }
    public void setSong(int songIndex){
        songPosn=songIndex;
    }
    public int getSong(){
        return songPosn;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(isMpPrepared) {
            if (player.getCurrentPosition() > 0) {
                mp.reset();
                playNext();
            }
        }
    }
    @Override
    public void onDestroy() {
        stopForeground(true);
    }
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();

        return false;
    }
    public int getPosn(){
        return player.getCurrentPosition();
    }

    public int getDur(){
        return player.getDuration();
    }

    public boolean isPng(){
        return player.isPlaying();
    }

    public void pausePlayer(){
        player.pause();
    }

    public void seek(int posn){
        player.seekTo(posn);
    }

    public void go(){
        player.start();
    }
    public void playPrev(){
        if(songs!=null) {
            songPosn--;
            if (songPosn < 0) songPosn = songs.size() - 1;
            playSong();
        }
    }

    //skip to next
    public void playNext(){
        if(songs!=null) {
            songPosn++;
            if (songPosn >= songs.size()) songPosn = 0;
            playSong();
        }

    }


    public void play(){
        player.start();
    }

    public  boolean getMpStatus(){
        return isMpPrepared;
    }
}

