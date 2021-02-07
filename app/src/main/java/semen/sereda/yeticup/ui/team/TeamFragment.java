package semen.sereda.yeticup.ui.team;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import semen.sereda.yeticup.R;

import static androidx.navigation.fragment.NavHostFragment.findNavController;

public class TeamFragment extends Fragment {

    private TeamViewModel teamViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        teamViewModel = new ViewModelProvider(this).get(TeamViewModel.class);
        View root = inflater.inflate(R.layout.fragment_team, container, false);

        teamViewModel.getText().observe(getViewLifecycleOwner(), s -> {
        });

        Button find = root.findViewById(R.id.btn_find_team);
        Button create = root.findViewById(R.id.btn_create_team);

        find.setOnClickListener(v -> {
            findNavController(this).navigate(R.id.action_navigation_team_to_findTeamFragment, null);
        });
        create.setOnClickListener(v -> {
            findNavController(this).navigate(R.id.action_navigation_team_to_createTeamFragment, null);
        });
        return root;
    }
}