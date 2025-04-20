package com.example.citycyclerentals;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RentBikeFragment extends Fragment {
    private BikeModel selectedBike;
    private TextView name, type, location,availability,price,amount,rating;
    private EditText pickupTime, promo;
    private Spinner rentalDuration, returnStation;
    private ImageView bikeImage;
    private Button btnRent, btnPromo;
    private DBHelper dbHelper;
    private double rentalAmount;
    private List<StationModel> stationModels;
    private int selectedStationId;
    private String userEmail;
    private final static String cupon = "RAINRIDE20";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            selectedBike = getArguments().getParcelable("SELECTED_BIKE");
        }

        dbHelper = new DBHelper(requireContext());
        stationModels = dbHelper.getAllStation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View views =  inflater.inflate(R.layout.fragment_rent_bike, container, false);

        name = views.findViewById(R.id.tvBikeName);
        type = views.findViewById(R.id.tvBikeType);
        location = views.findViewById(R.id.tvBikeLocation);
        availability = views.findViewById(R.id.tvBikeAvailability);
        price = views.findViewById(R.id.tvBikePrice);
        bikeImage = views.findViewById(R.id.reBikeImage);
        rentalDuration = views.findViewById(R.id.spDuration);
        amount= views.findViewById(R.id.tvAmount);
        btnRent = views.findViewById(R.id.btnBikeRent);
        rating = views.findViewById(R.id.tvRating);
        pickupTime = views.findViewById(R.id.etTime);
        returnStation = views.findViewById(R.id.spReturnStation);
        btnPromo = views.findViewById(R.id.btnPromo);
        promo = views.findViewById(R.id.etPromo);


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        userEmail = sharedPreferences.getString("email", null);

        loadStationSpinner();
        pickupTime.setOnClickListener(v -> showTimePicker());
        loadBikeData();
        setupDurationSpinner();


        btnPromo.setOnClickListener(v -> {
            applyPromoCode();
        });

        btnRent.setOnClickListener(v -> {
            addReservation();
        });

        return views;
    }

    private void addReservation() {
        UserModel userModel = dbHelper.getUserByEmail(userEmail);
        int userId = userModel.getUserId();
        String status = "Waiting";
        String bikePickupTime = pickupTime.getText().toString();
        String returnTime = calculateReturnTime(bikePickupTime, rentalDuration.getSelectedItem().toString());


        if (userId<0){
            Toast.makeText(requireContext(),"Invalid login! Log out & pedal back in! ",Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedBike.getStatus()!=1){
            Toast.makeText(requireContext(),"This bike is booked. Explore nearby options!",Toast.LENGTH_SHORT).show();
            return;
        }
        if (bikePickupTime.isEmpty()){
            Toast.makeText(requireContext(),"Schedule your bike pickup between 12:05 AM - 11:55 PM today",Toast.LENGTH_SHORT).show();
            return;
        }
        if (rentalAmount == 0){
            Toast.makeText(requireContext(),"Please choose your rental duration (e.g., 2 hours)",Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedStationId == -1) {
            Toast.makeText(requireContext(), "Select a station first!", Toast.LENGTH_SHORT).show();
            return;
        }
        Boolean result = dbHelper.addReservation(userId,selectedBike.getBikeId(),rentalAmount,bikePickupTime,returnTime,status,selectedBike.getLocation(),selectedStationId);
        if (result){
            Toast.makeText(requireContext(), "Reservation confirmed! Enjoy your ride!", Toast.LENGTH_SHORT).show();

            Fragment allRentFragment = new AllRentFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, allRentFragment)
                    .addToBackStack(null)
                    .commit();
        }else {
            Toast.makeText(requireContext(), "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
        }

    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), (view, hourOfDay, minute1) -> {
            String selectedTime = String.format("%02d:%02d", hourOfDay, minute1);

            pickupTime.setText(selectedTime);
        }, hour, minute, false);

        timePickerDialog.show();
    }

    private void loadBikeData() {
        name.setText(selectedBike.getName().toString());
        type.setText(" "+selectedBike.getType().toString());
        location.setText(" "+selectedBike.getLocation());
        price.setText("LKR: "+ selectedBike.getPrice()+" per 1h");
        bikeImage.setImageResource(selectedBike.getImage());
        rating.setText(" "+selectedBike.getRating()+"/5");
        if(selectedBike.getStatus()==1){
            availability.setText("Available");
        }else{
            availability.setText("unavailable");
        }
    }

    private void setupDurationSpinner() {
        rentalDuration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateAmount();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                updateAmount();
            }
        });
    }

    private void updateAmount() {
        if (rentalDuration.getSelectedItem().toString().equals("Duration(hours)")){
            amount.setText("Amount: 0");
            rentalAmount = 0;
        }else {
            int duration = Integer.parseInt(rentalDuration.getSelectedItem().toString());
            rentalAmount = selectedBike.getPrice() * duration;
            amount.setText(String.format("Amount: LKR %.2f", rentalAmount));
        }
    }

    private void loadStationSpinner(){
        List<String> stationNames = new ArrayList<>();
        for (StationModel station : stationModels) {
            stationNames.add(station.getLocation());
        }
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                stationNames
        );

        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        returnStation.setAdapter(locationAdapter);

        returnStation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStationId = stationModels.get(position).getStationId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private String calculateReturnTime(String pickupTime, String rentalDuration) {
        try {
            String[] timeParts = pickupTime.split(":");
            int hours = Integer.parseInt(timeParts[0]);
            int minutes = Integer.parseInt(timeParts[1]);
            int addHours = Integer.parseInt(rentalDuration);

            int totalHours = hours + addHours;
            int newHours = totalHours % 24;

            // Format as HH.00 with leading zeros
            return String.format("%02d:%02d", newHours, minutes);

        } catch (Exception e) {
            return "00:00";
        }
    }

    private void applyPromoCode(){
        String promoCode = promo.getText().toString();
        if (cupon.equals(promoCode)){
            rentalAmount = rentalAmount- (rentalAmount*0.2);
            amount.setText(String.format("Amount: LKR %.2f", rentalAmount));
            Toast.makeText(requireContext(),"Your Discount added! 20%",Toast.LENGTH_SHORT).show();
            return;

        }else{
            Toast.makeText(requireContext(),"Invalid Promo Code!",Toast.LENGTH_SHORT).show();
            return;
        }
    }
}