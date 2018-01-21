package com.example.embulanceapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import modules.Findpath;
import modules.FindpathListener;
import modules.Route;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, FindpathListener {

    private static final int REQUEST_LOCATION = 1;
    private GoogleMap mMap;
    private Button buttonEnter;
    private EditText startAdd;
    private EditText endAdd;
    private ImageButton buttonChange;
    private Button buttonPlace;
    Geocoder geocoder;
    List<Address> addresses;
    Double latitude = 24.18395; //緯度
    Double longitude = 120.604649 ; //經度
    private List<Marker> originMarkers = new ArrayList<>(); //起始Marker座標圖案
    private List<Marker> destinationMarkers = new ArrayList<>(); //目的地Marker座標圖案
    private List<Polyline> polylinePaths = new ArrayList<>(); //路徑
    private ProgressDialog progressDialog; //手機上會顯示讀取的視窗
    LocationManager locationManager; //用來找定位

    @SuppressLint("ServiceCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps); //回傳互動介面
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map); //建立起始位置
        mapFragment.getMapAsync(this);
        buttonEnter = (Button) findViewById(R.id.buttonEnter); //建立發送按鈕
        startAdd = (EditText) findViewById(R.id.startAdd); //起始位置
        endAdd = (EditText) findViewById(R.id.endAdd); // 目的地
        buttonChange = (ImageButton) findViewById(R.id.buttonChange); //建立互換按鈕
        buttonPlace = (Button) findViewById(R.id.buttonPlace); //建立"重新定位"按鈕
        geocoder = new Geocoder(this, Locale.getDefault()); //建立geocoder(是將座標轉為地址的)
        //定位
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        //發送按鈕按下去時
        buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textRead();
            }
        });
        //發送互換按鈕按下去時
        buttonChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String start = startAdd.getText().toString();   //起始位置裡的內容
                String ending = endAdd.getText().toString();    //目的地位置的內容

                if (start.isEmpty() && ending.isEmpty()) {
                } else if (start.isEmpty()) {    //如果起始textfield是空的
                    startAdd.setText(ending);
                    endAdd.setText("");
                } else if (ending.isEmpty()) {   //如果目的地textfield是空的
                    endAdd.setText(start);
                    startAdd.setText("");
                } else { //互換
                    startAdd.setText(ending);
                    endAdd.setText(start);
                }
            }
        });
        //'重新定位'按鈕 按下去的時候
        buttonPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(originMarkers.isEmpty()){}else{
                    originMarkers.remove(0); //原本可能就有先marker,所以需要先清掉
                }
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {//如果有辦法定位
                    getLocation();
                }
            }
        });

    }
    //從GPS得知位址
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //將位址從在 latitude 跟longtitude裡面
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                address();

            } else  if (location1 != null) {
                latitude  = location1.getLatitude();
                longitude = location1.getLongitude();
                address();
            }else{

                Toast.makeText(this,"無法得知位址",Toast.LENGTH_SHORT).show();

            }
        }
    }

    //將經緯線使用geocoder轉為位址
    private void address(){
        try{
            addresses = geocoder.getFromLocation(latitude,longitude ,1);
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();


            if (address == null) address ="";
            if (city == null) address ="";
            if (state == null) address ="";
            if (country == null) address ="";
            if (postalCode == null) address ="";
            String fulladress = postalCode+country+state+city;

            LatLng place = new LatLng(latitude, longitude );
            startAdd.setText(fulladress);
            //將起始位置圖案放在定位的位置上
            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon1))
                    .title(address)
                    .position(place)));
            //將畫面移到定位的地方
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 18));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void textRead() { //將讀入的起始位置跟目的地位置存起來
        String start = startAdd.getText().toString(); //都存在string裡面
        String ending = endAdd.getText().toString();
        if (start.isEmpty() && ending.isEmpty()) {
            Toast.makeText(this, "請輸入起始位置跟目的地位置", Toast.LENGTH_SHORT).show();
            return;
        } else if (start.isEmpty()) {    //如果起始textfield是空的
            Toast.makeText(this, "請輸入起始位置", Toast.LENGTH_SHORT).show();
            return;
        } else if (ending.isEmpty()) {   //如果目的地textfield是空的
            Toast.makeText(this, "請輸入目的地位置", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            //傳到Findpath來轉為路線
            new Findpath(this, start, ending).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng place = new LatLng(24.18395,120.604649 ); //建立起始畫面
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 18)); //放大地圖
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }

        mMap.setMyLocationEnabled(true);//可以定位

    }
    //跑的等待期間
    @Override
    public void onFindpathStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);
        //(可能之前有跑過一次) 所以要將原本的 markers跟原本的線去掉
        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }
        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }
        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    //成功跑完後
    @Override
    public void onFindpathSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);
            //將 圖片:起始marker 放到起始位置
            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon1))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            //將 圖片:目的地marker放到目的地位置
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon2))
                    .title(route.endAddress)
                    .position(route.endLocation)));
            //將線調為綠色
            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.GREEN).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }


}
