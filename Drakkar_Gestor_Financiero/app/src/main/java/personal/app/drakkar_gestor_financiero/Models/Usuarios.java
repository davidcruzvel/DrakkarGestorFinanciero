package personal.app.drakkar_gestor_financiero.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Usuarios { //modelo de la entidad usuarios.

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("correo")
    @Expose
    private String correo;

    @SerializedName("contrasenia")
    @Expose
    private String contrasenia;

    @SerializedName("nombre")
    @Expose
    private String nombre;

    @SerializedName("apellido")
    @Expose
    private String apellido;

    @SerializedName("gasto_total")
    @Expose
    private String gasto_total;

    @SerializedName("ingreso_total")
    @Expose
    private String ingreso_total;

    @SerializedName("saldo_general")
    @Expose
    private String saldo_general;

    public Usuarios(){

    }

    public Usuarios(int id, String correo, String contrasenia, String nombre, String apellido, String gasto_total, String ingreso_total, String saldo_general) {
        this.id = id;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.nombre = nombre;
        this.apellido = apellido;
        this.gasto_total = gasto_total;
        this.ingreso_total = ingreso_total;
        this.saldo_general = saldo_general;
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

}
