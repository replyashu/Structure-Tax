package com.structuretax.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.structuretax.R;
import com.structuretax.fragment.Given_Salary_Fragment;
import com.structuretax.fragment.Optimized_Salary_Fragment;
import com.structuretax.global.Controller;

public class MainActivity extends AppCompatActivity implements AppCompatSpinner.OnItemSelectedListener, View.OnClickListener{

    private TextView mTextMessage;
    private AppCompatSpinner spinEmployeeCount;
    private boolean isPfEligible = false;
    private double ctcSalary;
    private EditText editCtcSalary;
    private Button btnStructure;
    private Button btnSalaryGiven;
    private Controller appController;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
                case R.id.navigation_about:
                    mTextMessage.setText(R.string.title_about);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        initializeLayoutVariables();

    }

    public void initializeLayoutVariables(){

        spinEmployeeCount = (AppCompatSpinner) findViewById(R.id.spinEmployeeCount);
        editCtcSalary = (EditText) findViewById(R.id.editSalaryCtc);
        btnStructure = (Button) findViewById(R.id.btnStructure);
        btnSalaryGiven = (Button) findViewById(R.id.btnGiven);

        spinEmployeeCount.setOnItemSelectedListener(this);
        btnStructure.setOnClickListener(this);
        btnSalaryGiven.setOnClickListener(this);

        appController = Controller.getInstance();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (position){
            case 0:
                try {
                    Snackbar.make(view, "Select No Of Employees", Snackbar.LENGTH_LONG).show();
                }
                catch (NullPointerException e){

                }
                break;

            case 1:
                isPfEligible = false;
                break;
            default:
                isPfEligible = true;
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btnStructure:
                try {
                    ctcSalary = Double.parseDouble(editCtcSalary.getText().toString());
                }catch (NumberFormatException e){
                    Toast.makeText(this,"Enter Your CTC", Toast.LENGTH_LONG).show();
                }

                computeSalary(ctcSalary, isPfEligible);
                Log.d("salary", ctcSalary + " " + isPfEligible);
                break;

            case R.id.btnGiven:
                Fragment fragment = new Given_Salary_Fragment();

                FrameLayout fragmentLayout = new FrameLayout(this);
// set the layout params to fill the activity
                fragmentLayout.setLayoutParams(new  ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
// set an id to the layout
                fragmentLayout.setId(R.id.content); // some positive integer
// set the layout as Activity content
                setContentView(fragmentLayout);
// Finally , add the fragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, fragment).addToBackStack("given")
                        .commit();
                break;


        }
    }

    private void computeSalary(double salary, boolean pf){
//        appController.salaryBreak(salary, pf);
        Fragment fragment = new Optimized_Salary_Fragment();

        FrameLayout fragmentLayout = new FrameLayout(this);
// set the layout params to fill the activity
        fragmentLayout.setLayoutParams(new  ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
// set an id to the layout
        fragmentLayout.setId(R.id.content); // some positive integer
// set the layout as Activity content
        setContentView(fragmentLayout);
// Finally , add the fragment
        Bundle bundle = new Bundle();
        bundle.putDouble("salary", salary);
        bundle.putBoolean("pf", pf);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, fragment).addToBackStack("optimized")
                .commit();

    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(fm.getBackStackEntryCount() == 1) {
            fm.popBackStack();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        else {
            super.onBackPressed();
        }
//
    }
}
