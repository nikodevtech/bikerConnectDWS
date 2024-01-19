package com.bikerconnect.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.bikerconnect.dtos.UsuarioDTO;
import com.bikerconnect.servicios.IUsuarioServicio;

@Controller
public class RegistroControlador {
	
	@Autowired
	private IUsuarioServicio usuarioServicio;

	/**
	 * Gestiona la solicitud HTTP GET para la url /auth/registrar y mostrar la página de registro.
	 * @param model Modelo que se utiliza para enviar un usuarioDTO vacio a la vista.
	 * @return La vista de registro de usuario (registro.html).
	 */
	@GetMapping("/auth/crear-cuenta")
	public String registrarGet(Model model) {
		model.addAttribute("usuarioDTO", new UsuarioDTO());
		return "registro";
	}
	
	/**
	 * Procesa la solicitud HTTP POST para registro de un nuevo usuario.
	 * @param  usuarioDTO El objeto UsuarioDTO que recibe en el modelo y contiene los
	 *         datos del nuevo usuario.
	 * @return La vista de inicio de sesión (login.html) si fue exitoso el registro; 
	 * 		   de lo contrario, la vista de registro de usuario (registro.html).
	 */
	@PostMapping("/auth/crear-cuenta")
	public String registrarPost(@ModelAttribute UsuarioDTO usuarioDTO, Model model) {
		
		System.out.println(usuarioDTO);

		UsuarioDTO nuevoUsuario = usuarioServicio.registrarUsuario(usuarioDTO);
		
		if (nuevoUsuario != null) { // Si entra a este if es que el registro se completo correctamente
			model.addAttribute("mensajeRegistroExitoso", "Registro del nuevo usuario OK");
			return "login";
		} else { // De lo contrario, es que ya existe un usuario con el mismo email
			model.addAttribute("emailYaRegistrado", "Ya existe un usuario con ese email");
			return "registro";
		}
	}
	

}
