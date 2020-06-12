package com.example.aimeliveapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aimeliveapp.network_calls.RestApis;
import com.example.aimeliveapp.network_calls.getchekuser.GetUserDetail;
import com.example.aimeliveapp.network_calls.getchekuser.GetUserDetailsResponse;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "MYTAG";
    public static final String DIPLAYNAME = "DIPLAYNAME";
    public static final String LOGIN_ID = "LOGIN_ID";
    private LoginButton loginButton;
    private FirebaseAuth auth;
    private SignInButton button;
    private Button btn_GoogleSignin;
    private FirebaseAuth.AuthStateListener authStateListener;
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    public static final int RC_SIGN_IN = 2;
    RestApis restApis;
    String name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        intiviews();
        googleLogin();

    }


    private void googleLogin() {
        button = findViewById(R.id.googleBtn);
        btn_GoogleSignin = findViewById(R.id.btn_GoogleSignin);
        btn_GoogleSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    //                    Toast.makeText(LoginActivity.this, "User Alreayd logged in", Toast.LENGTH_SHORT).show();
                } else {
                    //                    Toast.makeText(LoginActivity.this, "User Not Logged in", Toast.LENGTH_SHORT).show();
                }
            }
        };
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void intiviews() {
        restApis = RestApis.retrofit.create(RestApis.class);
        auth = FirebaseAuth.getInstance();
        AppEventsLogger.activateApp(getApplication(), "540663299920280");
        callbackManager = CallbackManager.Factory.create();

        loginButton = findViewById(R.id.login_button);

        loginButton.setPermissions(Arrays.asList("email"));

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
//                Toast.makeText(LoginActivity.this, "User ID: " + loginResult.getAccessToken().getUserId(), Toast.LENGTH_SHORT).show();
                getLoginDetails(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(LoginActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(LoginActivity.this, "OnError" + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loginWithFB(View view) {
        loginButton.performClick();
    }


    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    private void getLoginDetails(AccessToken accessToken) {

        final AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());
        final String userId = accessToken.getUserId();
        SharedPreferences sharedPref = LoginActivity.this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(LOGIN_ID, userId);
        editor.apply();

        Log.d(TAG, "getLoginDetails: " + userId);
        auth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    updateUI(firebaseUser, userId);
                } else {
                    Toast.makeText(LoginActivity.this, "Could not registered the user:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUI(FirebaseUser firebaseUser, String login_id) {
//        String email = firebaseUser.getEmail();
//        String displayName = firebaseUser.getDisplayName();
//        Uri photoUrl = firebaseUser.getPhotoUrl();
//
//        String path = null;
//        if (photoUrl != null) {
//
//            path = photoUrl.getPath();
//
//        }

//        Toast.makeText(this, "Email : " + email, Toast.LENGTH_SHORT).show();


//        if (displayName != null) {
        getCheckuser(name, login_id);


//        } else {
//            Toast.makeText(this, "Your Profile does not have public E-mail or Name", Toast.LENGTH_LONG).show();
//        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("MYTAG", "Google sign in failed" + e);
                Toast.makeText(this, "Eror : " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        final String id = acct.getId();
        try {
            name = acct.getDisplayName();
//            Toast.makeText(this, "" + name, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        SharedPreferences sharedPref = LoginActivity.this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(LOGIN_ID, id);
//        Toast.makeText(this, "Google ID: " + id, Toast.LENGTH_SHORT).show();
        editor.apply();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            updateUI(user, id);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null, id);
                        }

                        // ...
                    }
                });
    }

    void getCheckuser(String userName, final String userID) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Processing data...");
        progressDialog.show();
        if (name == null) {
            name = "Update Name";
        }
        restApis.getCheckUser(userID, name).enqueue(new Callback<GetUserDetailsResponse>() {
            @Override
            public void onResponse(Call<GetUserDetailsResponse> call, Response<GetUserDetailsResponse> response) {

                try {
                    progressDialog.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getStatus()) {
                            List<GetUserDetail> list = response.body().getGetUserDetails();
                            for (GetUserDetail userDetail : list) {
                                SharedPreferences sharedPref = LoginActivity.this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString(DIPLAYNAME, "displayName");
                                editor.apply();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                LoginManager.getInstance().logOut();
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, "Exceptions: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
            @Override
            public void onFailure(Call<GetUserDetailsResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}
