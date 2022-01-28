package com.example.proyecto6;

public class Photo {
    private final String name;
    private final String URL;
    private final String dimensions;
    private int timesPresented;

    public Photo(String name, String URL, String dimensions) {
        this.name = name;
        this.URL = URL;
        this.dimensions = dimensions;
        this.timesPresented = 1;
    }

    public Photo(String name, String URL, String dimensions, int timesPresented) {
        this.name = name;
        this.URL = URL;
        this.dimensions = dimensions;
        this.timesPresented = timesPresented;
    }

    public String getName() {
        return name;
    }

    public String getURL() {
        return URL;
    }

    public String getDimensions() {
        return dimensions;
    }

    public int getTimesPresented() {
        return timesPresented;
    }

    public void augmentTimesPresented() {
        this.timesPresented++;
    }

    @Override
    public String toString(){ return name + ", URL: " + URL;};
}
