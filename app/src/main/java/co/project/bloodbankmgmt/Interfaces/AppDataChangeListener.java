package co.project.bloodbankmgmt.Interfaces;

import java.util.List;

import co.project.bloodbankmgmt.models.BloodGroup;
import co.project.bloodbankmgmt.models.IdSets;
import co.project.bloodbankmgmt.models.User;

/**
 * Created by Kunal on 02/04/17.
 */

public interface AppDataChangeListener {
    void onBloodGroupDataUpdated(List<BloodGroup> bloodGroupList);
    void onUserDataUpdated(List<User> userList);
    void onIdSetDataUpdated(IdSets idSets);
}
