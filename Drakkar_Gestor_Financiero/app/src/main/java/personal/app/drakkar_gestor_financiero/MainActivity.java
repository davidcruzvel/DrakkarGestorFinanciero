package personal.app.drakkar_gestor_financiero;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends Activity {

    SharedPreferences sharedPreferences; //objeto de tipo ShredPreferences.
    String NAME_FILE = "config"; //varibale que almacena el nomreb del SharedPreferences almacenado.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Thread.sleep(500); //tiempo de carga del splash.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(NAME_FILE, MODE_PRIVATE); //inicializamos el SharedPreferences.
        try {
            if(sharedPreferences.getString("id", "").isEmpty()){ //si el id almacenado en el SharedPreferences está vacío.
                //abrimos la Activity IniciarSesionActivity.
                Intent intent = new Intent(MainActivity.this, IniciarSesionActivity.class);
                startActivity(intent);
                finish();
            }else{ //si el id almacenado en el SharedPreferences no está vacío.
                //abrimos la Activity PrincipalActivity.
                Intent intent = new Intent(MainActivity.this, PrincipalActivity.class);
                startActivity(intent);
                finish();
            }
        }catch (Exception e){ //si da error.
            //abrimos la Activity IniciarSesionActivity.
            Intent intent = new Intent(MainActivity.this, IniciarSesionActivity.class);
            startActivity(intent);
            finish();
        }
    }

}