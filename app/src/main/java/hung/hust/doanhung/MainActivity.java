package hung.hust.doanhung;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import hung.hust.doanhung.define.Constans;
import hung.hust.doanhung.mylocation.GPSTracker;
import hung.hust.doanhung.service.LocationService;

public class MainActivity extends AppCompatActivity {
    private GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                }else {
                    startLocation();
                }
            }
        });

        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLocation();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1 && grantResults.length > 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startLocation();
            }
        }
    }

    public void getLocation(View view){
        gpsTracker = new GPSTracker(MainActivity.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            Toast.makeText(
                        this,
                    "latitude :" + Double.toString(latitude) +"\n"
                    +"longitude :" + Double.toString(longitude),
                    Toast.LENGTH_LONG
            ).show();
        }else{
            gpsTracker.showSettingsAlert();
        }
    }
    private boolean isServiceRuning(){
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if(activityManager!= null){
            for (ActivityManager.RunningServiceInfo serviceInfo:
                    activityManager.getRunningServices(Integer.MAX_VALUE)){
                if(LocationService.class.getName().equals(serviceInfo.service.getClassName())){
                    if(serviceInfo.foreground) return true;
                }
            }
            return false;
        }
        return false;
    }
    public void startLocation(){
        if(!isServiceRuning()){
            Intent i =  new Intent(getApplicationContext(), LocationService.class);
            i.setAction(Constans.LOCATION_START);
            startService(i);
            Toast.makeText(this,"Bắt đầu chạy service",Toast.LENGTH_LONG).show();
        }
    }
    public void stopLocation(){
        if(isServiceRuning()){
            Intent i =  new Intent(getApplicationContext(), LocationService.class);
            i.setAction(Constans.LOCATION_STOP);
            startService(i);
            Toast.makeText(this,"Dừng chạy service",Toast.LENGTH_LONG).show();
        }
    }
}