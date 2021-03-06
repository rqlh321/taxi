package com.example.taxidriverapp.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.taxidriverapp.DatabaseHelper;
import com.example.taxidriverapp.R;
import com.example.taxidriverapp.adapters.RecycleViewHistoryAdapter;

import butterknife.BindView;

public class HistoryFragment extends GeneralFragment {
    @BindView(R.id.history_list)
    RecyclerView recyclerView;

    @Override
    protected int layout() {
        return R.layout.history;
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        SQLiteDatabase sdb = dbHelper.getReadableDatabase();
        RecycleViewHistoryAdapter adapter = new RecycleViewHistoryAdapter(getContext(), sdb);
        recyclerView.setAdapter(adapter);
    }
}
