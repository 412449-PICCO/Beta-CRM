package ar.edu.utn.frc.tup.lc.iii.directory.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private UUID id;

    @NotBlank(message = "El email es requerido")
    @Email(message = "El email debe ser válido")
    private String email;

    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password; // Solo en creación/actualización

    private String firstName;
    private String lastName;
    private boolean enabled;
    private Set<RoleDto> roles; // Roles filtrados por el tenant actual en el contexto
    private Instant createdAt;
    private Instant updatedAt;
}
