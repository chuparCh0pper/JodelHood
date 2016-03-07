package com.chuparch0pper.jodelhood;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.chuparch0pper.jodelhood.Adatper.PostAdapter;
import com.chuparch0pper.jodelhood.AsyncTask.UpdateMapTask;
import com.chuparch0pper.jodelhood.backend.JodelPost;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback {
    public static GoogleMap mMap;
    public static HashMap<String, JodelPost> mHashMap = new HashMap<>();

    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this); // Set a listener for info window events.

        UpdateMapTask task = new UpdateMapTask(this);
        task.execute();

    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        showPopup(((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0), mHashMap.get(marker.getId()));

    }

    private void showPopup(View anchorView, JodelPost jodelPost) {
        View popupView = getLayoutInflater().inflate(R.layout.popup, null);

        RecyclerView mRecyclerView = (RecyclerView) popupView.findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setAdapter(new PostAdapter(jodelPost));

        popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        // PopupWindow centralized
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);

    }

    @Override
    public void onBackPressed() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            finish();
        }

    }
}
