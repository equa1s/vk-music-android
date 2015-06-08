package app.gmus.adapters;

import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder {

    private TextView title;
    private TextView artist;
    private ImageView play;

    public ImageView getPlay() {
        return play;
    }

    public void setPlay(ImageView play) {
        this.play = play;
    }

    public TextView getArtist() {
        return artist;
    }

    public void setArtist(TextView artist) {
        this.artist = artist;
    }

    public TextView getTitle() {
        return title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }
}
