package ar.edu.utn.frc.tup.lc.iii.directory.services;

import ar.edu.utn.frc.tup.lc.iii.directory.dtos.CreateUserRequest;
import ar.edu.utn.frc.tup.lc.iii.directory.dtos.UpdateUserRequest;
import ar.edu.utn.frc.tup.lc.iii.directory.dtos.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {
    UserDto createUser(CreateUserRequest request, UUID tenantId);
    UserDto updateUser(UUID id, UpdateUserRequest request, UUID tenantId);
    UserDto getUserById(UUID id, UUID tenantId);
    Page<UserDto> getAllUsers(UUID tenantId, Pageable pageable);
    void deleteUser(UUID id, UUID tenantId);
    void assignRole(UUID userId, UUID roleId, UUID tenantId);
    void removeRole(UUID userId, UUID roleId, UUID tenantId);
    void enableUser(UUID id, UUID tenantId);
    void disableUser(UUID id, UUID tenantId);
}

