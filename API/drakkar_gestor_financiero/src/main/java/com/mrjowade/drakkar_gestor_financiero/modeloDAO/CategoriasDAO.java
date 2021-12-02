package com.mrjowade.drakkar_gestor_financiero.modeloDAO;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.mrjowade.drakkar_gestor_financiero.interfaces.CategoriasInterface;
import com.mrjowade.drakkar_gestor_financiero.modelo.Categoria;


@Repository
public class CategoriasDAO implements CategoriasInterface {

	@Autowired
	JdbcTemplate template;

	@Override
	public List<Map<String, Object>> listar() {
		List<Map<String, Object>> list = template.queryForList("select * from categorias order by categoria");
		return list;
	}

	@Override
	public List<Map<String, Object>> listarGastos(int id) {
		List<Map<String, Object>> list = template.queryForList("select * from categorias where idusuario=? and tipo=1  order by categoria", id);
		return list;
	}
	
	@Override
	public List<Map<String, Object>> listarIngresos(int id) {
		List<Map<String, Object>> list = template.queryForList("select * from categorias where idusuario=? and tipo=2  order by categoria", id);
		return list;
	}

	@Override
	public int add(Categoria obj) {
		String sql = "insert into categorias(idusuario, categoria, tipo) values(?, ?, ?)";
		return template.update(sql, obj.getIdusuario(), obj.getCategoria(), obj.getTipo());
	}

	@Override
	public int edit(Categoria obj) {
		String sql="update categorias set categoria=? where id=?";		
		return template.update(sql, obj.getCategoria(), obj.getId());
	}

	@Override
	public int delete(int id) {
		String sql="delete from categorias where id=?";
		return template.update(sql, id);
	}

}
