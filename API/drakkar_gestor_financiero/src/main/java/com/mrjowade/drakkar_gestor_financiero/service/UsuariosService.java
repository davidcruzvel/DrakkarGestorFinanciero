package com.mrjowade.drakkar_gestor_financiero.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mrjowade.drakkar_gestor_financiero.interfaces.UsuariosInterface;
import com.mrjowade.drakkar_gestor_financiero.modelo.Usuarios;
import com.mrjowade.drakkar_gestor_financiero.modeloDAO.UsuariosDAO;

@Service
public class UsuariosService implements UsuariosInterface {

	@Autowired
	UsuariosDAO dao;

	@Override
	public List<Map<String, Object>> iniciarsesion(Usuarios obj) {
		// TODO Auto-generated method stub
		return dao.iniciarsesion(obj);
	}
	
	@Override
	public List<Map<String, Object>> listarId(int id) {
		// TODO Auto-generated method stub
		return dao.listarId(id);
	}
	
	@Override
	public List<Map<String, Object>> listarCorreo(String correo) {
		// TODO Auto-generated method stub
		return dao.listarCorreo(correo);
	}
	
	@Override
	public int cambiarContrasenia(Usuarios obj) {
		// TODO Auto-generated method stub
		return dao.cambiarContrasenia(obj);
	}
	
	@Override
	public int recuperarContrasenia(String correo, String contrasenia) {
		// TODO Auto-generated method stub
		return dao.recuperarContrasenia(correo, contrasenia);
	}
	
	@Override
	public int contolvEnviarCorreo(Usuarios obj) {
		// TODO Auto-generated method stub
		return dao.contolvEnviarCorreo(obj);
	}
	
	@Override
	public List<Map<String, Object>> verficarCodigoRecCon(String codigo)  {
		// TODO Auto-generated method stub
		return dao.verficarCodigoRecCon(codigo);
	}
	
	@Override
	public List<Map<String, Object>> disponibilidadCorreo(String correo) {
		// TODO Auto-generated method stub
		return dao.disponibilidadCorreo(correo);
	}
	
	@Override
	public List<Map<String, Object>> verficarCodigo(String codigo) {
		// TODO Auto-generated method stub
		return dao.verficarCodigo(codigo);
	}
	
	@Override
	public int activarUsuario(String codigo) {
		// TODO Auto-generated method stub
		return dao.activarUsuario(codigo);
	}

	@Override
	public int add(Usuarios obj) {		
		return dao.add(obj);
	}

	@Override
	public int edit(Usuarios obj) {
		// TODO Auto-generated method stub
		return dao.edit(obj);
	}
	
	@Override
	public int editDatos(Usuarios obj) {
		// TODO Auto-generated method stub
		return dao.editDatos(obj);
	}

	@Override
	public int delete(int id) {
		// TODO Auto-generated method stub
		return dao.delete(id);
	}
	
}
