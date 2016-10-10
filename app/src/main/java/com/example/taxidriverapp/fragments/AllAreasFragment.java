package com.example.taxidriverapp.fragments;

import android.app.ProgressDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.taxidriverapp.R;
import com.example.taxidriverapp.TaxiServer;
import com.example.taxidriverapp.adapters.AreasAdapter;
import com.example.taxidriverapp.adapters.RecycleViewAddressesAdapter;
import com.example.taxidriverapp.models.Area;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class AllAreasFragment extends GeneralFragment {
    @BindView(R.id.areas_buttons)
    GridView areasButtonsView;
    private AreasAdapter adapter;
    private ArrayList<Button> areasButtons = new ArrayList<>();
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected int layout() {
        return R.layout.all_areas;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter = new AreasAdapter(areasButtons);
        areasButtonsView.setAdapter(adapter);
        if (TaxiServer.Data.areas.size() == 0) {
            getAreas();
        } else {
            addButtons();
        }
    }

    private void getOrdersAddresses(String areaName, String addresses) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        final AlertDialog alertDialog = alert.create();
        alertDialog.setTitle(String.format(getString(R.string.area_alert_title), areaName));
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View promptView = layoutInflater.inflate(R.layout.alert_area, null);

        RecyclerView addressesList = (RecyclerView) promptView.findViewById(R.id.addresses_list);
        addressesList.setLayoutManager(new LinearLayoutManager(getContext()));
        final RecycleViewAddressesAdapter adapter = new RecycleViewAddressesAdapter(getContext(), addresses);
        addressesList.setAdapter(adapter);

        Button okButton = (Button) promptView.findViewById(R.id.apply);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                //TODO запрос статуса заказа
                //TODO если заказ свободен, занять его и перейти на активность с заказом
                Toast.makeText(getContext(), adapter.getSelectedAddress(), Toast.LENGTH_LONG).show();
            }
        });
        Button cancelButton = (Button) promptView.findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setView(promptView);
        alertDialog.show();
    }

    private void getAreas() {
        final ProgressDialog progress = new ProgressDialog(getContext());
        progress.setTitle(getString(R.string.loading));
        progress.setMessage(getString(R.string.wait_loading));
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);
        progress.show();
        Observable.just(true)
                .map(new Func1<Boolean, String>() {
                    @Override
                    public String call(Boolean b) {
                        return TaxiServer.getInstance().getAreas();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        progress.dismiss();
                    }

                    @Override
                    public void onNext(final String result) {
                        progress.dismiss();
                        try {
                            JSONArray jsonObjects = new JSONArray(result);
                            for (int i = 0; i < jsonObjects.length(); i++) {
                                Area area = mapper.readValue(jsonObjects.get(i).toString(), Area.class);
                                TaxiServer.Data.areas.add(area);
                            }
                            addButtons();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void addButtons() {
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (final Area area : TaxiServer.Data.areas) {
            final Button areaButton = new Button(new ContextThemeWrapper(getContext(), R.style.allAreasButton), null, R.style.allAreasButton);
            areaButton.setLayoutParams(lp);
            Observable.just(true)
                    .map(new Func1<Boolean, String>() {
                        @Override
                        public String call(Boolean b) {
                            return TaxiServer.getInstance().countDriversInArea(area.getId());
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(final String result) {
                            areaButton.setText(area.getName() + "\n" + "исполнителей: " + result);
                            areasButtons.add(areaButton);
                            adapter.notifyDataSetChanged();
                        }
                    });
        }
    }

}
