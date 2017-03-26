package co.project.bloodbankmgmt.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/** View Pager Adapter
 * Created by Shraddha on 25/1/17.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<String> fragmentTitle = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitle.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        if(!fragmentList.contains(fragment)) {
            fragmentList.add(fragment);
            fragmentTitle.add(title);
        }
    }
}
