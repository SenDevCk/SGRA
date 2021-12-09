package com.example.sgra;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar_sel_main;
    LinearLayout ll_topup, ll_setup, ll_consumer_list, ll_payment, ll_syncall, ll_statement;
    LinearLayout rel_profile, zxing_barcode_scanner2, change_mpin, share;
    View footer;
    TextView text_bav_bal;
    String[] mruLoaderString;
    BluetoothDevice con_dev = null;
    BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 99;
    int printid1 = 0;
    //BalanceLoader balance_loader;
    ImageView header_image, img_rotate;
    Animation rotation;
    ImageView imageViewheader;
    TextView text_header_name, text_header_mobile;
    String date_clik = "";
    TextView text_fromdate, text_todate, text_pen;
    private static final int MY_PERMISSIONS_REQUEST_ACCOUNTS = 1;
    //SyncroniseStatement syncroniseStatement;
    //SyncroniseStatementForMain syncroniseStatementForMain;
    //GetNEFTService getNEFTService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        toolbar_sel_main = (Toolbar) findViewById(R.id.toolbar_sel_main);
        toolbar_sel_main.setTitle("");
        setSupportActionBar(toolbar_sel_main);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar_sel_main, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //navigation header
        try {
            String version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            String code_v = String.valueOf(getPackageManager().getPackageInfo(getPackageName(), 0).versionCode);
            TextView app_name_tip = (TextView) navigationView.findViewById(R.id.app_name_tip);
            app_name_tip.setText(getResources().getString(R.string.app_name)+" ( " + code_v + "." + version + " ) V");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        View header = navigationView.getHeaderView(0);
        text_header_name = (TextView) header.findViewById(R.id.text_header_name);
        text_header_mobile = (TextView) header.findViewById(R.id.text_header_mobile);
        imageViewheader = (ImageView) header.findViewById(R.id.imageViewheader);

        //imageViewheader.setOnClickListener(this);
        header_image = (ImageView) findViewById(R.id.header_image);
        /*UserInfo2 userinfo = CommonPref.getUserDetails(MainActivity.this);
        if (userinfo.getUserID().startsWith("2")) {
            header_image.setImageDrawable(getResources().getDrawable(R.drawable.sblogo1));
            imageViewheader.setImageDrawable(getResources().getDrawable(R.drawable.sblogo1));
        } else if (userinfo.getUserID().startsWith("1")) {
            header_image.setImageDrawable(getResources().getDrawable(R.drawable.nblogo));
            imageViewheader.setImageDrawable(getResources().getDrawable(R.drawable.nblogo));

        }
        text_header_name.setText("" + userinfo.getUserName());
        text_header_mobile.setText("" + userinfo.getContactNo());*/

        text_pen = (TextView) findViewById(R.id.text_pen);
        text_pen.setVisibility(View.GONE);

        ll_topup = (LinearLayout) findViewById(R.id.ll_topup);
        ll_setup = (LinearLayout) findViewById(R.id.ll_setup);
        ll_consumer_list = (LinearLayout) findViewById(R.id.ll_consumer_list);
        ll_payment = (LinearLayout) findViewById(R.id.ll_payment);
        ll_syncall = (LinearLayout) findViewById(R.id.ll_syncall);
        ll_statement = (LinearLayout) findViewById(R.id.ll_statement);
        ll_topup.setOnClickListener(this);
        ll_setup.setOnClickListener(this);
        ll_consumer_list.setOnClickListener(this);
        ll_payment.setOnClickListener(this);
        ll_syncall.setOnClickListener(this);
        ll_statement.setOnClickListener(this);
        rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
        footerinit();
       /* UserInfo2 userInfo2 = CommonPref.getUserDetails(MainActivity.this);
        if (CommonPref.getCurrentDateForSync(MainActivity.this).equals("")){
            CommonPref.setCurrentDateForSync(MainActivity.this,Utiilties.getCurrentDateWithTime());
        }
        if (Utiilties.isOnline(MainActivity.this) && Utiilties.getCountOfDays(Utiilties.getCurrentDateWithTime(), CommonPref.getCurrentDateForSync(MainActivity.this)) >= 1) {
            syncroniseStatementForMain = (SyncroniseStatementForMain) new SyncroniseStatementForMain(MainActivity.this).execute(userInfo2.getUserID() + "|" + userInfo2.getPassword() + "|" + userInfo2.getImeiNo() + "|" + userInfo2.getSerialNo() + "|1");
        }*/
        /*if (Utiilties.isOnline(MainActivity.this)) {
            if (userInfo2 != null) {
                img_rotate.startAnimation(rotation);
                BalanceLoader.bindmListener(new BalanceListner() {
                    @Override
                    public void balanceReceived(final double balance) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public synchronized void run() {
                                text_bav_bal.setText(String.valueOf(balance).trim());
                                img_rotate.clearAnimation();
                            }
                        });
                    }
                });
                balance_loader = (BalanceLoader) (new BalanceLoader(MainActivity.this).execute(userInfo2.getUserID() + "|" + userInfo2.getPassword() + "|" + userInfo2.getImeiNo() + "|" + userInfo2.getSerialNo()));
            }
        }*/
    }

    private void footerinit() {
        //setPendingTransWeakup();
        footer = findViewById(R.id.footer);
        rel_profile = (LinearLayout) footer.findViewById(R.id.rel_profile);
        change_mpin = (LinearLayout) footer.findViewById(R.id.change_mpin);
        share = (LinearLayout) footer.findViewById(R.id.refresh);
        zxing_barcode_scanner2 = (LinearLayout) footer.findViewById(R.id.zxing_barcode_scanner2);
        img_rotate = (ImageView) footer.findViewById(R.id.img_rotate);
        rel_profile.setOnClickListener(this);
        change_mpin.setOnClickListener(this);
        zxing_barcode_scanner2.setOnClickListener(this);
        share.setOnClickListener(this);
        //qrScan = new IntentIntegrator(this);


        text_bav_bal = (TextView) findViewById(R.id.text_bav_bal);
        //text_bav_bal.setText(CommonPref.getUserDetails(MainActivity.this).getWalletAmount());


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.drawer_users) {
            startActivity(new Intent(MainActivity.this, ListActivity.class));

        } else if (id == R.id.drawer_batches) {
            startActivity(new Intent(MainActivity.this, ListActivity.class));
        } else if (id == R.id.drawer_attendee) {
            startActivity(new Intent(MainActivity.this, ListActivity.class));
        } else if (id == R.id.drawer_cert) {
            startActivity(new Intent(MainActivity.this, ListActivity.class));
        } else if (id == R.id.drawer_condidate_eligible) {
            startActivity(new Intent(MainActivity.this, ListActivity.class));
        } else if (id == R.id.drawer_councelling) {
            startActivity(new Intent(MainActivity.this, ListActivity.class));
        } else if (id == R.id.drawer_enrolments) {
           /* if (Utiilties.isOnline(MainActivity.this)) {
                UserInfo2 userInfo2 = CommonPref.getUserDetails(MainActivity.this);
                new MRUAlocatar(MainActivity.this, false).execute(userInfo2.getUserID() + "|" + userInfo2.getPassword() + "|" + userInfo2.getImeiNo() + "|" + userInfo2.getSerialNo());
            } else {
                Toast.makeText(MainActivity.this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
            }*/
        } else if (id == R.id.drawer_palcement) {
            startActivity(new Intent(MainActivity.this, ListActivity.class));

        } else if (id == R.id.drawer_events) {
            startActivity(new Intent(MainActivity.this, ListActivity.class));} else {
            //Toast.makeText(this, "Under Process..", Toast.LENGTH_SHORT).show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_payment) {

        } else if (v.getId() == R.id.ll_topup) {
            /* startActivity(new Intent(MainActivity.this, TopupActivity.class));*/

        } else if (v.getId() == R.id.ll_consumer_list) {
            //startActivity(new Intent(MainActivity.this, ConsumerListActivity.class).putExtra("from", "bmain"));
        } else if (v.getId() == R.id.ll_setup) {

        } else if (v.getId() == R.id.ll_syncall) {
            //code for synk

        } else if (v.getId() == R.id.ll_statement) {

        } else if (v.getId() == R.id.rel_profile) {
            //startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        } else if (v.getId() == R.id.change_mpin) {
            // startActivity(new Intent(MainActivity.this, ChangeMpinActivity.class));
        } else if (v.getId() == R.id.zxing_barcode_scanner2) {
            // qrScan.initiateScan();
            /*CommonPref.logout(MainActivity.this);
            Intent intent = new Intent(MainActivity.this, com.bih.nic.e_wallet.activities.LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);*/
        } else if (v.getId() == R.id.refresh) {

        }
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit ?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        MainActivity.super.onBackPressed();
                        //finish();
                    }

                }).create().show();

    }


}
