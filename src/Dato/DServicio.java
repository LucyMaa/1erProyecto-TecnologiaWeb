package Dato;

import DB.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DServicio {
    private Conexion con;

    public DServicio() {
        this.con = Conexion.getInstancia();
    }
    
    public ArrayList<Servicio> listar(int id){
        ArrayList<Servicio> lista = new ArrayList<>();
        try {
            String query = (id == 0) ? "SELECT servicios.id, direccion, codigo, tipo, estado, plans.nombre as plan,users.name as user,clientes.nombre as cliente FROM public.servicios \n" +
"INNER JOIN plans ON plans.id = servicios.plan_id INNER JOIN users ON users.id = servicios.user_id INNER JOIN clientes ON clientes.id=servicios.cliente_id" 
                                       : "SELECT servicios.id, direccion, codigo, tipo, estado, plans.nombre as plan,users.name as user,clientes.nombre as cliente FROM public.servicios \n" +
"INNER JOIN plans ON plans.id = servicios.plan_id INNER JOIN users ON users.id = servicios.user_id INNER JOIN clientes ON clientes.id=servicios.cliente_id WHERE servicios.id = "+id;
            PreparedStatement pre = con.conectar().prepareStatement(query);
            ResultSet result = pre.executeQuery();
            while(result.next()){
                lista.add(new Servicio(result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6),result.getString(7),result.getString(8)));
                
            }
            pre.close();
        } catch (Exception e) {
            System.out.println("Error DServicio "+e);
        }finally{
            con.desconectar();
        }
        return lista;
    }
    
    public boolean crear(String direccion, String codigo, String tipo, String estado, int plan_id,int user_id,int cliente_id){
        try {
            String query = "INSERT INTO public.servicios(direccion, codigo, tipo, estado, plan_id,user_id,cliente_id)VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pre = con.conectar().prepareStatement(query);
                pre.setString(1, direccion);
                pre.setString(2, codigo);
                pre.setString(3, tipo);
                pre.setString(4, estado);
                pre.setInt(5, plan_id);
                pre.setInt(6, user_id);
                pre.setInt(7, cliente_id);
                pre.execute();
            return true;
        } catch (Exception e) {
            System.out.println("Error DServicio crear: "+e);
        }finally{
            con.desconectar();
        }
        return false;
    }
    
    public boolean editar(int id, String direccion, String codigo, String tipo, String estado, int plan_id,int user_id,int cliente_id){
        try {
            String query = "UPDATE public.servicios SET direccion=?, codigo=?, tipo=?, estado=?, plan_id=?,user_id=?,cliente_id=? WHERE servicios.id = ?";
            PreparedStatement pre = con.conectar().prepareStatement(query);
            
            pre.setString(1, direccion);
            pre.setString(2, codigo);
            pre.setString(3, tipo);
            pre.setString(4, estado);
            pre.setInt(5, plan_id);
            pre.setInt(6, user_id);
            pre.setInt(7, cliente_id);
            pre.setInt(8, id);
            int res = pre.executeUpdate();
            pre.close();
            return res == 1;
        } catch (Exception e) {
            System.out.println("Error DServicio editar: "+e);
        }finally{
            con.desconectar();
        }
        return false; 
    }
    
    public boolean eliminar(int id){
        String query = "DELETE FROM public.servicios WHERE servicios.id = ?";
        try {
            if(listar(id).isEmpty()){
                return false;
            }
            PreparedStatement pre = con.conectar().prepareStatement(query);
            pre.setInt(1, id);
            pre.execute();
            pre.close();
            return true;
        } catch (Exception e) {
            System.out.println("Error DServicio eliminar: "+e);
        }finally{
            con.desconectar();
        }
        return false;
    }
}
