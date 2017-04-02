package co.project.bloodbankmgmt.app;

import android.app.Application;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import co.project.bloodbankmgmt.Interfaces.AppDataChangeListener;
import co.project.bloodbankmgmt.Interfaces.AppDataChangeNotifier;
import co.project.bloodbankmgmt.models.BloodGroup;
import co.project.bloodbankmgmt.models.IdSets;
import co.project.bloodbankmgmt.models.User;
import co.project.bloodbankmgmt.utils.SharedPrefUtils;

/**
 * Created by Shraddha on 26/3/17.
 */

public class BloodBankApplication extends Application implements AppDataChangeNotifier {
    private static final String TAG = BloodBankApplication.class.getSimpleName();
    private List<BloodGroup> bloodGroupList;
    private List<User> userList;
    private IdSets idSets;
    private List<AppDataChangeListener> listeners;

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPrefUtils.init(this);
        idSets = new IdSets();
        bloodGroupList = new ArrayList<>();
        userList = new ArrayList<>();
        listeners = new ArrayList<>();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("variable").child("idSet").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                idSets = dataSnapshot.getValue(IdSets.class);
                for (AppDataChangeListener listener : listeners) {
                    listener.onIdSetDataUpdated(idSets);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("master").child("bloodGroups").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<BloodGroup> bloodGroupList = new ArrayList<>();
                if(dataSnapshot.getChildrenCount() > 0) {
                    Iterable<DataSnapshot> childrenIterator = dataSnapshot.getChildren();
                    for (DataSnapshot children : childrenIterator) {
                        bloodGroupList.add(children.getValue(BloodGroup.class));
                    }
                    setBloodGroupList(bloodGroupList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference databaseReference = mDatabase.child("variable").child("users");
        databaseReference.orderByChild("admin").equalTo(false);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> userList = new ArrayList<>();
                if(dataSnapshot.getChildrenCount() > 0) {
                    Iterable<DataSnapshot> childrenIterator = dataSnapshot.getChildren();
                    for (DataSnapshot children : childrenIterator) {
                        userList.add(children.getValue(User.class));
                    }
                    setUserList(userList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        });
    }

    public List<BloodGroup> getBloodGroupList() {
        if(bloodGroupList == null) {
            bloodGroupList = new ArrayList<>();
        }
        return bloodGroupList;
    }

    public List<User> getUserList() {
        if(userList == null) {
            userList = new ArrayList<>();
        }
        return userList;
    }

    public void setBloodGroupList(List<BloodGroup> bloodGroupList) {
        this.bloodGroupList = bloodGroupList;
        for (AppDataChangeListener listener : listeners) {
            listener.onBloodGroupDataUpdated(bloodGroupList);
        }
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
        for (AppDataChangeListener listener : listeners) {
            listener.onUserDataUpdated(userList);
        }
    }

    public IdSets getIdSets() {
        return idSets;
    }

    @Override
    public void attach(AppDataChangeListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void detach(AppDataChangeListener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }
}
