package co.project.bloodbankmgmt.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import co.project.bloodbankmgmt.R;
import co.project.bloodbankmgmt.app.BloodBankApplication;
import co.project.bloodbankmgmt.models.BloodBankRequest;
import co.project.bloodbankmgmt.models.BloodGroup;
import co.project.bloodbankmgmt.models.User;

/**
 * Created by Kunal on 02/04/17.
 */

public class BloodRequestAdapter extends RecyclerView.Adapter<BloodRequestAdapter.ViewHolder> {
    private Context context;
    private List<BloodBankRequest> requestList;
    private OnChildClickListener listener;

    private List<User> userList;
    private List<BloodGroup> bloodGroups;

    public BloodRequestAdapter(Context context, List<BloodBankRequest> bloodBankRequests, OnChildClickListener listener) {
        this.context = context;
        this.requestList = bloodBankRequests;
        this.listener = listener;
        this.userList = ((BloodBankApplication)context.getApplicationContext()).getUserList();
        this.bloodGroups = ((BloodBankApplication)context.getApplicationContext()).getBloodGroupList();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_blood_request, parent, false);
        return new BloodRequestAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(requestList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public void updateBloodGroupData(List<BloodGroup> bloodGroups) {
        this.bloodGroups.clear();
        this.bloodGroups.addAll(bloodGroups);
        notifyDataSetChanged();
    }

    public void updateUserData(List<User> users) {
        this.userList.clear();
        this.userList.addAll(users);
        notifyDataSetChanged();
    }

    public void updateDataSource(List<BloodBankRequest> request) {
        this.requestList.clear();
        this.requestList.addAll(request);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtRequested;
        TextView txtQuantity;
        TextView txtBloodGroup;
        TextView txtStatus;
        Button btnAccept;
        Button btnReject;
        public ViewHolder(View itemView) {
            super(itemView);
            txtRequested = (TextView) itemView.findViewById(R.id.txt_user_name_value);
            txtQuantity = (TextView) itemView.findViewById(R.id.txt_quantity_value);
            txtBloodGroup = (TextView) itemView.findViewById(R.id.txt_blood_group_value);
            txtStatus = (TextView) itemView.findViewById(R.id.txt_status);
            btnAccept = (Button) itemView.findViewById(R.id.btn_accept);
            btnReject = (Button) itemView.findViewById(R.id.btn_reject);
        }

        public void bind(final BloodBankRequest bloodBankRequest, final OnChildClickListener onChildClickListener) {
            for (BloodGroup bloodGroup : bloodGroups) {
                if (bloodGroup.getId() == bloodBankRequest.getBloodGroup()) {
                    txtBloodGroup.setText(bloodGroup.getTitle());
                    break;
                }
            }
            for (User user : userList) {
                if (user.getId() == bloodBankRequest.getUserId()) {
                    txtRequested.setText(user.getFullname());
                    break;
                }
            }
            txtQuantity.setText(String.valueOf(bloodBankRequest.getUserId()));
            txtStatus.setText(bloodBankRequest.getStatus());
            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onChildClickListener.onAcceptButtonClicked(bloodBankRequest);
                }
            });
            btnReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onChildClickListener.onRejectButtonClicked(bloodBankRequest);
                }
            });

        }
    }

    public interface OnChildClickListener {
        void onAcceptButtonClicked(BloodBankRequest bloodBankRequest);
        void onRejectButtonClicked(BloodBankRequest bloodBankRequest);
    }
}
