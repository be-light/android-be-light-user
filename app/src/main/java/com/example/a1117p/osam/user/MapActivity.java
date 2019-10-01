package com.example.a1117p.osam.user;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.libraries.maps.CameraUpdateFactory;
import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.OnMapReadyCallback;
import com.google.android.libraries.maps.SupportMapFragment;
import com.google.android.libraries.maps.model.BitmapDescriptor;
import com.google.android.libraries.maps.model.BitmapDescriptorFactory;
import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.LatLngBounds;
import com.google.android.libraries.maps.model.Marker;
import com.google.android.libraries.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    SupportMapFragment mMapFragment;
    GoogleMap mGoogleMap;
    Boolean issearch = false;
    Marker selectedMarker = null;
    BitmapDescriptor bitmapDescriptor;
    long backKeyClickTime = 0;


    void OvalProfile() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ImageView imageView = findViewById(R.id.profile_img);
            imageView.setBackground(new ShapeDrawable(new OvalShape()));
            imageView.setClipToOutline(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.makerpin02);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 80, 120, false);
        bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(smallMarker);


        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);


        OvalProfile();

        findViewById(R.id.search_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                issearch = true;
                mGoogleMap.clear();

                InputMethodManager imm;
                imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(findViewById(R.id.search_edit).getWindowToken(), 0);

                String search = ((EditText) findViewById(R.id.search_edit)).getText().toString();
                Geocoder geocoder = new Geocoder(MapActivity.this);
                try {
                    List<Address> addressList = geocoder.getFromLocationName(search, 20);
                    if (addressList.size() == 0) {
                        Toast.makeText(MapActivity.this, "검색결과없음", Toast.LENGTH_LONG).show();
                        issearch = false;
                        onMapReady(mGoogleMap);
                        return;
                    }
                    LatLngBounds.Builder builder = LatLngBounds.builder();
                    for (Address address : addressList) {
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        mGoogleMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .icon(bitmapDescriptor)
                                .title(address.getFeatureName()));
                        builder = builder.include(latLng);
                    }
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
                    mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            if (selectedMarker != null && selectedMarker.equals(marker)) {
                                AlertDialog dialog = new AlertDialog.Builder(MapActivity.this)
                                        .setTitle("검색지점선택")
                                        .setMessage("(" + marker.getTitle() + ") 이 곳을 기점으로 검색하시겠습니까?")
                                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                issearch = false;
                                                final LatLng position = selectedMarker.getPosition();
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        final String html = RequestHttpURLConnection.request("https://be-light.store/api/map/hosts?lat=" + position.latitude + "&lng=" + position.longitude, null, "GET");
                                                        runOnUiThread(new Runnable() {

                                                            @Override
                                                            public void run() {

                                                                Toast.makeText(MapActivity.this, html, Toast.LENGTH_LONG).show();
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
                            } else {
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

        final DrawerLayout drawer = findViewById(R.id.drawer);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        findViewById(R.id.open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MapActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.order_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MapActivity.this, ReciptMgtActivity.class);
                startActivity(i);
            }
        });
        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestHttpURLConnection.cookie = "";
                Toast.makeText(MapActivity.this, "로그아웃되었습니다.", Toast.LENGTH_LONG).show();
                Intent i = new Intent(MapActivity.this, SplashActivity.class);
                i.putExtra("needLoading", false);
                startActivity(i);
                finish();
            }
        });
        findViewById(R.id.faq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapActivity.this, FAQActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, false);

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


            /*VectorDrawableCompat vectorDrawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_makerpin, null);

            Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.draw(canvas);*/

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(bitmapDescriptor);
            googleMap.addMarker(markerOptions);
        }
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyClickTime + 1000) {
            backKeyClickTime = System.currentTimeMillis();
            Toast.makeText(this,"앱을 종료하시려면 뒤로가기 버튼을 다시 눌러주세요",Toast.LENGTH_SHORT).show();
        }else {
            finish();
        }
    }


}
