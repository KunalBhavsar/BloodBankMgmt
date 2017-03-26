package co.project.bloodbankmgmt.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import java.util.List;

import co.project.bloodbankmgmt.R;
import co.project.bloodbankmgmt.adapter.SimpleDialogAdapter;
import co.project.bloodbankmgmt.models.BloodGroups;

/**
 * Created by Shraddha on 26/3/17.
 */

public class ActivityUtils {

    public static void showBloodGroupDialog(Context context, List<BloodGroups> filterList, final OnDialogClickListener intf) {

        final Dialog dialog = getCustomDialog(context);
        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recycle_list_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        SimpleDialogAdapter simpleDialogAdapter = new SimpleDialogAdapter(new OnDialogClickListener() {
            @Override
            public void onClick(long selection) {
                dialog.dismiss();
                intf.onClick(selection);
            }
        });
        recyclerView.setAdapter(simpleDialogAdapter);
        simpleDialogAdapter.setData(filterList);
        dialog.show();
    }

    private static Dialog getCustomDialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filter_selection_dialog);
        return dialog;
    }

    public interface OnDialogClickListener {

        void onClick(long selection);
    }
}
