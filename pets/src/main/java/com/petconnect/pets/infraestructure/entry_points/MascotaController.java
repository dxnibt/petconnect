package com.petconnect.pets.infraestructure.entry_points;

import com.petconnect.pets.domain.exception.MascotaExisteException;
import com.petconnect.pets.domain.model.Mascota;
import com.petconnect.pets.domain.usecase.MascotaUseCase;
import com.petconnect.pets.infraestructure.driver_adapters.jpa_repository.MascotaData;
import com.petconnect.pets.infraestructure.mapper.MascotaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/petconnect/mascotas")
@RequiredArgsConstructor
public class MascotaController {

    private final MascotaMapper mascotaMapper;
    private final MascotaUseCase mascotaUseCase;

    @PostMapping("/save")
    public ResponseEntity<?> saveMascota(@RequestBody MascotaData mascotaData){
        try {
            Mascota mascotaConvertida = mascotaMapper.toMascota(mascotaData);
            Mascota mascota = mascotaUseCase.guardarMascota(mascotaConvertida);
            return new ResponseEntity<>(mascota, HttpStatus.OK);
        } catch (IllegalArgumentException error){
            return new ResponseEntity<>(error.getMessage(),HttpStatus.OK);
        }
    }

    @GetMapping("/{pet_id}")
    public ResponseEntity <?> findByIdMascota (@PathVariable Long pet_id){

        Mascota mascota = mascotaUseCase.buscarPorId(pet_id);

        if (mascota.getPet_id()!=null){
            return new ResponseEntity<>(mascota, HttpStatus.OK);
        }
        return new ResponseEntity<>("La mascota no existe",HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/List")
    public ResponseEntity<?> listarMascotas(@RequestParam(defaultValue ="-1") int page, @RequestParam(defaultValue = "2") int size){
        List<Mascota> mascotas = mascotaUseCase.obtenerTodas(page,size);
        if (mascotas.isEmpty()){
            return ResponseEntity.ok("No hay más productos disponibles");
        }
        return ResponseEntity.ok(mascotas);
    }

    @PutMapping("/update")
    public ResponseEntity<?> actualizarMascota(@RequestBody MascotaData mascotaData){
        try {
            Mascota mascota = mascotaMapper.toMascota(mascotaData);
            Mascota mascotaValidadaAct = mascotaUseCase.actualizarMascota(mascota);
            return new ResponseEntity<>(mascotaValidadaAct,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{pet_id}")
    public ResponseEntity<String> deleteMascota(@PathVariable Long pet_id){
        try {
            mascotaUseCase.eliminarMascota(pet_id);
            return new ResponseEntity<>("Mascota Eliminada", HttpStatus.OK);
        }catch (Exception error){
            return new ResponseEntity<>(error.getMessage(),HttpStatus.OK);
        }
    }



}
