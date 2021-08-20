package com.mechatronic.codescanner;




import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;

public class PreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        LinearLayout mLayout=(LinearLayout)findViewById(R.id.llScanList);

/*
        EditText et=new EditText(...);
//....
        mLayout.addView(et);

 */
    }
}