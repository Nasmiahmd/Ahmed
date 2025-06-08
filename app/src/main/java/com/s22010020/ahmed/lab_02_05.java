package com.s22010020.ahmed;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class lab_02_05 extends AppCompatActivity {
    private EditText editTextUsername, editTextPassword;
    private Button btnLogin;
    DatabaseHelper myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lab0205);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);
        myDb = new DatabaseHelper(this);

        insertData();
    }
    public void insertData(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted = myDb.insertData(editTextUsername.getText().toString(), editTextPassword.getText().toString());
                if(isInserted){
                    Toast.makeText(lab_02_05.this, "Login Details Saved Successfully", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(lab_02_05.this, "Login Details Not Saved!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}