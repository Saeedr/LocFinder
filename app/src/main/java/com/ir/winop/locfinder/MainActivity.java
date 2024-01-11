package com.ir.winop.locfinder;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements LocationListener {


    public  static final int RequestPermissionCode  = 1 ;
    Button buttonEnable, buttonGet ;
    TextView textViewLongitude, textViewLatitude ;
    Context context;
    Intent intent1 ;
    Location location;
    LocationManager locationManager ;
    boolean GpsStatus = false ;
    Criteria criteria ;
    String Holder;
    boolean mPressedOnce = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EnableRuntimePermission();

        buttonEnable = (Button)findViewById(R.id.button);
        buttonGet = (Button)findViewById(R.id.button2);

        textViewLongitude = (TextView)findViewById(R.id.textView);
        textViewLatitude = (TextView)findViewById(R.id.textView2);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        criteria = new Criteria();

        Holder = locationManager.getBestProvider(criteria, false);

        context = getApplicationContext();

        CheckGpsStatus();

        buttonEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent1);


            }
        });




        buttonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckGpsStatus();

                if(GpsStatus == true) {
                    if (Holder != null) {
                        if (ActivityCompat.checkSelfPermission(
                                MainActivity.this,
                                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                &&
                                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                        != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        location = locationManager.getLastKnownLocation(Holder);
                        locationManager.requestLocationUpdates(Holder, 12000, 7, MainActivity.this);
                    }
                }else {

                    Toast.makeText(MainActivity.this, "لطفا ابتدا gps  را روشن کنید", Toast.LENGTH_LONG).show();

                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (mPressedOnce) {
            super.onBackPressed();
            finish();
            System.exit(0);
        }
        this.mPressedOnce = true;
        Toast.makeText(this, "برای خروج دوبار کلیک نمایید", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mPressedOnce =false;
            }
        }, 2000);
    }
    @Override
    public void onLocationChanged(Location location) {
        textViewLongitude.setText("long(Y)(:" + location.getLongitude());
        textViewLatitude.setText("lat(X)(:" + location.getLatitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void CheckGpsStatus(){

        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION))
        {

            Toast.makeText(MainActivity.this,"اجازه دسترسی به موقعیت یاب وجود دارد", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, RequestPermissionCode);

        }
    }
    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(MainActivity.this,"دسترسی به موقعیت یاب باز است", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(MainActivity.this,"دسترسی شما برای موقعیت یاب بسته است", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

}
