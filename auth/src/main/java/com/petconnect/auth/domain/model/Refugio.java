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
    private Integer currentPets;
    private String supportDocument;
    private boolean aprobado = false;
    private String shelterDescription;

    public boolean isValid() {
        return getName() != null &&
                getEmail() != null &&
                getPassword() != null &&
                getPhoneNumber() != null &&
                getCity() != null &&
                getAddress() != null &&
                getProfilePicture() != null &&
                getRole() != null &&
                getNit() != null &&
                getWebsite() != null &&
                getCurrentPets() != null &&
                getSupportDocument() != null &&
                getShelterDescription() != null;
    }

}
