package com.example.proyecto6;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ImageActivity extends AppCompatActivity {
    private String nombre;
    private static Data mData = Data.getInstance();
    private ImageView imageHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        //intent comunications
        nombre = getIntent().getStringExtra("nombre");;

        //binding elementos gui
        TextView TitleImage = findViewById(R.id.tituloImagen);
        imageHolder = findViewById(R.id.fullImageView);

        //setters
        TitleImage.setText(nombre);
        setImage(imageHolder);
    }


    public void setImage(ImageView imageHolder) {
        //si esta en cache
        if (mData.getBitmaps().containsKey(nombre)) {
            imageHolder.setImageBitmap(mData.getBitmaps().get(nombre));
        }
        else {
            new LoadImageTask(imageHolder, mData.getBitmaps()).execute(nombre);//descarga
        }

        //aniadir a historico
        //mData.addHistory(nombre);
    }


    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

        private ImageView imageView;

        public LoadImageTask(ImageView imageView, Map<String, Bitmap> bitmaps) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;

            HttpURLConnection connection = null;

            try {
                URL url = new URL(mData.getData().get(params[0]).getURL()); // create URL for image

                connection = (HttpURLConnection) url.openConnection();

                try (InputStream inputStream = connection.getInputStream()) {
                    bitmap = createScaledBitmapFromStream(inputStream,500,500);
                    mData.getBitmaps().put(params[0], bitmap);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                connection.disconnect();
            }

            return bitmap;
        }

        // Se coloca la imagen en el imageView
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }

    //funcion que regresa Bitmap de dimensiones establecidas
    protected Bitmap createScaledBitmapFromStream(InputStream inputStream, int minDWidth, int minDHeight ) {

        final BufferedInputStream iStream = new BufferedInputStream(inputStream, 32*1024);
        try {
            final BitmapFactory.Options decodeBitmapOptions = new BitmapFactory.Options();
            if( minDWidth >0 && minDHeight >0 ) {
                final BitmapFactory.Options decodeBoundsOptions = new BitmapFactory.Options();
                decodeBoundsOptions.inJustDecodeBounds = true;
                iStream.mark(32*1024);
                BitmapFactory.decodeStream(iStream,null,decodeBoundsOptions);
                iStream.reset();

                final int originalWidth = decodeBoundsOptions.outWidth;
                final int originalHeight = decodeBoundsOptions.outHeight;

                decodeBitmapOptions.inSampleSize = Math.max(1,Math.min(originalWidth / minDWidth, originalHeight / minDHeight));

            }


            return BitmapFactory.decodeStream(iStream,null,decodeBitmapOptions);

        } catch( IOException e ) {
            throw new RuntimeException(e);
        } finally {
            try {
                iStream.close();
            } catch( IOException ignored ) {}
        }

    }


}
