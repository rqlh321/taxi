package com.example.taxidriverapp.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.example.taxidriverapp.R;
import com.example.taxidriverapp.fragments.AllOrdersFragment;
import com.example.taxidriverapp.fragments.HistoryFragment;
import com.example.taxidriverapp.fragments.MapFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @OnClick(R.id.menu_exit)
    void exit() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new AllOrdersFragment())
                    .commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setTitle(R.string.menu);
        ab.setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_history:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new HistoryFragment())
                                .commit();
                        break;
                    case R.id.menu_map:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new MapFragment())
                                .commit();
                        break;
                    case R.id.menu_all_orders:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new AllOrdersFragment())
                                .commit();
                        break;
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mDrawerLayout.openDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }

}
