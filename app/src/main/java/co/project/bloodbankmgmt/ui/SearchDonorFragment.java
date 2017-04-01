package co.project.bloodbankmgmt.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import co.project.bloodbankmgmt.R;
import co.project.bloodbankmgmt.adapter.BloodGroupAdapter;
import co.project.bloodbankmgmt.adapter.SearchDonorAdapter;
import co.project.bloodbankmgmt.app.BloodBankApplication;
import co.project.bloodbankmgmt.models.BloodGroup;
import co.project.bloodbankmgmt.models.User;
import co.project.bloodbankmgmt.utils.ActivityUtils;
import co.project.bloodbankmgmt.utils.GeneralUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link SearchDonorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchDonorFragment extends Fragment {
    private static final String TAG = SearchDonorFragment.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private AppCompatButton btnSelectBloodGroup;

    private RecyclerView rvDonorList;
    private RelativeLayout rltProgress;
    private SearchDonorAdapter searchDonorAdapter;

    public SearchDonorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment SearchDonar.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchDonorFragment newInstance(String param1) {
        SearchDonorFragment fragment = new SearchDonorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_donar, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchDonorAdapter = new SearchDonorAdapter(getContext());
        btnSelectBloodGroup = (AppCompatButton) view.findViewById(R.id.btn_select_blood_group);
        rltProgress = (RelativeLayout) view.findViewById(R.id.rlt_progress);
        rvDonorList = (RecyclerView) view.findViewById(R.id.rv_donor_list);
        rvDonorList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvDonorList.setAdapter(searchDonorAdapter);

        btnSelectBloodGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BloodBankApplication bloodBankApplication = (BloodBankApplication) getActivity().getApplicationContext();
                GeneralUtils.createBloodGroupSingleSelectDialog(getActivity(), bloodBankApplication.getBloodGroupList(), new BloodGroupAdapter.OnBloodGroupSelectedListener() {
                    @Override
                    public void onBloodGroupSelected(BloodGroup bloodGroupSelected) {
                        btnSelectBloodGroup.setText(bloodGroupSelected.getTitle());
                        rltProgress.setVisibility(View.VISIBLE);

                        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                        Query query = database.child("variable").child("users").orderByChild("bloodGroup").equalTo(bloodGroupSelected.getId());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                rltProgress.setVisibility(View.GONE);
                                List<User> userList = new ArrayList<>();
                                if(dataSnapshot != null && dataSnapshot.getChildrenCount() > 0) {
                                    Iterable<DataSnapshot> childrenIterator = dataSnapshot.getChildren();

                                    for (DataSnapshot children : childrenIterator) {
                                        userList.add(children.getValue(User.class));
                                    }
                                    searchDonorAdapter.setData(userList);
                                } else {
                                    searchDonorAdapter.setData(userList);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e(TAG, databaseError.getMessage());
                                rltProgress.setVisibility(View.GONE);
                            }
                        });
                    }
                }).show();
            }
        });
    }
}
