package Presentacion;

import Dato.Usuario;
import Negocio.NActividad;
import Negocio.NCliente;
import Negocio.NUsuario;
import Negocio.NAlmacen;
import Negocio.NCategoria;
import Negocio.NProducto;
import Negocio.NPromocion;
import Negocio.NPlan;
import Negocio.NReporte;
import Negocio.NServicio;
import Negocio.NViatico;
import java.util.ArrayList;
import java.util.List;

public class Manejador {

    private int max = 0;
    private POP popmessage;
    private SMTP smtpmessage;

    private String correo_origen = "";

    private String comando = "";
    private String parametros = "";

    private int id;
    private String Nombre;
    private String Apellido;
    private String rol="";

    public Manejador() {
        popmessage = new POP();
        max = popmessage.getSize();
    }

    public void leer() {
        popmessage = new POP();
        if (popmessage.getSize() > max) {
            max++;
            boolean estado = analizarLineasSi(popmessage.getMessageArray(max));
            if (estado) {
                String personal = this.id + " - " + this.Nombre + " " + this.Apellido + " rol = " + this.rol;
                System.out.println(personal);
                ejecutarMetodos(this.comando, this.parametros, this.rol, this.correo_origen, personal);
            } else {
                System.out.println("Lo siento no se pudo mandar no se encontro el metodo.. \r\n");
            }
        }
        popmessage.cerrar();
    }

    private String getCorreoEmisor(String lineaUsuario) {
        //posiciones para usuario mail
        int posIni1 = lineaUsuario.indexOf("<");
        int posFin1 = lineaUsuario.indexOf(">");
        return lineaUsuario.substring(posIni1 + 1, posFin1);
    }

    private void enviarMensajeCorreoOrigen(String prt_mailFrom, String prt_asunto, String prt_mensaje) {
        smtpmessage = new SMTP();
        smtpmessage.sendMessage("grupo30sc@tecnoweb.org.bo", prt_mailFrom, "Consulta de : " + prt_asunto, prt_mensaje);
        smtpmessage.cerrar();
    }

    private boolean analizarLineasSi(List<String> messageArray) {
        for (String line : messageArray) {
//            System.out.println(line);
            if (line.contains("Return-Path:")) {
                //guardar correo emisor
                correo_origen = getCorreoEmisor(line);
                //System.out.println(correo_origen);
                NUsuario nusuario = new NUsuario();
                ArrayList<Usuario> lista = nusuario.buscarCorreo(correo_origen);
                if (lista.isEmpty()) {
                    return false;
                }
                Usuario usuario = lista.get(0);
                this.id = usuario.getId();
                this.Nombre = usuario.getNombre();
                this.Apellido = usuario.getApellido();
                this.rol = usuario.getRol();
            }
            if (line.contains("Subject:")) {
                System.out.println(line);
                if (line.split(":")[1] == "" || line.split(":")[1] == " ") {
                    return false;
                }
                String subject = line.split(":")[1];
                if (subject.contains("[") && subject.contains("]")) {
                    subject = subject.substring(1);
                    System.out.println(subject);
                    this.comando = subject.split("\\[")[0];
                    if ("HELP".equals(comando)) {
                        return true;
                    }
                    if (validarComando(comando)) {
                        if (subject.split("\\[")[1].length() >= 2) {
                            this.parametros = subject.split("\\[")[1].split("\\]")[0];
                        } else {
                            this.parametros = "0";
                        }
                        return true;
                    }
                    return false;
                }
                return false;
            }
        }
        return false;
    }

