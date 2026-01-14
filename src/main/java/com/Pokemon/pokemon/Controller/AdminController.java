
package com.Pokemon.pokemon.Controller;

import com.Pokemon.pokemon.DTO.PokemonAdminCardDTO;
import com.Pokemon.pokemon.JPA.Result;
import com.Pokemon.pokemon.JPA.Usuario;
import com.Pokemon.pokemon.Service.JwtService;
import com.Pokemon.pokemon.Service.PokemonService;
import com.Pokemon.pokemon.Service.UsuarioService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
@RequestMapping("/administrador")
public class AdminController {
    
    @Autowired
    JwtService jwtService;
  
    @Autowired
    UsuarioService usuarioService;
    
    private final PokemonService pokemonService;
    
    private final String url = "http://localhost:8080/admin/";

    public AdminController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }
    
        private String getTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("JWT_TOKEN".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @GetMapping("/pokemons")
    public String mostrarPokemonsAdmin(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<PokemonAdminCardDTO> pokemons = pokemonService.getPokemonsConFavoritos();
        
        model.addAttribute("userEmail", userDetails.getUsername());
        model.addAttribute("pokemons", pokemons);
        return "AdminIndex"; 
    }
    
    @GetMapping("detail/{email}")
    public String detalleUsuario(@PathVariable("email") String email,
                             HttpServletRequest request,
                             Model model,
                             RedirectAttributes redirectAttributes) {

    String token = getTokenFromCookie(request);
    if (token == null) {
        redirectAttributes.addAttribute("status", "Su sesión ha caducado");
        return "redirect:/login";
    }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Result<Usuario>> response =
            restTemplate.exchange(url + "detail/" + email,
                                  HttpMethod.GET,
                                  entity,
                                  new ParameterizedTypeReference<Result<Usuario>>() {});
        
        HttpHeaders headersUsers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<?> entityUsers = new HttpEntity<>(headers);

        RestTemplate restTemplateUsers = new RestTemplate();
        ResponseEntity<Result<Usuario>> responseUsers =
            restTemplate.exchange(url + "users",
                                  HttpMethod.GET,
                                  entity,
                                  new ParameterizedTypeReference<Result<Usuario>>() {});

        if (response.getStatusCode().is2xxSuccessful()) {
            Result resultUsuario = responseUsers.getBody();
            model.addAttribute("usuarios", resultUsuario.objects);
            Usuario usuario = (Usuario) response.getBody().object;
            model.addAttribute("usuario", usuario);
        }

        return "AdministradorPerfil";
    }
    
    @PostMapping("detail")
    public String actualizarDatos(@ModelAttribute("usuario") Usuario usuario,
                                  HttpServletRequest request,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {

        String token = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("JWT_TOKEN".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }

        if (token == null) {
            redirectAttributes.addAttribute("status", "Su sesión ha caducado");
            return "redirect:/login";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Usuario> requestEntity = new HttpEntity<>(usuario, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result<Usuario>> responseEntityUsuario =
            restTemplate.exchange(
                url + "detail",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Result<Usuario>>() {}
            );
        
        

        if (responseEntityUsuario.getStatusCode().value() == 201) {
            Result resultUsuario = responseEntityUsuario.getBody();
            Usuario user = (Usuario) resultUsuario.object;
            model.addAttribute("usuario", user);
            model.addAttribute("email", user.getEmail());
        }

        return "redirect:/administrador/detail/" + usuario.getEmail();
    }
    
    @GetMapping("/users")
    public String getAllUsers(  HttpServletRequest request, 
                                RedirectAttributes redirectAttributes,
                                Model model){
        
        String token = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("JWT_TOKEN".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }

        if (token == null) {
            redirectAttributes.addAttribute("status", "Su sesión ha caducado");
            return "redirect:/login";
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Result<Usuario>> response =
            restTemplate.exchange(url + "users",
                                  HttpMethod.GET,
                                  entity,
                                  new ParameterizedTypeReference<Result<Usuario>>() {});

        if (response.getStatusCode().is2xxSuccessful()) {
            
            Result resultUsuario = response.getBody();
            
            model.addAttribute("usuarios", resultUsuario.objects);
        }
    
        return "AdministradorPerfil";
    }
    
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") int id, 
                                HttpServletRequest request, 
                                RedirectAttributes redirectAttributes,
                                Model model) {
        
        String token = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("JWT_TOKEN".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }

        if (token == null) {
            redirectAttributes.addAttribute("status", "Su sesión ha caducado");
            return "redirect:/login";
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Result<Usuario>> responseDelete =
            restTemplate.exchange(url + "users/" + id,
                                  HttpMethod.DELETE,
                                  entity,
                                  new ParameterizedTypeReference<Result<Usuario>>() {});

        if (responseDelete.getStatusCode().is2xxSuccessful()) {
            
            Result resultUsuario = responseDelete.getBody();
            
            model.addAttribute("usuarios", resultUsuario.objects);
        }
        //buscar el email 
        String email = jwtService.extraerUsername(token);
        //buscar el id usuario 
        Long idAdmin = usuarioService.getUserIdByEmail(email);
        
        //buscar usuario
        Usuario admin = usuarioService.getById(idAdmin);
        
        return "redirect:/administrador/detail/" + admin.getEmail();
    }
}
