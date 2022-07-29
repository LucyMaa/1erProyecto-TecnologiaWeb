package Dato;

import DB.Conexion;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DUsuario {
    private Conexion con;

    public DUsuario() {
        this.con = Conexion.getInstancia();
    }
    
    public ArrayList<Usuario> listar(int id){
        ArrayList<Usuario> lista = new ArrayList<>();
        try {
            String query = (id == 0) ?"SELECT users.id, users.name, users.apellido, users.ci, users.email, users.rol	"
                    + "FROM users;" 
                    : "SELECT users.id, users.name, users.apellido, users.ci, users.email, users.rol "
                    + "FROM users WHERE users.id = "+id;
            PreparedStatement pre = con.conectar().prepareStatement(query);
            ResultSet result = pre.executeQuery();
            while(result.next()){
                lista.add(new Usuario(result.getInt(1), result.getString(2), result.getString(3), result.getInt(4), result.getString(5), result.getString(6)));
            }
            pre.close();
        } catch (Exception e) {
            System.out.println("Error DUsuario : "+e);
        }
        finally{
            con.desconectar();
        }
        return lista;
    }
    
    public boolean crear(String nombre, String apellido, int ci, String correo, String password,String rol){
        String query = "insert into users (name,apellido,ci,email,password,rol) values(?,?,?,?,?,?)";
        try {
            PreparedStatement pre = con.conectar().prepareStatement(query);
            pre.setString(1, nombre);
            pre.setString(2, apellido);
            pre.setInt(3, ci);
            pre.setString(4, correo);
            pre.setString(5, password);
            pre.setString(6, rol);
            pre.execute();
            pre.close();
            return true;
        } catch (Exception e) {
            System.out.println("Error DUsuario crear : "+e);
        }finally{
            con.desconectar();
        }
        return false;
    }
    
    public ArrayList<Usuario> buscarCorreo(String correo){
        System.out.println(correo);
        ArrayList<Usuario> lista = new ArrayList<>();
        try{
            String query = "SELECT id, name, apellido, rol FROM users WHERE email = ?";
            PreparedStatement pre = con.conectar().prepareStatement(query);
            pre.setString(1, correo);
            ResultSet result = pre.executeQuery();
            while(result.next()){
               lista.add(new Usuario(result.getInt(1), result.getString(2), result.getString(3), result.getString(4)));
            }
            pre.close();
        }catch(Exception e){
            System.out.println("Error DUsuario buscar : "+e);
        }finally{
            con.desconectar();
        }
        return lista;
    }
    
    public boolean editar(int id, String nombre, String apellido, int ci, String email, String password,String rol){
        String query = "UPDATE users SET name = ?, apellido = ?, ci = ?, email = ?, password = ?, rol = ?  WHERE id = ? ";
        try {      
            PreparedStatement pre = con.conectar().prepareStatement(query);
            pre.setString(1, nombre);
            pre.setString(2, apellido);
            pre.setInt(3, ci);
            pre.setString(4, email);
            pre.setString(5, password);
            pre.setString(6, rol);
            pre.setInt(7, id);
            int res = pre.executeUpdate();
            pre.close();
            return res == 1;
        } catch (Exception e) {
            System.out.println("Error DUsuario editar : "+e);
        }finally{
            con.desconectar();
        }
        return false;
    }
    
    public boolean eliminar(int id){
        String query = "delete from users where id = ?";
        try {
            if (listar(id).isEmpty()){
                return false;
            }
            PreparedStatement pre = con.conectar().prepareStatement(query);
            pre.setInt(1, id);
            pre.execute();
            pre.close();
            return true;
        } catch (Exception e) {
            System.out.println("Error DUsuario eliminar : "+e);
        }finally{
            con.desconectar();
        }
        return false;
    }
}
