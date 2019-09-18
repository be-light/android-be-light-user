package com.example.a1117p.osam.user;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.libraries.maps.CameraUpdateFactory;
import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.OnMapReadyCallback;
import com.google.android.libraries.maps.SupportMapFragment;
import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.LatLngBounds;
import com.google.android.libraries.maps.model.Marker;
import com.google.android.libraries.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    SupportMapFragment mMapFragment;
    GoogleMap mGoogleMap;
    Boolean issearch = false;
    Marker selectedMarker=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
        findViewById(R.id.search_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                issearch = true;
                String search = ((EditText) findViewById(R.id.search_edit)).getText().toString();
                Geocoder geocoder = new Geocoder(MapActivity.this);
                try {
                    List<Address> addressList = geocoder.getFromLocationName(search, 20);
                    if (addressList.size() == 0) {
                        Toast.makeText(MapActivity.this, "검색결과없음", Toast.LENGTH_LONG).show();
                        return;
                    }
                    LatLngBounds.Builder builder = LatLngBounds.builder();
                    for (Address address : addressList) {
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        mGoogleMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(address.getFeatureName()));
                        builder = builder.include(latLng);
                    }
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
                    mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            if(selectedMarker!=null &&selectedMarker.equals(marker)) {
                                AlertDialog dialog = new AlertDialog.Builder(MapActivity.this)
                                        .setTitle("검색지점선택")
                                        .setMessage("(" + marker.getTitle() + ") 이 곳을 기점으로 검색하시겠습니까?")
                                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                issearch = false;
                                                LatLng position = marker.getPosition();
                                                final HashMap params = new HashMap<String, String>();
                                                params.put("latitude",String.valueOf(position.getLatitude()));
                                                params.put("longitude", String.valueOf(position.getLongitude()));
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        final String html = RequestHttpURLConnection.request("http://121.184.10.219/api/map/hosts", params, "GET");
                                                        runOnUiThread(new Runnable() {

                                                            @Override
                                                            public void run() {

                                                                Toast.makeText(ReciptEditActivity.this, html, Toast.LENGTH_LONG).show();
                                                            }

                                                        });

                                                    }
                                                }).start();
                                            }
                                        })
                                        .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText(MapActivity.this, "다른 곳을 선택하세요", Toast.LENGTH_LONG).show();
                                            }
                                        }).create();
                                dialog.show();
                                return true;
                            }
                            else{
                                selectedMarker = marker;
                            }
                            return false;
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            // Getting latitude of the current location
            double latitude = location.getLatitude();

            // Getting longitude of the current location
            double longitude = location.getLongitude();

            // Creating a LatLng object for the current location
            LatLng latLng = new LatLng(latitude, longitude);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        }
    }
}
