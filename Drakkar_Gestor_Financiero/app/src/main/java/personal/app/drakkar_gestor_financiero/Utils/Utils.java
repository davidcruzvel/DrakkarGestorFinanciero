package personal.app.drakkar_gestor_financiero.Utils;

import android.content.Context;
import android.widget.EditText;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import personal.app.drakkar_gestor_financiero.R;

public class Utils { //métodos de evaluación de EditTexts.

    public static boolean verifyEditText(EditText editText, Context context) { //método para verificar si un EditTexts no está vacío.
        if (editText.getText().toString().isEmpty()) {
            editText.setError(context.getResources().getString(R.string.campovacio));
            return false;
        }
        return true;
    }

    public static boolean verifyEditTextMail(EditText editText, Context context) { //método para verificar que el texto del EditText de correo está en formato de correo.
        if (!editText.getText().toString().isEmpty()) {
            String regx = "^(.+)@(.+)$";
            Pattern pattern = Pattern.compile(regx);
            Matcher matcher = pattern.matcher(editText.getText().toString());
            if(matcher.matches()){
                return true;
            }else{
                editText.setError(context.getResources().getString(R.string.correoinvalido));
                return false;
            }
        }else{
            editText.setError(context.getResources().getString(R.string.campovacio));
            return false;
        }
    }

    public static boolean verifyEditTextPassword(EditText editText, Context context) { // //método para verificar que el texto del EditText de contraseña cumple con los requisitos.
        if (!editText.getText().toString().isEmpty()) {
            String regx = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$"; //un número, una minúscula, una mayúscula y longitud mínima 8 caracteres.
            Pattern pattern = Pattern.compile(regx);
            Matcher matcher = pattern.matcher(editText.getText().toString());
            if(matcher.matches()){
                return true;
            }else{
                editText.setError(context.getResources().getString(R.string.contraseniainvalida));
                return false;
            }
        }else{
            editText.setError(context.getResources().getString(R.string.campovacio));
            return false;
        }
    }

}
