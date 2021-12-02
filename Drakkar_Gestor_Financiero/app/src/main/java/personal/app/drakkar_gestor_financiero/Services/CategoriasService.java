package personal.app.drakkar_gestor_financiero.Services;

import java.util.List;
import personal.app.drakkar_gestor_financiero.Models.Categorias;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CategoriasService { //métodos de manejo de categorías, estos llaman a los métodos del controlador de categorías de la API.

    @GET("listar") //mostrar todas las categorías de un usuario.
    Call<List<Categorias>> getCategorias();

    @GET("listarGastos/{id}") //mostrar solo las categorías que son de gastos de un usuario.
    Call<List<Categorias>> listarGastos(@Path("id") int id);

    @GET("listarIngresos/{id}") //mostrar solo las categorías que son de ingresos de un usuario.
    Call<List<Categorias>> listarIngresos(@Path("id") int id);

    @POST("agregar") //método para agregar una categoría.
    Call<String> addCategoria(@Body Categorias categoria);

    @POST("actualizar") //método para editar una categoría.
    Call<String> editCategoria(@Body Categorias categoria);

    @POST("eliminar/{id}") //método para eliminar una categoría.
    Call<String> deleteCategoria(@Path("id") int id);

}
