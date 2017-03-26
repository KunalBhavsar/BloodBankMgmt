package co.project.bloodbankmgmt.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import java.util.List;

import co.project.bloodbankmgmt.adapter.BloodGroupAdapter;
import co.project.bloodbankmgmt.models.BloodGroup;
import co.project.bloodbankmgmt.ui.RegisterActivity;

/**
 * Created by Kunal on 26/03/17.
 */

public class GeneralUtils {

    public static AlertDialog createBloodGroupSingleSelectDialog(Context context, final List<BloodGroup> bloodGroups, final BloodGroupAdapter.OnBloodGroupSelectedListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Set the dialog title
        builder.setTitle("SELECT BLOOD GROUP")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setSingleChoiceItems(new BloodGroupAdapter(context, bloodGroups)
                        , 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                listener.onBloodGroupSelected(bloodGroups.get(i));
                                dialogInterface.dismiss();
                            }
                        });

        return builder.create();
    }
}
