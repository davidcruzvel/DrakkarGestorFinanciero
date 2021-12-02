package personal.app.drakkar_gestor_financiero.Services;

import java.util.List;
import personal.app.drakkar_gestor_financiero.Models.Transacciones;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TransaccionesService { //métodos de manejo de transacciones, estos llaman a los métodos del controlador de transacciones de la API.

    @GET("listar/{id}/{desde}/{hasta}") //mostrar todas las transacciones de un usuario por el rango de fecha enviado.
    Call<List<Transacciones>> listar(@Path("id") int id, @Path("desde") String desde, @Path("hasta") String hasta);

    @GET("listarUltTra/{id}") //mostrar las últimas 5 transacciones de un usuario.
    Call<List<Transacciones>> listarUltTra(@Path("id") int id);

    @GET("listarId/{id}") //mostrar todas las transacciones de un usuario.
    Call<List<Transacciones>> listarId(@Path("id") int id);

    @GET("estadisticaBc1/{id}/{desde}/{hasta}") //retorna el monto de ingresos y gastos de un usuario, estos se usan en la primer estadística.
    Call<List<Transacciones>> estadisticaBc1(@Path("id") int id, @Path("desde") String desde, @Path("hasta") String hasta);

    @GET("estadisticaBc2Bc3/{id}/{desde}/{hasta}/{tipo}") //retorna las transacciones de un usuario por el rango de fecha enviado y por el tipo, es decir gastos o ingresos.
    Call<List<Transacciones>> estadisticaBc2Bc3(@Path("id") int id, @Path("desde") String desde, @Path("hasta") String hasta, @Path("tipo") int tipo);

    @POST("agregar") //método para agregar una categoría.
    Call<String> addTransaccion(@Body Transacciones transaccion);

    @POST("actualizar") //método para editar una categoría.
    Call<String> editTransaccion(@Body Transacciones transaccion);

    @POST("eliminar/{id}") //método para eliminar una categoría.
    Call<String> deleteTransaccion(@Path("id") int id);

}
