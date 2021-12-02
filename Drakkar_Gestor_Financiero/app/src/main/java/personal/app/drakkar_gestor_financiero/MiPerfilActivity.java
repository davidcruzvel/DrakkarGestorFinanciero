package personal.app.drakkar_gestor_financiero;

import static personal.app.drakkar_gestor_financiero.PrincipalActivity.sharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.List;
import personal.app.drakkar_gestor_financiero.API.API;
import personal.app.drakkar_gestor_financiero.Models.Usuarios;
import personal.app.drakkar_gestor_financiero.Services.UsuariosService;
import personal.app.drakkar_gestor_financiero.Utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MiPerfilActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeMiPer;
    EditText etCorreoMiPer, etNombreMiPer, etApellidoMiPer; //componentes gráficos.
    UsuariosService service; //objeto service para acceder a los métodos de manejo de datos de usuarios.
    List<Usuarios> lstUsuarios = new ArrayList<>(); //lista de objetos de tipo Usuarios.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miperfil);
        swipeMiPer = findViewById(R.id.swipeMiPer);
        etCorreoMiPer = findViewById(R.id.etCorreoMiPer);
        etNombreMiPer = findViewById(R.id.etNombreMiPer);
        etApellidoMiPer = findViewById(R.id.etApellidoMiPer);
        cargarUsuario(); //cargar los datos del usuario.
        swipeMiPer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarUsuario(); //cargar los datos del usuario.
                swipeMiPer.setRefreshing(false);
            }
        });
    }

    public void cargarUsuario(){ //método para obtener los datos del usuario.
        try {
            service = API.getUsuariosService();
            Call<List<Usuarios>> call = service.listarId(Integer.parseInt(sharedPreferences.getString("id", "")));
            call.enqueue(new Callback<List<Usuarios>>() {
                @Override
                public void onResponse(Call<List<Usuarios>> call, Response<List<Usuarios>> response) {
                    if (response.isSuccessful()) { //si se ejecuta exitosamente.
                        lstUsuarios = response.body(); //seteamos el response.body() que nos devolvera una lista de usuarios en la variable lstUsuarios.
                        if(!lstUsuarios.isEmpty()){ //si la lista no está vacía.
                            //seteamos los datos del objeto que obtuvimos en los EditTexts.
                            etCorreoMiPer.setText(lstUsuarios.get(0).getCorreo());
                            etNombreMiPer.setText(lstUsuarios.get(0).getNombre());
                            etApellidoMiPer.setText(lstUsuarios.get(0).getApellido());
                        }
                    }
                }
                @Override
                public void onFailure(Call<List<Usuarios>> call, Throwable t) {
                }
            });
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void onClickbtnAtrasMiPer(View view) { //método que se llama al darle click a la flecha.
        try {
            finish();
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void onClicketContraseniaMiPer(View view) { //método que se llama al darle click al EditText de la contraseña.
        try {
            Intent intent = new Intent(MiPerfilActivity.this, MiPerfilCamConActivity.class);
            intent.putExtra("correo", etCorreoMiPer.getText().toString()); // enviamos el correo a la Activity MiPerfilCamConActivity.
            startActivity(intent);
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void onClickbtnGuardarMiPer(View view) { //método que se llama al darle click al boton guardar.
        try {
            //evaluamos que los EditTexts no esten vacíos.
            if(Utils.verifyEditText(etNombreMiPer, getApplicationContext()) && Utils.verifyEditText(etApellidoMiPer, getApplicationContext())) {
                //seteamos los datos en el objeto de tipo Usuarios.
                Usuarios obj = new Usuarios();
                obj.setId(Integer.parseInt(sharedPreferences.getString("id", "")));
                obj.setNombre(etNombreMiPer.getText().toString());
                obj.setApellido(etApellidoMiPer.getText().toString());
                SharedPreferences.Editor editorConfig = sharedPreferences.edit(); //inicalizamos un editor del SharedPreferences.
                editorConfig.putString("nombre", etNombreMiPer.getText().toString()); //guardamos el nombre en el SharedPreferences.
                editorConfig.commit(); //guardamos el SharedPreferences
                edit(obj); //enviamos el objeto al método editar.
            }
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void edit(Usuarios obj){ //método para editar el usuario.
        try {
            service = API.getUsuariosService();
            Call<String> call = service.editUsuario(obj);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.body().toString().equals("1")){ //si el response es 1 quiere decir que se ejecutó correctamente y se editó el usuario entonces cerramos la Activity.
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

    public void onClickbtnCerrarSesionMiPer(View view) { //método que se llama al darle click al boton cerrar sesión.
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(MiPerfilActivity.this); //inicialización del builder del dialog.
            builder.setMessage(R.string.cerrarsesiondescripcion);//seteamos el mensaje del builder.
            builder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() { //seteamos la acción que se llevará a cabo cuando demos click en sí.
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MiPerfilActivity.this, IniciarSesionActivity.class);
                    SharedPreferences.Editor editorConfig = sharedPreferences.edit(); //inicalizamos un editor del SharedPreferences.
                    editorConfig.clear(); //limpiamos el SharedPreferences.
                    editorConfig.commit(); //guardamos el SharedPreferences
                    startActivity(intent);
                    finishAffinity();
                }
            });
            builder.setNegativeButton(R.string.no, null); //seteamos la acción que se llevará a cabo cuando demos click en no.
            AlertDialog dialog = builder.create(); //seteamos el builder en el dialog.
            dialog.show(); //mostramos el dialog.
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

}