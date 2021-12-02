package personal.app.drakkar_gestor_financiero;

import static android.content.ContentValues.TAG;
import static personal.app.drakkar_gestor_financiero.PrincipalActivity.sharedPreferences;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
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
import personal.app.drakkar_gestor_financiero.Adapters.Estadisticabc1Adapter;
import personal.app.drakkar_gestor_financiero.Adapters.Estadisticabc2bc3Adapter;
import personal.app.drakkar_gestor_financiero.Models.Transacciones;
import personal.app.drakkar_gestor_financiero.Services.TransaccionesService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EstadisticaFragment extends Fragment {

    SwipeRefreshLayout swipeEst;
    TextView tvFechaDesdeEst, tvFechaHastaEst, tvIngNumEst, tvGasNumEst, tvSalNumEst, tvNoDatosEst1, tvNoDatosEst2, tvNoDatosEst3; //componentes gráficos.
    RecyclerView rvEstbc1, rvEstbc2, rvEstbc3; //componentes gráficos.
    TransaccionesService service; //objeto service para acceder a los métodos de manejo de datos de transacciones.
    List<Transacciones> lstTransacciones = new ArrayList<>(); //lista de objetos de tipo Transacciones.
    LinearLayoutManager linearLayoutManager; //objeto de tipo LinearLayoutManager
    Calendar c; //objeto Calendar usado en el método seleccionarFecha().
    DatePickerDialog dpd; //objeto DatePickerDialog, es el que se abre para seleccionar la fecha.
    DatePickerDialog.OnDateSetListener dpdl; //listener del objeto DatePickerDialog.
    double gastot = 0; //variable para almacenar los gastos totales.
    double ingtot = 0; //variable para almacenar los ingresos totales.
    double salgen = 0; //variable para almacenar los saldo general.

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_estadistica, container, false);
        swipeEst = view.findViewById(R.id.swipeEst);
        tvFechaDesdeEst = view.findViewById(R.id.tvFechaDesdeEst);
        tvFechaHastaEst = view.findViewById(R.id.tvFechaHastaEst);
        tvIngNumEst = view.findViewById(R.id.tvIngNumEst);
        tvGasNumEst = view.findViewById(R.id.tvGasNumEst);
        tvSalNumEst = view.findViewById(R.id.tvSalNumEst);
        rvEstbc1 = view.findViewById(R.id.rvEstbc1);
        rvEstbc2 = view.findViewById(R.id.rvEstbc2);
        rvEstbc3 = view.findViewById(R.id.rvEstbc3);
        tvNoDatosEst1 = view.findViewById(R.id.tvNoDatosEst1);
        tvNoDatosEst2 = view.findViewById(R.id.tvNoDatosEst2);
        tvNoDatosEst3 = view.findViewById(R.id.tvNoDatosEst3);
        setFechas(); //seteamos las fechas para que cargue la estadística del mes.
        cargarBc1(); //cargamos la estadistica del primer cuadro que a su vez manda a llamar a los otros dos dentro de este.
        swipeEst.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarBc1(); //cargamos la estadistica del primer cuadro que a su vez manda a llamar a los otros dos dentro de este.
                swipeEst.setRefreshing(false);
            }
        });
        tvFechaDesdeEst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //llamamos al método seleccionarFecha y le enviamos 2 para que abra el DatePickerDialog y setee la fecha del TextView fechaDesde.
                seleccionarFecha(1);
            }
        });
        tvFechaHastaEst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //llamamos al método seleccionarFecha y le enviamos 2 para que abra el DatePickerDialog y setee la fecha del TextView fechaHasta.
                seleccionarFecha(2);
            }
        });
        return view;
    }

    public void setFechas(){ //método para setear las fechas.
        try {
            c = Calendar.getInstance(); //obtenemos la fecha actual.
            int anio = c.get(Calendar.YEAR); //seteamos el año.
            int mes = c.get(Calendar.MONTH); //seteamos el mes.
            int dia = c.get(Calendar.DAY_OF_MONTH); //seteamos el día.
            tvFechaDesdeEst.setText(anio+"-"+(mes+1)+"-"+1); //seteamos la fecha del TextView fechaDesde.
            tvFechaHastaEst.setText(anio+"-"+(mes+1)+"-"+dia); //seteamos la fecha del TextView fechaHasta.
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
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onDateSet(DatePicker datePicker, int anio, int mes, int dia) {
                    Log.d(TAG, "onDataSet: yyyy-mm-dd: "+anio+"-"+(mes+1)+"-"+dia);  //seteamos la fecha en el DatePickerDialog.
                    String fechaString = anio+"-"+(mes+1)+"-"+dia; //creamos una variable para almacenar la fecha que seleccionamos.
                    if(flag == 1){ //si flag es 1 entonces seteamos la fecha del TextView fechaDesde.
                        tvFechaDesdeEst.setText(fechaString);
                    }else{ //si flag es 2 entonces seteamos la fecha del TextView fechaHasta.
                        tvFechaHastaEst.setText(fechaString);
                    }
                    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd"); //creamos un objeto de tipo SimpleDateFormat.
                    Date fechaDesde = null; //creamos una variable de tipo Date donde almacenaremos la fecha del TextView fechaDesde.
                    Date fechaHasta = null; //creamos una variable de tipo Date donde almacenaremos la fecha del TextView fechaHasta.
                    try {
                        fechaDesde = f.parse(tvFechaDesdeEst.getText().toString()); //seteamos la fecha del TextView fechaDesde.
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        fechaHasta = f.parse(tvFechaHastaEst.getText().toString()); //seteamos la fecha del TextView fechaHasta.
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(fechaDesde.after(fechaHasta)){ //evaluamos si la fechaDesde es mayor a la fechaHasta, es decir que las fechas no tienen sentido.
                        //le seteamos las fecha que seleccionamos a las dos fechas.
                        tvFechaDesdeEst.setText(fechaString);
                        tvFechaHastaEst.setText(fechaString);
                    }
                    cargarBc1(); //llamamos el método cargarBc1 para cargar la estadistica del primer cuadro que a su vez manda a llamar a los otros dos dentro de este.
                }
            };
            dpd = new DatePickerDialog(getContext(), dpdl, anio, mes, dia); //inicializamos el DatePickerDialog.
            dpd.show(); //desplegamos el DatePickerDialog.
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void cargarBc1(){ //método para cargar le estadística del primer cuadro.
        try {
            service = API.getTransaccionesService();
            Call<List<Transacciones>> call = service.estadisticaBc1(Integer.parseInt(sharedPreferences.getString("id", "")), tvFechaDesdeEst.getText().toString(), tvFechaHastaEst.getText().toString());
            call.enqueue(new Callback<List<Transacciones>>() {
                @Override
                public void onResponse(Call<List<Transacciones>> call, Response<List<Transacciones>> response) {
                    if (response.isSuccessful()) { //si se ejecuta exitosamente.
                        lstTransacciones = response.body(); //seteamos el response.body() que nos devolvera una lista de transacciones en la variable lstTransacciones.
                        if(lstTransacciones.isEmpty()){ //si la lista está vacía.
                            gastot = 0; //seteamos a 0 la variable gastot.
                            ingtot = 0; //seteamos a 0 la variable ingtot.
                            salgen = 0; //seteamos a 0 la variable salgen.
                            tvIngNumEst.setText(String.format("%.2f", ingtot)); //seteamos en el TextView el valor de la variable gastot.
                            tvGasNumEst.setText(String.format("%.2f", gastot)); //seteamos en el TextView el valor de la variable gastot.
                            tvSalNumEst.setText(String.format("%.2f", salgen)); //seteamos en el TextView el valor de la variable gastot.
                            rvEstbc1.setVisibility(View.INVISIBLE); //ponemos como invisble el RecylerView del primer cuadro.
                            tvNoDatosEst1.setVisibility(View.VISIBLE); //ponemos como visible el TextView que nos indica que no hay transacciones registradas.
                            rvEstbc2.setVisibility(View.GONE); //ponemos como invisble el RecylerView del segundo cuadro.
                            tvNoDatosEst2.setVisibility(View.VISIBLE); //ponemos como visible el TextView que nos indica que no hay transacciones registradas.
                            rvEstbc3.setVisibility(View.GONE); //ponemos como invisble el RecylerView del tercer cuadro.
                            tvNoDatosEst3.setVisibility(View.VISIBLE); //ponemos como visible el TextView que nos indica que no hay transacciones registradas.
                        }else{
                            rvEstbc1.setVisibility(View.VISIBLE); //ponemos como visble el RecylerView del primer cuadro.
                            tvNoDatosEst1.setVisibility(View.INVISIBLE);  //ponemos como invisible el TextView que nos indica que no hay transacciones registradas.
                            gastot = 0; //seteamos a 0 la variable gastot.
                            ingtot = 0; //seteamos a 0 la variable ingtot.
                            salgen = 0; //seteamos a 0 la variable salgen.
                            if(lstTransacciones.size() != 1){ //si la lista retorna 2 objetos.
                                if(lstTransacciones.get(0).getTipo().equals("1")){ //si el tipo de del primer objeto es 1 entonces es un gasto.
                                    gastot = gastot+(Double.parseDouble(lstTransacciones.get(0).getMonto())); //sumamos el monto a la variable gastot.
                                    salgen = salgen-(Double.parseDouble(lstTransacciones.get(0).getMonto())); //restamos el monto a la variable salgen.
                                }else{ //si el tipo de del primer objeto es 2 entonces es un ingreso.
                                    ingtot = ingtot+(Double.parseDouble(lstTransacciones.get(0).getMonto())); //sumamos el monto a la variable ingtot.
                                    salgen = salgen+(Double.parseDouble(lstTransacciones.get(0).getMonto())); //sumamos el monto a la variable salgen.
                                }
                                if(lstTransacciones.get(1).getTipo().equals("1")){ //si el tipo de del segundo objeto es 1 entonces es un gasto.
                                    gastot = gastot+(Double.parseDouble(lstTransacciones.get(1).getMonto())); //sumamos el monto a la variable gastot.
                                    salgen = salgen-(Double.parseDouble(lstTransacciones.get(1).getMonto())); //restamos el monto a la variable salgen.
                                }else{ //si el tipo de del segundo objeto es 2 entonces es un ingreso.
                                    ingtot = ingtot+(Double.parseDouble(lstTransacciones.get(1).getMonto())); //sumamos el monto a la variable ingtot.
                                    salgen = salgen+(Double.parseDouble(lstTransacciones.get(1).getMonto())); //sumamos el monto a la variable salgen.
                                }
                            }else{ //si la lista retorna 1 objeto.
                                if(lstTransacciones.get(0).getTipo().equals("1")){ //si el tipo del objeto es 1 entonces es un gasto.
                                    gastot = gastot+(Double.parseDouble(lstTransacciones.get(0).getMonto())); //sumamos el monto a la variable gastot.
                                    salgen = salgen-(Double.parseDouble(lstTransacciones.get(0).getMonto())); //restamos el monto a la variable salgen.
                                }else{ //si el tipo del objeto es 2 entonces es un ingreso.
                                    ingtot = ingtot+(Double.parseDouble(lstTransacciones.get(0).getMonto())); //sumamos el monto a la variable ingtot.
                                    salgen = salgen+(Double.parseDouble(lstTransacciones.get(0).getMonto())); //sumamos el monto a la variable salgen.
                                }
                            }
                            tvIngNumEst.setText(String.format("%.2f", ingtot)); //seteamos en el TextView el valor de la variable gastot.
                            tvGasNumEst.setText(String.format("%.2f", gastot)); //seteamos en el TextView el valor de la variable gastot.
                            tvSalNumEst.setText(String.format("%.2f", salgen)); //seteamos en el TextView el valor de la variable gastot.
                            //preparamos el RecylerView seteandole el layout.
                            linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                            rvEstbc1.setLayoutManager(linearLayoutManager);
                            //seteamos el adaptador del RecylerView y a este le enviamos la lista de transacciones y el contexto del Fragment.
                            rvEstbc1.setAdapter(new Estadisticabc1Adapter(lstTransacciones, getContext()));
                            cargarBc2(); //llamamos el método cargarBc2 para cargar la estadistica del segundo cuadro
                            cargarBc3(); //llamamos el método cargarBc3 para cargar la estadistica del tercer cuadro
                        }
                    }
                }
                @Override
                public void onFailure(Call<List<Transacciones>> call, Throwable t) { //si hay un error a la hora de comunicarse con la API
                    rvEstbc1.setVisibility(View.INVISIBLE); //ponemos como invisble el RecylerView del primer cuadro.
                    tvNoDatosEst1.setVisibility(View.VISIBLE); //ponemos como visible el TextView.
                    tvNoDatosEst1.setText(R.string.errorconexion); //le seteamos el texto de "Error de conexión" al TexView.
                    rvEstbc2.setVisibility(View.GONE); //ponemos como invisble el RecylerView del segundo cuadro.
                    tvNoDatosEst2.setVisibility(View.VISIBLE); //ponemos como visible el TextView.
                    tvNoDatosEst2.setText(R.string.errorconexion); //le seteamos el texto de "Error de conexión" al TexView.
                    rvEstbc3.setVisibility(View.GONE); //ponemos como invisble el RecylerView del tercer cuadro.
                    tvNoDatosEst3.setVisibility(View.VISIBLE); //ponemos como visible el TextView.
                    tvNoDatosEst3.setText(R.string.errorconexion); //le seteamos el texto de "Error de conexión" al TexView.
                }
            });
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void cargarBc2(){ //método para cargar le estadística del segundo cuadro.
        try {
            service = API.getTransaccionesService();
            Call<List<Transacciones>> call = service.estadisticaBc2Bc3(Integer.parseInt(sharedPreferences.getString("id", "")), tvFechaDesdeEst.getText().toString(), tvFechaHastaEst.getText().toString(), 1);
            call.enqueue(new Callback<List<Transacciones>>() {
                @Override
                public void onResponse(Call<List<Transacciones>> call, Response<List<Transacciones>> response) {
                    if (response.isSuccessful()) { //si se ejecuta exitosamente.
                        lstTransacciones = response.body(); //seteamos el response.body() que nos devolvera una lista de transacciones en la variable lstTransacciones.
                        if(lstTransacciones.isEmpty()){ //si la lista está vacía.
                            rvEstbc2.setVisibility(View.GONE); //ponemos como invisble el RecylerView del segundo cuadro.
                            tvNoDatosEst2.setVisibility(View.VISIBLE); //ponemos como visible el TextView que nos indica que no hay transacciones registradas.
                        }else{
                            rvEstbc2.setVisibility(View.VISIBLE); //ponemos como visble el RecylerView del segundo cuadro.
                            tvNoDatosEst2.setVisibility(View.INVISIBLE);  //ponemos como invisible el TextView que nos indica que no hay transacciones registradas.
                            //preparamos el RecylerView seteandole el layout.
                            linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                            rvEstbc2.setLayoutManager(linearLayoutManager);
                            //seteamos el adaptador del RecylerView y a este le enviamos la lista de transacciones, el contexto del Fragment
                            //la variables gastot, la variable ingtot y el numero 1 ques es para indicar que estamos cargando la estadistica
                            //del segundo cuadro es decir los gastos.
                            rvEstbc2.setAdapter(new Estadisticabc2bc3Adapter(lstTransacciones, getContext(), gastot, ingtot, 1));
                        }
                    }
                }
                @Override
                public void onFailure(Call<List<Transacciones>> call, Throwable t) { //si hay un error a la hora de comunicarse con la API
                    rvEstbc2.setVisibility(View.GONE); //ponemos como invisble el RecylerView del segundo cuadro.
                    tvNoDatosEst2.setVisibility(View.VISIBLE); //ponemos como visible el TextView.
                    tvNoDatosEst2.setText(R.string.errorconexion); //le seteamos el texto de "Error de conexión" al TexView.
                }
            });
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void cargarBc3(){ //método para cargar le estadística del tercer cuadro.
        try {
            service = API.getTransaccionesService();
            Call<List<Transacciones>> call = service.estadisticaBc2Bc3(Integer.parseInt(sharedPreferences.getString("id", "")), tvFechaDesdeEst.getText().toString(), tvFechaHastaEst.getText().toString(), 2);
            call.enqueue(new Callback<List<Transacciones>>() {
                @Override
                public void onResponse(Call<List<Transacciones>> call, Response<List<Transacciones>> response) {
                    if (response.isSuccessful()) { //si se ejecuta exitosamente.
                        lstTransacciones = response.body(); //seteamos el response.body() que nos devolvera una lista de transacciones en la variable lstTransacciones.
                        if(lstTransacciones.isEmpty()){ //si la lista está vacía.
                            rvEstbc3.setVisibility(View.GONE); //ponemos como invisble el RecylerView del tercer cuadro.
                            tvNoDatosEst3.setVisibility(View.VISIBLE); //ponemos como visible el TextView que nos indica que no hay transacciones registradas.
                        }else{
                            rvEstbc3.setVisibility(View.VISIBLE); //ponemos como visble el RecylerView del tercer cuadro.
                            tvNoDatosEst3.setVisibility(View.INVISIBLE); //ponemos como invisible el TextView que nos indica que no hay transacciones registradas.
                            //preparamos el RecylerView seteandole el layout.
                            linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                            rvEstbc3.setLayoutManager(linearLayoutManager);
                            //seteamos el adaptador del RecylerView y a este le enviamos la lista de transacciones, el contexto del Fragment
                            //la variables gastot, la variable ingtot y el numero 2 ques es para indicar que estamos cargando la estadistica
                            //del tercer cuadro es decir los ingresos.
                            rvEstbc3.setAdapter(new Estadisticabc2bc3Adapter(lstTransacciones, getContext(),  gastot, ingtot, 2));
                        }
                    }
                }
                @Override
                public void onFailure(Call<List<Transacciones>> call, Throwable t) { //si hay un error a la hora de comunicarse con la API
                    rvEstbc3.setVisibility(View.GONE); //ponemos como invisble el RecylerView del tercer cuadro.
                    tvNoDatosEst3.setVisibility(View.VISIBLE); //ponemos como visible el TextView.
                    tvNoDatosEst3.setText(R.string.errorconexion); //le seteamos el texto de "Error de conexión" al TexView.
                }
            });
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

}