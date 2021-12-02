package personal.app.drakkar_gestor_financiero;

import static android.content.ContentValues.TAG;
import static personal.app.drakkar_gestor_financiero.PrincipalActivity.categoria;
import static personal.app.drakkar_gestor_financiero.PrincipalActivity.idcategoria;
import static personal.app.drakkar_gestor_financiero.PrincipalActivity.opccatad;
import static personal.app.drakkar_gestor_financiero.PrincipalActivity.sharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class TransaccionesModEliActivity extends AppCompatActivity {

    TextView tvIdCategoriaTraModEli; //componente gráfico.
    EditText etDescripcionTraModEli, etMontoTraModEli, etCategoriaTraModEli, etFechaTraModEli; //componentes gráficos.
    TransaccionesService transaccionesservice; //objeto service para acceder a los métodos de manejo de datos de transacciones.
    UsuariosService usuariosservice; //objeto service para acceder a los métodos de manejo de datos de usuarios.
    Calendar c; //objeto Calendar usado en el método seleccionarFecha().
    DatePickerDialog dpd; //objeto DatePickerDialog, es el que se abre para seleccionar la fecha.
    DatePickerDialog.OnDateSetListener dpdl; //listener del objeto DatePickerDialog.
    Bundle bundle; //objeto de tipo Bundle para ver los datos enviados desde CategoriasActivity.
    double montoaux; //variable para guardar el monto del EditText.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transacciones_modeli);
        bundle = getIntent().getExtras(); //obtenemos los datos enviados desde CategoriasActivity.
        opccatad = 2;
        tvIdCategoriaTraModEli = findViewById(R.id.tvIdCategoriaTraModEli);
        etDescripcionTraModEli = findViewById(R.id.etDescripcionTraModEli);
        etMontoTraModEli = findViewById(R.id.etMontoTraModEli);
        etCategoriaTraModEli = findViewById(R.id.etCategoriaTraModEli);
        etFechaTraModEli = findViewById(R.id.etFechaTraModEli);
        montoaux = Double.parseDouble(bundle.getString("monto")); //setear el monto que se envio a traves del adaptador.
        setDatos(); // setear los datos.
    }

    @Override
    public void onResume(){ //método que se ejecuta cuando se retoma esta Activity.
        super.onResume();
        try {
            if(!idcategoria.isEmpty() && !categoria.isEmpty()){ //evaluar si no estas vacíos.
                tvIdCategoriaTraModEli.setText(idcategoria); //seteamos el idcategoria en el TextView del idcategoria que esta oculta.
                etCategoriaTraModEli.setText(categoria); //seteamos la categoria en el EditTexts categorías.
                idcategoria=""; //limpiamos la variable idcategoria que sirve para ver que categoria se seleccionó en CategoriasSeleccionarActivity.
                categoria=""; //limpiamos la variable categoria que sirve para ver que categoria se seleccionó en CategoriasSeleccionarActivity.
            }
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void setDatos(){ //método para setear losd atos enviados a traves del adaptador.
        try {
            tvIdCategoriaTraModEli.setText(bundle.getString("idcategoria")); //setear el idcategoria que se envio a traves del adaptador.
            etDescripcionTraModEli.setText(bundle.getString("descripcion")); //setear la  descripcion que se envio a traves del adaptador.
            etMontoTraModEli.setText(bundle.getString("monto")); //setear el monto que se envio a traves del adaptador.
            etCategoriaTraModEli.setText(bundle.getString("categoria")); //setear la categoria que se envio a traves del adaptador.
            etFechaTraModEli.setText(bundle.getString("fecha")); //setear la fecha que se envio a traves del adaptador.
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void onClickbtnAtrasTraModEli(View view) { //método que se llama al darle click a la flecha.
        try {
            finish();
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void onClicketCategoriaTraModEli(View view){
        try {
            Intent intent = new Intent(TransaccionesModEliActivity.this, CategoriasSeleccionarActivity.class);
            intent.putExtra("opc", bundle.getString("tipo"));
            startActivity(intent);
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void onClicketFechaTraModEli(View view){ //método para desplegar el DatePickerDialog para seleccionar la fecha.
        try {
            c = Calendar.getInstance(); //obtenemos la fecha actual.
            int anio = c.get(Calendar.YEAR); //seteamos el año.
            int mes = c.get(Calendar.MONTH); //seteamos el mes.
            int dia = c.get(Calendar.DAY_OF_MONTH); //seteamos el día.
            dpdl = new DatePickerDialog.OnDateSetListener() { //listener del DatePickerDialog.
                @Override
                public void onDateSet(DatePicker datePicker, int anio, int mes, int dia) {
                    Log.d(TAG, "onDataSet: yyyy-mm-dd: "+anio+"-"+(mes+1)+"-"+dia); //seteamos la fecha en el DatePickerDialog.
                    etFechaTraModEli.setText(anio+"-"+(mes+1)+"-"+dia); //seteamos la fecha en el EditText de la fecha en el Fragment.
                }
            };
            dpd = new DatePickerDialog(TransaccionesModEliActivity.this, dpdl, anio, mes, dia); //inicializamos el DatePickerDialog.
            dpd.show(); //desplegamos el DatePickerDialog.
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void onClickbtnGuardarTraModEli(View view) { //método que se ejecuta al darle click al boton guardar.
        try {
            //evaluamos que los EditTexts no esten vacíos.
            if(Utils.verifyEditText(etDescripcionTraModEli, getApplicationContext()) && Utils.verifyEditText(etMontoTraModEli, getApplicationContext()) && Utils.verifyEditText(etFechaTraModEli, getApplicationContext())) {
                //seteamos los datos en el objeto de tipo Transacciones.
                Transacciones obj = new Transacciones();
                obj.setId(Integer.parseInt(bundle.getString("id")));
                obj.setIdcategoria(Integer.parseInt(tvIdCategoriaTraModEli.getText().toString()));
                obj.setDescripcion(etDescripcionTraModEli.getText().toString());
                obj.setMonto(String.format("%.2f", Double.parseDouble(etMontoTraModEli.getText().toString())));
                obj.setFecha(etFechaTraModEli.getText().toString());
                edit(obj); //enviamos el objeto al método editar.
            }
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void edit(Transacciones obj){ //método para editar una transacción.
        try {
            transaccionesservice = API.getTransaccionesService();
            Call<String> call = transaccionesservice.editTransaccion(obj);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.body().equals("1")){
                        if(bundle.getString("tipo").equals("1")){ //si opc es 1 estamos editando un gasto entonces llamamos el método actualizarGastos().
                            actualizarGasto();
                        }else{ //si opc es 2 estamos editando un ingreso entonces llamamos el método actualizarIngresos().
                            actualizarIngreso();
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

    public void actualizarGasto(){ //método que se ejecuta despues de que se modificó el gasto para actualizar el campo gasto_total y saldo_general.
        //obtenemos el saldo_general y gasto_total actual que esta almacenado en el SharedPreferences.
        double saldo_general = Double.parseDouble(sharedPreferences.getString("saldo_general", ""));
        double gasto_total = Double.parseDouble(sharedPreferences.getString("gasto_total", ""));
        double monto = Double.parseDouble(etMontoTraModEli.getText().toString()); //obtenemos el monto del EditTexts del Activity.
        saldo_general = saldo_general+montoaux; //le sumamos al saldo_general el monto que guardamos en montoaux por si el monto se edito.
        saldo_general = saldo_general-monto; //le restamos el monto que editamos.
        gasto_total = gasto_total-montoaux; //le restamos al gasto_total el montoaux por si el monto se edito.
        gasto_total = gasto_total+monto; //le sumamos al gasto_total el monto que editamos.
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

    public void actualizarIngreso(){ //método que se ejecuta despues de que se modificó el ingreso para actualizar el campo ingreso_total y saldo_general.
        //obtenemos el saldo_general y ingreso_total actual que esta almacenado en el SharedPreferences.
        double saldo_general = Double.parseDouble(sharedPreferences.getString("saldo_general", ""));
        double ingreso_total = Double.parseDouble(sharedPreferences.getString("ingreso_total", ""));
        double monto = Double.parseDouble(etMontoTraModEli.getText().toString()); //obtenemos el monto del EditTexts del Activity.
        saldo_general = saldo_general-montoaux; //le restamos al saldo_general el monto que guardamos en montoaux por si el monto se edito.
        saldo_general = saldo_general+monto; //le sumamos el monto que editamos.
        ingreso_total = ingreso_total-montoaux; //le restamos al ingreso_total el montoaux por si el monto se edito.
        ingreso_total = ingreso_total+monto; //le sumamos al ingreso_total el monto que editamos.
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

    public void onClickbtnEliminarTraModEli(View view) { //método que se llama al darle click al boton eliminar.
        try {
            //inicializamos un dialog para preguntarle al usuario si quiere eliminar la transacción.
            AlertDialog.Builder builder = new AlertDialog.Builder(TransaccionesModEliActivity.this); //inicialización del builder del dialog.
            builder.setTitle(R.string.confirmacioneliminacion); //seteamos el título del builder.
            builder.setMessage(R.string.confelmdescripcion); //seteamos el mensaje del builder.
            builder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() { //seteamos la acción que se llevará a cabo cuando demos click en sí.
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    delete(Integer.parseInt(bundle.getString("id"))); //enviamos el id de la transacción al método eliminar.
                }
            });
            builder.setNegativeButton(android.R.string.cancel, null); //seteamos la acción que se llevará a cabo cuando demos click en cancelar.
            AlertDialog dialog = builder.create(); //seteamos el builder en el dialog.
            dialog.show(); //mostramos el dialog.
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void delete(int id){ //método para eliminar una transacción.
        try {
            transaccionesservice = API.getTransaccionesService();
            Call<String> call = transaccionesservice.deleteTransaccion(id);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.body().equals("1")){ //si opc es 1 estamos eliminando un gasto entonces llamamos el método eliminarGasto().
                        if(bundle.getString("tipo").equals("1")){
                            eliminarGasto();
                        }else{ //si opc es 2 estamos eliminando un ingreso entonces llamamos el método eliminarIngreso().
                            eliminarIngreso();
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

    public void eliminarGasto(){
        //obtenemos el saldo_general y ingreso_total actual que esta almacenado en el SharedPreferences.
        double saldo_general = Double.parseDouble(sharedPreferences.getString("saldo_general", ""));
        double gasto_total = Double.parseDouble(sharedPreferences.getString("gasto_total", ""));
        saldo_general = saldo_general+montoaux; //le sumamos al saldo_general el monto que guardamos en montoaux por si el monto se edito.
        gasto_total = gasto_total-montoaux; //le restamos al gasto_total el monto.
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

    public void eliminarIngreso(){
        //obtenemos el saldo_general y ingreso_total actual que esta almacenado en el SharedPreferences.
        double saldo_general = Double.parseDouble(sharedPreferences.getString("saldo_general", ""));
        double ingreso_total = Double.parseDouble(sharedPreferences.getString("ingreso_total", ""));
        saldo_general = saldo_general-montoaux; //le restamos al saldo_general el monto que guardamos en montoaux por si el monto se edito.
        ingreso_total = ingreso_total-montoaux; //le restamos al ingreso_total el monto.
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
                    if(response.body().equals("1")){ //si el response es 1 quiere decir que se ejecutó correctamente y se actualizó.
                        finish();
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

}