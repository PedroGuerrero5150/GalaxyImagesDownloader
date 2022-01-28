package com.example.proyecto6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.google.android.material.snackbar.Snackbar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PhotosAdapter.onClickListener{

    private List<Photo> photoList = new ArrayList<>();
    private PhotosAdapter adapter; // for binding data to RecyclerView
    private RecyclerView recyclerView;
    private Context contexto = this;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.history_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(contexto, HistoryActivity.class);
        startActivity(intent);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        URL url = createURL();
        // get reference to the RecyclerView to configure it
        recyclerView = findViewById(R.id.recyclerView);

        // use a LinearLayoutManager to display items in a vertical list
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // create RecyclerView.Adapter to bind tags to the RecyclerView
        adapter = new PhotosAdapter(this);
        recyclerView.setAdapter(adapter);
        //recyclerView.addItemDecoration(new ItemDivider(this));

        //download info from webPage
        GetPhotoInfoTask getPhotoInfo = new GetPhotoInfoTask();
        try {
            getPhotoInfo.execute(url);
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e("onCreate", "asynctask exeuction url");
        }
    }

    private URL createURL() {
        String baseUrl = "https://esahubble.org/images/archive/category/galaxies/";
        try {
            String urlString = baseUrl;
            return new URL(urlString);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null; // URL malformado
    }


    private final View.OnClickListener itemClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(contexto, ImageActivity.class);
                    String nombre = adapter.getNombre();
                    intent.putExtra("nombre", nombre);
                    startActivity(intent);

                }
            };


    @Override
    public void onItemClick(int position) {
        String nombre = adapter.getData().getName(position);
        Intent intent = new Intent(contexto, ImageActivity.class);
        intent.putExtra("nombre", nombre);
        startActivity(intent);

        //aniadir al historial

        adapter.getData().addHistory(nombre);
    }


    private class GetPhotoInfoTask
            extends AsyncTask<URL, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(URL... params) {
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) params[0].openConnection();
                int response = connection.getResponseCode();

                if (response == HttpURLConnection.HTTP_OK) {

                    StringBuilder builder = new StringBuilder();

                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()))) {

                        String line;

                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                    }
                    catch (IOException e) {
                        Snackbar.make(findViewById(R.id.constriantLayout),
                                R.string.read_error, Snackbar.LENGTH_LONG).show();
                        e.printStackTrace();
                        Log.e("GetPhotoInfoTask", "Read Error");

                    }

                    int upperIndex = builder.indexOf("var image");
                    int lowerIndex = builder.indexOf("];", upperIndex);
                    String dict = builder.substring(upperIndex, lowerIndex);

                    //Log.i("GetPhotoInfoTask", dict);

                    String subDict = "{\"var images\": ["+ dict.substring(dict.indexOf('{'), dict.lastIndexOf('}') + 1) + "]}";
                    return new JSONObject(subDict);
                }

                else {
                    Snackbar.make(findViewById(R.id.constriantLayout),
                            R.string.connect_error, Snackbar.LENGTH_LONG).show();
                    Log.e("GetPhotoInfoTask", "Connect Error 1, response: " + response);
                }
            }
            catch (Exception e) {
                Snackbar.make(findViewById(R.id.constriantLayout),
                        R.string.connect_error, Snackbar.LENGTH_LONG).show();
                e.printStackTrace();
                Log.e("GetPhotoInfoTask", "Connect Error 2");
            }
            finally {
                connection.disconnect(); // close the HttpURLConnection
            }

            return null;
        }

        // process JSON response and update ListView
        @Override
        protected void onPostExecute(JSONObject photo) {
            convertJSONtoArrayList(photo); // repopulate weatherList
            adapter.notifyDataSetChanged(); // rebind to ListView
        }/**/
    }

    private void convertJSONtoArrayList(JSONObject sourcePage) {
        try {
            // get page source's "list" JSONArray
            JSONArray list = sourcePage.getJSONArray("var images");

            // convert each element of list to a Weather object
            for (int i = 0; i < list.length(); ++i) {
                JSONObject photo = list.getJSONObject(i); // get one day's data

                //build photo dimensions
                String dimensions = photo.getString("width") +
                        "x" +
                        photo.getString("height");

                // add new photo object to weatherList
                adapter.getData().addData(
                        photo.getString("title"),
                        photo.getString("src"),
                        dimensions);

            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }



}