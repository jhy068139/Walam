package com.example.whalam;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import static android.app.PendingIntent.getActivity;
import static java.security.AccessController.getContext;

public class SetAlamActivity extends AppCompatActivity {

    @Bind(R.id.widget_recyclerview)
    RecyclerView recyclerView;


    private WifiScanAdapter Adapter;
    private WifiManager wifiManager;
    private List<ScanResult> scanDatas; // ScanResult List

    ImageView backIcon;
    ListView NowList,AbList,PvList;
    String[] strDate = {"GSM AP 01"};

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if(action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                scanDatas = wifiManager.getScanResults();
                Adapter.setResults(scanDatas);
            }else if(action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                getContext().sendBroadcast(new Intent("wifi.ON_NETWORK_STATE_CHANGED"));
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alam);






        WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

        if(ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)){

            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

        String ipAddressString;
        try {
            //내 아이피 가져오기
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        }catch (UnknownHostException ex){
            // 아이피 못가져 왔을때
            Log.e("WIFIIP","Unable to get Host address.");
            ipAddressString =null;
        }

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String wifi =  wifiInfo.getSSID();

        backIcon = findViewById(R.id.backIcon);

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SettingActivity.class);
                startActivity(intent);
            }
        });

        NowList = findViewById(R.id.NowList);
        AbList = findViewById(R.id.AbList);
        PvList = findViewById(R.id.recyclerView);

        final ArrayList<String> DataSet = new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_single_choice,DataSet);
        DataSet.add(strDate[0]+ipAddress+wifi);
        NowList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        AbList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        PvList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        NowList.setAdapter(adapter);
        AbList.setAdapter(adapter);
        PvList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter = new WifiScanAdapter(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilt   er(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        getContext().registerReceiver(receiver, intentFilter);
        wifiManager.startScan();
    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().unregisterReceiver(receiver);
    }


}
