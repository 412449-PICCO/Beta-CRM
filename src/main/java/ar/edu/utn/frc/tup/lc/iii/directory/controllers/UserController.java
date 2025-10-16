package ar.edu.utn.frc.tup.lc.iii.directory.controllers;

import ar.edu.utn.frc.tup.lc.iii.common.ApiResponse;
import ar.edu.utn.frc.tup.lc.iii.directory.dtos.CreateUserRequest;
import ar.edu.utn.frc.tup.lc.iii.directory.dtos.UpdateUserRequest;
import ar.edu.utn.frc.tup.lc.iii.directory.dtos.UserDto;
import ar.edu.utn.frc.tup.lc.iii.directory.services.UserService;
import ar.edu.utn.frc.tup.lc.iii.multitenancy.TenantContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Gestión de usuarios")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    @Operation(summary = "Listar todos los usuarios del tenant (paginado)")
    public ResponseEntity<ApiResponse<Page<UserDto>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir
    ) {
        UUID tenantId = UUID.fromString(TenantContext.getTenantId());
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<UserDto> users = userService.getAllUsers(tenantId, pageable);
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    @Operation(summary = "Obtener un usuario por ID")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable UUID id) {
        UUID tenantId = UUID.fromString(TenantContext.getTenantId());
        UserDto user = userService.getUserById(id, tenantId);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    @Operation(summary = "Crear un nuevo usuario")
    public ResponseEntity<ApiResponse<UserDto>> createUser(@Valid @RequestBody CreateUserRequest request) {
        UUID tenantId = UUID.fromString(TenantContext.getTenantId());
        UserDto createdUser = userService.createUser(request, tenantId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Usuario creado exitosamente", createdUser));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    @Operation(summary = "Actualizar un usuario")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserRequest request
    ) {
        UUID tenantId = UUID.fromString(TenantContext.getTenantId());
        UserDto updatedUser = userService.updateUser(id, request, tenantId);
        return ResponseEntity.ok(ApiResponse.success("Usuario actualizado exitosamente", updatedUser));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar un usuario (solo ADMIN, deshabilita el usuario)")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID id) {
        UUID tenantId = UUID.fromString(TenantContext.getTenantId());
        userService.deleteUser(id, tenantId);
        return ResponseEntity.ok(ApiResponse.success("Usuario eliminado exitosamente", null));
    }

    @PostMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    @Operation(summary = "Asignar un rol a un usuario")
    public ResponseEntity<ApiResponse<Void>> assignRole(
            @PathVariable UUID userId,
            @PathVariable UUID roleId
    ) {
        UUID tenantId = UUID.fromString(TenantContext.getTenantId());
        userService.assignRole(userId, roleId, tenantId);
        return ResponseEntity.ok(ApiResponse.success("Rol asignado exitosamente", null));
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    @Operation(summary = "Remover un rol de un usuario")
    public ResponseEntity<ApiResponse<Void>> removeRole(
            @PathVariable UUID userId,
            @PathVariable UUID roleId
    ) {
        UUID tenantId = UUID.fromString(TenantContext.getTenantId());
        userService.removeRole(userId, roleId, tenantId);
        return ResponseEntity.ok(ApiResponse.success("Rol removido exitosamente", null));
    }

    @PatchMapping("/{id}/enable")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Habilitar un usuario")
    public ResponseEntity<ApiResponse<Void>> enableUser(@PathVariable UUID id) {
        UUID tenantId = UUID.fromString(TenantContext.getTenantId());
        userService.enableUser(id, tenantId);
        return ResponseEntity.ok(ApiResponse.success("Usuario habilitado exitosamente", null));
    }

    @PatchMapping("/{id}/disable")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deshabilitar un usuario")
    public ResponseEntity<ApiResponse<Void>> disableUser(@PathVariable UUID id) {
        UUID tenantId = UUID.fromString(TenantContext.getTenantId());
        userService.disableUser(id, tenantId);
        return ResponseEntity.ok(ApiResponse.success("Usuario deshabilitado exitosamente", null));
    }
}

