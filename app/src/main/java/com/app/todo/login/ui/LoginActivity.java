package com.app.todo.login.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.app.todo.todohome.ui.Activity.ToDoMainActivity;
import com.app.todo.registration.ui.RegistrationActivity;
import com.app.todo.todohome.ui.Fragment.DailogFragment;
import com.crashlytics.android.Crashlytics;
import com.example.bridgeit.todoapp.BuildConfig;
import com.example.bridgeit.todoapp.R;
import com.app.todo.baseclass.BaseActivity;
import com.app.todo.model.UserModel;
import com.app.todo.login.presenter.LoginPresenter;
import com.app.todo.utils.Constants;
import com.app.todo.utils.SessionManagement;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import io.fabric.sdk.android.Fabric;

public class LoginActivity extends BaseActivity implements LoginViewInterface, GoogleApiClient.OnConnectionFailedListener {
    private String TAG = "LoginActivity";

    private FirebaseAuth mAuth;
    AppCompatEditText useremailedittext, userpasswordedittext;
    AppCompatTextView forgotpasswordtextview, createaccounttextview;
    AppCompatButton userloginbutton;
    String useremail, user_password;
    LoginButton fbloginbutton;
    // AppCompatButton googlesigninbutton;
    String emailPattern = Constants.email_pattern;
    LoginPresenter presenter;
    SessionManagement session;
    CallbackManager callbackManager;
    GoogleApiClient googleApiClient;
    FrameLayout frameLayout;

    String message;
    SignInButton signInButton;
    int RC_SIGN_IN = 999;

    boolean isFbLogin, isGoogleLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Fabric.with(this,new Crashlytics());


        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }
        callbackManager = CallbackManager.Factory.create();
        initView();
        fbloginbutton = (LoginButton) findViewById(R.id.fbLogIn);
        fbloginbutton.setReadPermissions("public_profile email");
        signInButton = (SignInButton) findViewById(R.id.signingoogle);
        signInButton.setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        fbloginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbloginbutton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.i(TAG, "onSuccess: ");
                        frameLayout.setVisibility(View.VISIBLE);

                        handleFacebook(loginResult.getAccessToken());
                        String accessToken = loginResult.getAccessToken().getToken();
                        Log.i("accesstoken", "access Token ");
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginActivity.this, "you have canceld login", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void handleFacebook(final AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, "onSuccess: ");
                    GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            try {
                                isFbLogin = true;
                                isGoogleLogin = false;
                                UserModel model = new UserModel();
                                model.setEmail(object.getString(Constants.key_fb_email));
                                model.setFullname(object.getString(Constants.fb_firstName) + " "
                                        + object.getString(Constants.fb_lastName));
                                String id = object.getString("id");
                                URL profile_pic = new URL(getString(R.string.fb_url_start) + id + getString(R.string.fb_url_last));
                                model.setId(task.getResult().getUser().getUid());
                                model.setMobileNo(String.valueOf(profile_pic));
                                model.setPassword("");
                                session.loginToSharedPref(model, isGoogleLogin, isFbLogin);
                                startActivity(new Intent(getApplicationContext(), ToDoMainActivity.class));
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                        }
                    });     Bundle bundle = new Bundle();

                    bundle.putString("fields", "id,first_name,last_name,email");
                    request.setParameters(bundle);
                    request.executeAsync();

                } else {
                    Toast.makeText(LoginActivity.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();

                }
            }


        });
    }

    @Override
    public void initView() {

        presenter = new LoginPresenter(this, this);

        mAuth = FirebaseAuth.getInstance();
        frameLayout= (FrameLayout) findViewById(R.id.dailogframelayout);
        //   googlesigninbutton= (AppCompatButton) findViewById(R.id.signingoogle);
        useremailedittext = (AppCompatEditText) findViewById(R.id.loginEdittext);
        userpasswordedittext = (AppCompatEditText) findViewById(R.id.passwordEdittext);
        forgotpasswordtextview = (AppCompatTextView) findViewById(R.id.forgotpassTextview);
        createaccounttextview = (AppCompatTextView) findViewById(R.id.createaccounttextview);
        userloginbutton = (AppCompatButton) findViewById(R.id.loginbutton);
        session = new SessionManagement(this);
        ///  googlesigninbutton.setOnClickListener(this);
        userloginbutton.setOnClickListener(this);
        forgotpasswordtextview.setOnClickListener(this);
        createaccounttextview.setOnClickListener(this);
    }

    public void onStart() {
        super.onStart();
        FirebaseUser currentuser = mAuth.getCurrentUser();
    }

    private boolean validate() {
        boolean valid = false;
        if (useremailedittext.getText().toString().length() == 0  ) {
            // Toast.makeText(getApplicationContext(), R.string.blank, Toast.LENGTH_SHORT).show();
            useremailedittext.setError(getString(R.string.blank));
        }
            else if (userpasswordedittext.getText().toString().length() == 0) {
            userpasswordedittext.setError(getString(R.string.blank));
            valid = false;
        } else {
            if (useremailedittext.getText().toString().matches(emailPattern) && userpasswordedittext.getText().toString().length() >= 6) {
                valid = true;
            }
            if (!useremailedittext.getText().toString().matches(emailPattern) )
            {
                useremailedittext.setError(getString(R.string.invalid_email));
                valid = false;
            }else if(userpasswordedittext.getText().toString().length() < 6){
                userpasswordedittext.setError(getString(R.string.invalid_pass));
            }
        }
        return valid;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.loginbutton:
                useremail = useremailedittext.getText().toString();
                user_password = userpasswordedittext.getText().toString();
                if (validate()) {
                    presenter.requestForLogin(useremail, user_password);
                }
                break;

            case R.id.createaccounttextview:
                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(i);
                break;

            case R.id.signingoogle:
                signIn();
                break;
            case R.id.forgotpassTextview:

                break;

        }
    }

    @Override
    public void loginSuccess(UserModel model, String uid) {
        isFbLogin=false;
        isGoogleLogin=false;
        session.loginToSharedPref(model, isGoogleLogin, isFbLogin);
        Intent intent = new Intent(LoginActivity.this, ToDoMainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void loginFailure(String message) {
        //Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    ProgressDialog progressDialog;

    @Override
    public void showProgressDialog(String message) {
        if (!isFinishing()) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    @Override
    public void hideProgressDialog() {
        if (!isFinishing() && progressDialog != null)
            progressDialog.dismiss();
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        frameLayout.setVisibility(View.INVISIBLE);
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                Toast.makeText(this, account.getEmail(), Toast.LENGTH_SHORT).show();
            } else {
                Log.i(TAG, "onActivityResult: " + result.getSignInAccount());
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {
        showProgressDialog("Loading...");
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    isGoogleLogin = true;
                    isFbLogin = false;
                    UserModel model = new UserModel();
                    model.setId(task.getResult().getUser().getUid());
                    model.setPassword("");
                    model.setMobileNo(account.getPhotoUrl().toString());
                    model.setFullname(account.getDisplayName());
                    model.setEmail(account.getEmail());

                    session.loginToSharedPref(model, isGoogleLogin, isFbLogin);

                    Toast.makeText(LoginActivity.this, "success...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, ToDoMainActivity.class);
                    startActivity(intent);
                    finish();
                } else
                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        // updateUI(null);
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}