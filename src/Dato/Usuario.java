package Dato;

public class Usuario {

    private int id;
    private String name;
    private String apellido;
    private int ci;
    private String email;
    private String password;
    private String rol;

    public Usuario(int id, String nombre, String apellido, int ci, String correo, String password, String rol) {
        this.id = id;
        this.name = nombre;
        this.apellido = apellido;
        this.ci = ci;
        this.email = correo;
        this.password = password;
        this.rol = rol;
    }

    public Usuario(int id, String nombre, String apellido) {
        this.id = id;
        this.name = nombre;
        this.apellido = apellido;
    }
    
    public Usuario(int id, String nombre, String apellido,int ci,String email,String rol) {
      this.id = id;
        this.name = nombre;
        this.apellido = apellido;
        this.ci=ci;
        this.email=email;
        this.rol=rol;        
    }

    Usuario(int id, String name, String apellido,String rol) {
     this.id=id;
     this.name=name;
     this.apellido=apellido;
     this.rol=rol;
    }
    
    @Override
    public String toString() {
        return "<tr>"
                + "<td>" + this.id + "</td>"
                + "<td>" + this.name + "</td>"
                + "<td>" + this.apellido + "</td>"
                + "<td>" + this.ci + "</td>"
                + "<td>" + this.email + "</td>"
                + "<td>" + this.rol + "</td>"             
                + "</tr>\n";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return name;
    }

    public void setNombre(String nombre) {
        this.name = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getCi() {
        return ci;
    }

    public void setCi(int ci) {
        this.ci = ci;
    }

    public String getCorreo() {
        return email;
    }

    public void setCorreo(String correo) {
        this.email = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
    
}
