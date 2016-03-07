package com.chuparch0pper.jodelhood.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chuparch0pper.jodelhood.Data.Constants;
import com.chuparch0pper.jodelhood.MapsActivity;
import com.chuparch0pper.jodelhood.backend.JodelAPI;
import com.chuparch0pper.jodelhood.backend.JodelPost;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class UpdateMapTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private ProgressDialog dialog;

    private List<JodelPost> posts;

    public UpdateMapTask(MapsActivity context) {
        this.context = context;
        dialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Loading...");
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        JodelAPI jodelAPI = new JodelAPI();
        String accessToken = jodelAPI.getAccessToken();
        posts = jodelAPI.getData(accessToken);

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {

        addMarkers();
        // Set camera
        MapsActivity.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(Constants.location_lat, Constants.location_lng), 12));

        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void addMarkers() {
        for (JodelPost currentPost : posts) {
            createMarker(currentPost);
        }
    }

    private void createMarker(JodelPost jodelPost) {
        Marker marker = MapsActivity.mMap.addMarker(
                new MarkerOptions().position(jodelPost.getLatLng())
                        .icon(getMarkerIcon("#" + jodelPost.getColor()))
                        .title(jodelPost.getMessage().get(0))
                        .snippet("Votes : " + jodelPost.getVote_count().get(0)));
        MapsActivity.mHashMap.put(marker.getId(), jodelPost);

        MapsActivity.mMap.addCircle(new CircleOptions()
                .center(jodelPost.getLatLng())
                .radius(500)
                .fillColor(Color.parseColor("#80" + jodelPost.getColor())) // #80 gives you 50% transparency
                .strokeColor(Color.parseColor("#80" + jodelPost.getColor())));

        MapsActivity.mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(context);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(context);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

    }

    @NonNull
    private BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

}
