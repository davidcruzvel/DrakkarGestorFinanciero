package personal.app.drakkar_gestor_financiero;

import static android.content.ContentValues.TAG;
import static personal.app.drakkar_gestor_financiero.PrincipalActivity.sharedPreferences;
import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import personal.app.drakkar_gestor_financiero.API.API;
import personal.app.drakkar_gestor_financiero.Adapters.TransaccionesAdapter;
import personal.app.drakkar_gestor_financiero.Models.Transacciones;
import personal.app.drakkar_gestor_financiero.Services.TransaccionesService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransaccionesFragment extends Fragment {

    SwipeRefreshLayout swipeTra;
    TextView tvFechaDesdeTra, tvFechaHastaTra, tvNoDatosTra, tvIngNumTra, tvGasNumTra, tvSalNumTra; //componentes gráficos.
    RecyclerView rvTranRes; //componente gráfico.
    TransaccionesService service; //objeto service para acceder a los métodos de manejo de datos de transacciones.
    List<Transacciones> lstTransacciones = new ArrayList<>(); //lista de objetos de tipo Transacciones.
    LinearLayoutManager linearLayoutManager; //objeto de tipo LinearLayoutManager
    Calendar c; //objeto Calendar usado en el método seleccionarFecha().
    DatePickerDialog dpd; //objeto DatePickerDialog, es el que se abre para seleccionar la fecha.
    DatePickerDialog.OnDateSetListener dpdl; //listener del objeto DatePickerDialog.

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transacciones, container, false);
        swipeTra = view.findViewById(R.id.swipeTra);
        tvFechaDesdeTra = view.findViewById(R.id.tvFechaDesdeTra);
        tvFechaHastaTra = view.findViewById(R.id.tvFechaHastaTra);
        rvTranRes = view.findViewById(R.id.rvTranRes);
        tvNoDatosTra = view.findViewById(R.id.tvNoDatosTra);
        tvIngNumTra = view.findViewById(R.id.tvIngNumTra);
        tvGasNumTra = view.findViewById(R.id.tvGasNumTra);
        tvSalNumTra = view.findViewById(R.id.tvSalNumTra);
        setFechas(); //seteamos las fechas.
        lstTransacciones(); //listamos las transacciones.
        swipeTra.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lstTransacciones(); //listamos las transacciones.
                swipeTra.setRefreshing(false);
            }
        });
        tvFechaDesdeTra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarFecha(1);
            }
        });
        tvFechaHastaTra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarFecha(2);
            }
        });
        return view;
    }

    @Override
    public void onResume(){ //método que se ejecuta cuando se retoma este Fragment.
        super.onResume();
        try {
            lstTransacciones(); //listamos las transacciones.
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void setDatos(){ ///método para setear los datos de ingreso_total, gasto_total y saldo_general.
        try {
            tvIngNumTra.setText(sharedPreferences.getString("ingreso_total", "")); //seteamos el dato de ingreso_total que esta almacenado en el SharedPreferences.
            tvGasNumTra.setText(sharedPreferences.getString("gasto_total", "")); //seteamos el dato de gasto_total que esta almacenado en el SharedPreferences.
            tvSalNumTra.setText(sharedPreferences.getString("saldo_general", "")); //seteamos el dato de saldo_general que esta almacenado en el SharedPreferences.
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void setFechas(){ //método para setear las fechas.
        try {
            c = Calendar.getInstance(); //obtenemos la fecha actual.
            int anio = c.get(Calendar.YEAR); //seteamos el año.
            int mes = c.get(Calendar.MONTH); //seteamos el mes.
            int dia = c.get(Calendar.DAY_OF_MONTH); //seteamos el día.
            tvFechaDesdeTra.setText(anio+"-"+(mes+1)+"-"+1); //seteamos la fecha del TextView fechaDesde.
            tvFechaHastaTra.setText(anio+"-"+(mes+1)+"-"+dia); //seteamos la fecha del TextView fechaHasta.
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void seleccionarFecha(int flag){ //método para desplegar el DatePickerDialog para seleccionar las fechas.
        try {
            c = Calendar.getInstance(); //obtenemos la fecha actual.
            int anio = c.get(Calendar.YEAR); //seteamos el año.
            int mes = c.get(Calendar.MONTH); //seteamos el mes.
            int dia = c.get(Calendar.DAY_OF_MONTH); //seteamos el día.
            dpdl = new DatePickerDialog.OnDateSetListener() { //listener del DatePickerDialog.
                @Override
                public void onDateSet(DatePicker datePicker, int anio, int mes, int dia) {
                    Log.d(TAG, "onDataSet: yyyy-mm-dd: "+anio+"-"+(mes+1)+"-"+dia);  //seteamos la fecha en el DatePickerDialog.
                    String fechaString = anio+"-"+(mes+1)+"-"+dia; //creamos una variable para almacenar la fecha que seleccionamos.
                    if(flag == 1){ //si flag es 1 entonces seteamos la fecha del TextView fechaDesde.
                        tvFechaDesdeTra.setText(fechaString);
                    }else{ //si flag es 2 entonces seteamos la fecha del TextView fechaHasta.
                        tvFechaHastaTra.setText(fechaString);
                    }
                    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd"); //creamos un objeto de tipo SimpleDateFormat.
                    Date fechaDesde = null; //creamos una variable de tipo Date donde almacenaremos la fecha del TextView fechaDesde.
                    Date fechaHasta = null; //creamos una variable de tipo Date donde almacenaremos la fecha del TextView fechaHasta.
                    try {
                        fechaDesde = f.parse(tvFechaDesdeTra.getText().toString()); //seteamos la fecha del TextView fechaDesde.
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        fechaHasta = f.parse(tvFechaHastaTra.getText().toString()); //seteamos la fecha del TextView fechaHasta.
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(fechaDesde.after(fechaHasta)){ //evaluamos si la fechaDesde es mayor a la fechaHasta, es decir que las fechas no tienen sentido.
                        //le seteamos las fecha que seleccionamos a las dos fechas.
                        tvFechaDesdeTra.setText(fechaString);
                        tvFechaHastaTra.setText(fechaString);
                    }
                    lstTransacciones(); //listamos las transacciones.
                }
            };
            dpd = new DatePickerDialog(getContext(), dpdl, anio, mes, dia);
            dpd.show();
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void lstTransacciones(){ //método para listar las transacciones en el RecyclerView.
        try {
            service = API.getTransaccionesService();
            Call<List<Transacciones>> call = service.listar(Integer.parseInt(sharedPreferences.getString("id", "")), tvFechaDesdeTra.getText().toString(), tvFechaHastaTra.getText().toString());
            call.enqueue(new Callback<List<Transacciones>>() {
                @Override
                public void onResponse(Call<List<Transacciones>> call, Response<List<Transacciones>> response) {
                    if (response.isSuccessful()) { //si se ejecuta exitosamente.
                        lstTransacciones = response.body(); //seteamos el response.body() que nos devolvera una lista de categorías en la variable lstTransacciones.
                        if(lstTransacciones.isEmpty()){ //si la lista está vacía.
                            rvTranRes.setVisibility(View.GONE); //ponemos como invisble el RecylerView de las transacciones.
                            tvNoDatosTra.setVisibility(View.VISIBLE); //ponemos como visible el TextView que nos indica que no hay transacciones registradas.
                        }else{ //si la lista no está vacía.
                            rvTranRes.setVisibility(View.VISIBLE); //ponemos como visble el RecylerView de las transacciones.
                            tvNoDatosTra.setVisibility(View.INVISIBLE); //ponemos como invisble el TextView que nos indica que no hay transacciones registradas.
                            //preparamos el RecylerView seteandole el layout.
                            linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                            rvTranRes.setLayoutManager(linearLayoutManager);
                            //seteamos el adaptador del RecylerView y a este le enviamos la lista de transacciones y el contexto del Fragment.
                            rvTranRes.setAdapter(new TransaccionesAdapter(lstTransacciones, getContext()));
                            setDatos(); //setear los datos de ingreso_total, gasto_total y saldo_general.
                        }
                    }
                }
                @Override
                public void onFailure(Call<List<Transacciones>> call, Throwable t) { //si hay un error a la hora de comunicarse con la API
                    rvTranRes.setVisibility(View.GONE); //ponemos como invisble el RecylerView de las transacciones.
                    tvNoDatosTra.setVisibility(View.VISIBLE); //ponemos como visible el TextView.
                    tvNoDatosTra.setText(R.string.errorconexion); //le seteamos el texto de "Error de conexión" al TexView.
                    tvIngNumTra.setText("0.00");
                    tvGasNumTra.setText("0.00");
                    tvSalNumTra.setText("0.00");
                }
            });
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

}