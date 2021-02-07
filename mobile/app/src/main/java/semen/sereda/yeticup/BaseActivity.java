package semen.sereda.yeticup;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.redmadrobot.inputmask.MaskedTextChangedListener;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import java.util.regex.Pattern;

public class BaseActivity extends AppCompatActivity {
    public static final String APP_PREFERENCES_PEOPLE = "Person";
    public static final String APP_PREFERENCES_USER = "User";
    protected static final String MY_SETTINGS = "my_settings";
    protected static final String TAG_EMAIL = "EmailPasswordLogin";
    protected static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    //"(?=.*[a-zA-Z])" +      //any letter
                    //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 4 characters
                    "$");
    private static final int UI_ANIMATION_DELAY = 50;
    public static SharedPreferences sp2;
    public static SharedPreferences user_sp;
    protected static SharedPreferences sp;
    private final Handler mHideHandler = new Handler();
    View decorView;
    private final Runnable mHidePart2Runnable = () -> {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.

        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    };
    private final Runnable mShowPart2Runnable = () -> {
        // Delayed display of UI elements
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        decorView.setVisibility(View.VISIBLE);
    };

    public static void telephone(EditText editTextTelephone) {
        final MaskedTextChangedListener listener = MaskedTextChangedListener.Companion.installOn(editTextTelephone,
                "+7 ([000]) [000]-[00]-[00]", (maskFilled, extractedValue, formattedValue) -> {
                    Log.d("TAG", extractedValue);
                    Log.d("TAG", String.valueOf(maskFilled));
                }
        );
        editTextTelephone.setHint(listener.placeholder());
    }

    public static float dpToPx(final Context context, final float dp) { //перевод единицы dp в px
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public void hideSystemUI() {
        decorView = getWindow().getDecorView();
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    public void showSystemUI() {
        decorView = getWindow().getDecorView();
        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    public void KeyboardVisibilityEvent() {
        decorView = getWindow().getDecorView();
        KeyboardVisibilityEvent.setEventListener(this,
                isOpen -> {
                    // some code depending on keyboard visibility status
                    if (isOpen) {
                        showSystemUI();
                    } else {
                        hideSystemUI();
                    }
                });
    }

    public void hideKeyboard(AppCompatActivity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}