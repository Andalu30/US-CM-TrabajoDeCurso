package us.cm.trabajodecurso;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;

public class VerInfoReservaActivity extends AppCompatActivity {

    private Integer prox;


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_info_reserva);


        //Recogemos informacion del intent
        final Reserva reservaIntent = (Reserva) getIntent().getSerializableExtra("reservaSeleccionada");
        final Integer numeroReserva = (Integer) getIntent().getIntExtra("codigoReserva",0);
        final List<Reserva> datasetreservas = (List<Reserva>) getIntent().getSerializableExtra(
                "datasetreservas");

        if (reservaIntent == null){
            Log.e("Activity", "NO se ha recibido ninguna reserva mediante el intent");
        }


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Reserva: #"+numeroReserva+" "+reservaIntent.getTitulo());
        actionBar.setDisplayHomeAsUpEnabled(true); //Activa boton atras


        TextView titulo = (TextView) findViewById(R.id.txTituloReservaInfo);
        titulo.setText(reservaIntent.getTitulo());

        TextView desc = (TextView) findViewById(R.id.txDescripcionReservaInfo);
        desc.setText(reservaIntent.getDescripcion());

        TextView fecha = (TextView) findViewById(R.id.fechaReservaInfo);
        fecha.setText(reservaIntent.getFecha().getTime().toString());

        TextView hor = (TextView) findViewById(R.id.txhorarioReservaInfo);
        hor.setText(reservaIntent.getHorario());

        TextView ubi = (TextView) findViewById(R.id.txubicacionReservaInfo);
        ubi.setText(reservaIntent.getCentro());


        Button btgmaps = (Button) findViewById(R.id.botonGoogleMapsInfo);

        btgmaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "http://maps.google.com/maps?q=" + reservaIntent.getCentro();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        });


        Button reservar = (Button) findViewById(R.id.botonReservarInfo);
        reservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(VerInfoReservaActivity.this);
                alertDialogBuilder
                        .setTitle("Reservar "+reservaIntent.getDescripcion())
                        .setMessage("¿Estás seguro de que quieres reservar este evento?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                reservarReserva();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }

            private void reservarReserva() {


                DatabaseReference db_reserv_user =
                        FirebaseDatabase.getInstance().getReference("/reservas/");

                db_reserv_user.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Object> numsReservas = (List<Object>) dataSnapshot.getValue();
                        for (int i = 0; i < numsReservas.size(); i++) {
                            compruebaReservaNum(i);
                        }

                    }

                    private void compruebaReservaNum(final int i) {
                        DatabaseReference db_reserv_user =
                                FirebaseDatabase.getInstance().getReference("/reservas/"+i);

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


                                if (reserva.equals(reservaIntent)) {
                                    getProxReservaUsuario(i);
                                    //guardaReservaBaseDatos(i);
                                }

                            }

                            private void guardaReservaBaseDatos(int value, int pos){
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef =
                                        database.getReference("/usuarios/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/susreservas/"+pos);
                                myRef.setValue(i);

                                DatabaseReference myRef2 =
                                        database.getReference("/reservas/"+ value+
                                                "/disponibilidad/");
                                myRef2.setValue("false");

                            }




                            private Integer getProxReservaUsuario(final Integer value){
                                DatabaseReference db_reserv_user =
                                        FirebaseDatabase.getInstance().getReference("/usuarios/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+
                                                "/susreservas/");

                                db_reserv_user.addListenerForSingleValueEvent(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        List<Long> numsReservas = (List<Long>) dataSnapshot.getValue();
                                        Integer posicion = numsReservas.size();
                                        guardaReservaBaseDatos(value, posicion);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Log.e("DB", "No se ha podido acceder a las reservas del usuario");
                                    }
                                });

                                return prox;
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e("DB", "No se ha podido acceder a la informacion de la reserva");
                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("DB", "No se ha podido acceder a las reservas del usuario");
                    }
                });
            }
        });

    }
    // Boton atras de la Toolbar
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
