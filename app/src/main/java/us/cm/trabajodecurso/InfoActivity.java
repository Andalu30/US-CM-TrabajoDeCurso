package us.cm.trabajodecurso;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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

    public void easteregg(View view) {
        Snackbar.make(view, "It is pitch black. You're likely to be eaten by a grue", Snackbar.LENGTH_LONG).show();
        AlertDialog.Builder alertadd = new AlertDialog.Builder(this);
        LayoutInflater factory = LayoutInflater.from(this);
        final View test = factory.inflate(R.layout.test_easter_egg, null);
        alertadd.setView(test);
        alertadd.setNeutralButton("NADIM!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {

            }
        });

        alertadd.show();
    }
}
