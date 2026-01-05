package com.Pokemon.pokemon.Controller;

import com.Pokemon.pokemon.DTO.LoginRequest;
import com.Pokemon.pokemon.Service.JwtService;
import com.Pokemon.pokemon.Service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/login")
public class AuthController {
    
    //para realizar la autenticacion del usuario
    @Autowired
    private AuthenticationManager authManager;
    
    @Autowired 
    JwtService jwtUtil;
    
    @Autowired
    UsuarioService usuarioService;
    
    @GetMapping
    public String showLoginForm(Model model) {
//        model.addAttribute("loginData", new HashMap<String, String>());
        return "Login";
    }
    
    @PostMapping
    public String iniciarSesion(@RequestParam String email, @RequestParam String password, Model model, HttpSession session) {

        RestTemplate restTemplate = new RestTemplate();
        LoginRequest loginReq = new LoginRequest(email, password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequest> request = new HttpEntity<>(loginReq, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                "http://localhost:8080/auth/login",
                HttpMethod.POST,
                request,
                Map.class
            );

            Map body = response.getBody();
            if (body != null && body.containsKey("token")) {
                String token = (String) body.get("token"); 
                session.setAttribute("jwt", token);
                return "redirect:/pokedex"; 
            } else {
                model.addAttribute("error", "Correo o contraseña incorrectos");
                return "Login";
            }

        } catch (Exception ex) {
            model.addAttribute("error", "Correo o contraseña incorrectos");
            return "Login"; 
        }
    }



}
