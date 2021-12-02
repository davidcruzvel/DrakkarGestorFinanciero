package personal.app.drakkar_gestor_financiero;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.List;
import personal.app.drakkar_gestor_financiero.API.API;
import personal.app.drakkar_gestor_financiero.Models.Categorias;
import personal.app.drakkar_gestor_financiero.Models.Usuarios;
import personal.app.drakkar_gestor_financiero.Services.CategoriasService;
import personal.app.drakkar_gestor_financiero.Services.UsuariosService;
import personal.app.drakkar_gestor_financiero.Utils.LoadingDialog;
import personal.app.drakkar_gestor_financiero.Utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarActivity extends AppCompatActivity {

    EditText etCorreoReg, etContraseniaReg, etContConfReg, etNombreReg, etApellidoReg; //componentes gráficos.
    UsuariosService service; //objeto service para acceder a los métodos de manejo de datos de usuarios.
    List<Usuarios> lstUsuarios = new ArrayList<>(); //lista de objetos de tipo Usuarios.
    CategoriasService serviceCategorias; //objeto service para acceder a los métodos de manejo de datos de categorias.
    LoadingDialog loadingDialog = new LoadingDialog(RegistrarActivity.this); //creamos un objeto de tipo LoadingDialog que es el dialog de cargando que creamos.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        etCorreoReg = findViewById(R.id.etCorreoReg);
        etContraseniaReg = findViewById(R.id.etContraseniaReg);
        etContConfReg = findViewById(R.id.etContConfReg);
        etNombreReg = findViewById(R.id.etNombreReg);
        etApellidoReg = findViewById(R.id.etApellidoReg);
    }

    public void onClickbtnAtrasReg(View view) { //método que se llama al darle click a la flecha.
        try {
            finish();
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void onClickbtnAceptarRegistrarReg(View view) { //método que se llama al darle click al boton registrar.
        try {
            //evaluamos que los EditTexts no esten vacíos.
            if(Utils.verifyEditTextMail(etCorreoReg, getApplicationContext()) && Utils.verifyEditTextPassword(etContraseniaReg, getApplicationContext()) && Utils.verifyEditText(etContConfReg, getApplicationContext()) && Utils.verifyEditText(etNombreReg, getApplicationContext()) && Utils.verifyEditText(etApellidoReg, getApplicationContext())) {
                //seteamos los datos en el objeto de tipo Usuarios.
                Usuarios obj = new Usuarios();
                obj.setCorreo(etCorreoReg.getText().toString());
                obj.setContrasenia(etContraseniaReg.getText().toString());
                obj.setNombre(etNombreReg.getText().toString());
                obj.setApellido(etApellidoReg.getText().toString());
                if (etContraseniaReg.getText().toString().equals(etContConfReg.getText().toString())) { //comprobamos que las contraseñas coincidan.
                    disponibilidadCorreo(obj); //llamamos al metodo disponibilidadCorreo el cual corroborara que no este registrado ese correo y procedera a registrar el usuario.
                } else {
                    etContConfReg.setError(getResources().getString(R.string.registrarerrorconfcont)); //seteamos el error en la contraseña de confirmacion ya que no coincide.
                }
            }
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void disponibilidadCorreo(Usuarios obj){ //método que comprueba si el correo esta disponible.
        try {
            service = API.getUsuariosService();
            Call<String> call = service.disponibilidadCorreo(obj.getCorreo());
            loadingDialog.startLoadingDialog(); //inicializamos el LoadingDialog para que el usuario espere a que se inicie sesión.
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.body().equals("1")){ //si el response es 1 quiere decir que el correo no está disponible.
                        loadingDialog.dismissDialog(); //cerramos el LoadingDialog.
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrarActivity.this); //inicialización del builder del dialog.
                        builder.setMessage(R.string.registrarcorreo); //seteamos el mensaje del builder.
                        builder.setPositiveButton(R.string.aceptar, null); //seteamos la acción que se llevará a cabo cuando demos click en aceptar.
                        AlertDialog dialog = builder.create(); //seteamos el builder en el dialog.
                        dialog.show(); //mostramos el dialog.
                    }else{ //si el response es 2 quiere decir que el correo está disponible.
                        add(obj); //llamamos el método agregar ya que el correo está disponible.
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) { //si hay un error a la hora de comunicarse con la API
                    loadingDialog.dismissDialog(); //cerramos el LoadingDialog.
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistrarActivity.this); //inicialización del builder del dialog.
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

    public void add(Usuarios obj){ //método que agregar el usuario.
        try {
            service = API.getUsuariosService();
            Call<String> call = service.addUsuario(obj);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.body().equals("1")){ //si el response es 1 quiere decir que se agrego correctamente entonces por medio del correo vamos a obtener el id del usuario.
                        listarCorreo(obj.getCorreo()); //llamamos al método listarCorreo para obtener el id por medio del correo.
                    }else{
                        loadingDialog.dismissDialog(); //cerramos el LoadingDialog.
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrarActivity.this); //inicialización del builder del dialog.
                        builder.setMessage(R.string.registrarerror); //seteamos el mensaje del builder.
                        builder.setPositiveButton(R.string.aceptar, null); //seteamos la acción que se llevará a cabo cuando demos click en aceptar.
                        AlertDialog dialog = builder.create(); //seteamos el builder en el dialog.
                        dialog.show(); //mostramos el dialog.
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) { //si hay un error a la hora de comunicarse con la API
                    loadingDialog.dismissDialog(); //cerramos el LoadingDialog.
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistrarActivity.this); //inicialización del builder del dialog.
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

    public void listarCorreo(String correo){ //método para obtener el id por medio del correo.
        try {
            service = API.getUsuariosService();
            Call<List<Usuarios>> call = service.listarCorreo(correo);
            call.enqueue(new Callback<List<Usuarios>>() {
                @Override
                public void onResponse(Call<List<Usuarios>> call, Response<List<Usuarios>> response) {
                    if (response.isSuccessful()) { //si se ejecuta exitosamente.
                        lstUsuarios = response.body(); //seteamos el response.body() que nos devolvera una lista con el objeto de tipo Usuarios en la variable lstUsuarios.
                        if (!lstUsuarios.isEmpty()) { //si la lista está vacía.
                            int id = lstUsuarios.get(0).getId(); //seteamos en la variable id el id que obtuvimos.
                            //Ahora procederemos a agregar todas las categorias que se crean automaticamente al crear un usaurio.
                            Categorias obj = new Categorias();
                            obj.setIdusuario(id);
                            obj.setTipo("1");
                            obj.setCategoria(getResources().getString(R.string.registrarcat1));
                            addCategorias(obj);
                            obj.setCategoria(getResources().getString(R.string.registrarcat2));
                            addCategorias(obj);
                            obj.setCategoria(getResources().getString(R.string.registrarcat3));
                            addCategorias(obj);
                            obj.setCategoria(getResources().getString(R.string.registrarcat4));
                            addCategorias(obj);
                            obj.setCategoria(getResources().getString(R.string.registrarcat5));
                            addCategorias(obj);
                            obj.setCategoria(getResources().getString(R.string.registrarcat6));
                            addCategorias(obj);
                            obj.setCategoria(getResources().getString(R.string.registrarcat7));
                            addCategorias(obj);
                            obj.setCategoria(getResources().getString(R.string.registrarcat8));
                            addCategorias(obj);
                            obj.setCategoria(getResources().getString(R.string.registrarcat9));
                            addCategorias(obj);
                            obj.setCategoria(getResources().getString(R.string.registrarcat10));
                            addCategorias(obj);
                            obj.setCategoria(getResources().getString(R.string.registrarcat11));
                            addCategorias(obj);
                            obj.setCategoria(getResources().getString(R.string.registrarcat12));
                            addCategorias(obj);
                            obj.setCategoria(getResources().getString(R.string.registrarcat13));
                            addCategorias(obj);
                            obj.setCategoria(getResources().getString(R.string.registrarcat14));
                            addCategorias(obj);
                            obj.setCategoria(getResources().getString(R.string.registrarcat15));
                            addCategorias(obj);
                            obj.setCategoria(getResources().getString(R.string.registrarcat16));
                            addCategorias(obj);
                            obj.setCategoria(getResources().getString(R.string.registrarcat17));
                            addCategorias(obj);
                            obj.setCategoria(getResources().getString(R.string.registrarcat18));
                            addCategorias(obj);
                            obj.setCategoria(getResources().getString(R.string.registrarcat19));
                            addCategorias(obj);
                            obj.setCategoria(getResources().getString(R.string.registrarcat20));
                            addCategorias(obj);
                            obj.setTipo("2");
                            obj.setCategoria(getResources().getString(R.string.registrarcat21));
                            addCategorias(obj);
                            obj.setCategoria(getResources().getString(R.string.registrarcat22));
                            addCategorias(obj);
                            obj.setCategoria(getResources().getString(R.string.registrarcat23));
                            addCategorias(obj);
                            obj.setCategoria(getResources().getString(R.string.registrarcat24));
                            addCategorias(obj);
                            //cuando ya se hayan agregado todas las categorías abrimos el Activity RegistrarResultadoActivity
                            Intent intent = new Intent(RegistrarActivity.this, RegistrarResultadoActivity.class);
                            startActivity(intent);
                            finish();
                            loadingDialog.dismissDialog(); //cerramos el LoadingDialog.
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

    public void addCategorias(Categorias obj){ //método para agregar una categoria.
        try {
            serviceCategorias = API.getCategoriasService();
            Call<String> call = serviceCategorias.addCategoria(obj);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
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