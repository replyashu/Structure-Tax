package com.structuremytax.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.structuremytax.R;
import com.structuremytax.global.Controller;

import java.text.DecimalFormat;

/**
 * Created by apple on 23/04/17.
 */

public class Saving_Fragment extends Fragment implements View.OnClickListener{

    private RecyclerView recyclerBreakup;
    private Button btnTakeHome;
    private EditText editHomeLoan;
    private EditText edit80c;
    private EditText edit80d;
    private EditText edit80ccd;
    private TextView txtTakeHome;

    private String takeHomeSalary;

    Controller appController;
    double salary;
    double tax;
    double basic, med, nps, home;
    private  String bas, me, np, ho;
    double saving = 0;
    private View view;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public Saving_Fragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appController = Controller.getInstance();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            salary = bundle.getDouble("salary");
            tax = bundle.getDouble("tax");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_saving, container, false);
        initializeLayoutVariables(view);

        edit80c.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!edit80c.getText().toString().isEmpty()) {
                    btnTakeHome.setEnabled(true);
                }
            }
        });
        return view;
    }

    private void initializeLayoutVariables(View v){
        editHomeLoan = (EditText) v.findViewById(R.id.editLoan);
        edit80c = (EditText) v.findViewById(R.id.edit80c);
        edit80d = (EditText) v.findViewById(R.id.edit80d);
        edit80ccd = (EditText) v.findViewById(R.id.editNPS);
        btnTakeHome = (Button) v.findViewById(R.id.btnTakeHome);
        txtTakeHome = (TextView) v.findViewById(R.id.txtTakeHome);
        txtTakeHome.setVisibility(View.GONE);
        btnTakeHome.setEnabled(false);

        edit80c.requestFocus();

        btnTakeHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnTakeHome){
            txtTakeHome.setVisibility(View.VISIBLE);
            saving = 0;
            basic = 0;
            med = 0;
            nps = 0;
            home = 0;
            bas = edit80c.getText().toString();
            me = edit80d.getText().toString();
            np = edit80ccd.getText().toString();
            ho = editHomeLoan.getText().toString();

            if(bas.isEmpty())
                basic = 0;
            else {
                basic = Double.parseDouble(bas);
                if(basic > 150000)
                    basic = 150000;
            }
            if(me.isEmpty())
                med = 0;
            else {
                med = Double.parseDouble(me);
                if(med > 15000)
                    med = 15000;
            }
            if(np.isEmpty())
                nps = 0;
            else {
                nps = Double.parseDouble(np);
                if(nps > 50000)
                    nps = 50000;
            }
            if(ho.isEmpty())
                home = 0;
            else {
                home = Double.parseDouble(ho);
                if(home > 200000)
                    home = 200000;
            }


            saving = tax + basic + med + nps + home;
            Log.d("salarysavbe", salary + " " + saving + "");

            saving = appController.computeTaxableSalary(salary, saving);

            SharedPreferences sp = getActivity().getSharedPreferences("TAKE_HOME", 0);
            long take_home = sp.getLong(takeHomeSalary, 0);
            double diff = (salary - saving)/12;
            double kat = diff - (double) (take_home/12);
            sendToFirebase(kat);


            txtTakeHome.setText("Your Monthly Take Home(Optimized) is : \nRs. " +
                    new DecimalFormat("##.##").format(diff) + "\n\n" +
                    "Difference : " + "Rs. " + new DecimalFormat("##.##").format(kat));

            // TODO add firebase db reference for global saving


        }
    }

    private void sendToFirebase(double amount){
        database = FirebaseDatabase.getInstance();

        String android_id = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        myRef = database.getReference("savings/");

        myRef.child(android_id).setValue(amount);
    }
}
