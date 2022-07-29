package Negocio;

import Dato.DServicio;
import Dato.Servicio;
import java.util.ArrayList;

public class NServicio {
    private DServicio dservicio;

    public NServicio() {
        this.dservicio = new DServicio();
    }
    
    public String listar(String id){
        String mensaje = "Error de parametros tiene: "+id+" deberia ser numerico";
        if(esNumero(id)){
            ArrayList<Servicio> lista = dservicio.listar(Integer.valueOf(id));
            if(!lista.isEmpty()){
                String res = "<h2> Lista de Servicios </h2>\n"
                        + "<table border=1>\n"
                        + "<tr>"
                        + "<td align=\"center\"valign=\"top\"  bgcolor=\"#7FFFD4\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 800; line-height: 24px; padding: 10px;\">ID</td>"
                        + "<td align=\"center\"valign=\"top\"  bgcolor=\"#7FFFD4\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 800; line-height: 24px; padding: 10px;\">Direccion</td>"
                        + "<td align=\"center\"valign=\"top\"  bgcolor=\"#7FFFD4\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 800; line-height: 24px; padding: 10px;\">Codigo</td>"
                        + "<td align=\"center\"valign=\"top\"  bgcolor=\"#7FFFD4\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 800; line-height: 24px; padding: 10px;\">Tipo</td>"
                        + "<td align=\"center\"valign=\"top\"  bgcolor=\"#7FFFD4\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 800; line-height: 24px; padding: 10px;\">Estado</td>"
                        + "<td align=\"center\"valign=\"top\"  bgcolor=\"#7FFFD4\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 800; line-height: 24px; padding: 10px;\">Plan</td>"
                        + "<td align=\"center\"valign=\"top\"  bgcolor=\"#7FFFD4\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 800; line-height: 24px; padding: 10px;\">Usuario</td>"
                        + "<td align=\"center\"valign=\"top\"  bgcolor=\"#7FFFD4\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 800; line-height: 24px; padding: 10px;\">Cliente</td>"
                        + "</tr>\n";
                for (Servicio servicio : lista) {
                    res += servicio.toString();
                }
                res+="</table>";
                return res;
            }else{
                mensaje = "El servicio no fuen encontrado";
            }
        }
        return mensaje;
    }
    
    public String crear(String[] parametros){
        String mensaje = "Error de parametros tiene: "+parametros.length+" deberia ser 7";
        if(parametros.length == 7 ){
            if(this.dservicio.crear(parametros[0], parametros[1], parametros[2], parametros[3], Integer.valueOf(parametros[4]),Integer.valueOf(parametros[5]),Integer.valueOf(parametros[6]))){
                return successMessage("Servicio registrado con exito");
            }
            mensaje = "Error al insertar el servicio";
        }
        return errorMessage(mensaje);
    }
    
    public String editar(String[] parametros){
        String mensaje = "Error de parametros tiene: "+parametros.length+" deberia ser solo 8";
        if(parametros.length == 8 && esNumero(parametros[0])){
            if(dservicio.editar(Integer.valueOf(parametros[0]), parametros[1], parametros[2], parametros[3], parametros[4], Integer.valueOf(parametros[5]),Integer.valueOf(parametros[6]),Integer.valueOf(parametros[7]))){
                return successMessage("Servicio editado exitosamente");
            }
            mensaje = "Error al editar el servicio";
        }
        return errorMessage(mensaje);
    }
    
    public String eliminar(String id){
        String mensaje = "Error de parametros tiene: "+id+" deberia ser numerico";
        if(esNumero(id)){
            if(this.dservicio.eliminar(Integer.valueOf(id))){
                return successMessage("Servicio eliminado exitosamente!");
            }
            mensaje = "Error al eliminar el servicio";
        }
        return errorMessage(mensaje);
    }
    
    public boolean esNumero(String prt_parametros) {
        try {
            Integer.parseInt(prt_parametros);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public String errorMessage(String parametro) {
        return "<div><strong>ERROR</strong><p>" + parametro + "</p></div>";
    }

    public String successMessage(String parametro) {
        return "<div><strong>EXITO</strong><p>" + parametro + "</p></div>";
    }
}
