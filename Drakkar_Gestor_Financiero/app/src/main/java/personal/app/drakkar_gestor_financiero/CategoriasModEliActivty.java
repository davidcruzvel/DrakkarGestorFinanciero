package personal.app.drakkar_gestor_financiero;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import personal.app.drakkar_gestor_financiero.API.API;
import personal.app.drakkar_gestor_financiero.Models.Categorias;
import personal.app.drakkar_gestor_financiero.Services.CategoriasService;
import personal.app.drakkar_gestor_financiero.Utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriasModEliActivty extends AppCompatActivity {

    TextView tvIdModEliCat; //componente gráfico.
    EditText etCategoriaModEliCat; //componente gráfico.
    CategoriasService service; //objeto service para acceder a los métodos de manejo de datos de categorias.
    Bundle bundle; //objeto de tipo Bundle para ver los datos enviados desde CategoriasActivity.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias_modeli);
        bundle = getIntent().getExtras(); //obtenemos los datos enviados desde CategoriasActivity.
        tvIdModEliCat = findViewById(R.id.tvIdModEliCat);
        etCategoriaModEliCat = findViewById(R.id.etCategoriaModEliCat);
        setDatos(); //seteamos los datos que recibimos del adaptador de Categorias.
    }

    public void setDatos(){ //método para setear los datos que recibimos del adaptador de Categorias.
        try {
            tvIdModEliCat.setText(bundle.getString("id"));
            etCategoriaModEliCat.setText(bundle.getString("categoria"));
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void onClickbtnAtrasModEliCat(View view) { //método que se llama al darle click a la flecha.
        try {
            finish();
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void onClickbtnGuardarModEliCat(View view) { //método que se llama al darle click al boton guardar.
        try {
            //evaluamos que los EditTexts no esten vacíos.
            if(Utils.verifyEditText(etCategoriaModEliCat, getApplicationContext())) {
                //seteamos los datos en el objeto de tipo Categorias.
                Categorias obj = new Categorias();
                obj.setId(Integer.parseInt(tvIdModEliCat.getText().toString()));
                obj.setCategoria(etCategoriaModEliCat.getText().toString());
                edit(obj); //enviamos el objeto al método editar.
            }
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void edit(Categorias obj){ //método para editar una categoria.
        try {
            service = API.getCategoriasService();
            Call<String> call=service.editCategoria(obj);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.body().toString().equals("1")){ //si el response es 1 quiere decir que se ejecutó correctamente y se editó la categoría entonces cerramos la Activity.
                        finish();
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                }
            });
            finish();
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void onClickbtnEliminarModEliCat(View view) { //método que se llama al darle click al boton eliminar.
        try {
            //inicializamos un dialog para preguntarle al usuario si quiere eliminar la categoría.
            AlertDialog.Builder builder = new AlertDialog.Builder(CategoriasModEliActivty.this); //inicialización del builder del dialog.
            builder.setTitle(R.string.confirmacioneliminacion); //seteamos el título del builder.
            builder.setMessage(R.string.confelmdescripcion); //seteamos el mensaje del builder.
            builder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() { //seteamos la acción que se llevará a cabo cuando demos click en sí.
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    delete(Integer.parseInt(tvIdModEliCat.getText().toString())); //enviamos el id de la categoría al método eliminar.
                }
            });
            builder.setNegativeButton(android.R.string.cancel, null); //seteamos la acción que se llevará a cabo cuando demos click en cancelar.
            AlertDialog dialog = builder.create(); //seteamos el builder en el dialog.
            dialog.show(); //mostramos el dialog.
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void delete(int id){ //método para eliminar una categoria.
        try {
            service = API.getCategoriasService();
            Call<String>call = service.deleteCategoria(id);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(!response.isSuccessful()){ //si no se ejecuta exitosamente.
                        AlertDialog.Builder builder = new AlertDialog.Builder(CategoriasModEliActivty.this); //inicialización del builder del dialog.
                        builder.setMessage(R.string.elicaterrordescripcion); //seteamos el mensaje del builder.
                        builder.setPositiveButton(R.string.aceptar, null); //seteamos la acción que se llevará a cabo cuando demos click en sí.
                        AlertDialog dialog = builder.create(); //seteamos el builder en el dialog.
                        dialog.show(); //mostramos el dialog.
                    }else{ //si se ejecuta exitosamente.
                        if (response.body().equals("1")) { //si el response es 1 quiere decir que se ejecutó correctamente y se eliminó la categoría entonces cerramos la Activity.
                            finish();
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

}