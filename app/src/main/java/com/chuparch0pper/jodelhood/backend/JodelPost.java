package com.chuparch0pper.jodelhood.backend;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class JodelPost {
    private String post_id;
    private List<String> message = new ArrayList<>();
    private List<Integer> vote_count = new ArrayList<>();
    private String color;
    private LatLng latLng;
    private int numOfEntries;

    public JodelPost(String post_id, String message, int vote_count, String color, LatLng latLng) {
        this.post_id = post_id;
        this.message.add(message);
        this.vote_count.add(vote_count);
        this.color = color;
        this.latLng = latLng;

        numOfEntries = 1;
    }

    public String getPost_id() {
        return post_id;
    }

    public void addComment(String message, int vote_count) {
        this.message.add(message);
        this.vote_count.add(vote_count);
        numOfEntries++;
    }

    public List<String> getMessage() {
        return message;
    }

    public List<Integer> getVote_count() {
        return vote_count;
    }

    public String getColor() {
        return color;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public int getNumOfEntries() {
        return numOfEntries;
    }
}