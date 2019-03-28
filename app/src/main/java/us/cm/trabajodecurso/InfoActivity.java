package us.cm.trabajodecurso;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Información");
        actionBar.setDisplayHomeAsUpEnabled(true); //Activa boton atras

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_info);
    }

    // Boton atras de la Toolbar
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
