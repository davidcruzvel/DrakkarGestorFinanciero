package com.mrjowade.drakkar_gestor_financiero.modeloDAO;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.mrjowade.drakkar_gestor_financiero.interfaces.UsuariosInterface;
import com.mrjowade.drakkar_gestor_financiero.modelo.Usuarios;

@Repository
public class UsuariosDAO implements UsuariosInterface {

	@Autowired
	JdbcTemplate template;

	@Override
	public List<Map<String, Object>> iniciarsesion(Usuarios obj) {
		List<Map<String, Object>> list = template.queryForList("select id, cast(aes_decrypt(correo, 'gNcaM7h3X9wm') as char) as correo, nombre, apellido, gasto_total, ingreso_total, saldo_general from usuarios where correo=aes_encrypt(?, 'gNcaM7h3X9wm') and contrasenia=aes_encrypt(?, 'gNcaM7h3X9wm') and activo=1", obj.getCorreo(), obj.getContrasenia());
		return list;
	}
	
	@Override
	public List<Map<String, Object>> listarId(int id) {
		List<Map<String, Object>> list = template.queryForList("select id, cast(aes_decrypt(correo, 'gNcaM7h3X9wm') as char) as correo, nombre, apellido, gasto_total, ingreso_total, saldo_general from usuarios where id=?", id);
		return list;
	}
	
	@Override
	public List<Map<String, Object>> listarCorreo(String correo) {
		List<Map<String, Object>> list = template.queryForList("select id from usuarios where correo=aes_encrypt(?, 'gNcaM7h3X9wm')", correo);
		return list;
	}
	
	@Override
	public int cambiarContrasenia(Usuarios obj) {
		String sql="update usuarios set contrasenia=aes_encrypt(?, 'gNcaM7h3X9wm'), codigo=null where id=?";		
		return template.update(sql, obj.getContrasenia(), obj.getId());
	}
	
	@Override
	public int recuperarContrasenia(String codigo, String contrasenia) {
		String sql="update usuarios set contrasenia=aes_encrypt(?, 'gNcaM7h3X9wm'), codigo=null where codigo=?";		
		return template.update(sql, contrasenia, codigo);
	}
	
	@Override
	public int contolvEnviarCorreo(Usuarios obj) {
		String sql="update usuarios set codigo=? where correo=aes_encrypt(?, 'gNcaM7h3X9wm')";		
		return template.update(sql, obj.getCodigo(), obj.getCorreo());
	}
	
	@Override
	public List<Map<String, Object>> verficarCodigoRecCon(String codigo) {
		List<Map<String, Object>> list = template.queryForList("select id from usuarios where codigo=?", codigo);
		return list;
	}
	
	
	@Override
	public List<Map<String, Object>> disponibilidadCorreo(String correo) {
		List<Map<String, Object>> list = template.queryForList("select id from usuarios where correo=aes_encrypt(?, 'gNcaM7h3X9wm')", correo);
		return list;
	}
	
	@Override
	public List<Map<String, Object>> verficarCodigo(String codigo) {
		List<Map<String, Object>> list = template.queryForList("select id from usuarios where codigo=? and activo=0", codigo);
		return list;
	}
	
	@Override
	public int activarUsuario(String codigo) {
		String sql="update usuarios set codigo=null, activo=1 where codigo=?";		
		return template.update(sql, codigo);
	}

	@Override
	public int add(Usuarios obj) {
		String sql = "insert into usuarios(correo, contrasenia, nombre, apellido, codigo, activo) values(aes_encrypt(?, 'gNcaM7h3X9wm'), aes_encrypt(?, 'gNcaM7h3X9wm'), ?, ?, ?, 0)";
		return template.update(sql, obj.getCorreo(), obj.getContrasenia(), obj.getNombre(), obj.getApellido(), obj.getCodigo());
	}

	@Override
	public int edit(Usuarios obj) {
		String sql="update usuarios set nombre=?, apellido=? where id=?";		
		return template.update(sql, obj.getNombre(), obj.getApellido(), obj.getId());
	}
	
	@Override
	public int editDatos(Usuarios obj) {
		String sql="update usuarios set gasto_total=?, ingreso_total=?, saldo_general=? where id=?";		
		return template.update(sql, obj.getGasto_total(), obj.getIngreso_total(), obj.getSaldo_general(), obj.getId());
	}

	@Override
	public int delete(int id) {
		String sql="delete from usuarios where id=?";
		return template.update(sql, id);
	}

}
