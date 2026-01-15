package com.Pokemon.pokemon.Controller;

import com.Pokemon.pokemon.DTO.LoginRequest;
import com.Pokemon.pokemon.JPA.Result;
import com.Pokemon.pokemon.JPA.Roll;
import com.Pokemon.pokemon.JPA.Usuario;
import com.Pokemon.pokemon.Service.JwtService;
import com.Pokemon.pokemon.Service.UsuarioService;
import java.util.Map;
import jakarta.servlet.http.Cookie; 
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final String url = "http://localhost:8080/auth";

    @Autowired
    private JwtService jwtUtil;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String showLoginForm(Model model, @ModelAttribute("status") String status) {
        model.addAttribute("status", status);
        return "Login";
    }

    @GetMapping("/login/registro")
    public String createAccount(Model model) {
        return "Registro";
    }

    @PostMapping("/login/registrar")
    public String registrarCuenta(@RequestParam String nombre,
                                  @RequestParam String apellidoPaterno,
                                  @RequestParam String apellidoMaterno,
                                  @RequestParam String email,
                                  @RequestParam String password,
                                  RedirectAttributes redirectAttributes) {

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellidoPaterno(apellidoPaterno);
        usuario.setApellidoMaterno(apellidoMaterno);
        usuario.setEmail(email);
        usuario.setPassword(password);
        usuario.setRoll(new Roll());
        usuario.getRoll().setIdRoll(2);
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

            if (responseEntityUsuario.getStatusCode().value() == 201) {
                redirectAttributes.addFlashAttribute("mensaje", "Usuario creado exitosamente. Ahora puedes iniciar sesión.");
                return "redirect:/login";
            } else {
                redirectAttributes.addFlashAttribute("error", "No se pudo crear el usuario.");
                return "redirect:/login";
            }

        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "No se pudo crear el usuario: " + ex.getMessage());
            return "redirect:/login";
        }
    }

    @PostMapping("/login")
    public String iniciarSesion(@RequestParam String email,
                                @RequestParam String password,
                                RedirectAttributes redirectAttributes,
                                HttpServletResponse response) {

        RestTemplate restTemplate = new RestTemplate();
        LoginRequest loginReq = new LoginRequest(email, password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequest> request = new HttpEntity<>(loginReq, headers);

        try {
            ResponseEntity<Map> resp = restTemplate.exchange(
                url + "/login",
                HttpMethod.POST,
                request,
                Map.class
            );

            Map body = resp.getBody();
            if (body != null && body.containsKey("token")) {

                redirectAttributes.getFlashAttributes().clear();

                String token = (String) body.get("token");
                String userEmail = jwtUtil.extraerUsername(token);

                // Guardar cookie
                Cookie cookie = new Cookie("JWT_TOKEN", token);
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                cookie.setMaxAge(60 * 60);
                response.addCookie(cookie);

                // Buscar usuario en BD
                Long iduser = usuarioService.getUserIdByEmail(userEmail);
                Usuario usuario = usuarioService.getById(iduser);

                // Redirigir según rol
                if (usuario.getRoll().getIdRoll() == 1) { // ADMIN
                    return "redirect:/administrador/pokemons";
                } else {
                    return "redirect:/pokedex";
                }

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
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("JWT_TOKEN", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // expira inmediatamente
        response.addCookie(cookie);

        return "redirect:/login?logout=true";
    }

}
