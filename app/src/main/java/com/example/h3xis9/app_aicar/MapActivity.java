package com.example.h3xis9.app_aicar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by h3xis9 on 2017/11/28.
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    Button btn_map;
    ImageView iv_aichan;

    //////////////////////////////////
    //need to add announce voice HERE
    //////////////////////////////////
    //MediaPlayer mpDewa;

    int i;
    String dest;
    String dest_str;
    LatLng dest_geo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        dest = new String();
        dest_str = new String();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                i = 0;
            } else {
                i = extras.getInt("geo");
            }
        } else {
            i = (int) savedInstanceState.getInt("geo");
        }

        if (i != 0){
            switch (i){
                case 1:
                    dest = "新宿駅";
                    dest_str = "目的地：新宿駅";
                    dest_geo = new LatLng(35.6894, 139.7003);
                    break;
                case 2:
                    dest = "ハチ公前";
                    dest_str = "目的地：ハチ公前";
                    dest_geo = new LatLng(35.6591, 139.7007);
                    break;
                case 3:
                    dest = "船の科学館駅";
                    dest_str = "目的地：船の科学館駅";
                    dest_geo = new LatLng(35.6213, 139.7731);
                    break;
            }
        }


        View.OnClickListener mOnClickListener = new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                Intent mIntent = new Intent();
                mIntent.setAction(Intent.ACTION_VIEW);
                mIntent.setClassName("com.google.android.apps.maps","com.google.android.maps.driveabout.app.NavigationActivity");

                //mIntent.setData(Uri.parse("google.navigation:q=国際展示場駅&mode=d"));    //国際展示場駅
                mIntent.setData(Uri.parse("google.navigation:q=" + dest + "&mode=d"));           //新宿駅
                //mIntent.setData(Uri.parse("google.navigation:q=国際展示場駅&mode=d"));    //

                /*
                    google.navigation:q=latitude,longitude
                    &mode=
                    d for driving
                    w for walking
                    b for bicycling
                */

                startActivity(mIntent);
            }
        };

        btn_map = (Button) findViewById(R.id.btn_map);
        btn_map.setOnClickListener(mOnClickListener);

        iv_aichan = (ImageView) findViewById(R.id.iv_aichan);
        iv_aichan.setOnClickListener(mOnClickListener);

        //fragmentをIDで呼び出して、onMapReadyの内容を実行（sync）させる。
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //////////////////////////////////
        //need to add announce voice HERE
        //////////////////////////////////
        //mpDewa.start();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {


        /*
            HAL -> Shinjuku St.
         */

        // cocoonTower & shinjuku
        //LatLng cocoonTowerGeo = new LatLng(35.68833058, 139.691497234);
        //LatLng shinjukuStationGeo = new LatLng(35.6894, 139.7003);
        //LatLng hachikomaeGeo = new LatLng(35.6591, 139.7007);


        // GEOCODE: bigsight & the nearest station

        //LatLng bigSightGeo = new LatLng(35.6298, 139.7942);
        //LatLng kaihinkouenGeo = new LatLng(35.6298, 139.7784);


        MarkerOptions mMarker = new MarkerOptions()
                .position(dest_geo)
                .title(dest_str)
                .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("logo_pin",91,145)));

        googleMap.addMarker(mMarker);

        googleMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(dest_geo, 16));

    }

    public Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

}
