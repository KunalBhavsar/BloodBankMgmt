package co.project.bloodbankmgmt.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import co.project.bloodbankmgmt.R;
import co.project.bloodbankmgmt.adapter.ViewPagerAdapter;
import co.project.bloodbankmgmt.ui.dummy.DummyContent;

public class HomeScreenActivity extends AppCompatActivity implements BloodBankListFragment.OnListFragmentInteractionListener {

    private static final String SEARCH_DONOR = "SEARCH_DONOR";
    private static final String BLOOD_BANK_LIST = "BLOOD_BANK_LIST";
    protected TabLayout tabLayout;
    protected ViewPager viewPager;

    protected Toolbar toolbar;

    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setupTabs();
    }

    private void initViews() {
        tabLayout  = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.container);
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
