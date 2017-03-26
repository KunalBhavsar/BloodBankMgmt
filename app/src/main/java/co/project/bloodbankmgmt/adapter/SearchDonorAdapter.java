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

/**
 * Adapter for service list
 * Created by SuperCoders
 */

public class SearchDonorAdapter extends RecyclerView.Adapter<SearchDonorAdapter.ServiceHolder> {

    private Context mContext;
    private List<String> mServiceList;

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
        holder.txtName.setText("Name" + position);
       // holder.imgService.setImageResource(mServiceList.get(position).getImgId());
    }

   /* @Override
    public int getItemCount() {
        return mServiceList == null ? 0 : mServiceList.size();
    }*/

   @Override
    public int getItemCount() {
        return  5;
    }

    /**
     * Notifies the list of adapter
     */
    /*public void setData(List<ServiceEntity> serviceList) {
        this.mServiceList = serviceList;
        notifyDataSetChanged();
    }*/

    class ServiceHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtAddress, txtCall;
        ImageView imgService;

        ServiceHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txt_name);
            txtAddress = (TextView) itemView.findViewById(R.id.txt_address);
            txtCall = (TextView) itemView.findViewById(R.id.txt_call);

        }
    }
}
