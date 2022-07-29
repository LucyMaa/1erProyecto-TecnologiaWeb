package Dato;

import DB.Conexion;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DActividad {
    private Conexion con;

    public DActividad() {
        this.con = Conexion.getInstancia();
    }
    
    public ArrayList<Actividad> listar(int id){
        ArrayList<Actividad> lista = new ArrayList<>();
        try {
            String query = (id == 0) ? "SELECT servicios.id,inicio, fin, foto, actividads.estado, servicios.codigo as servicio FROM public.actividads inner join servicios on servicios.id = actividads.servicio_id" : "SELECT servicios.id,inicio, fin, foto, actividads.estado, servicios.codigo as servicio FROM public.actividads inner join servicios on servicios.id = actividads.servicio_id where actividads.id ="+id;
            PreparedStatement pre = con.conectar().prepareStatement(query);
            ResultSet result = pre.executeQuery();
            while(result.next()){
                lista.add(new Actividad(result.getInt(1),result.getDate(2), result.getDate(3), result.getString(4), result.getString(5), result.getString(6)));
            }
            pre.close();
        } catch (Exception e) {
            System.out.println("Error DActividad: "+e);
        }finally{
            con.desconectar();
        }
        return lista;
    }
    
    public boolean registrarActividad(Date inicio, Date fin, String foto, String estado,int servicio_id){
        String query = "INSERT INTO public.actividads(inicio, fin, foto, estado, servicio_id) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pre = con.conectar().prepareStatement(query);
            pre.setDate(1, inicio);
            pre.setDate(2, fin);
            pre.setString(3, foto);
            pre.setString(4, estado);
            pre.setInt(5, servicio_id);
            pre.execute();
            pre.close();
            return true;
        } catch (Exception e) {
            System.out.println("Error DActividad crear: "+e);
        }finally{
            con.desconectar();
        }
        return false;
    }
    
    public boolean editar(int id, Date inicio, Date fin, String foto, String estado, int servicio_id){
        String query = "UPDATE public.actividads SET inicio=?, fin=?, foto=?, estado=?, servicio_id=? WHERE actividads.id = ?";
        try {
            PreparedStatement pre = con.conectar().prepareStatement(query);
            pre.setDate(1, inicio);
            pre.setDate(2, fin);
            pre.setString(3, foto);
            pre.setString(4, estado);
            pre.setInt(5, servicio_id);
            pre.setInt(6, id);
            int res = pre.executeUpdate();
            pre.close();
            return res == 1;
        } catch (Exception e) {
            System.out.println("Error DActividad editar: "+e);
        }finally{
            con.desconectar();
        }
        return false;
    }
    
    public boolean eliminar(int id){
        String query = "DELETE FROM public.actividads WHERE actividads.id = ?";
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
            System.out.println("Error DActividad eliminar: "+e);
        }finally{
            con.desconectar();
        }
        return false;
    }
}
