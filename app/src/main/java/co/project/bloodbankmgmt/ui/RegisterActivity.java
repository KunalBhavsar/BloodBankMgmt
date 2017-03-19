package co.project.bloodbankmgmt.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import co.project.bloodbankmgmt.R;
import co.project.bloodbankmgmt.models.User;

public class RegisterActivity extends AppCompatActivity {

    private Context mAppContext;
    private Activity mActivityContext;

    private View mProgressView;
    private View mRegisterFormView;

    private EditText edtDonorId;
    private EditText edtFullname;
    private RadioButton rdoMale;
    private RadioButton rdoFemale;
    private EditText edtMobileNumber;
    private EditText edtEmailAddress;
    private EditText edtDateOfBirth;
    private EditText edtUsername;
    private AutoCompleteTextView txtBloodGroup;
    private CheckBox chkIsDonor;
    private Button btnRegister;

    private User currentUser;

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
        edtEmailAddress = (EditText) findViewById(R.id.edt_email);
        edtDateOfBirth = (EditText) findViewById(R.id.edt_dob);
        edtUsername = (EditText) findViewById(R.id.edt_username);
        txtBloodGroup = (AutoCompleteTextView) findViewById(R.id.edt_blood_group);
        chkIsDonor = (CheckBox) findViewById(R.id.chk_is_donor);
        btnRegister = (Button) findViewById(R.id.btn_register);

        currentUser = new User();

        edtDonorId.setText("12345");

        String[] bloodgroups ={"A +ve", "B +ve", "AB +ve", "O +ve", "A -ve", "B -ve", "AB -ve", "O -ve"};

        //Creating the instance of ArrayAdapter containing list of language names
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item, bloodgroups);

        txtBloodGroup.setThreshold(1);//will start working from first character
        txtBloodGroup.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                edtDateOfBirth.setText(sdf.format(myCalendar.getTime()));
            }
        };
        edtDateOfBirth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(mActivityContext, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
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

    class BloddGroupArrayAdapter extends ArrayAdapter {

        public BloddGroupArrayAdapter(Context context, int resource) {
            super(context, resource);
        }
    }
}
