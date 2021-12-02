package personal.app.drakkar_gestor_financiero;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import personal.app.drakkar_gestor_financiero.API.API;
import personal.app.drakkar_gestor_financiero.Models.Categorias;
import personal.app.drakkar_gestor_financiero.Services.CategoriasService;
import personal.app.drakkar_gestor_financiero.Utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static personal.app.drakkar_gestor_financiero.PrincipalActivity.sharedPreferences;

public class CategoriasAgregarActivity extends AppCompatActivity {

    EditText etCategoriaAgrCat; //componente gráfico.
    CategoriasService service; //objeto service para acceder a los métodos de manejo de datos de categorias.
    Bundle bundle; //objeto de tipo Bundle para ver los datos enviados desde CategoriasActivity.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias_agregar);
        bundle = getIntent().getExtras(); //obtenemos los datos enviados desde CategoriasActivity.
        etCategoriaAgrCat = findViewById(R.id.etCategoriaAgrCat);
    }

    public void onClickbtnAtrasAgrCat(View view) { //método que se llama al darle click a la flecha.
        try {
            finish();
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void onClickbtnAgregarAgrCat(View view) { //método que se llama al darle click al boton agregar.
        try {
            //evaluamos que los EditTexts no esten vacíos.
            if(Utils.verifyEditText(etCategoriaAgrCat, getApplicationContext())) {
                //seteamos los datos en el objeto de tipo Categorias.
                Categorias obj = new Categorias();
                obj.setIdusuario(Integer.parseInt(sharedPreferences.getString("id", "")));
                obj.setCategoria(etCategoriaAgrCat.getText().toString());
                obj.setTipo(bundle.getString("opc"));
                add(obj); //enviamos el objeto al método agregar.
            }
        }catch(Exception ex){
            ex.getStackTrace();
        }

    }

    public void add(Categorias obj){ //método para agregar una categoria.
        try {
            service = API.getCategoriasService();
            Call<String> call = service.addCategoria(obj);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.body().equals("1")){ //si el response es 1 quiere decir que se ejecutó correctamente y se agregó la categoría entonces cerramos la Activity.
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