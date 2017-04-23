package com.structuretax.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.ScrollView;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.structuretax.R;
import com.structuretax.adapter.SalarySplitAdapter;
import com.structuretax.adapter.TaxBreakUpAdapter;
import com.structuretax.global.Controller;
import com.structuretax.model.Components;
import com.structuretax.model.TaxComponents;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
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
    double provident;
    boolean pf;
    private int optimize;
    private double tax1, tax2;
    private View view;


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
        view = inflater.inflate(R.layout.fragment_tax_calculation, container, false);
        initializeLayoutVariables(view);


        ArrayList<Components> component = appController.salaryBreak(salary, pf, 40);
        List<TaxComponents> taxComponent = appController.taxBreakup(component);
        tax1 = taxComponent.get(taxComponent.size() - 1).getTax();

        ArrayList<Components> components = appController.salaryBreak(salary, pf, 50);
        List<TaxComponents> taxComponents = appController.taxBreakup(components);
        tax2 = taxComponents.get(taxComponents.size() - 1).getTax();

        TaxBreakUpAdapter adapter;

        if(tax1 > tax2){
            components = appController.salaryBreak(salary, pf, 50);
            taxComponents = appController.taxBreakup(components);
            adapter = new TaxBreakUpAdapter(taxComponents);
        }
        else {
            component = appController.salaryBreak(salary, pf, 40);
            taxComponent = appController.taxBreakup(component);
            adapter = new TaxBreakUpAdapter(taxComponent);
        }

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
        int id = v.getId();
        switch (id){
            case R.id.btnOptimize:
                if(tax1 > tax2){
                    tax1 = tax2;
                }

                Fragment fragment = new Saving_Fragment();

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
                tax1 = (tax1 - 2400)/1.03;
                bundle.putDouble("tax", tax1);
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, fragment).addToBackStack("saving")
                        .commit();
                break;

            case R.id.btnSaveTax:
                saveToPdf();
                break;
        }
    }

    private void saveToPdf(){

        final ScrollView root = (ScrollView) view.findViewById(R.id.scrollView); //RelativeLayout is root view of my UI(xml) file.

        root.setDrawingCacheEnabled(true);

        int height = root.getChildAt(0).getHeight();
        int width = root.getChildAt(0).getWidth();


        Bitmap screen= getBitmapFromView(getActivity().getWindow().findViewById(R.id.scrollView), height, width); // here give id of our root layout (here its my RelativeLayout's id)

        root.setDrawingCacheEnabled(true);
        try
        {

            Document document = new Document(PageSize.A4);
            File pdfDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS), "Structure Tax");
            if (!pdfDir.exists()){
                pdfDir.mkdir();
            }

            File myFile= new File(pdfDir, "Tax_Structure");

            PdfWriter.getInstance(document, new FileOutputStream(myFile + ".pdf" ));
            document.open();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            screen.compress(Bitmap.CompressFormat.PNG, 100, stream);

            byte[] byteArray = stream.toByteArray();
            addImage(document,byteArray);
            document.close();
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public static Bitmap getBitmapFromView(View view, int height, int width) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);

        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    private static void addImage(Document document, byte[] byteArray)
    {

        Image image = null;
        try
        {
            image = Image.getInstance(byteArray);
        }
        catch (BadElementException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (MalformedURLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // image.scaleAbsolute(150f, 150f);
        try
        {
            image.scaleAbsolute(PageSize.A4);
            image.setAbsolutePosition(0, 0);
            document.add(image);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
