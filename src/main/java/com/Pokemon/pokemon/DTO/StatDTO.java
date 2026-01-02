package com.Pokemon.pokemon.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatDTO {

    @JsonProperty("base_stat")
    private int baseStat;

    @JsonProperty("stat")
    private Stat stat;

    public int getBaseStat() {
        return baseStat;
    }

    public void setBaseStat(int baseStat) {
        this.baseStat = baseStat;
    }

    public Stat getStat() {
        return stat;
    }

    public void setStat(Stat stat) {
        this.stat = stat;
    }

    public static class Stat {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
