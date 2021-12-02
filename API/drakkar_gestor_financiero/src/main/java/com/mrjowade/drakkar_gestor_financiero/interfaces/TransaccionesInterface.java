package com.mrjowade.drakkar_gestor_financiero.interfaces;

import java.util.List;
import java.util.Map;
import com.mrjowade.drakkar_gestor_financiero.modelo.Transaccion;

public interface TransaccionesInterface{
	public List<Map<String, Object>> listar(int id, String desde, String hasta);
	public List<Map<String, Object>> listarUltTra(int id);
	public List<Map<String, Object>> listarId(int id);
	public List<Map<String, Object>> estadisticaBc1(int id, String desde, String hasta);
	public List<Map<String, Object>> estadisticaBc2Bc3(int id, String desde, String hasta, int tipo);
	public int add(Transaccion obj);
	public int edit(Transaccion obj);
	public int delete(int id);

}
