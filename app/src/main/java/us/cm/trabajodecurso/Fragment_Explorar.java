package us.cm.trabajodecurso;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;

public class Fragment_Explorar extends Fragment {

    private TextView mTextMessage;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setToolBarTitle("Explorar");
        return inflater.inflate(R.layout.pantalla_explorar,null);

    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            //Este es el metodo que cambia el contenido de la pantalla al cambiar de pestaña
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText("Deportes");
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText("Entretenimiento");
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText("Estudios");
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mTextMessage = (TextView) getView().findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) getView().findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

}
