package com.Pokemon.pokemon.RestController;

import com.Pokemon.pokemon.DTO.PageResponse;
import com.Pokemon.pokemon.DTO.PokemonCardDTO;
import com.Pokemon.pokemon.DTO.PokemonDTO;
import com.Pokemon.pokemon.JPA.Favoritos;
import com.Pokemon.pokemon.JPA.Result;
import com.Pokemon.pokemon.JPA.Roll;
import com.Pokemon.pokemon.JPA.Usuario;
import com.Pokemon.pokemon.Service.PokemonService;
import com.Pokemon.pokemon.Service.UsuarioService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pokemon")
@CrossOrigin // si consumes desde frontend separado
public class PokemonRestController {
    
    @Autowired
    UsuarioService usuarioService;
    
    @Autowired
    private PokemonService pokemonService;

    // 🔹 LISTA DE CARTAS (con filtros + paginación)
    @GetMapping("/pokemons")//debe recibir la sesion desde la peticion asincrona
    public PageResponse<PokemonCardDTO> getPokemons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer number
    ) {
        return pokemonService.getPokemons(page, size, name, type, number);
    }

    // 🔹 DETALLE (MODAL)
    @GetMapping("/{name}")
    public PokemonDTO getPokemonByName(@PathVariable String name) {
        return pokemonService.getPokemonByName(name);
    }
    
    @GetMapping("/detail/{email}")
    public ResponseEntity userDetail(@PathVariable String email) {
        Result result = new Result();
         
        try {
            Long id = usuarioService.getUserIdByEmail(email);
            Usuario usuario = usuarioService.getById(id);
            
            result.object = usuario;
            result.correct = true;
            result.errorMessage = "Se encontro un usuario con ese id";
            result.status = 200;
         
         
         } catch (Exception ex) {
         
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            result.status = 500;
         
         }
         
    
         return ResponseEntity.status(result.status).body(result);
      
    }
    
    @PostMapping("/detail")
    public ResponseEntity userDetailPost(@RequestBody Usuario usuario) {
        Result result = new Result();
         
        try {
            
            Usuario user = usuarioService.getById(usuario.getId());
            //sacar el roll y asignarlo
            Roll roll = new Roll();
            roll.setIdRoll(user.roll.getIdRoll());
            usuario.setRoll(roll);
            //sacar el password y asignarlo
            usuario.setPassword(user.getPassword());
            //actualizar la lista de pokemones
            List<Favoritos> favoritos = user.getFavoritos();
            usuario.setFavoritos(favoritos);
            //mandar al restcontroller 
            usuarioService.update(user.getId(), usuario);
            Usuario userFind = usuarioService.getById(usuario.getId());
            
            result.object = userFind;
            result.correct = true;
            result.errorMessage = "Se encontro un usuario con ese id";
            result.status = 200;
         
         
         } catch (Exception ex) {
         
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            result.status = 500;
         
         }
         
    
         return ResponseEntity.status(result.status).body(result);
      
    }
}
