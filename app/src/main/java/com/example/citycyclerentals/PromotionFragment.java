package com.example.citycyclerentals;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class PromotionFragment extends Fragment {

    private PromotionAdapter adapter;
    private List<PromotionModel> promotionModelList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promotion, container, false);

        promotionModelList = new ArrayList<>();

        promotionModelList.add(new PromotionModel(
                101,
                "Monsoon Adventure Sale",
                "2025-03-01 - 2025-04-20",
                "Embrace Sri Lanka's rainy season with 20% OFF on scooters and ATVs! " +
                        "Ride through misty hill roads in Nuwara Eliya, chase waterfalls in Ella, or explore Colombo’s vibrant streets. " +
                        "Code: RAINRIDE20 | Valid until April 20, 2025.",
                "RAINRIDE20"));

        promotionModelList.add(new PromotionModel(
                103,
                "Long-Term Wanderer Discount 🌴",
                "2025-05-01 - 2025-08-31",
                "Stay longer, save more! **30% OFF** weekly rentals. Perfect for backpackers exploring Sigiriya’s ruins, " +
                        "surfers in Arugam Bay, or tea-tasting in Hatton. Includes eco-friendly bike options and free luggage racks. " +
                        "Code: BIKEWEEK30 | Valid May-Aug 2025.",
                "BIKEWEEK30"));

        promotionModelList.add(new PromotionModel(
                102,
                "Island Explorer Special 🏍️",
                "2025-01-01 - 2025-01-31",
                "Kickstart your year with **1 FREE DAY** on any bike rental! Cruise Galle’s colonial forts, " +
                        "ride along Mirissa’s beaches, or tackle Kandy’s mountain trails. Flexible drop-off island-wide. " +
                        "Code: LANKA25 | Limited to first 500 users.",
                "LANKA25"));





        setupRecycleView(view,R.id.rvPromotion,promotionModelList);

        return view;
    }

    private void setupRecycleView(View view, int rcId, List<PromotionModel> promotionList) {
        RecyclerView recyclerView = view.findViewById(rcId); // Use the correct view reference
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new PromotionAdapter(promotionList);
        recyclerView.setAdapter(adapter);
    }
}