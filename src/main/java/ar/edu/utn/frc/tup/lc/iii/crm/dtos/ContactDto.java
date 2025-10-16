package ar.edu.utn.frc.tup.lc.iii.crm.dtos;

import ar.edu.utn.frc.tup.lc.iii.customfields.dtos.CustomFieldValueDto;
import jakarta.validation.constraints.Email;
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
public class ContactDto {
    private UUID id;
    private UUID tenantId;
    private String firstName;
    private String lastName;
    private String phone;

    @Email(message = "Email inválido")
    private String email;

    private String company;
    private String position;
    private String address;
    private String notes;
    private String extraData;
    private Map<String, CustomFieldValueDto> customFields; // Campos personalizados
    private Instant createdAt;
    private Instant updatedAt;
}
