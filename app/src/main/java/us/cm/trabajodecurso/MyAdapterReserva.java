package us.cm.trabajodecurso;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class MyAdapterReserva extends RecyclerView.Adapter<MyAdapterReserva.MyViewHolder> {
    /**
     * Adaptador que se utiliza para poder llenar los recicler views con los objetos de tipo
     * Reserva
     */



    private List<Reserva> mDataset;
    private OnReservaListener mOnReservaListener;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapterReserva(List<Reserva> myDataset, OnReservaListener onReservaListener) {
        mDataset = myDataset;
        this.mOnReservaListener = onReservaListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapterReserva.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {

        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_layout, parent, false);





        MyViewHolder vh = new MyViewHolder(v,mOnReservaListener);
        return vh;
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


        holder.tituloItemList.setText(mDataset.get(position).getTitulo());
        holder.descripItemList.setText(mDataset.get(position).getDescripcion()+" \n"+mDataset.get(position).getFecha().getTime().toString());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public CardView cardItemList;
        public ImageView imagenItemList;
        public TextView tituloItemList;
        public TextView descripItemList;

        OnReservaListener onReservaListener;


        public MyViewHolder(View itemView, OnReservaListener onReservaListener) {
            super(itemView);
            cardItemList = itemView.findViewById(R.id.list_item_card);
            imagenItemList = itemView.findViewById(R.id.list_item_image);
            tituloItemList = itemView.findViewById(R.id.list_item_titulo);
            descripItemList = itemView.findViewById(R.id.list_item_descripcion);
            this.onReservaListener = onReservaListener;
            itemView.setOnClickListener(this);
        }

        //Importante para que se puedan tener en cuenta los clics en los reciclerViews
        @Override
        public void onClick(View v) {
            onReservaListener.onReservaClick(getAdapterPosition());
        }
    }

    //Importante para que se puedan tener en cuenta los clics en los reciclerViews
    public interface OnReservaListener{
        void onReservaClick(int position);
    }

}
