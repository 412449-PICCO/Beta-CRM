package ar.edu.utn.frc.tup.lc.iii.customfields.services.impl;

import ar.edu.utn.frc.tup.lc.iii.customfields.dtos.CustomFieldDto;
import ar.edu.utn.frc.tup.lc.iii.customfields.dtos.CustomFieldValueDto;
import ar.edu.utn.frc.tup.lc.iii.customfields.entities.CustomField;
import ar.edu.utn.frc.tup.lc.iii.customfields.entities.CustomFieldValue;
import ar.edu.utn.frc.tup.lc.iii.customfields.entities.EntityType;
import ar.edu.utn.frc.tup.lc.iii.customfields.entities.FieldType;
import ar.edu.utn.frc.tup.lc.iii.customfields.repositories.CustomFieldRepository;
import ar.edu.utn.frc.tup.lc.iii.customfields.repositories.CustomFieldValueRepository;
import ar.edu.utn.frc.tup.lc.iii.customfields.services.CustomFieldService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomFieldServiceImpl implements CustomFieldService {

    private final CustomFieldRepository customFieldRepository;
    private final CustomFieldValueRepository customFieldValueRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public CustomFieldDto createCustomField(CustomFieldDto customFieldDto, UUID tenantId) {
        // Validar que no exista un campo con la misma clave
        if (customFieldRepository.existsByFieldKeyAndTenantId(customFieldDto.getFieldKey(), tenantId)) {
            throw new IllegalArgumentException("Ya existe un campo con la clave: " + customFieldDto.getFieldKey());
        }

        CustomField customField = modelMapper.map(customFieldDto, CustomField.class);
        customField.setTenantId(tenantId);
        customField.setId(null);

        // Serializar opciones a JSON si es SELECT o MULTISELECT
        if ((customField.getFieldType() == FieldType.SELECT || customField.getFieldType() == FieldType.MULTISELECT)
                && customFieldDto.getOptions() != null) {
            try {
                customField.setOptions(objectMapper.writeValueAsString(customFieldDto.getOptions()));
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("Error al procesar las opciones del campo");
            }
        }

        CustomField savedField = customFieldRepository.save(customField);
        return mapToDto(savedField);
    }

    @Override
    @Transactional
    public CustomFieldDto updateCustomField(UUID id, CustomFieldDto customFieldDto, UUID tenantId) {
        CustomField customField = customFieldRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Campo personalizado no encontrado"));

        // Actualizar campos permitidos
        if (customFieldDto.getName() != null) customField.setName(customFieldDto.getName());
        if (customFieldDto.getDescription() != null) customField.setDescription(customFieldDto.getDescription());
        customField.setRequired(customFieldDto.isRequired());
        if (customFieldDto.getDefaultValue() != null) customField.setDefaultValue(customFieldDto.getDefaultValue());
        if (customFieldDto.getDisplayOrder() != null) customField.setDisplayOrder(customFieldDto.getDisplayOrder());
        customField.setActive(customFieldDto.isActive());

        // Actualizar opciones si aplica
        if ((customField.getFieldType() == FieldType.SELECT || customField.getFieldType() == FieldType.MULTISELECT)
                && customFieldDto.getOptions() != null) {
            try {
                customField.setOptions(objectMapper.writeValueAsString(customFieldDto.getOptions()));
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("Error al procesar las opciones del campo");
            }
        }

        CustomField updatedField = customFieldRepository.save(customField);
        return mapToDto(updatedField);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomFieldDto getCustomFieldById(UUID id, UUID tenantId) {
        CustomField customField = customFieldRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Campo personalizado no encontrado"));
        return mapToDto(customField);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomFieldDto> getAllCustomFields(UUID tenantId) {
        return customFieldRepository.findByTenantIdAndActiveTrue(tenantId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomFieldDto> getCustomFieldsByEntity(UUID tenantId, EntityType entityType) {
        return customFieldRepository.findByTenantIdAndEntityTypeAndActiveTrue(tenantId, entityType)
                .stream()
                .map(this::mapToDto)
                .sorted(Comparator.comparing(CustomFieldDto::getDisplayOrder))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCustomField(UUID id, UUID tenantId) {
        CustomField customField = customFieldRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Campo personalizado no encontrado"));

        // Soft delete
        customField.setActive(false);
        customFieldRepository.save(customField);

        // Opcionalmente eliminar los valores asociados
        // customFieldValueRepository.deleteByFieldId(id);
    }

    @Override
    @Transactional
    public void saveCustomFieldValues(UUID entityId, EntityType entityType, Map<String, String> values, UUID tenantId) {
        for (Map.Entry<String, String> entry : values.entrySet()) {
            String fieldKey = entry.getKey();
            String value = entry.getValue();

            // Buscar el campo personalizado
            CustomField customField = customFieldRepository.findByFieldKeyAndTenantId(fieldKey, tenantId)
                    .orElseThrow(() -> new EntityNotFoundException("Campo no encontrado: " + fieldKey));

            // Validar el valor según el tipo de campo
            validateValue(customField, value);

            // Buscar o crear el valor
            CustomFieldValue fieldValue = customFieldValueRepository
                    .findByFieldIdAndEntityId(customField.getId(), entityId)
                    .orElse(CustomFieldValue.builder()
                            .tenantId(tenantId)
                            .fieldId(customField.getId())
                            .entityId(entityId)
                            .entityType(entityType)
                            .build());

            fieldValue.setFieldValue(value);
            customFieldValueRepository.save(fieldValue);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, CustomFieldValueDto> getCustomFieldValues(UUID entityId, EntityType entityType, UUID tenantId) {
        List<CustomFieldValue> values = customFieldValueRepository.findByEntityIdAndEntityType(entityId, entityType);
        Map<String, CustomFieldValueDto> result = new HashMap<>();

        for (CustomFieldValue value : values) {
            CustomField field = customFieldRepository.findById(value.getFieldId()).orElse(null);
            if (field != null && field.getTenantId().equals(tenantId)) {
                CustomFieldValueDto dto = CustomFieldValueDto.builder()
                        .id(value.getId())
                        .fieldId(field.getId())
                        .fieldKey(field.getFieldKey())
                        .fieldName(field.getName())
                        .entityId(entityId)
                        .entityType(entityType)
                        .value(value.getFieldValue())
                        .build();
                result.put(field.getFieldKey(), dto);
            }
        }

        return result;
    }

    @Override
    @Transactional
    public void deleteCustomFieldValues(UUID entityId, EntityType entityType) {
        customFieldValueRepository.deleteByEntityIdAndEntityType(entityId, entityType);
    }

    private CustomFieldDto mapToDto(CustomField customField) {
        CustomFieldDto dto = modelMapper.map(customField, CustomFieldDto.class);

        // Deserializar opciones si existen
        if (customField.getOptions() != null && !customField.getOptions().isEmpty()) {
            try {
                List<String> options = objectMapper.readValue(
                        customField.getOptions(),
                        new TypeReference<List<String>>() {}
                );
                dto.setOptions(options);
            } catch (JsonProcessingException e) {
                dto.setOptions(new ArrayList<>());
            }
        }

        return dto;
    }

    private void validateValue(CustomField field, String value) {
        if (field.isRequired() && (value == null || value.trim().isEmpty())) {
            throw new IllegalArgumentException("El campo '" + field.getName() + "' es requerido");
        }

        if (value == null || value.isEmpty()) {
            return; // No validar valores vacíos si no son requeridos
        }

        switch (field.getFieldType()) {
            case NUMBER:
                try {
                    Long.parseLong(value);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("El campo '" + field.getName() + "' debe ser un número entero");
                }
                break;
            case DECIMAL:
            case CURRENCY:
                try {
                    Double.parseDouble(value);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("El campo '" + field.getName() + "' debe ser un número decimal");
                }
                break;
            case EMAIL:
                if (!value.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    throw new IllegalArgumentException("El campo '" + field.getName() + "' debe ser un email válido");
                }
                break;
            case URL:
                if (!value.matches("^(https?|ftp)://.*$")) {
                    throw new IllegalArgumentException("El campo '" + field.getName() + "' debe ser una URL válida");
                }
                break;
            case BOOLEAN:
                if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
                    throw new IllegalArgumentException("El campo '" + field.getName() + "' debe ser true o false");
                }
                break;
            case SELECT:
            case MULTISELECT:
                // Validar que el valor esté en las opciones
                try {
                    List<String> options = objectMapper.readValue(
                            field.getOptions(),
                            new TypeReference<List<String>>() {}
                    );
                    if (field.getFieldType() == FieldType.MULTISELECT) {
                        // Para multiselect, el valor puede ser una lista separada por comas
                        String[] selectedValues = value.split(",");
                        for (String selected : selectedValues) {
                            if (!options.contains(selected.trim())) {
                                throw new IllegalArgumentException(
                                        "El valor '" + selected + "' no es válido para el campo '" + field.getName() + "'"
                                );
                            }
                        }
                    } else {
                        if (!options.contains(value)) {
                            throw new IllegalArgumentException(
                                    "El valor '" + value + "' no es válido para el campo '" + field.getName() + "'"
                            );
                        }
                    }
                } catch (JsonProcessingException e) {
                    throw new IllegalArgumentException("Error al validar las opciones del campo");
                }
                break;
        }
    }
}
