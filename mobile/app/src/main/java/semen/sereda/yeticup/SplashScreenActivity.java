package semen.sereda.yeticup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;

import semen.sereda.yeticup.authorization.AuthorizationActivity;
import semen.sereda.yeticup.authorization.User;


public class SplashScreenActivity extends BaseActivity {
    public static ArrayList<User> UserList = new ArrayList<>();
    public static User ThisUser;
    public static String Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        sp = getSharedPreferences(MY_SETTINGS, MODE_PRIVATE);
        sp2 = getSharedPreferences(APP_PREFERENCES_PEOPLE, MODE_PRIVATE);
        user_sp = getSharedPreferences(APP_PREFERENCES_USER, MODE_PRIVATE);
        // проверяем, первый ли раз открывается программа
        boolean hasVisited = sp.getBoolean("hasVisited", false);
        hideSystemUI();
        new JsonGetTask().execute();
        if (!hasVisited) {
            // выводим нужную активность
            SharedPreferences.Editor e = sp.edit();
            e.putBoolean("hasVisited", true);
            e.apply(); // не забудьте подтвердить изменения
            startSplashAmimation();
        } else {

            startSplashAmimation2();
        }

    }

    private void startSplashAmimation2() {
        new Handler().postDelayed(() -> {
            Intent SplashIntent = new Intent(this, AuthorizationActivity.class);
            startActivity(SplashIntent);
            finish();
        }, 1500);
    }

    private void startSplashAmimation() {
        new Handler().postDelayed(() -> {
            Intent SplashIntent = new Intent(this, IntroActivity.class);
            startActivity(SplashIntent);
            finish();

        }, 3000);
    }

    public static class JsonParser {
        public void getUser(String response) throws JSONException {
            JSONArray userJson = new JSONArray(response);
            for (int i = 0; i < userJson.length(); i++) {
                JSONObject row = userJson.getJSONObject(i);
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
            }
        }
    }

    private class JsonGetTask extends AsyncTask<String, String, String> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            JsonParser jsonParser = new JsonParser();

            try {
                URL url = new URL("https://yetiapi.herokuapp.com/api/users/");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                String encoded = Base64.getEncoder().encodeToString(("yeti" + ":" + "yetiyeti").getBytes(StandardCharsets.UTF_8));

                connection.setRequestProperty("Authorization", "Basic " + encoded);
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    Log.e("AAAAAAAAAAA", line);
                    buffer.append(line).append("\n");
                }
                jsonParser.getUser(buffer.toString());
                return buffer.toString();
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
        protected void onPostExecute(String u) {
        }
    }
}
