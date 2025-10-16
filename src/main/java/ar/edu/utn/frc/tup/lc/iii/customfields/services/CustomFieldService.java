package ar.edu.utn.frc.tup.lc.iii.customfields.services;

import ar.edu.utn.frc.tup.lc.iii.customfields.dtos.CustomFieldDto;
import ar.edu.utn.frc.tup.lc.iii.customfields.dtos.CustomFieldValueDto;
import ar.edu.utn.frc.tup.lc.iii.customfields.entities.EntityType;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CustomFieldService {
    // Gestión de Custom Fields
    CustomFieldDto createCustomField(CustomFieldDto customFieldDto, UUID tenantId);
    CustomFieldDto updateCustomField(UUID id, CustomFieldDto customFieldDto, UUID tenantId);
    CustomFieldDto getCustomFieldById(UUID id, UUID tenantId);
    List<CustomFieldDto> getAllCustomFields(UUID tenantId);
    List<CustomFieldDto> getCustomFieldsByEntity(UUID tenantId, EntityType entityType);
    void deleteCustomField(UUID id, UUID tenantId);

    // Gestión de valores
    void saveCustomFieldValues(UUID entityId, EntityType entityType, Map<String, String> values, UUID tenantId);
    Map<String, CustomFieldValueDto> getCustomFieldValues(UUID entityId, EntityType entityType, UUID tenantId);
    void deleteCustomFieldValues(UUID entityId, EntityType entityType);
}

