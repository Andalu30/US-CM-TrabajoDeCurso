package us.cm.trabajodecurso;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Range;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.support.constraint.Constraints.TAG;

public class Fragment_Explorar extends Fragment implements MyAdapterReserva.OnReservaListener{


    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private List<Reserva> mdatasetReservas = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;



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
                    //mTextMessage.setText("Deportes");
                    return true;
                case R.id.navigation_dashboard:
                    //mTextMessage.setText("Entretenimiento");
                    return true;
                case R.id.navigation_notifications:
                    //mTextMessage.setText("Estudios");
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        BottomNavigationView navigation = (BottomNavigationView) getView().findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {

        }



//        //preparaRecicler();
//        List<Long> test = new ArrayList<>();
//        test.add(new Long(110));
//        test.add(new Long(108));
//        test.add(new Long(105));
//        getInformacionReservasUsuario(test);
        preparaRecicler();
    }







    private void preparaRecicler() {
        DatabaseReference db_reserv_user =
                FirebaseDatabase.getInstance().getReference("/reservas/");

        db_reserv_user.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Object> numsReservas = (List<Object>) dataSnapshot.getValue();
                List listnumeros = new ArrayList<>();
                for (int i = 0; i < numsReservas.size(); i++) {
                    listnumeros.add(i);
                }


                if (numsReservas == null || numsReservas.size()==1) //O no hay o es la nula (reserva 0)
                    errorDBsinReservas();
                else
                    Log.i(TAG, "onDataChange: "+numsReservas);
                    //listnumeros.addAll(numsReservas.keySet());
                    getInformacionReservasUsuario(listnumeros);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DB", "No se ha podido acceder a las reservas del usuario");
            }
        });
    }
    private void getInformacionReservasUsuario(List<Integer> reservas) {
        /**Se encarga de crear Reservas con la info de los numeros de las reservas del usuario */
        Log.i("DB", "numeros reservas usuario: "+reservas);

        final List<Map<String, Object>> infoReservas = new ArrayList<>();

        for (Integer numReserva:reservas) {
            if (numReserva == 0){
                continue;
            }

            DatabaseReference db_reserv_user =
                    FirebaseDatabase.getInstance().getReference("/reservas/"+numReserva);

            db_reserv_user.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    Log.i("DB",map.toString());


                    String titulo = (String) map.get("titulo");
                    String descripcion = (String) map.get("descripcion");
                    String horario = (String) map.get("horario");
                    String ubicacion = (String) map.get("ubicacion");
                    String fecha = (String) map.get("fecha");
                    String centro = (String) map.get("centro");
                    String disponibilidad = (String) map.get("disponibilidad");
                    String tipo = (String) map.get("tipo");


                    Reserva reserva = new Reserva(titulo, descripcion, horario,ubicacion, fecha, centro, disponibilidad, tipo);


                    if (reserva.getFecha().getTime().after(Calendar.getInstance().getTime())) {
                        //Si es en el futuro
                        dibujaReservasProximas(reserva);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("DB", "No se ha podido acceder a la informacion de la reserva");
                }
            });

        }
    }


    private void dibujaReservasProximas(Reserva reserva){

        recyclerView = (RecyclerView) this.getView().findViewById(R.id.reciclerReservas);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        mdatasetReservas.add(reserva);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapterReserva(mdatasetReservas,this);
        recyclerView.setAdapter(mAdapter);

    }





    private void errorDBsinReservas() {
        Toast.makeText(this.getContext(), "Error en la base de datos, no hay reservas", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onReservaClick(int position) {
        /**Metodo encargado de gestionar los clics en los recicler views. Hace un intent con la
         * informacion de la reserva para poder enseñarla en la otra activity*/

        Log.d(TAG, "onReservaClick: clicked! Posistion: "+position);
        Log.d(TAG, "onReservaClick: "+ mdatasetReservas.get(position).toString());
        Intent intent = new Intent(this.getContext(), VerInfoReservaActivity.class);

        intent.putExtra("reservaSeleccionada", mdatasetReservas.get(position));
        intent.putExtra("codigoReserva",position);
        intent.putExtra("datasetreservas", (Serializable) mdatasetReservas);
        getActivity().startActivity(intent);
    }
}
