package com.petconnect.pets.domain.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Mascota {
    private long pet_id;
    private String name;
    private String species;
    private String race;
    private Integer age;
    private String sex;
    private Boolean childFriendly;
    private Boolean requiresAmpleSpace;
    private Boolean sterilization;
    private String vaccines;
    private String description;
    private String imageUrl;
    private String state;




}
