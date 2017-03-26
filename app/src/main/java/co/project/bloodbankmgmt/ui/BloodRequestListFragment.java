package co.project.bloodbankmgmt.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import co.project.bloodbankmgmt.R;
import co.project.bloodbankmgmt.adapter.BloodRequestListAdapter;
import co.project.bloodbankmgmt.models.BloodBank;
import co.project.bloodbankmgmt.models.BloodBankRequest;
import co.project.bloodbankmgmt.models.User;
import co.project.bloodbankmgmt.utils.SharedPrefUtils;

/**
 * A fragment representing a list of Items.
 */
public class BloodRequestListFragment extends Fragment {
    private static final String TAG = BloodRequestListFragment.class.getSimpleName();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BloodRequestListFragment() {
    }

    public static BloodRequestListFragment newInstance() {
        return new BloodRequestListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blood_request_list, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set the adapter
        view.findViewById(R.id.fab_create_request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateBloodRequestActivity.class);
                getActivity().startActivity(intent);
            }
        });
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_blood_request);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("variable").child("bloodRequests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<BloodBankRequest> bloodBankRequests = new ArrayList<>();
                User currentUser = SharedPrefUtils.getCurrentUser();
                if(dataSnapshot.getChildrenCount() > 0) {
                    Iterable<DataSnapshot> childrenIterator = dataSnapshot.getChildren();
                    for (DataSnapshot children : childrenIterator) {
                        BloodBankRequest bloodBankRequest = children.getValue(BloodBankRequest.class);
                        if (bloodBankRequest.getUserId() == currentUser.getId()) {
                            bloodBankRequests.add(children.getValue(BloodBankRequest.class));
                        }
                    }
                }
                recyclerView.setAdapter(new BloodRequestListAdapter(getActivity(), bloodBankRequests));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("variable").child("bloodBank").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<BloodBank> bloodGroupList = new ArrayList<>();
                if(dataSnapshot.getChildrenCount() > 0) {
                    Iterable<DataSnapshot> childrenIterator = dataSnapshot.getChildren();
                    for (DataSnapshot children : childrenIterator) {
                        BloodBank bloodBank = children.getValue(BloodBank.class);
                        bloodGroupList.add(bloodBank);
                        switch (bloodBank.getBloodGroup()) {
                            case 1 :
                                ((TextView)view.findViewById(R.id.txt_bg_a_pos)).setText(String.valueOf(bloodBank.getQuantity()));
                                break;
                            case 2 :
                                ((TextView)view.findViewById(R.id.txt_bg_a_neg)).setText(String.valueOf(bloodBank.getQuantity()));
                                break;
                            case 3 :
                                ((TextView)view.findViewById(R.id.txt_bg_b_pos)).setText(String.valueOf(bloodBank.getQuantity()));
                                break;
                            case 4 :
                                ((TextView)view.findViewById(R.id.txt_bg_b_neg)).setText(String.valueOf(bloodBank.getQuantity()));
                                break;
                            case 5 :
                                ((TextView)view.findViewById(R.id.txt_bg_ab_pos)).setText(String.valueOf(bloodBank.getQuantity()));
                                break;
                            case 6 :
                                ((TextView)view.findViewById(R.id.txt_bg_ab_neg)).setText(String.valueOf(bloodBank.getQuantity()));
                                break;
                            case 7 :
                                ((TextView)view.findViewById(R.id.txt_bg_o_pos)).setText(String.valueOf(bloodBank.getQuantity()));
                                break;
                            case 8:
                                ((TextView)view.findViewById(R.id.txt_bg_o_neg)).setText(String.valueOf(bloodBank.getQuantity()));
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        });
    }
}
