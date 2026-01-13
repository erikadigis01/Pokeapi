
package com.Pokemon.pokemon.Controller;

import com.Pokemon.pokemon.DTO.PokemonAdminCardDTO;
import com.Pokemon.pokemon.Service.PokemonService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/administrador")
public class AdminController {
  

    private final PokemonService pokemonService;

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
    

}
