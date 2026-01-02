package com.Pokemon.pokemon.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonDTO {

    @JsonProperty("id")
    private Integer Id;

    @JsonProperty("name")
    private String Name;

    @JsonProperty("sprites")
    private Sprites Sprites;

    @JsonProperty("types")
    private List<PokemonTypeDTO> types;

    @JsonProperty("height")
    private double height;

    @JsonProperty("weight")
    private double weight;

    @JsonProperty("abilities")
    private List<AbilityDTO> abilities;

    @JsonProperty("stats")
    private List<StatDTO> stats;

    public PokemonDTO() {
    }

    public PokemonDTO(Integer Id, String Name, Sprites Sprites, List<PokemonTypeDTO> types, double height, double weight, List<AbilityDTO> abilities) {
        this.Id = Id;
        this.Name = Name;
        this.Sprites = Sprites;
        this.types = types;
        this.height = height;
        this.weight = weight;
        this.abilities = abilities;
    }

    public void setId(Integer Id) {
        this.Id = Id;
    }

    public Integer getId() {
        return Id;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getName() {
        return Name;
    }

    public void setSprites(Sprites Sprites) {
        this.Sprites = Sprites;
    }

    public Sprites getSprites() {
        return Sprites;
    }

    public List<PokemonTypeDTO> getTypes() {
        return types;
    }

    public void setTypes(List<PokemonTypeDTO> types) {
        this.types = types;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public List<AbilityDTO> getAbilities() {
        return abilities;
    }

    public void setAbilities(List<AbilityDTO> abilities) {
        this.abilities = abilities;
    }

    public List<StatDTO> getStats() {
        return stats;
    }

    public void setStats(List<StatDTO> stats) {
        this.stats = stats;
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
