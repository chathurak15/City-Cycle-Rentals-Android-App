package com.example.citycyclerentals;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchFragment extends Fragment implements MainBikeAdapter.OnRentButtonClickListener{

    private Spinner spPrice, spType, spLocation;
    private SearchView searchView;
    private DBHelper dbHelper;
    private List<BikeModel> bikesList;
    private MainBikeAdapter adapter;
    private List<StationModel> stationModels;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DBHelper(requireContext());
        stationModels = dbHelper.getAllStation();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View views = inflater.inflate(R.layout.fragment_search, container, false);
        bikesList = dbHelper.getAllBikes();
        searchView = views.findViewById(R.id.search);
        spPrice = views.findViewById(R.id.spPrice);
        spType = views.findViewById(R.id.spType);
        spLocation = views.findViewById(R.id.spCity);
        searchView.setQueryHint("find a rental Bike");
        searchView.setIconifiedByDefault(false);

        setupRecycleView(views,R.id.rvBike,bikesList);
        setupSearch();
        loadSpinner();
        setupFilters();

        return views;
    }

    private void loadSpinner() {
        List<String> stationNames = new ArrayList<>();
        stationNames.add("City");
        for (StationModel station : stationModels) {
            stationNames.add(station.getLocation());
        }
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                stationNames
        );

        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLocation.setAdapter(locationAdapter);
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterBike();
                return true;
            }

            });
        }
    private void setupFilters() {
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterBike();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spLocation.setOnItemSelectedListener(listener);
        spType.setOnItemSelectedListener(listener);
        spPrice.setOnItemSelectedListener(listener);
    }

    private void filterBike() {
        String searchText = searchView.getQuery().toString().toLowerCase();
        String selectedLocation = spLocation.getSelectedItem().toString();
        String selectedType = spType.getSelectedItem().toString();
        String selectedPrice = spPrice.getSelectedItem().toString();

        List<BikeModel> filteredList = bikesList.stream()
                .filter(b -> TextUtils.isEmpty(searchText) || b.getName().toLowerCase().contains(searchText) ||b.getType().toLowerCase().contains(searchText))
                .filter(b -> selectedType.equals("Type") || b.getType().equals(selectedType))
                .filter(b -> selectedLocation.equals("City") || b.getLocation().equals(selectedLocation))
                .collect(Collectors.toList());

        if (selectedPrice.equals("Low - High")) {
            filteredList.sort((b1, b2) -> Double.compare(b1.getPrice(), b2.getPrice()));
        } else if (selectedPrice.equals("High - Low")) {
            filteredList.sort((b1, b2) -> Double.compare(b2.getPrice(), b1.getPrice()));
        }

        adapter.updateList(filteredList);
    }

    private void setupRecycleView(View view, int rcId, List<BikeModel> bikeList) {
        RecyclerView recyclerView = view.findViewById(rcId); // Use the correct view reference
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new MainBikeAdapter(bikeList,this);
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