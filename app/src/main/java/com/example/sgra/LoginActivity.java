package com.example.sgra;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.example.sgra.utilities.MarshmallowPermission;
import com.example.sgra.utilities.Utiilties;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int MY_PERMISSIONS_REQUEST_ACCOUNTS = 10;
    EditText edit_user_name, edit_pass;
    Button button_login;
    ToggleButton toggleButton;
    String version;
    TelephonyManager tm;
    private static String imei = "";
    MarshmallowPermission MARSHMALLOW_PERMISSION;
    String serial_id = "";
    TextView text_ver,text_head;
    //SmsReceiver smsReceiver;
    IntentFilter filter;
    private boolean initse;
    private ProgressDialog dialog1;

    //private SmsVerificationService smsVerificationService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /*DataBaseHelper db = new DataBaseHelper(this);
        try {
            db.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            db.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;

        }*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Call some material design APIs here
            if (checkAndRequestPermissions()) {
                //init2();
                if (!initse)   init();
            }

        } else {
            if (!initse)   init();
        }
        
   
    }
    private void init(){
        dialog1 = new ProgressDialog(LoginActivity.this);
        text_head = (TextView) findViewById(R.id.text_head);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/header_font.ttf");
        text_head.setTypeface(face);
        edit_user_name = (EditText) findViewById(R.id.edit_user_name);
        edit_pass = (EditText) findViewById(R.id.edit_pass);
        text_ver = (TextView) findViewById(R.id.text_ver);
        toggleButton = (ToggleButton) findViewById(R.id.watch_pass);
        button_login = (Button) findViewById(R.id.button_login);
        button_login.setOnClickListener(this);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edit_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    edit_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        /*if (CommonPref.getUserDetails(LoginActivity.this)!=null){
            if (!CommonPref.getUserDetails(LoginActivity.this).getUserID().trim().equals("")){
                edit_user_name.setText(""+CommonPref.getUserDetails(LoginActivity.this).getUserID());
            }
        }*/
        initse=true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        readPhoneState();
    }

    @Override
    public void onClick(View v) {
        if (edit_user_name.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Enter User Name", Toast.LENGTH_SHORT).show();
        } else if (edit_pass.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }

    }

    public void readPhoneState() {
        MARSHMALLOW_PERMISSION = new MarshmallowPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE);
        if (MARSHMALLOW_PERMISSION.result == -1 || MARSHMALLOW_PERMISSION.result == 0) {
            try {
                tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                if (tm != null)
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    imei = tm.getDeviceId();
                } else {
                    imei = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID).toUpperCase();
                    //imei = Utiilties.getIMEI_forAndroid10(LoginActivity.this);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                if (tm != null) ;
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    imei = tm.getDeviceId();
                } else {
                    imei = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID).toUpperCase();
                    //imei = Utiilties.getIMEI_forAndroid10(LoginActivity.this);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                serial_id = Build.getSerial();
            }else{
               // serial_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID).toUpperCase();
                serial_id=imei;
            }
        } else {
            serial_id = Build.SERIAL;
        }
        try {
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            Log.e("App Version : ", "" + version + " ( " + imei + "/" + serial_id + " )");
            text_ver.setText("App Version : " + version + " ( " + imei + " )");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String reqString(String req_string) {
        byte[] chipperdata = rsaEncrypt(req_string.getBytes());
        Log.e("chiperdata", new String(chipperdata));
        String encString = Base64.encodeToString(chipperdata, Base64.NO_WRAP);
        Log.e("string",""+encString);
        encString=encString.replaceAll("\\/","SSLASH").replaceAll("\\=","EEQUAL").replaceAll("\\+","PPLUS");
        Log.e("string",""+encString);
        return encString;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    byte[] rsaEncrypt(byte[] data) {

        try {
            //getKeyPath();
            //PublicKey pubKey = readKeyFromFile();
            PublicKey pubKey = readKeyFromFile();

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] cipherData = cipher.doFinal(data);
            return cipherData;
        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    PublicKey readKeyFromFile() throws IOException {
        ObjectInputStream oin = null;
        try (InputStream in = getKeyPath();) {
            oin = new ObjectInputStream(new BufferedInputStream(in));
            PublicKey pubKey = (PublicKey) oin.readObject();
            return pubKey;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Spurious serialisation error", e);
        } finally {
            oin.close();
        }
    }
    public  InputStream getKeyPath() {
        InputStream app;
        try {
            app = getApplicationContext().getAssets().open("public.key");

        } catch (Exception ex) {
            System.out.println(ex);
            app = null;
        }
        return app;
    }



    private boolean checkAndRequestPermissions() {
        int read_sms = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        int receve_sms = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (read_sms != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (receve_sms != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_SMS);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MY_PERMISSIONS_REQUEST_ACCOUNTS);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCOUNTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!initse) init();
                } else {
                    if (!initse) init();
                }
                break;
        }
    }
}
