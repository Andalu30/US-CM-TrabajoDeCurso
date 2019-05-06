package us.cm.trabajodecurso;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AjustesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Ajustes");
        actionBar.setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_ajustes);


        Switch swReservasDestacadas = (Switch) findViewById(R.id.switchReservas);
        Switch swNotificaciones = (Switch) findViewById(R.id.switchNotificaciones);


        if (MainActivity.getAjustesReservaDestacada()){
            swReservasDestacadas.setChecked(true);
        }
        else
            swReservasDestacadas.setChecked(false);
        if (MainActivity.getAjustesNotificaciones()){
            swNotificaciones.setChecked(true);
        }

        else
            swNotificaciones.setChecked(false);



        if  (FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivity(new Intent(this, LoginActivity.class));
        }



        swReservasDestacadas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    MainActivity.setAjustesreservasdestacadas(true);
                    setdestacadas(true);}
                else{
                    MainActivity.setAjustesreservasdestacadas(false);
                    setdestacadas(false);}
            }
        });


        swNotificaciones.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    MainActivity.setAjustesNotificaciones(true);
                    setNotifs(true);
                }
                else{
                    MainActivity.setAjustesNotificaciones(false);
                    setNotifs(false);
                }
            }
        });


    }


    public static void setdestacadas(Boolean b){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef =
                database.getReference("/usuarios/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/ajustes/reservasDestacadas/");
        myRef.setValue(b);
    }

    public static void setNotifs(Boolean b){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef =
                database.getReference("/usuarios/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/ajustes/notificaciones/");
        myRef.setValue(b);
    }

    //Boton Atras de la Toolbar
    public boolean onSupportNavigateUp(){
        finish();
        startActivity(new Intent(this, MainActivity.class));
        return true;
    }

    @Override
    public void onBackPressed() {
        onSupportNavigateUp();
    }
}
