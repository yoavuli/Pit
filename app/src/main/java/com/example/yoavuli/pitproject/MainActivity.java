package com.example.yoavuli.pitproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.yoavuli.pitproject.views.Pit;

public class MainActivity extends AppCompatActivity {

    private Pit pitView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pitView = findViewById(R.id.pitView);
        View addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pitView.addPoint();
            }
        });
    }
}
