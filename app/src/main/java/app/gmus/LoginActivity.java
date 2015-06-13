package app.gmus;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;
import com.vk.sdk.dialogs.VKCaptchaDialog;
import com.vk.sdk.util.VKUtil;


public class LoginActivity extends AppCompatActivity {

    public static final String[] sMyScope = new String[] {
            VKScope.AUDIO
    };

    public Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        VKUIHelper.onCreate(this);
        VKSdk.initialize(sdkListener, "4945015");
        if (VKSdk.wakeUpSession()) {
            startMainActivity();
            return;
        }
        String[] fingerprint = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        if (fingerprint != null) {
            Log.d("Fingerprint", fingerprint[0]);
        }

        login = (Button) findViewById(R.id.sign_in_with_vk);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VKSdk.authorize(sMyScope, true, false);
            }
        });

    }

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

    /**
     * VK Listener
     * */
    private final VKSdkListener sdkListener = new VKSdkListener() {

        @Override
        public void onCaptchaError(VKError captchaError) {
            new VKCaptchaDialog(captchaError).show(getApplicationContext());
        }

        @Override
        public void onTokenExpired(VKAccessToken expiredToken) {
            VKSdk.authorize(sMyScope);
        }

        @Override
        public void onAccessDenied(final VKError authorizationError) {
            new AlertDialog.Builder(VKUIHelper.getTopActivity()).setMessage(authorizationError.toString()).show();
        }

        @Override
        public void onReceiveNewToken(VKAccessToken newToken) {
            startMainActivity();
        }

        @Override
        public void onAcceptUserToken(VKAccessToken token) {
            startMainActivity();
        }

    };

    /**
     * Starting MainActivity.java
     * */
    protected void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

}
