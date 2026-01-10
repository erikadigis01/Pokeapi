package com.Pokemon.pokemon.Controller;
 
 
import com.Pokemon.pokemon.JPA.Result;
import com.Pokemon.pokemon.JPA.Roll;
import com.Pokemon.pokemon.JPA.Usuario;
import com.Pokemon.pokemon.Service.JwtService;
import com.Pokemon.pokemon.Service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
 
@Controller
@RequestMapping("pokedex")
public class PokemonController {
    
    public String url = "http://localhost:8080/pokemon/";
    
    @Autowired 
    JwtService jwtUtil;
    
    @Autowired
    UsuarioService usuarioService;
    
//    VALIDAR SESSION PARA SEPARAR LOGICA
    public boolean validarSession(HttpSession session){
        String token = (String) session.getAttribute("token");
        boolean val = jwtUtil.isTokenExpiration(token);
        //se puede seapara aun mas 
        if(token != null && !jwtUtil.isTokenExpiration(token)) {
            return true;
        }else {
            return false;
        }
    }
    
    @GetMapping
    public String index(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        
        if (validarSession(session)) {
            String userEmail = jwtUtil.extraerUsername((String) session.getAttribute("token"));
            model.addAttribute("session", session);
            model.addAttribute("userEmail", userEmail);
        } else {
            // Si no hay token, redirige al login
            redirectAttributes.addAttribute("status", "Su sessión ha caducado");
            return "redirect:/login";
        }
        return "Index";
    }
 
    
    @GetMapping("detail/{email}")
    public String Form(@PathVariable("email") String email, Model model, HttpSession session, RedirectAttributes redirectAttributes) {

        if(validarSession(session)){

            HttpHeaders headers = new HttpHeaders();
            HttpEntity<?> requestEntity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate(); 
            
            ResponseEntity<Result<Usuario>> responseEntityUsuario =
              restTemplate.exchange(
                  url  + "detail/" + email,
                  HttpMethod.GET,
                  requestEntity,
                  new ParameterizedTypeReference<Result<Usuario>>() {}
              );
          if(responseEntityUsuario.getStatusCode().value() == 200){

              Result resultUsuario = responseEntityUsuario.getBody();
              Usuario user = (Usuario) resultUsuario.object;
              model.addAttribute("usuario", user);
              model.addAttribute("email", email);

          }

        }  else {
              redirectAttributes.addAttribute("status", "Su sessión ha caducado");
              return "redirect:/login";
          }


      return "Perfil";
      
    }
    
    @PostMapping("detail")
    public String ActualizarDatos(@ModelAttribute("usuario") Usuario usuario, 
            HttpSession session, RedirectAttributes redirectAttributes,
            Model model) {
        
        if(validarSession(session)){
        
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<Usuario> requestEntity = new HttpEntity<>(usuario, headers);
            RestTemplate restTemplate = new RestTemplate(); 
            
            ResponseEntity<Result<Usuario>> responseEntityUsuario =
               restTemplate.exchange(
                   url + "detail",
                   HttpMethod.POST,
                   requestEntity,
                   new ParameterizedTypeReference<Result<Usuario>>() {}
               );
            
            Result result = responseEntityUsuario.getBody();
            if(responseEntityUsuario.getStatusCode().value() == 201){

              Result resultUsuario = responseEntityUsuario.getBody();
              Usuario user = (Usuario) resultUsuario.object;
              model.addAttribute("usuario", user);
              model.addAttribute("email", user.getEmail());

          }

        
        } else {
            
            redirectAttributes.addAttribute("status", "Su sessión ha caducado");
            return "redirect:/login";
            
        }
        return "redirect:/pokedex/detail/";
    }
    


 
}