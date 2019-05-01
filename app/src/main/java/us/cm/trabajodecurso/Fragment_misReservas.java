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
import android.widget.ScrollView;
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
import java.util.List;
import java.util.Map;

import static android.support.constraint.Constraints.TAG;

public class Fragment_misReservas extends Fragment implements MyAdapterReserva.OnReservaListener{
    /**Fragment encargado de mostrar todos las reservas del usuario. Implementa MyAdapterReserva
     * .OnReservaListener para que se pueda pulsar la reserva dentro de cada uno de los
     * RecyclerView*/

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();

    private List<Reserva> mdatasetReservas = new ArrayList<>();
    private List<Reserva> mreservasPasadas = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ((MainActivity) getActivity()).setToolBarTitle("Mis reservas");
        return inflater.inflate(R.layout.pantalla_misreservas,null);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        final ScrollView svReservas = getView().findViewById(R.id.scrollViewReservas);
        final CardView cardProx = getView().findViewById(R.id.cardViewProxima);
        final Button btNologin = getView().findViewById(R.id.buttonNoLogin);
        final Button btNoReservas = getView().findViewById(R.id.buttonNingunaReserva);
        final TextView txNoReserva = getView().findViewById(R.id.textoNingunaReserva);
        final TextView txProx = getView().findViewById(R.id.proxres);
        final TextView txNoLogin = getView().findViewById(R.id.textoNoLogin);

        Button bt_explorar = (Button) getView().findViewById(R.id.buttonNingunaReserva);
        Button bt_login =  (Button) getView().findViewById(R.id.buttonNoLogin);
        Button bt_verReservaProx = (Button) getView().findViewById(R.id.bt_verReservaProxima);


