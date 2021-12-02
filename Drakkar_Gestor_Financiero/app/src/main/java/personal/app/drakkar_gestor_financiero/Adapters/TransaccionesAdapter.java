package personal.app.drakkar_gestor_financiero.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import personal.app.drakkar_gestor_financiero.Models.Transacciones;
import personal.app.drakkar_gestor_financiero.R;
import personal.app.drakkar_gestor_financiero.TransaccionesModEliActivity;

public class TransaccionesAdapter extends RecyclerView.Adapter<TransaccionesAdapter.ViewHolder> {

    List<Transacciones> lstTransacciones;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        TextView tvIdTL, tvIdCategoriaTL, tvTipoTL, tvDescripcionTL, tvMontoTL, tvCategoriaTL, tvFechaTL;
        ImageView ivCategoriaTL;
        ConstraintLayout clListaTransacciones, pnlSeparador1TL;
        Context context;
        public  ViewHolder(View view,Context context){
            super(view);
            tvIdTL = view.findViewById(R.id.tvIdTL);
            tvIdCategoriaTL = view.findViewById(R.id.tvIdCategoriaTL);
            tvTipoTL = view.findViewById(R.id.tvTipoTL);
            tvDescripcionTL = view.findViewById(R.id.tvDescripcionTL);
            tvMontoTL = view.findViewById(R.id.tvMontoTL);
            tvCategoriaTL = view.findViewById(R.id.tvCategoriaTL);
            tvFechaTL = view.findViewById(R.id.tvFechaTL);
            ivCategoriaTL = view.findViewById(R.id.ivCategoriaTL);
            clListaTransacciones = view.findViewById(R.id.clListaTransacciones);
            pnlSeparador1TL = view.findViewById(R.id.pnlSeparador1TL);
            this.context=context;
        }
    }

    public TransaccionesAdapter(List<Transacciones> lstTransacciones, Context context) {
        this.context = context;
        this.lstTransacciones = lstTransacciones;
    }

    @NonNull
    @Override
    public TransaccionesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transacciones_list,parent,false);
        ViewHolder vh = new ViewHolder(v,context);
        return vh;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transacciones obj = lstTransacciones.get(position);
        if(lstTransacciones.size()-1 == position){ //volver invisible el panel gris de abajo del último para que no aparezca.
            holder.pnlSeparador1TL.setVisibility(View.INVISIBLE);
        }
        holder.tvIdTL.setText(""+obj.getId());
        holder.tvIdCategoriaTL.setText(""+obj.getIdcategoria());
        holder.tvTipoTL.setText(obj.getTipo());
        holder.tvDescripcionTL.setText(obj.getDescripcion());
        holder.tvMontoTL.setText(obj.getMonto());
        holder.tvCategoriaTL.setText(obj.getCategoria());
        holder.tvFechaTL.setText(obj.getFecha());
        if(obj.getTipo().equals("1")){ //si el tipo es 1 que setee la imagen down.
            holder.ivCategoriaTL.setImageResource(R.drawable.down);
            holder.tvMontoTL.setTextColor(Color.parseColor("#F44336"));
        }else{ //si el tipo es 2 que setee la imagen up.
            holder.ivCategoriaTL.setImageResource(R.drawable.up);
            holder.tvMontoTL.setTextColor(Color.parseColor("#4CAF50"));
        }
        holder.clListaTransacciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //entrar a editar el elemento.
                Intent intent = new Intent(context, TransaccionesModEliActivity.class);
                intent.putExtra("id",""+obj.getId());
                intent.putExtra("idcategoria", ""+obj.getIdcategoria());
                intent.putExtra("tipo", obj.getTipo());
                intent.putExtra("descripcion", obj.getDescripcion());
                intent.putExtra("monto", obj.getMonto());
                intent.putExtra("categoria", obj.getCategoria());
                intent.putExtra("fecha", obj.getFecha());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() { return this.lstTransacciones.size(); }

}