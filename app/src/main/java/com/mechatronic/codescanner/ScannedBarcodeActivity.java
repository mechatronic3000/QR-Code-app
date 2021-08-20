package com.mechatronic.codescanner;



import static android.os.Environment.DIRECTORY_DOCUMENTS;
import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static android.os.Environment.getExternalStoragePublicDirectory;

import android.Manifest;
//import android.content.Intent;
import android.content.Intent;
import android.content.pm.PackageManager;
//import android.net.Uri;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.os.Environment;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
//import java.io.BufferedReader;
//import java.io.DataInputStream;
import java.io.File;
//import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
//import java.io.InputStreamReader;
import android.os.Vibrator;
import java.util.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import android.net.Uri;



public class ScannedBarcodeActivity extends AppCompatActivity {


    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    Button btnAction;
    Button btnClear;
    Button btnBack;
    String intentData = "";


    static int scans = 0;
    public static List<scanData> scanEntry = new ArrayList<scanData>();


    boolean scannedAlready = false;
    boolean removedAlready = false;

    private final String filename = "scans.csv";
    private final String filepath = DIRECTORY_DOWNLOADS;
    File myExternalFile;

    boolean isEmail = false;

    public ScannedBarcodeActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);

        initViews();
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            btnAction.setEnabled(false);
        }
        else {
            myExternalFile = new File(getExternalStoragePublicDirectory(filepath), filename);
        }

    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        btnAction = findViewById(R.id.btnAction);
        btnClear = findViewById(R.id.btnClear);
        btnBack = findViewById(R.id.btnBack);

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if (scanTable)) {
                   // if (isEmail)
                   //     startActivity(new Intent(ScannedBarcodeActivity.this, EmailActivity.class).putExtra("email_address", intentData));
                   // else {
                   //     startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(intentData)));
                   // }
                saveScansToStorage();
                emailScans();
                //}


            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPartNumberString("");
                setSerialNumberString("");
                setFSANString("");
                setMACString("");
                scanEntry.clear();
                setScanCount(scanEntry.size());
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScannedBarcodeActivity.this, MainActivity.class));
            }
        });
    }

    private void saveScansToStorage(){
        try {
            String st = buildScanTable();
            FileOutputStream fos = new FileOutputStream(myExternalFile);
            fos.write(st.getBytes());
            fos.close();
            Toast.makeText(getApplicationContext(),
                    "Saved to external storage. -",
                    Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private boolean isAlreadyScanned(scanData scanImmediate){
        for (scanData entry:scanEntry) {
            if(entry.getSerialNumber().equals(scanImmediate.getSerialNumber()))
                return true;
        }
        return false;
    }

    private void deleteScan(scanData scanImmediate){
        boolean found = false;
        if (scanEntry.size() > 0) {
            for (int i = 0; i < scanEntry.size(); i++)
                if (scanEntry.get(i).getSerialNumber().equals(scanImmediate.getSerialNumber())) {
                    scanEntry.remove(i);
                    found = true;
                }
            if (!found)
                Toast.makeText(getApplicationContext(),
                        "Item Not Found.",
                        Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(),
                        "Item Removed.",
                        Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(),
                    "No Items in List.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void setScanCount( int c){
        scans = c;
        TextView txtCount = findViewById(R.id.txtCount);
        txtCount.setText(new StringBuilder().append(scans));
    }

    private void setPartNumberString(String pn){
        TextView txtPartNumber = findViewById(R.id.txtPartNumber);
        txtPartNumber.setText(pn);
    }

    private void setSerialNumberString(String sn){
        TextView txtSerialNumber = findViewById(R.id.txtSerialNumber);
        txtSerialNumber.setText(sn);
    }

    private void setFSANString(String fs){
        TextView txtFSAN = findViewById(R.id.txtFSAN);
        txtFSAN.setText(fs);
    }

    private void setMACString(String mc){
        TextView txtMACAddress = findViewById(R.id.txtMACAddress);
        txtMACAddress.setText(mc);
    }
    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState);
    }


    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(extStorageState);
    }


    private void initialiseDetectorsAndSources() {

        Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScannedBarcodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScannedBarcodeActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }
                    setScanCount(scanEntry.size());
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getApplicationContext(), "Barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {

                    txtBarcodeValue.post(new Runnable() {

                        @Override
                        public void run() {
                            if (barcodes.valueAt(0).email != null) {
                                txtBarcodeValue.removeCallbacks(null);
                                intentData = barcodes.valueAt(0).email.address;
                                txtBarcodeValue.setText(intentData);
                                isEmail = true;
                                //btnAction.setText("ADD CONTENT TO THE MAIL");
                            } else {
                                isEmail = false;
                                //btnAction.setText("LAUNCH URL");
                                intentData = barcodes.valueAt(0).displayValue;
                                txtBarcodeValue.setText(intentData);
                                if (intentData.contains("http://cass.calix.com/")) {
                                    String serialNumber = intentData.substring(26, 38);
                                    String partNumber = intentData.substring(58,68);
                                    partNumber = partNumber.substring(0,3) + "-" + partNumber.substring(3,8) + " " + partNumber.substring(8,10);
                                    String MACAddress = intentData.substring(73,85);
                                    String FSAN = intentData.substring(89,101);
                                    String s_Source = MainActivity.s_source;
                                    String s_Customer = MainActivity.s_customer;
                                    String s_Powo = MainActivity.s_powo;
                                    String s_So = MainActivity.s_so;
                                    boolean b_Passfail = MainActivity.b_passfail;


                                    scanData scanImmediate = new scanData(
                                            serialNumber,
                                            partNumber,
                                            MACAddress,
                                            FSAN,
                                            s_Source,
                                            s_Customer,
                                            s_Powo,
                                            s_So,
                                            b_Passfail
                                            );

                                    Switch add_remove = findViewById(R.id.swDelete);

                                        if (!isAlreadyScanned(scanImmediate)) {
                                            if (!add_remove.isChecked()) {
                                                scanEntry.add(scanImmediate);
                                                Vibrator v = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                                                v.vibrate(100);
                                                scannedAlready = false;
                                                setScanCount(scanEntry.size());
                                                setPartNumberString(partNumber);
                                                setSerialNumberString(serialNumber);
                                                setFSANString(FSAN);
                                                setMACString(MACAddress);
                                            }else{
                                                if (!removedAlready) {
                                                    Toast.makeText(getApplicationContext(), "This item is not in the list to be removed.", Toast.LENGTH_SHORT).show();
                                                }
                                                removedAlready = true;
                                            }
                                        } else {
                                            if (add_remove.isChecked()) {
                                                deleteScan(scanImmediate);
                                                setScanCount(scanEntry.size());
                                                removedAlready = false;
                                                Vibrator v = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                                                v.vibrate(200);
                                            }else {
                                                if (!scannedAlready) {
                                                    Toast.makeText(getApplicationContext(), "This item has already been scanned.", Toast.LENGTH_SHORT).show();
                                                    scannedAlready = true;
                                                }
                                            }
                                        }

                                }
                            }
                        }
                    });
                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

    private String buildScanTable() {
        String scanTable = "";
        for (scanData entry : scanEntry) {


            String cu = entry.getCustomerCompany();
            String source = entry.getSourceCompany();
            String pw = entry.getOrderNumber();
            String so = entry.getSaleNumber();
            String partNumber = entry.getPartNumber();
            String serialNumber = entry.getSerialNumber();
            String mc = entry.getMACAddress();
            String fs = entry.getFSANnumber();

            boolean pf = entry.isPassFail();
            String pfString;

            if (pf) {
                pfString = "Fail";
            } else {
                pfString = "Pass";
            }

            SimpleDateFormat sdfCalander = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat sdfClock = new SimpleDateFormat("HH:mm");
            String currentDate = sdfCalander.format(new Date());
            String currentTime = sdfClock.format(new Date());

            if (pf) {
                cu = source;
            } else {
                if (cu.length() == 0 || source.length() == 0)
                    cu = source + cu;
                else
                    cu = source + " / " + cu;
                if (pw.length() == 0 || so.length() == 0)
                    pw = pw + so;
                else
                    pw = pw + " / " + so;
            }
            scanTable = scanTable +
                    String.format("%s,%s,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",,\"%s\",\"%s\",\"%s\"%n",
                            currentDate,
                            currentTime,
                            pfString,
                            cu,
                            pw,
                            partNumber,
                            serialNumber,
                            pfString,
                            mc,
                            fs
                    );
        }
        return scanTable;

    }
    private void emailScans(){

        SimpleDateFormat sdfCalander = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat sdfClock = new SimpleDateFormat("HH:mm");
        String currentDate = sdfCalander.format(new Date());
        String currentTime = sdfClock.format(new Date());

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"mechatronic3000@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, String.format("Scans %s %s",currentDate,currentTime));
        emailIntent.putExtra(Intent.EXTRA_TEXT, String.format("QR Code Scans %s %s",currentDate,currentTime));
        emailIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (!myExternalFile.exists() || !myExternalFile.canRead()) {
            return;
        }
        Uri uri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", myExternalFile);
        try {
            if (uri != null) {
                emailIntent.putExtra(Intent.EXTRA_STREAM,uri);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));

    }

}
