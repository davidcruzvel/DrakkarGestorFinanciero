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
import com.mrjowade.drakkar_gestor_financiero.modelo.Categoria;
import com.mrjowade.drakkar_gestor_financiero.service.CategoriasService;

@RestController
@RequestMapping("/categorias")
public class CategoriasControler {
	
	@Autowired
	private CategoriasService service;
	
	@GetMapping("/listar")
	public List<Map<String, Object>> listar(Model model) {
		return service.listar();
	}
	
	@GetMapping("/listarGastos/{id}")
	public List<Map<String, Object>> listarGastos(@PathVariable int id, Model model) {
		return service.listarGastos(id);
	}
	
	@GetMapping("/listarIngresos/{id}")
	public List<Map<String, Object>> listarIngresos(@PathVariable int id, Model model) {
		return service.listarIngresos(id);
	}
		
	@PostMapping("/agregar")
	public String add(@RequestBody Categoria obj, Model model) {
		int id = service.add(obj);
		if(id == 0) {
			return "2";
		} else {
			return "1";	
		}
	}
	
	@PostMapping("/actualizar")
	public String edit(@RequestBody Categoria obj, Model model) {
		int r = service.edit(obj);
		if(r == 0) {
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
