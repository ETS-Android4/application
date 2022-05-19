package com.gttime.android.viewmodel.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

import com.gttime.android.util.CallbackListener;
import com.gttime.android.R;

import java.util.Map;

public class SemesterListAdapter <K,V> extends BaseAdapter {
    private Context context;
    private Map<K,V> semesterMap;
    private int selected;
    private Object[] keyset;
    private CallbackListener callbackListner;

    public SemesterListAdapter(Context context, Map<K,V> semesterMap, int selected) {
        this.context = context;
        this.semesterMap = semesterMap;
        this.selected = selected;
        this.keyset = semesterMap.keySet().toArray();
    }
    @Override
    public int getCount() {
        return semesterMap.size();
    }

    @Override
    public Object getItem(int position) {
        return semesterMap.get(keyset[position]);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v =  View.inflate(context, R.layout.semester,null);
        final RadioButton semesterButton = v.findViewById(R.id.semesterID);
        semesterButton.setText(semesterMap.get(keyset[position]).toString());

        if (selected == (int) keyset[position]) semesterButton.setChecked(true);
        else {semesterButton.setChecked(false);}
        semesterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = (int) keyset[position];
                SemesterListAdapter.this.notifyDataSetChanged();
                callbackListner.callback(keyset[position]);
            }
        });

        v.setTag(semesterMap.get(keyset[position]).toString());
        return v;
    }

    public void setCallback(CallbackListener listener) {

        this.callbackListner = listener;

    }
}
