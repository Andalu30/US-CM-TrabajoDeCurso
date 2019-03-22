package us.cm.trabajodecurso;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar y NavigationDrawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        cambiaFragment("inicio");

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_1) {
            Toast.makeText(getApplicationContext(), "Opcion 1", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inicio) {
            cambiaFragment("inicio");
        } else if (id == R.id.nav_perfil) {
            cambiaFragment("perfil");
        } else if (id == R.id.nav_reservas) {
            cambiaFragment("reservas");
        } else if (id == R.id.nav_explorar) {
            cambiaFragment("explorar");
        } else if (id == R.id.nav_notificaciones) {
            cambiaFragment("notificaciones");
        } else if (id == R.id.nav_ajustes) {
            cambiaFragment("ajustes");
        } else if (id == R.id.nav_info) {
            cambiaFragment("info");
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void cambiaFragment(String pantalla) {
        Fragment fragment = null;

        if (pantalla == "inicio") {
            fragment = new Fragment_PantallaPrincipal();
        } else if (pantalla == "info") {
            fragment = new Fragment_informacion();
        } else if (pantalla == "reservas") {
            fragment = new Fragment_misReservas();
        } else {
            fragment = new Fragment_PantallaPrincipal();
        }

        FragmentManager fragMan = getSupportFragmentManager();
        FragmentTransaction ft = fragMan.beginTransaction();
        ft.replace(R.id.screenArea, fragment).commit();
    }

}