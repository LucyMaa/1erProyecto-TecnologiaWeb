package Dato;

public class Servicio {
    private int id;
    private String direccion;
    private String codigo;
    private String tipo;
    private String estado;
    private int plan_id;
    private String nombre_plan;
    private int user_id;
    private String nombre_user;
    private int cliente_id;
    private String nombre_cliente;
    

    public Servicio() {
    }

    public Servicio(int id, String direccion, String codigo, String tipo, String estado, String nombre_plan,String nombre_usuario,String nombre_cliente) {
        this.id = id;
        this.direccion = direccion;
        this.codigo = codigo;
        this.tipo = tipo;
        this.estado = estado;
        this.nombre_plan = nombre_plan;
        this.nombre_user=nombre_usuario;
        this.nombre_cliente=nombre_cliente;
    }

    public Servicio(int id, String direccion, String codigo, String tipo, String estado, int plan_id, String nombre_plan,int user_id,String nombre_user,int cliente_id,String nombre_cliente) {
        this.id = id;
        this.direccion = direccion;
        this.codigo = codigo;
        this.tipo = tipo;
        this.estado = estado;
        this.plan_id = plan_id;
        this.nombre_plan = nombre_plan;
        this.user_id=user_id;
        this.nombre_user=nombre_user;
        this.cliente_id=cliente_id;
        this.nombre_cliente=nombre_cliente;
    }

    @Override
    public String toString() {
        return "<tr>" + 
                "<td>" + this.id + "</td>" +
                "<td>" + this.direccion + "</td>" + 
                "<td>" + this.codigo + "</td>" + 
                "<td>" + this.tipo + "</td>" + 
                "<td>" + this.estado + "</td>" + 
                "<td>" + this.nombre_plan + "</td>" +
                "<td>" + this.nombre_user + "</td>" +
                "<td>" + this.nombre_cliente + "</td>" +
                "</tr>\n";
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(int plan_id) {
        this.plan_id = plan_id;
    }

    public String getNombre_plan() {
        return nombre_plan;
    }

    public void setNombre_plan(String nombre_plan) {
        this.nombre_plan = nombre_plan;
    }
    
     public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
     
    public String getNombre_user() {
        return nombre_user;
    }

    public void setNombre_user(String nombre_user) {
        this.nombre_user = nombre_user;
    }
    
         public int getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(int cliente_id) {
        this.cliente_id = cliente_id;
    }
     
    public String getNombre_cliente() {
        return nombre_cliente;
    }

    public void setNombre_cliente(String nombre_cliente) {
        this.nombre_cliente = nombre_cliente;
    }
}
