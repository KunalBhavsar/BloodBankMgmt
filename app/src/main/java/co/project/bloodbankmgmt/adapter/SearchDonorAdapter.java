package co.project.bloodbankmgmt.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import co.project.bloodbankmgmt.R;
import co.project.bloodbankmgmt.models.User;

/**
 * Adapter for service list
 * Created by SuperCoders
 */

public class SearchDonorAdapter extends RecyclerView.Adapter<SearchDonorAdapter.ServiceHolder> {

    private Context mContext;
    private List<User> mUserList;

    public SearchDonorAdapter(Context context) {
        mContext = context;
    }

    @Override
    public SearchDonorAdapter.ServiceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_item_donar_details, parent, false);
        return new ServiceHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchDonorAdapter.ServiceHolder holder, int position) {
        holder.txtName.setText(mUserList.get(position).getFullname());
        holder.txtAddress.setText(mUserList.get(position).getAddress());
        holder.txtCall.setText(mUserList.get(position).getMobileNumber());

    }

    @Override
    public int getItemCount() {
        return mUserList == null ? 0 : mUserList.size();
    }


    /**
     * Notifies the list of adapter
     */
    public void setData(List<User> serviceList) {
        this.mUserList = serviceList;
        notifyDataSetChanged();
    }

    class ServiceHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtAddress, txtCall;

        ServiceHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txt_name);
            txtAddress = (TextView) itemView.findViewById(R.id.txt_address);
            txtCall = (TextView) itemView.findViewById(R.id.txt_call);

        }
    }
}
