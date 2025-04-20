package com.example.citycyclerentals;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.PromotionViewHolder> {

    private List<PromotionModel> promotionModelList;

    public PromotionAdapter(List<PromotionModel> promotionModelList) {
        this.promotionModelList = promotionModelList;
    }

    @NonNull
    @Override
    public PromotionAdapter.PromotionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.promotion_card, parent, false);
        return new PromotionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PromotionAdapter.PromotionViewHolder holder, int position) {
        PromotionModel promotionModel = promotionModelList.get(position);
        holder.title.setText(promotionModel.getTitle());
        holder.timePeriod.setText(promotionModel.getTimePeriod());
        holder.description.setText(promotionModel.getDescription());
        holder.promo.setText("Promo: "+promotionModel.getPromoCode());


    }

    @Override
    public int getItemCount() {
        return promotionModelList.size();
    }

    public class PromotionViewHolder extends RecyclerView.ViewHolder {
        TextView title, timePeriod,description,promo;
        public PromotionViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.promotionTitle);
            timePeriod = itemView.findViewById(R.id.timePeriod);
            description = itemView.findViewById(R.id.description);
            promo = itemView.findViewById(R.id.cupon);
        }
    }
}
