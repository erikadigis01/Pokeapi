package com.Pokemon.pokemon.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("pokedex")
public class PokemonController {

    @GetMapping()
    public String index() {
        return "Index";
    }

}
