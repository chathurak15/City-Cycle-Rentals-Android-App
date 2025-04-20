package com.example.citycyclerentals;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class SignupActivity extends AppCompatActivity {
    private EditText etName,etEmail,etDob, etContact,etNIC, etPassword, etConfirmPassword;
    private RadioGroup rgGender;
    private ImageView imgProfile;
    private CheckBox cbTerms;
    private TextView tvLogin;
    private DBHelper dbHelper;

    private byte[] imageBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        dbHelper = new DBHelper(this);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etContact = findViewById(R.id.etContact);
        etDob = findViewById(R.id.etDob);
        etNIC = findViewById(R.id.etNIC);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        rgGender = findViewById(R.id.rgGender);
        imgProfile = findViewById(R.id.imgProfile);
        cbTerms = findViewById(R.id.cbTerms);
        tvLogin = findViewById(R.id.tvLogin);
        Button btnRegister = findViewById(R.id.btnRegister);

        etDob.setOnClickListener(view -> showDatePicker());

        // Set onClick for ImageView to select image
        imgProfile.setOnClickListener(view -> selectImageFromGallery());

        // Register button action
        btnRegister.setOnClickListener(view -> registerUser());

        tvLogin.setOnClickListener(view -> {
            Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
            startActivity(intent);
        });
    }

    private void registerUser() {
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String dob = etDob.getText().toString();
        String number = etContact.getText().toString();
        String nic = etNIC.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        int selectedGenderId = rgGender.getCheckedRadioButtonId();
        String gender = selectedGenderId != -1 ? ((RadioButton) findViewById(selectedGenderId)).getText().toString() : "";

        String emailValidation = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        String passwordValidation = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$";
        String contactValidation = "^\\+94\\d{9}$";

        if (name.isEmpty()|| email.isEmpty()|| dob.isEmpty()|| number.isEmpty()||nic.isEmpty()||password.isEmpty()||confirmPassword.isEmpty()|| gender.isEmpty()|| imageBytes.length==0){
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!email.matches(emailValidation)) {
            Toast.makeText(this, "Email address is invalid.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!number.matches(contactValidation)) {
            Toast.makeText(this, "Number must have this type: +94789362885", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.matches(passwordValidation)) {
            Toast.makeText(this, "Password must be 8-20 characters, include uppercase, lowercase, a number, and a special character.", Toast.LENGTH_LONG).show();
            return;
        }
        if (!password.equals(confirmPassword)){
            Toast.makeText(this, "Password and Confirm password do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!cbTerms.isChecked()) {
            Toast.makeText(this, "Please agree to Terms and Conditions", Toast.LENGTH_SHORT).show();
            return;
        }
        if (dbHelper.checkEmail(email)){
            Toast.makeText(this, "Your email already exists! Use another email", Toast.LENGTH_SHORT).show();
                return;
        }
        if (dbHelper.checkNIC(nic)){
            Toast.makeText(this, "Your NIC number already exists! Check again!", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean result = dbHelper.insertUser(name,email,number,nic,gender,dob,password,imageBytes);
        if (result){
            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignupActivity.this,SigninActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Registration failed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 100);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            imgProfile.setImageURI(imageUri);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                imageBytes = outputStream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
            etDob.setText(selectedDate);
        }, year, month, day);

        datePickerDialog.show();
    }
}