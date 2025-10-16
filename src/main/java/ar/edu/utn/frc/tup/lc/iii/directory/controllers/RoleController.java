package ar.edu.utn.frc.tup.lc.iii.directory.controllers;

import ar.edu.utn.frc.tup.lc.iii.common.ApiResponse;
import ar.edu.utn.frc.tup.lc.iii.directory.dtos.RoleDto;
import ar.edu.utn.frc.tup.lc.iii.directory.services.RoleService;
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
import java.util.UUID;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Tag(name = "Roles", description = "Gestión de roles y permisos")
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    @Operation(summary = "Listar todos los roles del tenant")
    public ResponseEntity<ApiResponse<List<RoleDto>>> getAllRoles() {
        UUID tenantId = UUID.fromString(TenantContext.getTenantId());
        List<RoleDto> roles = roleService.getAllRoles(tenantId);
        return ResponseEntity.ok(ApiResponse.success(roles));
    }

    @GetMapping("/system")
    @Operation(summary = "Listar roles del sistema (predefinidos)")
    public ResponseEntity<ApiResponse<List<RoleDto>>> getSystemRoles() {
        List<RoleDto> roles = roleService.getSystemRoles();
        return ResponseEntity.ok(ApiResponse.success(roles));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un rol por ID")
    public ResponseEntity<ApiResponse<RoleDto>> getRoleById(@PathVariable UUID id) {
        UUID tenantId = UUID.fromString(TenantContext.getTenantId());
        RoleDto role = roleService.getRoleById(id, tenantId);
        return ResponseEntity.ok(ApiResponse.success(role));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear un nuevo rol (solo ADMIN)")
    public ResponseEntity<ApiResponse<RoleDto>> createRole(@Valid @RequestBody RoleDto roleDto) {
        UUID tenantId = UUID.fromString(TenantContext.getTenantId());
        RoleDto createdRole = roleService.createRole(roleDto, tenantId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Rol creado exitosamente", createdRole));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar un rol (solo ADMIN)")
    public ResponseEntity<ApiResponse<RoleDto>> updateRole(
            @PathVariable UUID id,
            @Valid @RequestBody RoleDto roleDto
    ) {
        UUID tenantId = UUID.fromString(TenantContext.getTenantId());
        RoleDto updatedRole = roleService.updateRole(id, roleDto, tenantId);
        return ResponseEntity.ok(ApiResponse.success("Rol actualizado exitosamente", updatedRole));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar un rol (solo ADMIN, soft delete)")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable UUID id) {
        UUID tenantId = UUID.fromString(TenantContext.getTenantId());
        roleService.deleteRole(id, tenantId);
        return ResponseEntity.ok(ApiResponse.success("Rol eliminado exitosamente", null));
    }
}

