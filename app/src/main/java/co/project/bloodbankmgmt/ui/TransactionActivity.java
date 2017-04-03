package co.project.bloodbankmgmt.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import co.project.bloodbankmgmt.R;
import co.project.bloodbankmgmt.adapter.BloodGroupAdapter;
import co.project.bloodbankmgmt.app.BloodBankApplication;
import co.project.bloodbankmgmt.models.BloodBank;
import co.project.bloodbankmgmt.models.BloodBankRequest;
import co.project.bloodbankmgmt.models.BloodGroup;
import co.project.bloodbankmgmt.utils.ActivityUtils;
import co.project.bloodbankmgmt.utils.GeneralUtils;

public class TransactionActivity extends AppCompatActivity {

    private static final String TAG = TransactionActivity.class.getSimpleName();
    private AppCompatButton btnSelectBloodGroup;
    private EditText edtOrderBy, edtShippingDate, edtQuantity, edtContact, edtAddress, edtOrderDate;
    private RadioGroup rdoGroup;
    private final int  IMPORT_BLOOD = 1;
    private final int  EXPORT_BLOOD = 2;
    private Button btnSave;
    private BloodGroup bloodGroup;
    private int transactionType;
    private Activity mActivityContext;
    private Context mAppContext;
    private BloodGroup bloodGroupSelected;
    private List<BloodBank> bloodBankList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        mActivityContext = this;
        mAppContext = getApplicationContext();
        initViews();
    }

    private void initViews() {

        btnSelectBloodGroup = (AppCompatButton) findViewById(R.id.btn_select_blood_group);
/*        edtOrderBy = (EditText) findViewById(R.id.edt_order_by);
        edtOrderDate = (EditText) findViewById(R.id.edt_order_date);
        edtShippingDate = (EditText) findViewById(R.id.edt_shipping_date);*/
        edtQuantity = (EditText) findViewById(R.id.edt_quantity);
//        edtContact = (EditText) findViewById(R.id.edt_mobile);
//        edtAddress = (EditText) findViewById(R.id.edt_address);
        rdoGroup = (RadioGroup) findViewById(R.id.radio_group);
        btnSave = (Button) findViewById(R.id.btn_submit);
        bloodBankList = new ArrayList<>();
        final Dialog bloodGroupSelctionDialog = GeneralUtils.createBloodGroupSingleSelectDialog(mActivityContext
                , ((BloodBankApplication)getApplication()).getBloodGroupList(), new BloodGroupAdapter.OnBloodGroupSelectedListener() {
                    @Override
                    public void onBloodGroupSelected(BloodGroup bloodGroupSelectedNew) {
                        bloodGroupSelected = bloodGroupSelectedNew;
                        btnSelectBloodGroup.setText(bloodGroupSelected.getTitle());
                    }
                });

        btnSelectBloodGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bloodGroupSelctionDialog.show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateFields();
            }
        });

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("variable").child("bloodBank").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<BloodBank> bloodBanks = new ArrayList<>();
                if(dataSnapshot.getChildrenCount() > 0) {
                    Iterable<DataSnapshot> childrenIterator = dataSnapshot.getChildren();
                    for (DataSnapshot children : childrenIterator) {
                        bloodBanks.add(children.getValue(BloodBank.class));
                    }
                    bloodBankList = bloodBanks;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        });
