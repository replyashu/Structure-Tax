package com.structuretax.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.structuretax.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by apple on 15/04/17.
 */

public class Given_Salary_Fragment extends Fragment implements View.OnClickListener{

    EditText editBasic;
    EditText editHRA;
    EditText editDA;
    EditText editPf;
    EditText editConveyance;
    EditText editMedical;
    EditText editFood;
    EditText editTelephone;
    EditText editHelper;
    EditText editUniform;
    EditText editHealth;
    EditText editBooks;
    EditText editLta;
    EditText editSpecial;

    int width = 0;
    int height = 0;

    private static String FILE;

    Button btnSave;
    Button btnDiffernce;

    TextView txtNet;

    double basic, hra, da, conveyance, medical, food, telephone, helper, uniform, health, books, lta, special, pf;


    public Given_Salary_Fragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_structured_loss,container, false);
        initializeLayoutVariables(v);
        ((InputMethodManager) (getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        return v;
    }

    private void initializeLayoutVariables(View v){
        editBasic = (EditText) v.findViewById(R.id.editBasic);
        editHRA = (EditText) v.findViewById(R.id.editHRA);
        editDA = (EditText) v.findViewById(R.id.editDA);
        editPf = (EditText) v.findViewById(R.id.editPF);
        editConveyance = (EditText) v.findViewById(R.id.editConveyance);
        editMedical = (EditText) v.findViewById(R.id.editMedical);
        editFood = (EditText) v.findViewById(R.id.editFood);
        editTelephone = (EditText) v.findViewById(R.id.editMobile);
        editHelper = (EditText) v.findViewById(R.id.editHelper);
        editHealth = (EditText) v.findViewById(R.id.editHealth);
        editUniform = (EditText) v.findViewById(R.id.editUniform);
        editBooks = (EditText) v.findViewById(R.id.editBooks);
        editLta = (EditText) v.findViewById(R.id.editLta);
        editSpecial = (EditText) v.findViewById(R.id.editSpecial);

        btnSave = (Button) v.findViewById(R.id.btnSave);
        btnDiffernce = (Button) v.findViewById(R.id.btnDifference);


        txtNet = (TextView) v.findViewById(R.id.txtNet);

        editBasic.requestFocus();


        editBasic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                basic = Double.parseDouble(editBasic.getText().toString());
                computeAndAssignNet();
            }
        });
        editHRA.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                hra = Double.parseDouble(editHRA.getText().toString());
                computeAndAssignNet();
            }
        });
        editConveyance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                conveyance = Double.parseDouble(editConveyance.getText().toString());
                computeAndAssignNet();
            }
        });
        editPf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pf = Double.parseDouble(editPf.getText().toString());
                computeAndAssignNet();
            }
        });
        editDA.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                da = Double.parseDouble(editDA.getText().toString());
                computeAndAssignNet();
            }
        });
        editMedical.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                medical = Double.parseDouble(editMedical.getText().toString());
                computeAndAssignNet();
            }
        });
        editFood.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                food = Double.parseDouble(editFood.getText().toString());
                computeAndAssignNet();
            }
        });
        editTelephone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                telephone = Double.parseDouble(editTelephone.getText().toString());
                computeAndAssignNet();
            }
        });
        editHelper.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                health = Double.parseDouble(editHelper.getText().toString());
                computeAndAssignNet();
            }
        });
        editUniform.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                uniform = Double.parseDouble(editUniform.getText().toString());
                computeAndAssignNet();
            }
        });
        editHealth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                health = Double.parseDouble(editHealth.getText().toString());
                computeAndAssignNet();
            }
        });
        editBooks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                books = Double.parseDouble(editBooks.getText().toString());
                computeAndAssignNet();
            }
        });
        editLta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                lta = Double.parseDouble(editLta.getText().toString());
                computeAndAssignNet();
            }
        });
        editSpecial.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                special = Double.parseDouble(editSpecial.getText().toString());
                computeAndAssignNet();
            }
        });

        btnSave.setOnClickListener(this);
        btnDiffernce.setOnClickListener(this);

    }

    private void computeAndAssignNet(){
        txtNet.setText(basic+ hra+ da+
                conveyance+ medical+ food+ telephone+ helper+
                uniform+ health+ books+ lta+ special+ pf + "");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btnSave:
                saveToPdf(v);
                break;

            case R.id.btnDifference:
                break;
        }
    }

    private void saveToPdf(View v){
//        Bitmap screen;
//        View v1 = v.getRootView();
//        v1.setDrawingCacheEnabled(true);
//        screen= Bitmap.createBitmap(v1.getDrawingCache());
//        v1.setDrawingCacheEnabled(false);

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        final ScrollView root = (ScrollView) inflater.inflate(R.layout.fragment_structured_loss, null); //RelativeLayout is root view of my UI(xml) file.
        ViewTreeObserver viewTreeObs = root.getViewTreeObserver();

        viewTreeObs.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                root.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                width  = root.getChildAt(0).getMeasuredWidth();
                height = root.getMeasuredHeight();
                Log.d("heightwidth", height + " " + width);

            }
        });


        Bitmap screen= getBitmapFromView(getActivity().getWindow().findViewById(R.id.scrollOuter), height, width); // here give id of our root layout (here its my RelativeLayout's id)

        try
        {
            Document document = new Document();
            File myFile= new File(Environment.getExternalStorageDirectory(), "/Structure");

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

        Log.d("heightwid", view.getWidth() + " " + view.getHeight());
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
            document.add(image);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
