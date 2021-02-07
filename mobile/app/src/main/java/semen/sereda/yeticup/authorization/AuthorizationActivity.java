package semen.sereda.yeticup.authorization;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import semen.sereda.yeticup.BaseActivity;
import semen.sereda.yeticup.MainActivity;
import semen.sereda.yeticup.R;

import static semen.sereda.yeticup.SplashScreenActivity.Email;
import static semen.sereda.yeticup.SplashScreenActivity.ThisUser;
import static semen.sereda.yeticup.SplashScreenActivity.UserList;

public class AuthorizationActivity extends BaseActivity implements View.OnClickListener {

    static float calculatedHeight = 0;
    protected User userCreator = null;
    private String Password;

    private ImageView mImageButtonNewReg;
    private TextView mtextFieldForgotPassword;

    private EditText mLogEmailField;
    private EditText mLogPasswordField;
    private Button mButtonlogin;

    private EditText mRegNameField;
    private EditText mRegEmailField;
    private EditText mRegTelField;
    private EditText mRegAdressField;
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
        mRegTelField = findViewById(R.id.regeditTextTelephone);
        mRegAdressField = findViewById(R.id.regeditTextAdress);
        mRegPasswordField = findViewById(R.id.regeditTextTextPassword);
        mRegPasswordRepeatField = findViewById(R.id.regeditTextTextPasswordRepeat);
        mButtonReg = findViewById(R.id.buttonReg);

        login_view = findViewById(R.id.authorization);

        mImageButtonNewReg.setOnClickListener(this);
        mButtonlogin.setOnClickListener(this);
        mButtonReg.setOnClickListener(this);
        mtextFieldForgotPassword.setOnClickListener(this);
        ActiDeactiFunc(false);
        telephone(mRegTelField);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (sp2.contains(APP_PREFERENCES_PEOPLE))
            if (sp2.getBoolean(APP_PREFERENCES_PEOPLE, false)) {
                updateUI(true, user_sp.getString(APP_PREFERENCES_USER, Email));
            }
    }

    private void ActiDeactiFunc(boolean a) {  //выключение/включение полей регистрации
        if (a) {
            mButtonlogin.setEnabled(false);
            mLogPasswordField.setEnabled(false);
            mLogEmailField.setEnabled(false);

            mRegNameField.setEnabled(true);
            mRegEmailField.setEnabled(true);
            mRegTelField.setEnabled(true);
            mRegAdressField.setEnabled(true);
            mRegPasswordField.setEnabled(true);
            mRegPasswordRepeatField.setEnabled(true);
            mButtonReg.setEnabled(true);
        } else {
            mButtonlogin.setEnabled(true);
            mLogPasswordField.setEnabled(true);
            mLogEmailField.setEnabled(true);

            mRegNameField.setEnabled(false);
            mRegEmailField.setEnabled(false);
            mRegTelField.setEnabled(false);
            mRegAdressField.setEnabled(false);
            mRegPasswordField.setEnabled(false);
            mRegPasswordRepeatField.setEnabled(false);
            mButtonReg.setEnabled(false);
        }
    }

    private boolean validateFormReg(String email, String password) {
        boolean valid = true;

        if (TextUtils.isEmpty(mRegNameField.getText().toString())) {
            mRegNameField.setError("Заполните поле");
            valid = false;
        } else mRegNameField.setError(null);

        if (TextUtils.isEmpty(email)) {
            mRegEmailField.setError("Заполните поле");
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mRegEmailField.setError("Пожалуйста напишите почту верно.");
            valid = false;
        } else mRegEmailField.setError(null);

        if (TextUtils.isEmpty(mRegTelField.getText().toString())) {
            mRegTelField.setError("Заполните поле");
            valid = false;
        } else mRegTelField.setError(null);

        if (TextUtils.isEmpty(mRegAdressField.getText().toString())) {
            mRegAdressField.setError("Заполните поле");
            valid = false;
        } else mRegAdressField.setError(null);

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

    private void LoginInAccount(String email, String password) {  // проверка входных данных и вход
        Log.d(TAG_EMAIL, "signIn_Email:" + email);
        if (validateFormLogin(email, password)) return;
        boolean tolerance = false;
        for (User user : UserList) {
            String email_user = user.mail;
            String password_user = user.password;
            if (email.equals(email_user) && password.equals(password_user)) {
                tolerance = true;
                break;
            }
        }
        updateUI(tolerance, email);
    }

    private void createAccount(String email, String password, String name, String address, String tel) {  // создание аккаунта
        Log.d(TAG_EMAIL, "createAccount:" + email);
        if (validateFormReg(email, password)) return;
        String temp_tel = tel.replaceAll("[+\\-() ]", "");
        userCreator = new User(name, password, email, address, temp_tel);
        new JsonPostTask().execute();
    }

    private void updateUI(boolean event, String email) {
        if (event) {
            SharedPreferences.Editor editor = sp2.edit();
            editor.putBoolean(APP_PREFERENCES_PEOPLE, true);
            editor.apply();
            SharedPreferences.Editor editor2 = user_sp.edit();
            editor2.putString(APP_PREFERENCES_USER, email);
            editor2.apply();
            for (User users : UserList) {
                if (email.equals(users.mail)) {
                    ThisUser = users;
                    break;
                }
            }
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("user", ThisUser);
            startActivity(intent);
            this.finish();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.buttonLog) {//кнопка "Войти"
            hideKeyboard(this);
            LoginInAccount(mLogEmailField.getText().toString(), mLogPasswordField.getText().toString());
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
            createAccount(mRegEmailField.getText().toString(), mRegPasswordField.getText().toString(), mRegNameField.getText().toString(), mRegAdressField.getText().toString(), mRegTelField.getText().toString());
        } else if (i == R.id.textFieldForgotPassword) { //кнопка "забыли пароль"
            hideKeyboard(this);
            Intent intent = new Intent(this, ResetPasswordActivity.class);
            startActivity(intent);
        }
    }


    private class JsonPostTask extends AsyncTask<String, String, String> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(String... strings) {
            Gson gson = new Gson();
            JsonParser jsonParser = new JsonParser();
            String user = gson.toJson(userCreator);
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("https://yetiapi.herokuapp.com/api/users/");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                String encoded = Base64.getEncoder().encodeToString(("yeti" + ":" + "yetiyeti").getBytes(StandardCharsets.UTF_8));
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Basic " + encoded);
                connection.connect();

                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(user.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();

                if (connection.getResponseCode() == 200 || connection.getResponseCode() == 201) {
                    InputStream stream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuilder buffer = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        Log.e("A", line);
                        buffer.append(line).append("\n");
                    }
                    jsonParser.getUser(buffer.toString());
                    return buffer.toString();
                } else {
                    return ("Failed : HTTP error code : " + connection.getResponseCode());
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return "{}";
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println(s);
        }
    }

    public class JsonParser {
        public void getUser(String response) throws JSONException {
            JSONObject row = new JSONObject(response);
            long id = row.getInt("id");
            String name = row.getString("name");
            String password = row.getString("password");
            String mail = row.getString("mail");
            String address = row.getString("address");
            String phone = row.getString("phone");
            String photo = row.getString("photo");
            String date = row.getString("date");
            Email = mail;
            UserList.add(new User(id, name, password, mail, address, phone, photo, date));
            updateUI(true, Email);
        }
    }
}