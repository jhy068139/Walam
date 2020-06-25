package com.example.whalam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    LinearLayout plusBtn;
    private ListView listView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plusBtn = findViewById(R.id.plusBtn);
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SetAlamActivity.class);
                startActivity(intent);
            }
        });

        // 데이터 1000개 생성--------------------------------.
        String[] strDate = {"GSM AP 01"};
        int nDatCnt=0;
        ArrayList<ItemData> oData = new ArrayList<>();
        for (int i=0; i<1000; ++i)
        {
            ItemData oItem = new ItemData();
            oItem.strTitle = "데이터 " + (i+1);
            oItem.strDate = strDate[nDatCnt++];
            oData.add(oItem);
            if (nDatCnt >= strDate.length) nDatCnt = 0;
        }

        // ListView, Adapter 생성 및 연결 ------------------------
        listView =findViewById(R.id.listview);
        ListAdapter oAdapter = new ListAdapter(oData);
        listView.setAdapter(oAdapter);
    }
}
