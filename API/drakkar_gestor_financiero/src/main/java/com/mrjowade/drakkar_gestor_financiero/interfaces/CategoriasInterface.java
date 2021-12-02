package com.mrjowade.drakkar_gestor_financiero.interfaces;

import java.util.List;
import java.util.Map;
import com.mrjowade.drakkar_gestor_financiero.modelo.Categoria;

public interface CategoriasInterface{
	public List<Map<String, Object>> listar();
	public List<Map<String, Object>> listarGastos(int id);
	public List<Map<String, Object>> listarIngresos(int id);
	public int add(Categoria obj);
	public int edit(Categoria obj);
	public int delete(int id);

}
