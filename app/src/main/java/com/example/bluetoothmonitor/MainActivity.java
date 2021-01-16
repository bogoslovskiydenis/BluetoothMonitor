package com.example.bluetoothmonitor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private MenuItem menuItem;
    private BluetoothAdapter bluetoothAdapter;
    private final int ENABLE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //разкомитить для телефона , т.к на эмуляторе нет адаптера
       // initialization();
    }

    //создание кнопки Блютуз
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu , menu);
        menuItem = menu.findItem(R.id.bt_button);
        //разкомитить для телефона , т.к на эмуляторе нет адаптера
       // setBtnIcon();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.bt_button){
            if(bluetoothAdapter.isEnabled()){
                enableBluetooth();
            }else {
                bluetoothAdapter.disable();
                menuItem.setIcon(R.drawable.ic_bt_enable);
            }
            //запуск ListMenu
        } else if (item.getItemId() == R.id.id_memu){
            Intent i = new Intent(MainActivity.this, BtListActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    //запрос можно ли включить
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ENABLE_REQUEST){
            if(resultCode == RESULT_OK){
                setBtnIcon();
            }
        }
    }

    //состояние блютуза
    private void setBtnIcon(){
        if(bluetoothAdapter.isEnabled()){
            menuItem.setIcon(R.drawable.ic_bt_disable);
        }
        else {
            menuItem.setIcon(R.drawable.ic_bt_enable);
        }

    }

    //инизиализация
    private void initialization(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    private void enableBluetooth(){
        Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(i, ENABLE_REQUEST);
    }


}

