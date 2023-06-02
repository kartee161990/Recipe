package com.myorg.recipe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "recipe")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String instructions;

    private Boolean isVegetarian ;

    private Integer servingSize;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Ingredient> ingredient;
}
