package com.bikerconnect.servicios;

import java.util.List;

import com.bikerconnect.dtos.UsuarioDTO;
import com.bikerconnect.entidades.Usuario;

/**
 * Interfaz del servicio para la gestión de usuarios, donde se declaran los
 * métodos correspondientes que serán implementados.
 */
public interface IUsuarioServicio {
	
	/**
	 * Se registra a un usuario antes comprobando si ya se encuentra en la BBDD registrado el usuario
	 * @param userDTO El usuario a registrar
	 * @return El usuario registrado
	 */
	public UsuarioDTO registrarUsuario(UsuarioDTO userDTO);
	
	/**
	 * Inicia el proceso de recuperacion (generando token y vencimiento) con el email del usuario 
	 * @param emailUsuario El email del usuario que quiere recuperar la contraseña
	 * @return true si el proceso se ha iniciado correctamente, false en caso contrario
	 */
	public boolean iniciarProcesoRecuperacion(String emailUsuario); 
	
	/**
	 * Busca un usuario por su token de recuperación.
	 * @param token que identifica al usuario recibió un correo con la url y dicho token
	 * @return el usuario buscado
	 */
	public UsuarioDTO obtenerUsuarioPorToken(String token);

	
	/**
	 * Establece la nueva contraseña del usuario con el token
	 * @param usuario El usuario al que se le establecera la nueva contraseña
	 * @return true si el proceso se ha realizado correctamente, false en caso contrario
	 */
	public boolean modificarContraseñaConToken(UsuarioDTO usuario);
	



}
