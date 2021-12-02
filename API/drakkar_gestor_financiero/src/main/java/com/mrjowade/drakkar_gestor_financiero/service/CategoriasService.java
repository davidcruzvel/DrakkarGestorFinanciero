package com.mrjowade.drakkar_gestor_financiero.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mrjowade.drakkar_gestor_financiero.interfaces.CategoriasInterface;
import com.mrjowade.drakkar_gestor_financiero.modelo.Categoria;
import com.mrjowade.drakkar_gestor_financiero.modeloDAO.CategoriasDAO;

@Service
public class CategoriasService implements CategoriasInterface {

	@Autowired
	CategoriasDAO dao;
	
	@Override
	public List<Map<String, Object>> listar() {		
		return dao.listar();
	}

	@Override
	public List<Map<String, Object>> listarGastos(int id) {
		// TODO Auto-generated method stub
		return dao.listarGastos(id);
	}
	
	@Override
	public List<Map<String, Object>> listarIngresos(int id) {
		// TODO Auto-generated method stub
		return dao.listarIngresos(id);
	}

	@Override
	public int add(Categoria obj) {		
		return dao.add(obj);
	}

	@Override
	public int edit(Categoria obj) {
		// TODO Auto-generated method stub
		return dao.edit(obj);
	}

	@Override
	public int delete(int id) {
		// TODO Auto-generated method stub
		return dao.delete(id);
	}

}
