package com.Pokemon.pokemon.RestController;


import com.Pokemon.pokemon.DTO.LoginRequest;
import com.Pokemon.pokemon.JPA.Result;
import com.Pokemon.pokemon.JPA.Usuario;
import com.Pokemon.pokemon.Service.JwtService;
import com.Pokemon.pokemon.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/auth") // Ruta para la API
public class AuthRestController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired 
    private JwtService jwtUtil;

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            final UserDetails userDetails = usuarioService.loadUserByUsername(loginRequest.getEmail());
            final String token = jwtUtil.creatToken(userDetails);

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("status", "success");
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Email o contraseña incorrectos");
            return ResponseEntity.ok(response);
        }
    }
    
    @PostMapping ("/addUsuario")
    public ResponseEntity Add(@RequestBody  Usuario usuario) {
        
         Result result = new Result();
         
         try {
            //CIFRAR CONTRASE;A
            String password = passwordEncoder.encode(usuario.getPassword());
            usuario.setPassword(password);
            //CREAR USUARIO
            Usuario newUsuario = usuarioService.add(usuario);
            result.object = newUsuario;
            result.correct = true;
            result.errorMessage = "Se agrego un nuevo usuario correctamente";
            result.status = 201;
         
         
         } catch (Exception ex) {
         
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            result.status = 500;
         
         }
         
    
         return ResponseEntity.status(result.status).body(result);
      
    
    }

}
