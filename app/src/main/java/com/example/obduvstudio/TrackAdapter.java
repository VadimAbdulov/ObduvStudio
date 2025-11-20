package com.example.obduvstudio;

import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.io.IOException;
import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {
    private List<AudioTrack> tracks;
    private MediaPlayer mediaPlayer;

    public TrackAdapter(List<AudioTrack> tracks) {
        this.tracks = tracks;
        this.mediaPlayer = new MediaPlayer();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_track, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AudioTrack track = tracks.get(position);
        holder.trackName.setText(track.getName());

        holder.playTrackButton.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                holder.playTrackButton.setText("PLAY");
            } else {
                try {
                    mediaPlayer.setDataSource(track.getFilePath());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    holder.playTrackButton.setText("STOP");

                    mediaPlayer.setOnCompletionListener(mp -> {
                        holder.playTrackButton.setText("PLAY");
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        holder.deleteTrackButton.setOnClickListener(v -> {
            tracks.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView trackName;
        public Button playTrackButton;
        public Button deleteTrackButton;

        public ViewHolder(View view) {
            super(view);
            trackName = view.findViewById(R.id.trackName);
            playTrackButton = view.findViewById(R.id.playTrackButton);
            deleteTrackButton = view.findViewById(R.id.deleteTrackButton);
        }
    }
}