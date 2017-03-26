package co.project.bloodbankmgmt.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.project.bloodbankmgmt.R;
import co.project.bloodbankmgmt.models.BloodGroups;
import co.project.bloodbankmgmt.utils.ActivityUtils;

/**
 * Created by Shraddha on 29/11/16.
 */

public class SimpleDialogAdapter extends RecyclerView.Adapter<SimpleDialogAdapter.DialogHolder> {

    private List<BloodGroups> filterList;
    private ActivityUtils.OnDialogClickListener onDialogClickListener;

    public SimpleDialogAdapter(ActivityUtils.OnDialogClickListener onDialogClickListener) {
        this.filterList = new ArrayList<>();
        this.onDialogClickListener = onDialogClickListener;
    }

    public void setData(List<BloodGroups> filterList) {
        this.filterList = filterList;
        notifyDataSetChanged();
    }

    @Override
    public DialogHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dialog, parent, false);
        return new DialogHolder(view);
    }

    @Override
    public void onBindViewHolder(DialogHolder holder, int position) {
        holder.txtName.setText(filterList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return filterList == null ? 0 : filterList.size();
    }

    class DialogHolder extends RecyclerView.ViewHolder {

        TextView txtName;

        DialogHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txt_style);
            txtName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDialogClickListener.onClick(filterList.get(getAdapterPosition()).getId());
                }
            });
        }
    }
}
