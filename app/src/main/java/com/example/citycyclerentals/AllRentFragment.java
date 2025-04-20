package com.example.citycyclerentals;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class AllRentFragment extends Fragment {


    private List<RentalModel> rentList;
    private DBHelper dbHelper;

    private RentalAdapter adapter;
    private String userEmail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DBHelper(requireContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_rent, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        userEmail = sharedPreferences.getString("email", null);
        UserModel userModel = dbHelper.getUserByEmail(userEmail);

        rentList = dbHelper.getAllRentHistryByUserId(userModel.getUserId());
        setupRecycleView(view,R.id.rvRentHistry,rentList);
        return view;
    }

    private void setupRecycleView(View view, int rvRentHistry, List<RentalModel> rentList) {
        RecyclerView recyclerView = view.findViewById(rvRentHistry); // Use the correct view reference
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new RentalAdapter(rentList,requireContext());
        recyclerView.setAdapter(adapter);
    }
}