package com.mechatronic.codescanner;

import static com.mechatronic.codescanner.R.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnTakePicture, btnScanBarcode;

    private EditText customer;
    private EditText source;
    private EditText powo;
    private EditText so;
    private Switch passfail;

    public static String s_customer = "";
    public static String s_source = "";
    public static String s_powo = "";
    public static String s_so = "";
    public static Boolean b_passfail = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);
        initViews();
    }

    private void initViews() {
        //btnTakePicture = findViewById(R.id.btnTakePicture);
        btnScanBarcode = findViewById(id.btnScanBarcode);
        //btnTakePicture.setOnClickListener(this);
        btnScanBarcode.setOnClickListener(this);
        customer =  (EditText) findViewById(id.editTextCustomer);
        source =  (EditText) findViewById(id.editTextSource);
        powo =  (EditText) findViewById(id.editTextPOWO);
        passfail = (Switch) findViewById(id.swPassFail);
        so =  (EditText) findViewById(id.editTextSO);
        customer.setText(s_customer);
        source.setText(s_source);
        powo.setText(s_powo);
        so.setText(s_so);
        passfail = (Switch) findViewById(id.swPassFail);
        passfail.setChecked(b_passfail);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            /*case R.id.btnTakePicture:
                startActivity(new Intent(MainActivity.this, PictureBarcodeActivity.class));
                break;*/
            case id.btnScanBarcode:
                customer =  (EditText) findViewById(id.editTextCustomer);
                s_customer = customer.getText().toString();

                source =  (EditText) findViewById(id.editTextSource);
                s_source = source.getText().toString();

                powo =  (EditText) findViewById(id.editTextPOWO);
                s_powo = powo.getText().toString();

                so =  (EditText) findViewById(id.editTextSO);
                s_so = so.getText().toString();

                passfail = (Switch) findViewById(id.swPassFail);
                b_passfail = passfail.isChecked();

                startActivity(new Intent(MainActivity.this, ScannedBarcodeActivity.class));
                break;
        }

    }
}
