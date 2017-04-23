package com.structuretax.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.structuretax.R;
import com.structuretax.adapter.SalarySplitAdapter;
import com.structuretax.adapter.TaxBreakUpAdapter;
import com.structuretax.global.Controller;
import com.structuretax.model.Components;
import com.structuretax.model.TaxComponents;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 08/04/17.
 */

public class Optimized_Salary_Fragment extends Fragment implements View.OnClickListener{
    private RecyclerView recyclerBreakup;
    private Button btnOptimize;
    private Button btnComputeTax;
    double tax, tax1;
    Controller appController;
    double salary;
    boolean pf;
    private int optimize;

    public Optimized_Salary_Fragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appController = Controller.getInstance();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            salary = bundle.getDouble("salary");
            pf = bundle.getBoolean("pf");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_optimized, container, false);
        initializeLayoutVariables(view);

        ArrayList<Components> tmp = new ArrayList<>();

        ArrayList<Components> component = appController.salaryBreak(salary, pf, 40);
        List<TaxComponents> taxComponent = appController.taxBreakup(component);
        tax = taxComponent.get(taxComponent.size() - 1).getTax();

        ArrayList<Components> components = appController.salaryBreak(salary, pf, 50);
        List<TaxComponents> taxComponents = appController.taxBreakup(components);
        tax1 = taxComponents.get(taxComponents.size() - 1).getTax();


        SalarySplitAdapter adapter;

        if(tax > tax1){
            components = appController.salaryBreak(salary, pf, 50);
            adapter = new SalarySplitAdapter(components);
        }
        else {
            component = appController.salaryBreak(salary, pf, 40);
            adapter = new SalarySplitAdapter(component);
        }

        recyclerBreakup.invalidate();
        recyclerBreakup.setAdapter(adapter);
        return view;
    }

    private void initializeLayoutVariables(View v){
        recyclerBreakup = (RecyclerView) v.findViewById(R.id.recyclerBreakup);
        recyclerBreakup.setLayoutManager(new LinearLayoutManager(getActivity()));
        btnOptimize = (Button) v.findViewById(R.id.btnOptimize);
        btnComputeTax = (Button) v.findViewById(R.id.btnTax);
        btnComputeTax.setOnClickListener(this);
        btnOptimize.setOnClickListener(this);
        optimize = 40;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.btnOptimize){
            optimize = 50;
            List<Components> component = appController.salaryBreak(salary, pf, optimize);
            SalarySplitAdapter adapter = new SalarySplitAdapter(component);
            recyclerBreakup.invalidate();
            recyclerBreakup.setAdapter(adapter);
        }

        else if(id == R.id.btnTax){

            Fragment fragment = new Tax_Computation_Fragment();

            FrameLayout fragmentLayout = new FrameLayout(getActivity());
// set the layout params to fill the activity
            fragmentLayout.setLayoutParams(new  ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
// set an id to the layout
            fragmentLayout.setId(R.id.content); // some positive integer
// set the layout as Activity content
            getActivity().setContentView(fragmentLayout);
// Finally , add the fragment
            Bundle bundle = new Bundle();
            bundle.putDouble("salary", salary);
            bundle.putBoolean("pf", pf);
            bundle.putInt("optimize", optimize);
            fragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, fragment).addToBackStack("tax")
                    .commit();

//            ArrayList<Components> component = appController.salaryBreak(salary, pf, optimize);
//            List<TaxComponents> taxComponents = appController.taxBreakup(component);
//
//            getActivity().getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.content, new Tax_Computation_Fragment()).addToBackStack("Tax")
//                    .commit();
//            TaxBreakUpAdapter adapter = new TaxBreakUpAdapter(taxComponents);


        }

    }
}
