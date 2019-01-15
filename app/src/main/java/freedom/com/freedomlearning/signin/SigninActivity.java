package freedom.com.freedomlearning.signin;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import freedom.com.freedomlearning.R;
import freedom.com.freedomlearning.utilities.DatabaseService;

public class SigninActivity extends AppCompatActivity {

    public static final String TAG = SigninActivity.class.getSimpleName();

    private ImageView imgFacebook;
    private ImageView imgGoogle;
    private SigninFacebook signinFacebook;
    private SigninGoogle signinGoogle;
    private LoginManager loginManager;
    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private DatabaseService mData = DatabaseService.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        FacebookSdk.sdkInitialize(getApplicationContext());

        getControls();
        setEvents();
    }

    private void getControls() {
        imgFacebook = (ImageView) findViewById(R.id.img_facebook);
        imgGoogle = (ImageView) findViewById(R.id.img_google);
        //login facebook
        loginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();
        signinFacebook = new SigninFacebook(loginManager, callbackManager, this);

        //login google
//        signinGoogle = new SigninGoogle(getString(R.string.default_web_client_id), this);

        mAuth = mData.getFirebaseAuth();
    }

    private void setEvents() {

        imgFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signinFacebook.signinFacebook();
            }
        });

        imgGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = signinGoogle.getmGoogleSignInClient().getSignInIntent();
                startActivityForResult(signInIntent, signinGoogle.RC_SIGN_IN);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        signinFacebook.callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == signinGoogle.RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                signinGoogle.firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }
}
