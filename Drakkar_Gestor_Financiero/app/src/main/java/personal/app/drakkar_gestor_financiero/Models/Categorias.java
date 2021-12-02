package personal.app.drakkar_gestor_financiero.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Categorias { //modelo de la entidad categorias.

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("idusuario")
    @Expose
    private int idusuario;

    @SerializedName("categoria")
    @Expose
    private String categoria;

    @SerializedName("tipo")
    @Expose
    private String tipo;

    public Categorias(){

    }

    public Categorias(int id, int idusuario, String categoria, String tipo) {
        this.id = id;
        this.idusuario = idusuario;
        this.categoria = categoria;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
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
}
