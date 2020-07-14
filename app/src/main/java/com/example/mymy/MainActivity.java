package com.example.mymy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    EditText id, pwd;
    Button button, mSignInBtn;

    public static CallbackManager callbackManager;
    LoginButton loginButton_facebook;

    private BackPressCloseHandler backPressCloseHandler;


    //이메일 비밀번호 로그인 모듈 변수
    private FirebaseAuth mAuth;
    //현재 로그인 된 유저 정보를 담을 변수
    private FirebaseUser currentUser;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        backPressCloseHandler = new BackPressCloseHandler(this);

        //로그인 모듈 변수
        mAuth = FirebaseAuth.getInstance();


        button = findViewById(R.id.btn_1);
        mSignInBtn = findViewById(R.id.sign_in_button);
        id = findViewById(R.id.inputId);
        pwd = findViewById(R.id.inputPwd);

        mProgress = new ProgressDialog(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Id = id.getText().toString();
                String Pwd = pwd.getText().toString();
                if(Pwd.length() == 0 || Id.length() == 0) {
                    Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show();
                }else{
                    loginStart(Id, Pwd);
                }
//                if( Pwd.equals("ekqls") && Id.equals("db97828")) {
//                    String contents = id.getText().toString();
//                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.putExtra("id", contents);
//                    startActivity(intent);
//                    Toast.makeText(getApplicationContext(), contents + "님이 로그인하셨습니다", Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignInActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

//        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        boolean isLoggedIn = (accessToken != null && !accessToken.isExpired());
//
//        AppEventsLogger.activateApp(getApplication());
//
//        callbackManager = CallbackManager.Factory.create();
//
//        loginButton_facebook = (LoginButton) findViewById(R.id.login_button_facebook);
//        loginButton_facebook.setReadPermissions("email");
//
//
//        loginButton_facebook.setOnClickListener(new View.OnClickListener() {
//            private ProfileTracker mProfileTracker;
//            @Override
//            public void onClick(View v) {
//                loginButton_facebook.setReadPermissions("email");
//                loginButton_facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//                        // App code
//                        LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile"));
//                        //AccessToken accessToken = loginResult.getAccessToken();
//
//                        Profile profile = Profile.getCurrentProfile();
//                        if (profile == null) {
//                                mProfileTracker = new ProfileTracker() {
//                                    @Override
//                                    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
//                                        mProfileTracker.stopTracking();
//                                    }
//                                };
//                        }else{
//                                String contents = String.valueOf(Profile.getCurrentProfile().getFirstName());
//                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                intent.putExtra("id", contents);
//                                startActivity(intent);
//                                Toast.makeText(getApplicationContext(), contents + " 님이 로그인하셨습니다", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        // App code
//                        LoginManager.getInstance().logOut();
//                    }
//
//                    @Override
//                    public void onError(FacebookException exception) {
//                        // App code
//                        LoginManager.getInstance().logOut();
//                    }
//                });
//            }
//        });

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                }
            }
        };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//       if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
//            return;
//        }

    }

@Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            mAuth.signOut();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        id.setText(null);
        pwd.setText(null);

        if (firebaseAuthListener != null) {
            mAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //LoginManager.getInstance().logOut();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(MainActivity.this,"연결이 해제되었습니다",Toast.LENGTH_SHORT).show();
    }

    public void loginStart(final String email, String password){
        mProgress.setMessage("로그인 중입니다. 잠시 기다려 주세요");
        mProgress.show();

//        Toast.makeText(MainActivity.this,"loginStart 함수 안으로" ,Toast.LENGTH_SHORT).show();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mProgress.dismiss();
//                Toast.makeText(MainActivity.this,"mAuth. onComplete 함수" ,Toast.LENGTH_SHORT).show();
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, email + "로그인 성공",Toast.LENGTH_SHORT).show();
                    mAuth.addAuthStateListener(firebaseAuthListener);
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(MainActivity.this,"아이디와 비밀번호를 확인하세요." ,Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //뒤로가기 누를 때 실행
    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

}
