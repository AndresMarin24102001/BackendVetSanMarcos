package cl.duocuc.Usuario.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record RegisterRequest(
        @NotBlank @Size(max = 13) String run,
        @NotBlank @Size(max = 150) String nombre,
        @NotBlank @Size(max = 150) String apellido,
        @NotNull @Past LocalDate fechaNac,
        @Email @NotBlank @Size(max = 150) String correo,
        @NotBlank @Size(max = 300) String direccion,
        @NotBlank @Size(min = 8, max = 72) String password) {
}
