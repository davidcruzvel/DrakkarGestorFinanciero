package personal.app.drakkar_gestor_financiero;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PrincipalActivity extends AppCompatActivity {

    //inicializamos los Fragments.
    InicioFragment iniciofragment = new InicioFragment();
    TransaccionesFragment transaccionesfragment = new TransaccionesFragment();
    AgregarFragment agregarfragment = new AgregarFragment();
    EstadisticaFragment estadisticafragment = new EstadisticaFragment();
    BottomNavigationView navigation;
    //inicializamos las diferentews variables que se usaran dentro de los Fragments.
    public static SharedPreferences sharedPreferences; //objeto de tipo ShredPreferences.
    public static String NAME_FILE = "config"; //varibale que almacena el nomreb del SharedPreferences almacenado.
    public static int opccatad; //variable que sirve para saber desde que FRagment se está usando el adapter.
    public static String idcategoria = ""; //variable donde almacenaremos el id de la categoria que selecciones en CategoriasSeleccionarActivity.
    public static String categoria = ""; //variable donde almacenaremos la categoria que selecciones en CategoriasSeleccionarActivity.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        sharedPreferences = getSharedPreferences(NAME_FILE, MODE_PRIVATE); //inicializamos el SharedPreferences.
        navigation = findViewById(R.id.menu);
        loadFragment(iniciofragment); //cargamos el Fragment de InicioFragment
        navigation.setOnItemSelectedListener(item -> { //listener para acceder a los diferentes Fragments.
            switch (item.getItemId()){
                case R.id.InicioFragment:
                    loadFragment(iniciofragment);
                    return true;
                case R.id.TransaccionesFragment:
                    loadFragment(transaccionesfragment);
                    return true;
                case R.id.AgregarFragment:
                    loadFragment(agregarfragment);
                    return true;
                case R.id.EstadisticaFragment:
                    loadFragment(estadisticafragment);
                    return true;
            }
            return false;
        });
    }

    public void loadFragment(Fragment fragment){ //método para cargar los Fragments.
        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.clContenedor, fragment);
            transaction.commit();
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

}