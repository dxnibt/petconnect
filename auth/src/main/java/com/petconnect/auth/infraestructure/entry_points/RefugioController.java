package com.petconnect.auth.infraestructure.entry_points;

import com.petconnect.auth.domain.exception.CamposIncompletosException;
import com.petconnect.auth.domain.exception.RefugioNoEncontradoException;
import com.petconnect.auth.domain.model.Refugio;
import com.petconnect.auth.domain.usecase.RefugioUseCase;
import com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.refugio.RefugioActualizarDto;
import com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.refugio.RefugioData;
import com.petconnect.auth.infraestructure.mapper.RefugioMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/petconnect/refugios")
@RequiredArgsConstructor
public class RefugioController {

    private final RefugioMapper refugioMapper;
    private final RefugioUseCase refugioUseCase;

    @PostMapping("/save")
    public ResponseEntity<?> saveRefugio(@Valid @RequestBody RefugioData refugioData) {

        try {
            Refugio nuevoRefugio = refugioMapper.toRefugio(refugioData);
            Refugio refugio = refugioUseCase.guardarRefugio(nuevoRefugio);
            return new ResponseEntity<>(refugio, HttpStatus.CREATED);

        } catch (CamposIncompletosException error){
            return new ResponseEntity<>(error.getMessage(),HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>("Error al guardar refugio: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> actualizarRefugio(@Valid @PathVariable Long id, @RequestBody RefugioActualizarDto dto) {

        try {
            Refugio refugioActualizado = refugioUseCase.actualizarRefugio(id, dto);
            return ResponseEntity.ok(refugioActualizado);

        } catch (RefugioNoEncontradoException error) {
            return ResponseEntity.badRequest().body(error.getMessage());
        } catch (Exception e) {
        return new ResponseEntity<>("Error al guardar refugio: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
