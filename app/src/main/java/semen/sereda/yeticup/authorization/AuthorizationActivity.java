package semen.sereda.yeticup.authorization;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;

import semen.sereda.yeticup.BaseActivity;
import semen.sereda.yeticup.MainActivity;
import semen.sereda.yeticup.R;

public class AuthorizationActivity extends BaseActivity implements View.OnClickListener {
    static float calculatedHeight = 0;
    private String Email;
    private String Password;

    private ImageView mImageButtonNewReg;
    private TextView mtextFieldForgotPassword;

    private EditText mLogEmailField;
    private EditText mLogPasswordField;
    private Button mButtonlogin;

    private EditText mRegNameField;
    private EditText mRegEmailField;
    private EditText mRegPasswordField;
    private EditText mRegPasswordRepeatField;
    private Button mButtonReg;

    private ConstraintLayout login_view;
    private boolean temp = true; //false - окно регистрации открыто, true - закрыто

    private static void moveUpLogin(View view) {
        ValueAnimator va = ValueAnimator.ofFloat(0, -calculatedHeight);
        va.setDuration(350);
        va.addUpdateListener(animation -> view.setTranslationY((float) animation.getAnimatedValue()));
        va.start();
    }

    private static void moveDownLogin(View view) {
        ValueAnimator va = ValueAnimator.ofFloat(-calculatedHeight, 0);
        va.setDuration(350);
        va.addUpdateListener(animation -> view.setTranslationY((float) animation.getAnimatedValue()));
        va.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_reg_activity);
        KeyboardVisibilityEvent();
        hideSystemUI();

        mImageButtonNewReg = findViewById(R.id.imageViewButton);
        mtextFieldForgotPassword = findViewById(R.id.textFieldForgotPassword);

        mLogEmailField = findViewById(R.id.logEditTextTextEmailAddress);
        mLogPasswordField = findViewById(R.id.logEditTextTextPassword);
        mButtonlogin = findViewById(R.id.buttonLog);

        mRegNameField = findViewById(R.id.editTextTextPersonName);
        mRegEmailField = findViewById(R.id.regeditTextTextEmailAddress);
        mRegPasswordField = findViewById(R.id.regeditTextTextPassword);
        mRegPasswordRepeatField = findViewById(R.id.regeditTextTextPasswordRepeat);
        mButtonReg = findViewById(R.id.buttonReg);

        login_view = findViewById(R.id.ggggg);

        LottieAnimationView lottie = findViewById(R.id.animation_view2);
        lottie.setProgress(0);
        lottie.setAnimation(R.raw.regist);
        lottie.playAnimation();

        mImageButtonNewReg.setOnClickListener(this);
        mButtonlogin.setOnClickListener(this);
        mButtonReg.setOnClickListener(this);
        mtextFieldForgotPassword.setOnClickListener(this);
        ActiDeactiFunc(false);
        sp2 = getSharedPreferences(APP_PREFERENCES_PEOPLE, MODE_PRIVATE);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (sp2.contains(APP_PREFERENCES_PEOPLE))
            if (!sp2.getBoolean(APP_PREFERENCES_PEOPLE, false))
                signOut();
    }

    public void signOut() {
//        FirebaseAuth.getInstance().signOut();
//        mGoogleSignInClient.signOut().addOnCompleteListener(this,
//                new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        updateUI(null);
//                    }
//                });
//        mAuth.signOut();
    }

    private void ActiDeactiFunc(boolean a) {  //выключение/включение полей регистрации
        if (a) {
            mButtonlogin.setEnabled(false);
            mLogPasswordField.setEnabled(false);
            mLogEmailField.setEnabled(false);

            mRegNameField.setEnabled(true);
            mRegEmailField.setEnabled(true);
            mRegPasswordField.setEnabled(true);
            mRegPasswordRepeatField.setEnabled(true);
            mRegPasswordRepeatField.setEnabled(true);
            mButtonReg.setEnabled(true);
        } else {
            mButtonlogin.setEnabled(true);
            mLogPasswordField.setEnabled(true);
            mLogEmailField.setEnabled(true);

            mRegNameField.setEnabled(false);
            mRegEmailField.setEnabled(false);
            mRegPasswordField.setEnabled(false);
            mRegPasswordRepeatField.setEnabled(false);
            mRegPasswordRepeatField.setEnabled(false);
            mButtonReg.setEnabled(false);
        }
    }

    private boolean validateFormReg(String email, String password) {
        boolean valid = true;

        if (TextUtils.isEmpty(mRegNameField.getText().toString())) {
            mRegEmailField.setError("Заполните поле");
            valid = false;
        } else mRegEmailField.setError(null);

        if (TextUtils.isEmpty(email)) {
            mRegEmailField.setError("Заполните поле");
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mRegEmailField.setError("Пожалуйста напишите почту верно.");
            valid = false;
        } else mRegEmailField.setError(null);

        if (mRegPasswordRepeatField.getText().toString().equals(password)) {
            if (TextUtils.isEmpty(password)) {
                mRegPasswordField.setError("Заполните поле");
                valid = false;
            } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
                mRegPasswordField.setError("Пароль слишком легкий");
                valid = false;
            } else {
                mRegPasswordField.setError(null);
            }
        } else mRegPasswordRepeatField.setError("Пароли не совпадают.");

        return !valid;
    }

    private boolean validateFormLogin(String email, String password) {
        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            mLogEmailField.setError("Field can't be empty.");
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mLogEmailField.setError("Please enter a valid email address");
            valid = false;
        } else {
            mLogEmailField.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            mLogPasswordField.setError("Field can't be empty.");
            valid = false;
        } else {
            mLogPasswordField.setError(null);
        }

        return !valid;
    }

    private void differentiateLink(String email, String password) {  // проверка входных данных и вход
        Log.d(TAG_EMAIL, "signIn_Email:" + email);
//        if (validateFormLogin(email, password)) return;
        updateUI(false);
    }

    private void createAccount(String email, String password) {  // создание аккаунта
        Log.d(TAG_EMAIL, "createAccount:" + email);
        if (validateFormReg(email, password)) return;

    }

    private void updateUI(boolean user) {
        if (!user) {
            SharedPreferences.Editor editor = sp2.edit();
            editor.putBoolean(APP_PREFERENCES_PEOPLE, true);
            editor.apply();
            Intent SplashIntent = new Intent(this, MainActivity.class);
            startActivity(SplashIntent);
            this.finish();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.buttonLog) {//кнопка "Войти"
            hideKeyboard(this);
            differentiateLink(mLogEmailField.getText().toString(), mLogPasswordField.getText().toString());
        } else if (i == R.id.imageViewButton) { //кнопка "+"
            hideKeyboard(this);
            calculatedHeight = login_view.getHeight();
            if (temp) {
                ActiDeactiFunc(true);
                moveUpLogin(login_view);
                mImageButtonNewReg.setImageResource(R.drawable.ic_next_step);
                mImageButtonNewReg.setRotation(180);
                temp = false;
            } else {
                ActiDeactiFunc(false);
                moveDownLogin(login_view);
                mImageButtonNewReg.setImageResource(R.drawable.ic_reg_step);
                temp = true;
            }

        } else if (i == R.id.buttonReg) { //кнопка "зарегистрироваться"
            hideKeyboard(this);
        } else if (i == R.id.textFieldForgotPassword) { //кнопка "забыли пароль"
            hideKeyboard(this);
            Intent intent = new Intent(this, ResetPasswordActivity.class);
            startActivity(intent);
        }
    }
}
