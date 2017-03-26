package co.project.bloodbankmgmt.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import co.project.bloodbankmgmt.R;
import co.project.bloodbankmgmt.models.BloodGroup;
import co.project.bloodbankmgmt.models.User;

import static android.R.attr.data;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Context mAppContext;
    private Activity mActivityContext;

    private View mProgressView;
    private View mRegisterFormView;

    private EditText edtDonorId;
    private EditText edtFullname;
    private RadioButton rdoMale;
    private RadioButton rdoFemale;
    private EditText edtMobileNumber;
    private EditText edtAddress;
    private EditText edtEmailAddress;
    private EditText edtDateOfBirth;
    private EditText edtUsername;
    private AutoCompleteTextView txtBloodGroup;
    private CheckBox chkIsDonor;
    private Button btnRegister;
    private EditText edtPassword;

    private Calendar calendarDOB;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mActivityContext = this;
        mAppContext = getApplicationContext();

        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);

        showProgress(true);

        edtDonorId = (EditText) findViewById(R.id.edt_donor_id);
        edtFullname = (EditText) findViewById(R.id.edt_fullname);
        rdoMale = (RadioButton) findViewById(R.id.rdo_male);
        rdoFemale = (RadioButton) findViewById(R.id.rdo_female);
        edtMobileNumber = (EditText) findViewById(R.id.edt_mobile_number);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        edtAddress = (EditText) findViewById(R.id.edt_address);
        edtEmailAddress = (EditText) findViewById(R.id.edt_email);
        edtDateOfBirth = (EditText) findViewById(R.id.edt_dob);
        edtUsername = (EditText) findViewById(R.id.edt_username);
        txtBloodGroup = (AutoCompleteTextView) findViewById(R.id.edt_blood_group);
        chkIsDonor = (CheckBox) findViewById(R.id.chk_is_donor);
        btnRegister = (Button) findViewById(R.id.btn_register);

        edtDonorId.setText("12345");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("master").child("bloodGroups").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<BloodGroup> bloodGroupList = new ArrayList<BloodGroup>();
                if (dataSnapshot.getChildrenCount() > 0) {
                    Iterable<DataSnapshot> childrens = dataSnapshot.getChildren();
                    for (DataSnapshot children : childrens) {
                        bloodGroupList.add(children.getValue(BloodGroup.class));
                    }
                }

                BloodGroupArrayAdapter adapter = new BloodGroupArrayAdapter(mActivityContext,
                        android.R.layout.simple_list_item_1, bloodGroupList);
                txtBloodGroup.setAdapter(adapter);
                txtBloodGroup.setThreshold(1);

                txtBloodGroup.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (b) {
                            ((AutoCompleteTextView)view).showDropDown();
                        }
                        else {
                            ((AutoCompleteTextView)view).dismissDropDown();
                        }
                    }
                });
                showProgress(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error in fetching blood group data : " + databaseError.getMessage());

            }
        });

        calendarDOB = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendarDOB.set(Calendar.YEAR, year);
                calendarDOB.set(Calendar.MONTH, monthOfYear);
                calendarDOB.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                edtDateOfBirth.setText(sdf.format(calendarDOB.getTime()));
            }
        };
        edtDateOfBirth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(mActivityContext, date, calendarDOB
                        .get(Calendar.YEAR), calendarDOB.get(Calendar.MONTH),
                        calendarDOB.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegisterClicked();
            }
        });
    }

    private void onRegisterClicked() {
        String donorId = edtDonorId.getText().toString();
        String fullname = edtFullname.getText().toString();
        String gender = rdoMale.isChecked() ? "male" : "female";
        String mobileNumber = edtMobileNumber.getText().toString();
        String email = edtEmailAddress.getText().toString();
        long dob = calendarDOB.getTimeInMillis();
        String username = edtUsername.getText().toString();
        boolean isDonor = chkIsDonor.isChecked();
        String address = edtAddress.getText().toString();
        String password = edtPassword.getText().toString();

        //TODO: add blood group
        User user = new User();
        user.setAddress(address);
        user.setDob(dob);
        user.setFullname(fullname);
        user.setGender(gender);
        user.setMobileNumber(mobileNumber);
        user.setEmailAddress(email);
        user.setUsername(username);
        user.setPassword(password);
        user.setDonor(isDonor);
        user.setDonorId(Integer.parseInt(donorId));

        mDatabase.child("variable").child("users").setValue(user);
        finish();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    class BloodGroupArrayAdapter extends ArrayAdapter<BloodGroup> {
        Context context;
        int layoutResourceId;
        List<BloodGroup> data = null;

        public BloodGroupArrayAdapter(Context context, int resource, List<BloodGroup> data) {
            super(context, resource, data);
            this.layoutResourceId = resource;
            this.context = context;
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Nullable
        @Override
        public BloodGroup getItem(int position) {
            return data.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;

            if(row == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(layoutResourceId, null);
            }

            TextView txtBloodGroup = (TextView)row.findViewById(android.R.id.text1);

            txtBloodGroup.setText(data.get(position).getTitle());
            return row;
        }
    }
}
