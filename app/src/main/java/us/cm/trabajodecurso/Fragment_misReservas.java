package us.cm.trabajodecurso;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
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
import android.widget.ScrollView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Fragment_misReservas extends Fragment {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setToolBarTitle("Mis reservas");

        return inflater.inflate(R.layout.pantalla_misreservas,null);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        // DEBUG SWITCHES
        Switch swSinReservas = getView().findViewById(R.id.switchNoReserva);
        Switch swNoLogin = getView().findViewById(R.id.switchNoLogin);
        Switch swNoProx = getView().findViewById(R.id.switchProxima);

        final ScrollView svReservas = getView().findViewById(R.id.scrollViewReservas);

        final CardView cardProx = getView().findViewById(R.id.cardViewProxima);

        final Button btNologin = getView().findViewById(R.id.buttonNoLogin);
        final Button btNoReservas = getView().findViewById(R.id.buttonNingunaReserva);

        final TextView txNoLogin = getView().findViewById(R.id.textoNoLogin);
        final TextView txNoReserva = getView().findViewById(R.id.textoNingunaReserva);
        final TextView txProx = getView().findViewById(R.id.proxres);

        swNoProx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txProx.setVisibility(View.GONE);
                    cardProx.setVisibility(View.GONE);
                } else {
                    txProx.setVisibility(View.VISIBLE);
                    cardProx.setVisibility(View.VISIBLE);
                }
            }
        });

        swSinReservas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                    svReservas.setVisibility(View.GONE);
                    btNoReservas.setVisibility(View.VISIBLE);
                    txNoReserva.setVisibility(View.VISIBLE);

                    txNoLogin.setVisibility(View.GONE);
                    btNologin.setVisibility(View.GONE);

                }else{
                    svReservas.setVisibility(View.VISIBLE);
                    btNoReservas.setVisibility(View.VISIBLE);
                    txNoReserva.setVisibility(View.VISIBLE);

                    txNoLogin.setVisibility(View.VISIBLE);
                    btNologin.setVisibility(View.VISIBLE);

                }
            }
        });

        swNoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                    svReservas.setVisibility(View.GONE);
                    btNoReservas.setVisibility(View.GONE);
                    txNoReserva.setVisibility(View.GONE);

                    txNoLogin.setVisibility(View.VISIBLE);
                    btNologin.setVisibility(View.VISIBLE);

                }else{
                    svReservas.setVisibility(View.VISIBLE);
                }
            }
        });



        // DEBUG SWITCHES}


        Button bt_explorar = (Button) getView().findViewById(R.id.buttonNingunaReserva);
        Button bt_login =  (Button) getView().findViewById(R.id.buttonNoLogin);
        Button bt_verReservaProx = (Button) getView().findViewById(R.id.bt_verReservaProxima);



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
                Fragment fragment = new Fragment_ver_reserva();
                FragmentManager fragMan = getFragmentManager();
                FragmentTransaction ft = fragMan.beginTransaction();
                ft.replace(R.id.screenArea, fragment).addToBackStack("back").commit();
            }
        });



        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            this.NoLogueado();
        }




        // Read de la DB


        final List<Long> reservas_del_usuario = new ArrayList<>();


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference db_reserv_user = database.getReference("/usuarios/andalu30/susreservas");

        db_reserv_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Long> test = (List<Long>) dataSnapshot.getValue();
                for (Long x:test) {
                    Log.i("DB","resIndiv: "+x);
                    reservas_del_usuario.add(reservas_del_usuario.size(),x);
                }

                Log.i("DB",test.toString());
          }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DB", "No se ha podido acceder a las reservas del usuario");
            }
        });

        Log.i("DB","reservas del usuario="+reservas_del_usuario);

        for (Long k : reservas_del_usuario) {
            System.out.println(k);
            Log.i("RESERVAUSER","Vamos por la reserva:"+k);

            //Get info de las reservas del usuario
            DatabaseReference db_reserva_ind = database.getReference("/reservas/"+k);

            db_reserva_ind.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Log.i("DB",dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("DB", "No se ha podido acceder a la informacion de la reserva");
                }
            });

        }










        recyclerView = (RecyclerView) view.findViewById(R.id.pmisreservas_recycler);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        String[] mydataset = {"Titulo","Descripcion","Titulo","Descripcion","Titulo","Descripcion","Titulo","Descripcion","Titulo","Descripcion","Titulo","Descripcion"};
        mAdapter = new MyAdapter(mydataset);
        recyclerView.setAdapter(mAdapter);













        }

    private void NoLogueado() {
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

}
