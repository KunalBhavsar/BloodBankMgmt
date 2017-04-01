package co.project.bloodbankmgmt.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import co.project.bloodbankmgmt.R;

public class AdminHomeScreenActivity extends AppCompatActivity {

    private Button btnBloodBank;
    private RecyclerView recyclerView;
    private FloatingActionButton fabManualTransations;

    private Activity mActivityContext;
    private Context mAppContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_screen);

        btnBloodBank = (Button) findViewById(R.id.btn_blood_bank);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_requests);
        fabManualTransations = (FloatingActionButton) findViewById(R.id.fab_add_manual_transations);

        fabManualTransations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivityContext, TransactionActivity.class);
                mActivityContext.startActivity(intent);
            }
        });
    }

}
