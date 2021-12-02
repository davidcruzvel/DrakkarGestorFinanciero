package personal.app.drakkar_gestor_financiero;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ContOlvResultadoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cont_olv_resultado);
    }

    public void onClickbtnIniSesContOlvRes(View view) { //método que se llama al darle click al boton iniciar sesión.
        try {
            finish();
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

}