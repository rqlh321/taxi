package com.example.taxidriverapp.activity;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.taxidriverapp.DatabaseHelper;
import com.example.taxidriverapp.GpsService;
import com.example.taxidriverapp.R;
import com.example.taxidriverapp.TaxiServer;
import com.example.taxidriverapp.Utils;

import java.text.DateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.utils.GeoPoint;

public class OrderActivity extends AppCompatActivity {
    @BindView(R.id.my_speed)
    TextView mySpeedView;
    @BindView(R.id.map)
    MapView mapView;
    @BindView(R.id.order_begin_set)
    LinearLayout orderBegin;
    @BindView(R.id.order_active_set)
    LinearLayout orderActive;
    @BindView(R.id.order_end_set)
    LinearLayout orderEnd;
    @BindView(R.id.order_status)
    TextView orderStatus;
    @BindView(R.id.prise)
    TextView prise;
    @BindView(R.id.on_wait_click)
    LinearLayout waitClick;
    @BindView(R.id.priseView)
    LinearLayout priseView;
    @BindView(R.id.timer)
    TextView timer;
    @BindView(R.id.distance)
    TextView distance;
    @BindView(R.id.counter)
    TextView counter;
    private MapController mapController;
    private double latFrom = 0.0;
    private double lonFrom = 0.0;
    private double latTo = 0.0;
    private double lonTo = 0.0;
    private double totalSpeed = 0.0;
    private int totalTime = 0;
    private double totalDistance = 0.0;
    private int inCity = 14;
    private int inning = 40;
    private int timeRate = 4;
    private int counterValue = 40;
    private boolean paused = false;
    private boolean doNotHaveStartCoordinates = true;
    private boolean needToGetStartCoordinates = false;
    private Date startTime;
    private Date endTime;

    @OnClick(R.id.on_fixation_click)
    void fixation() {
        orderStatus.setText(String.format(getString(R.string.order_status),
                getResources().getStringArray(R.array.order_status_values)[0]));
        orderBegin.setVisibility(View.GONE);
        orderActive.setVisibility(View.VISIBLE);
        //запуск счетчика для приема клиента с сервера
        startTimer();
        startTime = new Date();
        needToGetStartCoordinates = true;
    }

    @OnClick(R.id.ready_to_go_click)
    void sendSms() {
        //TODO запрос на смс клиенту
    }

    @OnClick(R.id.on_wait_click)
    void pause() {
        if (paused) {
            //снять с паузы
            paused = false;
            waitClick.setBackgroundResource(R.drawable.right_order_button_normal);
            startTime = new Date();
        } else {
            //поставить время на паузу
            paused = true;
            waitClick.setBackgroundResource(R.drawable.right_order_button_pressed);
            endTime = new Date();
        }
    }

    @OnClick(R.id.on_calculation_click)
    void calculation() {
        //показать сумму на весь экран
        priseView.setVisibility(View.VISIBLE);
        prise.setText(String.format(getString(R.string.pay_info), counterValue));
        orderStatus.setText(String.format(getString(R.string.order_status),
                getResources().getStringArray(R.array.order_status_values)[1]));
        orderActive.setVisibility(View.GONE);
        orderEnd.setVisibility(View.VISIBLE);
        if (!paused) {
            endTime = new Date();
        }//если расчет произошел во время переезда
        paused = false;
    }

    @OnClick(R.id.on_go_on_click)
    void goOn() {
        //убрать сумму
        priseView.setVisibility(View.INVISIBLE);
        orderStatus.setText(String.format(getString(R.string.order_status),
                getResources().getStringArray(R.array.order_status_values)[0]));
        orderEnd.setVisibility(View.GONE);
        orderActive.setVisibility(View.VISIBLE);
        startTime = new Date();
    }

