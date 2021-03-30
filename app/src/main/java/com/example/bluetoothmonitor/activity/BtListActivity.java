package com.example.bluetoothmonitor.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bluetoothmonitor.R;
import com.example.bluetoothmonitor.adapter.BtAdapter;
import com.example.bluetoothmonitor.model.ListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BtListActivity extends AppCompatActivity {

    private ListView listView;
    private BtAdapter adapter;
    private BluetoothAdapter bluetoothAdapter;
    private List<ListItem> list;
    private boolean isBtPermissionGranted =false;
    private final int BT_REQUEST_PERM = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt_list);
        initialization();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //when device found ->to BroadcastReceiver
        IntentFilter intentFilter1 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        //when state changed ->to BroadcastReceiver
        IntentFilter intentFilter2 = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(broadcastReceiver, intentFilter1);
        registerReceiver(broadcastReceiver, intentFilter2);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
        }
        bluetoothAdapter.startDiscovery();

        return super.onOptionsItemSelected(item);
    }

    private void initialization(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        list=new ArrayList<>();

        ActionBar actionBar = getSupportActionBar();
        if(actionBar ==null) return;
        actionBar.setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.listView);

        //Проверка , заполняем Блютуз item
//        List<ListItem> list = new ArrayList<>();
//        ListItem item = new ListItem();
//        item.setBtName("BT 133131321");
//        list.add(item);
//        list.add(item);
//        list.add(item);
//        list.add(item);

        adapter = new BtAdapter(this, R.layout.bt_list_item , list);
        listView.setAdapter(adapter);
        getBtPermission();
        getDevices();

    }

    //Query paired devices
   private void getDevices (){

       Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

       if(pairedDevices.size()>0){
           //paired devices. Get name and address paired devices
           list.clear();
           for(BluetoothDevice device: pairedDevices){
               ListItem item = new ListItem();
               item.setBtName(device.getName());
               item.setMacAddress(device.getAddress());//mac
               list.add(item);
           }
           adapter.notifyDataSetChanged();
       }

   }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == BT_REQUEST_PERM){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                isBtPermissionGranted =true;
                Toast.makeText(this, "Разрешение получено!", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Нет разрешения на поиск Бт устройства", Toast.LENGTH_SHORT).show();
            }
        }else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    //Доступ к положению смартфона
    private void getBtPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, BT_REQUEST_PERM);
        }else {
            isBtPermissionGranted = true;
        }
    }


    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Toast.makeText(context, "Найдено устройство , имя :" + device.getName(), Toast.LENGTH_SHORT).show();
            }
        }
    };

}