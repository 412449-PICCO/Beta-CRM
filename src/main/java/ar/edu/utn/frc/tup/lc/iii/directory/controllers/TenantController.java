package ar.edu.utn.frc.tup.lc.iii.directory.controllers;

import ar.edu.utn.frc.tup.lc.iii.common.ApiResponse;
import ar.edu.utn.frc.tup.lc.iii.directory.dtos.CreateTenantRequest;
import ar.edu.utn.frc.tup.lc.iii.directory.dtos.TenantDto;
import ar.edu.utn.frc.tup.lc.iii.directory.services.TenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
@Tag(name = "Tenants", description = "Gestión de CRMs/Tenants")
public class TenantController {

    private final TenantService tenantService;

    @PostMapping
    @PreAuthorize("hasAuthority('tenants.create') or hasAuthority('*')")
    @Operation(summary = "Crear nuevo CRM", description = "Crea un nuevo CRM (tenant) en el sistema. Solo super admins pueden crear nuevos CRMs.")
    public ResponseEntity<ApiResponse<TenantDto>> createTenant(
            @Valid @RequestBody CreateTenantRequest request) {

        TenantDto tenant = tenantService.createTenant(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.<TenantDto>builder()
                        .success(true)
                        .message("CRM creado exitosamente")
                        .data(tenant)
                        .build());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('tenants.read') or hasAuthority('*')")
    @Operation(summary = "Obtener CRM por ID", description = "Obtiene la información de un CRM específico")
    public ResponseEntity<ApiResponse<TenantDto>> getTenantById(
            @Parameter(description = "ID del CRM") @PathVariable UUID id) {

        TenantDto tenant = tenantService.getTenantById(id);

        return ResponseEntity.ok(ApiResponse.<TenantDto>builder()
                .success(true)
                .data(tenant)
                .build());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('tenants.read') or hasAuthority('*')")
    @Operation(summary = "Listar todos los CRMs", description = "Obtiene la lista de todos los CRMs con paginación")
    public ResponseEntity<ApiResponse<Page<TenantDto>>> getAllTenants(
            @Parameter(hidden = true) Pageable pageable) {

        Page<TenantDto> tenants = tenantService.getAllTenants(pageable);

        return ResponseEntity.ok(ApiResponse.<Page<TenantDto>>builder()
                .success(true)
                .data(tenants)
                .build());
    }

    @GetMapping("/active")
    @PreAuthorize("hasAuthority('tenants.read') or hasAuthority('*')")
    @Operation(summary = "Listar CRMs activos", description = "Obtiene la lista de todos los CRMs activos")
    public ResponseEntity<ApiResponse<List<TenantDto>>> getActiveTenants() {

        List<TenantDto> tenants = tenantService.getAllActiveTenants();

        return ResponseEntity.ok(ApiResponse.<List<TenantDto>>builder()
                .success(true)
                .data(tenants)
                .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('tenants.update') or hasAuthority('*')")
    @Operation(summary = "Actualizar CRM", description = "Actualiza la información de un CRM existente")
    public ResponseEntity<ApiResponse<TenantDto>> updateTenant(
            @Parameter(description = "ID del CRM") @PathVariable UUID id,
            @Valid @RequestBody CreateTenantRequest request) {

        TenantDto tenant = tenantService.updateTenant(id, request);

        return ResponseEntity.ok(ApiResponse.<TenantDto>builder()
                .success(true)
                .message("CRM actualizado exitosamente")
                .data(tenant)
                .build());
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('tenants.delete') or hasAuthority('*')")
    @Operation(summary = "Desactivar CRM", description = "Desactiva un CRM (soft delete)")
    public ResponseEntity<ApiResponse<Void>> deactivateTenant(
            @Parameter(description = "ID del CRM") @PathVariable UUID id) {

        tenantService.deactivateTenant(id);

        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("CRM desactivado exitosamente")
                .build());
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('tenants.update') or hasAuthority('*')")
    @Operation(summary = "Activar CRM", description = "Activa un CRM previamente desactivado")
    public ResponseEntity<ApiResponse<Void>> activateTenant(
            @Parameter(description = "ID del CRM") @PathVariable UUID id) {

        tenantService.activateTenant(id);

        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("CRM activado exitosamente")
                .build());
    }
}

