package com.example.taxidriverapp;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.taxidriverapp.activity.OrderActivity;

public class NetworkChangeReceiver extends BroadcastReceiver {
    String TAG = "NetworkChangeReceiver";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (checkInternet(context)) {
            sendToServer(context);
        }
    }

    boolean checkInternet(Context context) {
        ServiceManager serviceManager = new ServiceManager(context);
        return serviceManager.isNetworkAvailable();
    }

    void sendToServer(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //выгрузить из локальной базы не синхронезированые строки в глобальную
                DatabaseHelper dbHelper = new DatabaseHelper(context);
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor = db.query(DatabaseHelper.DATABASE_TABLE,
                        new String[]{
                                DatabaseHelper.CONTENT_IS_IN_LIST,
                                DatabaseHelper.CONTENT_LAT_FROM,
                                DatabaseHelper.CONTENT_LAT_TO,
                                DatabaseHelper.CONTENT_LON_TO,
                                DatabaseHelper.CONTENT_LON_FROM,
                                DatabaseHelper.CONTENT_IS_ADDRESS_FOUND,
                                DatabaseHelper.CONTENT_ADDRESS,
                                DatabaseHelper._ID},
                        null, null, null, null, null);
                while (cursor.moveToNext()) {
                    if (cursor.getInt(cursor.getColumnIndex(DatabaseHelper.CONTENT_IS_ADDRESS_FOUND)) == 0) {
                        //найти адрес
                        String addresses = "";
                        addresses = addresses + OrderActivity.getAddress(cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.CONTENT_LAT_FROM)),
                                cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.CONTENT_LON_FROM)), context);
                        addresses = addresses + " - " + OrderActivity.getAddress(cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.CONTENT_LAT_TO)),
                                cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.CONTENT_LON_TO)), context);

                        int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper._ID));
                        ContentValues args = new ContentValues();
                        args.put(DatabaseHelper.CONTENT_ADDRESS, addresses);
                        args.put(DatabaseHelper.CONTENT_IS_ADDRESS_FOUND, 1);
                        db.update(DatabaseHelper.DATABASE_TABLE, args, DatabaseHelper._ID + "=" + id, null);
                    }

                    if (cursor.getInt(cursor.getColumnIndex(DatabaseHelper.CONTENT_IS_IN_LIST)) == 0) {
                        //TODO отправить на сервер
                        //http://320320.ru:320/test_terminal/hs/mobile/sendorderstatus?imei=0123456789&login=998&Idorder=000000000000009&date=00/23/1234_18:00:00&statusorder=11&sum=100&priznaksum=0&priznakzagorodom=0&peregruzka=0&format=xml&os=android
                        int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper._ID));
                        ContentValues args = new ContentValues();
                        args.put(DatabaseHelper.CONTENT_IS_IN_LIST, 1);
                        db.update(DatabaseHelper.DATABASE_TABLE, args, DatabaseHelper._ID + "=" + id, null);
                    }

                }
                cursor.close();
            }
        }).start();
    }


    public class ServiceManager extends ContextWrapper {


        public ServiceManager(Context base) {
            super(base);
        }

        public boolean isNetworkAvailable() {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
    }
}