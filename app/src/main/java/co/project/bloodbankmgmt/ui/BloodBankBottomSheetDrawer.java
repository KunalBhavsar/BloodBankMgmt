package co.project.bloodbankmgmt.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import co.project.bloodbankmgmt.R;
import co.project.bloodbankmgmt.models.BloodBank;

/**
 * Created by Kunal on 02/04/17.
 */

public class BloodBankBottomSheetDrawer extends BottomSheetDialogFragment {

    private static final String TAG = BloodBankBottomSheetDrawer.class.getSimpleName();
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {}
    };

    @Override
    public void setupDialog(Dialog dialog, int style) {
        final View contentView = View.inflate(getContext(), R.layout.layout_blood_bank_status, null);
        dialog.setContentView(contentView);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("variable").child("bloodBank").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<BloodBank> bloodGroupList = new ArrayList<>();
                if(dataSnapshot.getChildrenCount() > 0) {
                    Iterable<DataSnapshot> childrenIterator = dataSnapshot.getChildren();
                    for (DataSnapshot children : childrenIterator) {
                        BloodBank bloodBank = children.getValue(BloodBank.class);
                        bloodGroupList.add(bloodBank);
                        switch (bloodBank.getBloodGroup()) {
                            case 1 :
                                ((TextView)contentView.findViewById(R.id.txt_bg_a_pos)).setText(String.valueOf(bloodBank.getQuantity()));
                                break;
                            case 2 :
                                ((TextView)contentView.findViewById(R.id.txt_bg_a_neg)).setText(String.valueOf(bloodBank.getQuantity()));
                                break;
                            case 3 :
                                ((TextView)contentView.findViewById(R.id.txt_bg_b_pos)).setText(String.valueOf(bloodBank.getQuantity()));
                                break;
                            case 4 :
                                ((TextView)contentView.findViewById(R.id.txt_bg_b_neg)).setText(String.valueOf(bloodBank.getQuantity()));
                                break;
                            case 5 :
                                ((TextView)contentView.findViewById(R.id.txt_bg_ab_pos)).setText(String.valueOf(bloodBank.getQuantity()));
                                break;
                            case 6 :
                                ((TextView)contentView.findViewById(R.id.txt_bg_ab_neg)).setText(String.valueOf(bloodBank.getQuantity()));
                                break;
                            case 7 :
                                ((TextView)contentView.findViewById(R.id.txt_bg_o_pos)).setText(String.valueOf(bloodBank.getQuantity()));
                                break;
                            case 8:
                                ((TextView)contentView.findViewById(R.id.txt_bg_o_neg)).setText(String.valueOf(bloodBank.getQuantity()));
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        });
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }
}
