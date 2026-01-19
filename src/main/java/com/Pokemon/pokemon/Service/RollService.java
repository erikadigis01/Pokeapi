
package com.Pokemon.pokemon.Service;

import com.Pokemon.pokemon.JPA.Roll;
import com.Pokemon.pokemon.Repository.RollRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RollService {
    
    @Autowired
    RollRepository rollRepository;
    
    @Transactional
    public List<Roll> getAll() {
        return rollRepository.findAll();
    }
    
}
