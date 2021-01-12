package hung.hust.doanhung.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import hung.hust.doanhung.R;
import hung.hust.doanhung.databinding.FragmentMapBinding;
import hung.hust.doanhung.mylocation.GPSTracker;

public class fragment_map extends Fragment {

    FragmentMapBinding binding;
    private GoogleMap mMap;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       binding = FragmentMapBinding.inflate(inflater,container,false);
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                MarkerOptions markerOptions = new MarkerOptions();
                LatLng latLng = getLocation();
                markerOptions.position(latLng);
                markerOptions.title("lat : "+latLng.latitude+""+"log : "+latLng.longitude);
                
                googleMap.clear();
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10000));
                googleMap.addMarker(markerOptions);

            }
        });
       return  binding.getRoot();

    }

    public LatLng getLocation(){
        GPSTracker gpsTracker = new GPSTracker(getContext());
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            return new LatLng(latitude,longitude);
        }else{
            gpsTracker.showSettingsAlert();
            return new LatLng(00,0);
        }
    }
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(20.9838634, 105.8208195);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Bạn đang ở đây   "));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//    }
}
