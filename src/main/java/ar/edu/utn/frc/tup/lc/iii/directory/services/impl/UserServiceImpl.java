package ar.edu.utn.frc.tup.lc.iii.directory.services.impl;

import ar.edu.utn.frc.tup.lc.iii.directory.dtos.CreateUserRequest;
import ar.edu.utn.frc.tup.lc.iii.directory.dtos.RoleDto;
import ar.edu.utn.frc.tup.lc.iii.directory.dtos.UpdateUserRequest;
import ar.edu.utn.frc.tup.lc.iii.directory.dtos.UserDto;
import ar.edu.utn.frc.tup.lc.iii.directory.entities.Role;
import ar.edu.utn.frc.tup.lc.iii.directory.entities.User;
import ar.edu.utn.frc.tup.lc.iii.directory.entities.UserRole;
import ar.edu.utn.frc.tup.lc.iii.directory.repositories.RoleRepository;
import ar.edu.utn.frc.tup.lc.iii.directory.repositories.UserRepository;
import ar.edu.utn.frc.tup.lc.iii.directory.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public UserDto createUser(CreateUserRequest request, UUID tenantId) {
        // Validar que no exista un usuario con el mismo email
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + request.getEmail());
        }

        // Crear usuario global (sin tenant específico)
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .enabled(true)
                .isSuperAdmin(false)
                .userRoles(new HashSet<>())
                .build();

        // Asignar roles en el tenant especificado
        if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            for (UUID roleId : request.getRoleIds()) {
                Role role = roleRepository.findByIdAndTenantId(roleId, tenantId)
                        .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado: " + roleId));

                UserRole userRole = UserRole.builder()
                        .user(user)
                        .role(role)
                        .tenantId(tenantId)
                        .build();
                user.getUserRoles().add(userRole);
            }
        }

        User savedUser = userRepository.save(user);
        return mapToDto(savedUser, tenantId);
    }

    @Override
    @Transactional
    public UserDto updateUser(UUID id, UpdateUserRequest request, UUID tenantId) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        // Verificar que el usuario tenga acceso al tenant (o sea super admin)
        if (!user.isSuperAdmin() && !user.hasAccessToTenant(tenantId)) {
            throw new IllegalArgumentException("El usuario no tiene acceso a este tenant");
        }

        // Actualizar campos si están presentes
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            // Validar que el nuevo email no esté en uso
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new IllegalArgumentException("El email ya está en uso");
            }
            user.setEmail(request.getEmail());
        }

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getEnabled() != null) user.setEnabled(request.getEnabled());

        // Actualizar roles si están presentes (solo para este tenant)
        if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            // Limpiar roles existentes SOLO de este tenant
            user.getUserRoles().removeIf(ur -> ur.getTenantId().equals(tenantId));

            // Asignar nuevos roles en este tenant
            for (UUID roleId : request.getRoleIds()) {
                Role role = roleRepository.findByIdAndTenantId(roleId, tenantId)
                        .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado: " + roleId));

                UserRole userRole = UserRole.builder()
                        .user(user)
                        .role(role)
                        .tenantId(tenantId)
                        .build();
                user.getUserRoles().add(userRole);
            }
        }

        User updatedUser = userRepository.save(user);
        return mapToDto(updatedUser, tenantId);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(UUID id, UUID tenantId) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        // Verificar acceso
        if (!user.isSuperAdmin() && !user.hasAccessToTenant(tenantId)) {
            throw new EntityNotFoundException("Usuario no encontrado");
        }

        return mapToDto(user, tenantId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> getAllUsers(UUID tenantId, Pageable pageable) {
        // Obtener todos los usuarios y mapear a DTO
        // Nota: Idealmente esto debería filtrar con un query personalizado en el repository
        // Por ahora, mapeamos todos y mostramos solo los roles del tenant actual
        Page<User> users = userRepository.findAll(pageable);

        return users.map(user -> mapToDto(user, tenantId));
    }

    @Override
    @Transactional
    public void deleteUser(UUID id, UUID tenantId) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        // Verificar acceso
        if (!user.isSuperAdmin() && !user.hasAccessToTenant(tenantId)) {
            throw new EntityNotFoundException("Usuario no encontrado");
        }

        // Soft delete
        user.setEnabled(false);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void assignRole(UUID userId, UUID roleId, UUID tenantId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        Role role = roleRepository.findByIdAndTenantId(roleId, tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado"));

        // Verificar si el rol ya está asignado en este tenant
        boolean alreadyAssigned = user.getUserRoles().stream()
                .anyMatch(ur -> ur.getRole().getId().equals(roleId)
                        && ur.getTenantId().equals(tenantId));

        if (!alreadyAssigned) {
            UserRole userRole = UserRole.builder()
                    .user(user)
                    .role(role)
                    .tenantId(tenantId)
                    .build();
            user.getUserRoles().add(userRole);
            userRepository.save(user);
        }
    }

    @Override
    @Transactional
    public void removeRole(UUID userId, UUID roleId, UUID tenantId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        user.getUserRoles().removeIf(userRole ->
                userRole.getRole().getId().equals(roleId)
                && userRole.getTenantId().equals(tenantId));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void enableUser(UUID id, UUID tenantId) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        // Verificar acceso
        if (!user.isSuperAdmin() && !user.hasAccessToTenant(tenantId)) {
            throw new EntityNotFoundException("Usuario no encontrado");
        }

        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void disableUser(UUID id, UUID tenantId) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        // Verificar acceso
        if (!user.isSuperAdmin() && !user.hasAccessToTenant(tenantId)) {
            throw new EntityNotFoundException("Usuario no encontrado");
        }

        user.setEnabled(false);
        userRepository.save(user);
    }

    private UserDto mapToDto(User user, UUID tenantId) {
        UserDto dto = UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .enabled(user.isEnabled())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();

        // Mapear solo los roles del tenant actual
        if (user.getUserRoles() != null) {
            Set<RoleDto> roleDtos = user.getUserRoles().stream()
                    .filter(userRole -> userRole.getTenantId().equals(tenantId))
                    .map(userRole -> mapRoleToDto(userRole.getRole()))
                    .collect(Collectors.toSet());
            dto.setRoles(roleDtos);
        }

        return dto;
    }

    private RoleDto mapRoleToDto(Role role) {
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
