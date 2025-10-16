package ar.edu.utn.frc.tup.lc.iii.directory.services.impl;

import ar.edu.utn.frc.tup.lc.iii.directory.dtos.RoleDto;
import ar.edu.utn.frc.tup.lc.iii.directory.entities.Role;
import ar.edu.utn.frc.tup.lc.iii.directory.repositories.RoleRepository;
import ar.edu.utn.frc.tup.lc.iii.directory.services.RoleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public RoleDto createRole(RoleDto roleDto, UUID tenantId) {
        // Validar que no exista un rol con el mismo código
        if (roleRepository.existsByCodeAndTenantId(roleDto.getCode(), tenantId)) {
            throw new IllegalArgumentException("Ya existe un rol con el código: " + roleDto.getCode());
        }

        Role role = modelMapper.map(roleDto, Role.class);
        role.setTenantId(tenantId);
        role.setId(null);
        role.setSystem(false); // Los roles creados por usuarios no son del sistema

        // Serializar permisos a JSON
        if (roleDto.getPermissions() != null) {
            try {
                role.setPermissions(objectMapper.writeValueAsString(roleDto.getPermissions()));
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("Error al procesar los permisos del rol");
            }
        }

        Role savedRole = roleRepository.save(role);
        return mapToDto(savedRole);
    }

    @Override
    @Transactional
    public RoleDto updateRole(UUID id, RoleDto roleDto, UUID tenantId) {
        Role role = roleRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado"));

        if (role.isSystem()) {
            throw new IllegalArgumentException("No se pueden modificar roles del sistema");
        }

        // Actualizar campos permitidos
        if (roleDto.getName() != null) role.setName(roleDto.getName());
        if (roleDto.getDescription() != null) role.setDescription(roleDto.getDescription());
        role.setActive(roleDto.isActive());

        // Actualizar permisos
        if (roleDto.getPermissions() != null) {
            try {
                role.setPermissions(objectMapper.writeValueAsString(roleDto.getPermissions()));
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("Error al procesar los permisos del rol");
            }
        }

        Role updatedRole = roleRepository.save(role);
        return mapToDto(updatedRole);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDto getRoleById(UUID id, UUID tenantId) {
        Role role = roleRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado"));
        return mapToDto(role);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDto> getAllRoles(UUID tenantId) {
        return roleRepository.findByTenantIdAndActiveTrue(tenantId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDto> getSystemRoles() {
        return roleRepository.findByIsSystemTrue()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteRole(UUID id, UUID tenantId) {
        Role role = roleRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado"));

        if (role.isSystem()) {
            throw new IllegalArgumentException("No se pueden eliminar roles del sistema");
        }

        // Soft delete
        role.setActive(false);
        roleRepository.save(role);
    }

    private RoleDto mapToDto(Role role) {
        RoleDto dto = modelMapper.map(role, RoleDto.class);

        // Deserializar permisos
        if (role.getPermissions() != null && !role.getPermissions().isEmpty()) {
            try {
                List<String> permissions = objectMapper.readValue(
                        role.getPermissions(),
                        new TypeReference<List<String>>() {}
                );
                dto.setPermissions(permissions);
            } catch (JsonProcessingException e) {
                dto.setPermissions(new ArrayList<>());
            }
        }

        return dto;
    }
}

