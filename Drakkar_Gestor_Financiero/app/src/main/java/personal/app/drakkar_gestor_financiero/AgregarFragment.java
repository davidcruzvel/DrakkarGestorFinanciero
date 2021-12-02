package personal.app.drakkar_gestor_financiero;

import static android.content.ContentValues.TAG;
import static personal.app.drakkar_gestor_financiero.PrincipalActivity.categoria;
import static personal.app.drakkar_gestor_financiero.PrincipalActivity.idcategoria;
import static personal.app.drakkar_gestor_financiero.PrincipalActivity.opccatad;
import static personal.app.drakkar_gestor_financiero.PrincipalActivity.sharedPreferences;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Calendar;
import personal.app.drakkar_gestor_financiero.API.API;
import personal.app.drakkar_gestor_financiero.Models.Transacciones;
import personal.app.drakkar_gestor_financiero.Models.Usuarios;
import personal.app.drakkar_gestor_financiero.Services.TransaccionesService;
import personal.app.drakkar_gestor_financiero.Services.UsuariosService;
import personal.app.drakkar_gestor_financiero.Utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgregarFragment extends Fragment {

    TextView tvIdCategoriaAgr; //componente gráfico.
    EditText etDescripcionAgr, etMontoAgr, etCategoriaAgr, etFechaAgr; //componentes gráficos.
    Button btnGastoAgr, btnIngresoAgr, btnAgregarAgr; //componentes gráficos.
    TransaccionesService transaccionesservice; //objeto service para acceder a los métodos de manejo de datos de transacciones.
    UsuariosService usuariosservice; //objeto service para acceder a los métodos de manejo de datos de usuarios.
    Calendar c; //objeto Calendar usado en el método seleccionarFecha().
    DatePickerDialog dpd; //objeto DatePickerDialog, es el que se abre para seleccionar la fecha.
    DatePickerDialog.OnDateSetListener dpdl; //listener del objeto DatePickerDialog.
    int opc = 1; //variable opc que sirve para ver si se despliegan gastos o ingresos al seleccionar la categoría.

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agregar, container, false);
        opccatad = 2; //seteamos esta variable en 2 para indicar que estamos usando el adapter desde la activity CategoriasSeleccionarActivity
        tvIdCategoriaAgr = view.findViewById(R.id.tvIdCategoriaAgr);
        etDescripcionAgr = view.findViewById(R.id.etDescripcionAgr);
        etMontoAgr = view.findViewById(R.id.etMontoAgr);
        etCategoriaAgr = view.findViewById(R.id.etCategoriaAgr);
        etFechaAgr = view.findViewById(R.id.etFechaAgr);
        btnGastoAgr = view.findViewById(R.id.btnGastoAgr);
        btnIngresoAgr = view.findViewById(R.id.btnIngresoAgr);
        btnAgregarAgr = view.findViewById(R.id.btnAgregarAgr);
        seleccionarGastos(); //llamamos este método para que al abrir el fragment aparezca en la opción de agregar gasto.
        btnGastoAgr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarGastos();
            }
        });
        btnIngresoAgr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarIngresos();
            }
        });
        etCategoriaAgr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarCategoria();
            }
        });
        etFechaAgr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarFecha();
            }
        });
        btnAgregarAgr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickbtnAgregarAgr();
            }
        });
        return view;
    }

    @Override
    public void onResume(){ //método que se ejecuta cuando se retoma este Fragment.
        super.onResume();
        try {
            opccatad = 2; //seteamos esta variable en 2 para indicar que estamos usando el adapter desde el Fragment AgregarFragment.
            if(!idcategoria.isEmpty() && !categoria.isEmpty()){ //evaluar si no estas vacíos.
                tvIdCategoriaAgr.setText(idcategoria); //seteamos el idcategoria en el TextView del idcategoria que esta oculta.
                etCategoriaAgr.setText(categoria); //seteamos la categoria en el EditTexts categorías.
                idcategoria = ""; //limpiamos la variable idcategoria que sirve para ver que categoria se seleccionó en CategoriasSeleccionarActivity.
                categoria = ""; //limpiamos la variable categoria que sirve para ver que categoria se seleccionó en CategoriasSeleccionarActivity.
            }else{
                limpiarEditTexts();
            }
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void seleccionarGastos(){ //método para desplegar la opción de agregar gasto.
        try {
            btnIngresoAgr.setTextColor(Color.parseColor("#808080")); //seteamos el color en el boton ingreso a gris claro.
            btnGastoAgr.setTextColor(Color.parseColor("#F44336")); //seteamos el color en el boton gasto a rojo.
            btnAgregarAgr.setBackgroundColor(Color.parseColor("#F44336")); //seteamos el color en el boton agregar a rojo.
            limpiarEditTexts();
            idcategoria = ""; //limpiamos la variable idcategoria que sirve para ver que categoria se seleccionó en CategoriasSeleccionarActivity.
            categoria = ""; //limpiamos la variable categoria que sirve para ver que categoria se seleccionó en CategoriasSeleccionarActivity.
            opc = 1; //seteamos opc en 1 para indicar que seleccionamos la opción de agregar gasto.
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void seleccionarIngresos(){ //método para desplegar la opción de agregar ingreso.
        try {
            btnGastoAgr.setTextColor(Color.parseColor("#808080")); //seteamos el color en el boton gasto a gris claro.
            btnIngresoAgr.setTextColor(Color.parseColor("#4CAF50")); //seteamos el color en el boton ingreso a verde.
            btnAgregarAgr.setBackgroundColor(Color.parseColor("#4CAF50")); //seteamos el color en el boton agregar a verde.
            limpiarEditTexts();
            idcategoria = ""; //limpiamos la variable idcategoria que sirve para ver que categoria se seleccionó en CategoriasSeleccionarActivity.
            categoria = ""; //limpiamos la variable categoria que sirve para ver que categoria se seleccionó en CategoriasSeleccionarActivity.
            opc = 2; //seteamos opc en 2 para indicar que seleccionamos la opción de agregar ingreso.
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void seleccionarCategoria(){ //método para desplegar la Activity de CategoriasSeleccionarActivity.
        try {
            idcategoria = tvIdCategoriaAgr.getText().toString(); //seteamos el idcategorias del Textview del Fragment en la variable idcategoria.
            categoria = etCategoriaAgr.getText().toString(); //setemoas la categoria del EditText del Fragment en la variable categoria.
            Intent intent = new Intent(getActivity(), CategoriasSeleccionarActivity.class);
            intent.putExtra("opc", ""+opc); //mandamos la variable opc al Intent para ver si estamos agregando un gasto o un ingreso.
            startActivity(intent);
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void seleccionarFecha(){ //método para desplegar el DatePickerDialog para seleccionar la fecha.
        try {
            c = Calendar.getInstance(); //obtenemos la fecha actual.
            int anio = c.get(Calendar.YEAR); //seteamos el año.
            int mes = c.get(Calendar.MONTH); //seteamos el mes.
            int dia = c.get(Calendar.DAY_OF_MONTH); //seteamos el día.
            dpdl = new DatePickerDialog.OnDateSetListener() { //listener del DatePickerDialog.
                @Override
                public void onDateSet(DatePicker datePicker, int anio, int mes, int dia) {
                    Log.d(TAG, "onDataSet: yyyy-mm-dd: "+anio+"-"+(mes+1)+"-"+dia); //seteamos la fecha en el DatePickerDialog.
                    etFechaAgr.setText(anio+"-"+(mes+1)+"-"+dia); //seteamos la fecha en el EditText de la fecha en el Fragment.
                }
            };
            dpd = new DatePickerDialog(getContext(), dpdl, anio, mes, dia); //inicializamos el DatePickerDialog.
            dpd.show(); //desplegamos el DatePickerDialog.
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void onClickbtnAgregarAgr() { //método que se ejecuta al darle click al boton agregar.
        try {
            //evaluamos que los EditTexts no esten vacíos.
            if(Utils.verifyEditText(etDescripcionAgr, getContext()) && Utils.verifyEditText(etMontoAgr, getContext()) && Utils.verifyEditText(etCategoriaAgr, getContext()) && Utils.verifyEditText(etFechaAgr, getContext())) {
                //seteamos los datos en el objeto de tipo Transacciones.
                Transacciones obj = new Transacciones();
                obj.setIdcategoria(Integer.parseInt(tvIdCategoriaAgr.getText().toString()));
                obj.setDescripcion(etDescripcionAgr.getText().toString());
                obj.setMonto(String.format("%.2f", Double.parseDouble(etMontoAgr.getText().toString())));
                obj.setFecha(etFechaAgr.getText().toString());
                add(obj); //enviamos el objeto al método agregar.
            }
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void add(Transacciones obj){ //método para agregar una transacción.
        try {
            transaccionesservice = API.getTransaccionesService();
            Call<String> call = transaccionesservice.addTransaccion(obj);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.body().equals("1")){
                        if (opc == 1) { //si opc es 1 estamos agregando un gasto entonces llamamos el método actualizarGastos().
                            actualizarGastos();
                        } else { //si opc es 2 estamos agregando un ingreso entonces llamamos el método actualizarIngresos().
                            actualizarIngresos();
                        }
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                }
            });
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void actualizarGastos(){ //método que se ejecuta despues de que se agrego el nuevo gasto para actualizar el campo gasto_total y saldo_general.
        //obtenemos el saldo_general y gasto_total actual que esta almacenado en el SharedPreferences.
        double saldo_general = Double.parseDouble(sharedPreferences.getString("saldo_general", ""));
        double gasto_total = Double.parseDouble(sharedPreferences.getString("gasto_total", ""));
        double monto = Double.parseDouble(etMontoAgr.getText().toString()); //obtenemos el monto del EditTexts del Fragment.
        saldo_general = saldo_general-monto; //le restamos el monto al saldo general ya que estamos agregando un gasto.
        gasto_total = gasto_total+monto; //le sumamos el monto al gasto total ya que estamos agregando un gasto.
        //Actualizamos los datos en el SharedPreferences.
        SharedPreferences.Editor editorConfig = sharedPreferences.edit();
        editorConfig.putString("gasto_total", String.format("%.2f", gasto_total));
        editorConfig.putString("saldo_general", String.format("%.2f", saldo_general));
        editorConfig.commit();
        //seteamos los datos en el objeto de tipo Usuarios.
        Usuarios obj = new Usuarios();
        obj.setId(Integer.parseInt(sharedPreferences.getString("id", "")));
        obj.setSaldo_general(String.format("%.2f", Double.parseDouble(sharedPreferences.getString("saldo_general", ""))));
        obj.setGasto_total(String.format("%.2f", Double.parseDouble(sharedPreferences.getString("gasto_total", ""))));
        obj.setIngreso_total(String.format("%.2f", Double.parseDouble(sharedPreferences.getString("ingreso_total", ""))));
        editDatosUsuario(obj); //enviamos el objeto al método editDatosUsuario.
    }

    public void actualizarIngresos(){ //método que se ejecuta despues de que se agrego el nuevo ingreso para actualizar el campo ingreso_total y saldo_general.
        //obtenemos el saldo_general y ingreso_total actual que esta almacenado en el SharedPreferences.
        double saldo_general = Double.parseDouble(sharedPreferences.getString("saldo_general", ""));
        double ingreso_total = Double.parseDouble(sharedPreferences.getString("ingreso_total", ""));
        double monto = Double.parseDouble(etMontoAgr.getText().toString()); //obtenemos el monto del EditTexts del Fragment.
        saldo_general = saldo_general+monto; //le sumamos el monto al saldo general ya que estamos agregando un ingreso.
        ingreso_total = ingreso_total+monto; //le sumamos el monto al ingreso total ya que estamos agregando un ingreso.
        //Actualizamos los datos en el SharedPreferences.
        SharedPreferences.Editor editorConfig = sharedPreferences.edit();
        editorConfig.putString("ingreso_total", String.format("%.2f",ingreso_total));
        editorConfig.putString("saldo_general", String.format("%.2f",saldo_general));
        editorConfig.commit();
        //seteamos los datos en el objeto de tipo Usuarios.
        Usuarios obj = new Usuarios();
        obj.setId(Integer.parseInt(sharedPreferences.getString("id", "")));
        obj.setSaldo_general(String.format("%.2f", Double.parseDouble(sharedPreferences.getString("saldo_general", ""))));
        obj.setGasto_total(String.format("%.2f", Double.parseDouble(sharedPreferences.getString("gasto_total", ""))));
        obj.setIngreso_total(String.format("%.2f", Double.parseDouble(sharedPreferences.getString("ingreso_total", ""))));
        editDatosUsuario(obj); //enviamos el objeto al método editDatosUsuario.
    }

    public void editDatosUsuario(Usuarios obj){ //método para actualizar los campos saldo_general, gasto_total e ingreso_total del usuario en la base de datos.
        try {
            usuariosservice = API.getUsuariosService();
            Call<String> call = usuariosservice.editDatosUsuario(obj);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.body().equals("1")){ //si el response es 1 quiere decir que se ejecutó correctamente y se actualizó entonces limpiamos los EditTexts.
                        limpiarEditTexts();
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                }
            });
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void limpiarEditTexts(){ //método para limpiar todos los EditTexts.
        try {
            tvIdCategoriaAgr.setText("");
            etDescripcionAgr.setText("");
            etMontoAgr.setText("");
            etCategoriaAgr.setText("");
            etFechaAgr.setText("");
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

}