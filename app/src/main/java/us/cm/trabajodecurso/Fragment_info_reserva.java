package us.cm.trabajodecurso;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Calendar;
import java.util.Locale;

public class Fragment_info_reserva extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pantalla_info_reserva,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Button calendario = (Button) getView().findViewById(R.id.botonCalendario);
        Button btgmaps = (Button) getView().findViewById(R.id.botonGoogleMaps);

        btgmaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "http://maps.google.com/maps?q=" + "ETSII, Reina Mercedes, 41012 Sevilla";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        });


        calendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Calendar beginTime = Calendar.getInstance();
                beginTime.set(2019, 5, 19, 7, 30);

                Calendar endTime = Calendar.getInstance();
                endTime.set(2019, 5, 19, 8, 30);

                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                                beginTime.getTimeInMillis())
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                                endTime.getTimeInMillis())
                        .putExtra(CalendarContract.Events.TITLE, "Title")
                        .putExtra(CalendarContract.Events.DESCRIPTION, "Description")
                        .putExtra(CalendarContract.Events.EVENT_LOCATION, "TestEvent")
                        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                        .putExtra(Intent.EXTRA_EMAIL, "test@test.test");

                startActivity(intent);
            }
        });

    }
}
