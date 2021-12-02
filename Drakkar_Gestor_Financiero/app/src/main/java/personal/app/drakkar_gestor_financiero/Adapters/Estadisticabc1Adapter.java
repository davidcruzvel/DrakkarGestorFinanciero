package personal.app.drakkar_gestor_financiero.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import java.util.ArrayList;
import java.util.List;
import personal.app.drakkar_gestor_financiero.Models.Transacciones;
import personal.app.drakkar_gestor_financiero.R;

public class Estadisticabc1Adapter extends RecyclerView.Adapter<Estadisticabc1Adapter.ViewHolder> {

    //adaptador del primer RecyclerView de la estadistica.
    List<Transacciones> lstTransacciones; //lista de transacciones.
    Context context; //contexto de la Activity o Fragment.
    List<BarEntry> barEntriesArrayList = new ArrayList<>(); //lista donde se almacenaran los datos del gráfico.
    BarData barData; //objeto del gráfico
    BarDataSet barDataSet; //objeto para setear los datos en el objeto del gráfico.

    public static class ViewHolder extends RecyclerView.ViewHolder { //inicialización del ViewHolder.
        BarChart bc1Est;
        Context context;
        public  ViewHolder(View view,Context context){
            super(view);
            bc1Est = view.findViewById(R.id.bc1Est);
            this.context=context;
        }
    }

    public Estadisticabc1Adapter(List<Transacciones> lstTransacciones, Context context) { //constructor.
        this.context = context;
        this.lstTransacciones = lstTransacciones;
    }

    @NonNull
    @Override
    public Estadisticabc1Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.estadisticabc1,parent,false);
        ViewHolder vh = new ViewHolder(v,context);
        return vh;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        double gastot = 0; //variable par almacenar el monto de los gastos.
        double ingtot = 0; //variable para almacenar el monto de los ingresos.
        //preparamos las configuraciones del gráfico.
        holder.bc1Est.getDescription().setEnabled(false);
        holder.bc1Est.getXAxis().setDrawGridLines(false);
        holder.bc1Est.getAxisLeft().setDrawGridLines(false);
        holder.bc1Est.getAxisRight().setDrawGridLines(false);
        holder.bc1Est.getAxisLeft().setDrawAxisLine(false);
        holder.bc1Est.getAxisRight().setDrawAxisLine(false);
        holder.bc1Est.getXAxis().setDrawAxisLine(false);
        holder.bc1Est.getLegend().setEnabled(false);
        holder.bc1Est.getAxisLeft().setDrawLabels(false);
        holder.bc1Est.getAxisRight().setDrawLabels(false);
        holder.bc1Est.getXAxis().setDrawLabels(false);
        holder.bc1Est.setTouchEnabled(false);
        if(lstTransacciones.size() != 1){ //si la lista retorna 2 objetos.
            if(lstTransacciones.get(0).getTipo().equals("1")){ //si el tipo de del primer objeto es 1 entonces es un gasto.
                gastot = gastot+(Double.parseDouble(lstTransacciones.get(0).getMonto())); //sumamos el monto a la variable gastot.
            }else{
                ingtot = ingtot+(Double.parseDouble(lstTransacciones.get(0).getMonto())); //sumamos el monto a la variable ingtot.
            }
            if(lstTransacciones.get(1).getTipo().equals("1")){ //si el tipo de del segundo objeto es 1 entonces es un gasto.
                gastot = gastot+(Double.parseDouble(lstTransacciones.get(1).getMonto())); //sumamos el monto a la variable gastot.
            }else{
                ingtot = ingtot+(Double.parseDouble(lstTransacciones.get(1).getMonto())); //sumamos el monto a la variable ingtot.
            }
        }else{ //si la lista retorna 1 objeto.
            if(lstTransacciones.get(0).getTipo().equals("1")){ //si el tipo del objeto es 1 entonces es un gasto.
                gastot = gastot+(Double.parseDouble(lstTransacciones.get(0).getMonto())); //sumamos el monto a la variable gastot.
            }else{ //si el tipo del objeto es 2 entonces es un ingreso.
                ingtot = ingtot+(Double.parseDouble(lstTransacciones.get(0).getMonto())); //sumamos el monto a la variable ingtot.
            }
        }
        barEntriesArrayList = new ArrayList<>(); //inicalizamos la lista donde se almacenaran los datos del gráfico.
        barEntriesArrayList.add(new BarEntry(1, 0)); //seteamos la primera en blanco para que ponga un espacio a la izquierda.
        barEntriesArrayList.add(new BarEntry(2,  (int)ingtot)); //seteamos el valor de los ingresos.
        barEntriesArrayList.add(new BarEntry(3, (int)gastot)); //seteamos el valor de los gastos.
        barEntriesArrayList.add(new BarEntry(4, 0)); //seteamos la primera en blanco para que ponga un espacio a la derecha.
        barDataSet = new BarDataSet(barEntriesArrayList, ""); //seteamos la lista en el DataSet del gráfico.
        barDataSet.setDrawValues(false); //ponemos como false el setDrawValues para que no aparezca el porcentaje arriba de las barras.
        int[] colors = new int[] {Color.WHITE, Color.parseColor("#4CAF50"), Color.parseColor("#F44336"), Color.WHITE,}; //declaramos una la lista de colores.
        barDataSet.setColors(colors); //seteamos la lista de colores en el DataSet.
        barData = new BarData(barDataSet); // seteamos el DataSet en el gráfico.
        holder.bc1Est.setData(barData); //seteamos el gráfico en el componente del gráfico del Fragment.
    }

    @Override
    public int getItemCount() { return this.lstTransacciones.size(); }

}