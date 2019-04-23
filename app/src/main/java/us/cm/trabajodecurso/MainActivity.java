package us.cm.trabajodecurso;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {


    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    public static final String ANONYMOUS = "anonymous";

    private String FirebaseUserNombre;
    private String FirebaseUserEmail;
    private String FirebaseFotoPerfil;
    public MenuItem menuCerrarSesion;

    private GoogleApiClient mGoogleApiClient;


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

        menuCerrarSesion = (MenuItem) findViewById(R.id.bt_cerrarSesion);

        cambiaFragment("inicio");


        //Firebase
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();



        View header = navigationView.getHeaderView(0);
        TextView name = (TextView) header.findViewById(R.id.nombreUsuario_header);
        TextView email = (TextView) header.findViewById(R.id.mailUsuario_header);
        ImageView fotoperfil = (ImageView) header.findViewById(R.id.fotoPerfil);
        Button headerLogin = (Button) header.findViewById(R.id.bt_nav_login);

        headerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getBaseContext(), LoginActivity.class));
            }
        });


        if (mFirebaseUser == null) {

            FirebaseUserNombre = ANONYMOUS;
            FirebaseUserEmail = ANONYMOUS;
            FirebaseFotoPerfil = null;
            headerLogin.setVisibility(View.VISIBLE);



//            // Not signed in, launch the Sign In activity
//            startActivity(new Intent(this, LoginActivity.class));
//            finish();
//            return;
        } else {
            headerLogin.setVisibility(View.GONE);
            FirebaseUserNombre = mFirebaseUser.getDisplayName();
            FirebaseUserEmail = mFirebaseUser.getEmail();
            if (mFirebaseUser.getPhotoUrl() != null) {
                FirebaseFotoPerfil = mFirebaseUser.getPhotoUrl().toString();
            }
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();


        //Cambiar datos header
        name.setText(FirebaseUserNombre);
        email.setText(FirebaseUserEmail);

        //Descargar imagen del usuario y colocarla
        GetBitmapFromURLAsync getBitmapFromURLAsync = new GetBitmapFromURLAsync();
        getBitmapFromURLAsync.execute(FirebaseFotoPerfil);
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


    public static class GetBitmapFromURLAsync extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            return getBitmapFromURL(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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

        switch (item.getItemId()) {
            case R.id.bt_cerrarSesion:
                mFirebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                mFirebaseUser = null;
                FirebaseUserNombre = ANONYMOUS;
                FirebaseFotoPerfil = null;
                Toast.makeText(this,"Se ha cerrado la sesión", Toast.LENGTH_LONG);
                finish();
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
            notificacionDePrueba();
            //cambiaFragment("notificaciones");
        } else if (id == R.id.nav_info) {
            startActivity(new Intent(this, InfoActivity.class));
        } else if (id == R.id.nav_ajustes) {
            startActivity(new Intent(this, AjustesActivity.class));
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
        } else if (pantalla == "explorar") {
            fragment = new Fragment_Explorar();
        }else if (pantalla == "perfil") {
            fragment = new Fragment_Perfil();
        } else {
            fragment = new Fragment_PantallaPrincipal();
        }

        FragmentManager fragMan = getSupportFragmentManager();
        FragmentTransaction ft = fragMan.beginTransaction();
        ft.replace(R.id.screenArea, fragment).addToBackStack("back").commit();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("Fallo conexion", "onConnectionFailed:" + connectionResult);
    }

    public void setToolBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }



    private void notificacionDePrueba() {

        String id_canal_notificaciones_solo_para_o = "esta llegua no es mi vieja llegua gris";

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String channelId = "nadim_notifs";
            id_canal_notificaciones_solo_para_o = channelId;

            CharSequence channelName = "Canal de notificaciones de NADIM reservas";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = null;
                notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(notificationChannel);
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, id_canal_notificaciones_solo_para_o)
                .setSmallIcon(R.drawable.ic_notifications)
                .setAutoCancel(true)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentTitle("Notificacion de prueba")
                .setContentText("Este es el contenido de la notificación.")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        notificationManager.notify(0, builder.build());


    }

}