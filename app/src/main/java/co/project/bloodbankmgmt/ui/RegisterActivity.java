package co.project.bloodbankmgmt.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

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
import co.project.bloodbankmgmt.adapter.BloodGroupAdapter;
import co.project.bloodbankmgmt.app.BloodBankApplication;
import co.project.bloodbankmgmt.models.BloodGroup;
import co.project.bloodbankmgmt.models.User;
import co.project.bloodbankmgmt.utils.GeneralUtils;
import co.project.bloodbankmgmt.utils.SharedPrefUtils;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Context mAppContext;
    private RegisterActivity mActivityContext;

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
    private EditText edtBloodGroup;
    private CheckBox chkIsDonor;
    private Button btnRegister;
    private EditText edtPassword;

    private Calendar calendarDOB;

    private BloodGroup bloodGroupSelected;

    private boolean existingUser;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mActivityContext = this;
        mAppContext = getApplicationContext();

        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);

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
        edtBloodGroup = (EditText) findViewById(R.id.edt_blood_group);
        chkIsDonor = (CheckBox) findViewById(R.id.chk_is_donor);
        btnRegister = (Button) findViewById(R.id.btn_register);

        calendarDOB = Calendar.getInstance();

        existingUser = SharedPrefUtils.getInstance().get(SharedPrefUtils.IS_LOGGED_IN, false);
        if (existingUser) {
            user = SharedPrefUtils.getCurrentUser();
            edtDonorId.setText(user.getId() + "");
            edtFullname.setText(user.getFullname());
            if (user.getGender().equalsIgnoreCase("male")) {
                rdoMale.setChecked(true);
            }
            else if (user.getGender().equalsIgnoreCase("female")) {
                rdoFemale.setChecked(true);
            }
            edtMobileNumber.setText(user.getMobileNumber());
            edtPassword.setText(user.getPassword());
            edtAddress.setText(user.getAddress());
            edtEmailAddress.setText(user.getEmailAddress());
            calendarDOB.setTimeInMillis(user.getDob());
            String myFormat = "MM/dd/yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            edtDateOfBirth.setText(sdf.format(calendarDOB.getTime()));
            edtUsername.setText(user.getFullname());
            List<BloodGroup> bloodGroups = ((BloodBankApplication)getApplication()).getBloodGroupList();
            for (BloodGroup bloodGroup : bloodGroups) {
                if (bloodGroup.getId() == user.getBloodGroup()) {
                    bloodGroupSelected = bloodGroup;
                    edtBloodGroup.setText(bloodGroup.getTitle());
                }
            }
            chkIsDonor.setChecked(user.isDonor());
            btnRegister.setText("Update");
        }
        else {
            edtDonorId.setText(String.valueOf(((BloodBankApplication) mAppContext).getIdSets().getUserId() + 1));
        }
        final Dialog bloodGroupSelctionDialog = GeneralUtils.createBloodGroupSingleSelectDialog(mActivityContext
                , ((BloodBankApplication)getApplication()).getBloodGroupList(), new BloodGroupAdapter.OnBloodGroupSelectedListener() {
            @Override
            public void onBloodGroupSelected(BloodGroup bloodGroupSelected) {
                mActivityContext.bloodGroupSelected = bloodGroupSelected;
                edtBloodGroup.setText(bloodGroupSelected.getTitle());
            }
        });

        edtBloodGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bloodGroupSelctionDialog.show();
            }
        });
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendarDOB.set(Calendar.YEAR, year);
                calendarDOB.set(Calendar.MONTH, monthOfYear);
                calendarDOB.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "MM/dd/yyyy"; //In which you need put here
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

        boolean validData = true;

        if (TextUtils.isEmpty(fullname)) {
            validData = false;
            edtFullname.setError("This field is required");
        }
        else {
            edtFullname.setError(null);
        }
        if (TextUtils.isEmpty(mobileNumber)) {
            validData = false;
            edtMobileNumber.setError("This field is required");
        }
        else {
            edtMobileNumber.setError(null);
        }
        if (TextUtils.isEmpty(email)) {
            validData = false;
            edtEmailAddress.setError("This field is required");
        }
        else {
            edtEmailAddress.setError(null);
        }
        if (TextUtils.isEmpty(username)) {
            validData = false;
            edtUsername.setError("This field is required");
        }
        else {
            edtUsername.setError(null);
        }
        if (TextUtils.isEmpty(password)) {
            validData = false;
            edtPassword.setError("This field is required");
        }
        else {
            edtPassword.setError(null);
        }
        if (TextUtils.isEmpty(address)) {
            validData = false;
            edtAddress.setError("This field is required");
        }
        else {
            edtAddress.setError(null);
        }

        if (bloodGroupSelected == null) {
            validData = false;
            edtBloodGroup.setError("This field is required");
        }
        else {
            edtBloodGroup.setError(null);
        }
        if (!validData) {
            return;
        }

        user = new User();
        user.setId(Integer.parseInt(donorId));
        user.setAddress(address);
        user.setDob(dob);
        user.setFullname(fullname);
        user.setGender(gender);
        user.setMobileNumber(mobileNumber);
        user.setEmailAddress(email);
        user.setUsername(username);
        user.setPassword(password);
        user.setDonor(isDonor);
        user.setBloodGroup(bloodGroupSelected.getId());

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        if (existingUser) {
            mDatabase.child("variable").child("users").child(user.getId() + "").setValue(user);
        }
        else {
            mDatabase.child("variable").child("users").child(user.getId() + "").setValue(user);
            mDatabase.child("variable").child("idSet").child("userId").setValue(user.getId());
            Intent intent = new Intent();
            intent.putExtra("username", username);
            setResult(RESULT_OK, intent);
        }
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
}
