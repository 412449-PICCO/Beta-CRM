package ar.edu.utn.frc.tup.lc.iii.customfields.dtos;

import ar.edu.utn.frc.tup.lc.iii.customfields.entities.EntityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomFieldValueDto {
    private UUID id;
    private UUID fieldId;
    private String fieldKey;
    private String fieldName;
    private UUID entityId;
    private EntityType entityType;
    private String value;
}

