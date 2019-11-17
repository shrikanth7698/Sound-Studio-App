package me.shrikanthravi.songstudio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.shrikanthravi.songstudio.adapters.SongsAdapter;
import me.shrikanthravi.songstudio.data.model.Song;
import me.shrikanthravi.songstudio.data.remote.APIService;
import me.shrikanthravi.songstudio.data.remote.GlobalData;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {

    String TAG = "HomeActivity";

    //Views
    RecyclerView songsRV;
    SongsAdapter songsAdapter;

    //Vars
    List<Song> songList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //UI Init
        initUI();

        //TODO Add internet check before gettings songs from api
        getSongs();
    }

    //UI init
    void initUI(){
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        songsRV = findViewById(R.id.songsRV);
        songsAdapter = new SongsAdapter(songList, getApplicationContext(), new SongsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Song item) {
                //TODO implement play function
            }
        });
        songsRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
        songsRV.setAdapter(songsAdapter);

    }

    // Fetching songs from given API using Retrofit
    void getSongs(){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl(GlobalData.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        APIService apiService = retrofit.create(APIService.class);
        Call apiCall = apiService.getSongs();
        apiCall.enqueue(new retrofit2.Callback<List<Song>>() {
            @Override
            public void onResponse(@NonNull Call<List<Song>> call, @NonNull Response<List<Song>> response) {

                System.out.println("fetch songs retro response code "+response.code()+" "+response.message());

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    /*for(Song song: response.body()){
                        System.out.println(song.getSongName());
                    }*/
                    songList.clear();
                    songList.addAll(response.body());
                    songsAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Server Error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<Song>> call, @NonNull Throwable t) {

                Log.e(TAG, "Unable to submit post to API.");
                System.out.println("fetch songs error "+ t.getLocalizedMessage());

            }
        });
    }

}
