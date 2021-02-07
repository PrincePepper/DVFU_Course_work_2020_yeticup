package semen.sereda.yeticup.ui.profile;

import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.fragment.app.FragmentTransaction;

import com.redmadrobot.inputmask.MaskedTextChangedListener;

import de.hdodenhof.circleimageview.CircleImageView;
import semen.sereda.yeticup.BaseActivity;
import semen.sereda.yeticup.R;
import semen.sereda.yeticup.authorization.AuthorizationActivity;
import semen.sereda.yeticup.authorization.User;
import semen.sereda.yeticup.ui.profile.history.HistoryFragment;

public class ProfileFragment extends Fragment {

    EditText editTextAddress, editTextDate, editTextEmail, editTextTelephone;
    TextView textViewName;
    CircleImageView circleImageView;
    boolean temp = false;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        User yourUser = (User) getActivity().getIntent().getExtras().get("user");
        TextView exit_account = root.findViewById(R.id.exit_account);
        circleImageView = root.findViewById(R.id.profile_image);

        textViewName = root.findViewById(R.id.textViewName);
        editTextTelephone = root.findViewById(R.id.editTextTelephone);
        editTextEmail = root.findViewById(R.id.editTextEmail);
        editTextDate = root.findViewById(R.id.editTextDate);
        editTextAddress = root.findViewById(R.id.editTextAddress);

        textViewName.setText(yourUser.name);
        editTextAddress.setText(yourUser.address);
        editTextEmail.setText(yourUser.mail);
        editTextTelephone.setText(yourUser.phone);
        BaseActivity.telephone(editTextTelephone);
        date();
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
            Fragment frag2 = new HistoryFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.nav_host_fragment, frag2);
            ft.commit();
        });

        exit_account.setOnClickListener(v -> {
            SharedPreferences.Editor editor = BaseActivity.sp2.edit();
            editor.putBoolean(BaseActivity.APP_PREFERENCES_PEOPLE, false);
            editor.apply();
            Intent IntentLogin = new Intent(this.getActivity(), AuthorizationActivity.class);
            startActivity(IntentLogin);
            this.requireActivity().finish();
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