package us.cm.trabajodecurso;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputContentInfo;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Fragment_PantallaPrincipal extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pantallaprincipal,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        final Switch swNoprox = (Switch) getView().findViewById(R.id.swNoProx);
        final TextView proxText = (TextView) getView().findViewById(R.id.txProximoEvento);
        final CardView cardProx = (CardView) getView().findViewById(R.id.cardProximoEvento);

        swNoprox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    proxText.setVisibility(View.INVISIBLE);
                    cardProx.setVisibility(View.GONE);
                }else{
                    proxText.setVisibility(View.VISIBLE);
                    cardProx.setVisibility(View.VISIBLE);
                }
            }
        });


    }
}
