package ar.edu.utn.frc.tup.lc.iii.crm.dtos;

import ar.edu.utn.frc.tup.lc.iii.customfields.dtos.CustomFieldValueDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeadDto {
    private UUID id;
    private UUID tenantId;

    @NotBlank(message = "El nombre es requerido")
    private String name;

    private String phone;

    @Email(message = "Email inválido")
    private String email;

    private String source;
    private String status;
    private Integer score;
    private UUID ownerId;
    private String notes;
    private Map<String, CustomFieldValueDto> customFields; // Campos personalizados
    private Instant createdAt;
    private Instant updatedAt;
}
