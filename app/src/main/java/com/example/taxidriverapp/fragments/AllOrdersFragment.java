package com.example.taxidriverapp.fragments;

import android.app.ProgressDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.taxidriverapp.R;
import com.example.taxidriverapp.TaxiServer;
import com.example.taxidriverapp.adapters.AddressesAdapter;

import butterknife.BindView;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class AllOrdersFragment extends GeneralFragment {
    @BindView(R.id.addresses_list)
    RecyclerView addresses;
    AddressesAdapter adapter;

    @Override
    protected int layout() {
        return R.layout.fragment_all_orders;
    }

    @Override
    public void onStart() {
        super.onStart();
        addresses.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AddressesAdapter(getContext(), TaxiServer.Data.getInstance().orders);
        addresses.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        getOrders();
    }

    private void getOrders() {
        TaxiServer.Data.getInstance().orders.clear();
        final ProgressDialog progress = new ProgressDialog(getContext());
        progress.setTitle(getString(R.string.loading));
        progress.setMessage(getString(R.string.wait_loading));
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);
        progress.show();

        Observable.just(true)
                .map(new Func1<Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean aBoolean) {
                        TaxiServer.getInstance().getOrders();
                        return aBoolean;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        progress.dismiss();
                    }

                    @Override
                    public void onNext(final Boolean result) {
                        progress.dismiss();
                        adapter.notifyDataSetChanged();
                    }
                });
    }

}
