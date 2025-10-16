package ar.edu.utn.frc.tup.lc.iii.customfields.dtos;

import ar.edu.utn.frc.tup.lc.iii.customfields.entities.EntityType;
import ar.edu.utn.frc.tup.lc.iii.customfields.entities.FieldType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CustomFieldDto {
    private UUID id;
    private UUID tenantId;

    @NotBlank(message = "El nombre es requerido")
    private String name;

    @NotBlank(message = "La clave del campo es requerida")
    private String fieldKey;

    @NotNull(message = "El tipo de campo es requerido")
    private FieldType fieldType;

    @NotNull(message = "El tipo de entidad es requerido")
    private EntityType entityType;

    private boolean required;
    private String defaultValue;
    private String description;
    private List<String> options; // Para SELECT/MULTISELECT
    private String validationRules;
    private Integer displayOrder;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
}

