package com.example.mymy;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private EditText mEtSignId, mEtSignPw, mEtSignPh;
    private TextView mTvSignId, mTvSignPw, mTvSignPh, mTvSignIn;

    private CheckBox mCheckAll, mCheck14, mCheckService, mCheckPrivacy, mCheckAlarm;

    private boolean isAll, is14, isSv, isP, isA;

    private String id, pw, ph;

    private ImageView mBackBtn;

    //로그인 모듈 변수
    private FirebaseAuth mAuth;

    ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        //로그인
        mAuth = FirebaseAuth.getInstance();

        mProgress = new ProgressDialog(this);

        //초기 설정
        mBackBtn = findViewById(R.id.iv_sign_back);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTvSignIn = findViewById(R.id.tv_btn_signin);
        mTvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                id = mEtSignId.getText().toString();
                pw = mEtSignPw.getText().toString();
                ph = mEtSignPh.getText().toString();

                signStart(id,pw,ph);
//                Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });


        mEtSignId = findViewById(R.id.et_sign_id);
        mEtSignPw = findViewById(R.id.et_sign_pw);
        mEtSignPh = findViewById(R.id.et_sign_ph);
        mTvSignId = findViewById(R.id.tv_signIn_id);
        mTvSignPw = findViewById(R.id.tv_sign_pw);
        mTvSignPh = findViewById(R.id.tv_sign_phone);

        id = mEtSignId.getText().toString();
        pw = mEtSignPw.getText().toString();
        ph = mEtSignPh.getText().toString();

        mCheckAll = findViewById(R.id.checkbox_all);
        mCheck14 = findViewById(R.id.checkbox_over_14);
        mCheckService = findViewById(R.id.checkbox_service_agree);
        mCheckPrivacy = findViewById(R.id.checkbox_privacy_agree);
        mCheckAlarm = findViewById(R.id.checkbox_alarm_agree);

        mCheckAll.setOnCheckedChangeListener(this);
        mCheck14.setOnCheckedChangeListener(this);
        mCheckService.setOnCheckedChangeListener(this);
        mCheckPrivacy.setOnCheckedChangeListener(this);
        mCheckAlarm.setOnCheckedChangeListener(this);

        mEtSignId.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!isEmail(mEtSignId.getText().toString())) {
                            mTvSignId.setText("올바른 이메일 형식이 아닙니다");
                            mTvSignId.setTextColor(R.color.colorError);
                        } else {
                            mTvSignId.setText("이메일");
                        }
                    }
                }
        );

        mEtSignPw.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (mEtSignPw.getText().toString().length() < 8) {
                            mTvSignPw.setText("8자리 이상 입력해주세요.");
                            mTvSignPw.setTextColor(R.color.colorError);
                        } else if (mEtSignPw.getText().toString().length() == 0) {
                            mTvSignPw.setText("비밀번호를 입력해주세요.");
                            mTvSignPw.setTextColor(R.color.colorError);
                        } else if (!isPw(mEtSignPw.getText().toString())) {
                            mTvSignPw.setText("영문,숫자,특수문자를 모두 포함해주세요.");
                            mTvSignPw.setTextColor(R.color.colorError);
                        } else {
                            mTvSignPw.setText("비밀번호");
                        }
                    }
                }
        );

        mEtSignPh.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (mEtSignPh.getText().toString().length() < 10) {
                            mTvSignPh.setText("휴대폰번호는 10자리 이상입니다.");
                            mTvSignPh.setTextColor(R.color.colorError);
                        } else {
                            mTvSignPh.setText("휴대폰번호");
                        }
                    }
                }
        );

    }

    public static boolean isEmail(String email) {
        boolean returnValue = false;
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if (m.matches()) {
            returnValue = true;
        }
        return returnValue;
    }

    public static boolean isPw(String pw) {
        boolean returnValue = false;
        String regex = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%^*#?&]).{8,20}.$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(pw);
        if (m.matches()) {
            returnValue = true;
        }
        return returnValue;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.checkbox_all:
                isAll = isChecked;
                if (isChecked) {
                    if (!mCheck14.isChecked())
                        mCheck14.setChecked(isChecked);
                    if (!mCheckService.isChecked())
                        mCheckService.setChecked(isChecked);
                    if (!mCheckPrivacy.isChecked())
                        mCheckPrivacy.setChecked(isChecked);
                    if (!mCheckAlarm.isChecked())
                        mCheckAlarm.setChecked(isChecked);
                } else {
                    if (mCheckAll.isPressed()) {
                        if (!isAll) {
                            mCheck14.setChecked(isChecked);
                            mCheckService.setChecked(isChecked);
                            mCheckPrivacy.setChecked(isChecked);
                            mCheckAlarm.setChecked(isChecked);
                        } else if (isAll) {
                            mCheck14.setChecked(false);
                            mCheckService.setChecked(false);
                            mCheckPrivacy.setChecked(false);
                            mCheckAlarm.setChecked(false);
                        }
                    }
                }
            case R.id.checkbox_over_14:
                is14 = isChecked;
                if (is14 && isP && isSv && isA) {
                    mCheckAll.setChecked(true);
                } else {
                    mCheckAll.setChecked(false);
                }
                break;
            case R.id.checkbox_service_agree:
                isSv = isChecked;
                if (is14 && isP && isSv && isA) {
                    mCheckAll.setChecked(true);
                } else {
                    mCheckAll.setChecked(false);
                }
                break;
            case R.id.checkbox_privacy_agree:
                isP = isChecked;
                if (is14 && isP && isSv && isA) {
                    mCheckAll.setChecked(true);
                } else {
                    mCheckAll.setChecked(false);
                }
                break;
            case R.id.checkbox_alarm_agree:
                isA = isChecked;
                if (is14 && isP && isSv && isA) {
                    mCheckAll.setChecked(true);
                } else {
                    mCheckAll.setChecked(false);
                }
                break;
        }
    }

    //회원가입
    public void signStart(final String id, final String pw, final String ph){
        mProgress.setMessage("등록중입니다. 잠시만 기다려 주세요");
        mProgress.show();
        mAuth.createUserWithEmailAndPassword(id, pw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mProgress.dismiss();
                if(!task.isSuccessful()) {
                    try {
                        throw task.getException();
                    } catch(FirebaseAuthWeakPasswordException e) {
                        Toast.makeText(SignInActivity.this,"비밀번호가 간단해요.." ,Toast.LENGTH_SHORT).show();
                    } catch(FirebaseAuthInvalidCredentialsException e) {
                        Toast.makeText(SignInActivity.this,"email 형식에 맞지 않습니다." ,Toast.LENGTH_SHORT).show();
                    } catch(FirebaseAuthUserCollisionException e) {
                        Toast.makeText(SignInActivity.this,"이미존재하는 email 입니다." ,Toast.LENGTH_SHORT).show();
                    } catch(Exception e) {
                        Toast.makeText(SignInActivity.this,"다시 확인해주세요.." ,Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SignInActivity.this, "가입 성공  " ,Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();

                    String uid = user.getUid();

                    HashMap<Object,String> hashMap = new HashMap<>();

                    hashMap.put("uid",uid);
                    hashMap.put("email",id);
                    hashMap.put("password",pw);
                    hashMap.put("phone",ph);

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("Users");
                    reference.child(uid).setValue(hashMap);

                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
    }

}
