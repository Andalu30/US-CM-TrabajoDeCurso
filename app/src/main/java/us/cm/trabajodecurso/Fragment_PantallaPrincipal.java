package us.cm.trabajodecurso;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.constraint.Constraints.TAG;

public class Fragment_PantallaPrincipal extends Fragment {

    private FirebaseUser mFirebaseUser;

    private Reserva mReservaMasProxima = new Reserva();

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
                startActivity(new Intent(getContext(), VerReservaActivity.class));
            }
        });






        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mFirebaseUser == null){
            NoProximoEvento();
        }else{
            PreparaProximaReserva();
            PreparaReservaDestacada();
        }



    }

    public void NoProximoEvento(){
        final TextView proxText = (TextView) getView().findViewById(R.id.txProximoEvento);
        final CardView cardProx = (CardView) getView().findViewById(R.id.cardProximoEvento);
        proxText.setVisibility(View.GONE);
        cardProx.setVisibility(View.GONE);
    }


    private void PreparaReservaDestacada(){

    }


    private void PreparaProximaReserva(){
        DatabaseReference db_reserv_user = FirebaseDatabase.getInstance().getReference("/usuarios/"+mFirebaseUser.getUid()+"/susreservas");
        db_reserv_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Long> numsReservas = (List<Long>) dataSnapshot.getValue();

                if (numsReservas == null || numsReservas.size()==1) //O no hay o es la nula (reserva 0)
                    NoProximoEvento();
                else
                    getInformacionReservasUsuario(numsReservas);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DB", "No se ha podido acceder a las reservas del usuario");
            }
        });



    }


    private void getInformacionReservasUsuario(List<Long> reservas) {
        Log.i("DB", "numeros reservas usuario: "+reservas);

        final List<Map<String, Object>> infoReservas = new ArrayList<>();

        for (Long numReserva:reservas) {
            if (numReserva == 0){
                continue;
            }
            // Numeros de las reservas del usuario

            DatabaseReference db_reserv_user = FirebaseDatabase.getInstance().getReference("/reservas/"+numReserva);
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


                    dibujaReservas(reserva);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("DB", "No se ha podido acceder a la informacion de la reserva");
                }
            });

        }
    }

    private void dibujaReservas(final Reserva reserva){
        TextView titulo = this.getView().findViewById(R.id.pprincipal_proxev_tit);
        titulo.setText(reserva.getTitulo());
        TextView desc = this.getView().findViewById(R.id.pprincipal_proxev_descrip);
        desc.setText(reserva.getDescripcion());

        Button prox = this.getView().findViewById(R.id.bt_proxReserva);
        prox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), VerReservaActivity.class);
                intent.putExtra("reservaSeleccionada", reserva);
                getActivity().startActivity(intent);
            }
        });

    }


}
