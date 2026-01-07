
package com.Pokemon.pokemon.DTO;

import java.util.List;

public class PokemonCardDTO {
    private int id;
    private String name;
    private String image;
    private List<String> types;

    public PokemonCardDTO() {
    }

    public PokemonCardDTO(int id, String name, String image, List<String> types) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.types = types;
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
    
    
}

