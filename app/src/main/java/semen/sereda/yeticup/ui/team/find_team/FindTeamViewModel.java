package semen.sereda.yeticup.ui.team.find_team;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FindTeamViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FindTeamViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}