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
import java.util.List;

import app.gmus.adapters.AudioAdapter;
import app.gmus.audio.Audio;


public class MainActivity extends AppCompatActivity {

    private ArrayList<Audio> audioList;
    private ListView listView;
    private VKResponse responseString;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(this);
        setContentView(R.layout.activity_main);
            listView = (ListView) findViewById(R.id.list_response);
            audioList = new ArrayList<Audio>();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        new VKRequest("audio.get").executeWithListener(requestListener);
        //listView.setOnItemClickListener(onItem);
    }

    VKRequest.VKRequestListener requestListener = new VKRequest.VKRequestListener() {
        @Override
        public void onComplete(VKResponse response) {
            try {
                responseString = response;
                audioList = toAudioList(responseString);
                listView.setAdapter(new AudioAdapter(getApplicationContext(), audioList));
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
            ArrayList<Audio> audioList = new ArrayList<Audio>();
            JSONObject jsonObject = response.json.getJSONObject("response");
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            JSONObject audio;
            for(int i = 0; i < jsonArray.length(); i++) {
                audio = (JSONObject) jsonArray.get(i);
                audioList.add(new Audio(audio.optString("artist"), audio.optString("title"), audio.optString("url")));
            }
        return audioList;
    }

    // TODO: RELEASE THIS LISTENER
/*    ListView.OnItemClickListener onItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(getApplicationContext(), "Position item: " + position, Toast.LENGTH_SHORT).show();
            ImageView v = (ImageView) view.findViewById(R.id.image_play);
            v.setImageResource(R.drawable.pause_icon);
            Audio a = audioList.get(position);
            try {
                mediaPlayer.setDataSource(a.getUrl().toString());
                mediaPlayer.
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };*/

}