/*        edtShippingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.datePickerDialog(TransactionActivity.this, new ActivityUtils.DateTimeInterface() {
                    @Override
                    public void getDateTime(int year, int month, int day) {
                        edtShippingDate.setText(day + "/" + month + 1 + "/" + year);
                    }
                }, System.currentTimeMillis() - 1000);
            }
        });

        edtOrderDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.datePickerDialog(TransactionActivity.this, new ActivityUtils.DateTimeInterface() {
                    @Override
                    public void getDateTime(int year, int month, int day) {
                        edtOrderDate.setText(day + "/" + month + 1 + "/" + year);
                    }
                }, System.currentTimeMillis() - 1000);
            }
        });*/
    }

    public String getOrederBy() {
        return edtOrderBy.getText().toString().trim();
    }

    public String getAddress() {
        return edtAddress.getText().toString().trim();
    }

    public String getQuntity() {
        return edtQuantity.getText().toString().trim();
    }

    public String getContact() {
        return edtContact.getText().toString().trim();
    }

    public String getShippingDate() {
        return edtShippingDate.getText().toString().trim();
    }

    public String getOrederDate() {
        return edtOrderDate.getText().toString().trim();
    }

    private void validateFields() {

        ActivityUtils.hideKeyboard(this);
        if (TextUtils.isEmpty(getQuntity()) || bloodGroupSelected == null) {
            Snackbar.make(findViewById(R.id.root_transaction), "Please enter all the details", Snackbar.LENGTH_SHORT).show();
        }
        /*if (TextUtils.isEmpty(getOrederBy()) || TextUtils.isEmpty(getContact()) || TextUtils.isEmpty(getAddress()) || TextUtils.isEmpty(getQuntity())
                || TextUtils.isEmpty(getShippingDate()) || TextUtils.isEmpty(getOrederDate()) || bloodGroup == null) {
            Snackbar.make(findViewById(R.id.root_transaction), "Please enter all the details", Snackbar.LENGTH_SHORT).show();
        } else if (getContact().length() < 10) {
            Snackbar.make(findViewById(R.id.root_transaction), "Please enter valid mobile number", Snackbar.LENGTH_SHORT).show();
        }*/
        else {

            if(rdoGroup.getCheckedRadioButtonId() == R.id.rdo_import) {
                transactionType  = IMPORT_BLOOD;
            } else {
                transactionType = EXPORT_BLOOD;
            }
            updateStatusOfBloodRequest(bloodGroupSelected, Integer.parseInt(getQuntity()), transactionType);
/*            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            Query query = database.child("variable").child("bloodBank").orderByChild("bloodGroup").equalTo(bloodGroup.getId());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //rltProgress.setVisibility(View.GONE);

                    if (dataSnapshot != null && dataSnapshot.getChildrenCount() > 0) {
                        Iterable<DataSnapshot> childrenIterator = dataSnapshot.getChildren();

                        for (DataSnapshot children : childrenIterator) {
                            BloodBank bloodBank = children.getValue(BloodBank.class);
                            if(bloodBank.getQuantity() < 1 && transactionType == EXPORT_BLOOD) {
                                Snackbar.make(findViewById(R.id.root_transaction), "Blood Group not available. Export rejected.", Snackbar.LENGTH_SHORT).show();
                            } else if(transactionType == EXPORT_BLOOD) {
                                // TODO: 2/4/17 reduce quantity by 1
                            } else if(transactionType == IMPORT_BLOOD) {
                                // TODO: 2/4/17 add quantity by 1

                            }
                        }

                    } else {
                        if(transactionType == EXPORT_BLOOD) {
                            Snackbar.make(findViewById(R.id.root_transaction), "Blood Group not available. Export rejected.", Snackbar.LENGTH_SHORT).show();
                        } else {
                            // TODO: 2/4/17 add blood group value in db
                            FirebaseDatabase.getInstance().getReference().child("variable").child("bloodBank").setValue(bloodGroup);
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //Log.e(TAG, databaseError.getMessage());
                    //rltProgress.setVisibility(View.GONE);
                }
            });*/
        }
    }

    private void updateStatusOfBloodRequest(BloodGroup bloodGroup, int quantity, int transationType) {
        for (BloodBank bloodBank : bloodBankList) {
            if (bloodBank.getBloodGroup() == bloodGroup.getId()) {
                if (transationType == EXPORT_BLOOD && bloodBank.getQuantity() < quantity) {
                    Toast.makeText(mActivityContext, "Not enough stock available for transaction", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (transationType == EXPORT_BLOOD) {
                        bloodBank.setQuantity(bloodBank.getQuantity() - quantity);
                    }else {
                        bloodBank.setQuantity(bloodBank.getQuantity() + quantity);
                    }
                    FirebaseDatabase.getInstance().getReference().child("variable").child("bloodBank").setValue(bloodBankList);

                    Toast.makeText(mActivityContext, "Transaction placed", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
}

