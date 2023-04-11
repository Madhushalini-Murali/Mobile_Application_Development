package com.example.tipcalculator;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText InputBill,CountPeople;
    TextView TipAmt,TotalTip,TotalPerHead;
    Button go,clear;
    RadioGroup radiogrp;

    float totalAmt= 0.0f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InputBill = (EditText) findViewById(R.id.InputBill);
        CountPeople = (EditText) findViewById(R.id.CountPeople);
        TipAmt= (TextView) findViewById(R.id.TipAmt);
        TotalTip= (TextView) findViewById(R.id.TotalTip);
        TotalPerHead= (TextView) findViewById(R.id.TotalPerHead);
        radiogrp= (RadioGroup) findViewById(R.id.radiogroup);
        go= (Button) findViewById(R.id.go);
        clear= (Button) findViewById(R.id.clear);
        clear.setOnClickListener(clear_button -> {
            InputBill.setText("");
            CountPeople.setText("");
            TipAmt.setText("");
            TotalTip.setText("");
            TotalPerHead.setText("");
            radiogrp.clearCheck();
        });

    }

    public void Calc_total_per_person(View v){
        String count = CountPeople.getText().toString();

        String Total = TotalTip.getText().toString();
        if(!count.equals("") && !Total.equals("") && !count.equals("0")){
            int count_of_people = Integer.parseInt(count);
            if(Total.charAt(0) == '$'){
                Total = Total.substring(1);
            }
            else {
                InputBill.setText(String.format("$"+Total));
            }
            if(Total!="" && count_of_people!=0){

                double Total_split = Double.parseDouble(Total)/count_of_people;
                //Total_split+=0.01;
                TotalPerHead.setText(String.format("$%.2f", Total_split));
            }
        }


    }
    public void Calc_Tip(View v){
        String Total = InputBill.getText().toString();

        if(!Total.equals("")){
            if(Total.charAt(0) == '$'){
                Total = Total.substring(1);
            }
            else {
                InputBill.setText(String.format("$"+Total));
            }
            Log.d("main","Total: "+Total);
            Double Total_bill = Double.parseDouble(Total);
            Double tip = 0.0;
            Double total_tip= 0.0;
            Double total_amt=0.0;
            if(v.getId()==R.id.percent_twelve)
                tip = 0.12;
            else if(v.getId()== R.id.percent_fifteen)
                tip=0.15;
            else if(v.getId() == R.id.percent_eighteen)
                tip=0.18;
            else if(v.getId() == R.id.percent_twenty)
                tip=0.20;
            total_tip= tip*Total_bill;
            total_amt = total_tip+Total_bill;
            TipAmt.setText(String.format("$%.2f",total_tip));
            TotalTip.setText(String.format("$%.2f",total_amt));

        }
        else radiogrp.clearCheck();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("KEY_TipAmt", TipAmt.getText().toString());
        savedInstanceState.putString("KEY_TotalTip", TotalTip.getText().toString());
        savedInstanceState.putString("KEY_TotalPerHead",TotalPerHead.getText().toString());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String tipAmt= savedInstanceState.getString("KEY_TipAmt");
        TipAmt.setText(tipAmt);
        String totAmt=savedInstanceState.getString("KEY_TotalTip");;
        TotalTip.setText(totAmt);
        String totHead=savedInstanceState.getString("KEY_TotalPerHead");;
        TotalPerHead.setText(totHead);
    }
}