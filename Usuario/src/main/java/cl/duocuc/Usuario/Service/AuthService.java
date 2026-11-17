package cl.duocuc.Usuario.Service;

import cl.duocuc.Usuario.Dto.LoginRequest;
import cl.duocuc.Usuario.Dto.RegisterRequest;
import cl.duocuc.Usuario.Model.Rol;
import cl.duocuc.Usuario.Model.Usuario;
import cl.duocuc.Usuario.Repository.UsuarioRepository;
import cl.duocuc.Usuario.Security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarios;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    /** true si se creó el usuario; false si no se pudo (correo o RUN ya registrados, etc.). */
    public boolean registrar(RegisterRequest req) {
        String correo = req.correo().trim().toLowerCase();
        String run = req.run().trim();
        if (usuarios.existsByCorreo(correo) || usuarios.existsByRun(run)) {
            return false;
        }
        Usuario u = Usuario.builder()
                .run(run)
                .nombre(req.nombre().trim())
                .apellido(req.apellido().trim())
                .fechaNac(req.fechaNac())
                .correo(correo)
                .direccion(req.direccion().trim())
                .passwordHash(encoder.encode(req.password()))
                .rol(Rol.USER)                 // el servidor asigna el rol
                .build();
        try {
            usuarios.save(u);
            return true;
        } catch (DataIntegrityViolationException e) {
            // Solo la primera línea del error: las siguientes pueden incluir la fila completa (con el hash).
            String causa = String.valueOf(e.getMostSpecificCause().getMessage()).lines().findFirst().orElse("");
            log.warn("Registro rechazado por la BD: {}", causa);
            return false;
        }
    }

    /** Devuelve el JWT si las credenciales son válidas; vacío en cualquier otro caso. */
    public Optional<String> login(LoginRequest req) {
        return usuarios.findByCorreo(req.correo().trim().toLowerCase())
                .filter(u -> encoder.matches(req.password(), u.getPasswordHash()))
                .map(jwt::generateToken);
    }
}
