package com.example.citycyclerentals;

import android.os.Bundle;

import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements BikeAdapter.OnRentButtonClickListener {

    private DBHelper dbHelper;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DBHelper(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false); // Store view reference

        List<BikeModel> bestRentBikes = dbHelper.getAllBikes();

        setupRecycleView(view, R.id.rvMostRental, bestRentBikes);
        setupRecycleView(view, R.id.rvRecentAdded, bestRentBikes);

        return view; // Return the inflated view

    }

    private void setupRecycleView(View view, int rcId, List<BikeModel> bikeList) {
        RecyclerView recyclerView = view.findViewById(rcId); // Use the correct view reference
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        BikeAdapter adapter = new BikeAdapter(bikeList,this);
        recyclerView.setAdapter(adapter);
    }
    public void onRentButtonClicked(BikeModel bike) {
        Fragment rentFragment = new RentBikeFragment();

        // Pass data using Bundle
        Bundle args = new Bundle();
        args.putParcelable("SELECTED_BIKE", bike);
        rentFragment.setArguments(args);

        // Perform fragment transaction
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, rentFragment)
                .addToBackStack(null)
                .commit();
    }
}