package com.example.bluetoothmonitor.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bluetoothmonitor.R;

import java.util.ArrayList;
import java.util.List;

public class BtAdapter extends ArrayAdapter<ListItem> {

    private List<ListItem> mainList;
    private List<ViewHolder> listViewHolders;
    private SharedPreferences sharedPreferences;

    public BtAdapter(@NonNull Context context, int resource, List<ListItem> btList) {
        super(context, resource, btList);
        mainList = btList;
        listViewHolders = new ArrayList<>();
        sharedPreferences = context.getSharedPreferences(BtConsts.MY_PREFERENCE, Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {

            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bt_list_item, null, false);
            viewHolder.textView = convertView.findViewById(R.id.textView);
            viewHolder.checkBoxSelected = convertView.findViewById(R.id.checkBox);
            convertView.setTag(viewHolder);

            listViewHolders.add(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textView.setText(mainList.get(position).getBtName());
        viewHolder.checkBoxSelected.setOnClickListener(v -> {
            for(ViewHolder holder: listViewHolders){
                holder.checkBoxSelected.setChecked(false);
            }
            viewHolder.checkBoxSelected.setChecked(true);
            savePreference(position);
        });
       // viewHolder.checkBoxSelected.setChecked(true);
        if (sharedPreferences.getString(BtConsts.MAC_KEY, "выбрать подключение")
                .equals(mainList.get(position)
                        .getMacAdress()))viewHolder.checkBoxSelected.setChecked(true);
        return convertView;
    }

    private void savePreference(int pos){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BtConsts.MAC_KEY,  mainList.get(pos).getMacAdress());
        editor.apply();
    }

    static class ViewHolder {

        TextView textView;
        CheckBox checkBoxSelected;
    }
}
