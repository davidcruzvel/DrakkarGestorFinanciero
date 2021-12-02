package personal.app.drakkar_gestor_financiero;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RegistrarResultadoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_resultado);
    }

    public void onClickbtnIniSesRegRes(View view) { //método que se llama al darle click al boton iniciar sesión.
        try {
            finish();
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

}