package co.project.bloodbankmgmt.app;

import android.app.Application;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.project.bloodbankmgmt.models.BloodGroups;
import co.project.bloodbankmgmt.models.IdSets;
import co.project.bloodbankmgmt.models.User;

/**
 * Created by Shraddha on 26/3/17.
 */

public class BloodBankApplication extends Application {

    private List<BloodGroups> bloodGroupList;
    private IdSets idSets;

    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        idSets = new IdSets();
        mDatabase.child("variable").child("idSet").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                idSets = dataSnapshot.getValue(IdSets.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

    public IdSets getIdSets() {
        return idSets;
    }
}
