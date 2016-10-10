package com.example.taxidriverapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taxidriverapp.DatabaseHelper;
import com.example.taxidriverapp.R;

public class RecycleViewHistoryAdapter extends RecyclerView.Adapter<RecycleViewHistoryAdapter.ViewHolder> {

    int[] status;
    String[] startTime;
    String[] endTime;
    String[] payment;
    String[] address;
    Context context;
    public RecycleViewHistoryAdapter(Context context, SQLiteDatabase db) {
        this.context=context;
        int pointer=0;
        Cursor cursor = db.query(DatabaseHelper.DATABASE_TABLE,
                new String[]{
                        DatabaseHelper.CONTENT_START_TIME,
                        DatabaseHelper.CONTENT_ADDRESS,
                        DatabaseHelper.CONTENT_END_TIME,
                        DatabaseHelper.CONTENT_IS_IN_LIST,
                        DatabaseHelper.CONTENT_PAYMENT
                },
                null, null, null, null, null);
        this.status=new int[cursor.getCount()];
        this.startTime=new String[cursor.getCount()];
        this.endTime=new String[cursor.getCount()];
        this.payment=new String[cursor.getCount()];
        this.address=new String[cursor.getCount()];
        cursor.moveToLast();
        if(cursor.getCount()>0) {
            do {
                status[pointer] = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.CONTENT_IS_IN_LIST));
                startTime[pointer] = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CONTENT_START_TIME));
                endTime[pointer] = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CONTENT_END_TIME));
                payment[pointer] = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CONTENT_PAYMENT));
                address[pointer] = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CONTENT_ADDRESS));
                pointer++;
            } while (cursor.moveToPrevious());
        }
        cursor.close();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.address.setText(address[position]);
        holder.startTime.setText(String.format(context.getResources().getStringArray(R.array.history_params)[0],startTime[position]));
        holder.endTime.setText(String.format(context.getResources().getStringArray(R.array.history_params)[1],endTime[position]));
        holder.payment.setText(String.format(context.getResources().getStringArray(R.array.history_params)[2],payment[position]));
        if(status[position]==1){
            holder.statusImage.setBackgroundResource(R.drawable.vector_drawable_ic_check_circle_black___px);
        }else{
            holder.statusImage.setBackgroundResource(R.drawable.vector_drawable_ic_cached_black___px);
        }
    }

    @Override
    public int getItemCount() {
        return  status.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView address;
        public final TextView startTime;
        public final TextView endTime;
        public final TextView payment;
        public final ImageView statusImage;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            address = (TextView) view.findViewById(R.id.address);
            startTime = (TextView) view.findViewById(R.id.start_time);
            endTime = (TextView) view.findViewById(R.id.end_time);
            payment = (TextView) view.findViewById(R.id.payment);
            statusImage = (ImageView) view.findViewById(R.id.indication);
        }

        @Override
        public String toString() {
            return super.toString();
        }

    }

}