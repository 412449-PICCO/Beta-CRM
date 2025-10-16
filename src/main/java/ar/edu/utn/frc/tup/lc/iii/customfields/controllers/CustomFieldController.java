package ar.edu.utn.frc.tup.lc.iii.customfields.controllers;

import ar.edu.utn.frc.tup.lc.iii.common.ApiResponse;
import ar.edu.utn.frc.tup.lc.iii.customfields.dtos.CustomFieldDto;
import ar.edu.utn.frc.tup.lc.iii.customfields.dtos.CustomFieldValueDto;
import ar.edu.utn.frc.tup.lc.iii.customfields.entities.EntityType;
import ar.edu.utn.frc.tup.lc.iii.customfields.services.CustomFieldService;
import ar.edu.utn.frc.tup.lc.iii.multitenancy.TenantContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/custom-fields")
@RequiredArgsConstructor
@Tag(name = "Custom Fields", description = "Gestión de campos personalizados dinámicos")
public class CustomFieldController {

    private final CustomFieldService customFieldService;

    @GetMapping
    @Operation(summary = "Listar todos los campos personalizados del tenant")
    public ResponseEntity<ApiResponse<List<CustomFieldDto>>> getAllCustomFields() {
        UUID tenantId = UUID.fromString(TenantContext.getTenantId());
        List<CustomFieldDto> fields = customFieldService.getAllCustomFields(tenantId);
        return ResponseEntity.ok(ApiResponse.success(fields));
    }

    @GetMapping("/entity/{entityType}")
    @Operation(summary = "Listar campos personalizados por tipo de entidad")
    public ResponseEntity<ApiResponse<List<CustomFieldDto>>> getCustomFieldsByEntity(
            @PathVariable EntityType entityType
    ) {
        UUID tenantId = UUID.fromString(TenantContext.getTenantId());
        List<CustomFieldDto> fields = customFieldService.getCustomFieldsByEntity(tenantId, entityType);
        return ResponseEntity.ok(ApiResponse.success(fields));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un campo personalizado por ID")
    public ResponseEntity<ApiResponse<CustomFieldDto>> getCustomFieldById(@PathVariable UUID id) {
        UUID tenantId = UUID.fromString(TenantContext.getTenantId());
        CustomFieldDto field = customFieldService.getCustomFieldById(id, tenantId);
        return ResponseEntity.ok(ApiResponse.success(field));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    @Operation(summary = "Crear un nuevo campo personalizado")
    public ResponseEntity<ApiResponse<CustomFieldDto>> createCustomField(
            @Valid @RequestBody CustomFieldDto customFieldDto
    ) {
        UUID tenantId = UUID.fromString(TenantContext.getTenantId());
        CustomFieldDto createdField = customFieldService.createCustomField(customFieldDto, tenantId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Campo personalizado creado exitosamente", createdField));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    @Operation(summary = "Actualizar un campo personalizado")
    public ResponseEntity<ApiResponse<CustomFieldDto>> updateCustomField(
            @PathVariable UUID id,
            @Valid @RequestBody CustomFieldDto customFieldDto
    ) {
        UUID tenantId = UUID.fromString(TenantContext.getTenantId());
        CustomFieldDto updatedField = customFieldService.updateCustomField(id, customFieldDto, tenantId);
        return ResponseEntity.ok(ApiResponse.success("Campo personalizado actualizado exitosamente", updatedField));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar un campo personalizado (soft delete)")
    public ResponseEntity<ApiResponse<Void>> deleteCustomField(@PathVariable UUID id) {
        UUID tenantId = UUID.fromString(TenantContext.getTenantId());
        customFieldService.deleteCustomField(id, tenantId);
        return ResponseEntity.ok(ApiResponse.success("Campo personalizado eliminado exitosamente", null));
    }

    @PostMapping("/values/{entityType}/{entityId}")
    @Operation(summary = "Guardar valores de campos personalizados para una entidad")
    public ResponseEntity<ApiResponse<Void>> saveCustomFieldValues(
            @PathVariable EntityType entityType,
            @PathVariable UUID entityId,
            @RequestBody Map<String, String> values
    ) {
        UUID tenantId = UUID.fromString(TenantContext.getTenantId());
        customFieldService.saveCustomFieldValues(entityId, entityType, values, tenantId);
        return ResponseEntity.ok(ApiResponse.success("Valores guardados exitosamente", null));
    }

    @GetMapping("/values/{entityType}/{entityId}")
    @Operation(summary = "Obtener valores de campos personalizados de una entidad")
    public ResponseEntity<ApiResponse<Map<String, CustomFieldValueDto>>> getCustomFieldValues(
            @PathVariable EntityType entityType,
            @PathVariable UUID entityId
    ) {
        UUID tenantId = UUID.fromString(TenantContext.getTenantId());
        Map<String, CustomFieldValueDto> values = customFieldService.getCustomFieldValues(entityId, entityType, tenantId);
        return ResponseEntity.ok(ApiResponse.success(values));
    }
}

