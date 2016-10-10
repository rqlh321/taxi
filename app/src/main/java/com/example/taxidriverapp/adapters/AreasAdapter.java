package com.example.taxidriverapp.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;

public class AreasAdapter extends BaseAdapter {
    ArrayList<Button> areasButtons = new ArrayList<>();

    public AreasAdapter(ArrayList<Button> areasButtons) {
        this.areasButtons = areasButtons;
    }

    @Override
    public int getCount() {
        return areasButtons.size();
    }

    @Override
    public Object getItem(int arg0) {
        return areasButtons.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return areasButtons.get(position);
    }

}

