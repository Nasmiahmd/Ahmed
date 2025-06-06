package com.s22010020.ahmed;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class lab_03 extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap myMap;
    private EditText editTextAddress;
    private Button btnSearch;
    private Geocoder geocoder;

    List<Address> listGeocorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lab03);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextAddress = findViewById(R.id.editTextaddress);
        btnSearch = findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(v -> searchLocation());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        geocoder = new Geocoder(this, Locale.getDefault());
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;
        // Configure map settings
        myMap.getUiSettings().setZoomControlsEnabled(true);
        myMap.getUiSettings().setCompassEnabled(true);

        // Set default location (OUSL Puttalam)
        LatLng ousl = new LatLng(8.02341519686238, 79.83395214243748);
        myMap.addMarker(new MarkerOptions().position(ousl).title("OUSL Puttalam"));
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ousl, 15f));
    }

    private void searchLocation() {
        String location = editTextAddress.getText().toString().trim();

        if (location.isEmpty()) {
            Toast.makeText(this, "Please enter an address", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading state
        btnSearch.setEnabled(false);
        btnSearch.setText("Searching...");

        new Thread(() -> {
            try {
                List<Address> addresses = geocoder.getFromLocationName(location, 1);

                runOnUiThread(() -> {
                    btnSearch.setEnabled(true);
                    btnSearch.setText("Show location");

                    if (addresses == null || addresses.isEmpty()) {
                        Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!Geocoder.isPresent()) {
                        Toast.makeText(this, "Geocoder service not available on this device",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    Address address = addresses.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    // Update map
                    myMap.clear();
                    myMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(location));
                    myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
                });
            } catch (IOException e) {
                runOnUiThread(() -> {
                    btnSearch.setEnabled(true);
                    btnSearch.setText("Show location");
                    Toast.makeText(this, "Network error. Please check your internet connection", Toast.LENGTH_SHORT).show();
                });
            } catch (IllegalArgumentException e) {
                runOnUiThread(() -> {
                    btnSearch.setEnabled(true);
                    btnSearch.setText("Show location");
                    Toast.makeText(this, "Invalid address format", Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    btnSearch.setEnabled(true);
                    btnSearch.setText("Show location");
                    Toast.makeText(this, "Geocoder service not available", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
}


//        try{
//            listGeocorder = new Geocoder(this).getFromLocationName("Open Univerty, Puttalam", 1);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//
//        double longtitude = listGeocorder.get(0).getLongitude();
//        double latitude = listGeocorder.get(0).getLatitude();
//        String countryName = listGeocorder.get(0).getCountryName();
//
//        Log.i("GOOGLE_MAP_TAG", "Longtitude value is: " + String.valueOf(longtitude) + "    " + "Latitude value is: " + String.valueOf(latitude) + "    "  + "Country name is: " + String.valueOf(countryName));
//    }
//    public void getLocation(String address){
//        try{
//            Uri uri = Uri.parse("https://www.google.com/maps/search/" + address);
//
//            Intent intent =new Intent(Intent.ACTION_VIEW,uri);
//
//            intent.setPackage(String.valueOf(R.id.map));
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//            startActivity(intent);
//        }catch(ActivityNotFoundException e){
//            Uri uri =  Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps&hl=en&gl=US");
//            Intent intent =new Intent(Intent.ACTION_VIEW,uri);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//
//        }
//    }
//    @Override
//    public void onMapReady(@NonNull GoogleMap googleMap) {
//        myMap = googleMap;
//        LatLng ousl = new LatLng(8.02341519686238, 79.83395214243748);
//        myMap.addMarker(new MarkerOptions().position(ousl).title("Marker OUSL Puttalam"));
//        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ousl, 18.0f));
//        myMap.getUiSettings().setZoomControlsEnabled(true);
//    }
//}