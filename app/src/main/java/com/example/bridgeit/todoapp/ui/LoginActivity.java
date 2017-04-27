package com.example.bridgeit.todoapp.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.example.bridgeit.todoapp.R;
import com.example.bridgeit.todoapp.baseclass.BaseActivity;
import com.example.bridgeit.todoapp.model.UserModel;
import com.example.bridgeit.todoapp.presenter.LoginPresenter;
import com.example.bridgeit.todoapp.utils.Constants;
import com.example.bridgeit.todoapp.utils.SessionManagement;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends BaseActivity implements LoginViewInterface, GoogleApiClient.OnConnectionFailedListener {
    private String TAG = "LoginActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
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
    GoogleSignInOptions googleSignInOptions;
    GoogleApiClient googleApiClient;
    SharedPreferences userPref;
    SharedPreferences.Editor editor;
    String message;
    SignInButton signInButton;
    int RC_SIGN_IN = 999;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        callbackManager = CallbackManager.Factory.create();
        initView();
        fbloginbutton = (LoginButton) findViewById(R.id.fbLogIn);
        fbloginbutton.setReadPermissions("public_profile ","email");

        userPref = this.getSharedPreferences(Constants.key_pref, MODE_PRIVATE);
        editor = userPref.edit();

        signInButton= (SignInButton) findViewById(R.id.signingoogle);

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
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, "onSuccess: ");
                    GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            String emailid;
                            try {
                                emailid = object.getString("email");
                                editor.putString(Constants.userRegister, "true");
                                editor.putString(Constants.userEmail, emailid);
                                editor.putString(Constants.profilePic,  object.getString("profile_Pic"));
                                editor.putString(Constants.firstName, object.getString("first_name"));
                                editor.putString(Constants.lastName, object.getString("last_name"));
                                editor.commit();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    Bundle bundle = new Bundle();
                    bundle.putString("fields", "id,first_name,last_name,email");
                    request.setParameters(bundle);
                    request.executeAsync();
                    startActivity(new Intent(getApplicationContext(), ToDoMainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();

                }
            }


        });



    }

    private Bundle getFacebookData(JSONObject object) {
        try {
            Bundle bundle = new Bundle();
            String id = object.getString("Id");
            try {

                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                bundle.putString("profilepic", profile_pic.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("fbid", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            return bundle;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void initView() {

        presenter = new LoginPresenter(this, this);

        mAuth = FirebaseAuth.getInstance();
     //   googlesigninbutton= (AppCompatButton) findViewById(R.id.signingoogle);
        useremailedittext = (AppCompatEditText) findViewById(R.id.loginEdittext);
        userpasswordedittext = (AppCompatEditText) findViewById(R.id.passwordEdittext);
        forgotpasswordtextview = (AppCompatTextView) findViewById(R.id.forgotpassTextview);
       createaccounttextview = (AppCompatTextView) findViewById(R.id.createaccounttextview);
        userloginbutton = (AppCompatButton) findViewById(R.id.loginbutton);

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
        if (useremailedittext.getText().toString().length() == 0 || userpasswordedittext.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), R.string.blank, Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            if (useremailedittext.getText().toString().matches(emailPattern) && userpasswordedittext.getText().toString().length() >= 6) {
                valid = true;
            }
            if (!useremailedittext.getText().toString().matches(emailPattern) && userpasswordedittext.getText().toString().length() < 6) {
                valid = false;
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
        userPref = getApplicationContext().getSharedPreferences(Constants.key_pref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userPref.edit();
        editor.putString("keyemail", model.getEmail());
        editor.putString("keyname", model.getFullname());
        editor.putString("uid", uid);
        editor.commit();

        Intent intent = new Intent(LoginActivity.this, ToDoMainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void loginFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()){
                    GoogleSignInAccount account = result.getSignInAccount();
                    firebaseAuthWithGoogle(account);
                    Toast.makeText(this, account.getEmail(), Toast.LENGTH_SHORT).show();
                }else{
                    Log.i(TAG, "onActivityResult: "+result.getSignInAccount());
                }
            }
        }


    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        showProgressDialog(message);
        AuthCredential credential= GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "success...", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
            }
        });
    }

    private  void signIn()
    {
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