package personal.app.drakkar_gestor_financiero;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.List;
import personal.app.drakkar_gestor_financiero.API.API;
import personal.app.drakkar_gestor_financiero.Models.Usuarios;
import personal.app.drakkar_gestor_financiero.Services.UsuariosService;
import personal.app.drakkar_gestor_financiero.Utils.LoadingDialog;
import personal.app.drakkar_gestor_financiero.Utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IniciarSesionActivity extends AppCompatActivity {

    EditText etCorreoIniSes, etContraseniaIniSes; //componentes gráficos.
    UsuariosService service; //objeto service para acceder a los métodos de manejo de datos de usuarios.
    List<Usuarios> lstUsuarios = new ArrayList<>(); //lista de objetos de tipo Usuarios.
    SharedPreferences sharedPreferences; //objeto de tipo ShredPreferences.
    String NAME_FILE = "config"; //varibale que almacena el nomreb del SharedPreferences almacenado.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciarsesion);
        etCorreoIniSes = findViewById(R.id.etCorreoIniSes);
        etContraseniaIniSes = findViewById(R.id.etContraseniaIniSes);
        sharedPreferences = getSharedPreferences(NAME_FILE, MODE_PRIVATE); //inicializamos el SharedPreferences.
    }

    public void onClickbtnIniSesIniSes(View view) { //método que se llama al darle click al boton de iniciar sesión.
        try {
            //evaluamos que los EditTexts no esten vacíos.
            if(Utils.verifyEditTextMail(etCorreoIniSes, getApplicationContext()) && Utils.verifyEditText(etContraseniaIniSes, getApplicationContext())){
                etCorreoIniSes.onEditorAction(EditorInfo.IME_ACTION_DONE);
                etContraseniaIniSes.onEditorAction(EditorInfo.IME_ACTION_DONE);
                iniciarSesion(etCorreoIniSes.getText().toString(), etContraseniaIniSes.getText().toString()); //enviamos el correo y la contraseña al método iniciarSesion.
            }
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void iniciarSesion(String correo, String contrasenia){ //método para iniciar sesión en la app.
        try {
            //seteamos el correo y la contraseña en el objeto de tipo Usuarios.
            Usuarios usuario = new Usuarios();
            usuario.setCorreo(correo);
            usuario.setContrasenia(contrasenia);
            service = API.getUsuariosService();
            Call<List<Usuarios>> call = service.iniciarsesion(usuario);
            LoadingDialog loadingDialog = new LoadingDialog(IniciarSesionActivity.this); //creamos un objeto de tipo LoadingDialog que es el dialog de cargando que creamos.
            loadingDialog.startLoadingDialog(); //inicializamos el LoadingDialog para que el usuario espere a que se inicie sesión.
            call.enqueue(new Callback<List<Usuarios>>() {
                @Override
                public void onResponse(Call<List<Usuarios>> call, Response<List<Usuarios>> response) {
                    if (response.isSuccessful()) { //si se ejecuta exitosamente.
                        lstUsuarios = response.body(); //seteamos el response.body() que nos devolvera una lista con el objeto de tipo Usuarios en la variable lstUsuarios.
                        if (lstUsuarios.isEmpty()) { //si la lista está vacía.
                            loadingDialog.dismissDialog(); //cerramos el LoadingDialog.
                            AlertDialog.Builder builder = new AlertDialog.Builder(IniciarSesionActivity.this); //inicialización del builder del dialog.
                            builder.setMessage(R.string.errorinicses); //seteamos el mensaje del builder.
                            builder.setPositiveButton(R.string.aceptar, null); //seteamos la acción que se llevará a cabo cuando demos click en aceptar.
                            AlertDialog dialog = builder.create(); //seteamos el builder en el dialog.
                            dialog.show(); //mostramos el dialog.
                        } else { //si la lista no está vacía es decir que si existe el usuario.
                            Usuarios usuario = lstUsuarios.get(0); //seteamos el objeto del usuario.
                            SharedPreferences.Editor editorConfig = sharedPreferences.edit(); //inicalizamos un editor del SharedPreferences.
                            editorConfig.putString("id", String.valueOf(usuario.getId())); //guardamos el id en el SharedPreferences.
                            editorConfig.putString("correo", usuario.getCorreo()); //guardamos el correo en el SharedPreferences.
                            editorConfig.putString("nombre", usuario.getNombre()); //guardamos el nombre en el SharedPreferences.
                            editorConfig.putString("apellido", usuario.getApellido()); //guardamos el apellido en el SharedPreferences.
                            editorConfig.putString("gasto_total", usuario.getGasto_total()); //guardamos el gasto_total en el SharedPreferences.
                            editorConfig.putString("ingreso_total", usuario.getIngreso_total()); //guardamos el ingreso_total en el SharedPreferences.
                            editorConfig.putString("saldo_general", usuario.getSaldo_general()); //guardamos el saldo_general en el SharedPreferences.
                            editorConfig.commit(); //guardamos el SharedPreferences
                            Intent intent = new Intent(IniciarSesionActivity.this, PrincipalActivity.class); //iniciamos la Acitivty PrincipalActivity.
                            startActivity(intent);
                            finish();
                            loadingDialog.dismissDialog(); //cerramos el LoadingDialog.
                        }
                    }
                }
                @Override
                public void onFailure(Call<List<Usuarios>> call, Throwable t) { //si hay un error a la hora de comunicarse con la API
                    loadingDialog.dismissDialog(); //cerramos el LoadingDialog.
                    AlertDialog.Builder builder = new AlertDialog.Builder(IniciarSesionActivity.this); //inicialización del builder del dialog.
                    builder.setMessage(R.string.errorconexion); //seteamos el mensaje del builder.
                    builder.setPositiveButton(R.string.aceptar, null); //seteamos la acción que se llevará a cabo cuando demos click en aceptar.
                    AlertDialog dialog = builder.create(); //seteamos el builder en el dialog.
                    dialog.show(); //mostramos el dialog.
                }
            });
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void onClicktvOlvideContraIniSes(View view) { //método que se llama al darle click al TextView de olvide la contraseña.
        try {
            Intent intent = new Intent(IniciarSesionActivity.this, ContOlvActivity.class);
            startActivity(intent);
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void onClicktvRegistrarseIniSes(View view) { //método que se llama al darle click al TextView de registrarse.
        try {
            Intent intent = new Intent(IniciarSesionActivity.this, RegistrarActivity.class);
            startActivity(intent);
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

}