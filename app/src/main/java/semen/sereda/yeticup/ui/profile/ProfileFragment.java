package semen.sereda.yeticup.ui.profile;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.redmadrobot.inputmask.MaskedTextChangedListener;

import semen.sereda.yeticup.R;

import static androidx.navigation.fragment.NavHostFragment.findNavController;

public class ProfileFragment extends Fragment {

    EditText editTextAddress, editTextDate, editTextEmail, editTextTelephone;
    boolean temp = false;
    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView exit_account = root.findViewById(R.id.exit_account);
        editTextTelephone = root.findViewById(R.id.editTextTelephone);
        telephone();
        editTextEmail = root.findViewById(R.id.editTextEmail);
        editTextDate = root.findViewById(R.id.editTextDate);
        date();
        editTextAddress = root.findViewById(R.id.editTextAddress);
        ActiDeactiFunc(false);
        Button change_info = root.findViewById(R.id.change_info);
        Button btn_stats = root.findViewById(R.id.btn_stats);
        change_info.setOnClickListener(v -> {
            if (temp) {
                if (validateForm()) return;
                ActiDeactiFunc(false);
                /// TODO здесь должна быть запись информации в бд
            } else ActiDeactiFunc(true);
            temp = !temp;
        });

        btn_stats.setOnClickListener(v -> {
            findNavController(this).navigate(R.id.action_navigation_profile_to_historyFragment, null);
        });

        exit_account.setOnClickListener(v -> {
            ///TODO здесь нужно выйти из аккаунта
        });

        return root;
    }

    private void ActiDeactiFunc(boolean a) {  // выключение/включение полей
        if (a) {
            editTextAddress.setEnabled(true);
            editTextDate.setEnabled(true);
            editTextEmail.setEnabled(true);
            editTextTelephone.setEnabled(true);
        } else {
            editTextAddress.setEnabled(false);
            editTextDate.setEnabled(false);
            editTextEmail.setEnabled(false);
            editTextTelephone.setEnabled(false);
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        if (TextUtils.isEmpty(editTextAddress.getText().toString())) {
            editTextAddress.setError("Заполните поле");
            valid = false;
        } else editTextAddress.setError(null);

        if (TextUtils.isEmpty(editTextEmail.getText().toString())) {
            editTextEmail.setError("Заполните поле");
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText().toString()).matches()) {
            editTextEmail.setError("Пожалуйста напишите почту верно.");
            valid = false;
        } else editTextEmail.setError(null);

        if (TextUtils.isEmpty(editTextDate.getText().toString())) {
            editTextAddress.setError("Заполните поле");
            valid = false;
        } else editTextAddress.setError(null);

        if (TextUtils.isEmpty(editTextTelephone.getText().toString())) {
            editTextAddress.setError("Заполните поле");
            valid = false;
        } else editTextAddress.setError(null);

        return !valid;
    }

    private void telephone() {
        final MaskedTextChangedListener listener = MaskedTextChangedListener.Companion.installOn(editTextTelephone,
                "+7 ([000]) [000]-[00]-[00]", (maskFilled, extractedValue, formattedValue) -> {
                    Log.d("TAG", extractedValue);
                    Log.d("TAG", String.valueOf(maskFilled));
                }
        );
        editTextTelephone.setHint(listener.placeholder());
    }

    private void date() {
        final MaskedTextChangedListener listener = MaskedTextChangedListener.Companion.installOn(editTextDate,
                "[00]{/}[00]{/}[0000]", (maskFilled, extractedValue, formattedValue) -> {
                    Log.d("TAG", extractedValue);
                    Log.d("TAG", String.valueOf(maskFilled));
                }
        );
        editTextDate.setHint(listener.placeholder());
    }

}