package com.Pokemon.pokemon.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("pokedex")
public class PokemonController {

    @GetMapping()
    public String index() {
        return "Index";
    }
    @GetMapping("detail/{email}")
    public String Form(@PathVariable("email") String email, Model model) {
        
        
        model.addAttribute("email", email);
        return "Perfil";
    }
    
    

}
