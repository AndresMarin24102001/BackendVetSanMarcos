package cl.duocuc.Usuario.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 13, nullable = false)
    private String run;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 150)
    private String apellido;

    @Column(nullable = false, name = "fecha_nac")
    private LocalDate fechaNac;

    
    @Column(nullable = false, unique = true, length = 150)
    private String correo;

    @Column(nullable = false, length = 300)
    private String direccion;

    
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void alCrear() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }
}
