package com.Pokemon.pokemon.Service;

import com.Pokemon.pokemon.JPA.Result;
import com.Pokemon.pokemon.JPA.VerificationCode;
import com.Pokemon.pokemon.Repository.VerificationCodeRepository;
import jakarta.transaction.Transactional;
import java.security.SecureRandom;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationCodeService {
    
    private static SecureRandom secureRandom = new SecureRandom();
    
    @Autowired
    VerificationCodeRepository verificationCodeRepository;
    
    @Transactional
    public Result add(VerificationCode verificationCode) {
        Result result = new Result();
        try {
            verificationCode.setUsos(0);
            verificationCodeRepository.save(verificationCode);
            
            result.object = verificationCodeRepository.findByCode(verificationCode.getCode());
            result.correct = true;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            
        }
        return result;
    }
    
    @Transactional
    public VerificationCode getByCode(String code) {
        return verificationCodeRepository.findByCode(code);
    }
    
    @Transactional
    public void delete(Long idCode) {
        verificationCodeRepository.deleteById(idCode);
    }
    
    public String generarCodigoSeguro() {
        String codigo = UUID.randomUUID().toString();
        return codigo;
    }
    
    public boolean verificarCode(String code) {
    
        VerificationCode verificationCode = verificationCodeRepository.findByCode(code);
        
        if(verificationCode.getUsos() < 5) {//es valido solo si se ha usado menos de 5 veces
            int usos = verificationCode.getUsos();
            verificationCode.setUsos( usos + 1);
            return true;
        } else { return false;}
    }
}
