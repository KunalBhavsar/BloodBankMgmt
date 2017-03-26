package co.project.bloodbankmgmt.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import co.project.bloodbankmgmt.R;
import co.project.bloodbankmgmt.adapter.ViewPagerAdapter;
import co.project.bloodbankmgmt.app.BloodBankApplication;
import co.project.bloodbankmgmt.models.BloodGroups;
import co.project.bloodbankmgmt.models.User;
import co.project.bloodbankmgmt.ui.dummy.DummyContent;

import static android.R.attr.password;

public class HomeScreenActivity extends AppCompatActivity implements BloodBankListFragment.OnListFragmentInteractionListener {

    private static final String SEARCH_DONOR = "SEARCH_DONOR";
    private static final String BLOOD_BANK_LIST = "BLOOD_BANK_LIST";
    protected TabLayout tabLayout;
    protected ViewPager viewPager;

    protected Toolbar toolbar;

    private ViewPagerAdapter viewPagerAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setupTabs();

        getBloodGroupData();
    }

    private void getBloodGroupData() {
        progressDialog.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.cancel();
                List<BloodGroups> bloodGroupList = new ArrayList<>();
                if(dataSnapshot.getChildrenCount() > 0) {
                    Iterable<DataSnapshot> childrenIterator = dataSnapshot.child("master").child("bloodGroups").getChildren();
                   // Iterable<DataSnapshot> childrenIterator = dataSnapshot.getC
                    for (DataSnapshot children : childrenIterator) {
                        bloodGroupList.add(children.getValue(BloodGroups.class));
                    }

                    BloodBankApplication bloodBankApplication = (BloodBankApplication) getApplicationContext();
                    bloodBankApplication.setBloodGroupList(bloodGroupList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.show();
                Log.e("Home screen", databaseError.getMessage());
            }
        });
    }

    private void initViews() {
        tabLayout  = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.container);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading");
    }

    private void setupTabs() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        setupViewPagerTabs();
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    /**
     * Method used to setup view pagers
     */
    private void setupViewPagerTabs() {
        viewPagerAdapter.addFragment(SearchDonorFragment.newInstance(SEARCH_DONOR), SEARCH_DONOR);
        viewPagerAdapter.addFragment(BloodBankListFragment.newInstance(1), BLOOD_BANK_LIST);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
