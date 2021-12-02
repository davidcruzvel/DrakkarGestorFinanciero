package com.mrjowade.drakkar_gestor_financiero.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mrjowade.drakkar_gestor_financiero.interfaces.TransaccionesInterface;
import com.mrjowade.drakkar_gestor_financiero.modelo.Transaccion;
import com.mrjowade.drakkar_gestor_financiero.modeloDAO.TransaccionesDAO;

@Service
public class TransaccionesService implements TransaccionesInterface {

	@Autowired
	TransaccionesDAO dao;
	
	@Override
	public List<Map<String, Object>> listar(int id, String desde, String hasta) {
		// TODO Auto-generated method stub
		return dao.listar(id, desde, hasta);
	}
	
	@Override
	public List<Map<String, Object>> listarUltTra(int id) {
		// TODO Auto-generated method stub
		return dao.listarUltTra(id);
	}
	
	@Override
	public List<Map<String, Object>> listarId(int id) {
		// TODO Auto-generated method stub
		return dao.listarId(id);
	}
	
	@Override
	public List<Map<String, Object>> estadisticaBc1(int id, String desde, String hasta) {
		// TODO Auto-generated method stub
		return dao.estadisticaBc1(id, desde, hasta);
	}
	
	@Override
	public List<Map<String, Object>> estadisticaBc2Bc3(int id, String desde, String hasta, int tipo) {
		// TODO Auto-generated method stub
		return dao.estadisticaBc2Bc3(id, desde, hasta, tipo);
	}

	@Override
	public int add(Transaccion obj) {		
		return dao.add(obj);
	}

	@Override
	public int edit(Transaccion obj) {
		// TODO Auto-generated method stub
		return dao.edit(obj);
	}

	@Override
	public int delete(int id) {
		// TODO Auto-generated method stub
		return dao.delete(id);
	}

}
