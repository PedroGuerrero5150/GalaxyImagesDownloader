package com.example.proyecto6;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Collection;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private static Data mData = Data.getInstance();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historyactivity_layout);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Context context = this;

        //binding elementos gui
        TextView historyTextView = findViewById(R.id.historyTextView);
        Button clearDataButton = findViewById(R.id.clearDataButton);
        mData = Data.getInstance();



        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            public void run() {
                // Get the stack of Activity lifecycle methods called and print to TextView
                StringBuilder sbHistory = new StringBuilder();
                Collection <String> listHistory = mData.getHistory().values();
                for (String h : listHistory) {
                    Log.i("HistoryActivity.OnCreate()", h);
                    sbHistory.insert(0, h + "\r\n");
                }
                if(historyTextView != null) {
                    historyTextView.setText(sbHistory.toString());
                }

                else
                    Log.i("HistoryActivity.OnCreate()", "aca");

            }
        }, 500);

        clearDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.clear();
                finish();
            }

        });




    }
}
