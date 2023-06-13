package com.myorg.recipe.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "ingredient")
public class Ingredient {
    @Id
    @GeneratedValue
    private Long ingId;

    private String ingName;

    private String measurement;

    @Nullable
    private String note;

}
