package com.example.citycyclerentals;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class RentalAdapter extends RecyclerView.Adapter<RentalAdapter.RentalViewHolder> {
    private List<RentalModel> rentalModelList;
    private BikeModel bike;
    private String status;
    private DBHelper dbHelper;

    public RentalAdapter(List<RentalModel> rentalModelList, Context context) {
        this.rentalModelList = rentalModelList;
        this.dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public RentalAdapter.RentalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rent_card, parent, false);

        return new RentalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RentalAdapter.RentalViewHolder holder, int position) {
        RentalModel rent = rentalModelList.get(position);
        bike = dbHelper.getBikeById(rent.getBikeId());

        System.out.println(rent.getReturnStationId());
        autoUpdateStatus(rent.getStatus(), rent.getStartTime(), rent.getEndTime(), rent.getRentalId(), bike.getBikeId());
        loadData(holder,rent,bike);
        holder.cancel.setOnClickListener(v -> {
            cancelAction(holder,rent);
        });
        holder.completed.setOnClickListener(v -> {
            completeReservation(holder,rent);
        });
    }

    @Override
    public int getItemCount() {
        return rentalModelList.size();
    }

    public class RentalViewHolder extends RecyclerView.ViewHolder {
        private TextView name,amount,pickUpLocation,pickUpTime,returnTime,returnLocation,tvStatus;
        private ImageView image;
        private Button cancel, completed;
        public RentalViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.bikeName);
            amount = itemView.findViewById(R.id.tvBikePrice);
            pickUpLocation = itemView.findViewById(R.id.tvBikeLocation);
            pickUpTime = itemView.findViewById(R.id.pickupTime);
            returnTime = itemView.findViewById(R.id.returnTime);
            returnLocation = itemView.findViewById(R.id.returnStation);
            image = itemView.findViewById(R.id.bikeImage);
            tvStatus=itemView.findViewById(R.id.tracking);
            cancel = itemView.findViewById(R.id.btnCancel);
            completed = itemView.findViewById(R.id.btnEdit);

        }
    }

    private void loadData(RentalViewHolder holder, RentalModel rent, BikeModel bike){
        status = rent.getStatus();

        if (bike.getImage() > 0) {
            holder.image.setImageResource(bike.getImage());
        } else {
            holder.image.setImageResource(R.drawable.logo4);
        }
        holder.name.setText("" + bike.getName());
        holder.amount.setText("Amount : LKR " + rent.getAmount());
        holder.pickUpLocation.setText("Pickup : " + rent.getPickupStation());
        holder.pickUpTime.setText("Pickup Time : " + rent.getStartTime());
        holder.returnTime.setText("Return Time : " + rent.getEndTime());
        holder.returnLocation.setText(" " + rent.getReturnStation());
        if (status.equals("Ongoing")) {
            setTimecount(holder,rent);

        } else {
            holder.tvStatus.setText(rent.getStatus() + " ");
        }


    }

    private void autoUpdateStatus(@NonNull String status, String pickupTime, String returnTime, int id, int bikeId){
        if (status.equals("Waiting")){
            System.out.println(status);
            String[] parts = pickupTime.split(":");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);

            Calendar pickup = Calendar.getInstance();
            pickup.set(Calendar.HOUR_OF_DAY, hours);
            pickup.set(Calendar.MINUTE, minutes);
            pickup.set(Calendar.SECOND, 0);
            pickup.set(Calendar.MILLISECOND, 0);

            long currentMillis = System.currentTimeMillis();

            if (currentMillis >= pickup.getTimeInMillis()) {
                dbHelper.updateReservationStatus(id, "Ongoing",bikeId);
            }
        }
    }

    private void cancelAction(RentalViewHolder holder, RentalModel rent){
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Confirm Cancellation")
                    .setMessage("Are you sure you want to cancel this rental?\nThis action cannot be undone!")
                    .setPositiveButton("YES", (dialog, which) -> {
                        if (rent.getStatus().equals("Waiting")) {
                            if (dbHelper.updateReservationStatus(rent.getRentalId(), "Canceled", rent.getBikeId())) {
                                Toast.makeText(holder.itemView.getContext(), "Rental Vanished!\n" + "Your reservation was ghosted! ", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(holder.itemView.getContext(),
                                        "Failed to cancel!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(holder.itemView.getContext(), "Can't cancel ongoing rental!", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("OOPS, KEEP IT", (dialog, which) -> {
                        Toast.makeText(holder.itemView.getContext(), "Your rental is still active!", Toast.LENGTH_SHORT).show();
                    })
                    .setIcon(R.drawable.cancel)
                    .create()
                    .show();
    }
    private void completeReservation(RentalViewHolder holder, RentalModel rent) {
        new AlertDialog.Builder(holder.itemView.getContext())
                .setTitle("Confirm Completion")
                .setMessage("Are you sure you want to Complete(END) this rental?")
                .setPositiveButton("YES", (dialog, which) -> {
                    if (rent.getStatus().equals("Ongoing") || rent.getStatus().equals("Delayed")) {
                        if (dbHelper.updateReservationStatus(rent.getRentalId(), "Completed", rent.getBikeId())) {
                            dbHelper.updateBikeStation(rent.getBikeId(),rent.getReturnStationId());
                            Toast.makeText(holder.itemView.getContext(), "Rental Completed!\n" + "Your ride was done! ", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(holder.itemView.getContext(),
                                    "Failed to Completed!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(holder.itemView.getContext(), "Can't Complete .This rental will have no start stage", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("OOPS, KEEP IT", (dialog, which) -> {
//                    Toast.makeText(holder.itemView.getContext(), "Your rental is still active!", Toast.LENGTH_SHORT).show();
                })
                .setIcon(R.drawable.cancel)
                .create()
                .show();

    }

    public void setTimecount(RentalViewHolder holder, RentalModel rent ){
        long timeCount = 0;
        String[] parts = rent.getEndTime().split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        Calendar returnCalender = Calendar.getInstance();
        returnCalender.set(Calendar.HOUR_OF_DAY, hours);
        returnCalender.set(Calendar.MINUTE, minutes);
        returnCalender.set(Calendar.SECOND, 0);
        returnCalender.set(Calendar.MILLISECOND, 0);
        long currentMillis = System.currentTimeMillis();
        long returnTime = returnCalender.getTimeInMillis();

        if (returnTime < currentMillis) {
            dbHelper.updateReservationStatus(rent.getRentalId(),"Delayed",rent.getBikeId());
            return;
        }
        timeCount = returnTime - currentMillis;
        new CountDownTimer(timeCount, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                holder.tvStatus.setText(f.format(hour) + ":" + f.format(min)+ ":" + f.format(sec)+" ");
            }
            @Override
            public void onFinish() {
                holder.tvStatus.setText(rent.getStatus());
            }
        }.start();
    }
}
