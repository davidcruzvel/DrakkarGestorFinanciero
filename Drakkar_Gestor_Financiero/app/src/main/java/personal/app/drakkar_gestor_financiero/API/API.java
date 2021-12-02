package personal.app.drakkar_gestor_financiero.API;

import personal.app.drakkar_gestor_financiero.Services.CategoriasService;
import personal.app.drakkar_gestor_financiero.Services.TransaccionesService;
import personal.app.drakkar_gestor_financiero.Services.UsuariosService;

public class API { //clase de la API.

    public static final String URL_001 = "http://192.168.1.9:8080/usuarios/"; //URL del controlador de usuarios de la API.
    public static final String URL_002 = "http://192.168.1.9:8080/categorias/"; //URL del controlador de categorias de la API.
    public static final String URL_003 = "http://192.168.1.9:8080/transacciones/"; //URL del controlador de transacciones de la API.

    public static UsuariosService getUsuariosService(){ //método para acceder a los métodos del controlador de usuarios.
        return  Cliente.getClient(URL_001).create(UsuariosService.class);
    }

    public static CategoriasService getCategoriasService(){ //método para acceder a los métodos del controlador de categorias.
        return  Cliente.getClient(URL_002).create(CategoriasService.class);
    }

    public static TransaccionesService getTransaccionesService(){ //método para acceder a los métodos del controlador de transacciones.
        return  Cliente.getClient(URL_003).create(TransaccionesService.class);
    }

}
