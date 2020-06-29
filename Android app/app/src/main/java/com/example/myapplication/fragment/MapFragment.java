package com.example.myapplication.fragment;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.myapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap map;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng ns = new LatLng(45.259333, 19.831826);
        map.addMarker(new MarkerOptions().position(ns).title("Novi Sad"));
        map.moveCamera(CameraUpdateFactory.newLatLng(ns));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }
}