    @OnClick(R.id.on_pay_click)
    void pay() {
        addToDb(startTime, endTime, prise.getText().toString(), OrderActivity.this);
        finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        orderBegin.setVisibility(View.VISIBLE);

        GpsReceiver gpsReceiver = new GpsReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GpsService.MY_ACTION);
        registerReceiver(gpsReceiver, intentFilter);

        mapController = mapView.getMapController();
        mapController.setPositionNoAnimationTo(new GeoPoint(47.222184, 38.919234));
    }

    private void addToDb(final Date startTime, final Date endTime, final String price, final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseHelper dbHelper = new DatabaseHelper(context);
                SQLiteDatabase sdb = dbHelper.getReadableDatabase();
                ContentValues values = new ContentValues();
                DateFormat df = DateFormat.getTimeInstance(DateFormat.SHORT);
                values.put(DatabaseHelper.CONTENT_START_TIME, df.format(startTime));
                values.put(DatabaseHelper.CONTENT_END_TIME, df.format(endTime));
                values.put(DatabaseHelper.CONTENT_LAT_FROM, latFrom);
                values.put(DatabaseHelper.CONTENT_LON_FROM, lonFrom);
                values.put(DatabaseHelper.CONTENT_LAT_TO, latTo);
                values.put(DatabaseHelper.CONTENT_LON_TO, lonTo);
                values.put(DatabaseHelper.CONTENT_PAYMENT, price);
                values.put(DatabaseHelper.CONTENT_ID_IN_GLOBAL_DB, TaxiServer.Data.getInstance().order.getId());

                if (isNetworkAvailable()) {
                    String addresses = "";
                    addresses = addresses + Utils.getAddress(latFrom, lonFrom, OrderActivity.this);
                    addresses = addresses + " - " + Utils.getAddress(latTo, lonTo, OrderActivity.this);
                    values.put(DatabaseHelper.CONTENT_ADDRESS, addresses);
                    values.put(DatabaseHelper.CONTENT_IS_ADDRESS_FOUND, 1);
                    //отправка данных о завершенном заказе на сервер
                    TaxiServer.getInstance().finishOrder(TaxiServer.Data.getInstance().order.getId());
                    values.put(DatabaseHelper.CONTENT_IS_IN_LIST, 1);
                } else {
                    values.put(DatabaseHelper.CONTENT_ADDRESS, "");
                    values.put(DatabaseHelper.CONTENT_IS_ADDRESS_FOUND, 0);
                    values.put(DatabaseHelper.CONTENT_IS_IN_LIST, 0);
                }
                sdb.insert(DatabaseHelper.DATABASE_TABLE, null, values);
            }
        }).start();
    }

    private void startTimer() {
        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!paused) {
                    totalTime++;
                }
                totalDistance = totalDistance + totalSpeed;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timer.setText(String.format(getString(R.string.order_time), TimeUnit.MILLISECONDS.toMinutes(totalTime * 1000)));
                        distance.setText(getString(R.string.order_distance, String.valueOf((int) totalDistance / 1000)));
                        counterValue = inning + (int) (totalDistance / 1000) * inCity + totalTime / 60 * timeRate;
                        counter.setText(getString(R.string.pay_info, counterValue));
                    }
                });
            }
        }, 0, 1000);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private class GpsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (doNotHaveStartCoordinates && needToGetStartCoordinates) {
                mapController.setZoomCurrent(17);
                latFrom = intent.getDoubleExtra("lat", 0.0);
                lonFrom = intent.getDoubleExtra("lon", 0.0);
                doNotHaveStartCoordinates = false;
            }
            latTo = intent.getDoubleExtra("lat", 0.0);
            lonTo = intent.getDoubleExtra("lon", 0.0);
            totalSpeed = intent.getDoubleExtra("speed", 0.0);
            mySpeedView.setText(getString(R.string.order_speed, String.valueOf((int) totalSpeed * 3600 / 1000)));
            mapController.setPositionAnimationTo(new GeoPoint(latTo, lonTo));
        }

    }

}
