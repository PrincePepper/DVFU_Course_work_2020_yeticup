package semen.sereda.yeticup.ui.team.apply_team;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ApplyTeamModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ApplyTeamModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}