        //Clics de botons
        bt_explorar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Fragment_Explorar();
                FragmentManager fragMan = getFragmentManager();
                FragmentTransaction ft = fragMan.beginTransaction();
                ft.replace(R.id.screenArea, fragment).addToBackStack("back").commit();
            }
        });

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LoginActivity.class));
                return;
            }
        });

        bt_verReservaProx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), VerReservaActivity.class));
            }
        });



        btNoReservas.setVisibility(View.GONE);
        txNoReserva.setVisibility(View.GONE);

        txNoLogin.setVisibility(View.GONE);
        btNologin.setVisibility(View.GONE);




        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            // Si no esta logueado llevarlo a que se loguee
            startActivity(new Intent(this.getContext(), LoginActivity.class));

        }


        Log.i("USER",mFirebaseUser.getUid());



        // Igual que en MainActivity, no es lo mejor, porque gastamos consultas en firebase pero
        // da el apaño.


        // Numeros de las reservas del usuario
        DatabaseReference db_reserv_user =
                database.getReference("/usuarios/"+mFirebaseUser.getUid()+"/susreservas/");

        db_reserv_user.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Long> numsReservas = (List<Long>) dataSnapshot.getValue();

                if (numsReservas == null || numsReservas.size()==1) //O no hay o es la nula (reserva 0)
                    ningunaReserva();
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
        /**Se encarga de crear Reservas con la info de los numeros de las reservas del usuario */
        Log.i("DB", "numeros reservas usuario: "+reservas);

        final List<Map<String, Object>> infoReservas = new ArrayList<>();

        for (Long numReserva:reservas) {
            if (numReserva == 0){
                continue;
            }

            DatabaseReference db_reserv_user = database.getReference("/reservas/"+numReserva);

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


                    if (reserva.getFecha().getTime().after(Calendar.getInstance().getTime())){
                        //Si es en el futuro
                        dibujaReservasProximas(reserva);
                    }else{
                        dibujaReservasPasadas(reserva);
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("DB", "No se ha podido acceder a la informacion de la reserva");
                }
            });

        }


        Log.i("INFO", mdatasetReservas.toString());
    }

    private void dibujaReservasPasadas(Reserva reserva){
        /**Se añade la reserva al dataset correspondiente y se llama al adapter para poder
         * meterlo en el recycler view */
        recyclerView = (RecyclerView) this.getView().findViewById(R.id.reservasPasadasRecicler);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        mreservasPasadas.add(reserva);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapterReserva(mreservasPasadas,this);
        recyclerView.setAdapter(mAdapter);

    }

    private void dibujaReservasProximas(Reserva reserva){
        //Lo mismo pero con las proximas

        recyclerView = (RecyclerView) this.getView().findViewById(R.id.pmisreservas_recycler);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        mdatasetReservas.add(reserva);
        actualizaProximaReserva();

        // specify an adapter (see also next example)
        mAdapter = new MyAdapterReserva(mdatasetReservas,this);
        recyclerView.setAdapter(mAdapter);
    }

    private void actualizaProximaReserva(){
        /** Modifica la tarjeta de la proxima reserva con la proxmia del dataset de las futuras.*/

        TextView proxTit = (TextView) getView().findViewById(R.id.mis_proxtit);
        TextView proxhor = (TextView) getView().findViewById(R.id.mis_proxhor);
        TextView proxdesc = (TextView) getView().findViewById(R.id.mis_proxdesc);
        Button verproxbut = (Button) getView().findViewById(R.id.bt_verReservaProxima);

        Calendar aux = Calendar.getInstance();
        aux.set(3000,12,12);
        Calendar now = Calendar.getInstance();

        for (final Reserva reserva : mdatasetReservas){
            Calendar a = reserva.getFecha();
            if (reserva.getFecha().before(aux) && reserva.getFecha().after(now)){
                aux = reserva.getFecha();

                proxTit.setText(reserva.getTitulo());
                proxhor.setText(reserva.getHorario());
                proxdesc.setText(reserva.getFecha().getTime().toString());


                verproxbut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), VerReservaActivity.class);

                        intent.putExtra("reservaSeleccionada", reserva);
                        getActivity().startActivity(intent);
                    }
                });


            }
        }
    }


    private void ningunaReserva() {
        /**Prepara la pantalla si no hay ninguna reserva*/
        final ScrollView svReservas = getView().findViewById(R.id.scrollViewReservas);
        final Button btNologin = getView().findViewById(R.id.buttonNoLogin);
        final Button btNoReservas = getView().findViewById(R.id.buttonNingunaReserva);
        final TextView txNoReserva = getView().findViewById(R.id.textoNingunaReserva);
        final TextView txNoLogin = getView().findViewById(R.id.textoNoLogin);


        svReservas.setVisibility(View.GONE);
        btNoReservas.setVisibility(View.VISIBLE);
        txNoReserva.setVisibility(View.VISIBLE);

        txNoLogin.setVisibility(View.GONE);
        btNologin.setVisibility(View.GONE);
    }

    private void NoLogueado() {
        /**Prepara la pantalla si no esta logueado, aunque no haria falta porque se lleva al
         * login al usuario.*/

        final ScrollView svReservas = getView().findViewById(R.id.scrollViewReservas);
        final CardView cardProx = getView().findViewById(R.id.cardViewProxima);
        final Button btNologin = getView().findViewById(R.id.buttonNoLogin);
        final Button btNoReservas = getView().findViewById(R.id.buttonNingunaReserva);
        final TextView txNoLogin = getView().findViewById(R.id.textoNoLogin);
        final TextView txNoReserva = getView().findViewById(R.id.textoNingunaReserva);
        final TextView txProx = getView().findViewById(R.id.proxres);


        svReservas.setVisibility(View.GONE);
        btNoReservas.setVisibility(View.GONE);
        txNoReserva.setVisibility(View.GONE);

        txNoLogin.setVisibility(View.VISIBLE);
        btNologin.setVisibility(View.VISIBLE);

    }

    @Override
    public void onReservaClick(int position) {
        /**Metodo encargado de gestionar los clics en los recicler views. Hace un intent con la
         * informacion de la reserva para poder enseñarla en la otra activity*/

        Log.d(TAG, "onReservaClick: clicked! Posistion: "+position);
        Log.d(TAG, "onReservaClick: "+ mdatasetReservas.get(position).toString());
        Intent intent = new Intent(this.getContext(), VerReservaActivity.class);

        intent.putExtra("reservaSeleccionada", mdatasetReservas.get(position));
        intent.putExtra("codigoReserva",position);
        getActivity().startActivity(intent);
    }
}
