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

import java.util.ArrayList;

import app.gmus.R;
import app.gmus.audio.Audio;

public class AudioAdapter extends ArrayAdapter<Audio> {

    private boolean isPlaying = false;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private ViewHolder viewHolder = new ViewHolder();
    private ArrayList<Audio> audios;
    Audio audio = null;

    public AudioAdapter(Context context, int id, ArrayList<Audio> audios) {
        super(context, id, audios);
        this.audios = audios;
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(complete);
    }

    @Override
    public Audio getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(Audio item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        audio = audios.get(position);
        if(v == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
                v = inflater.inflate(R.layout.list_item, null);
                viewHolder.setTitle((TextView) v.findViewById(R.id.audio_title));
                viewHolder.setArtist((TextView) v.findViewById(R.id.audio_artist));
                viewHolder.setPlay((ImageView) v.findViewById(R.id.image_play));
                v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }
        viewHolder.getTitle().setText(audio.getTitle());
        viewHolder.getArtist().setText(audio.getArtist());
        return v;
    }


    MediaPlayer.OnCompletionListener complete = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            viewHolder.getPlay().setImageResource(R.drawable.play_icon);
        }
    };

}
