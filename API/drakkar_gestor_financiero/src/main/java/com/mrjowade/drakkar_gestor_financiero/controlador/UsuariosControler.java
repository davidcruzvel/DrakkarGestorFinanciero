package com.mrjowade.drakkar_gestor_financiero.controlador;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.mrjowade.drakkar_gestor_financiero.modelo.Usuarios;
import com.mrjowade.drakkar_gestor_financiero.service.UsuariosService;
import net.bytebuddy.utility.RandomString;

@RestController
@RequestMapping("/usuarios")
public class UsuariosControler {
	
	@Autowired
	private UsuariosService service;
	
    @Autowired
    private JavaMailSender mailSender;
	
	@PostMapping("/iniciarsesion")
	public List<Map<String, Object>> iniciarsesion(@RequestBody Usuarios obj, Model model) {
		return service.iniciarsesion(obj);
	}
	
	@GetMapping("/listarId/{id}")
	public List<Map<String, Object>> listarId(@PathVariable int id, Model model) {
		return service.listarId(id);
	}
	
	@GetMapping("/listarCorreo/{correo}")
	public List<Map<String, Object>> listarCorreo(@PathVariable String correo, Model model) {
		return service.listarCorreo(correo);
	}
	
	@PostMapping("/cambiarContrasenia")
	public String cambiarContrasenia(@RequestBody Usuarios obj, Model model) {
		int r = service.cambiarContrasenia(obj);
		if(r == 0) {
			return "2";
		} else {
			return "1";	
		}
	}
	
	@PostMapping("/resetpasswordresult")
	public ModelAndView recuperarContrasenia(HttpServletRequest request, Model model) {
	    String codigo = request.getParameter("codigo");
	    String password = request.getParameter("password");
		int r = service.recuperarContrasenia(codigo, password);
		ModelAndView modelAndView = new ModelAndView();
		if(r == 0) {
	    	modelAndView.setViewName("reset_password_fail");
	        return modelAndView;
		} else {
	    	modelAndView.setViewName("reset_password_success");
	        return modelAndView;
		}
	}
	
	@GetMapping("/resetpassword/{codigo}")
	public ModelAndView verficarCodigoRecCon(@PathVariable String codigo) {
		List<Map<String, Object>> list = service.verficarCodigoRecCon(codigo);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("codigo", codigo);
	    if (list.isEmpty()) {
	    	modelAndView.setViewName("reset_password_fail");
	        return modelAndView;
	    }else{
	    	modelAndView.setViewName("reset_password");
	        return modelAndView;
	    }
	}
	
	@PostMapping("/contolvEnviarCorreo")
	public String contolvEnviarCorreo(@RequestBody Usuarios obj, Model model, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException{
		String randomCode = RandomString.make(64);
		obj.setCodigo(randomCode);
		int r = service.contolvEnviarCorreo(obj);
		if(r == 0) {
			return "2";
		} else {
			enviarCorreoRecCon(obj, obtenerURL(request));
			return "1";
		}
	}

	
	@GetMapping("/disponibilidadCorreo/{correo}")
	public String disponibilidadCorreo(@PathVariable String correo,  Model model) {
		List<Map<String, Object>> list = service.disponibilidadCorreo(correo);
	    if (list.isEmpty()) {
	    	return "2";
	    } else {
	    	return "1";
	    }
	}
	
	@GetMapping("/verify/{codigo}")
	public ModelAndView  verficarCodigo(@PathVariable String codigo) {
		List<Map<String, Object>> list = service.verficarCodigo(codigo);
		ModelAndView modelAndView = new ModelAndView();
	    if (list.isEmpty()) {
	        modelAndView.setViewName("verify_fail");
	        return modelAndView;
	    }else{
	    	service.activarUsuario(codigo);
	        modelAndView.setViewName("verify_success");
	        return modelAndView;
	    }
	}
		
	@PostMapping("/agregar")
	public String save(@RequestBody Usuarios obj, Model model, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException{
		String randomCode = RandomString.make(64);
		obj.setCodigo(randomCode);
		int r = service.add(obj);
		if(r == 0) {
			return "2";
		} else {
			enviarCorreoVerificacion(obj, obtenerURL(request));
			return "1";
		}
	}
	
	@PostMapping("/actualizar")
	public String edit(@RequestBody Usuarios obj, Model model) {
		int r = service.edit(obj);
		if(r == 0) {
			return "2";
		} else {
			return "1";	
		}
	}
	
	@PostMapping("/actualizarDatos")
	public String editDatos(@RequestBody Usuarios obj, Model model) {
		int r = service.editDatos(obj);
		if(r == 0) {
			return "2";
		} else {
			return "1";	
		}
	}
	
	@GetMapping("/eliminar/{id}")
	public String delete(@PathVariable int id, Model model) {
		int r = service.delete(id);
		if(r == 0) {
			return "2";
		} else {
			return "1";	
		}
	}
	
    private String obtenerURL(HttpServletRequest request) {
	    String siteURL = request.getRequestURL().toString();
	    return siteURL.replace(request.getServletPath(), "");
	}
    
    private void enviarCorreoVerificacion(Usuarios obj, String siteURL) throws MessagingException, UnsupportedEncodingException {
        String toAddress = obj.getCorreo();
        String fromAddress = "mr.jowade@gmail.com";
        String senderName = "Drakkar Financial Manager Team";
        String subject = "Account verification";
        String content = "<h1>Dear [[name]]</h1>\n"
        		+ "<p>Please click the link below to verify your account:</p>\n"
        		+ "<h3><a href=\"[[URL]]\">VERIFY</a></h3>\n"
        		+ "<p>Thank you, Drakkar Financial Manager Team.</p>";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        content = content.replace("[[name]]", obj.getNombre()+" "+obj.getApellido());
        String verifyURL = siteURL + "/usuarios/verify/" + obj.getCodigo();
        content = content.replace("[[URL]]", verifyURL);
        helper.setText(content, true);
        mailSender.send(message);
    }
    
    private void enviarCorreoRecCon(Usuarios obj, String siteURL) throws MessagingException, UnsupportedEncodingException {
        String toAddress = obj.getCorreo();
        String fromAddress = "mr.jowade@gmail.com";
        String senderName = "Drakkar Financial Manager Team";
        String subject = "Reset password";
        String content = "<h1>Hello, you have requested to reset your password.</h1>\n"
        		+ "<p>Click the link below to change your password:</p>\n"
        		+ "<p><a href=\"[[URL]]\">Change my password</a></p>\n"
        		+ "<p>Ignore this email if you have not made this request and change the password of your account immediately.</p>";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        String resetURL = siteURL + "/usuarios/resetpassword/" + obj.getCodigo();
        content = content.replace("[[URL]]", resetURL);
        helper.setText(content, true);
        mailSender.send(message);
    }
	
}
