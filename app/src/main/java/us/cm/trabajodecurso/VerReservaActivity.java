package us.cm.trabajodecurso;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;

public class VerReservaActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_ver_reserva);


        //Recogemos informacion del intent
        final Reserva reservaIntent = (Reserva) getIntent().getSerializableExtra("reservaSeleccionada");


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Reserva: "+reservaIntent.getTitulo());
        actionBar.setDisplayHomeAsUpEnabled(true); //Activa boton atras


        TextView titulo = (TextView) findViewById(R.id.txTituloReserva);
        titulo.setText(reservaIntent.getTitulo());

        TextView desc = (TextView) findViewById(R.id.txDescripcionReserva);
        desc.setText(reservaIntent.getDescripcion());

        TextView hor = (TextView) findViewById(R.id.txhorarioReserva);
        hor.setText(reservaIntent.getHorario());

        TextView ubi = (TextView) findViewById(R.id.txubicacionReserva);
        ubi.setText(reservaIntent.getUbicacion()); //TODO: Las reservas no tienen ubicacion, se debe de sacar del centro





        Button calendario = (Button) findViewById(R.id.botonCalendario);
        Button btgmaps = (Button) findViewById(R.id.botonGoogleMaps);

        btgmaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "http://maps.google.com/maps?q=" + reservaIntent.getUbicacion();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        });


        calendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("FECHA", reservaIntent.getFecha().toString());
                String[] horario = reservaIntent.getHorario().split(":");


                Calendar beginTime = reservaIntent.getFecha();
                //beginTime.set(reservaIntent.getFecha().getYear(), reservaIntent.getFecha().getMonth(), reservaIntent.getFecha().getDay(), new Integer(horario[0].trim()), new Integer(horario[1].trim()));

                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                                beginTime.getTimeInMillis())
                        .putExtra(CalendarContract.Events.TITLE, reservaIntent.getTitulo())
                        .putExtra(CalendarContract.Events.DESCRIPTION, reservaIntent.getDescripcion())
                        .putExtra(CalendarContract.Events.EVENT_LOCATION, "TestEvent")
                        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
                startActivity(intent);
            }
        });

    }
    // Boton atras de la Toolbar
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
