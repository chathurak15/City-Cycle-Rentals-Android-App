package com.example.citycyclerentals;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "CityCycle.db";
    private static int DB_VERSION = 1;
    private static String USER_TABLE = "user";
    private static String USERDETAILS_TABLE = "user_details";
    private static String BIKE_TABLE = "bike";
    private static String STATION_TABLE = "station";
    private static String RESERVATION_TABLE = "reservation";
    public DBHelper(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //create user table
        String userTable = "CREATE TABLE " + USER_TABLE + "(" +
                "userId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "mobile_number TEXT, " +
                "email TEXT UNIQUE, " +
                "NIC TEXT UNIQUE, " +
                "password TEXT, " +
                "image BLOB)";
        try {
            db.execSQL(userTable);
            Log.d(TAG, "onCreate: user Table Created successfully");
        }catch (SQLException e){
            Log.d(TAG, String.valueOf(e));
        }

        //create userDetails table
        String userDetailsTable = "CREATE TABLE " + USERDETAILS_TABLE + "(" +
                "NIC TEXT PRIMARY KEY, " +
                "DOB TEXT, " +
                "gender TEXT)";

        try {
            db.execSQL(userDetailsTable);
            Log.d(TAG, "onCreate: userDetails Table Created successfully");
        }catch (SQLException e){
            Log.d(TAG, String.valueOf(e));
        }

        //create bikeTable
        String bikeTable = "CREATE TABLE " + BIKE_TABLE + "(" +
                "bikeId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "type TEXT, " +
                "status INT, " +
                "price double, " +
                "rating double, " +
                "stationId INT, " +
                "image INT)";

        try {
            db.execSQL(bikeTable);
            Log.d(TAG, "onCreate: bike Table Created successfully");
            insertBike(db);
        }catch (SQLException e){
            Log.d(TAG, String.valueOf(e));
        }

        //create Station Table
        String stationTable = "CREATE TABLE " + STATION_TABLE + "(" +
                "stationId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "location TEXT)";
        try {
            db.execSQL(stationTable);
            insertBikeStation(db);
            Log.d(TAG, "onCreate: Station Table Created successfully");
        }catch (SQLException e){
            Log.d(TAG, String.valueOf(e));
        }

        //CREATE RESERVATION TABLE
        String reservationTable = "CREATE TABLE " + RESERVATION_TABLE + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userId INT, " +
                "bikeId INT, " +
                "amount dobule, " +
                "status TEXT, " +
                "startTime TEXT, " +
                "endTime TEXT, " +
                "pickup_station TEXT, " +
                "return_stationId INT)";

        try {
            db.execSQL(reservationTable);
            Log.d(TAG, "onCreate: reservation Table Created successfully");
        }catch (SQLException e){
            Log.d(TAG, String.valueOf(e));
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE );
        onCreate(db);

    }
    public void  insertBike(SQLiteDatabase db)
    {
         db.execSQL("INSERT INTO "+BIKE_TABLE+" (name,type,status,price,rating,stationId,image) " +
                "VALUES " +
                 "('Trek FX 2 Disc', 'Hybrid Bike', 1, 40.0, 4.5,1," + R.drawable.trek_fx2_disc + "), " +
                 "('Orbea Vector 20', 'Road Bike', 1, 44.0, 4.6,1," + R.drawable.orbea_vector + "), " +
                 "('Quick Women Remixte', 'Womens Bike', 1, 45.0, 4.4,3," + R.drawable.quick3 + "), " +
                 "('APEX ONYX GX 3000x', 'Road Bike', 1, 40.0, 4.5,7, " + R.drawable.apex_onyx + "), " +
                 "('2022 Diverge E5', 'Womens Bike', 1, 40.0, 4.9,5, " + R.drawable.specialized_sirrus+ "), " +
                 "('Woom 4 kids Bike', 'Kids Bike', 1, 26.0, 4.6,6, " + R.drawable.woom+ "), " +
                 "('APEX TERRAE2020 2', 'Road Bike', 1, 45.0, 4.8,3, " + R.drawable.apex_terr + "), " +
                 "('Tomahawk Kids Hero', 'Kids Bike', 1, 25.0, 4.8,4, " + R.drawable.tomahawk_super+"), " +
                 "('Merida Big Nine 300', 'Mountain Bike', 1, 48.0, 4.7,2,"+ R.drawable.merida_2022 +" )");

    }
    public void insertBikeStation(SQLiteDatabase db){
        db.execSQL("INSERT INTO "+STATION_TABLE+" (location) " +
                "VALUES ('Bambalapitiya'), " +
                "('Maharagama'), " +
                "('Colombo Fort'), " +
                "('Pettah Central'), " +
                "('Wellawatte Beach'), " +
                "('Dehiwala Junction'), " +
                "('Boralasgamuwa')");
    }
    public boolean insertUser(String name, String email, String contact, String nic, String gender, String dob, String password, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("mobile_number", contact);
        values.put("NIC", nic);
        values.put("password", password);
        values.put("image", image);

        long user = db.insert(USER_TABLE, null, values);
        if(user != -1){
            ContentValues userValues = new ContentValues();
            userValues.put("NIC", nic);
            userValues.put("DOB", dob);
            userValues.put("gender", gender);

            long result = db.insert(USERDETAILS_TABLE, null, userValues);
            return result != -1;
        }
        return false;
    }
    public boolean checkUser(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE email = ? AND password = ?",
                new String[]{email, password});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
    public boolean checkEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT email FROM " + USER_TABLE + " WHERE email = ?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }
    public boolean checkNIC(String nic) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT email FROM " + USER_TABLE + " WHERE NIC = ?", new String[]{nic});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }
    @SuppressLint("Range")
    public UserModel getUserByEmail(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT u.*, ud.dob AS dob, ud.gender AS gender " +
                "FROM "+ USER_TABLE+ " u " +
                "JOIN "+USERDETAILS_TABLE+ " ud ON ud.NIC= u.NIC " +
                "WHERE u.email = ?" , new String[]{email});

        UserModel userModel = null;

        if (cursor !=null){
            if (cursor.moveToFirst()){
                userModel = new UserModel();
                userModel.setUserId(cursor.getInt(cursor.getColumnIndex("userId")));
                userModel.setName(cursor.getString(cursor.getColumnIndex("name")));
                userModel.setNumber(cursor.getString(cursor.getColumnIndex("mobile_number")));
                userModel.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                userModel.setDob(cursor.getString(cursor.getColumnIndex("dob")));
                userModel.setGender(cursor.getString(cursor.getColumnIndex("gender")));
                userModel.setNic(cursor.getString(cursor.getColumnIndex("NIC")));
                userModel.setImage(cursor.getBlob(cursor.getColumnIndex("image")));

            }
            cursor.close();
        }
        return userModel;
    }
    public List<BikeModel> getAllBikes() {
        List<BikeModel> bikeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT b.*, s.location AS location " +
                "FROM " + BIKE_TABLE+" b " +
                "JOIN "+STATION_TABLE+" s ON s.stationId=b.stationId " +
                "ORDER BY bikeid DESC ",null);

        if (cursor.moveToFirst()) {
            do {
                bikeList.add(new BikeModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getDouble(4),
                        cursor.getDouble(5),
                        cursor.getString(8),
                        cursor.getInt(7)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bikeList;
    }
    public boolean updateUser(int id, String name,String contact, String dob,String gender, byte[] image, String nic){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("mobile_number", contact);
        values.put("image", image);

        long user = db.update(USER_TABLE, values, "userId = ?", new String[]{String.valueOf(id)});

        if(user != -1){
            ContentValues userValues = new ContentValues();
            userValues.put("DOB",dob);
            userValues.put("gender",gender);

            long result = db.update(USERDETAILS_TABLE, userValues, "NIC = ?", new String[]{(nic)});
            return result != -1;
        }
        return false;
    }
    public List<StationModel> getAllStation(){
        List<StationModel> stationList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + STATION_TABLE,null);
        if (cursor.moveToFirst()) {
            do {
                stationList.add(new StationModel(
                        cursor.getInt(0),
                        cursor.getString(1)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return stationList;

    }
    public boolean addReservation(int userId, int bikeId,double amount,String pickupTime, String endTime,String status,String pickupStation, int returnStationId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId",userId);
        values.put("bikeId",bikeId);
        values.put("amount",amount);
        values.put("status",status);
        values.put("startTime",pickupTime);
        values.put("endTime",endTime);
        values.put("pickup_station",pickupStation);
        values.put("return_stationId",returnStationId);

        long reservation = db.insert(RESERVATION_TABLE,null,values);
        if (reservation != -1){
            ContentValues bike = new ContentValues();
            bike.put("status",0);

            long result = db.update(BIKE_TABLE,bike,"bikeId=?",new String[]{String.valueOf(bikeId)});
            return result != -1;

        }
        return false;
    }
    public List<RentalModel> getAllRentHistryByUserId(int userId){
        List<RentalModel> rentalModelList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT r.*, s.location AS location " +
                "FROM " + RESERVATION_TABLE +" r " +
                "JOIN "+STATION_TABLE+" s ON s.stationId=r.return_stationId " +
                "WHERE userId = "+userId +
                " ORDER BY r.id DESC ",null);

        if (cursor.moveToFirst()) {
            do {
                rentalModelList.add(new RentalModel(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getDouble(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getInt(8),
                        cursor.getString(9)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return rentalModelList;
    }
    @SuppressLint("Range")
    public BikeModel getBikeById(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT b.*, s.location AS location " +
                "FROM " + BIKE_TABLE+" b " +
                "JOIN "+STATION_TABLE+" s ON s.stationId=b.stationId " +
                "WHERE bikeId = ?" , new String[]{String.valueOf(id)});

        BikeModel bikeModel = null;

        if (cursor !=null){
            if (cursor.moveToFirst()){
                bikeModel = new BikeModel();
                bikeModel.setBikeId(cursor.getInt(cursor.getColumnIndex("bikeId")));
                bikeModel.setName(cursor.getString(cursor.getColumnIndex("name")));
                bikeModel.setType(cursor.getString(cursor.getColumnIndex("type")));
                bikeModel.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                bikeModel.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
                bikeModel.setRating(cursor.getDouble(cursor.getColumnIndex("rating")));
                bikeModel.setLocation(cursor.getString(cursor.getColumnIndex("location")));
                bikeModel.setImage(cursor.getInt(cursor.getColumnIndex("image")));
            }
            cursor.close();
        }
        return bikeModel;
    }
    public boolean updateReservationStatus(int rentId, String status,int bikeId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", status);
        long updateStatus = db.update(RESERVATION_TABLE, values, "id = ?", new String[]{String.valueOf(rentId)});

        if (updateStatus != -1 && status.equals("Canceled") || status.equals("Completed")){
            ContentValues bike = new ContentValues();
            bike.put("status", 1);
            long result = db.update(BIKE_TABLE, bike, "bikeId = ?", new String[]{String.valueOf(bikeId)});
            return result !=-1;
        }
        return updateStatus !=-1;
    }
    public boolean updateBikeStation(int bikeId,int stationId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("stationId", stationId);
        long result = db.update(BIKE_TABLE, values, "bikeId = ?", new String[]{String.valueOf(bikeId)});
        return result != -1;
    }
}
