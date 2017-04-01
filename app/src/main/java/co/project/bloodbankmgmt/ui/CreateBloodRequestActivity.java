package co.project.bloodbankmgmt.ui;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import co.project.bloodbankmgmt.R;
import co.project.bloodbankmgmt.adapter.BloodGroupAdapter;
import co.project.bloodbankmgmt.app.BloodBankApplication;
import co.project.bloodbankmgmt.models.BloodBankRequest;
import co.project.bloodbankmgmt.models.BloodGroup;
import co.project.bloodbankmgmt.utils.GeneralUtils;
import co.project.bloodbankmgmt.utils.SharedPrefUtils;

public class CreateBloodRequestActivity extends AppCompatActivity {

    private EditText edtRequestId;
    private EditText edtBloodGroup;
    private EditText edtQuantity;
    private Button btnSubmit;
    private BloodGroup bloodGroupSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_blood_request);
        edtRequestId = (EditText) findViewById(R.id.edt_request_id);
        edtBloodGroup = (EditText) findViewById(R.id.edt_blood_group);
        edtQuantity = (EditText) findViewById(R.id.edt_quantity);
        btnSubmit = (Button) findViewById(R.id.btn_submit);

        edtRequestId.setText(String.valueOf(((BloodBankApplication) getApplication()).getIdSets().getBloodRequestId() + 1));
        final Dialog bloodGroupSelctionDialog = GeneralUtils.createBloodGroupSingleSelectDialog(this
                , ((BloodBankApplication)getApplication()).getBloodGroupList(), new BloodGroupAdapter.OnBloodGroupSelectedListener() {
                    @Override
                    public void onBloodGroupSelected(BloodGroup bloodGroup) {
                        bloodGroupSelected = bloodGroup;
                        edtBloodGroup.setText(bloodGroupSelected.getTitle());
                    }
                });

        edtBloodGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bloodGroupSelctionDialog.show();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmitClicked();
            }
        });
    }

    private void onSubmitClicked() {
        String requestId = edtRequestId.getText().toString().trim();
        String quantity = edtQuantity.getText().toString().trim();

        boolean isValid = true;

        if (bloodGroupSelected == null) {
            isValid = false;
            edtBloodGroup.setError("This field is required");
        } else {
            edtBloodGroup.setError(null);
        }

        if (TextUtils.isEmpty(quantity)) {
            isValid = false;
            edtQuantity.setError("This field is required");
        } else {
            edtQuantity.setError(null);
        }

        if (!isValid) {
            return;
        }

        BloodBankRequest bloodBankRequest = new BloodBankRequest();
        bloodBankRequest.setBloodGroup(bloodGroupSelected.getId());
        bloodBankRequest.setId(Integer.parseInt(requestId));
        bloodBankRequest.setQuantity(Integer.parseInt(quantity));
        bloodBankRequest.setStatus("PENDING");
        bloodBankRequest.setUserId(SharedPrefUtils.getCurrentUser().getId());

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("variable").child("bloodRequests").child(bloodBankRequest.getId() + "").setValue(bloodBankRequest);
        mDatabase.child("variable").child("idSet").child("bloodRequestId").setValue(bloodBankRequest.getId());
        Toast.makeText(getApplicationContext(), "Blood request created successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}
