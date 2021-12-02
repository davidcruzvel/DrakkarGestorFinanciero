package com.mrjowade.drakkar_gestor_financiero.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "usuarios")
@Entity
public class Usuarios {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private int id;
	@Column
	private String correo;
	@Column
	private String contrasenia;
	@Column
	private String nombre;
	@Column
	private String apellido;
	@Column
	private String gasto_total;
	@Column
	private String ingreso_total;
	@Column
	private String saldo_general;
	@Column
	private String codigo;
	@Column
	private int activo;
	
	public Usuarios() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getContrasenia() {
		return contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getGasto_total() {
		return gasto_total;
	}

	public void setGasto_total(String gasto_total) {
		this.gasto_total = gasto_total;
	}

	public String getIngreso_total() {
		return ingreso_total;
	}

	public void setIngreso_total(String ingreso_total) {
		this.ingreso_total = ingreso_total;
	}

	public String getSaldo_general() {
		return saldo_general;
	}

	public void setSaldo_general(String saldo_general) {
		this.saldo_general = saldo_general;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo_ver) {
		this.codigo = codigo_ver;
	}

	public int getActivo() {
		return activo;
	}

	public void setActivo(int activo) {
		this.activo = activo;
	}

}
