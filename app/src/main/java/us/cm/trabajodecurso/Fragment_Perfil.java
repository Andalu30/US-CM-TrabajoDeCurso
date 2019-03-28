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

import java.util.concurrent.ExecutionException;

public class Fragment_Perfil extends Fragment {

    private String FirebaseUserNombre;
    private String FirebaseUserEmail;
    private String mPhotoUrl;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    public static final String ANONYMOUS = "anonymous";



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setToolBarTitle("Mi perfil");
        return inflater.inflate(R.layout.pantalla_perfil,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        Switch swNoprox = (Switch) getView().findViewById(R.id.swNoProx);
        Switch swNoOtras = getView().findViewById(R.id.swNoOtras);

        final TextView proxText = (TextView) getView().findViewById(R.id.txProximoEvento);
        final CardView cardProx = (CardView) getView().findViewById(R.id.cardProximoEvento);

        final TextView otrasText = (TextView) getView().findViewById(R.id.textOtras);
        final CardView otrascard = (CardView) getView().findViewById(R.id.cardOtras);


        Button btExplorar = (Button) getView().findViewById(R.id.bt_explorar);
        Button btMisReservas = (Button) getView().findViewById(R.id.bt_verReservas);
        Button btProxReserva = (Button) getView().findViewById(R.id.bt_proxReserva);

        // DEBUG SWITCH
        swNoOtras.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    otrasText.setVisibility(View.GONE);
                    otrascard.setVisibility(View.GONE);
                }else{
                    otrasText.setVisibility(View.VISIBLE);
                    otrascard.setVisibility(View.VISIBLE);
                }
            }
        });
        swNoprox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    proxText.setVisibility(View.GONE);
                    cardProx.setVisibility(View.GONE);
                }else{
                    proxText.setVisibility(View.VISIBLE);
                    cardProx.setVisibility(View.VISIBLE);
                }
            }
        });
        // DEBUG SWITCH----


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
                Fragment fragment = new Fragment_ver_reserva();
                FragmentManager fragMan = getFragmentManager();
                FragmentTransaction ft = fragMan.beginTransaction();
                ft.replace(R.id.screenArea, fragment).addToBackStack("back").commit();
            }
        });


        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this.getActivity(), LoginActivity.class));
            this.getActivity().recreate();
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

    }



}
