package com.mrjowade.drakkar_gestor_financiero.interfaces;

import java.util.List;
import java.util.Map;
import com.mrjowade.drakkar_gestor_financiero.modelo.Usuarios;

public interface UsuariosInterface{
	public List<Map<String, Object>> iniciarsesion(Usuarios obj);
	public List<Map<String, Object>> listarId(int id);
	public List<Map<String, Object>> listarCorreo(String correo);
	public int cambiarContrasenia(Usuarios obj);
	public int recuperarContrasenia(String correo, String contrasenia);
	public int contolvEnviarCorreo(Usuarios obj);
	public List<Map<String, Object>> verficarCodigoRecCon(String codigo) ;
	public List<Map<String, Object>> disponibilidadCorreo(String correo);
	public List<Map<String, Object>> verficarCodigo(String codigo);
	public int activarUsuario(String codigo);
	public int add(Usuarios obj);
	public int edit(Usuarios obj);
	public int editDatos(Usuarios obj);
	public int delete(int id);

}
