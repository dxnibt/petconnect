package com.petconnect.auth.infraestructure.entry_points;

import com.petconnect.auth.domain.exception.AdoptanteMenorEdadException;
import com.petconnect.auth.domain.exception.AdoptanteNoEncontradoException;
import com.petconnect.auth.domain.exception.CamposIncompletosException;
import com.petconnect.auth.domain.exception.SalarioNoAprobadoException;
import com.petconnect.auth.domain.model.Adoptante;
import com.petconnect.auth.domain.usecase.AdoptanteUseCase;
import com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.adoptante.AdoptanteActualizarDto;
import com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.adoptante.AdoptanteData;
import com.petconnect.auth.infraestructure.mapper.AdoptanteMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/petconnect/adoptantes")
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

        } catch (CamposIncompletosException | AdoptanteMenorEdadException | SalarioNoAprobadoException error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getMessage());

        } catch (Exception e) {
            return new ResponseEntity<>("Error al guardar adoptante: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> actualizarAdoptante(@Valid @PathVariable Long id, @RequestBody AdoptanteActualizarDto dto) {

        try {
            Adoptante adoptanteActualizado = adoptanteUseCase.actualizarAdoptante(id, dto);
            return ResponseEntity.ok(adoptanteActualizado);

        } catch (AdoptanteNoEncontradoException error) {
            return ResponseEntity.badRequest().body(error.getMessage());

        } catch (Exception e) {
            return new ResponseEntity<>("Error al guardar adoptante: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
