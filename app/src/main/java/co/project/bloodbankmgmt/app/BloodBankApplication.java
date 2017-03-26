package co.project.bloodbankmgmt.app;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import co.project.bloodbankmgmt.models.BloodGroups;

/**
 * Created by Shraddha on 26/3/17.
 */

public class BloodBankApplication extends Application {

    private List<BloodGroups> bloodGroupList;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public List<BloodGroups> getBloodGroupList() {
        if(bloodGroupList == null) {
            bloodGroupList = new ArrayList<>();
        }
        return bloodGroupList;
    }

    public void setBloodGroupList(List<BloodGroups> bloodGroupList) {
        this.bloodGroupList = bloodGroupList;
    }
}
