package me.shrikanthravi.songstudio.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.shrikanthravi.songstudio.R;
import me.shrikanthravi.songstudio.adapters.SongsAdapter;
import me.shrikanthravi.songstudio.data.model.Song;
import me.shrikanthravi.songstudio.data.remote.APIService;
import me.shrikanthravi.songstudio.data.remote.GlobalData;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class SongsFragment extends Fragment {


    public SongsFragment() {
        // Required empty public constructor
    }
    String TAG = "SongsFragment";

    //Views
    RecyclerView songsRV;
    SongsAdapter songsAdapter;

    //Vars
    List<Song> songList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_songs, container, false);
        initView(view);
        //TODO Add internet check before gettings songs from api
        getSongs();
        return view;
    }

    void initView(View view){
        songsRV = view.findViewById(R.id.songsRV);
        songsAdapter = new SongsAdapter(songList, getActivity().getApplicationContext(), new SongsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Song item) {
                //TODO implement play function
            }
        });
        songsRV.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(),RecyclerView.VERTICAL,false));
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
                    Toast.makeText(getActivity().getApplicationContext(),"Server Error", Toast.LENGTH_SHORT).show();
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
