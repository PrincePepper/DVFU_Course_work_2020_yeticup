package semen.sereda.yeticup.ui.team.create_team;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CreateTeamViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CreateTeamViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}