package com.example.citycyclerentals;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.citycyclerentals.databinding.ActivityMainBinding;

import java.util.List;

public class MainBikeAdapter extends RecyclerView.Adapter<MainBikeAdapter.MainBikeViewHolder> {

    private List<BikeModel> bikeModelList;
    private OnRentButtonClickListener listener;

    public interface OnRentButtonClickListener {
        void onRentButtonClicked(BikeModel bike);
    }
    public MainBikeAdapter(List<BikeModel> bikeModelList, OnRentButtonClickListener listener) {
        this.bikeModelList = bikeModelList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MainBikeAdapter.MainBikeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bike_card_search, parent, false);

        return new MainBikeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainBikeAdapter.MainBikeViewHolder holder, int position) {
        BikeModel bikeModel = bikeModelList.get(position);
        holder.name.setText(bikeModel.getName());
        holder.price.setText("LKR "+bikeModel.getPrice()+" per 1h");
        holder.type.setText("Type: "+bikeModel.getType());
        holder.location.setText(" "+bikeModel.getLocation());
        holder.bikeImage.setImageResource(bikeModel.getImage());
//        holder.ratingBar.setRating((float) bikeModel.getRating());
        if(bikeModel.getStatus()==1){
            holder.availability.setText("Available");
        }else{
            holder.availability.setText("unavailable");
        }
        holder.btnRent.setOnClickListener(view -> {
            if (listener != null) {
                listener.onRentButtonClicked(bikeModel);
            }
        });

    }

    @Override
    public int getItemCount() {
       return bikeModelList.size();
    }

    public void updateList(List<BikeModel> filteredList) {
        bikeModelList = filteredList;
        notifyDataSetChanged();
    }

    public class MainBikeViewHolder extends RecyclerView.ViewHolder {
        private TextView name,price,type,availability,location;
        private Button btnRent;
        private RatingBar ratingBar;
        private ImageView bikeImage;

        public MainBikeViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.bikeName);
            price = itemView.findViewById(R.id.bikePrice);
            type = itemView.findViewById(R.id.bikeType);
            availability = itemView.findViewById(R.id.bikeAvailability);
//            ratingBar = itemView.findViewById(R.id.bikeRating);
            bikeImage= itemView.findViewById(R.id.bikeImage);
            btnRent = itemView.findViewById(R.id.btnBikeRent);
            location = itemView.findViewById(R.id.bikeLocation);

        }
    }
}
