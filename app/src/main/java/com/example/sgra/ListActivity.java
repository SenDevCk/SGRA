package com.example.sgra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sgra.adapters.ConsumerItemAdapter;
import com.example.sgra.models.MRUEntity;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    RecyclerView recycler_list_consumer;
    TextView text_no_data_found;
    Toolbar toolbar_sel_topup;
    ConsumerItemAdapter consumerItemAdapter;
    ArrayList<MRUEntity> mruEntities;
    RelativeLayout rel_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        toolbar_sel_topup=(Toolbar)findViewById(R.id.toolbar_sel_topup);
        toolbar_sel_topup.setTitle("Search List");
        setSupportActionBar(toolbar_sel_topup);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar_sel_topup.setNavigationOnClickListener(v -> ListActivity.super.onBackPressed());
        text_no_data_found=(TextView) findViewById(R.id.text_no_data_found);
        recycler_list_consumer=(RecyclerView)findViewById(R.id.recycler_list_consumer);
        rel_search=(RelativeLayout)findViewById(R.id.rel_search);
        rel_search.setOnClickListener(v -> setUpDialog());
        mruEntities=new ArrayList<>();

        MRUEntity mruEntity=new MRUEntity();
        mruEntity.setCON_ID("sf35fdwf6");
        mruEntity.setCNAME("Chandan Singh");
        mruEntity.setBILL_ADDR1("Patna, Bihar");
        mruEntity.setACT_NO("BRO1DP3519");
        mruEntity.setBOOK_NO("2344");
        mruEntities.add(mruEntity);

        MRUEntity mruEntity1=new MRUEntity();
        mruEntity1.setCON_ID("sf35fdwf6");
        mruEntity1.setCNAME("Gunjan Rastogi");
        mruEntity1.setBILL_ADDR1("Patna, Bihar");
        mruEntity1.setACT_NO("BRO1DP3519");
        mruEntity1.setBOOK_NO("2344");
        mruEntities.add(mruEntity1);

        MRUEntity mruEntity2=new MRUEntity();
        mruEntity2.setCON_ID("sf35fdwf6");
        mruEntity2.setCNAME("Amit Bdana");
        mruEntity2.setBILL_ADDR1("Patna, Bihar");
        mruEntity2.setACT_NO("BRO1DP3519");
        mruEntity2.setBOOK_NO("2344");
        mruEntities.add(mruEntity);
        if (mruEntities!=null) {
            if (mruEntities.size() > 0) {
                text_no_data_found.setVisibility(View.GONE);
                consumerItemAdapter = new ConsumerItemAdapter(mruEntities, ListActivity.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ListActivity.this);
                recycler_list_consumer.setLayoutManager(mLayoutManager);
                recycler_list_consumer.setItemAnimator(new DefaultItemAnimator());
                recycler_list_consumer.setAdapter(consumerItemAdapter);
            } else {
                text_no_data_found.setVisibility(View.VISIBLE);
                recycler_list_consumer.setVisibility(View.GONE);
            }
        }
    }

    private void setUpDialog() {
        final Dialog setup_dialog = new Dialog(ListActivity.this);
        // Include dialog.xml file
        setup_dialog.setContentView(R.layout.search_layout);
        // Set dialog title
        setup_dialog.setTitle("");
        setup_dialog.setCancelable(false);
        // set values for custom dialog components - text, image and button
        final EditText edit_con_id = (EditText) setup_dialog.findViewById(R.id.edit_con_id);
        final EditText edit_acount_no = (EditText) setup_dialog.findViewById(R.id.edit_acount_no);
        final EditText edit_book_no = (EditText) setup_dialog.findViewById(R.id.edit_book_no);

        edit_con_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && edit_acount_no.getText().toString().trim().length() > 0) {
                    edit_acount_no.setText("");
                }
                if (s.length() > 0 && edit_book_no.getText().toString().trim().length() > 0) {
                    edit_book_no.setText("");
                }
            }
        });

        edit_acount_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && edit_con_id.getText().toString().trim().length() > 0) {
                    edit_con_id.setText("");
                }
                if (s.length() > 0 && edit_book_no.getText().toString().trim().length() > 0) {
                    edit_book_no.setText("");
                }
            }
        });
        edit_book_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && edit_acount_no.getText().toString().trim().length() > 0) {
                    edit_acount_no.setText("");
                }
                if (s.length() > 0 && edit_con_id.getText().toString().trim().length() > 0) {
                    edit_con_id.setText("");
                }
            }
        });
        ImageView close_setup = (ImageView) setup_dialog.findViewById(R.id.img_close);
        close_setup.setOnClickListener(v -> setup_dialog.dismiss());
        Button search_for_mru = (Button) setup_dialog.findViewById(R.id.search_for_mru);
        search_for_mru.setOnClickListener(v -> {
            // Close dialog
            /*UserInfo2 userInfo2= CommonPref.getUserDetails(ConsumerListActivity.this);
            if (edit_con_id.getText().toString().trim().length()>0) {
                setup_dialog.dismiss();
                new MRULoader().execute(userInfo2.getUserID() + "|" +userInfo2.getPassword() + "|" + userInfo2.getImeiNo() + "|" + userInfo2.getSerialNo() + "|NA|" + edit_con_id.getText().toString().trim() + "|NA");
            }else if (edit_acount_no.getText().toString().trim().length()>0) {
                setup_dialog.dismiss();
                new MRULoader().execute(userInfo2.getUserID() + "|" + userInfo2.getPassword() + "|" + userInfo2.getImeiNo() + "|" + userInfo2.getSerialNo() + "|NA|NA|" + edit_acount_no.getText().toString().trim());
            }
            else if (edit_book_no.getText().toString().trim().length()>0) {
                setup_dialog.dismiss();
                new MRULoader().execute(userInfo2.getUserID() + "|" + userInfo2.getPassword() + "|" + userInfo2.getImeiNo() + "|" + userInfo2.getSerialNo() +"|"+edit_book_no.getText().toString().trim() +"|NA|NA");
            }
            else {
                Toast.makeText(ConsumerListActivity.this, "Please Enter Cons Id or Account No or Book No", Toast.LENGTH_SHORT).show();
            }*/
            setup_dialog.dismiss();
        });
        setup_dialog.show();
    }
}