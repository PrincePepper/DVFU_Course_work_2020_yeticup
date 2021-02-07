package semen.sereda.yeticup;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import semen.sereda.yeticup.authorization.AuthorizationActivity;


public class IntroActivity extends semen.sereda.yeticup.BaseActivity {
    private static float step = 0;
    private View view;
    private TextView headerTextView;
    private TextView contextTextView;
    private int a = 1;

    private static void moveDots(Context context, View view) {
        ValueAnimator va = ValueAnimator.ofFloat(step, step + dpToPx(context, 16));
        va.setDuration(500);
        va.addUpdateListener(animation -> view.setTranslationX((float) animation.getAnimatedValue()));
        va.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                step += view.getX();
            }
        });
        va.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_activity);
        hideSystemUI();
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        view = findViewById(R.id.anim_dot);
        headerTextView = findViewById(R.id.header_text);
        contextTextView = findViewById(R.id.context_text);

        LottieAnimationView lottie = findViewById(R.id.animation_view);
        LottieAnimationView lottie2 = findViewById(R.id.animation_view2);
        LottieAnimationView lottie3 = findViewById(R.id.animation_view3);
        lottie.setAnimation(R.raw.friendship);
        lottie2.setAnimation(R.raw.results);
        lottie3.setAnimation(R.raw.search);
        lottie2.setVisibility(View.INVISIBLE);
        lottie3.setVisibility(View.INVISIBLE);

        lottie.playAnimation();

        ImageView buttonView = findViewById(R.id.double_skip);
        buttonView.setOnClickListener(v -> {
            moveDots(semen.sereda.yeticup.IntroActivity.this, view);
            switch (a) {
                case 1:
                    contextTextView.setText(R.string.intro_text_2);
                    headerTextView.setText(R.string.intro_htext_2);
                    lottie.cancelAnimation();
                    lottie.setVisibility(View.INVISIBLE);
                    lottie2.setVisibility(View.VISIBLE);
                    lottie2.playAnimation();
                    a++;
                    break;
                case 2:
                    contextTextView.setText(R.string.intro_text_3);
                    headerTextView.setText(R.string.intro_htext_3);
                    lottie2.cancelAnimation();
                    lottie2.setVisibility(View.INVISIBLE);
                    lottie3.setVisibility(View.VISIBLE);
                    lottie3.playAnimation();
                    a++;
                    break;
                case 3:
                    Intent intent = new Intent(semen.sereda.yeticup.IntroActivity.this, AuthorizationActivity.class);
                    startActivity(intent);
                    finish();
            }
        });
    }
}