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

import com.structuretax.R;
import com.structuretax.adapter.SalarySplitAdapter;
import com.structuretax.global.Controller;
import com.structuretax.model.Components;

import java.util.List;

/**
 * Created by apple on 08/04/17.
 */

public class Optimized_Salary_Fragment extends Fragment implements View.OnClickListener{
    private RecyclerView recyclerBreakup;
    private Button btnOptimize;
    List<Components> components;
    Controller appController;
    Intent intent;
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

        recyclerBreakup.setAdapter(new SalarySplitAdapter(appController.salaryBreak(salary, pf, optimize)));
        return view;
    }

    private void initializeLayoutVariables(View v){
        recyclerBreakup = (RecyclerView) v.findViewById(R.id.recyclerBreakup);
        recyclerBreakup.setLayoutManager(new LinearLayoutManager(getActivity()));
        btnOptimize = (Button) v.findViewById(R.id.btnOptimize);
        btnOptimize.setOnClickListener(this);
        optimize = 40;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.btnOptimize){
            optimize = 50;
            SalarySplitAdapter adapter = new SalarySplitAdapter(appController.salaryBreak(salary, pf, optimize));
            recyclerBreakup.invalidate();
            recyclerBreakup.setAdapter(adapter);
        }
    }
}
