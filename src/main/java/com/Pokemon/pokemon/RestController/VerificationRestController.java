package com.Pokemon.pokemon.RestController;

import com.Pokemon.pokemon.Service.VerificationTokenService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class VerificationRestController {

    @Autowired
    private VerificationTokenService verificationTokenService;

    @GetMapping("/verify")
    public ResponseEntity<Void> verify(@RequestParam("token") String token) {

        boolean isValid = verificationTokenService.validateVerificationToken(token);

        if (!isValid) {
            return ResponseEntity
                    .status(302)
                    .header("Location", "http://localhost:8080/error-verificacion")
                    .build();
        }

        return ResponseEntity
                .status(302)
                .header("Location", "http://localhost:8080/login?verified=true")
                .build();
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<String> resendVerification(@RequestBody Map<String, String> body) {

        String email = body.get("email");

        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body("Email inválido");
        }

        boolean enviado = verificationTokenService.resendVerification(email);

        if (!enviado) {
            return ResponseEntity
                    .badRequest()
                    .body("No se pudo reenviar el correo de verificación");
        }

        return ResponseEntity.ok("Correo de verificación reenviado");
    }

}
