package co.project.bloodbankmgmt.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import co.project.bloodbankmgmt.R;
import co.project.bloodbankmgmt.adapter.ViewPagerAdapter;
import co.project.bloodbankmgmt.utils.SharedPrefUtils;

public class HomeScreenActivity extends AppCompatActivity {

    private static final String SEARCH_DONOR = "SEARCH DONOR";
    private static final String BLOOD_BANK_LIST = "BLOOD REQUESTS";
    private static final String OTHER = "OTHER";
    protected TabLayout tabLayout;
    protected ViewPager viewPager;

    protected Toolbar toolbar;

    private ViewPagerAdapter viewPagerAdapter;
    private ProgressDialog progressDialog;
    private Activity mActivityContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivityContext = this;
        initViews();
        setupTabs();
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
        viewPagerAdapter.addFragment(BloodRequestListFragment.newInstance(), BLOOD_BANK_LIST);
        viewPagerAdapter.addFragment(OtherFragment.newInstance(), OTHER);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
