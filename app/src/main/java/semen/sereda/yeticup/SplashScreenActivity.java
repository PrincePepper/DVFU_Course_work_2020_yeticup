package semen.sereda.yeticup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import semen.sereda.yeticup.authorization.AuthorizationActivity;


public class SplashScreenActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        sp = getSharedPreferences(MY_SETTINGS, MODE_PRIVATE);
        // проверяем, первый ли раз открывается программа
        boolean hasVisited = sp.getBoolean("hasVisited", false);
        hideSystemUI();
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
}
