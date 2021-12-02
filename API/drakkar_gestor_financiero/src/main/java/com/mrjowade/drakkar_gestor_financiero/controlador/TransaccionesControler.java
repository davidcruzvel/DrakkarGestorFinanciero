package com.mrjowade.drakkar_gestor_financiero.controlador;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mrjowade.drakkar_gestor_financiero.modelo.Transaccion;
import com.mrjowade.drakkar_gestor_financiero.service.TransaccionesService;

@RestController
@RequestMapping("/transacciones")
public class TransaccionesControler {
	
	@Autowired
	private TransaccionesService service;
	
	@GetMapping("/listar/{id}/{desde}/{hasta}")
	public List<Map<String, Object>> listar(@PathVariable int id, @PathVariable String desde, @PathVariable String hasta, Model model) {
		return service.listar(id, desde, hasta);
	}
	
	@GetMapping("/listarUltTra/{id}")
	public List<Map<String, Object>> listarUltTra(@PathVariable int id, Model model) {
		return service.listarUltTra(id);
	}
	
	@GetMapping("/listarId/{id}")
	public List<Map<String, Object>> listarId(@PathVariable int id, Model model) {
		return service.listarId(id);
	}
		
	@GetMapping("/estadisticaBc1/{id}/{desde}/{hasta}")
	public List<Map<String, Object>> estadisticaBc1(@PathVariable int id, @PathVariable String desde, @PathVariable String hasta, Model model) {
		return service.estadisticaBc1(id, desde, hasta);
	}
	
	@GetMapping("/estadisticaBc2Bc3/{id}/{desde}/{hasta}/{tipo}")
	public List<Map<String, Object>> estadisticaBc2Bc3(@PathVariable int id, @PathVariable String desde, @PathVariable String hasta, @PathVariable int tipo, Model model) {
		return service.estadisticaBc2Bc3(id, desde, hasta, tipo);
	}
	
	@PostMapping("/agregar")
	public String add(@RequestBody Transaccion obj, Model model) {
		int r = service.add(obj);
		if(r == 0) {
			return "2";
		} else {
			return "1";	
		}
	}
	
	@PostMapping("/actualizar")
	public String edit(@RequestBody Transaccion obj, Model model) {
		int r = service.edit(obj);
		if(r == 0){
			return "2";
		} else {
			return "1";	
		}
	}
	
	@PostMapping("/eliminar/{id}")
	public String delete(@PathVariable int id, Model model) {
		int r = service.delete(id);
		if(r == 0) {
			return "2";
		} else {
			return "1";	
		}
	}
	
}
