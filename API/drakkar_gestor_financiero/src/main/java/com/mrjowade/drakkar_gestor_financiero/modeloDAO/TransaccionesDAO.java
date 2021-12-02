package com.mrjowade.drakkar_gestor_financiero.modeloDAO;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.mrjowade.drakkar_gestor_financiero.interfaces.TransaccionesInterface;
import com.mrjowade.drakkar_gestor_financiero.modelo.Transaccion;

@Repository
public class TransaccionesDAO implements TransaccionesInterface {

	@Autowired
	JdbcTemplate template;
	
	@Override
	public List<Map<String, Object>> listar(int id, String desde, String hasta) {
		List<Map<String, Object>> list = template.queryForList("select a.id, a.idcategoria, b.categoria, b.tipo, a.monto, a.descripcion, a.fecha from transacciones a, categorias b where a.idcategoria = b.id and b.idusuario=? and a.fecha between ? and ? order by a.id desc, a.fecha desc", id, desde, hasta);
		return list;
	}

	@Override
	public List<Map<String, Object>> listarUltTra(int id) {
		List<Map<String, Object>> list = template.queryForList("select a.id, a.idcategoria, b.categoria, b.tipo, a.monto, a.descripcion, a.fecha from transacciones a, categorias b where a.idcategoria = b.id and b.idusuario=? order by a.id desc, a.fecha desc limit 5", id);
		return list;
	}
	
	@Override
	public List<Map<String, Object>> listarId(int id) {
		List<Map<String, Object>> list = template.queryForList("select a.id, a.idcategoria, b.categoria, b.tipo, a.monto, a.descripcion, a.fecha from transacciones a, categorias b where a.idcategoria = b.id and a.id=?", id);
		return list;
	}
	
	@Override
	public List<Map<String, Object>> estadisticaBc1(int id, String desde, String hasta) {
		List<Map<String, Object>> list = template.queryForList("select b.tipo, sum(a.monto) as monto from transacciones a, categorias b where a.idcategoria = b.id and b.idusuario=? and a.fecha between ? and ? group by b.tipo", id, desde, hasta);
		return list;
	}
	
	@Override
	public List<Map<String, Object>> estadisticaBc2Bc3(int id, String desde, String hasta, int tipo) {
		List<Map<String, Object>> list = template.queryForList("select b.categoria, sum(a.monto) as monto from transacciones a, categorias b where a.idcategoria = b.id and b.idusuario=? and a.fecha between ? and ? and b.tipo=? group by b.categoria", id, desde, hasta, tipo);
		return list;
	}

	@Override
	public int add(Transaccion obj) {
		String sql = "insert into transacciones(idcategoria, monto, descripcion, fecha) values(?, ?, ?, ?)";
		return template.update(sql, obj.getIdcategoria(), obj.getMonto(), obj.getDescripcion(), obj.getFecha());
	}

	@Override
	public int edit(Transaccion obj) {
		String sql="update transacciones set idcategoria=?, monto=?, descripcion=?, fecha=? where id=?";		
		return template.update(sql, obj.getIdcategoria(), obj.getMonto(), obj.getDescripcion(), obj.getFecha(), obj.getId());
	}

	@Override
	public int delete(int id) {
		String sql="delete from transacciones where id=?";
		return template.update(sql, id);
	}

}
