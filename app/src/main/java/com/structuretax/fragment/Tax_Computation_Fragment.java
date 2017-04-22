package com.structuretax.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.structuretax.R;
import com.structuretax.adapter.SalarySplitAdapter;
import com.structuretax.adapter.TaxBreakUpAdapter;
import com.structuretax.global.Controller;
import com.structuretax.model.Components;
import com.structuretax.model.TaxComponents;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 23/04/17.
 */

public class Tax_Computation_Fragment extends Fragment implements View.OnClickListener{

    private RecyclerView recyclerBreakup;
    private Button btnOptimize;
    private Button btnComputeTax;
    List<Components> components;
    Controller appController;
    Intent intent;
    double salary;
    boolean pf;
    private int optimize;


    public Tax_Computation_Fragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appController = Controller.getInstance();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            salary = bundle.getDouble("salary");
            pf = bundle.getBoolean("pf");
            optimize = bundle.getInt("optimize");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tax_calculation, container, false);
        initializeLayoutVariables(view);


        ArrayList<Components> component = appController.salaryBreak(salary, pf, optimize);
        List<TaxComponents> taxComponents = appController.taxBreakup(component);

            TaxBreakUpAdapter adapter = new TaxBreakUpAdapter(taxComponents);
        recyclerBreakup.setAdapter(adapter);
        return view;

    }

    private void initializeLayoutVariables(View v){
        recyclerBreakup = (RecyclerView) v.findViewById(R.id.recyclerBreakup);
        recyclerBreakup.setLayoutManager(new LinearLayoutManager(getActivity()));
        btnOptimize = (Button) v.findViewById(R.id.btnOptimize);
        btnComputeTax = (Button) v.findViewById(R.id.btnSaveTax);
        btnComputeTax.setOnClickListener(this);
        btnOptimize.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
