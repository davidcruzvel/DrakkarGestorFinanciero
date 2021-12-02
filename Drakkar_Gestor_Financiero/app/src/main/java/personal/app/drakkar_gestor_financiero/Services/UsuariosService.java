package personal.app.drakkar_gestor_financiero.Services;

import java.util.List;
import personal.app.drakkar_gestor_financiero.Models.Usuarios;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UsuariosService {

    @POST("iniciarsesion") //método que retorna el usuario que se consulto para guardar los datos en el SharedPreferences para iniciar sesión.
    Call<List<Usuarios>> iniciarsesion(@Body Usuarios usuario);

    @GET("listarId/{id}") //retornar los datos de un usuario enviando su id.
    Call<List<Usuarios>> listarId(@Path("id") int id);

    @GET("listarCorreo/{correo}") //retornar los datos de un usuario enviando su correo.
    Call<List<Usuarios>> listarCorreo(@Path("correo") String correo);

    @POST("cambiarContrasenia/") //método para cambiar la contraseña de un usuario.
    Call<String> cambiarContrasenia(@Body Usuarios usuario);

    @POST("contolvEnviarCorreo/") //método para enviar un correo de recuperación de contraseña.
    Call<String> contolvEnviarCorreo(@Body Usuarios usuario);

    @GET("disponibilidadCorreo/{correo}") //método para comprobar si un correo no está en uso, se usa a la hora de registrar un usuario.
    Call<String> disponibilidadCorreo(@Path("correo") String correo);

    @POST("agregar") //método para agregar un usuario.
    Call<String> addUsuario(@Body Usuarios usuario);

    @POST("actualizar/") //método para editar un usuario.
    Call<String> editUsuario(@Body Usuarios usuario);

    @POST("actualizarDatos/") //método para edtiar los campos gastos_total, ingresos_total y saldo_general un usuario.
    Call<String> editDatosUsuario(@Body Usuarios usuario);

    @GET("eliminar/{id}") //método para eliminar un usuario.
    Call<String> deleteUsuario(@Path("id") int id);

}
