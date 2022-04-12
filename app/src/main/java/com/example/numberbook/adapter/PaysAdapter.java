package com.example.numberbook.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.numberbook.classes.Pays;

import java.util.List;

public class PaysAdapter extends BaseAdapter {

    private List<Pays> pays ;
    private LayoutInflater inflater;

    public PaysAdapter(List<Pays> pays, Activity activity) {
        this.pays = pays;
        this.inflater =(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return pays.size();
    }

    @Override
    public Object getItem(int i) {
        return pays.get(i);
    }

    @Override
    public long getItemId(int i) {
        return pays.get(i).getIdP();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
