package com.example.citycyclerentals;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class ProfileFragment extends Fragment {

    private EditText etName,etEmail, etContact,etDob,etNIC;
    private TextView etWelcome;
    private ImageView ivProfile;
    private String email;
    private int id;
    private RadioGroup rgGender;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private Button update,logout,promotion;
    private DBHelper dbHelper;
    private byte[] imageBytes;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         dbHelper= new DBHelper(requireContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View views = inflater.inflate(R.layout.fragment_profile, container, false);
        etName = views.findViewById(R.id.etName);
        etEmail= views.findViewById(R.id.etEmail);
        etContact = views.findViewById(R.id.etContact);
        etNIC = views.findViewById(R.id.etNIC);
        etDob = views.findViewById(R.id.etDob);
        ivProfile = views.findViewById(R.id.imgUpdateProfile);
        update = views.findViewById(R.id.btnUpdate);
        logout = views.findViewById(R.id.btnLogout);
        promotion = views.findViewById(R.id.btnPromotion);
        rgGender = views.findViewById(R.id.rgGender);
        etWelcome = views.findViewById(R.id.tvWelcome);
        rbMale = views.findViewById(R.id.rbMale);
        rbFemale = views.findViewById(R.id.rbFemale);

        etDob.setOnClickListener(view -> showDatePicker());

        ivProfile.setOnClickListener(view -> selectImageFromGallery());

        update.setOnClickListener(view -> updateUser());
        promotion.setOnClickListener(v -> {
            FragmentManager fm = requireActivity().getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.frameLayout, new PromotionFragment())
                    .addToBackStack(null)
                    .commit();
        });
        logout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SigninActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        loadUserData();
        return views;
    }

    public void loadUserData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("email", null);  // Retrieve email from shared preferences
        UserModel userModel = dbHelper.getUserByEmail(email);

        if (userModel!=null) {
            etWelcome.setText("Welcome, " + userModel.getName() +"!" );
            id = userModel.getUserId();
            etName.setText(userModel.getName());
            etEmail.setText(userModel.getEmail());
            etContact.setText(userModel.getNumber());
            etNIC.setText(userModel.getNic());
            etDob.setText(userModel.getDob());
            if (userModel.getGender() != null) {
                if (userModel.getGender().equals("Male")) {
                    rbMale.setChecked(true);
                } else if (userModel.getGender().equals("Female")) {
                    rbFemale.setChecked(true);
                }
            }

            if (userModel.getImage() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(userModel.getImage(), 0, userModel.getImage().length);
                ivProfile.setImageBitmap(bitmap);
                imageBytes = userModel.getImage();
            } else {
                ivProfile.setImageResource(R.drawable.profile_icon); // Default profile image
            }
        }
    }


    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, year1, month1, dayOfMonth) -> {
            String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
            etDob.setText(selectedDate);
        }, year, month, day);

        datePickerDialog.show();
    }

    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            ivProfile.setImageURI(imageUri);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                imageBytes = outputStream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void updateUser() {
        String name = etName.getText().toString();
        String contact = etContact.getText().toString();
        String dob = etDob.getText().toString();
        String nic = etNIC.getText().toString();
        int selectedGenderId = rgGender.getCheckedRadioButtonId();
        String gender = selectedGenderId != -1 ? ((RadioButton) getView().findViewById(selectedGenderId)).getText().toString() : "";

        if (name.isEmpty() || contact.isEmpty() || dob.isEmpty() || imageBytes.length ==0) {
            Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean result = dbHelper.updateUser(id,name, contact, dob, gender, imageBytes, nic);
        if (result) {
            Toast.makeText(requireContext(), "update successful!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Error updating user!", Toast.LENGTH_SHORT).show();
        }
    }



}
