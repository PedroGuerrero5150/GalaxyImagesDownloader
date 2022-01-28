package com.example.proyecto6;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder>  {

    // listeners from MainActivity that are registered for each list item
    private static Data mData;
    private String nombre;
    public onClickListener onClickListener;

    //constructor
    public PhotosAdapter(onClickListener onClickListener){
        this.onClickListener = onClickListener;
        this.mData = Data.getInstance();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public static TextView nameTextView;
        public static TextView addressTextView;
        public static TextView dimensionsTextView;
        public onClickListener onClickListener;

        // configures a RecyclerView item's ViewHolder
        public ViewHolder(View itemView, onClickListener onClickListener) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            dimensionsTextView = itemView.findViewById(R.id.dimensionsTextView);
            this.onClickListener = onClickListener;

            // attach listeners to itemView
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onItemClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public PhotosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate the list_item layout
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_items, parent, false);

        // create a ViewHolder for current item
        return (new ViewHolder(view, this.onClickListener));
    }

    @Override
    public void onBindViewHolder(@NonNull PhotosAdapter.ViewHolder holder, int position) {

        List<Photo> listValues = new ArrayList<>(mData.getData().values());
        Photo p = listValues.get(position);
        nombre = p.getName();
        //set children texts
        holder.nameTextView.setText(p.getName());
        holder.addressTextView.setText("Direccion: " + p.getURL());
        holder.dimensionsTextView.setText("Dimensiones: " + p.getDimensions());
        /**/
    }

    @Override
    public int getItemCount() {
        return mData.getData().size();
    }

    public String getNombre() {
        return nombre;
    }

    public Data getData() {return mData;}

    public interface onClickListener{
        void onItemClick (int position);
    }


}
