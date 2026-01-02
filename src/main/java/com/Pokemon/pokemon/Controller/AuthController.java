package com.Pokemon.pokemon.Controller;

import com.Pokemon.pokemon.DTO.LoginRequest;
import com.Pokemon.pokemon.JPA.Result;
import com.Pokemon.pokemon.Service.JwtService;
import com.Pokemon.pokemon.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
    public String iniciarSesion(@RequestParam String email, @RequestParam String password) throws Exception{
    
       Result result = new Result();
       String url = "http://localhost:8080/auth";
    
       try {
           
            RestTemplate restTemplate = new RestTemplate();
           
            LoginRequest loginReq = new LoginRequest(email, password);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<LoginRequest> request = new HttpEntity<>(loginReq, headers);
            
            ResponseEntity<Result> response = restTemplate.exchange(
                url + "/login",
                HttpMethod.POST,
                request,
                Result.class
            );
            
//            String token = (String) response.getBody().get("token");
            
            return "redirect:/login?login=correct";

        }catch (BadCredentialsException ex){
            
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            
            return "redirect:/login?login=error";
            
        } 
    
    
    }

}
