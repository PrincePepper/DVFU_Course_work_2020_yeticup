package semen.sereda.yeticup.authorization;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

import semen.sereda.yeticup.BaseActivity;
import semen.sereda.yeticup.R;

public class ResetPasswordActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_activity);
        KeyboardVisibilityEvent();
        hideSystemUI();
        ImageView imageView = findViewById(R.id.arrow_right);
        imageView.setOnClickListener(v -> finish());

        Button button = findViewById(R.id.button_next);
        button.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Письмо отправлено")
                    .setMessage("Письмо сброса пароля отправлено вам на почту")
                    .setPositiveButton("ОК", (dialog, id) -> {
                        // Закрываем окно
                        dialog.cancel();
                        finish();
                    });
            builder.create();
            builder.show();
        });
    }

}
