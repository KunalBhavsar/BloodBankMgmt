package co.project.bloodbankmgmt.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import co.project.bloodbankmgmt.R;
import co.project.bloodbankmgmt.app.BloodBankApplication;
import co.project.bloodbankmgmt.models.BloodBankRequest;
import co.project.bloodbankmgmt.models.BloodGroup;

/**
 * {@link RecyclerView.Adapter} that can display
 */
public class BloodRequestListAdapter extends RecyclerView.Adapter<BloodRequestListAdapter.ViewHolder> {

    private final List<BloodBankRequest> bloodBankRequests;
    private final List<BloodGroup> bloodGroups;
    private Context context;
    public BloodRequestListAdapter(Context context, List<BloodBankRequest> bloodBankRequests) {
        this.context = context;
        this.bloodBankRequests = bloodBankRequests;
        this.bloodGroups = ((BloodBankApplication)context.getApplicationContext()).getBloodGroupList();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_blood_request_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        /*holder.mItem = mValues.get(position).;*/
        BloodBankRequest bloodBankRequest = bloodBankRequests.get(position);
        holder.txtRequestId.setText(String.valueOf(bloodBankRequest.getId()));
        for (BloodGroup bloodGroup : bloodGroups) {
            if (bloodGroup.getId() == bloodBankRequest.getBloodGroup()) {
                holder.txtBloodGroup.setText(bloodGroup.getTitle());
                break;
            }
        }
        holder.txtQuantity.setText(String.valueOf(bloodBankRequest.getQuantity()));
        holder.txtStatus.setText(bloodBankRequest.getStatus());
    }

    @Override
    public int getItemCount() {
        return bloodBankRequests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView txtRequestId;
        public final TextView txtBloodGroup;
        public final TextView txtQuantity;
        public final TextView txtStatus;

        public ViewHolder(View view) {
            super(view);
            txtRequestId = (TextView) view.findViewById(R.id.txt_request_id);
            txtBloodGroup = (TextView) view.findViewById(R.id.txt_blood_group);
            txtQuantity = (TextView) view.findViewById(R.id.txt_blood_quantity);
            txtStatus = (TextView) view.findViewById(R.id.txt_status);
        }
    }
}
