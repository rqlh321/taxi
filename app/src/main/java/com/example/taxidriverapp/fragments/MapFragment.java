package com.example.taxidriverapp.fragments;

import com.example.taxidriverapp.R;

import butterknife.BindView;
import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.utils.GeoPoint;

public class MapFragment extends GeneralFragment {
    @BindView(R.id.map)
    MapView mapView;

    @Override
    protected int layout() {
        return R.layout.map;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.showFindMeButton(true);
        mapView.showZoomButtons(true);
        MapController mapController = mapView.getMapController();
        mapController.setPositionNoAnimationTo(new GeoPoint(47.222184, 38.919234));
        mapController.setZoomCurrent(15);
        /*mapView.getMapController().getOverlayManager().getMyLocation().addMyLocationListener(new OnMyLocationListener() {
            @Override
            public void onMyLocationChange(MyLocationItem myLocationItem) {
                GeoPoint geoPoint = myLocationItem.getGeoPoint();
            }
        });*/
    }
}

