package personal.app.drakkar_gestor_financiero;

import static personal.app.drakkar_gestor_financiero.PrincipalActivity.sharedPreferences;
import static personal.app.drakkar_gestor_financiero.PrincipalActivity.categoria;
import static personal.app.drakkar_gestor_financiero.PrincipalActivity.idcategoria;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import personal.app.drakkar_gestor_financiero.API.API;
import personal.app.drakkar_gestor_financiero.Adapters.CategoriasAdapter;
import personal.app.drakkar_gestor_financiero.Models.Categorias;
import personal.app.drakkar_gestor_financiero.Services.CategoriasService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriasSeleccionarActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeSelCat;
    Button btnAgregarAgrSelCat; //componente gráfico.
    TextView tvNoDatosAgrSelCat; //componente gráfico.
    RecyclerView rvAgrSelCatRes; //componente gráfico.
    CategoriasService service; //objeto service para acceder a los métodos de manejo de datos de categorias.
    List<Categorias> lstCategorias = new ArrayList<>(); //lista de objetos de tipo Categorias.
    LinearLayoutManager linearLayoutManager; //objeto de tipo LinearLayoutManager
    Bundle bundle; //objeto de tipo Bundle para ver los datos enviados desde AgregarFragment.
    String opc; //variable en el que se almacenara el opc que se envio del bundle.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias_seleccionar);
        swipeSelCat = findViewById(R.id.swipeSelCat);
        btnAgregarAgrSelCat = findViewById(R.id.btnAgregarAgrSelCat);
        tvNoDatosAgrSelCat = findViewById(R.id.tvNoDatosAgrSelCat);
        rvAgrSelCatRes = findViewById(R.id.rvAgrSelCatRes);
        bundle = getIntent().getExtras();  //obtenemos los datos enviados desde CategoriasActivity.
        opc = bundle.getString("opc");  //seteamos el opc del bundle.
        if(opc.equals("1")){ //si es 1 entonces.
            //seteamos el color en el boton agregar a rojo.
            btnAgregarAgrSelCat.setBackgroundTintList(ColorStateList.valueOf((Color.parseColor("#F44336"))));
        }else{ //si es 2 entonces.
            //seteamos el color en el boton agregar a verde.
            btnAgregarAgrSelCat.setBackgroundTintList(ColorStateList.valueOf((Color.parseColor("#4CAF50"))));
        }
        swipeSelCat.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lstCategorias(); //listamos las categorías.
                swipeSelCat.setRefreshing(false);
            }
        });
        lstCategorias(); //listamos las categorías.
    }

    @Override
    public void onResume(){ //método que se ejecuta cuando se retoma esta Activity.
        super.onResume();
        try {
            lstCategorias(); //listamos las categorías.
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void onClickbtnAtrasAgrSelCat(View view) { //método que se llama al darle click a la flecha.
        try {
            finish();
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void onClickbtnAgregarAgrSelCat(View view) { //método que se llama al darle click al boton agregar.
        try {
            Intent intent = new Intent(CategoriasSeleccionarActivity.this, CategoriasAgregarActivity.class);
            intent.putExtra("opc", bundle.getString("opc")); //mandamos la variable opc al Intent para ver si estamos agregando un gasto o un ingreso.
            startActivity(intent);
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void lstCategorias(){ //método para listar las categorias en el RecyclerView.
        try {
            service = API.getCategoriasService();
            Call<List<Categorias>> call;
            if(opc.equals("1")){  //si opc es 1 entonces listamos los gastos.
                call = service.listarGastos(Integer.parseInt(sharedPreferences.getString("id", "")));
            }else{  //si opc es 2 entonces listamos los ingresos.
                call = service.listarIngresos(Integer.parseInt(sharedPreferences.getString("id", "")));
            }
            call.enqueue(new Callback<List<Categorias>>() {
                @Override
                public void onResponse(Call<List<Categorias>> call, Response<List<Categorias>> response) {
                    if (response.isSuccessful()) { //si se ejecuta exitosamente.
                        lstCategorias = response.body(); //seteamos el response.body() que nos devolvera una lista de categorías en la variable lstCategorias.
                        if(lstCategorias.isEmpty()){ //si la lista está vacía.
                            rvAgrSelCatRes.setVisibility(View.GONE);
                            tvNoDatosAgrSelCat.setVisibility(View.VISIBLE); //ponemos como visible el TextView que nos indica que no hay categorias registradas.
                        }else{ //si la lista no está vacía.
                            //preparamos el RecylerView seteandole el layout.
                            linearLayoutManager = new LinearLayoutManager(CategoriasSeleccionarActivity.this, RecyclerView.VERTICAL, false);
                            rvAgrSelCatRes.setLayoutManager(linearLayoutManager);
                            //seteamos el adaptador del RecylerView y a este le enviamos la lista de categorias y el contexto de la Activity.
                            rvAgrSelCatRes.setAdapter(new CategoriasAdapter(lstCategorias, getApplicationContext()));
                            tvNoDatosAgrSelCat.setVisibility(View.INVISIBLE); //ponemos como invisible el TextView que nos indica que no hay categorias registradas.
                        }
                    }
                }
                @Override
                public void onFailure(Call<List<Categorias>> call, Throwable t) { //si hay un error a la hora de comunicarse con la API
                    rvAgrSelCatRes.setVisibility(View.GONE);
                    tvNoDatosAgrSelCat.setVisibility(View.VISIBLE); //ponemos como visible el TextView que nos indica que no hay categorias registradas.
                    tvNoDatosAgrSelCat.setText(R.string.errorconexion); //le seteamos el texto de "Error de conexión" al TexView.
                }
            });
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

}
