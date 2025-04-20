package com.example.citycyclerentals;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class BikeAdapter extends RecyclerView.Adapter<BikeAdapter.BikeViewHolder> {

    private List<BikeModel> bikeList;
    private BikeAdapter.OnRentButtonClickListener listener;
    public interface OnRentButtonClickListener {
        void onRentButtonClicked(BikeModel bike);
    }


    public BikeAdapter(List<BikeModel> bikeList, BikeAdapter.OnRentButtonClickListener listener) {
        this.bikeList = bikeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BikeAdapter.BikeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.bike_card_home, parent, false);
        return new BikeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BikeAdapter.BikeViewHolder holder, int position) {
        BikeModel bike = bikeList.get(position);

        holder.imageView.setImageResource(bike.getImage());
        holder.name.setText(bike.getName());
        holder.price.setText("LKR "+bike.getPrice()+" per 1h");
        if(bike.getStatus()==1){
            holder.availability.setText("Available");
        }else{
            holder.availability.setText("unavailable");
        }
        holder.btnRent.setOnClickListener(view -> {
            if (listener != null) {
                listener.onRentButtonClicked(bike);
            }
        });

    }

    private void rentBike() {

    }

    @Override
    public int getItemCount() {
        return bikeList.size();
    }

    public class BikeViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name,price,availability;
        Button btnRent;

        public BikeViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            availability = itemView.findViewById(R.id.availability);
            btnRent = itemView.findViewById(R.id.btnRent);
        }
    }
}
