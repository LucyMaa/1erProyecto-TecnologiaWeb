package Dato;

public class Almacen {
    private int id;
    private String direccion;

    public Almacen(int id, String direccion) {
        this.id = id;
        this.direccion= direccion;
    }

    public Almacen() {
    }

    @Override
    public String toString() {
        return "<tr>" + 
                "<td>" + this.id + "</td>" + 
                "<td>" + this.direccion + "</td>" + 
                "</tr>\n";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDirreccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }   
}
