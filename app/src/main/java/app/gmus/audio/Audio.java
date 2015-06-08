package app.gmus.audio;

import java.net.MalformedURLException;
import java.net.URL;

public class Audio {

    private String artist;
    private String title;
    private URL url;

    public Audio(String artist, String title, String url) {
        this.artist = artist;
        this.title = title;
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

}
