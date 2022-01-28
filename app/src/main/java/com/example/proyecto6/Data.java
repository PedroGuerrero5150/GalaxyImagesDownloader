package com.example.proyecto6;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.*;

public class Data{
    private LinkedHashMap<String, Photo> data;
    private LinkedHashMap<String,String> history;
    private Map<String, Bitmap> bitmaps;//cache
    private Data mData;

    //constructor privado
    private Data() {
        data = new LinkedHashMap<>();
        history = new LinkedHashMap<String, String>();
        bitmaps = new HashMap<>();
        mData = Data.getInstance();
    }

    //instancia unica privada
    private static Data ourInstance = new Data();

    //metodo regresar unica instanica
    public static Data getInstance() {
        return ourInstance;
    }

    //metodo borrar datos
    public void clear() {
        history.clear();
        data.clear();
        bitmaps.clear();
    }

    //getters, setters
    public LinkedHashMap<String,String> getHistory() {
        return history;
    }

    public LinkedHashMap<String,Photo> getData() {
        return data;
    }

    public void addHistory(String PhotoName) {
        data.get(PhotoName).augmentTimesPresented();
        history.put(PhotoName, "-Nombre Fotografia: " + PhotoName + "\n" +
                "Veces que se ha presentado: " + data.get(PhotoName).getTimesPresented() + "\n\n");
        Log.i("Data.addHistory():",String.valueOf(data.get(PhotoName).getTimesPresented()));

    }

    public void addData(String photoName, String photoAddress, String photoDimensions){
        data.put(photoName, new Photo (photoName, photoAddress, photoDimensions));
    }

    public Map<String, Bitmap> getBitmaps() {
        return bitmaps;
    }

    public String getName(int index) {
        Iterator <String> itr = data.keySet().iterator();
        for(int i = 0; i < index; i++)
            itr.next();

        return itr.next();
    }
}
