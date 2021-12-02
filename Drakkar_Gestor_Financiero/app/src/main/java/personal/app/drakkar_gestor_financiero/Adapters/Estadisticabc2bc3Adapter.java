package personal.app.drakkar_gestor_financiero.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import personal.app.drakkar_gestor_financiero.Models.Transacciones;
import personal.app.drakkar_gestor_financiero.R;

public class
 Estadisticabc2bc3Adapter extends RecyclerView.Adapter<Estadisticabc2bc3Adapter.ViewHolder> {

    //adaptador del segundo y tercer RecyclerView de la estadistica.
    List<Transacciones> lstTransacciones; //lista de transacciones.
    Context context; //contexto de la Activity o Fragment.
    int tipo = 0; //variable para saber si son gastos o ingresos.
    double gastot = 0; //variable para almacenar el monto total de gastos.
    double ingtot = 0; //variable para almacenar el monto total de ingresos.

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        TextView tvCategoriabc2bc3, tvPorcentajebc2bc3, tvMontobc2bc3;
        ConstraintLayout pnlSeparador1bc2bc3;
        Context context;
        public  ViewHolder(View view,Context context){ //inicialización del ViewHolder.
            super(view);
            tvCategoriabc2bc3 = view.findViewById(R.id.tvCategoriabc2bc3);
            tvPorcentajebc2bc3 = view.findViewById(R.id.tvPorcentajebc2bc3);
            tvMontobc2bc3 = view.findViewById(R.id.tvMontobc2bc3);
            pnlSeparador1bc2bc3 = view.findViewById(R.id.pnlSeparador1bc2bc3);
            this.context=context;
        }
    }

    public Estadisticabc2bc3Adapter(List<Transacciones> lstTransacciones, Context context, double gastot, double ingtot, int tipo) { //constructor.
        this.context = context;
        this.lstTransacciones = lstTransacciones;
        this.gastot = gastot;
        this.ingtot = ingtot;
        this.tipo = tipo;
    }

    @NonNull
    @Override
    public Estadisticabc2bc3Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.estadisticabc2bc3,parent,false);
        ViewHolder vh = new ViewHolder(v,context);
        return vh;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transacciones obj = lstTransacciones.get(position);
        if(lstTransacciones.size()-1 == position){ //volver invisible el panel gris de abajo del último para que no aparezca.
            holder.pnlSeparador1bc2bc3.setVisibility(View.INVISIBLE);
        }
        holder.tvCategoriabc2bc3.setText(obj.getCategoria());
        holder.tvMontobc2bc3.setText(String.format("%.2f", Double.parseDouble(obj.getMonto())));
        if(tipo == 1){ //si el tipo es 1 entonces son gastos.
            holder.tvMontobc2bc3.setTextColor(Color.parseColor("#F44336")); //damos color rojo al TextView del monto.
            double porcentaje = (Double.parseDouble(obj.getMonto())*100)/(gastot); //calculamos el porcentaje de ese monto.
            holder.tvPorcentajebc2bc3.setText(String.format("%.2f", porcentaje)+"%"); //seteamos el porcentaje.
        }else{ //si el tipo es 2 entonces son ingresos.
            holder.tvMontobc2bc3.setTextColor(Color.parseColor("#4CAF50")); //damos color verde al TextView del monto.
            double porcentaje = (Double.parseDouble(obj.getMonto())*100)/(ingtot); //calculamos el porcentaje de ese monto.
            holder.tvPorcentajebc2bc3.setText(String.format("%.2f", porcentaje)+"%");//seteamos el porcentaje.
        }
    }

    @Override
    public int getItemCount() { return this.lstTransacciones.size(); }

}
