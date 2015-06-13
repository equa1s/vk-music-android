package app.gmus;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.AsyncPlayer;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.List;

import app.gmus.adapters.AudioAdapter;
import app.gmus.audio.Audio;


public class MainActivity extends AppCompatActivity {

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mDrawerAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    private List<Audio> audioList = new ArrayList<>();
    private ListView listView;
    private AsyncPlayer asyncPlayer = new AsyncPlayer("");
    private boolean isPlaying = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(this);
        setContentView(R.layout.activity_main);

        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        listView = (ListView) findViewById(R.id.list_response);
        new VKRequest("audio.get").executeWithListener(vkRequestListener);
        listView.setOnItemClickListener(onItemClickListener);

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    VKRequest.VKRequestListener vkRequestListener = new VKRequest.VKRequestListener() {
        @Override
        public void onComplete(VKResponse response) {
            try {
                audioList = toAudioList(response);
                AudioAdapter audioAdapter = new AudioAdapter(getApplicationContext(), audioList);
                listView.setAdapter(audioAdapter);
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
        if(asyncPlayer != null) {
            asyncPlayer.stop();
        }
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

    ListView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Audio a = audioList.get(position);
            ImageView playImage = (ImageView) view.findViewById(R.id.show_audio_status);
            if(isPlaying) {
                asyncPlayer.stop();
                playImage.setVisibility(View.INVISIBLE);
            } else {
                asyncPlayer.play(getApplicationContext(), Uri.parse(a.getUrl()), false, AudioManager.STREAM_MUSIC);
                playImage.setVisibility(View.VISIBLE);
                playImage.setImageResource(R.drawable.ic_action_pause);
            }
            isPlaying = !isPlaying;
        }
    };

    private void addDrawerItems() {
        final String[] menuArray = getResources().getStringArray(R.array.drawer_array);
        mDrawerAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, menuArray);
        mDrawerList.setAdapter(mDrawerAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), menuArray[position], Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupDrawer() {

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getSupportActionBar().setTitle(mActivityTitle);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.drawer_menu_title);
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
