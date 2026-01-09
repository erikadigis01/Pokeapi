package com.Pokemon.pokemon.Controller;


import com.Pokemon.pokemon.Service.JwtService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("pokedex")
public class PokemonController {
    
    @Autowired 
    JwtService jwtUtil;
    
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
        
        
        model.addAttribute("email", email);
        return "Perfil";
    }
    
    

}
