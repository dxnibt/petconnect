package com.petconnect.auth.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Refugio extends Usuario{

    private String nit;
    private String website;
    private Integer currentPets = 0;
    private String supportDocument;
    private boolean aprobado = false;
    private String shelterDescription;

}
