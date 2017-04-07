package co.project.bloodbankmgmt.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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
import co.project.bloodbankmgmt.models.BloodBank;
import co.project.bloodbankmgmt.models.BloodBankRequest;
import co.project.bloodbankmgmt.models.BloodGroup;
import co.project.bloodbankmgmt.models.IdSets;
import co.project.bloodbankmgmt.models.User;
import co.project.bloodbankmgmt.utils.SharedPrefUtils;

public class AdminHomeScreenActivity extends AppCompatActivity implements AppDataChangeListener {

    private static final String TAG = AdminHomeScreenActivity.class.getSimpleName();
    private Button btnBloodBank;
    private ImageView btnLogout;
    private RecyclerView recyclerView;
    private FloatingActionButton fabManualTransations;

    private BloodRequestAdapter bloodRequestAdapter;

    private Activity mActivityContext;
    private Context mAppContext;

    private ProgressDialog progressDialog;
    private List<BloodBank> bloodBankList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_screen);

        mActivityContext = this;
        mAppContext = getApplicationContext();

        progressDialog = new ProgressDialog(mActivityContext);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();

        bloodBankList = new ArrayList<>();

        btnBloodBank = (Button) findViewById(R.id.btn_blood_bank);
        btnLogout = (ImageView) findViewById(R.id.btn_logout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_requests);
        fabManualTransations = (FloatingActionButton) findViewById(R.id.fab_add_manual_transations);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mActivityContext).setMessage("Are you sure you want to logout??").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPrefUtils.getInstance().resetSharedPrefsValue();
                        Intent intent = new Intent(mActivityContext, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        mActivityContext.finish();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
            }
        });

        btnBloodBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BloodBankBottomSheetDrawer sharePaymentOptionsDialog = new BloodBankBottomSheetDrawer();
                sharePaymentOptionsDialog.show((((AppCompatActivity)mActivityContext).getSupportFragmentManager()), TAG);
            }
        });

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
                updateStatusOfBloodRequest(bloodBankRequest, true);
            }

            @Override
            public void onRejectButtonClicked(BloodBankRequest bloodBankRequest) {
                updateStatusOfBloodRequest(bloodBankRequest, false);
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
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
                progressDialog.dismiss();
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("variable").child("bloodBank").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<BloodBank> bloodBanks = new ArrayList<>();
                if(dataSnapshot.getChildrenCount() > 0) {
                    Iterable<DataSnapshot> childrenIterator = dataSnapshot.getChildren();
                    for (DataSnapshot children : childrenIterator) {
                        bloodBanks.add(children.getValue(BloodBank.class));
                    }
                    bloodBankList = bloodBanks;
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

    private void updateStatusOfBloodRequest(BloodBankRequest bloodBankRequest, boolean accept) {
        progressDialog.show();
        if (accept) {
            for (BloodBank bloodBank : bloodBankList) {
                if (bloodBank.getBloodGroup() == bloodBankRequest.getBloodGroup()) {
                    if (bloodBank.getQuantity() < bloodBankRequest.getQuantity()) {
                        Toast.makeText(mActivityContext, "Not enough stock available for request", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        bloodBank.setQuantity(bloodBank.getQuantity() - bloodBankRequest.getQuantity());
                        FirebaseDatabase.getInstance().getReference().child("variable").child("bloodBank").setValue(bloodBankList);

                        bloodBankRequest.setStatus("ACCEPTED");
                        FirebaseDatabase.getInstance().getReference().child("variable").child("bloodRequests")
                                .child(bloodBankRequest.getId() + "").setValue(bloodBankRequest);
                        Toast.makeText(mActivityContext, "Request Accepted", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        else {
            bloodBankRequest.setStatus("REJECTED");
            FirebaseDatabase.getInstance().getReference().child("variable").child("bloodRequests")
                    .child(bloodBankRequest.getId() + "").setValue(bloodBankRequest);
            Toast.makeText(mActivityContext, "Request Rejected", Toast.LENGTH_SHORT).show();
        }
        bloodRequestAdapter.notifyDataSetChanged();
        progressDialog.dismiss();
    }
}
