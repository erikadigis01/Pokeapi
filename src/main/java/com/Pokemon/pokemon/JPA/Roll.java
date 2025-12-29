package com.Pokemon.pokemon.JPA;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name  = "ROLL")
public class Roll {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idroll")
    private int IdRoll;
    
    @Column(name = "nombre")
    private String Nombre;
    
    public Roll(){}
    
    public Roll(int IdRoll, String Nombre){
        
        this.IdRoll = IdRoll;
        this.Nombre = Nombre;
    }
    
    public void setIdRoll(int IdRoll){
        this.IdRoll = IdRoll;
    }
    public int getIdRoll(){
        return IdRoll;
    }
    
    public void setNombre(String Nombre){
        this.Nombre = Nombre;
    }
    public String getNombre(){
        return Nombre;
    }
}
