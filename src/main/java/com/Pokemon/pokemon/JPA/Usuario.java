package com.Pokemon.pokemon.JPA;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name  = "USUARIO")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer Id;
    
    @Column(name = "nombre")
    private String nombre;
    
    @Column(name = "apellidopaterno")
    private String ApellidoPaterno;
    
    @Column(name = "apellidomaterno")
    private String ApellidoMaterno;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "password")
    private String Password;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idroll", nullable = true)
    public Roll Roll;
    
    public Usuario(){}
    
    public Usuario (Integer Id,String nombre, String ApellidoPaterno, String ApellidoMaterno, String email, String Password, Roll Roll){
    
        this.Id = Id;
        this.nombre = nombre;
        this.ApellidoPaterno = ApellidoPaterno;
        this.ApellidoMaterno = ApellidoMaterno;
        this.email = email;
        this.Password = Password;
        this.Roll = Roll;
    }
    

    // Getter y Setter para Id
    public Integer getId() {
        return Id;
    }

    public void setId(Integer Id) {
        this.Id = Id;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    // Getter y Setter para ApellidoPaterno
    public String getApellidoPaterno() {
        return ApellidoPaterno;
    }

    public void setApellidoPaterno(String ApellidoPaterno) {
        this.ApellidoPaterno = ApellidoPaterno;
    }

    // Getter y Setter para ApellidoMaterno
    public String getApellidoMaterno() {
        return ApellidoMaterno;
    }

    public void setApellidoMaterno(String ApellidoMaterno) {
        this.ApellidoMaterno = ApellidoMaterno;
    }

    // Getter y Setter para Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter y Setter para Password
    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    // Getter y Setter para Roll
    public Roll getRoll() {
        return Roll;
    }

    public void setRoll(Roll Roll) {
        this.Roll = Roll;
    }



}
