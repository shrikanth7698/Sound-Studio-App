package me.shrikanthravi.songstudio.data.remote;

import java.util.List;
import me.shrikanthravi.songstudio.data.model.Song;
import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {
    @GET("studio")
    Call<List<Song>> getSongs();
}
