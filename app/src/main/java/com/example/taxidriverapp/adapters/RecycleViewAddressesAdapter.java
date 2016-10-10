package com.example.taxidriverapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.taxidriverapp.R;

public class RecycleViewAddressesAdapter extends RecyclerView.Adapter<RecycleViewAddressesAdapter.ViewHolder> {
    int selectedPosition = -1;
    Context context;
    String[] addresses;


    public RecycleViewAddressesAdapter(Context context, String addresses) {
        this.context = context;
        this.addresses = addresses.split(",");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.item.setSelected(selectedPosition == position);
        String address = addresses[position].split("\\[")[1];
        holder.startLocation.setText(address.substring(0, address.length() - 2));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Updating old as well as new positions
                notifyItemChanged(selectedPosition);
                selectedPosition = position;
                notifyItemChanged(selectedPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return addresses.length;
    }

    public String getSelectedAddress() {
        if (selectedPosition != -1) {
            return addresses[selectedPosition].split("\\[")[0].trim();
        } else return "";
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView startLocation;
        //public final TextView destination;
        public final LinearLayout item;

        public ViewHolder(View view) {
            super(view);

            mView = view;
            startLocation = (TextView) view.findViewById(R.id.starting_place);
            //destination = (TextView) view.findViewById(R.id.destination);
            item = (LinearLayout) view.findViewById(R.id.address_item);
        }

        @Override
        public String toString() {
            return super.toString();
        }

    }

}