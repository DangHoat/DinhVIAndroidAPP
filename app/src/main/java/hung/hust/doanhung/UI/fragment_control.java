package hung.hust.doanhung.UI;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import hung.hust.doanhung.databinding.FragmentControlBinding;
import hung.hust.doanhung.define.Constans;
import hung.hust.doanhung.mylocation.GPSTracker;
import hung.hust.doanhung.service.LocationService;


public class fragment_control  extends Fragment {
    Context context;
    private GPSTracker gpsTracker;
    FragmentControlBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentControlBinding.inflate(inflater,container,false);
        context = getContext();
        View v = binding.getRoot();
        binding.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(context,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                }else {
                    startLocation();
                }

            }
        });
        binding.btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLocation();
            }
        });
        binding.btnLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });
        return v;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
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

    public void getLocation(){
        gpsTracker = new GPSTracker(getContext());
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            Toast.makeText(
                    context,
                    "latitude :" + Double.toString(latitude) +"\n"
                            +"longitude :" + Double.toString(longitude),
                    Toast.LENGTH_LONG
            ).show();
        }else{
            gpsTracker.showSettingsAlert();
        }
    }
    private boolean isServiceRuning(){
        ActivityManager activityManager = (ActivityManager)getActivity().getSystemService(Context.ACTIVITY_SERVICE);
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
            Intent i =  new Intent(getContext(), LocationService.class);
            i.setAction(Constans.LOCATION_START);
            getActivity().startService(i);
            Toast.makeText(context,"Bắt đầu chạy service",Toast.LENGTH_LONG).show();
        }
    }
    public void stopLocation(){
        if(isServiceRuning()){
            Intent i =  new Intent(context, LocationService.class);
            i.setAction(Constans.LOCATION_STOP);
            context.startService(i);
            Toast.makeText(context,"Dừng chạy service",Toast.LENGTH_LONG).show();
        }
    }
}
