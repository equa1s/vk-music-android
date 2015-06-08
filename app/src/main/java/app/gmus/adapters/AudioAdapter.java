package app.gmus.adapters;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import app.gmus.R;
import app.gmus.audio.Audio;

public class AudioAdapter extends ArrayAdapter<Audio> {

    boolean isPlaying = false;
    MediaPlayer mediaPlayer = new MediaPlayer();
    ViewHolder viewHolder;
    Audio audio;

    public AudioAdapter(Context context, List<Audio> audios) {
        super(context, R.layout.list_item, audios);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        audio = getItem(position);
        if(convertView == null) {
                viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_item, parent, false);
                viewHolder.title = (TextView) convertView.findViewById(R.id.audio_title);
                viewHolder.artist = (TextView) convertView.findViewById(R.id.audio_artist);
                viewHolder.play = (ImageView) convertView.findViewById(R.id.image_play);
                viewHolder.play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "Position: " + getPosition(audio), Toast.LENGTH_SHORT).show();
                        if (isPlaying) {
                            viewHolder.play.setImageResource(R.drawable.pause_icon);
                            try {
                                mediaPlayer.reset();
                                mediaPlayer.setDataSource(audio.getUrl().toString());
                                mediaPlayer.prepare();
                                mediaPlayer.start();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            viewHolder.play.setImageResource(R.drawable.play_icon);
                            mediaPlayer.pause();
                        }
                        isPlaying = !isPlaying;
                    }
                });
                convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(audio.getTitle());
        viewHolder.artist.setText(audio.getArtist());

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                viewHolder.play.setImageResource(R.drawable.play_icon);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView title;
        TextView artist;
        ImageView play;
    }

}
