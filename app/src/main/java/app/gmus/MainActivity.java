package app.gmus;


import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import app.gmus.adapters.AudioAdapter;
import app.gmus.audio.Audio;


public class MainActivity extends AppCompatActivity {

    private ArrayList<Audio> audioList;
    private MediaPlayer mediaPlayer;
    private boolean clicked = true;
    private ImageView playImage;
    private ListView listView;
    private int onItemClickPosition;
    private View onItemClickView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(this);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list_response);
            audioList = new ArrayList<>();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(onCompletionListener);
        new VKRequest("audio.get").executeWithListener(vkRequestListener);
        listView.setOnItemClickListener(onItemClickListener);
    }


    VKRequest.VKRequestListener vkRequestListener = new VKRequest.VKRequestListener() {
        @Override
        public void onComplete(VKResponse response) {
            try {
                audioList = toAudioList(response);
                listView.setAdapter(new AudioAdapter(getApplicationContext(), audioList));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(), "Getting list of audios...", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
            super.attemptFailed(request, attemptNumber, totalAttempts);
        }

        @Override
        public void onError(VKError error) {
            super.onError(error);
        }

        @Override
        public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
            super.onProgress(progressType, bytesLoaded, bytesTotal);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
    }

    protected ArrayList<Audio> toAudioList(VKResponse response) throws JSONException {
            ArrayList<Audio> audioList = new ArrayList<>();
            JSONObject jsonObject = response.json.getJSONObject("response");
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            JSONObject audio;
            for(int i = 0; i < jsonArray.length(); i++) {
                audio = (JSONObject) jsonArray.get(i);
                audioList.add(new Audio(audio.optString("artist"), audio.optString("title"), audio.optString("url")));
            }
        return audioList;
    }

    /**
     *  OnItemClickListener for main ListView in MainActivity.class
     */
    ListView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            onItemClickPosition = position;
            onItemClickView = view;
            playImage = (ImageView) onItemClickView.findViewById(R.id.image_play);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(clicked) {
                                Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT).show();
                                Audio a = audioList.get(onItemClickPosition);
                                try {
                                    mediaPlayer.setDataSource(a.getUrl().toString());
                                    mediaPlayer.prepare();
                                    mediaPlayer.start();
                                    playImage.setImageResource(R.drawable.pause_icon);
                                    clicked = false;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }  else {
                                mediaPlayer.reset();
                                mediaPlayer.stop();
                                playImage.setVisibility(View.INVISIBLE);
                                clicked = true;
                            }
                        }
                    });
                }
            }).start();

        }
    };

    /**
     *  OnCompletionListener for MediaPlayer
     *  TODO: Realize queue from current track
     */
    MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mp) {
            // playImage = (ImageView) onItemClickView.findViewById(R.id.image_play);
            playImage.setVisibility(View.INVISIBLE);
            // mediaPlayer = mp;
            /* new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                                Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT).show();
                                Audio a = audioList.get(onItemClickPosition + 1);
                                try {
                                    mediaPlayer.setDataSource(a.getUrl().toString());
                                    mediaPlayer.prepare();
                                    mediaPlayer.start();
                                    playImage.setVisibility(View.VISIBLE);
                                    playImage.setImageResource(R.drawable.pause_icon);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                        }
                    });
                }
            }).start();*/
        }
    };

}
