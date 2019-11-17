package me.shrikanthravi.songstudio.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import me.shrikanthravi.songstudio.R;
import me.shrikanthravi.songstudio.data.model.Song;

public class SongsAdapter  extends RecyclerView.Adapter<SongsAdapter.MyViewHolder>{
    private List<Song> songList;
    private final OnItemClickListener listener;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout rootLayout;
        public CardView songCoverCV;
        public ImageView songCoverIV;
        public TextView songNameTV;
        public TextView artistTV;
        public MyViewHolder(View view) {
            super(view);
            rootLayout = view.findViewById(R.id.rootLayout);
            songCoverCV = view.findViewById(R.id.songCoverCV);
            songCoverIV = view.findViewById(R.id.songCoverIV);
            songNameTV = view.findViewById(R.id.songNameTV);
            artistTV = view.findViewById(R.id.artistTV);

        }
    }

    public SongsAdapter(List<Song> verticalList, Context context, OnItemClickListener listener) {
        this.songList = verticalList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_row_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Song song = songList.get(position);
        Picasso.get().load(song.getCoverImage()).into(holder.songCoverIV);
        holder.songNameTV.setText(song.getSongName());
        holder.artistTV.setText(song.getArtists());
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(song);
            }
        });
    }
    @Override
    public int getItemCount() {
        return songList.size();
    }


    public interface OnItemClickListener {
        void onItemClick(Song item);
    }
}
