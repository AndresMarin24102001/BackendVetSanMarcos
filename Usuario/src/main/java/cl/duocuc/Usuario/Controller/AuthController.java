package cl.duocuc.Usuario.Controller;

import cl.duocuc.Usuario.Dto.AuthResponse;
import cl.duocuc.Usuario.Dto.LoginRequest;
import cl.duocuc.Usuario.Dto.RegisterRequest;
import cl.duocuc.Usuario.Service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService auth;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        if (auth.registrar(req)) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("mensaje", "Usuario creado"));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", "No se pudo completar el registro"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        Optional<String> token = auth.login(req);
        if (token.isPresent()) {
            return ResponseEntity.ok(new AuthResponse(token.get()));
        }
        // Mismo mensaje si el email no existe o la contraseña es incorrecta.
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Credenciales inválidas"));
    }
}
