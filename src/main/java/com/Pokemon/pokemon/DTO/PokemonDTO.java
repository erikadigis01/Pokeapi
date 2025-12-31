package com.Pokemon.pokemon.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;


public class PokemonDTO {
    
    @JsonProperty("id")
    private Integer Id;
    
    @JsonProperty("name")
    private String Name; 
    
    @JsonProperty("sprites")
    private Sprites Sprites;
    
    public PokemonDTO(){}
    
    public PokemonDTO(Integer Id, String Name, Sprites Sprites){
    
        this.Id = Id;
        this.Name = Name;
        this.Sprites = Sprites;
    }
    
    public void setId(Integer Id) {
        this.Id = Id;
    }
    public Integer getId(){
        return Id;
    }
    
    public void setName(String Name) {
        this.Name = Name;
    }
    public String getName(){
        return Name;
    }
    
    public void setSprites(Sprites Sprites) {
        this.Sprites  = Sprites ;
    }
    public Sprites  getSprites(){
        return Sprites;
    }
    
   public static class Sprites { 
       
       @JsonProperty("front_default") 
       private String front_default; 
       @JsonProperty("front_shiny") 
       private String front_shiny; 
       
       public String getFront_default() { 
           return front_default; 
       }
       public void setFront_default(String front_default) {
           this.front_default = front_default; 
       } 
       public String getFront_shiny() { 
           return front_shiny; 
       } 
       public void setFront_shiny(String front_shiny) { 
           this.front_shiny = front_shiny; 
       } 
   }

}
