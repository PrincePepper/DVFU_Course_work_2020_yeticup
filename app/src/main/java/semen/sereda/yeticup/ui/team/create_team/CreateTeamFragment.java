package semen.sereda.yeticup.ui.team.create_team;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import semen.sereda.yeticup.R;

public class CreateTeamFragment extends Fragment {
    private CreateTeamViewModel createTeamViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        createTeamViewModel = new ViewModelProvider(this).get(CreateTeamViewModel.class);
        View root = inflater.inflate(R.layout.fragment_create_team, container, false);
        createTeamViewModel.getText().observe(getViewLifecycleOwner(), s -> {
        });
        Button createteam = root.findViewById(R.id.btn_create_team_now);
        TextView name_team = root.findViewById(R.id.editTextTeam);

        createteam.setOnClickListener(v -> {
            String name = name_team.getText().toString();
            /*TODO здесь должна быть поиск нашего название в бд и
               вывод если такое или нет, если нет то создаем такую команду*/
            snackBarEvent(false, root);
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void snackBarEvent(boolean a, View view) { // вывод сообщение что команда создана/существует
        Snackbar mySnackbar;
        if (a) {
            mySnackbar = Snackbar.make(view, "Команда создана", Snackbar.LENGTH_SHORT);
            mySnackbar.setBackgroundTint(getResources().getColor(R.color.lightgreen));
        } else {
            mySnackbar = Snackbar.make(view, "Такая команда уже существует", Snackbar.LENGTH_SHORT);
            mySnackbar.setBackgroundTint(getResources().getColor(R.color.red));
        }
        mySnackbar.show();
    }

}