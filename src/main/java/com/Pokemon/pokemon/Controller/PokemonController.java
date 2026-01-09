package com.Pokemon.pokemon.Controller;


import com.Pokemon.pokemon.JPA.Roll;
import com.Pokemon.pokemon.JPA.Usuario;
import com.Pokemon.pokemon.Service.JwtService;
import com.Pokemon.pokemon.Service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("pokedex")
public class PokemonController {
    
    @Autowired 
    JwtService jwtUtil;
    
    @Autowired
    UsuarioService usuarioService;
    
    @GetMapping
    public String index(Model model, HttpSession session) {
        String token = (String) session.getAttribute("token");
        if (token != null) {
            String userEmail = jwtUtil.extraerUsername(token);
            model.addAttribute("userEmail", userEmail);
        } else {
            // Si no hay token, redirige al login
            return "redirect:/login";
        }
        return "Index";
    }

    
    @GetMapping("detail/{email}")
    public String Form(@PathVariable("email") String email, Model model) {
        
//        //traer el id con el email
        Integer idUsuario = usuarioService.getUserIdByEmail(email);
//        //traer el resto del usuario
        Usuario usuario = usuarioService.getById(idUsuario);
//        //agregarlo al modelo
        model.addAttribute("usuario", usuario);
        model.addAttribute("email", email);
        return "Perfil";
    }
    
    @PostMapping("detail")
    public String ActualizarDatos(@ModelAttribute("usuario") Usuario usuario) {
        
        Usuario user = usuarioService.getById(usuario.getId());
        //sacar el roll y asignarlo
        Roll roll = new Roll();
        roll.setIdRoll(user.Roll.getIdRoll());
        usuario.setRoll(roll);
        //sacar el password y asignarlo
        usuario.setPassword(user.getPassword());
        //mandar al restcontroller 
        usuarioService.update(user.getId(), usuario);
        return "redirect:/pokedex/detail/" +  user.getEmail();
    }
    

}
