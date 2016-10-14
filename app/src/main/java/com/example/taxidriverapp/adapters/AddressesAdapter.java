package com.example.taxidriverapp.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taxidriverapp.Order;
import com.example.taxidriverapp.R;
import com.example.taxidriverapp.TaxiServer;
import com.example.taxidriverapp.activity.OrderActivity;

import java.util.ArrayList;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.ViewHolder> {
    Context context;
    ArrayList<Order> orders;

    public AddressesAdapter(Context context, ArrayList<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_address, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.startLocation.setText(orders.get(position).getAddress());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOrderDialog(orders.get(position));
            }
        });
    }

    private void getOrderDialog(final Order order) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setTitle(order.getAddress());
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.alert_order, null);

        Button okButton = (Button) promptView.findViewById(R.id.apply);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                TaxiServer.Data.getInstance().order = order;
                takeOrder(order.getId());
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

    private void takeOrder(final int id) {
        TaxiServer.Data.getInstance().orders.clear();
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setTitle(context.getString(R.string.loading));
        progress.setMessage(context.getString(R.string.wait_loading));
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);
        progress.show();

        Observable.just(id)
                .map(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer id) {
                        return TaxiServer.getInstance().takeOrder(id);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        progress.dismiss();
                    }

                    @Override
                    public void onNext(final Integer result) {
                        progress.dismiss();
                        if (result == 0) {
                            Toast.makeText(context, context.getString(R.string.denie), Toast.LENGTH_LONG).show();
                        } else {
                            context.startActivity(new Intent(context, OrderActivity.class));
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView startLocation;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            startLocation = (TextView) view.findViewById(R.id.starting_place);
        }
    }

}