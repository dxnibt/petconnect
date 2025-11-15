package com.petconnect.auth.infraestructure.entry_points;

import com.petconnect.auth.domain.model.Adoptante;
import com.petconnect.auth.domain.usecase.AdoptanteUseCase;
import com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.adoptante.AdoptanteData;
import com.petconnect.auth.infraestructure.mapper.AdoptanteMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/adoptantes")
@RequiredArgsConstructor
public class AdoptanteController {

    private final AdoptanteMapper adoptanteMapper;
    private final AdoptanteUseCase adoptanteUseCase;

    @PostMapping("/save")
    public ResponseEntity<?> saveAdoptante(@Valid @RequestBody AdoptanteData adoptanteData){

        try{
            Adoptante nuevoAdoptante = adoptanteMapper.toAdoptante(adoptanteData);
            Adoptante adoptante = adoptanteUseCase.guardarAdoptante(nuevoAdoptante);
            return new ResponseEntity<>(adoptante, HttpStatus.CREATED);

        } catch (IllegalArgumentException error){
            return new ResponseEntity<>(error.getMessage(),HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>("Error al guardar adoptante: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
