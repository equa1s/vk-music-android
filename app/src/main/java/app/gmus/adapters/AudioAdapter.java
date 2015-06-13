package app.gmus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import app.gmus.R;
import app.gmus.audio.Audio;

public class AudioAdapter extends ArrayAdapter<Audio> {

    private ViewHolder viewHolder;
    private List<Audio> audios;
    Audio audio = null;

    public AudioAdapter(Context context, List<Audio> audios) {
        super(context, R.layout.list_item, audios);
        this.audios = audios;
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
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
                v = inflater.inflate(R.layout.list_item, parent, false);
                viewHolder.setTitle((TextView) v.findViewById(R.id.audio_title));
                viewHolder.setArtist((TextView) v.findViewById(R.id.audio_artist));
                v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }
        viewHolder.getTitle().setText(audio.getTitle());
        viewHolder.getArtist().setText(audio.getArtist());
        return v;
    }

}
