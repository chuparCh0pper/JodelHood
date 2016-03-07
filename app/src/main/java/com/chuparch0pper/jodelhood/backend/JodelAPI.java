package com.chuparch0pper.jodelhood.backend;

import com.chuparch0pper.jodelhood.Data.Constants;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JodelAPI {

    public String getAccessToken() {
        String line;
        StringBuilder jsonString = new StringBuilder();
        try {

            String payload = "{\"client_id\":" + "\"81e8a76e-1e02-4d17-9ba0-8a7020261b26\","
                    + "\"device_uid\": \"kdkintm6t53coji9ycven77rm81ufc6v76qwhdts9z23vcolt3huj0ddaxq8ao2\","
                    + "\"location\":" + "{\"loc_accuracy\": 19.0," + "\"city\": \"Mannheim\"," + "\"loc_coordinates\":"
                    + "{\"lat\": " + Constants.location_lat + "," + "\"lng\": " + Constants.location_lng +
                    "}," + "\"country\": \"DE\"} }";

            URL url = new URL("https://api.go-tellm.com/api/v2/users/");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            writer.write(payload);
            writer.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }
            br.close();
            connection.disconnect();


            return new JSONObject(jsonString.toString()).getString("access_token");

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

    public List<JodelPost> getData(String access_token) {

        String result = "";
        try {
            URL url = new URL("https://api.go-tellm.com/api/v2/posts?limit=200");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Bearer " + access_token);
            urlConnection.setRequestProperty("User-Agent",
                    "Jodel/65000 Dalvik/2.1.0 (Linux; U; Android 6.0.1; Nexus 6P Build/MMB29P");
            urlConnection.setRequestProperty("Connection", "keep-alive");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept-Encoding", "gzip");

            urlConnection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            result = sb.toString();

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // convert to posts
        List<JodelPost> posts = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray array = jsonObject.getJSONArray("posts");
            for (int i = 0; i < array.length(); i++) {
                JodelPost jodelPost;

                JSONObject currentJSONObject = (JSONObject) array.get(i);

                String message = currentJSONObject.getString("message");
                int vote_count = currentJSONObject.getInt("vote_count");
                String color = currentJSONObject.getString("color");

                // Location
                JSONObject location = ((JSONObject) array.get(i)).getJSONObject("location");
                JSONObject loc_coordinates = location.getJSONObject("loc_coordinates");
                String lat = loc_coordinates.getString("lat");
                String lng = loc_coordinates.getString("lng");
                LatLng latLng = new LatLng(Float.valueOf(lat), Float.valueOf(lng));

                jodelPost = new JodelPost("id", message, vote_count, color, latLng);

                if (currentJSONObject.has("children")) {
                    // Comments
                    JSONArray comments = currentJSONObject.getJSONArray("children");
                    for (int j = 0; j < comments.length(); j++) {
                        JSONObject currentComment = (JSONObject) array.get(j);
                        jodelPost.addComment(currentComment.getString("message"), currentComment.getInt("vote_count"));

                    }
                }


                posts.add(jodelPost);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return posts;
    }
}
