package co.project.bloodbankmgmt.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

import co.project.bloodbankmgmt.R;
import co.project.bloodbankmgmt.models.User;
import co.project.bloodbankmgmt.utils.SharedPrefUtils;

/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final int REGISTER_ACTIVITY_INTENT = 1;
    private DatabaseReference mDatabase;

    // UI references.
    private EditText edtUsername;
    private EditText edtPassword;
    private View mProgressView;
    private View mLoginFormView;

    private Context mAppContext;
    private LoginActivity mActivityContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAppContext = getApplicationContext();
        mActivityContext = this;

        if (SharedPrefUtils.getInstance().get(SharedPrefUtils.IS_LOGGED_IN, false)) {
            if (SharedPrefUtils.getInstance().get(SharedPrefUtils.IS_ADMIN, false)) {
                Intent intent = new Intent(LoginActivity.this, AdminHomeScreenActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
                startActivity(intent);
                finish();
            }
        }

        // Set up the login form.
        edtUsername = (EditText) findViewById(R.id.edt_username);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        edtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = (Button) findViewById(R.id.btn_sign_in);
        TextView mRegisterButton = (TextView) findViewById(R.id.txt_register);
        mRegisterButton.setPaintFlags(mRegisterButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivityContext, RegisterActivity.class);
                startActivityForResult(intent, REGISTER_ACTIVITY_INTENT);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REGISTER_ACTIVITY_INTENT && resultCode == RESULT_OK) {
            String username = data.getStringExtra("username");
            edtUsername.setText(username);
            edtPassword.setFocusableInTouchMode(true);
            edtPassword.setFocusable(true);
            edtPassword.requestFocus();
            AppCompatActivity activity = mActivityContext;
            Snackbar snackbar = Snackbar.make(activity.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Successfully register, now enter your password to login.",
                    Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        edtUsername.setError(null);
        edtPassword.setError(null);

        // Store values at the time of the login attempt.
        String username = edtUsername.getText().toString();
        final String password = edtPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            edtPassword.setError(getString(R.string.error_invalid_password));
            focusView = edtPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            edtUsername.setError(getString(R.string.error_field_required));
            focusView = edtUsername;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            edtUsername.setError(getString(R.string.error_invalid_username));
            focusView = edtUsername;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            DatabaseReference firebaseUserDB = mDatabase.child("variable").child("users");
            firebaseUserDB.orderByChild("username").equalTo(username);
            firebaseUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User userSelected = null;
                    if (dataSnapshot.getChildrenCount() > 0) {
                        Iterable<DataSnapshot> childrens = dataSnapshot.getChildren();
                        for (DataSnapshot children : childrens) {
                            userSelected = children.getValue(User.class);
                            if (userSelected.getPassword().equals(password)) {
                                break;
                            }
                            else {
                                userSelected = null;
                            }
                        }
                    }

                    if (userSelected != null) {
                        SharedPrefUtils.getInstance().add(SharedPrefUtils.CURRENT_USER, new Gson().toJson(userSelected));
                        SharedPrefUtils.getInstance().add(SharedPrefUtils.IS_LOGGED_IN, true);
                        SharedPrefUtils.getInstance().add(SharedPrefUtils.IS_ADMIN, userSelected.isAdmin());
                        if (userSelected.isAdmin()) {
                            Intent intent = new Intent(LoginActivity.this, AdminHomeScreenActivity.class);
                            startActivity(intent);
                            Toast.makeText(mAppContext, "User Exist", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
                            startActivity(intent);
                            Toast.makeText(mAppContext, "User Exist", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    else {
                        Toast.makeText(mAppContext, "User doesn't exist", Toast.LENGTH_SHORT).show();
                    }
                    showProgress(false);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(mAppContext, "Database error occured", Toast.LENGTH_SHORT).show();
                    showProgress(false);
                }
            });
        }
    }

    private boolean isUsernameValid(String username) {
        return !TextUtils.isEmpty(username);
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
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

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

