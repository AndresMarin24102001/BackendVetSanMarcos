package cl.duocuc.Usuario.Service;

import cl.duocuc.Usuario.Dto.LoginRequest;
import cl.duocuc.Usuario.Dto.RegisterRequest;
import cl.duocuc.Usuario.Model.Rol;
import cl.duocuc.Usuario.Model.Usuario;
import cl.duocuc.Usuario.Repository.UsuarioRepository;
import cl.duocuc.Usuario.Security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarios;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    /** true si se creó el usuario; false si no se pudo (p. ej. email ya registrado). */
    public boolean registrar(RegisterRequest req) {
        String email = req.email().trim().toLowerCase();
        if (usuarios.existsByEmail(email)) {
            return false;
        }
        Usuario u = Usuario.builder()
                .email(email)
                .passwordHash(encoder.encode(req.password()))
                .rol(Rol.USER)                 // el servidor asigna el rol
                .build();
        try {
            usuarios.save(u);
            return true;
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }

    /** Devuelve el JWT si las credenciales son válidas; vacío en cualquier otro caso. */
    public Optional<String> login(LoginRequest req) {
        return usuarios.findByEmail(req.email().trim().toLowerCase())
                .filter(u -> encoder.matches(req.password(), u.getPasswordHash()))
                .map(jwt::generateToken);
    }
}
