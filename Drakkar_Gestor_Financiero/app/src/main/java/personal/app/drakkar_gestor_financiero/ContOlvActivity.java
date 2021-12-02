package personal.app.drakkar_gestor_financiero;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import personal.app.drakkar_gestor_financiero.API.API;
import personal.app.drakkar_gestor_financiero.Models.Usuarios;
import personal.app.drakkar_gestor_financiero.Services.UsuariosService;
import personal.app.drakkar_gestor_financiero.Utils.LoadingDialog;
import personal.app.drakkar_gestor_financiero.Utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContOlvActivity extends AppCompatActivity {

    EditText etCorreoContOlv; //componente gráfico.
    UsuariosService service; //objeto service para acceder a los métodos de manejo de datos de usuarios.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cont_olv);
        etCorreoContOlv = findViewById(R.id.etCorreoContOlv);
    }

    public void onClickbtnAtrasContOlv(View view) { //método que se llama al darle click a la flecha.
        try {
            finish();
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void onClickbtnEnviarContOlv(View view) { //método que se llama al darle click al boton enviar.
        try {
            //evaluamos que los EditTexts no esten vacíos.
            if(Utils.verifyEditTextMail(etCorreoContOlv, getApplicationContext())){
                //seteamos los datos en el objeto de tipo Usuarios.
                Usuarios obj = new Usuarios();
                obj.setCorreo(etCorreoContOlv.getText().toString());
                contolvEnviarCorreo(obj); //enviamos el objeto al método contolvEnviarCorreo.
            }
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void contolvEnviarCorreo(Usuarios obj){ //método para enviar el correo para recuperar la contraseña.
        try {
            service = API.getUsuariosService();
            Call<String> call = service.contolvEnviarCorreo(obj);
            LoadingDialog loadingDialog = new LoadingDialog(ContOlvActivity.this); //creamos un objeto de tipo LoadingDialog que es el dialog de cargando que creamos
            loadingDialog.startLoadingDialog(); //inicializamos el LoadingDialog para que el usuario espere a que se envie el correo.
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.body().toString().equals("1")){ //si el response es 1 quiere decir que se ejecutó correctamente y se envio el correo.
                        //cerramos la Activity e iniciamos la Activity ContOlvResultadoActivity.
                        Intent intent = new Intent(ContOlvActivity.this, ContOlvResultadoActivity.class);
                        startActivity(intent);
                        finish();
                        loadingDialog.dismissDialog(); //cerramos el LoadingDialog.
                    }else{ //si el response es 2 quiere decir que no se ejecutó correctamente.
                        loadingDialog.dismissDialog(); loadingDialog.dismissDialog(); //cerramos el LoadingDialog.
                        AlertDialog.Builder builder = new AlertDialog.Builder(ContOlvActivity.this); //inicialización del builder del dialog.
                        builder.setMessage(R.string.contolverror); //seteamos el mensaje del builder.
                        builder.setPositiveButton(R.string.aceptar, null); //seteamos la acción que se llevará a cabo cuando demos click en sí.
                        AlertDialog dialog = builder.create(); //seteamos el builder en el dialog.
                        dialog.show(); //mostramos el dialog.
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) { //si hay un error a la hora de comunicarse con la API
                    loadingDialog.dismissDialog(); //cerramos el LoadingDialog.
                    AlertDialog.Builder builder = new AlertDialog.Builder(ContOlvActivity.this); //inicialización del builder del dialog.
                    builder.setMessage(R.string.errorconexion); //seteamos el mensaje de "Error de conexión" del builder.
                    builder.setPositiveButton(R.string.aceptar, null); //seteamos la acción que se llevará a cabo cuando demos click en sí.
                    AlertDialog dialog = builder.create(); //seteamos el builder en el dialog.
                    dialog.show(); //mostramos el dialog.
                }
            });
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

}