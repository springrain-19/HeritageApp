package com.rgzn.heritage;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private static final int REQUEST_PERMISSION = 2;
    private DatabaseHelper databaseHelper;
    private EditText editTextNickname, editTextPassword, editTextSignature;
    private Spinner spinnerPreferences;
    private String selectedAvatarPath = "";
    private String selectedBirthday = "";
    private ImageView imageView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        databaseHelper = new DatabaseHelper(this);
        editTextNickname = findViewById(R.id.editTextNickname);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextSignature = findViewById(R.id.editTextSignature);
        Button buttonSelectBirthday = findViewById(R.id.buttonSelectBirthday);
        Button buttonUploadAvatar = findViewById(R.id.buttonUploadAvatar);
        CheckBox checkBoxAgreement = findViewById(R.id.checkBoxAgreement);
        spinnerPreferences = findViewById(R.id.spinnerPreferences);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.preferences_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPreferences.setAdapter(adapter);

        buttonSelectBirthday.setOnClickListener(v -> showDatePickerDialog());
        buttonUploadAvatar.setOnClickListener(v -> selectImageFromGallery());
        checkBoxAgreement.setOnClickListener(v -> showAgreementDialog());

        Button buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(v -> attemptRegistration());
        imageView3 = findViewById(R.id.imageView3);
        if (selectedAvatarPath.isEmpty()) {
            imageView3.setImageResource(R.drawable.me);
        }
        // 为返回按钮添加点击事件
        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    selectedBirthday = year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                }, year, month, day);
        datePickerDialog.show();
    }



    private void selectImageFromGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImageFromGallery();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void showAgreementDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("用户协议")
                .setMessage("此app解释权归作者所有")
                .setPositiveButton("确定", (dialog, id) -> dialog.dismiss());
        builder.create().show();
    }

    private void attemptRegistration() {
        String username = editTextNickname.getText().toString();
        String password = editTextPassword.getText().toString();
        String signature = editTextSignature.getText().toString();
        String preference = spinnerPreferences.getSelectedItem().toString();

        if (username.isEmpty() || password.isEmpty() || selectedAvatarPath.isEmpty() || selectedBirthday.isEmpty() || preference.isEmpty()) {
            Toast.makeText(this, "请填写所有必填项", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isInserted = databaseHelper.addUser(username, password, signature, selectedAvatarPath, selectedBirthday, preference);
        if (isInserted) {
            showSuccessDialog();
        } else {
            Toast.makeText(this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
        }

    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("注册成功")
                .setMessage("3秒后将跳转到登录界面")
                .setCancelable(false)
                .show();

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 3000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            try {
                selectedAvatarPath = data.getData().toString();
                imageView3.setImageURI(data.getData());
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error while selecting image", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
