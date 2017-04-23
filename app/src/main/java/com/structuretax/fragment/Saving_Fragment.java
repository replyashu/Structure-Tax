package com.structuretax.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.structuretax.R;
import com.structuretax.global.Controller;
import com.structuretax.model.Components;

import java.util.List;

/**
 * Created by apple on 23/04/17.
 */

public class Saving_Fragment extends Fragment {

    private RecyclerView recyclerBreakup;
    private Button btnShowTax;
    private Button btnComputeTax;
    private EditText editHomeLoan;
    private EditText edit80c;
    private EditText edit80d;
    private EditText edit80ccd;

    List<Components> components;
    Controller appController;
    Intent intent;
    double salary;
    double provident;
    double tax;
    private int optimize;
    private double tax1, tax2;
    private View view;

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
        return view;
    }

    private void initializeLayoutVariables(View v){
//        btnShowTax;
//        btnComputeTax;
//        editHomeLoan;
//        edit80c;
//        edit80d;
//        edit80ccd;
    }
}
