package personal.app.drakkar_gestor_financiero;

import static personal.app.drakkar_gestor_financiero.PrincipalActivity.sharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
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

public class MiPerfilCamConActivity extends AppCompatActivity {

    EditText etContActMiPerCamCon, etContNueMiPerCamCon, etContConfMiPerCamCon; //componentes gráficos.
    UsuariosService service; //objeto service para acceder a los métodos de manejo de datos de usuarios.
    List<Usuarios> lstUsuarios = new ArrayList<>(); //lista de objetos de tipo Usuarios.
    Bundle bundle; //objeto de tipo Bundle para ver los datos enviados desde CategoriasActivity.
    LoadingDialog loadingDialog = new LoadingDialog(MiPerfilCamConActivity.this); //creamos un objeto de tipo LoadingDialog que es el dialog de cargando que creamos.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miperfil_camcon);
        bundle = getIntent().getExtras(); //obtenemos los datos enviados desde MiPerfilActivity.
        etContActMiPerCamCon = findViewById(R.id.etContActMiPerCamCon);
        etContNueMiPerCamCon = findViewById(R.id.etContNueMiPerCamCon);
        etContConfMiPerCamCon = findViewById(R.id.etContConfMiPerCamCon);
    }

    public void onClickbtnAtrasMiPerCamCon(View view) { //método que se llama al darle click a la flecha.
        try {
            finish();
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void onClickbtnCambiarMiPerCamCon(View view) { //método que se llama al darle click al boton cambiar.
        try {
            //evaluamos que los EditTexts no esten vacíos.
            if(Utils.verifyEditText(etContActMiPerCamCon, getApplicationContext()) && Utils.verifyEditTextPassword(etContNueMiPerCamCon, getApplicationContext()) && Utils.verifyEditText(etContConfMiPerCamCon, getApplicationContext())){
                AlertDialog.Builder builder = new AlertDialog.Builder(MiPerfilCamConActivity.this); //inicialización del builder del dialog.
                builder.setMessage(R.string.cambiarcontraseniaaviso); //seteamos el mensaje del builder.
                builder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() { //seteamos la acción que se llevará a cabo cuando demos click en sí.
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //enviamos el correo y la contraseña para evaluar si la contraseña actual que se pidio es correcta y si es así proceder a cambiar la contraseña.
                        comprobarContAct(bundle.getString("correo"), etContActMiPerCamCon.getText().toString());
                    }
                });
                builder.setNegativeButton(R.string.no, null); //seteamos la acción que se llevará a cabo cuando demos click en no.
                AlertDialog dialog = builder.create(); //seteamos el builder en el dialog.
                dialog.show(); //mostramos el dialog.
            }
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void comprobarContAct(String correo, String contrasenia) { //método que se llama al darle click al boton cambiar.
        try {
            Usuarios usuario = new Usuarios();
            usuario.setCorreo(correo);
            usuario.setContrasenia(contrasenia);
            service = API.getUsuariosService();
            Call<List<Usuarios>> call = service.iniciarsesion(usuario);
            loadingDialog.startLoadingDialog(); //inicializamos el LoadingDialog para que el usuario espere.
            call.enqueue(new Callback<List<Usuarios>>() {
                @Override
                public void onResponse(Call<List<Usuarios>> call, Response<List<Usuarios>> response) {
                    if (response.isSuccessful()) { //si se ejecuta exitosamente.
                        lstUsuarios = response.body(); //seteamos el response.body() que nos devolvera una lista con el objeto de tipo Usuarios en la variable lstUsuarios.
                        if (!lstUsuarios.isEmpty()) { //si la lista no está vacía.
                            if (etContNueMiPerCamCon.getText().toString().equals(etContConfMiPerCamCon.getText().toString())) {//evaluamos la contraseña de confirmacion coincida.
                                cambiarContrasenia(); //llamamos al método cambiarContrasenia.
                            } else {
                                loadingDialog.dismissDialog(); //cerramos el LoadingDialog.
                                etContConfMiPerCamCon.setError(getResources().getString(R.string.errorconfcont)); //seteamos el error en el EditText de la contraseña de confirmacion.
                            }
                        } else {
                            loadingDialog.dismissDialog(); //cerramos el LoadingDialog.
                            etContActMiPerCamCon.setError(getResources().getString(R.string.errorcontact)); //seteamos el error en el EditText de la contraseña actual.
                        }
                    }
                }
                @Override
                public void onFailure(Call<List<Usuarios>> call, Throwable t) { //si hay un error a la hora de comunicarse con la API
                    loadingDialog.dismissDialog(); //cerramos el LoadingDialog.
                    AlertDialog.Builder builder = new AlertDialog.Builder(MiPerfilCamConActivity.this); //inicialización del builder del dialog.
                    builder.setMessage(R.string.errorconexion); //seteamos el mensaje del builder.
                    builder.setPositiveButton(R.string.aceptar, null); //seteamos la acción que se llevará a cabo cuando demos click en aceptar.
                    AlertDialog dialog = builder.create(); //seteamos el builder en el dialog.
                    dialog.show(); //mostramos el dialog.
                }
            });
        } catch (Exception ex) {
            ex.getStackTrace();
        }
    }

    public void cambiarContrasenia(){
        try {
            Usuarios usuario = new Usuarios();
            usuario.setId(Integer.parseInt(sharedPreferences.getString("id", "")));
            usuario.setContrasenia(etContNueMiPerCamCon.getText().toString());
            service = API.getUsuariosService();
            Call<String> call = service.cambiarContrasenia(usuario);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.body().toString().equals("1")){ //si el response es 1 quiere decir que se ejecutó correctamente y se cambio la contraseña.
                        loadingDialog.dismissDialog(); //cerramos el LoadingDialog.
                        AlertDialog.Builder builder = new AlertDialog.Builder(MiPerfilCamConActivity.this); //inicialización del builder del dialog.
                        builder.setMessage(R.string.cambiarcontraseniaexito); //seteamos el mensaje del builder.
                        builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() { //seteamos la acción que se llevará a cabo cuando demos click en aceptar.
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        AlertDialog dialog = builder.create(); //seteamos el builder en el dialog.
                        dialog.show(); //mostramos el dialog.
                    }else{ //si el response es 2 quiere decir que no se ejecutó correctamente y no se cambio la contraseña.
                        loadingDialog.dismissDialog(); //cerramos el LoadingDialog.
                        AlertDialog.Builder builder = new AlertDialog.Builder(MiPerfilCamConActivity.this); //inicialización del builder del dialog.
                        builder.setMessage(R.string.cambiarcontraseniaerror); //seteamos el mensaje del builder.
                        builder.setPositiveButton(R.string.aceptar, null); //seteamos la acción que se llevará a cabo cuando demos click en aceptar.
                        AlertDialog dialog = builder.create(); //seteamos el builder en el dialog.
                        dialog.show(); //mostramos el dialog.
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    loadingDialog.dismissDialog(); //cerramos el LoadingDialog.
                    AlertDialog.Builder builder = new AlertDialog.Builder(MiPerfilCamConActivity.this); //inicialización del builder del dialog.
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

}