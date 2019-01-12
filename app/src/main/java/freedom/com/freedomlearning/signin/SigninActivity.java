package freedom.com.freedomlearning.signin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import freedom.com.freedomlearning.R;
import freedom.com.freedomlearning.utilities.DatabaseService;

public class SigninActivity extends AppCompatActivity {

    public static final String TAG = SigninActivity.class.getSimpleName();

    private ImageView imgFacebook;
    private ImageView imgGoogle;
    private SigninFacebook signinFacebook;
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

        mAuth = mData.getFirebaseAuth();
    }

    private void setEvents() {

        imgFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signinFacebook.signinFacebook();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        signinFacebook.callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
