package com.Pokemon.pokemon.Controller;

import com.Pokemon.pokemon.DTO.LoginRequest;
import com.Pokemon.pokemon.JPA.Result;
import com.Pokemon.pokemon.JPA.Roll;
import com.Pokemon.pokemon.JPA.Usuario;
import com.Pokemon.pokemon.Service.JwtService;
import com.Pokemon.pokemon.Service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {
    
    public String url = "http://localhost:8080/auth";
    
    //para realizar la autenticacion del usuario
    @Autowired
    private AuthenticationManager authManager;
    
    @Autowired 
    JwtService jwtUtil;
    
    @Autowired
    UsuarioService usuarioService;
    
    @GetMapping("login")
    public String showLoginForm(Model model, @ModelAttribute("status") String status) {
        model.addAttribute("status", status);
        return "Login";
    }
    
    @GetMapping("/login/registro")
    public String createAccount(Model model) {
        return "Registro";
    }
    @PostMapping("/login/registrar")
    public String RegistrarCuenta(@RequestParam String nombre, @RequestParam String apellidoPaterno,
            @RequestParam String apellidoMaterno,@RequestParam String email,
            @RequestParam String password, Model model, RedirectAttributes redirectAttributes ){
        
        //crear al usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellidoPaterno(apellidoPaterno);
        usuario.setApellidoMaterno(apellidoMaterno);
        usuario.setEmail(email);
        usuario.setPassword(password);
        usuario.roll = new Roll();
        usuario.roll.setIdRoll(4);//por default
        
        //mandar al restcontroller
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Usuario> requestEntity = new HttpEntity<>(usuario, headers);
        
        try {
        
            ResponseEntity<Result<Usuario>> responseEntityUsuario =
            restTemplate.exchange(
                url + "/addUsuario",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Result<Usuario>>() {}
            );
            
            if(responseEntityUsuario.getStatusCode().value() == 201){
                Result resultUsuario = responseEntityUsuario.getBody();
                redirectAttributes.addFlashAttribute("mensaje", "Usuario creado exitosamente. Ahora puedes iniciar sesión.");
                return "redirect:/login";
            } else {
                redirectAttributes.addFlashAttribute("error", "No se pudo crear el usuario: ");
                return "redirect:/login";
            }
        
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "No se pudo crear el usuario: " + ex.getMessage());
            return "redirect:/login";
        
        }
    }
    
    @PostMapping("login")
    public String iniciarSesion(@RequestParam String email, @RequestParam String password, 
            Model model, HttpSession session, RedirectAttributes redirectAttributes) {

        RestTemplate restTemplate = new RestTemplate();
        LoginRequest loginReq = new LoginRequest(email, password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequest> request = new HttpEntity<>(loginReq, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                url + "/login",
                HttpMethod.POST,
                request,
                Map.class
            );

            Map body = response.getBody();
            if (body != null && body.containsKey("token")) {
                String token = (String) body.get("token"); 
                //se manda el atributo al index
                session.setAttribute("token", token);
                //extraer el nombre de usuario o correo
                String userEmail = jwtUtil.extraerUsername(token);
                redirectAttributes.addFlashAttribute("userEmail", userEmail);
                return "redirect:/pokedex"; 
            } else {
                redirectAttributes.addFlashAttribute("error", "Correo o contraseña incorrectos");
                return "redirect:/login";
            }

        } catch (Exception ex) {
           redirectAttributes.addFlashAttribute("error", "Correo o contraseña incorrectos");
           return "redirect:/login"; 
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout=true";
    } 
}
