package us.cm.trabajodecurso;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Fragment_misReservas extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pantalla_misreservas,null);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Switch swSinReservas = getView().findViewById(R.id.switchNoReserva);
        Switch swNoLogin = getView().findViewById(R.id.switchNoLogin);
        Switch swNoProx = getView().findViewById(R.id.switchProxima);

        final ScrollView svReservas = getView().findViewById(R.id.scrollViewReservas);

        final CardView cardProx = getView().findViewById(R.id.cardViewProxima);

        final Button btNologin = getView().findViewById(R.id.buttonNoLogin);
        final Button btNoReservas = getView().findViewById(R.id.buttonNingunaReserva);

        final TextView txNoLogin = getView().findViewById(R.id.textoNoLogin);
        final TextView txNoReserva = getView().findViewById(R.id.textoNingunaReserva);
        final TextView txProx = getView().findViewById(R.id.proxres);

        swNoProx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txProx.setVisibility(View.INVISIBLE);
                    cardProx.setVisibility(View.GONE);
                } else {
                    txProx.setVisibility(View.VISIBLE);
                    cardProx.setVisibility(View.VISIBLE);
                }
            }
        });

        swSinReservas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                    svReservas.setVisibility(View.INVISIBLE);
                    btNoReservas.setVisibility(View.VISIBLE);
                    txNoReserva.setVisibility(View.VISIBLE);

                    txNoLogin.setVisibility(View.INVISIBLE);
                    btNologin.setVisibility(View.INVISIBLE);

                }else{
                    svReservas.setVisibility(View.VISIBLE);
                    btNoReservas.setVisibility(View.VISIBLE);
                    txNoReserva.setVisibility(View.VISIBLE);

                    txNoLogin.setVisibility(View.VISIBLE);
                    btNologin.setVisibility(View.VISIBLE);

                }
            }
        });

        swNoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                    svReservas.setVisibility(View.INVISIBLE);
                    btNoReservas.setVisibility(View.INVISIBLE);
                    txNoReserva.setVisibility(View.INVISIBLE);

                    txNoLogin.setVisibility(View.VISIBLE);
                    btNologin.setVisibility(View.VISIBLE);

                }else{
                    svReservas.setVisibility(View.VISIBLE);
                }
            }
        });

    }
}
