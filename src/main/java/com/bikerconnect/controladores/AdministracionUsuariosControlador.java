package com.bikerconnect.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.bikerconnect.dtos.UsuarioDTO;
import com.bikerconnect.servicios.IUsuarioServicio;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Clase que ejerce de controlador de la vista de administracion de usuarios
 * para gestionar el CRUD de usuarios.
 */
@Controller
public class AdministracionUsuariosControlador {

    @Autowired
    private IUsuarioServicio usuarioServicio;

    /**
     * Gestiona la solicitud HTTP GET para la url /privada/administracion-usuarios
     * y muestra la página de administración de usuarios con el listado de usuarios.
     *
     * @param model           Modelo que se utiliza para enviar el listado de usuarios a la vista.
     * @param request         HttpServletRequest para comprobar el rol del usuario.
     * @param authentication  Objeto Authentication que contiene el username/email.
     * @return La vista de administración de usuarios (administracionUsuarios.html) si es rol user
     *         o la vista del dashboard si el usuario no es rol admin.
     */
    @GetMapping("/privada/administracion-usuarios")
    public String listadoUsuarios(Model model, HttpServletRequest request, Authentication authentication) {
        try {
            List<UsuarioDTO> usuarios = usuarioServicio.obtenerTodos();
            model.addAttribute("usuarios", usuarios);

            if (request.isUserInRole("ROLE_ADMIN")) {
                return "administracionUsuarios";
            }

            model.addAttribute("noAdmin", "No eres admin");
            model.addAttribute("nombreUsuario", authentication.getName());
            return "dashboard";
        } catch (Exception e) {
            model.addAttribute("Error", "Ocurrió un error al obtener la lista de usuarios");
            return "dashboard";
        }
    }

    /**
     * Gestiona la solicitud HTTP GET para la url /privada/eliminar-usuario/{id}
     * y elimina al usuario con el ID proporcionado.
     *
     * @param id     ID del usuario a eliminar.
     * @param model  Modelo que se utiliza para enviar mensajes y el listado actualizado a la vista.
     * @param request HttpServletRequest para comprobar el rol del usuario.
     * @return La vista de administración de usuarios con el resultado de la eliminación.
     */
    @GetMapping("/privada/eliminar-usuario/{id}")
    public String eliminarUsuario(@PathVariable Long id, Model model, HttpServletRequest request) {
        try {
            UsuarioDTO usuario = usuarioServicio.buscarPorId(id);
            List<UsuarioDTO> usuarios = usuarioServicio.obtenerTodos();

            if (request.isUserInRole("ROLE_ADMIN") && usuario.getRol().equals("ROLE_ADMIN")) {
                model.addAttribute("noSePuedeEliminar", "No se puede eliminar a un admin");
                model.addAttribute("usuarios", usuarios);
                return "administracionUsuarios";
            }

            usuarioServicio.eliminar(id);
            model.addAttribute("eliminacionCorrecta", "El usuario se ha eliminado correctamente");
            model.addAttribute("usuarios", usuarioServicio.obtenerTodos());
            return "administracionUsuarios";
        } catch (Exception e) {
            model.addAttribute("Error", "Ocurrió un error al eliminar el usuario");
            return "dashboard";
        }
    }

    /**
     * Gestiona la solicitud HTTP GET para la url /privada/editar-usuario/{id}
     * y muestra el formulario de edición del usuario con el ID proporcionado.
     *
     * @param id     ID del usuario a editar.
     * @param model  Modelo que se utiliza para enviar el usuario a editar a la vista.
     * @return La vista de editarUsuario con el formulario de edición.
     */
    @GetMapping("/privada/editar-usuario/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        try {
            UsuarioDTO usuarioDTO = usuarioServicio.buscarPorId(id);
            if (usuarioDTO == null) {
                return "administracionUsuarios";
            }
            model.addAttribute("usuarioDTO", usuarioDTO);
            return "editarUsuario";
        } catch (Exception e) {
            model.addAttribute("Error", "Ocurrió un error al obtener el usuario para editar");
            return "dashboard";
        }
    }

    /**
     * Gestiona la solicitud HTTP POST para la url /privada/procesar-editar
     * y procesa el formulario de edición del usuario.
     *
     * @param usuarioDTO UsuarioDTO con los datos editados.
     * @param model      Modelo que se utiliza para enviar mensajes y el listado actualizado a la vista.
     * @return La vista de administración de usuarios con el resultado de la edición.
     */
    @PostMapping("/privada/procesar-editar")
    public String procesarFormularioEdicion(@ModelAttribute("usuarioDTO") UsuarioDTO usuarioDTO, Model model) {
        try {
            usuarioServicio.actualizarUsuario(usuarioDTO);
            model.addAttribute("edicionCorrecta", "El Usuario se ha editado correctamente");
            model.addAttribute("usuarios", usuarioServicio.obtenerTodos());
            return "administracionUsuarios";
        } catch (Exception e) {
            model.addAttribute("Error", "Ocurrió un error al editar el usuario");
            return "dashboard";
        }
    }

}
