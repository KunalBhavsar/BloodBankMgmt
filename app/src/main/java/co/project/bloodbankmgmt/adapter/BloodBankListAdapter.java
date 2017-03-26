package co.project.bloodbankmgmt.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.project.bloodbankmgmt.R;
import co.project.bloodbankmgmt.models.BloodGroups;
import co.project.bloodbankmgmt.ui.BloodBankListFragment.OnListFragmentInteractionListener;
import co.project.bloodbankmgmt.ui.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class BloodBankListAdapter extends RecyclerView.Adapter<BloodBankListAdapter.ViewHolder> {

    private final List<BloodGroups> mValues;
    private final OnListFragmentInteractionListener mListener;

    public BloodBankListAdapter(List<BloodGroups> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_blood_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        /*holder.mItem = mValues.get(position).;*/
        holder.mIdView.setText(mValues.get(position).getTitle());
        holder.mContentView.setText("Available stocks : " + mValues.get(position).getId());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.txt_category);
            mContentView = (TextView) view.findViewById(R.id.txt_service);
        }
    }
}
