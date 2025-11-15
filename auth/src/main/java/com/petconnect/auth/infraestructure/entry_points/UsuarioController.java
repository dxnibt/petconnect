package com.petconnect.auth.infraestructure.entry_points;

import com.petconnect.auth.domain.model.Usuario;
import com.petconnect.auth.domain.usecase.UsuarioUseCase;
import com.petconnect.auth.infraestructure.driver_adapters.jpa_repository.usuario.UsuarioData;
import com.petconnect.auth.infraestructure.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/petconnect/usuario")
@RequiredArgsConstructor
public class UsuarioController {



}
