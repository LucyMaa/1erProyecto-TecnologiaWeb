package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    
    private final String DRIVER = "org.postgresql.Driver";
    private final String DB = "db_grupo30sc";
    private final String USER = "grupo30sc";
    private final String PASSWORD  = "grup030grup030";
    private final String URL = "jdbc:postgresql://www.tecnoweb.org.bo/";
    
    private static Conexion instancia;
    private Connection con;
    
    private Conexion(){
        this.con = null;
    }
    
    public Connection conectar(){
        try {
            Class.forName(DRIVER);
            this.con = DriverManager.getConnection(this.URL+this.DB,this.USER,this.PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error DB: "+e);
        }
        return this.con;
    }
    
    public void desconectar(){
        try {
            this.con.close();
//            System.out.println("desconectado :"+(this.con==null));
        } catch (SQLException e) {
            System.out.println("Error DB: "+e);
        }
    }
    
    public static Conexion getInstancia(){
        if(instancia == null){
            instancia = new Conexion();
        }
        return instancia;
    }

}