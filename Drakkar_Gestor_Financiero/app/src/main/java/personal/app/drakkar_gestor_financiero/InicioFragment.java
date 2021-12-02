package personal.app.drakkar_gestor_financiero;

import static personal.app.drakkar_gestor_financiero.PrincipalActivity.sharedPreferences;
import static personal.app.drakkar_gestor_financiero.PrincipalActivity.opccatad;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import personal.app.drakkar_gestor_financiero.API.API;
import personal.app.drakkar_gestor_financiero.Adapters.TransaccionesAdapter;
import personal.app.drakkar_gestor_financiero.Models.Transacciones;
import personal.app.drakkar_gestor_financiero.Models.Usuarios;
import personal.app.drakkar_gestor_financiero.Services.TransaccionesService;
import personal.app.drakkar_gestor_financiero.Services.UsuariosService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InicioFragment extends Fragment {

    SwipeRefreshLayout swipeIni;
    Button btnMiPerfilIni; //componente gráfico.
    TextView tvNomPerfilIni, tvSalGenNumIni, tvIngTotNumIni, tvGasTotNumIni, tvNoDatosIni; //componentes gráficos.
    ConstraintLayout clCuadro3Ini; //componente gráfico.
    RecyclerView rvUltTra; //componente gráfico.
    TransaccionesService transaccionesService; //objeto service para acceder a los métodos de manejo de datos de transacciones.
    List<Transacciones> lstTransacciones = new ArrayList<>(); //lista de objetos de tipo Transacciones.
    UsuariosService usuariosService; //objeto service para acceder a los métodos de manejo de datos de usuarios.
    List<Usuarios> usuario = new ArrayList<>(); //lista de objetos de tipo Usuarios.
    LinearLayoutManager linearLayoutManager; //objeto de tipo LinearLayoutManager

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);
        swipeIni = view.findViewById(R.id.swipeIni);
        opccatad = 1; //seteamos esta variable en 1 para indicar que estamos usando el adapter desde el Fragment InicioFragment.
        btnMiPerfilIni = view.findViewById(R.id.btnMiPerfilIni);
        rvUltTra = view.findViewById(R.id.rvUltTraIni);
        tvNomPerfilIni = view.findViewById(R.id.tvNomPerfilIni);
        tvSalGenNumIni = view.findViewById(R.id.tvSalGenNumIni);
        tvIngTotNumIni = view.findViewById(R.id.tvIngTotNumIni);
        tvGasTotNumIni = view.findViewById(R.id.tvGasTotNumIni);
        tvNoDatosIni = view.findViewById(R.id.tvNoDatosIni);
        clCuadro3Ini = view.findViewById(R.id.clCuadro3Ini);
        cargarUsuario(); //cargamos los datos del usuario.
        lstTransacciones(); //listamos las ultimas 5 transacciones.
        swipeIni.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarUsuario(); //cargamos los datos del usuario.
                lstTransacciones(); //listamos las ultimas 5 transacciones.
                swipeIni.setRefreshing(false);
            }
        });
        btnMiPerfilIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickbtnMiPerfilIni();
            }
        });
        clCuadro3Ini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarCategorias();
            }
        });
        return view;
    }

    @Override
    public void onResume(){ //método que se ejecuta cuando se retoma este Fragment.
        super.onResume();
        try {
            opccatad = 1; //seteamos esta variable en 1 para indicar que estamos usando el adapter desde el Fragment InicioFragment.
            cargarUsuario(); //cargamos los datos del usuario.
            lstTransacciones(); //listamos las transacciones.
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void cargarUsuario(){
        try {
            usuariosService = API.getUsuariosService();
            Call<List<Usuarios>> call = usuariosService.listarId(Integer.parseInt(sharedPreferences.getString("id", "")));
            call.enqueue(new Callback<List<Usuarios>>() {
                @Override
                public void onResponse(Call<List<Usuarios>> call, Response<List<Usuarios>> response) {
                    if (response.isSuccessful()) {
                        usuario = response.body();
                        Usuarios aux = usuario.get(0);
                        SharedPreferences.Editor editorConfig = sharedPreferences.edit();
                        editorConfig.putString("correo", aux.getCorreo());
                        editorConfig.putString("nombre", aux.getNombre());
                        editorConfig.putString("apellido", aux.getApellido());
                        editorConfig.putString("gasto_total", aux.getGasto_total());
                        editorConfig.putString("ingreso_total", aux.getIngreso_total());
                        editorConfig.putString("saldo_general", aux.getSaldo_general());
                        editorConfig.commit();
                        setDatos();
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

    public void lstTransacciones(){ //método para listar las transacciones en el RecyclerView.
        try {
            transaccionesService = API.getTransaccionesService();
            Call<List<Transacciones>> call = transaccionesService.listarUltTra(Integer.parseInt(sharedPreferences.getString("id", "")));
            call.enqueue(new Callback<List<Transacciones>>() {
                @Override
                public void onResponse(Call<List<Transacciones>> call, Response<List<Transacciones>> response) {
                    if (response.isSuccessful()) { //si se ejecuta exitosamente.
                        lstTransacciones = response.body(); //seteamos el response.body() que nos devolvera una lista de categorías en la variable lstTransacciones.
                        if(lstTransacciones.isEmpty()){ //si la lista está vacía.
                            rvUltTra.setVisibility(View.GONE);
                            tvNoDatosIni.setVisibility(View.VISIBLE); //ponemos como visible el TextView que nos indica que no hay transacciones registradas.
                        }else{ //si la lista no está vacía.
                            //preparamos el RecylerView seteandole el layout.
                            linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                            rvUltTra.setLayoutManager(linearLayoutManager);
                            //seteamos el adaptador del RecylerView y a este le enviamos la lista de transacciones y el contexto del Fragment.
                            rvUltTra.setAdapter(new TransaccionesAdapter(lstTransacciones, getContext()));
                            rvUltTra.setVisibility(View.VISIBLE);
                            tvNoDatosIni.setVisibility(View.INVISIBLE); //ponemos como invisible el TextView que nos indica que no hay transacciones registradas.
                        }
                    }
                }
                @Override
                public void onFailure(Call<List<Transacciones>> call, Throwable t) { //si hay un error a la hora de comunicarse con la API
                    rvUltTra.setVisibility(View.GONE);
                    tvNoDatosIni.setVisibility(View.VISIBLE); //ponemos como visible el TextView.
                    tvNoDatosIni.setText(R.string.errorconexion); //le seteamos el texto de "Error de conexión" al TexView.
                }
            });
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void setDatos(){
        try {
            tvNomPerfilIni.setText(sharedPreferences.getString("nombre", ""));
            tvSalGenNumIni.setText(sharedPreferences.getString("saldo_general", ""));
            tvIngTotNumIni.setText(sharedPreferences.getString("ingreso_total", ""));
            tvGasTotNumIni.setText(sharedPreferences.getString("gasto_total", ""));
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public void onClickbtnMiPerfilIni() { //método que se llama al darle click al boton mi perfil.
        Intent intent = new Intent(getActivity(), MiPerfilActivity.class);
        startActivity(intent);
    }

    public void seleccionarCategorias(){ //método que se llama al darle click al cuadro de las categorías.
        try {
            Intent intent = new Intent(getActivity(), CategoriasActivity.class);
            startActivity(intent);
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

}