package ar.edu.utn.frc.tup.lc.iii.directory.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
    private UUID id;
    private UUID tenantId;

    @NotBlank(message = "El nombre del rol es requerido")
    private String name;

    @NotBlank(message = "El código del rol es requerido")
    private String code;

    private String description;
    private List<String> permissions; // Array de permisos
    private boolean active;
    private boolean isSystem; // Roles del sistema no se pueden eliminar
    private Instant createdAt;
    private Instant updatedAt;
}

