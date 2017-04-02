package co.project.bloodbankmgmt.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import co.project.bloodbankmgmt.Interfaces.AppDataChangeListener;
import co.project.bloodbankmgmt.R;
import co.project.bloodbankmgmt.adapter.BloodRequestAdapter;
import co.project.bloodbankmgmt.app.BloodBankApplication;
import co.project.bloodbankmgmt.models.BloodBankRequest;
import co.project.bloodbankmgmt.models.BloodGroup;
import co.project.bloodbankmgmt.models.IdSets;
import co.project.bloodbankmgmt.models.User;

public class AdminHomeScreenActivity extends AppCompatActivity implements AppDataChangeListener {

    private static final String TAG = AdminHomeScreenActivity.class.getSimpleName();
    private Button btnBloodBank;
    private RecyclerView recyclerView;
    private FloatingActionButton fabManualTransations;

    private BloodRequestAdapter bloodRequestAdapter;

    private Activity mActivityContext;
    private Context mAppContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_screen);

        mActivityContext = this;
        mAppContext = getApplicationContext();

        btnBloodBank = (Button) findViewById(R.id.btn_blood_bank);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_requests);
        fabManualTransations = (FloatingActionButton) findViewById(R.id.fab_add_manual_transations);

        fabManualTransations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivityContext, TransactionActivity.class);
                mActivityContext.startActivity(intent);
            }
        });

        bloodRequestAdapter = new BloodRequestAdapter(mAppContext, new ArrayList<BloodBankRequest>(), new BloodRequestAdapter.OnChildClickListener() {
            @Override
            public void onAcceptButtonClicked(BloodBankRequest bloodBankRequest) {

            }

            @Override
            public void onRejectButtonClicked(BloodBankRequest bloodBankRequest) {

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(mActivityContext, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(bloodRequestAdapter);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("variable").child("bloodRequests").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<BloodBankRequest> bloodBankRequests = new ArrayList<>();
                if(dataSnapshot.getChildrenCount() > 0) {
                    Iterable<DataSnapshot> childrenIterator = dataSnapshot.getChildren();
                    for (DataSnapshot children : childrenIterator) {
                        bloodBankRequests.add(children.getValue(BloodBankRequest.class));
                    }
                    bloodRequestAdapter.updateDataSource(bloodBankRequests);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((BloodBankApplication)mAppContext).attach(this);
        bloodRequestAdapter.updateBloodGroupData(((BloodBankApplication)mAppContext).getBloodGroupList());
        bloodRequestAdapter.updateUserData(((BloodBankApplication)mAppContext).getUserList());
    }

    @Override
    protected void onPause() {
        ((BloodBankApplication)mAppContext).detach(this);
        super.onPause();
    }

    @Override
    public void onBloodGroupDataUpdated(List<BloodGroup> bloodGroupList) {
        bloodRequestAdapter.updateBloodGroupData(bloodGroupList);
    }

    @Override
    public void onUserDataUpdated(List<User> userList) {
        bloodRequestAdapter.updateUserData(userList);
    }

    @Override
    public void onIdSetDataUpdated(IdSets idSets) {

    }
}
