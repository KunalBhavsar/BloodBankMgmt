package co.project.bloodbankmgmt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import co.project.bloodbankmgmt.models.BloodGroup;

/**
 * Created by Kunal on 26/03/17.
 */

public class BloodGroupAdapter extends BaseAdapter {
    private Context context;
    private List<BloodGroup> bloodGroups;
    public BloodGroupAdapter(Context context, List<BloodGroup> bloodGroups) {
        this.context = context;
        this.bloodGroups = bloodGroups;
    }

    public void updateDataSource(List<BloodGroup> bloodGroups) {
        this.bloodGroups.clear();
        this.bloodGroups.addAll(bloodGroups);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return bloodGroups.size();
    }

    @Override
    public Object getItem(int i) {
        return bloodGroups.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View row = view;

        if(row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(android.R.layout.simple_list_item_1, null);
        }

        TextView txtBloodGroup = (TextView)row.findViewById(android.R.id.text1);

        txtBloodGroup.setText(bloodGroups.get(position).getTitle());
        return row;
    }

    public interface OnBloodGroupSelectedListener {
        void onBloodGroupSelected(BloodGroup bloodGroupSelected);
    }
}
