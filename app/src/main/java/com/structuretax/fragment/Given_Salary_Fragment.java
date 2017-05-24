package com.structuretax.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.structuretax.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_OK;
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

    private boolean isPfEligible = false;
    private double ctcSalary;


    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 123;
    private static int IMG_RESULT = 1;


    int width = 0;
    int height = 0;

    private static String FILE;

    Button btnSave;
    Button btnDiffernce;

    TextView txtNet;

    View view;

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
        view = inflater.inflate(R.layout.fragment_structured_loss,container, false);
        initializeLayoutVariables(view);
        ((InputMethodManager) (getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        return view;
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
                if(editBasic.getText().toString().isEmpty())
                    basic = 0;
                else
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
                if(editHRA.getText().toString().isEmpty())
                    hra = 0;
                else
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
                if(editConveyance.getText().toString().isEmpty())
                    conveyance = 0;
                else
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
                if(editPf.getText().toString().isEmpty())
                    pf = 0;
                else
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
                if(editDA.getText().toString().isEmpty())
                    da = 0;
                else
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
                if(editMedical.getText().toString().isEmpty())
                    medical = 0;
                else
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
                if(editFood.getText().toString().isEmpty())
                    food = 0;
                else
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
                if(editTelephone.getText().toString().isEmpty())
                    telephone = 0;
                else
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
                if(editHealth.getText().toString().isEmpty())
                    health = 0;
                else
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
                if(editUniform.getText().toString().isEmpty())
                    uniform = 0;
                else
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
                if(editHealth.getText().toString().isEmpty())
                    health = 0;
                else
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
                if(editBooks.getText().toString().isEmpty())
                    books = 0;
                else
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
                if(editLta.getText().toString().isEmpty())
                    lta = 0;
                else
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
                if(editSpecial.getText().toString().isEmpty())
                    special = 0;
                else
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
                Toast.makeText(getActivity().getApplicationContext(), "Saving file for you", Toast.LENGTH_LONG).show();
                // TODO add runtime permissions
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    try {
                        getPermission();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    saveToPdf(v);
                }
                break;

            case R.id.btnDifference:
                try {
                    ctcSalary = Double.parseDouble(txtNet.getText().toString());
                }catch (NumberFormatException e){
//                    Toast.makeText(getActivity(),"Enter Your CTC", Toast.LENGTH_LONG).show();
                }

                computeSalary(ctcSalary, isPfEligible);
                Log.d("salary", ctcSalary + " " + isPfEligible);
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getPermission() throws ExecutionException, InterruptedException {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("External Storage");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

                    saveToPdf(view);



                } else {
                    // Permission Denied
                    Toast.makeText(getActivity(), "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_RESULT && resultCode == RESULT_OK
                && null != data) {

        }
    }

    private void saveToPdf(View v){

        final ScrollView root = (ScrollView) view.findViewById(R.id.scrollOuter); //RelativeLayout is root view of my UI(xml) file.

        root.setDrawingCacheEnabled(true);

        int height = root.getChildAt(0).getHeight();
        int width = root.getChildAt(0).getWidth();


        Bitmap screen= getBitmapFromView(getActivity().getWindow().findViewById(R.id.scrollOuter), height, width); // here give id of our root layout (here its my RelativeLayout's id)

        root.setDrawingCacheEnabled(true);
        try
        {

            Document document = new Document(PageSize.A4);
            File pdfDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS), "Structure Tax");
            if (!pdfDir.exists()){
                pdfDir.mkdir();
            }

            File myFile= new File(pdfDir, "Structure");

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

    private void computeSalary(double salary, boolean pf){
//        appController.salaryBreak(salary, pf);
        Fragment fragment = new Optimized_Salary_Fragment();

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
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, fragment).addToBackStack("Given")
                .commit();

    }

    public static Bitmap getBitmapFromView(View view, int height, int width) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);

        Log.d("heightwid", height + " " + width);
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
