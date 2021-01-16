package com.example.bluetoothmonitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.bluetoothmonitor.adapter.BtAdapter;
import com.example.bluetoothmonitor.adapter.ListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BtListActivity extends AppCompatActivity {

    private ListView listView;
    private BtAdapter adapter;
    private BluetoothAdapter bluetoothAdapter;
    private List<ListItem> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt_list);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void initialization(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        list=new ArrayList<>();

        ActionBar actionBar = getSupportActionBar();
        if(actionBar ==null) return;
        actionBar.setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.listView);

//        List<ListItem> list = new ArrayList<>();
//        ListItem item = new ListItem();
//        item.setBtName("BT 133131321");
//        list.add(item);
//        list.add(item);
//        list.add(item);
//        list.add(item);

        adapter = new BtAdapter(this, R.layout.bt_list_item , list);
        listView.setAdapter(adapter);

        getDevices();
    }

   private void getDevices (){

       Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

       if(pairedDevices.size()>0){
           list.clear();
           for(BluetoothDevice device: pairedDevices){
               ListItem item = new ListItem();
               item.setBtName(device.getName());
               item.setMacAdress(device.getAddress());//mac
               list.add(item);
           }
           adapter.notifyDataSetChanged();
       }


   }

}