package me.shrikanthravi.songstudio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.TimeUnit;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //TODO Add internet check
        getSongs();
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
                    for(Song song: response.body()){
                        System.out.println(song.getSongName());
                    }
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
