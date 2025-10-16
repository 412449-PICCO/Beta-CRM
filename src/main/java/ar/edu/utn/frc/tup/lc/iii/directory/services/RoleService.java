package ar.edu.utn.frc.tup.lc.iii.directory.services;

import ar.edu.utn.frc.tup.lc.iii.directory.dtos.RoleDto;

import java.util.List;
import java.util.UUID;

public interface RoleService {
    RoleDto createRole(RoleDto roleDto, UUID tenantId);
    RoleDto updateRole(UUID id, RoleDto roleDto, UUID tenantId);
    RoleDto getRoleById(UUID id, UUID tenantId);
    List<RoleDto> getAllRoles(UUID tenantId);
    void deleteRole(UUID id, UUID tenantId);
    List<RoleDto> getSystemRoles();
}

