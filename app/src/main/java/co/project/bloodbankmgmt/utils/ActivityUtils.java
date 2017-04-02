package co.project.bloodbankmgmt.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.List;

import co.project.bloodbankmgmt.R;
import co.project.bloodbankmgmt.adapter.SimpleDialogAdapter;
import co.project.bloodbankmgmt.models.BloodGroup;

/**
 * Created by Shraddha on 26/3/17.
 */

public class ActivityUtils {

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getWindow().getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getWindow().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void datePickerDialog(Context context, DateTimeInterface dateTimeInterface, long minDate) {
        final DateTimeInterface dateInterface = dateTimeInterface;
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.date_time_picker);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        final DatePicker dateWidget = (DatePicker) dialog.findViewById(R.id.datePicker);
        dateWidget.setMinDate(0);
        dateWidget.setMinDate(minDate);
        Log.e("calendar", "min date " + dateWidget.getMinDate());
        Button btnPositive = (Button) dialog.findViewById(R.id.confirm);
        Button btnNegative = (Button) dialog.findViewById(R.id.cancel);

        dateWidget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        btnPositive.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dateInterface.getDateTime(dateWidget.getYear(), dateWidget.getMonth(), dateWidget.getDayOfMonth());
                dialog.dismiss();
            }
        });
        btnNegative.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public interface DateTimeInterface {
        void getDateTime(int year, int month, int day);
    }

    public static void showBloodGroupDialog(Context context, List<BloodGroup> filterList, final OnDialogClickListener intf) {

        final Dialog dialog = getCustomDialog(context);
        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recycle_list_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        SimpleDialogAdapter simpleDialogAdapter = new SimpleDialogAdapter(new OnDialogClickListener() {
            @Override
            public void onClick(long selectionId, String selection) {
                dialog.dismiss();
                intf.onClick(selectionId, selection);
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

        void onClick(long selectionId, String bloodGroup);
    }
}
