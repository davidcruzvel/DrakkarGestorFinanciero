package personal.app.drakkar_gestor_financiero.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Transacciones { //modelo de la entidad transacciones.

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("idcategoria")
    @Expose
    private int idcategoria;

    @SerializedName("categoria")
    @Expose
    private String categoria;

    @SerializedName("tipo")
    @Expose
    private String tipo;

    @SerializedName("monto")
    @Expose
    private String monto;

    @SerializedName("descripcion")
    @Expose
    private String descripcion;

    @SerializedName("fecha")
    @Expose
    private String fecha;

    public Transacciones() {

    }

    public Transacciones(int id, int idcategoria, String categoria, String tipo, String monto, String descripcion, String fecha) {
        this.id = id;
        this.idcategoria = idcategoria;
        this.categoria = categoria;
        this.tipo = tipo;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdcategoria() {
        return idcategoria;
    }

    public void setIdcategoria(int idcategoria) {
        this.idcategoria = idcategoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

}
