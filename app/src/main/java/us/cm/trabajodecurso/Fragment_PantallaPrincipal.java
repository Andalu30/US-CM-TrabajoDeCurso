package us.cm.trabajodecurso;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Fragment_PantallaPrincipal extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pantalla_principal,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        final Switch swNoprox = (Switch) getView().findViewById(R.id.swNoProx);
        final TextView proxText = (TextView) getView().findViewById(R.id.txProximoEvento);
        final CardView cardProx = (CardView) getView().findViewById(R.id.cardProximoEvento);


        // DEBUG SWITCH
        swNoprox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    proxText.setVisibility(View.GONE);
                    cardProx.setVisibility(View.GONE);
                }else{
                    proxText.setVisibility(View.VISIBLE);
                    cardProx.setVisibility(View.VISIBLE);
                }
            }
        });
        // DEBUG SWITCH----


        Button btExplorar = (Button) getView().findViewById(R.id.bt_explorar);
        Button btProxReserva = (Button) getView().findViewById(R.id.bt_proxReserva);

        btExplorar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Fragment_Explorar();
                FragmentManager fragMan = getFragmentManager();
                FragmentTransaction ft = fragMan.beginTransaction();
                ft.replace(R.id.screenArea, fragment).addToBackStack("back").commit();
            }
        });

        btProxReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Fragment_ver_reserva();
                FragmentManager fragMan = getFragmentManager();
                FragmentTransaction ft = fragMan.beginTransaction();
                ft.replace(R.id.screenArea, fragment).addToBackStack("back").commit();
            }
        });





//        // Write a message to the database
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("usuarios/test");
//
//        myRef.setValue("Hello, World!");
//
//
//        DatabaseReference myRef2 = database.getReference("usuarios/anotherOne");
//        myRef2.setValue("Hello, World!");
//




        // Read a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("reservas");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("DB","On data change llamado");
//
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                Map<String, Object> reservas = (HashMap<String, Object>) dataSnapshot.getValue();
//
//                Map<String, Object> reserva1 = (Map<String, Object>) reservas.get("reserva1");
//
//                String descripcion = reserva1.get("descripcion").toString();
//                String fecha = reserva1.get("fecha").toString();
//                String disponibilidad = reserva1.get("disponibilidad").toString();
//                String horario = reserva1.get("horario").toString();
//                String titulo = reserva1.get("titulo").toString();
//                String id = reserva1.get("id").toString();
//
//
//                Log.d("DB",id+" "+titulo+" "+descripcion+" "+horario+" "+fecha+" "+disponibilidad);
//
//                TextView cardTit = getView().findViewById(R.id.pprincipal_proxev_tit);
//                TextView carddesc = getView().findViewById(R.id.pprincipal_proxev_descrip);
//
//                cardTit.setText(titulo);
//                carddesc.setText(descripcion);



            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("DB", "Failed to read value.", error.toException());
            }
        });



    }
}
