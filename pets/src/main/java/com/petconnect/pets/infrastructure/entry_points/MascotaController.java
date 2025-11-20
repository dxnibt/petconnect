package com.petconnect.pets.infrastructure.entry_points;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.petconnect.pets.domain.model.Mascota;
import com.petconnect.pets.domain.usecase.MascotaUseCase;
import com.petconnect.pets.infrastructure.driver_adapters.jpa_repository.JwtDto.JwtUserDetails;
import com.petconnect.pets.infrastructure.driver_adapters.jpa_repository.mascotas.ActualizationData;
import com.petconnect.pets.infrastructure.driver_adapters.jpa_repository.mascotas.MascotaData;
import com.petconnect.pets.infrastructure.mapper.MascotaMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/petconnect/mascotas")
@RequiredArgsConstructor
public class MascotaController {

    private final MascotaMapper mascotaMapper;
    private final MascotaUseCase mascotaUseCase;

    @PostMapping("/save")
    public ResponseEntity<?> saveMascota(
            @RequestBody @Valid MascotaData mascotaData,
            Authentication authentication) {  // <- aquí Authentication

        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>("Token inválido o ausente", HttpStatus.UNAUTHORIZED);
        }

        // Validar rol
        boolean esRefugio = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_REFUGIO"));
        boolean esAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!esRefugio && !esAdmin) {
            return new ResponseEntity<>("No tienes permisos para esta acción", HttpStatus.FORBIDDEN);
        }

        try {
            Mascota mascotaConvertida = mascotaMapper.toMascota(mascotaData);
            Mascota mascota = mascotaUseCase.guardarMascota(mascotaConvertida, null); // si quieres pasar userDetails puedes mapearlo
            return new ResponseEntity<>(mascota, HttpStatus.OK);
        } catch (IllegalArgumentException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



    @GetMapping("/{pet_id}")
    public ResponseEntity<?> findByIdMascota(@PathVariable Long pet_id) {

        Mascota mascota = mascotaUseCase.buscarPorId(pet_id);

        if (mascota.getPet_id() != null) {
            return new ResponseEntity<>(mascota, HttpStatus.OK);
        }
        return new ResponseEntity<>("La mascota no existe", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/List")
    public ResponseEntity<?> listarMascotas(@RequestParam(defaultValue = "-1") int page, @RequestParam(defaultValue = "2") int size, JwtUserDetails userDetails) {
        List<Mascota> mascotas = mascotaUseCase.obtenerTodas(page, size);
        if (mascotas.isEmpty()) {
            return ResponseEntity.ok("No hay más productos disponibles");
        }
        return ResponseEntity.ok(mascotas);
    }

    @PatchMapping("/update/{pet_id}")
    public ResponseEntity<?> actualizarMascota(
            @PathVariable Long pet_id,
            @RequestBody ActualizationData data,JwtUserDetails jwtUserDetails) {

        try {
            Mascota mascotaActualizada = mascotaUseCase.actualizarMascota(pet_id, data,jwtUserDetails);
            return ResponseEntity.ok(mascotaActualizada);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping("/delete/{pet_id}")
    public ResponseEntity<String> deleteMascota(@PathVariable Long pet_id, JwtUserDetails jwtUserDetails){
        try {
            mascotaUseCase.eliminarMascota(pet_id,jwtUserDetails);
            return new ResponseEntity<>("Mascota Eliminada", HttpStatus.OK);
        }catch (Exception error){
            return new ResponseEntity<>(error.getMessage(),HttpStatus.OK);
        }
    }



}
