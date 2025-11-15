package com.petconnect.auth.infraestructure.entry_points;

import com.petconnect.auth.domain.model.Refugio;
import com.petconnect.auth.domain.usecase.RefugioUseCase;
import com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.refugio.RefugioData;
import com.petconnect.auth.infraestructure.mapper.RefugioMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        } catch (IllegalArgumentException error){
            return new ResponseEntity<>(error.getMessage(),HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>("Error al guardar refugio: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
