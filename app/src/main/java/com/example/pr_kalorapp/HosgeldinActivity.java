package com.example.pr_kalorapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class HosgeldinActivity extends AppCompatActivity {
    Button btn_hosgeldin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosgeldin);

    }
    public void btnTiklandi(View view)
    {
        Button btn_hosgeldin = findViewById(R.id.btn_hosgeldin);
        EditText edtxtName=findViewById(R.id.edtxtName);
        btn_hosgeldin.setText("MERHABA "+edtxtName.getText().toString());

        btn_hosgeldin=findViewById(R.id.btn_hosgeldin);
        btn_hosgeldin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HosgeldinActivity.this,MainActivity.class));
            }
        });
    }

}