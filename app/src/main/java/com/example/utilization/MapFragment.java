package com.example.utilization;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MapFragment extends Fragment {

    private MapView map;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Configuration.getInstance().setUserAgentValue(requireContext().getPackageName());

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        map = view.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        //точки на карте по координатам
        GeoPoint center = new GeoPoint(51.767976,55.097234);
        map.getController().setZoom(17.0);
        map.getController().setCenter(center);
        addPoint(51.737768,55.099623, "Раздельный сбор отходов");
        addPoint(51.760083,55.103432, "Пункт приема батареек");
        addPoint(51.761159,55.095392, "Пункт приема прочего вторсырья, пластика");
        addPoint(51.77059,55.104178, "Пункт приема прочего вторсырья, пластика");
        addPoint(51.770562,55.184784, "Пункт приема пластика");
        addPoint(51.770194,55.184343, "Приём б/у аккумуляторов");
        addPoint(51.809329,55.103414, "Пункт приема прочего вторсырья, пластика");
        addPoint(51.813545,55.07836, "Переработка пластиковых бутылок");
        addPoint(51.845549,55.192482, "Точка сбора мусора");
        addPoint(51.825035,55.135394, "Пункт приема бумаги, пластика");
        addPoint(51.833429,55.128872, "Экообменники");
        addPoint(51.825375,55.109487, "Экоорг");

        return view;
    }

    //добавление точек на карту
    private void addPoint(double lat, double lon, String title) {
        Marker marker = new Marker(map);
        marker.setPosition(new GeoPoint(lat, lon));
        marker.setTitle(title);
        map.getOverlays().add(marker);
    }

    //при обновлении
    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    //при паузе
    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }
}
