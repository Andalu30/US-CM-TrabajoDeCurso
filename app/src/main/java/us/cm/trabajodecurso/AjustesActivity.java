package us.cm.trabajodecurso;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

public class AjustesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Ajustes");
        actionBar.setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_ajustes);
    }

    //Boton Atras de la Toolbar
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
