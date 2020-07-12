package com.example.mymy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.kakao.auth.Session;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    EditText id, pwd;
    Button button, mSignInBtn;

    public static CallbackManager callbackManager;
    LoginButton loginButton_facebook;
//    com.kakao.usermgmt.LoginButton loginButton_kakao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);


        //LoginManager.getInstance().logOut();

//        SessionCallback callback = new SessionCallback();
//        Session.getCurrentSession().addCallback(callback);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        button = findViewById(R.id.btn_1);
        mSignInBtn = findViewById(R.id.sign_in_button);
        id = findViewById(R.id.inputId);
        pwd = findViewById(R.id.inputPwd);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Pwd = pwd.getText().toString();
                String Id = id.getText().toString();
                if( Pwd.equals("ekqls") && Id.equals("db97828")) {
                    String contents = id.getText().toString();
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("id", contents);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), contents + "님이 로그인하셨습니다", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = (accessToken != null && !accessToken.isExpired());

        AppEventsLogger.activateApp(getApplication());

        callbackManager = CallbackManager.Factory.create();

        loginButton_facebook = (LoginButton) findViewById(R.id.login_button_facebook);
        loginButton_facebook.setReadPermissions("email");


        loginButton_facebook.setOnClickListener(new View.OnClickListener() {
            private ProfileTracker mProfileTracker;
            @Override
            public void onClick(View v) {
                loginButton_facebook.setReadPermissions("email");
                loginButton_facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile"));
                        //AccessToken accessToken = loginResult.getAccessToken();

                        Profile profile = Profile.getCurrentProfile();
                        if (profile == null) {
                                mProfileTracker = new ProfileTracker() {
                                    @Override
                                    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                                        mProfileTracker.stopTracking();
                                    }
                                };
                        }else{
                                String contents = String.valueOf(Profile.getCurrentProfile().getFirstName());
                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("id", contents);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), contents + " 님이 로그인하셨습니다", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        LoginManager.getInstance().logOut();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        LoginManager.getInstance().logOut();
                    }
                });
            }
        });
//        loginButton_facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                // App code
//                String contents = String .valueOf(Profile.getCurrentProfile().getFirstName());
//                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("id", contents);
//                startActivity(intent);
//                Toast.makeText(getApplicationContext(), contents + " 님이 로그인하셨습니다", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancel() {
//                // App code
//
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                // App code
//            }
//        });

//        LoginManager.getInstance().registerCallback(callbackManager,
//                new FacebookCallback<LoginResult>() {
//            private ProfileTracker mProfileTracker;
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//                        // App code
//                        LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile"));
//                        AccessToken accessToken = loginResult.getAccessToken();
//
//                        Profile profile = Profile.getCurrentProfile();
//                        if(profile == null) {
//                            mProfileTracker = new ProfileTracker() {
//                                @Override
//                                protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
//                                    mProfileTracker.stopTracking();
//                                }
//                            };
//                        }else {
//                            String name = accessToken.getUserId();
//                            String contents = String.valueOf(profile.getFirstName());
//                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            intent.putExtra("id", contents);
//                            startActivity(intent);
//                            Toast.makeText(getApplicationContext(), contents + " 님이 로그인하셨습니다", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        // App code
//                    }
//
//                    @Override
//                    public void onError(FacebookException exception) {
//                        // App code
//                    }
//                });

        //LoginManager.getInstance().logOut();

//        loginButton_kakao = findViewById(R.id.login_button_kakao);
//        loginButton_kakao.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Session session = Session.getCurrentSession();
//                session.addCallback(new SessionCallback());
//                session.open(AuthType.KAKAO_LOGIN_ALL, MainActivity.this);
//            }
//        });
//        UserManagement.requestLogout(new LogoutResponseCallback() {
//            @Override
//            public void onCompleteLogout() {
//                Toast.makeText(MainActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
       if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

    }

@Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if(AccessToken.getCurrentAccessToken() != null){
//            LoginManager.getInstance().logOut();
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        id.setText(null);
        pwd.setText(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //LoginManager.getInstance().logOut();
    }
}
