
package com.Pokemon.pokemon.RestController;

import com.Pokemon.pokemon.JPA.Result;
import com.Pokemon.pokemon.JPA.Roll;
import com.Pokemon.pokemon.Service.RollService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roll")
@CrossOrigin 
public class RollRestController {
    
    @Autowired
    RollService rollService;
    
    @GetMapping
    public ResponseEntity getAll() {
        Result result = new Result();

        try {
           
            List<Roll> rolles = rollService.getAll();

            result.objects = rolles;
            result.correct = true;
            result.errorMessage = "No se encontraron rolles";
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
