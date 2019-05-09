package us.cm.trabajodecurso;

import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static android.support.constraint.Constraints.TAG;

public class Fragment_Perfil extends Fragment {

    private String FirebaseUserNombre;
    private String FirebaseUserEmail;
    private String mPhotoUrl;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    public static final String ANONYMOUS = "anonymous";
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private List<Reserva> mdatasetReservas = new ArrayList<>();




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setToolBarTitle("Mi perfil");
        return inflater.inflate(R.layout.pantalla_perfil,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        final TextView proxText = (TextView) getView().findViewById(R.id.txProximoEvento);
        final CardView cardProx = (CardView) getView().findViewById(R.id.cardProximoEvento);

        final TextView otrasText = (TextView) getView().findViewById(R.id.textOtras);
        final CardView otrascard = (CardView) getView().findViewById(R.id.cardOtras);


        Button btExplorar = (Button) getView().findViewById(R.id.bt_explorar);
        Button btMisReservas = (Button) getView().findViewById(R.id.bt_verReservas);
        Button btProxReserva = (Button) getView().findViewById(R.id.bt_proxReserva);



        btMisReservas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Fragment_misReservas();
                FragmentManager fragMan = getFragmentManager();
                FragmentTransaction ft = fragMan.beginTransaction();
                ft.replace(R.id.screenArea, fragment).addToBackStack("back").commit();
            }
        });

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
                startActivity(new Intent(getActivity(), VerReservaActivity.class));
            }
        });




        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this.getActivity(), LoginActivity.class));
            return;
        } else {
            FirebaseUserNombre = mFirebaseUser.getDisplayName();
            FirebaseUserEmail = mFirebaseUser.getEmail();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }

            //Cambiar datos header
            TextView name = (TextView) view.findViewById(R.id.nombreUsuario_header);
            TextView email = (TextView) view.findViewById(R.id.mailUsuario_header);
            ImageView fotoperfil = (ImageView) view.findViewById(R.id.fotoPerfil);
            Button loginheader = (Button) view.findViewById(R.id.bt_nav_login);
            loginheader.setVisibility(View.GONE);


            name.setText(FirebaseUserNombre);
            email.setText(FirebaseUserEmail);


            //Descargar imagen del usuario y colocarla
            MainActivity.GetBitmapFromURLAsync getBitmapFromURLAsync = new MainActivity.GetBitmapFromURLAsync();
            getBitmapFromURLAsync.execute(mPhotoUrl);
            Bitmap imagenUsuario = null;
            try {
                imagenUsuario = getBitmapFromURLAsync.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            fotoperfil.setImageBitmap(imagenUsuario);
        }


        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        DatabaseReference db_reserv_user =
                database.getReference("/usuarios/"+mFirebaseUser.getUid()+"/susreservas/");

        db_reserv_user.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Long> numsReservas = (List<Long>) dataSnapshot.getValue();

                if (numsReservas == null || numsReservas.size()==1) //O no hay o es la nula (reserva 0)
                    ;
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

                    mdatasetReservas.add(reserva);
                    PreparaReservaDestacada();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("DB", "No se ha podido acceder a la informacion de la reserva");
                }
            });

        }


        Log.i("INFO", mdatasetReservas.toString());
    }


    private void PreparaReservaDestacada(){
        /**Encargada de preparar la tarjeta de la reserva destacada*/
        Button btMasinfoDestacada = (Button) getView().findViewById(R.id.bt_proxReserva);

        TextView titulo = (TextView) getView().findViewById(R.id.titdestper);
        TextView desc = (TextView) getView().findViewById(R.id.descperperf);

        titulo.setText(mdatasetReservas.get(0).getTitulo());
        desc.setText(mdatasetReservas.get(0).getFecha().getTime().toString());

        btMasinfoDestacada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), VerInfoReservaActivity.class);

                intent.putExtra("reservaSeleccionada", mdatasetReservas.get(0));
                intent.putExtra("codigoReserva",0);
                intent.putExtra("datasetreservas", (Serializable) mdatasetReservas);
                startActivity(intent);
            }
        });

    }


}
