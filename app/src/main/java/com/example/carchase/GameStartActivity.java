package com.example.carchase;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;



public class GameStartActivity extends FragmentActivity implements OnMapReadyCallback {

    private TextView scoreTable;
    private TextView bestScore;
    private TextView bestScore2;
    private TextView bestScore3;

    //Buttons
    private Button easyButton;
    private Button hardButton;
    private Button quitButton;
    //map
    GoogleMap map;
    int lastScore;
    int best1,best2,best3;
    //location
    private static double latitudeCurrent;
    private static double longitudeCurrent;
    Location userLocation;
    FusedLocationProviderClient client;
    //Fragment
    public static SupportMapFragment mapFragment;
    //Best players locations
    private LatLng besti1;
    private LatLng besti2;
    private LatLng besti3;
    //Request code
    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        final Intent intent2 = new Intent(getApplicationContext(), HardModeActivity.class);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_start);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        SharedPreferences preferences = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        lastScore = preferences.getInt("HIGH_SCORE", 0);
        best1 = preferences.getInt("best1", 0);
        best2 = preferences.getInt("best2", 0);
        best3 = preferences.getInt("best3", 0);

        scoreTable = findViewById(R.id.table);
        bestScore = findViewById(R.id.best1);
        bestScore2 = findViewById(R.id.best2);
        bestScore3 = findViewById(R.id.best3);
        easyButton = findViewById(R.id.easyButton);
        hardButton = findViewById(R.id.hardButton);
        quitButton = findViewById(R.id.quitButton);

        besti1 = new LatLng(32.107108, 34.791407);
        besti2 = new LatLng(32.113064, 34.817666);
        besti3 = new LatLng(32.320654, 34.846436);


        requestPermission();

        client= LocationServices.getFusedLocationProviderClient(GameStartActivity.this);

        if(ActivityCompat.checkSelfPermission(GameStartActivity.this, ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            return;
        }


        client.getLastLocation().addOnSuccessListener(GameStartActivity.this, new OnSuccessListener<Location>() {

            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    latitudeCurrent = location.getLatitude();
                    longitudeCurrent = location.getLongitude();
                    userLocation = location;
                    Log.i("Print inside start","the location"+latitudeCurrent + " " + longitudeCurrent);
                }
            }
        });


        // Handle score table
        if(lastScore > best3 && lastScore < best2){
            best3 = lastScore;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("best3", best3);
            editor.apply();
        }

        if(lastScore > best2 && lastScore < best1){
            int temp = best2;
            best2 = lastScore;
            best3 = temp;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("best3", best3);
            editor.putInt("best2", best2);
            editor.apply();
        }

        if(lastScore > best1){
            int temp = best1;
            best1 = lastScore;
            best2 = temp;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("best2", best2);
            editor.putInt("best1", best1);
            editor.apply();
        }
        // display scores
        scoreTable.setText("HIGH SCORES : ");
        bestScore.setText("1st Place  " + best1);
        bestScore2.setText("2nd Place  " + best2);
        bestScore3.setText("3rd Place  " + best3);

        bestScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(besti3, 14));
            }
        });

        bestScore2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(besti2, 14));
            }
        });

        bestScore3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(besti1, 14));
            }
        });


        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent2);
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask();
                } else {
                    finish();
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.addMarker(new MarkerOptions().position(besti1).title("Best1"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(besti1, 14));
        map.addMarker(new MarkerOptions().position(besti2).title("Best2"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(besti2, 14));
        map.addMarker(new MarkerOptions().position(besti3).title("Best3"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(besti3, 14));
    }


    private void requestPermission(){
        ActivityCompat.requestPermissions(GameStartActivity.this, new String[]{ACCESS_FINE_LOCATION},1);
    }
}
