package com.example.taxidriverapp.fragments;

import com.example.taxidriverapp.R;

import butterknife.BindView;
import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.OverlayManager;
import ru.yandex.yandexmapkit.overlay.location.MyLocationItem;
import ru.yandex.yandexmapkit.overlay.location.OnMyLocationListener;

public class MapFragment extends GeneralFragment {
    @BindView(R.id.map)
    MapView mapView;
    MapController mapController;
    OverlayManager overlayManager;

    @Override
    protected int layout() {
        return R.layout.fragment_map;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.showFindMeButton(true);
        mapView.showZoomButtons(true);
        mapController = mapView.getMapController();
        overlayManager = mapController.getOverlayManager();
        mapController.setZoomCurrent(15);
        mapController.getOverlayManager().getMyLocation().addMyLocationListener(new OnMyLocationListener() {
            @Override
            public void onMyLocationChange(MyLocationItem myLocationItem) {
                mapController.setPositionNoAnimationTo(myLocationItem.getGeoPoint());
            }
        });
    }

}

