package personal.app.drakkar_gestor_financiero;

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
import static personal.app.drakkar_gestor_financiero.PrincipalActivity.sharedPreferences;

public class CategoriasActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeCat;
    Button btnGastoCat, btnIngresoCat, btnAgregarCat; //componentes gráficos.
    TextView tvNoDatosCat; //componente gráfico.
    RecyclerView rvCatRes; //componente gráfico.
    CategoriasService service; //objeto service para acceder a los métodos de manejo de datos de categorias.
    List<Categorias> lstCategorias = new ArrayList<>(); //lista de objetos de tipo Categorias.
    LinearLayoutManager linearLayoutManager; //objeto de tipo LinearLayoutManager
    int opc = 1; //variable opc que sirve para ver si se despliegan gastos o ingresos.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);
        swipeCat = findViewById(R.id.swipeCat);
        btnGastoCat = findViewById(R.id.btnGastoCat);
        btnIngresoCat = findViewById(R.id.btnIngresoCat);
        btnAgregarCat = findViewById(R.id.btnAgregarCat);
        tvNoDatosCat = findViewById(R.id.tvNoDatosCat);
        rvCatRes = findViewById(R.id.rvCatRes);
        seleccionarGastos(); //llamamos este método para que al abrir la Activity aparezcan los gastos.
        swipeCat.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(opc == 1){
                    seleccionarGastos(); //llamamos este método para que al abrir la Activity aparezcan los gastos.
                }else{
                    seleccionarIngresos(); //llamamos este método para que al abrir la Activity aparezcan los ingresos.
                }
                swipeCat.setRefreshing(false);
            }
        });
    }

    @Override
    public void onResume(){ //método que se ejecuta cuando se retoma esta Activity.
        super.onResume();
        try {
            if(opc == 1){
                seleccionarGastos(); //llamamos este método para que al abrir la Activity aparezcan los gastos.
            }else{
                seleccionarIngresos(); //llamamos este método para que al abrir la Activity aparezcan los ingresos.
            }
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void onClickbtnAtrasCat(View view) { //método que se llama al darle click a la flecha.
        try {
            finish();
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void onClickbtnGastoCat(View view) { //método que se llama al darle click al boton de gastos.
        try {
            opc = 1; //seteamos opc en 1 para indicar que seleccionamos la opción de agregar gasto.
            seleccionarGastos(); //llamamos este método para que aparezcan los gastos.
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void seleccionarGastos(){ //método para desplegar los gastos.
        try {
            btnIngresoCat.setTextColor(Color.parseColor("#808080")); //seteamos el color en el boton ingreso a gris claro.
            btnGastoCat.setTextColor(Color.parseColor("#F44336")); //seteamos el color en el boton gasto a rojo.
            btnAgregarCat.setBackgroundTintList(ColorStateList.valueOf((Color.parseColor("#F44336")))); //seteamos el color en el boton agregar a rojo.
            lstCategorias(opc); //enviamos la variable opc al método lstCategorias para desplegar los gastos en el RecyclerView.
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void onClickbtnIngresoCat(View view) {
        try {
            opc = 2; //seteamos opc en 2 para indicar que seleccionamos la opción de agregar ingresos.
            seleccionarIngresos(); //llamamos este método para que aparezcan los ingresos.
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void seleccionarIngresos(){ //método para desplegar los ingresos.
        try {
            btnGastoCat.setTextColor(Color.parseColor("#808080")); //seteamos el color en el boton gasto a gris claro.
            btnIngresoCat.setTextColor(Color.parseColor("#4CAF50")); //seteamos el color en el boton ingreso a verde.
            btnAgregarCat.setBackgroundTintList(ColorStateList.valueOf((Color.parseColor("#4CAF50")))); //seteamos el color en el boton agregar a verde.
            lstCategorias(opc); //enviamos la variable opc al método lstCategorias para desplegar los ingresos en el RecyclerView.
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void lstCategorias(int opc){ //método para listar las categorias en el RecyclerView.
        try {
            service = API.getCategoriasService();
            Call<List<Categorias>> call;
            if(opc == 1){ //si opc es 1 entonces listamos los gastos.
                call = service.listarGastos(Integer.parseInt(sharedPreferences.getString("id", "")));
            }else{ //si opc es 2 entonces listamos los ingresos.
                call = service.listarIngresos(Integer.parseInt(sharedPreferences.getString("id", "")));
            }
            call.enqueue(new Callback<List<Categorias>>() {
                @Override
                public void onResponse(Call<List<Categorias>> call, Response<List<Categorias>> response) {
                    if (response.isSuccessful()) { //si se ejecuta exitosamente.
                        lstCategorias = response.body(); //seteamos el response.body() que nos devolvera una lista de categorías en la variable lstCategorias.                        
                        if(lstCategorias.isEmpty()){ //si la lista está vacía.
                            rvCatRes.setVisibility(View.GONE);
                            tvNoDatosCat.setVisibility(View.VISIBLE); //ponemos como visible el TextView que nos indica que no hay categorias registradas.
                        }else{ //si la lista no está vacía.
                            //preparamos el RecylerView seteandole el layout.
                            linearLayoutManager = new LinearLayoutManager(CategoriasActivity.this, RecyclerView.VERTICAL, false);
                            rvCatRes.setLayoutManager(linearLayoutManager);
                            //seteamos el adaptador del RecylerView y a este le enviamos la lista de categorias y el contexto de la Activity.
                            rvCatRes.setAdapter(new CategoriasAdapter(lstCategorias, getApplicationContext()));
                            tvNoDatosCat.setVisibility(View.INVISIBLE); //ponemos como invisible el TextView que nos indica que no hay categorias registradas.
                        }
                    }
                }
                @Override
                public void onFailure(Call<List<Categorias>> call, Throwable t) { //si hay un error a la hora de comunicarse con la API
                    tvNoDatosCat.setVisibility(View.VISIBLE); //ponemos como visible el TextView.
                    rvCatRes.setVisibility(View.GONE);
                    tvNoDatosCat.setText(R.string.errorconexion); //le seteamos el texto de "Error de conexión" al TexView.
                }
            });
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void onClickbtnAgregarCat(View view) { //método que se llama al darle click al boton agregar.
        try {
            Intent intent = new Intent(CategoriasActivity.this, CategoriasAgregarActivity.class);
            intent.putExtra("opc", ""+opc); //mandamos la variable opc al Intent para ver si estamos agregando un gasto o un ingreso.
            startActivity(intent);
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

}
