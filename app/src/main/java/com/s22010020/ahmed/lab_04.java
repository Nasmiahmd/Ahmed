package com.s22010020.ahmed;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class lab_04 extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor tempSensor;
    private TextView tempTextView;
    private TextView thresholdTextView;
    private MediaPlayer mediaPlayer;
    private final float TEMP_THRESHOLD = 20; // Last two digits of my SID (S22010020)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lab04);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tempTextView = findViewById(R.id.tempTextView);
        tempTextView.setGravity(Gravity.CENTER);
        thresholdTextView = findViewById(R.id.thresholdTextView);
        thresholdTextView.setGravity(Gravity.CENTER);

        // Initialize sensor manager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Check if temperature sensor exists or not
        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null) {
            tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        } else {
            Toast.makeText(this, "No temperature sensor found!", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initialize media player
        mediaPlayer = MediaPlayer.create(this, R.raw.audio);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float currentTemp = event.values[0];
        tempTextView.setText("Current Temp: " + currentTemp + "°C");

        if (currentTemp > TEMP_THRESHOLD && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            Toast.makeText(this, "Temperature exceeded threshold!", Toast.LENGTH_SHORT).show();
        } else if (currentTemp <= TEMP_THRESHOLD && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0); // Rewind to beginning
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        sensorManager.unregisterListener(this);
    }
}