package com.example.taxidriverapp.activity;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.taxidriverapp.DatabaseHelper;
import com.example.taxidriverapp.R;
import com.example.taxidriverapp.fragments.AllAreasFragment;
import com.example.taxidriverapp.GpsService;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.utils.GeoPoint;

public class OrderActivity extends AppCompatActivity {
    double latFrom = 0.0;
    double lonFrom = 0.0;
    double latTo = 0.0;
    double lonTo = 0.0;

    double totalSpeed = 0.0;
    int totalTime = 0;
    double totalDistance = 0.0;

    int inCity = 14;
    int outOfCity = 16;
    int inning = 40;
    int timeRate = 4;
    int counterValue = 40;

    boolean paused = false;
    boolean doNotHaveStartCoordinates = true;
    boolean needToGetStartCoordinates = false;

    MapController mapController;
    TextView mySpeedView;

    public static String getAddress(double lat, double lon, Context context) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> address = null;
        try {
            address = geocoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (address != null && address.size() > 0) {
            String street = address.get(0).getThoroughfare();
            String houseNumber = address.get(0).getSubThoroughfare();
            return street + ", " + houseNumber;
        }
        return "";
    }

    @Override
    public void onStart() {
        super.onStart();
        GpsReceiver gpsReceiver = new GpsReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GpsService.MY_ACTION);
        registerReceiver(gpsReceiver, intentFilter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mySpeedView = (TextView) findViewById(R.id.my_speed);
        MapView mapView = (MapView) findViewById(R.id.map);
        mapController = mapView.getMapController();
        mapController.setPositionNoAnimationTo(new GeoPoint(47.222184, 38.919234));
    }

    protected int topLayoutId() {
        return R.layout.order;
    }

    protected int bottomLayoutId() {
        return R.layout.button_set_order;
    }

    protected void viewLayout() {

    }

    protected void buttonLayout() {
        final LinearLayout orderBegin = (LinearLayout) findViewById(R.id.order_begin_set);
        final LinearLayout orderActive = (LinearLayout) findViewById(R.id.order_active_set);
        final LinearLayout orderEnd = (LinearLayout) findViewById(R.id.order_end_set);
        final TextView orderStatus = (TextView) findViewById(R.id.order_status);
        final TextView prise = (TextView) findViewById(R.id.prise);
        final Date[] startTime = new Date[1];
        final Date[] endTime = new Date[1];
        orderBegin.setVisibility(View.VISIBLE);

       final LinearLayout[] onFixation = {(LinearLayout) findViewById(R.id.on_fixation_click)};
        onFixation[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderStatus.setText(String.format(getString(R.string.order_status),
                        getResources().getStringArray(R.array.order_status_values)[0]));
                orderBegin.setVisibility(View.GONE);
                orderActive.setVisibility(View.VISIBLE);
                //запуск счетчика для приема клиента с сервера
                startTimer();
                startTime[0] = new Date();
                needToGetStartCoordinates = true;
            }
        });

        final LinearLayout readyToGoClick = (LinearLayout) findViewById(R.id.ready_to_go_click);
        readyToGoClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO запрос на смс клиенту
            }
        });

        final LinearLayout waitClick = (LinearLayout) findViewById(R.id.on_wait_click);
        waitClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paused) {
                    //снять с паузы
                    paused = false;
                    waitClick.setBackgroundResource(R.drawable.right_order_button_normal);
                    startTime[0] = new Date();
                } else {
                    //поставить время на паузу
                    paused = true;
                    waitClick.setBackgroundResource(R.drawable.right_order_button_pressed);
                    endTime[0] = new Date();
                }
            }
        });

        final LinearLayout priseView = (LinearLayout) findViewById(R.id.priseView);
        final LinearLayout calculationClick = (LinearLayout) findViewById(R.id.on_calculation_click);
        calculationClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //показать сумму на весь экран
                priseView.setVisibility(View.VISIBLE);
                prise.setText(String.format(getString(R.string.pay_info), counterValue));
                orderStatus.setText(String.format(getString(R.string.order_status),
                        getResources().getStringArray(R.array.order_status_values)[1]));
                orderActive.setVisibility(View.GONE);
                orderEnd.setVisibility(View.VISIBLE);
                if (!paused) {
                    endTime[0] = new Date();
                }//если расчет произошел во время переезда
                paused = false;
            }
        });

        LinearLayout goOnClick = (LinearLayout) findViewById(R.id.on_go_on_click);
        goOnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //убрать сумму
                priseView.setVisibility(View.INVISIBLE);
                orderStatus.setText(String.format(getString(R.string.order_status),
                        getResources().getStringArray(R.array.order_status_values)[0]));
                orderEnd.setVisibility(View.GONE);
                orderActive.setVisibility(View.VISIBLE);
                startTime[0] = new Date();
            }
        });

        LinearLayout payClick = (LinearLayout) findViewById(R.id.on_pay_click);
        payClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToDb(startTime[0], endTime[0], prise.getText().toString(), OrderActivity.this);
                Intent intent = new Intent(OrderActivity.this, AllAreasFragment.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
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

                if (isNetworkAvailable()) {
                    String addresses = "";
                    addresses = addresses + OrderActivity.getAddress(latFrom, lonFrom, OrderActivity.this);
                    addresses = addresses + " - " + OrderActivity.getAddress(latTo, lonTo, OrderActivity.this);
                    values.put(DatabaseHelper.CONTENT_ADDRESS, addresses);
                    values.put(DatabaseHelper.CONTENT_IS_ADDRESS_FOUND, 1);
                    //TODO отправка данных о завершенном заказе на сервер
                    //http://320320.ru:320/test_terminal/hs/mobile/sendorderstatus?imei=0123456789&login=998&Idorder=000000000000009&date=00/23/1234_18:00:00&statusorder=11&sum=100&priznaksum=0&priznakzagorodom=0&peregruzka=0&format=xml&os=android
                    //***
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
        final TextView timer = (TextView) findViewById(R.id.timer);
        final TextView distance = (TextView) findViewById(R.id.distance);
        final TextView counter = (TextView) findViewById(R.id.counter);

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

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}
