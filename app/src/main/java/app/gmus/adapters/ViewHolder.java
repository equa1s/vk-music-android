package app.gmus.adapters;

import android.widget.TextView;

public class ViewHolder {

    private TextView title;
    private TextView artist;;

    public TextView getTitle() {
        return title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    public TextView getArtist() {
        return artist;
    }

    public void setArtist(TextView artist) {
        this.artist = artist;
    }
}
