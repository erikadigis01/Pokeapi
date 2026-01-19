package com.Pokemon.pokemon.Repository;

import com.Pokemon.pokemon.JPA.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long>{
    
    VerificationCode findByCode(String code);

}