    boolean validarComando(String comando) {
        String[] comandoGenerador = {
            "CLIENTE",
            "TELEFONO",
            "SERVICIO",
            "PLAN",
            "PROMOCION",
            "PRODUCTO",
            "ALMACEN",
            "CATEGORIA",
            "ACTIVIDADES",
            "VIATICO",
            "ROL",
            "USUARIO",
            "REPORTE1",
            "REPORTE2"
        };
        String[] opciones = {
            "LIST",
            "REG",
            "EDI",
            "DEL"
        };
        for (String coman : comandoGenerador) {
            for (String opcion : opciones) {
//                System.out.println(opcion+""+coman);
//                System.out.println(comando);
                if (comando.equals(opcion + "" + coman)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void ejecutarMetodos(String comando, String parametros, String rol, String prt_mailFrom, String personal) {
        System.out.println("El comando: " + comando);
        System.out.println("Los parametros: " + parametros);
        System.out.println("La direccion origen es: " + prt_mailFrom);
        switch (rol) {
            case "GERENTE":
                permisosGerente(comando, parametros, rol, prt_mailFrom, personal);
                break;
            case "TECNICO":
                permisosTecnico(comando, parametros, rol, prt_mailFrom, personal);
                break;    
        }
    }
    
    private void permisosTecnico(String comando, String parametros, String rol, String prt_mailFrom, String personal) {
        String resp = "";
        String[] arreglo;
        switch (comando) {
            case "HELP":
                enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(getMensajeAyuda(), personal));
                break;
            /*---------------GESTIONAR VIATICOS--------------*/
            case "LISTVIATICO":
                if (!parametros.contains(",")) {
                    NViatico listviatico = new NViatico();
                    resp = listviatico.listar(parametros);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "REGVIATICO":
                if (parametros.contains(",")) {
                    NViatico regviatico = new NViatico();
                    arreglo = parametros.split(",");
                    resp = regviatico.crear(arreglo);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "EDIVIATICO":
                if (parametros.contains(",")) {
                    NViatico editviatico = new NViatico();
                    arreglo = parametros.split(",");
                    resp = editviatico.editar(arreglo);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "DELVIATICO":
                if (!parametros.contains(",")) {
                    NViatico delviatico = new NViatico();
                    resp = delviatico.eliminar(parametros);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
        }
    }

    private void permisosGerente(String comando, String parametros, String rol, String prt_mailFrom, String personal) {
        String resp = "";
        String[] arreglo;
        switch (comando) {

//---------- USERS --------------------------------------------------------
            case "LISTUSUARIO":
                if (!parametros.contains(",")) {
                    NUsuario listusuario = new NUsuario();
                    resp = listusuario.listar(parametros);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "REGUSUARIO":
                if (parametros.contains(",")) {
                    NUsuario regusuario = new NUsuario();
                    arreglo = parametros.split(",");
                    resp = regusuario.crear(arreglo);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "EDIUSUARIO":
                if (parametros.contains(",")) {
                    NUsuario ediusuario = new NUsuario();
                    arreglo = parametros.split(",");
                    resp = ediusuario.editar(arreglo);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "DELUSUARIO":
                if (!parametros.contains(",")) {
                    NUsuario delusuario = new NUsuario();
                    resp = delusuario.eliminar(parametros);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;

//---------- CLIENTE ----------------------------------------------------
            case "LISTCLIENTE":
                if (!parametros.contains(",")) {
                    NCliente listcliente = new NCliente();
                    resp = listcliente.listar(parametros);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "REGCLIENTE":
                if (parametros.contains(",")) {
                    NCliente regcliente = new NCliente();
                    arreglo = parametros.split(",");
                    resp = regcliente.crear(arreglo);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "EDICLIENTE":
                if (parametros.contains(",")) {
                    NCliente edicliente = new NCliente();
                    arreglo = parametros.split(",");
                    resp = edicliente.editar(arreglo);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "DELCLIENTE":
                if (!parametros.contains(",")) {
                    NCliente delcliente = new NCliente();
                    resp = delcliente.eliminar(parametros);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
                
//-------------------ALMACEN-----------------------------------
            case "LISTALMACEN":
                if (!parametros.contains(",")) {
                    NAlmacen listalmacen = new NAlmacen();
                    resp = listalmacen.listar(parametros);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "REGALMACEN":
                if (!parametros.contains(",")) {
                    NAlmacen regalmacen = new NAlmacen();
                    arreglo = parametros.split(",");
                    resp = regalmacen.crear(arreglo);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "EDIALMACEN":
                if (parametros.contains(",")) {
                    NAlmacen edialmacen = new NAlmacen();
                    arreglo = parametros.split(",");
                    resp = edialmacen.editar(arreglo);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "DELALMACEN":
                if (!parametros.contains(",")) {
                    NAlmacen delalmacen = new NAlmacen();
                    resp = delalmacen.eliminar(parametros);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
//--------------------CATEGORIA-----------------------------------------
            case "LISTCATEGORIA":
                if (!parametros.contains(",")) {
                    NCategoria listcategoria = new NCategoria();
                    resp = listcategoria.listar(parametros);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "REGCATEGORIA":
                if (parametros.contains(",")) {
                    NCategoria regcategoria = new NCategoria();
                    arreglo = parametros.split(",");
                    resp = regcategoria.crear(arreglo);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "EDICATEGORIA":
                if (parametros.contains(",")) {
                    NCategoria edicategoria = new NCategoria();
                    arreglo = parametros.split(",");
                    resp = edicategoria.editar(arreglo);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "DELCATEGORIA":
                if (!parametros.contains(",")) {
                    NCategoria delcategoria = new NCategoria();
                    resp = delcategoria.eliminar(parametros);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
//---------------------PRODUCTO--------------------------------------
            case "LISTPRODUCTO":
                if (!parametros.contains(",")) {
                    NProducto listproducto = new NProducto();
                    resp = listproducto.listar(parametros);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "REGPRODUCTO":
                if (parametros.contains(",")) {
                    NProducto regproducto = new NProducto();
                    arreglo = parametros.split(",");
                    resp = regproducto.crear(arreglo);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "EDIPRODUCTO":
                if (parametros.contains(",")) {
                    NProducto ediproducto = new NProducto();
                    arreglo = parametros.split(",");
                    resp = ediproducto.editar(arreglo);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "DELPRODUCTO":
                if (!parametros.contains(",")) {
                    NProducto delproducto = new NProducto();
                    resp = delproducto.eliminar(parametros);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
//-----------------------PROMOCION---------------------------------------
            case "LISTPROMOCION":
                if (!parametros.contains(",")) {
                    NPromocion listpromocion = new NPromocion();
                    resp = listpromocion.listar(parametros);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "REGPROMOCION":
                if (parametros.contains(",")) {
                    NPromocion regpromocion = new NPromocion();
                    arreglo = parametros.split(",");
                    resp = regpromocion.crear(arreglo);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "EDIPROMOCION":
                if (parametros.contains(",")) {
                    NPromocion edipromocion = new NPromocion();
                    arreglo = parametros.split(",");
                    resp = edipromocion.editar(arreglo);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "DELPROMOCION":
                if (!parametros.contains(",")) {
                    NPromocion delpromocion = new NPromocion();
                    resp = delpromocion.eliminar(parametros);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
//-------------------------PLAN-------------------------
            case "LISTPLAN":
                if (!parametros.contains(",")) {
                    NPlan listplan = new NPlan();
                    resp = listplan.listar(parametros);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "REGPLAN":
                if (parametros.contains(",")) {
                    NPlan regplan = new NPlan();
                    arreglo = parametros.split(",");
                    resp = regplan.crear(arreglo);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "EDIPLAN":
                if (parametros.contains(",")) {
                    NPlan ediplan = new NPlan();
                    arreglo = parametros.split(",");
                    resp = ediplan.editar(arreglo);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "DELPLAN":
                if (!parametros.contains(",")) {
                    NPlan delplan = new NPlan();
                    resp = delplan.eliminar(parametros);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            /*---------------------------------- ACTIVIDAD ------------------------------------------------------------------------------------*/
            case "LISTACTIVIDADES":
                if (!parametros.contains(",")) {
                    NActividad listactividad = new NActividad();
                    resp = listactividad.listar(parametros);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "REGACTIVIDADES":
                if (parametros.contains(",")) {
                    NActividad regactividad = new NActividad();
                    arreglo = parametros.split(",");
                    resp = regactividad.crear(arreglo);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "EDIACTIVIDADES":
                if (parametros.contains(",")) {
                    NActividad editactividad = new NActividad();
                    arreglo = parametros.split(",");
                    resp = editactividad.editar(arreglo);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "DELACTIVIDADES":
                if (!parametros.contains(",")) {
                    NActividad deltactividad = new NActividad();
                    resp = deltactividad.eliminar(parametros);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;

            /*------------------------------------------ SERVICIOS ----------------------------------------------------------------------*/
            case "LISTSERVICIO":
                if (!parametros.contains(",")) {
                    NServicio listservicio = new NServicio();
                    resp = listservicio.listar(parametros);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "REGSERVICIO":
                if (parametros.contains(",")) {
                    NServicio regservicio = new NServicio();
                    arreglo = parametros.split(",");
                    resp = regservicio.crear(arreglo);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "EDISERVICIO":
                if (parametros.contains(",")) {
                    NServicio editservicio = new NServicio();
                    arreglo = parametros.split(",");
                    resp = editservicio.editar(arreglo);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "DELSERVICIO":
                if (!parametros.contains(",")) {
                    NServicio delservicio = new NServicio();
                    resp = delservicio.eliminar(parametros);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            /*------------------------------------ REPORTES -------------------------------------------*/
            case "LISTREPORTE1":
                if (!parametros.contains(",")) {
                    NReporte reporte1 = new NReporte();
                    resp = reporte1.reporteServicioPlan();
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "LISTREPORTE2":
                if (!parametros.contains(",")) {
                    NReporte reporte2 = new NReporte();
                    resp = reporte2.resporteSignacionTecnico();
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            /*---------------GESTIONAR VIATICOS--------------*/
            case "LISTVIATICO":
                if (!parametros.contains(",")) {
                    NViatico listviatico = new NViatico();
                    resp = listviatico.listar(parametros);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "REGVIATICO":
                if (parametros.contains(",")) {
                    NViatico regviatico = new NViatico();
                    arreglo = parametros.split(",");
                    resp = regviatico.crear(arreglo);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "EDIVIATICO":
                if (parametros.contains(",")) {
                    NViatico editviatico = new NViatico();
                    arreglo = parametros.split(",");
                    resp = editviatico.editar(arreglo);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "DELVIATICO":
                if (!parametros.contains(",")) {
                    NViatico delviatico = new NViatico();
                    resp = delviatico.eliminar(parametros);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "HELP":
                enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(getMensajeAyuda(), personal));
                break;
        }
    }

    private void permisosTecnico(String comando, String parametros, int rol, String prt_mailFrom, String personal) {
        String resp = "";
        String[] arreglo;
        switch (comando){
        /*---------------------------------- ACTIVIDAD ------------------------------------------------------------------------------------*/
            case "LISTACTIVIDADES":
                if (!parametros.contains(",")) {
                    NActividad listactividad = new NActividad();
                    resp = listactividad.listar(parametros);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "REGACTIVIDADES":
                if (parametros.contains(",")) {
                    NActividad regactividad = new NActividad();
                    arreglo = parametros.split(",");
                    resp = regactividad.crear(arreglo);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "EDIACTIVIDADES":
                if (parametros.contains(",")) {
                    NActividad editactividad = new NActividad();
                    arreglo = parametros.split(",");
                    resp = editactividad.editar(arreglo);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "DELACTIVIDADES":
                if (!parametros.contains(",")) {
                    NActividad deltactividad = new NActividad();
                    resp = deltactividad.eliminar(parametros);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;

            /*---------------GESTIONAR VIATICOS--------------*/
            case "LISTVIATICO":
                if (!parametros.contains(",")) {
                    NViatico listviatico = new NViatico();
                    resp = listviatico.listar(parametros);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "REGVIATICO":
                if (parametros.contains(",")) {
                    NViatico regviatico = new NViatico();
                    arreglo = parametros.split(",");
                    resp = regviatico.crear(arreglo);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "EDIVIATICO":
                if (parametros.contains(",")) {
                    NViatico editviatico = new NViatico();
                    arreglo = parametros.split(",");
                    resp = editviatico.editar(arreglo);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
            case "DELVIATICO":
                if (!parametros.contains(",")) {
                    NViatico delviatico = new NViatico();
                    resp = delviatico.eliminar(parametros);
                    System.out.println(resp);
                    enviarMensajeCorreoOrigen(prt_mailFrom, comando + " + " + parametros, getMensajeRespuesta(resp, personal));
                }
                break;
        }
    }
    
    public String getMensajeAyuda(){
        String cad = "";
        cad += "<body>\n" +
"    <h1>Formato de ayuda:</h1>\n" +
"    <h3>HELP[]</h3>\n" +
"    <ul>\n" +
"        <li>LISTUSUARIO[]</li>\n" +
"        <li>REGUSUARIO[NAME,APELLIDO,CI,EMAIL,PASSWORD,ROL]</li>\n" +
"        <li>EDIUSUARIO[ID,NAME,APELLIDO,CI,EMAIL,PASSWORD,ROL]</li>\n" +
"        <li>DELUSUARIO[ID]</li>\n" +
"    </ul>\n" +
"    <ul>\n" +
"        <li>LISTCLIENTE[]</li>\n" +
"        <li>REGCLIENTE[NOMBRE,APELLIDO,CI,CORREO]</li>\n" +
"        <li>EDICLIENTE[ID,NOMBRE,APELLIDO,CI,CORREO]</li>\n" +
"        <li>DELCLIENTE[ID]</li>\n" +
"    </ul>\n" +
"    <ul>\n" +
"        <li>LISTPLAN[]</li>\n" +
"        <li>REGPLAN[NOMBRE,DESCRIPCION,TARIFA,PROMOCION_ID]</li>\n" +
"        <li>EDIPLAN[[ID,NOMBRE,DESCRIPCION,TARIFA,PROMOCION_ID]</li>\n" +
"        <li>DELPLAN[ID]</li>\n" +
"    </ul>\n" +
"    <ul>\n" +
"        <li>LISTALMACEN[]</li>\n" +
"        <li>REGALMACEN[DIRRECION]</li>\n" +
"        <li>EDIALMACEN[ID,DIRRECION]</li>\n" +
"        <li>DELALMACEN[ID]</li>\n" +
"    </ul>\n" +
"    <ul>\n" +
"        <li>LISTCATEGORIA[]</li>\n" +
"        <li>REGCATEGORIA[NOMBRE,DESCRIPCION]</li>\n" +
"        <li>EDICATEGORIA[ID,NOMBRE,DESCRIPCION]</li>\n" +
"        <li>DELCATEGORIA[ID]</li>\n" +
"    </ul>\n" +
"    <ul>\n" +
"        <li>LISTPRODUCTO[]</li>\n" +
"        <li>REGPRODUCTO[NOMBRE,CANTIDAD,CODIGO,ALMACEN_ID,CATEGORIA_ID,ACTIVIDAD_ID]</li>\n" +
"        <li>EDIPRODUCTO[ID,NOMBRE,CANTIDAD,CODIGO,ALMACEN_ID,CATEGORIA_ID,ACTIVIDAD_ID]</li>\n" +
"        <li>DELPRODUCTO[ID]</li>\n" +
"    </ul>\n" +
"    <ul>\n" +
"        <li>LISTSERVICIO[]</li>\n" +
"        <li>REGSERVICIO[DIRECCION,CODIGO,TIPO,ESTADO,PLAN_ID,USER_ID,CLIENTE_ID]</li>\n" +
"        <li>EDISERVICIO[ID,DIRECCION,CODIGO,TIPO,ESTADO,PLAN_ID,USER_ID,CLIENTE_ID]</li>\n" +
"        <li>DELSERVICIO[ID]</li>\n" +
"    </ul>\n" +
"    <ul>\n" +
"        <li>LISTPROMOCION[]</li>\n" +
"        <li>REGPROMOCION[NOMBRE,DESCUENTO,2022-07-11,FIN]</li>\n" +
"        <li>EDIPROMOCION[ID,NOMBRE,DESCUENTO,INICIO,FIN]</li>\n" +
"        <li>DELPROMOCION[ID]</li>\n" +
"    </ul>\n" +
"    <ul>\n" +
"        <li>LISTACTIVIDADES[]</li>\n" +
"        <li>REGACTIVIDADES[2022-07-11,FIN,FOTO,ESTADO,SERVICIO_ID]</li>\n" +
"        <li>EDIACTIVIDADES[ID,INICIO,FIN,FOTO,ESTADO,SERVICIO_ID]</li>\n" +
"        <li>DELACTIVIDADES[ID]</li>\n" +
"    </ul>\n" +
"    <ul>\n" +
"        <li>LISTVIATICO[]</li>\n" +
"        <li>REGVIATICO[MONTO,2022-07-11,RAZON,USER_ID]</li>\n" +
"        <li>EDIVIATICO[ID,MONTO,FECHA,RAZON,USER_ID]</li>\n" +
"        <li>DELVIATICO[ID]</li>\n" +
"    </ul>\n" +
"    <ul>\n" +
"        <li>LISTREPORTE1[]</li>\n" +
"    </ul>\n" +
"</body>";
        return cad;
    }

    private String getMensajeRespuesta(String res, String personal) {
        String estilo = "<link rel='stylesheet' href='https://codepen.io/ingyas/pen/NENBOm.css'>";
        return "Content-Type:text/html;\r\n<html>" + estilo + res + "</br><p>" + personal + "</p></html>";
    }

}
